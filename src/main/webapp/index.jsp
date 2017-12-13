<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="java.util.*" %>
<html>
<head>
    <title>login</title>
</head>
<body>
<div id="exit">
    <%  String st=(String) request.getAttribute("exitMessage");
        if(st!=null)
            out.println(st);%>
</div>

<form action="login">
    Name:<input type="text" name="name1"/><br>
    Pass:<input type="password" name="pass1"><br>
    <input type="submit" value="Submit">
    <div id="loginError">
        <%  String s=(String)request.getAttribute("loginError");
            if(s!=null)
                out.println(s);
             %>
    </div>
    </form>
    <form action="register">
        Name:<input type="text" name="name2"/><br>
        Pass:<input type="password" name="pass2"><br>
        <input type="submit" value="Submit">
        <div id="registerError">
            <%  String ss=(String)request.getAttribute("registerError");
                if(ss!=null)
                    out.println(ss);
            %>
        </div>
</form>
</body>
</html>
