<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    HttpSession session1 = request.getSession();
    session1.removeAttribute("phMounth");
    session1.removeAttribute("phAmount");
    session1.removeAttribute("phTotalAmount");
%>
<!DOCTYPE html>
<html>
    <head>
    	<title></title>
    	<meta charset="utf-8">
        <meta name="viewport" content="target-densitydpi=320, width=640, user-scalable=no">
        <link rel="stylesheet"  href="<c:url value="/view/mobile/css/base.css"/>"/>
        <link rel="stylesheet"  href="<c:url value="/view/mobile/css/common.css"/>"/>
        <link rel="stylesheet"  href="<c:url value="/view/mobile/css/public.css"/>"/>
        <script type="text/javascript" src="<c:url value="/view/mobile/js/jquery-1.9.1.min.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/view/mobile/js/template.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/view/mobile/js/common.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/view/mobile/js/module.js"/>"></script>
        <script>
            var mounth = getUrlParam("mounth");
            var amount = getUrlParam("amount");
            var tradeTime = getUrlParam("tradeTime");
            $(function() {
                $("#ds1").html("定期理财"+mounth+"个月");
                $("#ds2").html(amount+"元");
                $("#ds3").html(tradeTime);
            });
        </script>
    </head>
    <body>
        <div class="container buy-success">
            <div class="header">
                定期理财
            </div>
            <div class="content">  
                <div class="success-title">
                    <img src="<c:url value="/view/mobile/images/buy-success-hook.png"/>"/>
                    <p>　　恭喜您，理财产品购买成功！</p>
                </div>
                <div class="success-text">
                    <div>
                        <span class="left">商      品</span>
                        <span class="right" id="ds1"></span>
                    </div>
                    <div>
                        <span class="left">交易金额</span>
                        <span class="right" id="ds2"></span>
                    </div>
                    <div>
                        <span class="left">交易时间</span>
                        <span class="right" id="ds3"></span>
                    </div>
                    <div>
                        <span class="left">当前状态</span>
                        <span class="right">支付成功</span>
                    </div>
                </div>
                <div style=""></div>
            </div>
            <img src="<c:url value="/view/mobile/images/bolang.png"/>"/>
            
            <div class="bottom">
                <span class="buttom-menu">
                    <div class="first"></div>
                    <p>首页</p>
                </span>
                <span class="buttom-menu">
                    <div class="second"></div>
                    <p>投资列表</p>
                </span>
                <span class="buttom-menu">
                    <div class="third"></div>
                    <p>我的账户</p>
                </span>
                <span class="buttom-menu">
                    <div class="fourth"></div>
                    <p>更多</p>
                </span>
            </div>
        </div>
    </body>
</html>