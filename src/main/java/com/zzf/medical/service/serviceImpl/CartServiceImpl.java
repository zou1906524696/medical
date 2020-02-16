package com.zzf.medical.service.serviceImpl;

import com.google.gson.Gson;
import com.zzf.medical.dao.ProductMapper;
import com.zzf.medical.enums.EmCommonError;
import com.zzf.medical.enums.ProductStatusEnum;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.Cart;
import com.zzf.medical.pojo.Product;
import com.zzf.medical.request.CartAddReq;
import com.zzf.medical.request.CartUpdateReq;
import com.zzf.medical.service.ICartService;
import com.zzf.medical.vo.CartProductVo;
import com.zzf.medical.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class CartServiceImpl implements ICartService {
    private final static String CART_REDIS_KEY_TEMPLATE = "cart_%d";

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private Gson gson = new Gson();

    @Override
    public CartVo add(Integer uid,CartAddReq cartAddReq) throws BusinessException {
        Integer quantity = 1;
        Product product = productMapper.selectByPrimaryKey(cartAddReq.getProductId());
        //判断商品是否存在
        if (product == null){
            throw new BusinessException(EmCommonError.PRODUCT_NOT_EXIST);
        }
        //商品是否正常在售
        if(!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())){
            throw new BusinessException(EmCommonError.PRODUCT_OFF_SALE_OR_DELETE);
        }
        //商品库存是否充足
        if(product.getStock() <= 0){
            throw new BusinessException(EmCommonError.PRODUCT_STOCK_ERROR);
        }
        //写入到redis里面
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Cart cart;
        String value = opsForHash.get(redisKey,String.valueOf(product.getId()));
        if(StringUtils.isEmpty(value)){
            //没有该商品新增
            cart = new Cart(product.getId(),quantity,cartAddReq.getSelected());
        }else {
            //已经有数量加+1
            cart = gson.fromJson(value,Cart.class);
            cart.setQuantity(cart.getQuantity() + quantity);
        }

        opsForHash.put(redisKey,
                String.valueOf(product.getId()),
                gson.toJson(cart));
        return list(uid);
    }

    @Override
    public CartVo list(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);

        Map<String,String> entries = opsForHash.entries(redisKey);

        boolean selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;

        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList= new ArrayList<>();

        Set<String> cartIdSet = entries.keySet();
        List<Product> products = productMapper.selectByCartIdSet(cartIdSet);
        //for (Product product : products){
        for (Map.Entry<String,String> entry : entries.entrySet()){
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(),Cart.class);
            //TODO,需要优化，使用mysql 的in
            //Product product = productMapper.selectByPrimaryKey(cart.getProductId());
            for (Product product : products) {
                if (product != null && product.getId().equals(cart.getProductId())) {
                    CartProductVo cartProductVo = new CartProductVo(productId,
                            cart.getQuantity(),
                            product.getName(),
                            product.getSubtitle(),
                            product.getMainImage(),
                            product.getPrice(),
                            product.getStatus(),
                            product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                            product.getStock(),
                            cart.getProductSelected()
                    );
                    cartProductVoList.add(cartProductVo);
                    if (!cart.getProductSelected()) {
                        selectAll = false;
                    }
                    if (cart.getProductSelected()) {
                        cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                    }
                }
            }

            cartTotalQuantity += cart.getQuantity();
        }
        cartVo.setSelectAll(selectAll);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartProductVoList(cartProductVoList);
        return cartVo;
    }

    @Override
    public CartVo update(Integer uid, Integer productId, CartUpdateReq cartUpdateReq) throws BusinessException {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        String value = opsForHash.get(redisKey,String.valueOf(productId));
        if(StringUtils.isEmpty(value)){
            //没有该商品新增
            throw new BusinessException(EmCommonError.CART_PRODUCT_NOT_EXIST);
        }
        Cart cart = gson.fromJson(value,Cart.class);
        if(cartUpdateReq.getQuantity() != null
                && cartUpdateReq.getQuantity() >=0){
            cart.setQuantity(cartUpdateReq.getQuantity());
        }
        if (cartUpdateReq.getSelected() !=null){
            cart.setProductSelected(cartUpdateReq.getSelected());
        }
        opsForHash.put(redisKey,String.valueOf(productId),gson.toJson(cart));
        return list(uid);
    }

    @Override
    public CartVo delete(Integer uid, Integer productId) throws BusinessException {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        String value = opsForHash.get(redisKey,String.valueOf(productId));
        if(StringUtils.isEmpty(value)){
            //没有该商品新增
            throw new BusinessException(EmCommonError.CART_PRODUCT_NOT_EXIST);
        }
        opsForHash.delete(redisKey,String.valueOf(productId));
        return list(uid);
    }

    @Override
    public CartVo selectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        for (Cart cart :listForCart(uid)){
            cart.setProductSelected(true);
            opsForHash.put(redisKey,String.valueOf(cart.getProductId()),gson.toJson(cart));
        }
        return list(uid);
    }

    @Override
    public CartVo unSelectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        for (Cart cart :listForCart(uid)){
            cart.setProductSelected(false);
            opsForHash.put(redisKey,String.valueOf(cart.getProductId()),gson.toJson(cart));
        }
        return list(uid);
    }

    @Override
    public int sum(Integer uid) {
        Integer sum = listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0,Integer::sum);
        return sum;
    }

    public List<Cart> listForCart(Integer uid){
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE,uid);
        Map<String,String> entries = opsForHash.entries(redisKey);

        List<Cart> cartList = new ArrayList<>();
        for (Map.Entry<String,String> entry : entries.entrySet()){
            cartList.add(gson.fromJson(entry.getValue(),Cart.class));
        }
        return cartList;
    }
}
