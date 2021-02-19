<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.jsp">Home</a></h3>
<hr>
<h2>Users</h2>
<section>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Id</th>
            <th>Login</th>
            <th>Role</th>
            <th>e-mail</th>
            <th>Date created</th>
            <th>Switch user</th>
        </tr>
        </thead>
        <c:forEach items="${users}" var="user">
            <jsp:useBean id="user" type="ru.javawebinar.topjava.model.User"/>
            <tr>
                <td>${user.id}</td>
                <th><a href="users?action=change&userId=${user.id}">${user.name}</a></th>
                <td>${user.roles}</td>
                <td>${user.email}</td>
                <td>${user.registered}</td>
                <th><a href="users?action=change&userId=${user.id}">Select user</a></th>
            </tr>
        </c:forEach>
    </table>
</section>

</body>
</html>