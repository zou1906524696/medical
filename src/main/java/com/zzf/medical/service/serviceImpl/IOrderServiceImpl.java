package com.zzf.medical.service.serviceImpl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zzf.medical.dao.OrderItemMapper;
import com.zzf.medical.dao.OrderMapper;
import com.zzf.medical.dao.ProductMapper;
import com.zzf.medical.dao.ShippingMapper;
import com.zzf.medical.enums.EmCommonError;
import com.zzf.medical.enums.OrderStatusEnum;
import com.zzf.medical.enums.PaymentTypeEnum;
import com.zzf.medical.enums.ProductStatusEnum;
import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.pojo.*;
import com.zzf.medical.service.ICartService;
import com.zzf.medical.service.IOrderService;
import com.zzf.medical.vo.OrderItemVo;
import com.zzf.medical.vo.OrderVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class IOrderServiceImpl implements IOrderService {
    @Autowired
    private ShippingMapper shippingMapper;
    @Autowired
    private ICartService cartService;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private OrderItemMapper orderItemMapper;
    @Autowired
    private OrderMapper orderMapper;
    @Override
    @Transactional
    public OrderVo create(Integer uid, Integer shippingId) throws BusinessException {
        //1.收货地址校验(总之需要查出来)
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);
        if (shipping == null){
            throw new BusinessException(EmCommonError.SHIPPING_ADDRESS_NOT_EXIST);
        }
        //2.下单的商品需要通过UID去查询购物车，校验(是否有商品、库存)
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());
        if (CollectionUtils.isEmpty(cartList)){
            throw new BusinessException(EmCommonError.CART_SELECTED_IS_EMPTY);
        }
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId)
                .collect(Collectors.toSet());
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        Map<Integer,Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId,product -> product ));

        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();
        for (Cart cart : cartList) {
            //根据productId查询数据库
            Product product = map.get(cart.getProductId());
            if ( product == null ){
                throw new BusinessException(EmCommonError.PRODUCT_NOT_EXIST, "商品不存在，productId" + cart.getProductId());
            }
            if ( !ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus()) ){
                throw new BusinessException(EmCommonError.PRODUCT_OFF_SALE_OR_DELETE,"商品不是在售状态" + product.getName() );
            }
            if ( product.getStock() < cart.getQuantity()){
                throw new BusinessException(EmCommonError.PRODUCT_STOCK_ERROR, "库存不正确" + product.getName());
            }
            OrderItem orderItem = buildOrderItem(uid, orderNo, product, cart.getQuantity());
            orderItemList.add(orderItem);
            //5.减库存
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row <= 0){
                throw new BusinessException(EmCommonError.ORDER_INSERT_ERROR);
            }
        }
        //TODO 加入到优惠券
        //3.计算总价，只计算选中的商品

        //4.生成订单，入库：Order和OrderItem，事务
        Order order = buildOrder(orderNo, uid, shippingId, orderItemList);

        int row = orderMapper.insertSelective(order);
        if (row <= 0){
            throw new BusinessException(EmCommonError.ORDER_INSERT_ERROR);
        }
        row = orderItemMapper.batchInsert(orderItemList);
        if (row <= 0){
            throw new BusinessException(EmCommonError.ORDER_INSERT_ERROR);
        }
        //更新购物车，删除选中的商品
        //Redis事务，不能回滚
        for (Cart cart : cartList) {
            cartService.delete(uid,cart.getProductId());
        }
        //返回给前端构造orderVo对象

        return buildOrderVo(order,orderItemList,shipping);
    }

    @Override
    public PageInfo list(Integer uid, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<Order> orderList = orderMapper.selectByUid(uid);
        Set<Long> orderNoSet = orderList.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        //希望map,通过orderNo分类
        Map<Long,List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));

        Set<Integer> shippingIdSet = orderList.stream()
                .map(Order::getShippingId)
                .collect(Collectors.toSet());
        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingIdSet);
        Map<Integer,Shipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(Shipping::getId,shipping -> shipping));

        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order,
                    orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }
        PageInfo pageInfo = new PageInfo(orderList);
        pageInfo.setList(orderVoList);
        return pageInfo;
    }

    @Override
    public OrderVo detail(Integer uid, Long orderNo) throws BusinessException {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)){
            throw new BusinessException(EmCommonError.ORDER_NOT_EXIST);
        }
        Set<Long> orderNoSet = new HashSet<>();
        orderNoSet.add(order.getOrderNo());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);

        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());

        OrderVo orderVo = buildOrderVo(order,orderItemList,shipping);
        return orderVo;
    }

    @Override
    public void cancel(Integer uid, Long orderNo) throws BusinessException {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)){
            throw new BusinessException(EmCommonError.ORDER_NOT_EXIST);
        }
        //只有未付款订单才可以取消
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())){
            throw new BusinessException(EmCommonError.ORDER_STATUS_ERROR);
        }
        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());

        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0){
            throw new BusinessException(EmCommonError.SERVICE_ERROR);
        }
    }

    @Override
    public void paid(Long orderNo) throws BusinessException {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null){
            throw new BusinessException(EmCommonError.ORDER_NOT_EXIST,"订单ID" + orderNo);
        }
        //只有未付款订单才可以取消
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode())){
            throw new BusinessException(EmCommonError.ORDER_STATUS_ERROR, "订单ID" + orderNo);
        }
        order.setStatus(OrderStatusEnum.PAID.getCode());
        //TODO 时间需要修改
        order.setPaymentTime(new Date());

        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0){
            throw new BusinessException(EmCommonError.SERVICE_ERROR,"订单ID" + orderNo);
        }
    }

    private Long generateOrderNo(){
        return System.currentTimeMillis() + new Random().nextInt(999);
    }

    private OrderItem buildOrderItem(Integer uid, Long orderNo,Product product,Integer quantity){
        OrderItem orderItem = new OrderItem();
        orderItem.setUserId(uid);
        orderItem.setOrderNo(orderNo);
        orderItem.setProductId(product.getId());
        orderItem.setProductName(product.getName());
        orderItem.setProductImage(product.getMainImage());
        orderItem.setCurrentUnitPrice(product.getPrice());
        orderItem.setQuantity(quantity);
        orderItem.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));

        orderItem.setCreateTime(new Date());
        orderItem.setUpdateTime(new Date());
        return orderItem;
    }
    private Order buildOrder(Long orderNo,
                             Integer uid,
                             Integer shippingId,
                             List<OrderItem> orderItemList){
        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(orderItemList.stream().map(OrderItem::getTotalPrice).reduce(BigDecimal.ZERO,BigDecimal::add));
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        return order;
    }
    private OrderVo buildOrderVo(Order order,
                                 List<OrderItem> orderItemList,
                                 Shipping shipping){
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order,orderVo);
        List<OrderItemVo> orderItemVoList = orderItemList.stream().map(e ->{
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e,orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());

        orderVo.setOrderItemList(orderItemVoList);
        if (shipping != null){
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }
        return orderVo;
    }
}
