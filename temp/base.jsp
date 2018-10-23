<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common.jsp"%>
<%

    String userName = (String)request.getSession().getAttribute("UserName");
    if (null == userName) {
        response.sendRedirect(request.getSession().getAttribute("ContextPath") + "/");
    }
%>
<link href="${ContextPath}/css/base/galaxy.base.css" rel="stylesheet" type="text/css" />
<link href="${ContextPath}/lib/galaxy-iconfont/iconfont.css" rel="stylesheet" type="text/css" />
<link href="${ContextPath}/css/app/galaxy.admin.css" rel="stylesheet" type="text/css" />
<link href="${ContextPath}/css/app/galaxy.skin.css" rel="stylesheet" type="text/css" />
<link href="${ContextPath}/lib/select2-4.0.2/dist/css/select2.css" rel="stylesheet" type="text/css" />