package com.zzf.medical.service.serviceImpl;

import com.zzf.medical.common.Const;
import com.zzf.medical.dao.CategoryMapper;
import com.zzf.medical.pojo.Category;
import com.zzf.medical.service.ICategoryService;
import com.zzf.medical.vo.CategoryVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;
    /**
     * 耗时：http(请求微信api) >磁盘 > 内存
     * mysql（内网+磁盘）
     * java对象
     * for循环不应该写http和SQL
     * ***/
    @Override
    public List<CategoryVo> selectAll() {
        List<Category> categories = categoryMapper.selectAll();
//        for (Category category : categories){
//            if (category.getParentId().equals(Const.ROOT_PARENT_ID)){
//                CategoryVo categoryVo = new CategoryVo();
//                BeanUtils.copyProperties(category,categoryVo);
//                categoryVoList.add(categoryVo);
//            }
//        }
        //lambda + stream
        List<CategoryVo> categoryVoList = categories.stream()
                .filter(e -> e.getParentId().equals(Const.ROOT_PARENT_ID))
                .map(this::category2CategoryVo)
                .sorted(Comparator.comparing(CategoryVo::getSortOrder).reversed())//排序
                .collect(Collectors.toList());
        findSubCategory(categoryVoList,categories);
        return categoryVoList;
    }

    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        findSubCategoryId(id,resultSet,categories);
    }

    private void findSubCategoryId(Integer id, Set<Integer> resultSet,List<Category> categories) {
        for (Category category :categories){
            if (category.getParentId().equals(id)){
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(),resultSet,categories);
            }
        }
    }

    private void findSubCategory(List<CategoryVo> categoryVoList,List<Category> categories){
        for (CategoryVo categoryVo : categoryVoList){
            List<CategoryVo> subCategoryVoList = new ArrayList<>();
            for (Category category : categories){
                //如果查到内容，设置subCategory,继续往下查询
                if (categoryVo.getId().equals(category.getParentId())){
                    subCategoryVoList.add(category2CategoryVo(category));
                }
                //排序
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getSortOrder).reversed());

                categoryVo.setSubCategories(subCategoryVoList);
                //一直递归，多级目录的显示
                findSubCategory(subCategoryVoList,categories);
            }
        }
    }
    private CategoryVo category2CategoryVo(Category category){
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
