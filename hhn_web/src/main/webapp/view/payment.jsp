<%@ page pageEncoding="UTF-8" language="java" %>
<%@page import="java.util.Map,java.util.HashMap" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    HttpSession session2 = request.getSession();
    Map map = new HashMap();
    map.put("mounth",request.getAttribute("mounth"));
    map.put("amount",request.getAttribute("amount"));
    map.put("source",request.getAttribute("source"));
    session.setAttribute("parameterMap", map);
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
        <title>充值页面</title>
        <link rel="stylesheet"  href="<c:url value='/css/base.css'/>">
        <link rel="stylesheet"  href="<c:url value='/css/common.css'/>">
        <link rel="stylesheet"  href="<c:url value='/css/public.css'/>">
        <script type="text/javascript" src="<c:url value='/js/jquery-1.11.1.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/js/common.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/js/module.js'/>"></script>
        <script type="text/javascript">
            var it;count=60;
            $(function(){
                if ((navigator.userAgent.indexOf('MSIE') >= 0)&&(navigator.userAgent.indexOf('Opera') < 0)){
                    $('#userName').val('输入持卡人姓名');
                    $('#userName').attr('onfocus',"if(this.value == '输入持卡人姓名') this.value = ''");
                    $('#userName').attr('onblur',"if(this.value == '') this.value = '输入持卡人姓名'");
                    $('#userAccount').val('输入银行卡号');
                    $('#userAccount').attr('onfocus',"if(this.value == '输入银行卡号') this.value = ''");
                    $('#userAccount').attr('onblur',"if(this.value == '') this.value = '输入银行卡号'");
                }
                $("#btn2").bind("click",function(){
                    getVerifyCode();
                });
            });
            function subPay(){
                var bankCode = $("#bankCode").val();
                var userAccount = $('#userAccount').val();
                var passwd = $("#userPassword").val();
                var phone = $("#phone").attr("phone");
                var verfiyCode = $("#verfiyCode").val();
                if(bankCode==''){
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>请选择银行</p><a onclick="closePop()">确定</a></div>');
                    return;
                }
                if(userAccount == ""){
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>请输入银行卡号</p><a onclick="closePop()">确定</a></div>');
                    return;
                }else{
                    if(userAccount.replace(/( )/g, "").length<16 || userAccount.replace(/( )/g, "").length>19){
                        $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>输入有误，请输入16至19位银行卡号</p><a onclick="closePop()">确定</a></div>');
                        return;
                    }
                }
                if(passwd==""){
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>登录密码不能为空</p><a onclick="closePop()">确定</a></div>');
                    return;
                }
                if(phone==""){
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>还未绑定手机号，请先绑定</p><a onclick="closePop()">确定</a></div>');
                    return;
                }
                if(verfiyCode==""){
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>请输入验证码</p><a onclick="closePop()">确定</a></div>');
                    return;
                }
                if($('#agreementSelect').attr('class') != "onclick"){
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>请认真阅读并签署代扣协议</p><a onclick="closePop()">确定</a></div>');
                    return;
                }
                $('body').append('<div class="cover"></div><div class="payingPop">正在支付请耐心等待。。。</div>');
                var param = [];
                param.push("userName=<c:out value="${userName}" />");
                param.push("bankCode="+bankCode);
                param.push("userAccount=" + userAccount.replace(/( )/g, ""));
                param.push("password="+passwd);
                param.push("amount=<c:out value="${amount}" />");
                param.push("mounth=<c:out value="${mounth}" />");
                param.push("totalAmount=<c:out value="${totalAmount}" />");
                param.push("phone="+phone);
                param.push("verfiyCode="+verfiyCode);
                param.push("boss-token=<%=request.getSession().getAttribute("boss-token")%>");
                param = param.join("&");
                ajax({
                    url:"<c:url value="/doChargeMoney.do"/>",
                    type:'post',//非必须.默认get
                    data:param,
                    dataType:'json',//非必须.默认text
                    async:true,//非必须.默认true
                    cache:false,//非必须.默认false
                    timeout:60000,//非必须.默认30秒
                    success:subPaySuccess//非必须
                });
            }
            function subPaySuccess(data){
                if(data.returnCode != 0){
                    closePop();
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>'+data.messageInfo+'</p><a onclick="closePop()">确定</a></div>');
                    setInterval("goToPage()",30000);
                }else{
                    closePop();
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifuchenggong.png"/>"><p>充值成功</p><a onclick="closePop()">确定</a></div>');
                    $("#form2").submit();
                }
            }
            function closePop(){
                $('.cover').remove();
                $('.payFailure').remove();
                $('.payingPop').remove();
            }
            function selectAgree(ob){
                if(!$(ob).attr('class')){
                    $(ob).attr('class','onclick');
                }else{
                    $(ob).attr('class','');
                }
            }
            function goToPage(){
                self.location.href = "<c:url value="/view/conductFinancial.jsp"/>";
            }
            function formatInput(ob){
                var sID = $.trim(ob.id);
                var sValue = $.trim($(ob).val());
                sValue = sValue.replace(/\s/g,"");
                sValue = sValue.substring(0,19); 
                if(sID.indexOf("user") != -1){
                    sValue = sValue.replace(/.{4}/g,function(str){
                        return str+' '; 
                    });
                    sValue = $.trim(sValue);
                    if(sValue.length > 25){
                        sValue = $.trim(sValue.substr(0,25))+'...';
                    }
                }else{
                    if(sValue.length > 16){
                        sValue = $.trim(sValue.substr(0,16))+'...';
                    }   
                }
                sValue = $.trim(sValue);
                if(sValue != "")$('#'+sID+'FormatTip').html(sValue).show();
                else $('#'+sID+'FormatTip').hide();
            }

            function getVerifyCode(){
                $("#btn2").unbind("click");
                $("#btn2").attr("disabled","disabled");
                it = setInterval("relayTime()",1000);
                <%--$.post("http://10.111.0.203:6050/common/send-phone-virifycode.do", {mobile:'<c:out value="${phone}" />'}, function(data) {--%>
                $.post("/common/send-phone-virifycode.do", {mobile:'<c:out value="${phone}" />'}, function(data) {
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
                    $("#btn2").html(count + "后可重新发送");
                }else{
                    clearInterval(it);
                    $("#btn2").html("获取验证码");
                    $("#btn2").removeAttr("disabled");
                    $("#btn2").bind("click",function(){getVerifyCode();});
                    count=60;
                }
            }
            //选择银行点击后银行code保存在隐藏域中
            function selectBank(ob,b){
                var nodes = $(ob).siblings();
                for (var i = 0; i < nodes.length; i++) {
                    $(nodes[i]).removeClass('onClick');
                };
                $(ob).addClass('onClick');
                $('#bankCode').val(b);
            }
        </script>
    </head>
    <body>
    <jsp:include page="./include/top.jsp" flush="true" />
        <div class="content payment">
            <div class="content-nav">
                <div class="content-nav-sec">
                    <span class="triangle-pic"></span>
                    <a class="content-nav-text">返回列表</a>
                </div>
            </div>
            <div class="content-body">
                <div class="success-box">
                    <img style="position: absolute;top:32px;left:226px;" src="<c:url value="/images/paymentIcon.png"/>"/>
                    <p>支付金额<span><c:out value="${amount}" /></span>元,账户余额<span><c:out value="${balance}" /></span>元</p>
                    <div class="inputBox">
                        <div>姓名　　<input type="text" id="userName" name="userName" value="<c:out value="${userName}"/>" placeholder="输入持卡人姓名" disabled="disabled" /></div>
                        <c:if test="${empty userName}"><span style="text-decoration:underline;padding-left: 74px;"><a href="/owerInformationInit.do?fromUrl=/hhn_web/buyDetailPage.do" target="_blank">完善个人信息</a></span><br/></c:if>
                        <div style="height:auto;margin:0">
                            <input id="bankCode" name="bankCode" type="hidden" value="0" />
                            <span style="position: absolute;top:0;left:0">银行</span>
                            <span class="bankBox">
                                <span class="bankLogo onClick" onclick="selectBank(this,0)">
                                    <img src="<c:url value='/images/zgyh.jpg'/>">
                                    <p class="bankName">中国银行</p>
                                </span>
                                <span class="bankLogo" onclick="selectBank(this,1)">
                                    <img src="<c:url value='/images/nyyh.jpg'/>" >
                                    <p class="bankName">农业银行</p>
                                </span>
                                <span class="bankLogo" onclick="selectBank(this,2)">
                                    <img src="<c:url value='/images/jsyh.jpg'/>">   
                                    <p class="bankName">建设银行</p>   
                                </span>
                                <span style="margin-right:0px" class="bankLogo" onclick="selectBank(this,3)">
                                    <img src="<c:url value='/images/jtyh.jpg'/>">
                                    <p class="bankName">交通银行</p>
                                </span>
                                <span class="bankLogo" onclick="selectBank(this,4)">
                                    <img src="<c:url value='/images/zsyh.jpg'/>">
                                    <p class="bankName">招商银行</p>
                                </span>
                                <span class="bankLogo" onclick="selectBank(this,5)">
                                    <img src="<c:url value='/images/ycyh.jpg'/>">
                                    <p class="bankName">邮储银行</p>
                                </span>
                                <span class="bankLogo" onclick="selectBank(this,6)">
                                    <img src="<c:url value='/images/xyyh.jpg'/>">
                                    <p class="bankName">兴业银行</p>
                                </span>
                                <span style="margin-right:0px" class="bankLogo" onclick="selectBank(this,7)">
                                    <img src="<c:url value='/images/gdyh.jpg'/>">
                                    <p class="bankName">光大银行</p>
                                </span>
                            </span>
                        </div>
                        <div>卡号　　<input type="text" id="userAccount" name="userAccount" placeholder="输入银行卡号" onkeyup="formatBankNo(this)" onkeydown="formatBankNo(this)" onblur="$('#userAccountFormatTip').hide()" oninput="formatInput(this)" onpropertychange="formatInput(this)"/><div class="accountNoO" id="userAccountFormatTip"></div></div>
                        <div>密码　　<input type="password" id="userPassword" name="userPassword" placeholder="请输入合和年网站的登陆密码"/></div>
                        <div>手机号　<input type="text" id="phone" name="phone" placeholder="输入手机号" value="<c:out value="${phone }"/>" phone="<c:out value="${phone }"/>" disabled="disabled" /></div>
                        <c:if test="${empty phone}"><span style="text-decoration:underline;padding-left: 74px;"><a href="/updatexgmm.do?ivp=1&fromUrl=/hhn_web/buyDetailPage.do" target="_blank">修改手机号</a></span><br/></c:if>
                        <div>　　　　<input type="text" id="verfiyCode" name="verfiyCode" placeholder="输入验证码" id="code" style="width:120px">&nbsp;&nbsp;&nbsp;&nbsp;<button style="height:40px" id="btn2" >获取验证码</button><font color="red" style="font-size: 13px;" id="s_telephone"><c:if test="${empty phone}">未绑定手机号，请先绑定！</c:if></font></div>
                        <div class="agreement"><span id="agreementSelect" onclick="selectAgree(this)"></span><a href="#">同意合和年代扣协议</a></div>
                        <a class="paymentBtn" onclick="subPay()">确认支付</a>
                    </div>
                </div>
            </div>
            <div style="display: none;">
                <form action="<c:url value="/getIntPcRate.do"/>" id="form2" name="form2" method="get">
                    <input type="hidden" name="mounth" value="<c:out value="${mounth}"/>" />
                    <input type="hidden" name="totalAmount" value="<c:out value="${totalAmount}"/>" />
                    <input type="hidden" name="boss-token" value="<%=request.getSession().getAttribute("boss-token")%>" />
                </form>
            </div>
        </div>
        <jsp:include page="./include/footer.jsp" flush="true" />
    </body>
</html>