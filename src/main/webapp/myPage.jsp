<%--
  Created by IntelliJ IDEA.
  User: seonghwan
  Date: 11/17/23
  Time: 1:35 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<script>
/*onload 시 세션에 저장된 id에 따라 데이터 불러오기  */

</script>


<body>

마이페이지입니다. 비동기로 두 칸을 바꿔줘야 합니다.<br>
<form action = "api/updateMember.do" method = "post">
<label>비밀번호 바꾸기</label>
<input type = "password" name = "password">
<button type = "submit">변경하기</button>
</form>
<br>
히스토리 가져오기
<br>
</body>
</html>
