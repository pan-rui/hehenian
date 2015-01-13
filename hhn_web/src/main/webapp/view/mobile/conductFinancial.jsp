<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <script type="text/javascript">
            function getInvestRate(mounth){
                var param = [];
                param.push("mounth="+mounth);
                param = param.join("&");
                ajax({
                    url:"<c:url value="/getInvestRate.do"/>",
                    type:'get',//非必须.默认get
                    data:param,
                    jsonp:"jsoncallback", 
                    dataType:'json',//非必须.默认text
                    async:true,//非必须.默认true
                    cache:false,//非必须.默认false
                    timeout:30000,//非必须.默认30秒
                    success: getInvestRateSuccess
                },mounth);
            }

            function getInvestRateSuccess(data,mounth){
                if(data.returnCode !=0 ){
                    alert(data.messageInfo);
                    return;
                }
                document.location.href = "<c:url value="/view/mobile/buy-confirm.jsp"/>?mounth="+mounth+"&InvestRate="+data.data.rate+"&balance="+data.data.balance;
            }
        </script>
    </head>
    <body>
        <div class="container conductFinancial">
            <div class="header">
                <div>
                    <p style="font-size:39px;color:#ffffff;padding-top:40px;padding-left:200px">最安全的理财平台</p>
                    <p style="font-size:24px;color:#ffffff;padding-top:20px;padding-left:200px">预期年化收益率高达14%</p>
                </div>
            </div>
           <div class="list">
                <span class="list-one">
                    <span class="icon"></span>
                    <span class="list-text">定期<br/>理财</span>
                    <span class="big">3个月</span>
                </span>
                <span class="list-one">
                    <span class="icon" style="background-position: -33px 0;"></span>
                    <span class="list-text">年化<br/>收益</span>
                    <span class="big">8%</span>
                </span>
                <a class="buyBtn" onclick="getInvestRate(3)">买入</a>
           </div>
           <div class="list">
                <span class="list-one">
                    <span class="icon"></span>
                    <span class="list-text">定期<br/>理财</span>
                    <span class="big">6个月</span>
                </span>
                <span class="list-one">
                    <span class="icon" style="background-position: -33px 0;"></span>
                    <span class="list-text">年化<br/>收益</span>
                    <span class="big">10%</span>
                </span>
                <a class="buyBtn" onclick="getInvestRate(6)">买入</a>
           </div>
           <div class="list">
                <span class="list-one">
                    <span class="icon"></span>
                    <span class="list-text">定期<br/>理财</span>
                    <span class="big">12个月</span>
                </span>
                <span class="list-one">
                    <span class="icon" style="background-position: -33px 0;"></span>
                    <span class="list-text">年化<br/>收益</span>
                    <span class="big">14%</span>
                </span>
                <a class="buyBtn" onclick="getInvestRate(12)">买入</a>
           </div>
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