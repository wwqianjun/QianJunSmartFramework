package com.qianjun.service;

import com.qianjun.helper.DatabaseHelper;
import com.qianjun.model.TCustomer;

import java.util.List;
import java.util.Map;

/**
 * Created by ZiJun
 * Description: 提供客户数据服务
 * Date: 2015/11/18 :14:35.
 */
public class CustomerService {

    /**
     * 获取客户列表
     * @param keyWord
     * @return
     */
    public List<TCustomer> getCustomerList(String keyWord){
        String sql = "SELECT * FROM TCustomer";
        return DatabaseHelper.queryEntityList(TCustomer.class, sql);
    }

    /**
     * 获取客户
     * @param id
     * @return
     */
    public TCustomer getCustomer(long id){
        String sql = "SELECT * FROM TCustomer where id=?" ;
        return DatabaseHelper.queryEntity(TCustomer.class, sql,id);
    }

    /**
     * 获取客户
     * @param id
     * @return
     */
    public List<Map<String, Object>> getCustomerListMap(long id){
        String sql = "SELECT * FROM TCustomer where id=?" ;
        return DatabaseHelper.executeQuery(sql, id);
    }

    /**
     * 创建客户
     * @param fieldMap
     * @return
     */
    public boolean  createCustomer(Map<String, Object> fieldMap){
        return DatabaseHelper.insertEntity(TCustomer.class, fieldMap);
    }

    /**
     * 更新客户
     * @param id
     * @param filedMap
     * @return
     */
    public  boolean updateCustomer(long id, Map<String,Object> filedMap){
        // TODO
        return false;
    }

    /**
     * 删除客户
     * @param id
     * @return
     */
    public boolean deleteCustomer(long id){
        // TODO
        return DatabaseHelper.deleteEntity(TCustomer.class,id);
    }
}
