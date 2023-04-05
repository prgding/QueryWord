<%@ page contentType="text/html;charset=UTF-8" %>
<html>
	<head>
		<title>index page</title>
	</head>
	<body>
		<form action="${pageContext.request.contextPath}/get_passage" method="post">
			<input id="input" name="passage">
			<input type="submit" value="提交">
		</form>
	</body>
</html>
