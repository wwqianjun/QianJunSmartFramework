<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: john
  Date: 2015/11/18
  Time: 15:00
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set var="basePath" value="${pageContext.request.contextPath}"/>
<html>
<head>
    <title>客户管理管理</title>
</head>
<body>
  <h1>客户列表</h1>

  <table>
    <tr>
      <th>客户名称</th>
      <th>联系人</th>
      <th>电话号码</th>
      <th>邮箱地址</th>
      <th>操作</th>
    </tr>
    <c:forEach var="customer" items="${customerList}">
      <tr>
        <td>${customer.name}</td>
        <td>${customer.contact}</td>
        <td>${customer.telephone}</td>
        <td>${customer.email}</td>
        <td>
          <a href="${basePath}/customer_edit?id=${customer.id}">编辑</a>
          <a href="${basePath}/customer_create?id=${customer.id}">删除</a>
        </td>
      </tr>
    </c:forEach>
  </table>

<c:if test="result==boolean">alert("success")</c:if>
</body>
</html>
