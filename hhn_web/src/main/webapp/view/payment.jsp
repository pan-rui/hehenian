<%@ page pageEncoding="UTF-8" language="java" %>
<%@page import="java.util.*" %>
<%@ page import="java.math.BigDecimal" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    HttpSession session2 = request.getSession();
    HashMap map = new HashMap();
    Integer mth = (Integer)request.getAttribute("mounth");
    BigDecimal amt = (BigDecimal)request.getAttribute("totalAmount");
    String sc = (String)request.getAttribute("source");
    map.put("mounth", new String[]{mth.toString()});
    map.put("amount",new String[]{amt.toString()});
    map.put("source",new String[]{sc});
    session2.setAttribute("parameterMap", map);
    session2.removeAttribute("fromUrl");
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
                    $('#userName').attr('disabled',"disabled");
                    $('#userAccount').attr('disabled',"disabled");
                    $('#phone').attr('disabled',"disabled");
                }
                $("#btn2").bind("click",function(){
                    getVerifyCode();
                });
            });
            function subPay(){
                var userName = $("#userName").attr("data");
                var userAccount = $("#userAccount").attr("data");
                var phone = $("#phone").attr("data");
                var bankCode = $("#bankCode").val();
                var passwd = $("#userPassword").val();
                var verfiyCode = $("#verfiyCode").val();
                if(userName==''){
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>你未实名登记，请先完善个人信息</p><a onclick="closePop()">确定</a></div>');
                    return;
                }
                if(bankCode==''){
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>请选择银行</p><a onclick="closePop()">确定</a></div>');
                    return;
                }
                if(userAccount==''){
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>你未绑定银行卡，请先完成绑定</p><a onclick="closePop()">确定</a></div>');
                    return;
                }
                if(passwd==""){
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>登录密码不能为空</p><a onclick="closePop()">确定</a></div>');
                    return;
                }
                if(phone==''){
                    $('body').append('<div class="cover"></div><div class="payFailure"><img src="<c:url value="/images/zhifushibai.png"/>"><p>请先绑定手机号</p><a onclick="closePop()">确定</a></div>');
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
//            function formatInput(ob){
//                var sID = $.trim(ob.id);
//                var sValue = $.trim($(ob).val());
//                sValue = sValue.replace(/\s/g,"");
//                sValue = sValue.substring(0,19);
//                if(sID.indexOf("user") != -1){
//                    sValue = sValue.replace(/.{4}/g,function(str){
//                        return str+' ';
//                    });
//                    sValue = $.trim(sValue);
//                    if(sValue.length > 25){
//                        sValue = $.trim(sValue.substr(0,25))+'...';
//                    }
//                }else{
//                    if(sValue.length > 16){
//                        sValue = $.trim(sValue.substr(0,16))+'...';
//                    }
//                }
//                sValue = $.trim(sValue);
//                if(sValue != "")$('#'+sID+'FormatTip').html(sValue).show();
//                else $('#'+sID+'FormatTip').hide();
//            }

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
                        <div>姓名　　<input type="text" id="userName" name="userName" value="<c:out value="${userName}"/>" style="width: 334px;"
                                        data="<c:out value="${userName}"/>" placeholder="输入持卡人姓名" disabled="disabled" />
                            <span class="personalDataBox">
                                <a class="personalData" href="/owerInformationInit.do?fromUrl=<c:url value="/buyDetailPage.do"/>" target="_self">
                                    <c:if test="${empty userName}">完善个人信息</c:if><c:if test="${not empty userName}">修改个人信息</c:if></a>
                            </span></div>
                        <div style="height:auto;margin:0">
                            <input id="bankCode" name="bankCode" type="hidden"
                                <c:choose>
                                    <c:when test="${not empty bankCard and not empty bankCard.bank_code}"> value="<c:out value="${bankCard.bank_code}"/>"</c:when>
                                    <c:otherwise> value="0104"</c:otherwise>
                                </c:choose> />
                            <span style="position: absolute;top:0;left:0">银行</span>
                            <span class="bankBox">
                                <c:choose>
                                    <c:when test="${empty bankCard or empty bankCard.bank_code}">
                                        <span class="bankLogo onClick" onclick="selectBank(this,'0104')">
                                            <img src="<c:url value='/images/zgyh.jpg'/>">
                                            <p class="bankName">中国银行</p>
                                        </span>
                                        <span class="bankLogo" onclick="selectBank(this,'0103')">
                                            <img src="<c:url value='/images/nyyh.jpg'/>" >
                                            <p class="bankName">农业银行</p>
                                        </span>
                                        <span class="bankLogo" onclick="selectBank(this,'0105')">
                                            <img src="<c:url value='/images/jsyh.jpg'/>">
                                            <p class="bankName">建设银行</p>
                                        </span>
                                        <span style="margin-right:0px" class="bankLogo" onclick="selectBank(this,'0301')">
                                            <img src="<c:url value='/images/jtyh.jpg'/>">
                                            <p class="bankName">交通银行</p>
                                        </span>
                                        <span class="bankLogo" onclick="selectBank(this,'0308')">
                                            <img src="<c:url value='/images/zsyh.jpg'/>">
                                            <p class="bankName">招商银行</p>
                                        </span>
                                        <span class="bankLogo" onclick="selectBank(this,'0403')">
                                            <img src="<c:url value='/images/ycyh.jpg'/>">
                                            <p class="bankName">邮储银行</p>
                                        </span>
                                        <span class="bankLogo" onclick="selectBank(this,'0309')">
                                            <img src="<c:url value='/images/xyyh.jpg'/>">
                                            <p class="bankName">兴业银行</p>
                                        </span>
                                        <span style="margin-right:0px" class="bankLogo" onclick="selectBank(this,'0303')">
                                            <img src="<c:url value='/images/gdyh.jpg'/>">
                                            <p class="bankName">光大银行</p>
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${bankCard.bank_code=='0104'}">
                                            <span class="bankLogo onClick">
                                                <img src="<c:url value='/images/zgyh.jpg'/>">
                                                <p class="bankName">中国银行</p>
                                            </span>
                                        </c:if>
                                        <c:if test="${bankCard.bank_code=='0103'}">
                                            <span class="bankLogo onClick">
                                                <img src="<c:url value='/images/nyyh.jpg'/>" >
                                                <p class="bankName">农业银行</p>
                                            </span>
                                        </c:if>
                                        <c:if test="${bankCard.bank_code=='0105'}">
                                            <span class="bankLogo onClick">
                                            <img src="<c:url value='/images/jsyh.jpg'/>">
                                            <p class="bankName">建设银行</p>
                                            </span>
                                        </c:if>
                                        <c:if test="${bankCard.bank_code=='0301'}">
                                            <span class="bankLogo onClick">
                                                <img src="<c:url value='/images/jtyh.jpg'/>">
                                                <p class="bankName">交通银行</p>
                                            </span>
                                        </c:if>
                                        <c:if test="${bankCard.bank_code=='0308'}">
                                            <span class="bankLogo onClick">
                                                <img src="<c:url value='/images/zsyh.jpg'/>">
                                                <p class="bankName">招商银行</p>
                                            </span>
                                        </c:if>
                                        <c:if test="${bankCard.bank_code=='0403'}">
                                            <span class="bankLogo onClick">
                                                <img src="<c:url value='/images/ycyh.jpg'/>">
                                                <p class="bankName">邮储银行</p>
                                            </span>
                                        </c:if>
                                        <c:if test="${bankCard.bank_code=='0309'}">
                                            <span class="bankLogo onClick">
                                                <img src="<c:url value='/images/xyyh.jpg'/>">
                                                <p class="bankName">兴业银行</p>
                                            </span>
                                        </c:if>
                                        <c:if test="${bankCard.bank_code=='0303'}">
                                            <span class="bankLogo onClick">
                                                <img src="<c:url value='/images/gdyh.jpg'/>">
                                                <p class="bankName">光大银行</p>
                                            </span>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </span>
                        </div>
                        <div>卡号　　<input type="text" id="userAccount" name="userAccount" value="<c:out value="${bankCard.card_no}"/>" style="width: 334px;"
                                        data="<c:out value="${bankCard.card_no}"/>" placeholder="输入银行卡号" disabled="disabled" />
                            <span class="personalDataBox">
                                <a class="personalData" href="<c:url value="/queryBankCard.do"/>?fromUrl=/buyDetailPage.do" target="_self">
                                    <c:if test="${empty bankCard or empty bankCard.card_no}">绑定银行卡</c:if><c:if test="${not empty bankCard and not empty bankCard.card_no}">修改银行卡</c:if></a>
                            </span></div>
                        <div>密码　　<input type="password" id="userPassword" name="userPassword" placeholder="请输入合和年网站的登陆密码" style="width: 334px;" /></div>
                        <div>手机号　<input type="text" id="phone" name="phone" placeholder="输入手机号" value="<c:out value="${phone }"/>" style="width: 334px;"
                                        data="<c:out value="${phone }"/>" disabled="disabled" />
                            <span class="personalDataBox">
                                <a class="personalData" href="/updatexgmm.do?ivp=1&fromUrl=<c:url value="/buyDetailPage.do"/>" target="_self">
                                    <c:if test="${empty phone}">填写手机号</c:if><c:if test="${not empty phone}">修改手机号</c:if></a>
                            </span></div>
                        <div>　　　　<input type="text" id="verfiyCode" name="verfiyCode" maxlength="6" placeholder="输入验证码" id="code" style="width:120px">&nbsp;&nbsp;&nbsp;&nbsp;<button style="height:40px" id="btn2" >获取验证码</button><font color="red" style="font-size: 13px;" id="s_telephone"><c:if test="${empty phone}">未绑定手机号，请先绑定！</c:if></font></div>
                        <div class="agreement"><span id="agreementSelect" onclick="selectAgree(this)"></span><a href="<c:url value="/view/withholdingAgreement.jsp"/>" target="_blank">同意合和年代扣协议</a></div>
                        <a class="paymentBtn" onclick="subPay()">确认支付</a>
                    </div>
                </div>
            </div>
            <div style="display: none;">
                <form action="<c:url value="/getIntPcRate.do"/>" id="form2" name="form2" method="get">
                    <input type="hidden" name="mounth" value="<c:out value="${mounth}"/>" />
                    <input type="hidden" name="totalAmount" value="<c:out value="${totalAmount}"/>" />
                </form>
            </div>
        </div>
        <jsp:include page="./include/footer.jsp" flush="true" />
    </body>
</html>