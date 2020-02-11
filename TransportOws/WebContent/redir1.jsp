<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import ="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.TreeMap" %>    
    
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<%
	List<String>results=(List)request.getAttribute("Transports_Type");

		for(int i=0;i<results.size();i++){		
%>
	

	<p>Transport : <%= results.get(i) %> <p>
	<% }%>		
	
</body>
</html>