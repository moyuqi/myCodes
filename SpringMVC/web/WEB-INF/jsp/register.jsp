<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
    Title:
    Description:
    Parameter:1、
    Procedure:1、
    Author: ZhengHongEn
    Revision History:
    2013/05/21             ZhengHongEn             Create
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Register</title>
</head>
<body>
<form method="post">
    UserName:<input type="text" name="username" value="${user.username}" /><br>
    Password:<input type="password" name="password" /><br>
    city:<select>
    <c:forEach items="${cityList}" var="city">
        <option>${city}</option>
    </c:forEach>
    </select><br>
    <input type="submit" value="注册" />
</form>

</body>
</html>