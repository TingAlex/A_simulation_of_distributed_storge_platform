<%@ page import="java.util.LinkedList" %>
<%@ page import="beans.StorageNode" %><%--
  Created by IntelliJ IDEA.
  User: Ting
  Date: 2017/7/16
  Time: 22:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@page isELIgnored="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <meta http-equiv="refresh" content="10">
    <title>console</title>
    <style>
        table, td, th
        {
            border:1px solid green;
        }
        th
        {
            background-color:green;
            color:white;
        }
    </style>
</head>
<body>
<h1>you get here</h1>
<a href="/exit">Exit</a>
<form action="upload" enctype="multipart/form-data" method="post">
    Upload File<input type="file" name="uploadFile"/>
    <%--<label><input type="radio" name="Access" onclick="if(this.value==1){this.value=0;this.checked=0}else this.c=1"  value="public">goPublic</label>--%>
    <input type="submit" value="Submit">
</form>

<%  String st=(String) request.getAttribute("uploadMessage");
    String stt=(String) request.getAttribute("UID");
    if(st!=null)
        out.println(st);
    if(stt!=null)
        out.println(stt);%>
<form action="download">
    Download UID:<input type="text" name="downloadFile"/>
    <input type="submit" value="Submit">
</form>
<%  String s=(String) request.getAttribute("downloadMessage");
    if(s!=null)
        out.println(s);%>
<form action="delete">
    Delete UID:<input type="text" name="deleteFile"/>
    <input type="submit" value="Submit">
</form>
<%  String ss=(String) request.getAttribute("deleteMessage");
    if(ss!=null)
        out.println(ss);%>
<table>
    <tr>
        <th>On</th>
        <th>Name</th>
        <th>IP</th>
        <th>Capacity</th>
        <th>Used</th>
        <th>Remain</th>
    </tr>
    <c:forEach items="${requestScope.NodeInfo}" var="o" varStatus="st">
        <tr>
            <td align="center">${o.online }</td>
            <td align="center">${o.name }</td>
            <td align="center">${o.IP }:${o.port }</td>
            <td align="center">${o.capacity }</td>
            <td align="center">${o.used }</td>
            <td align="center">${o.remain }</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
