package com.zzf.medical.service.serviceImpl;

import com.zzf.medical.exception.BusinessException;
import com.zzf.medical.service.IProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceImplTest {

    @Autowired
    private IProductService productService;
    @Test
    public void list() {
        productService.list(null,1,1);
    }
    @Test
    public void detail() throws BusinessException {
        productService.detail(27);
    }
}