<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<html lang="ru">
<head>
    <title>Users</title>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Users</h2>
<section>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <c:forEach items="${users}" var="user">
            <jsp:useBean id="user" type="ru.javawebinar.topjava.model.User"/>
            <tr>
                <td>${user.id}</td>
                <th><a href="users?action=change&userId=${user.id}">${user.name}</a></th>
                <td>${user.email}</td>
                <th><a href="users?action=change&userId=${user.id}">Select user</a></th>
            </tr>
        </c:forEach>
        </thead>
    </table>
</section>

</body>
</html>