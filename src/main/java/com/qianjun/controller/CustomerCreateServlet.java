package com.qianjun.controller;

import com.qianjun.service.CustomerService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by ZiJun
 * Description:创建客户
 * Date: 2015/11/18 :14:25.
 */
@WebServlet("/customer_create")
public class CustomerCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 4043571944089153374L;

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
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    /**
     * 处理 创建客户的界面
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean result = customerService.deleteCustomer(Long.parseLong(request.getParameter("id")));

        request.setAttribute("result", result);
//        response.sendRedirect();
        request.getRequestDispatcher("/customer").forward(request, response);
    }
}
