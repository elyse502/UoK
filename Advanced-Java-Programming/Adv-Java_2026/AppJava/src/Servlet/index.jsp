<%-- 
    Document   : index
    Created on : Apr 26, 2026, 10:08:12 AM
    Author     : idtda
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>First JSP Page</title>
    </head>
    <body>
        <h1>Hello JSP!</h1>
        <%
            java.util.Date date = new java.util.Date();
        %>
        <<h2>Now is <% =date.toString() %></h2>
    </body>
</html>
