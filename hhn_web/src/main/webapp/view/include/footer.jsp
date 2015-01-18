<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2014/12/24
  Time: 15:10
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="http://www.hehenian.com/top" %>
<% Cookie[] cookies=request.getCookies();
  String sessionId=null;
  for(Cookie cookie:cookies){
    if("s".equals(cookie.getName()))
      sessionId=cookie.getValue();
  }
  request.setAttribute("sessionId", ";s="+sessionId);
%>
<t:top url="${applicationScope.footerView}" suffix="${sessionId}" />