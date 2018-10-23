<!DOCTYPE html>
<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ include file="/portralonly.jsp"%>
<%@ page import="com.dcits.dynamic.web.action.LoginAction" %>
<%
   String ssoflag = LoginAction.getSSOFlag();
   if ("true".equals(ssoflag)) {
       //跳转到根目录，由根页面即gologin.jsp去跳转到指定页面
       response.sendRedirect(request.getSession().getAttribute("ContextPath") + "/");
   }
%>
<html>
<head>
    <title>交易辅助系统运营管理平台</title>
</head>
<body onkeypress="keyLogin(event);">
<div class="logo">
    </div>
    <form action="" method="post"  id="login-main" >
        <p class="text">交易辅助系统运营管理平台</p>
        <p class="copyright"></p>
        <input name="name" placeholder="账号" class="name" maxlength="20" id="InputUserName" required />
        <input name="password" placeholder="密码" class="password" type="password" maxlength="32s" id="InputPassword" required />
        <input  class="btn" type="button" value="  登   录  " onclick="return login(this)" id="login-btn"/>
    </form>
    <div class="msg" id="login-msg" style="display:none;"><span class="textMsg" >用户名和密码不匹配，请重新输入</span></div>
</body></html>

