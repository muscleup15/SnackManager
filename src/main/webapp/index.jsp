<%-- Created by IntelliJ IDEA. User: seonghwan Date: 11/17/23 Time: 1:35 PM To
change this template use File | Settings | File Templates. --%> <%@ page
contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Title</title>
  </head>
  <body>
    <jsp:include page="/header.jsp" />

    <a href="index.jsp">home</a> <br />
    <a href="login.jsp">login</a> <br />
    <a href="admin/selectAll.do">admin-inventory</a> <br />
    <a href="student/selectAll.do">student-inventory</a> <br />
    <a href="admin/registerSnack.do">admin-register-snack</a> <br>





    <a href="getMemberRank.do">member rank</a>
    <a href="getSnackRank.do">snack rank</a>
  </body>
</html>
