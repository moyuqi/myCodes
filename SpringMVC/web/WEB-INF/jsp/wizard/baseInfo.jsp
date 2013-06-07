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
    用户名:<input type="text" name="username" value="${user.username}" /><br>
    密码:<input type="password" name="password" /><br>
    真实姓名: <input type="text" name="username" value="${user.realname}" /><br>
    <input type="submit" name="_target1" value="下一步" />
</form>

</body>
</html>