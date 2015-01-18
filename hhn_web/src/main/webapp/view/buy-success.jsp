<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.hhn.util.DateUtil,java.util.Date" %>
<%
    String expectTime = request.getParameter("expectTime");
    if(expectTime!=null && !"".equals(expectTime)) {
        expectTime = DateUtil.format(new Date(Long.parseLong(expectTime)));
    }
    HttpSession session1 = request.getSession();
    session1.removeAttribute("parameterMap");
%>
<!DOCTYPE html>
<html>
    <head>
    	<title></title>
    	<meta charset="utf-8">
        <link rel="stylesheet"  href="<c:url value='/css/base.css'/>">
        <link rel="stylesheet"  href="<c:url value='/css/common.css'/>">
        <link rel="stylesheet"  href="<c:url value='/css/public.css'/>">
        <script type="text/javascript" src="<c:url value='/js/jquery-1.11.1.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/js/common.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/js/module.js'/>"></script>
        <script>
        </script>
    </head>
    <body>
    <jsp:include page="./include/top.jsp" flush="true" />
        <div class="content buy-success">
            <div class="content-nav">
                <div class="content-nav-sec">
                    <span class="triangle-pic"></span>
                    <a class="content-nav-text">返回列表</a>
                </div>
            </div>
            <div class="content-body">
                <div class="success-box">
                    <span class="step first">
                        1.提交购买信息
                    </span>
                    <span class="step second">
                        2.理财产品购买成功
                    </span>
                    <span class="step third">
                        3.坐等产品收益
                    </span>
                    <span class="Hook-pic"></span>
                    <p class="success-text gongxi">恭喜您，理财产品购买成功！</p>
                    <p class="success-text yuji">预计<%=DateUtil.getThirtDate(new Date(), 1) %>开始产生收益。</p>
                    <p class="success-text ninke">您可以登录“个人中心”-“理财管理”查看交易明细。</p>
                    <a class="loginBtn" href="/home.do">登录个人中心　></a>
                </div>

            </div>
        </div>
        <jsp:include page="./include/footer.jsp" flush="true" />
    </body>
</html>