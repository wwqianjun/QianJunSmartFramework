package com.qianjun.test;

import com.alibaba.fastjson.JSON;
import com.qianjun.model.TCustomer;
import com.qianjun.service.CustomerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ZiJun
 * Description: CustomerService 单元测试
 * Date: 2015/11/18 :14:48.
 */
public class CustomerServiceTest {

    private static final CustomerService customerService = new CustomerService();





    @Before
    public void init(){
        // TODO 初始化数据库
    }

    @Test
    public void getCustomerListTest(){
        List<TCustomer> customerList = customerService.getCustomerList(null);
        Assert.assertEquals(3, customerList.size());
    }

    @Test
    public void getCustomerTest(){
        TCustomer customer= customerService.getCustomer(1);
        System.out.println(JSON.toJSONString(customer));
        Assert.assertEquals("13512345678", customer.getTelephone());
    }

    @Test
    public void getCustomerListMapTest(){
        System.out.println(JSON.toJSONString(customerService.getCustomerListMap(2)));
    }

    @Test
    public void insertEntityTest(){
        Map<String, Object> fieldMap = new HashMap<>();
        fieldMap.put("name","zijun");
        fieldMap.put("contact","zijun");
        fieldMap.put("telephone","16312345678");
        fieldMap.put("email","zijun@163.com");
        fieldMap.put("remark","VIP");

        Assert.assertTrue(customerService.createCustomer(fieldMap));
    }
}
