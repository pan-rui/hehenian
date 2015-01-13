<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title></title>
        <meta charset="utf-8">
        <meta http-equiv="Access-Control-Allow-Origin" content="*">
        <meta name="viewport" content="target-densitydpi=320, width=640, user-scalable=no">
        <link rel="stylesheet"  href="<c:url value="/view/mobile/css/base.css"/>"/>
        <link rel="stylesheet"  href="<c:url value="/view/mobile/css/common.css"/>"/>
        <link rel="stylesheet"  href="<c:url value="/view/mobile/css/public.css"/>"/>
        <script type="text/javascript" src="<c:url value="/view/mobile/js/jquery-1.9.1.min.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/view/mobile/js/template.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/view/mobile/js/common.js"/>"></script>
        <script type="text/javascript" src="<c:url value="/view/mobile/js/module.js"/>"></script>
        <script type="text/javascript">
            var it;count=60;
            var mounth = getUrlParam("mounth");
            var userName = decodeURI(getUrlParam("userName"));
            var phone = getUrlParam("phone");
            var balance = getUrlParam("balance");
            var amount = getUrlParam("amount");
            var totalAmount = getUrlParam("totalAmount");
            $(function(){
                $("#userName").val(userName);
                $("#phone").val(phone);
                $(".payManey").append("支付金额<span id='maney'>"+amount+"</span>元");
                $(".payManey").append("&nbsp;&nbsp;账户余额<span>"+balance+"</span>");
                $(".codeButton").bind("click",function(){
                    getVerifyCode();
                });
            });
            function subPay(){
                if($('#subBtn').text() == "正在支付....."){
                    return;
                }
                var bankCode = $("#bankCode").val();
                if(bankCode==''){
                    alert("请选择开户银行");
                    return;
                }
                var userAccount = $('#userAccount').val();
                if(userAccount == ""){
                    alert('银行卡号不能为空');
                    return;
                }else{
                    if(userAccount.replace(/( )/g, "").length<16 || userAccount.replace(/( )/g, "").length>19){
                        alert("卡号有误，请输入16至19银行卡号");
                        return;
                    }
                }
                var passwd = $("#passwd").val();
                if(passwd==""){
                    alert("登录密码不能为空！");
                    return;
                }
                var verfiyCode = $("#verfiyCode").val();
                if(verfiyCode==""){
                    alert("验证码不能为空！");
                    return;
                }
                if($('#agreementSelect').attr('class') != "onclick"){
                    alert('请同意合和年代扣协议');
                    return;
                }
                var param = [];
                param.push("mounth=" + mounth);
                param.push("userName=" + userName);
                param.push("bankCode=" + bankCode);
                param.push("userAccount=" + userAccount.replace(/( )/g, ""));
                param.push("passwd=" + passwd);
                param.push("amount="+amount);
                param.push("totalAmount="+totalAmount);
                param.push("phone="+phone);
                param.push("source=" + getSource());
                param.push("verfiyCode="+verfiyCode);
                param = param.join("&");
                ajax({
                    url:"<c:url value="/chargeMoneyPhone.do"/>",
                    type:'post',//非必须.默认get
                    data:param,
                    dataType:'json',//非必须.默认text
                    async:true,//非必须.默认true
                    cache:false,//非必须.默认false
                    timeout:120000,//非必须.默认30秒
                    success:subPaySuccess//非必须
                });
                $('#subBtn').addClass('colorGray');
                $('#subBtn').text('正在支付.....');
            }
            function subPaySuccess(data){
                if(data.returnCode !=0){
                    alert(data.messageInfo);
                }else{
                    var param="?mounth="+data.data.mounth;
                    param += "&amount="+data.data.amount;
                    param += "&tradeTime="+data.data.tradeTime;
                    document.location.href = "<c:url value="/view/mobile/buy-success.jsp"/>"+param;
                }
            }
            function selectAgree(ob){
                if(!$(ob).attr('class')){
                    $(ob).attr('class','onclick');
                }else{
                    $(ob).attr('class','');
                }
            }
            function getVerifyCode(){
                $(".codeButton").unbind("click");
                $(".codeButton").attr("disabled","disabled");
                it = setInterval("relayTime()",1000);
                <%--$.post("http://10.111.0.203:6050/common/send-phone-virifycode.do", {mobile:'<c:out value="${phone}" />'}, function(data) {--%>
                $.post("/common/send-phone-virifycode.do", {mobile:phone}, function(data) {
                    var ret= data.ret;
                    if (ret == "0") {
                        $("#s_telephone").html("");
                    }else {
                        $("#s_telephone").html("验证码获取失败");
                    }
                });
            }
            function relayTime(){
                count--;
                if(count>0) {
                    $(".codeButton").html(count + "后可重新发送");
                }else{
                    clearInterval(it);
                    $(".codeButton").html("获取验证码");
                    $(".codeButton").removeAttr("disabled");
                    $(".codeButton").bind("click",function(){getVerifyCode();});
                    count=60;
                }
            }
             function showBank(){
                $('#cover').show();
                $('#bankBox').show();
            }
            function choiceBank(ob,code){
                $('#bankName').val($(ob).text());
                $('#bankCode').val(code);
                $('#cover').hide();
                $('#bankBox').hide();
            }
        </script>
    </head>
    <body>
        <div class="container payment">
            <div class="cover" id="cover"></div>
            <div class="bankBox" id="bankBox">
                <a class="bankText" onclick="choiceBank(this,0)">中国银行</a>
                <a class="bankText" onclick="choiceBank(this,1)">农业银行</a>
                <a class="bankText" onclick="choiceBank(this,2)">建设银行</a>
                <a class="bankText" onclick="choiceBank(this,3)">交通银行</a>
                <a class="bankText" onclick="choiceBank(this,4)">招商银行</a>
                <a class="bankText" onclick="choiceBank(this,5)">邮储银行</a>
                <a class="bankText" onclick="choiceBank(this,6)">兴业银行</a>
                <a class="bankText" onclick="choiceBank(this,7)">光大银行</a>
            </div>
            <div class="header">
                定期理财
            </div>
            <div class="content">
                <p class="payManey"><img src="<c:url value="/view/mobile/images/paymentIcon.png"/>"/></p>
                <div class="inputBox">
                    <input type="text" id="userName" name="userName" placeholder="请输入持卡人姓名" disabled="disabled"/>
                </div>
                <div class="inputBox">
                    <input type="hidden" id="bankCode" name="bankCode" />
                    <input type="text" id="bankName" placeholder="请选择开户银行" onclick="showBank()" />
                </div>
                <div class="inputBox">
                    <input type="text" id="userAccount" name="userAccount" placeholder="请输入银行卡号" onkeyup="formatBankNo(this)" onkeydown="formatBankNo(this)" onblur="$('#userAccountFormatTip').hide()" oninput="formatInput(this)" onpropertychange="formatInput(this)"/><div class="accountNoO" id="userAccountFormatTip"></div>
                </div>
                <div class="inputBox">
                    <input type="password" id="passwd" name="passwd" placeholder="请输入登录密码"/>
                </div>
                <div class="inputBox">
                    <input type="text" id="phone" name="phone" placeholder="请输入手机号" disabled="disabled"/>
                </div>
            </div>
            <div style="margin:30px 20px">
                <input type="text" id="verfiyCode" name="verfiyCode" placeholder="请输入验证码" class="codeInput" /><button class="codeButton">获取验证码</button><font color="red" style="font-size: 13px;" id="s_telephone"></font>
            </div>
            <a class="subBtn" id="subBtn" onclick="subPay()">确认支付</a>
            <div class="agreementBox">
                <span id="agreementSelect" class="onclick" onclick="selectAgree(this)"></span>
                　　同意《<a style="color:#4aa3e9">合和年协议及风险提示</a>》
            </div>
        </div>
    </body>
</html>