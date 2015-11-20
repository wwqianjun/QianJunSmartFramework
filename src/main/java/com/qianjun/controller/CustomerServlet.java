package com.qianjun.controller;

import com.qianjun.model.TCustomer;
import com.qianjun.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by ZiJun
 * Description:进入客户列表界面
 * Date: 2015/11/18 :14:25.
 */
@WebServlet("/customer")
public class CustomerServlet extends HttpServlet {

    private static final long serialVersionUID = 6150851829186946739L;

    private CustomerService customerService;

    @Override
    public void init() throws ServletException {
        super.init();
        customerService = new CustomerService();
    }

    /**
     * 处理 创建客户的请求
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    /**
     * 处理 创建客户的界面
     * @param request
     * @param response
     * @throws javax.servlet.ServletException
     * @throws java.io.IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<TCustomer> customerList = customerService.getCustomerList(null);
        request.setAttribute("customerList", customerList);
        request.getRequestDispatcher("/WEB-INF/view/customer.jsp").forward(request, response);
    }
}
