package com.zzf.medical.service.serviceImpl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zzf.medical.service.ICartService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CartServiceImplTest {

    @Autowired
    private ICartService cartService;

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void add() {
    }

    @Test
    public void list() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void selectAll() {

    }

    @Test
    public void unSelectAll() {
    }

    @Test
    public void sum() {
    }
}