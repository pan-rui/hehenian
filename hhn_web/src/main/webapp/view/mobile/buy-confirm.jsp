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
        <script>
            var mounth = getUrlParam("mounth");
            var investRate = getUrlParam("InvestRate");
            var balance = getUrlParam("balance");
            $(function() {
                $('#mounth').text(mounth + '个月');
                $('#investRate').text((investRate * 100) + '%');
                if (balance != '') {
                    $('.yuqi').append("&nbsp;&nbsp;账户余额：<span style='color:#ff9833' id='banSpan'>"+balance+"元</span>");
                }
                //计算预期收益
                $("#amount").change(function(){
                    var amountVal = $(this).val();
                    var mVal = 0;
                    if(isNaN(amountVal)){
                        mVal = 0;
                    }else {
                        mVal = parseInt(amountVal) * investRate;
                    }
                    $("#rateSpan").text(mVal+"元");
                });
            });
            function subBuy(){
                var amount = $('#amount').val();
                if(amount < 1000 || amount%1000 != 0){
                    alert('请输入买入金额,为1000的整倍数');
                    return;
                }
                if($('#agreementSelect').attr('class') != "onclick"){
                    alert('请同意协议');
                    return;
                }
                var param = [];
                param.push("mounth=" + mounth);
                param.push("amount=" + amount);
                param.push("source=" + getSource());
                param.push("boss-token=<%=request.getSession().getAttribute("boss-token")%>");
                param = param.join("&");
                ajax({
                   url:"<c:url value="/buyFunancial.do"/>",
                   type:'post',//非必须.默认get
                   data:param,
                   dataType:'json',//非必须.默认text
                   async:true,//非必须.默认true
                   cache:false,//非必须.默认false
                   timeout:30000,//非必须.默认30秒
                   success:submitBuySuccess//非必须
                });
            }
            function submitBuySuccess(data){
                if(data.returnCode == 0){
                    var param="?mounth="+data.data.mounth;
                    param += "&amount="+data.data.amount;
                    param += "&tradeTime="+data.data.tradeTime;
                    document.location.href = "<c:url value="/view/mobile/buy-success.jsp"/>"+param;
                }else if(data.returnCode==1){
                    alert(data.messageInfo);
                }else if(data.returnCode==2){
                    var param="?mounth="+data.data.mounth;
                    param += "&userName="+encodeURI(data.data.userName);
                    param += "&phone="+data.data.phone;
                    param += "&balance="+data.data.balance;
                    param += "&amount="+data.data.amount;
                    param += "&totalAmount="+data.data.totalAmount;
                    document.location.href = "<c:url value="/view/mobile/payment.jsp"/>"+param;
                }
            }
            function selectAgree(ob){
                if(!$(ob).attr('class')){
                    $(ob).attr('class','onclick');
                }else{
                    $(ob).attr('class','');
                }
            }
        </script>
    </head>
    <body>
        <div class="container buy-confirm">
            <div class="header">
                定期理财
            </div>
            <div class="content">
                <div class="species">
                    <span style="border-right:1px solid #eeeeee">
                        <div class="icon"></div>
                        <p class="first-font">　定期理财</p>
                        <p class="second-font" id="mounth"></p>
                    </span>
                    <span>
                        <div class="icon" style="background-position: -33px 0;"></div>
                        <p class="first-font">　年化收益</p>
                        <p class="second-font" id="investRate"></p>
                    </span>
                </div>
            </div>
            <img src="<c:url value="/view/mobile/images/bolang.png"/>"/>
            <span class="prompt-icon"></span>
            <p class="prompt">买入金额最低1000.00元</p>
            <div class="inputBox">
                <span>买入金额</span>
                <input type="text" id="amount" name="amount" placeholder="请输入买入金额,为1000的整倍数" />
            </div>
            <p class="yuqi">预期收益：<span style="color:#ff9833" id="rateSpan">0元</span></p>
            <a class="confirmBtn" onclick="subBuy()">确认购买</a>
            <div class="agreementBox">
                <span id="agreementSelect" onclick="selectAgree(this)"></span>
                　　同意《<a style="color:#4aa3e9">合和年协议及风险提示</a>》
            </div>
        </div>
    </body>
</html>