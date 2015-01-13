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
            function subPay(){
                if($('#subBtn').text() == "正在支付....."){
                    return;
                }
                if($('#agreementSelect').attr('class') != "onclick"){
                    alert('请同意协议');
                    return;
                }
                var userName = $('#userName').val();
                var userAccount = $('#userAccount').val();
                if(userName == ""){
                    alert('用户名不能为空');
                    return;
                }
                if(userAccount == ""){
                    alert('账号不能为空');
                    return;
                }else{
                    var reg = /^\d{16,19}$/g; // 银行卡号16到19位数字
                    if(!reg.test(userAccount)){
                        alert("帐号有误，请输入16至19银行卡号");
                        return;
                    }
                }
                var param = [];
                param.push("userId=1");
                param.push("userName=" + userName);
                param.push("userAccount=" + userAccount);
                param.push("amount=10000");
                param = param.join("&");
                ajax({
                    url:"<c:url value="/doChargeMoney.do"/>",
                    type:'post',//非必须.默认get
                    data:param,
                    dataType:'json',//非必须.默认text
                    async:true,//非必须.默认true
                    cache:false,//非必须.默认false
                    timeout:30000,//非必须.默认30秒
                    success:subPaySuccess//非必须
                });
                $('#subBtn').addClass('colorGray');
                $('#subBtn').text('正在支付.....');
            }
            function subPaySuccess(data){
                if(data.returnCode !=0){
                    alert(data.messageInfo);
                }else{
                    alert("充值成功");
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
        <div class="container payment">
            <div class="header">
                定期理财
            </div>
            <div class="content">
                <p class="payManey"><img src="<c:url value="/view/mobile/images/paymentIcon.png"/>"/>支付金额<span id="maney">1000.00</span>元</p>
                <div class="inputBox">
                    <input type="text" id="userName" placeholder="请输入持卡人姓名"/>
                </div>
                <div class="inputBox">
                    <input type="text" id="userAccount" placeholder="请输入银行卡号"/>
                </div>
                <div class="inputBox">
                    <input type="text" id="amount" placeholder="请输入金额"/>
                </div>
            </div>
            <div style="margin:30px 20px">
                <input type="text" placeholder="请输入验证码" class="codeInput" /><button class="codeButton">获取验证码</button>
            </div>
            <a class="subBtn" id="subBtn" onclick="subPay()">确认支付</a>
        </div>
    </body>
</html>