<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        <script type="text/javascript">
           $(function(){
                $('#mounth').val(3);
               //查询余额
               ajax({
                   url:"<%=request.getContextPath() %>/queryUserBalance.do",
                   type:'post',//非必须.默认get
                   data:{},
                   dataType:'json',//非必须.默认text
                   async:true,//非必须.默认true
                   cache:false,//非必须.默认false
                   timeout:30000,//非必须.默认30秒
                   success:function(data){
                       $("#totalAm").val(data.data.bt);
                       $("#balanceDiv").html("当前可投金额"+data.data.bt+"元,账户余额:"+data.data.ba+"元");
                       $("#balanceDiv").show();
                   }
               });
               <c:if test="${not empty errorMsg}">
               alert('<c:out value="${errorMsg}"/>');
               </c:if>
               getExpectedReturn();
           });
           function submitBuy(){
               var mounth = $('#mounth').val();
               var amount = $.trim($('#amount').val());
               var totalAm = $('#totalAm').val();
               var source = getSource();
               $("#source").val(source);
               if(amount < 1000 || amount%1000 != 0){
                   $('#amount').css('color','#ff0000');
                   $('#amount').val("输入金额必须是1000的整数倍，最低1000元!");
                   return;
               }
               if(amount>totalAm){
                   alert("投资金额大于可投金额！");
                   return;
               }
               $("#form1").submit();
           }
           function changeClass(ob){
               var className = $(ob).attr('name');
               var nodes = $("."+className);
               for(var i = 0,j = nodes.length;i < j; i++){
                   $(nodes[i]).removeClass('onClick');
                   $(nodes[i]).siblings().removeClass('click');
               }
               $(ob).addClass("onClick");
               $(ob).siblings().addClass('click');
               $('#mounth').val($(ob).attr('data'));
           }
            //禁用Enter键表单自动提交
            document.onkeydown = function(event) {
                var target, code, tag;
                if (!event) {
                    event = window.event; //针对ie浏览器
                    target = event.srcElement;
                    code = event.keyCode;
                    if (code == 13) {
                        tag = target.tagName;
                        if (tag == "TEXTAREA") { return true; }
                        else { return false; }
                    }
                }else{
                    target = event.target; //针对遵循w3c标准的浏览器，如Firefox
                    code = event.keyCode;
                    if (code == 13) {
                        tag = target.tagName;
                        if (tag == "INPUT") { return false; }
                        else { return true; }
                    }
                }
            };
           //获取预期收益
           function getExpectedReturn(){
               var money = $('#amount').val();
               if(isNaN(money)){
                   return;
               }
               if(money == ''){
                   $('#expectedReturn').text(0);
                   return;
               }
               var Rate;
               var mounth = $('#mounth').val();
               if(mounth == 3){
                   Rate = 0.06;
               }else if(mounth == 6){
                   Rate = 0.08;
               }else if(mounth == 12){
                   Rate = 0.1;
               }
               $('#expectedReturn').text((money*Rate).toFixed(2));
               return;
           }
           function cleanInput(){
               $('#amount').val('');
               $('#amount').css('color','#666666');
           }
        </script>
        <style type="text/css">
            .selectWindow{
                position: relative;
                width: 1140px;
                height: 360px;
                border: 15px solid #fef1db;
                background: #ffffff;
            }
            .selectBox{
                position: relative;
                margin-top: 30px;
            }
            .selectBox li{
                position: relative;
                display: inline-block;
            }
            .selectBox li .changeSpan{
                display: inline-block;
                position: relative;
                width: 296px;
                height: 142px;
                border:4px solid #f1f1f1;
                border-top: 4px solid #ff9900;
                margin-left: 55px;
                cursor: pointer;
                overflow: hidden;
            }
            .selectBox .changeSpan.onClick{
                border:4px solid #ff9900;
            }
            .selectBox .changeSpan .financeTitle{
                height: 50px;
                line-height: 50px;
                color: #706968;
                font-size: 22px;
                padding-left: 80px;
            }
            .selectBox .changeSpan .financeTitle img{
                position: absolute;
                top:0;
                left: 0;
            }
            .selectBox .changeSpan em {
            position: absolute;
            left: -240px;
            top: 0;
            width: 211px;
            transform: skewx(-15deg);
            -o-transform: skewx(-25deg);
            -moz-transform: skewx(-25deg);
            -webkit-transform: skewx(-25deg);
            background-image: -moz-linear-gradient(0deg, rgba(253,255,120,0), rgba(253,255,120,0.4), rgba(253,255,120,0));
            background-image: -webkit-linear-gradient(0deg,rgba(253,255,120,0), rgba(253,255,120,0.4), rgba(253,255,120,0));
            background-image: -o-linear-gradient(0deg,rgba(253,255,120,0), rgba(253,255,120,0.4), rgba(253,255,120,0));
            background-image: linear-gradient(0deg,rgba(253,255,120,0), rgba(253,255,120,0.4), rgba(253,255,120,0));
            height: 142px;
            }
            .selectBox .changeSpan:hover em{
            left: 350px;
            -moz-transition: .5s;
            -o-transition: .5s;
            -webkit-transition: .5s;
            transition: .5s;
            }
            .selectBox .profitBox{
                background: #f8f8f8;
                height: 94px;
                position: relative;
            }
            .selectBox .profitBox .baozhang{
                position: absolute;
                display: block;
                top:19px;
                left: 19px;
                padding-top: 10px;
                height: 47px;
                width: 74px;
                border-right: 1px dashed #dddddd;
                font-size: 12px;
                color: #666666;
            }
            .selectBox .profitBox .baozhang p{
                line-height: 20px;
            }
            .selectBox .profitBox .moneyRate{
                position: absolute;
                display: block;
                top:19px;
                left: 94px;
                height: 57px;
                padding-top: 5px;
                padding-left: 25px;
                font-size: 12px;
                color: #666666;
            }
            .selectBox .hook-pic{
                position: absolute;
                top:118px;
                left: 333px;
                width: 46px;
                height: 46px;
                background:url(../images/conductFinancial-icon.png) -51px 0 ; 
                border:none;
                margin: 0;
            }
            .selectBox .hook-pic.click{
                background:url(../images/conductFinancial-icon.png); 
            }
            .inputBox{
                width: 1020px;
                height: 58px;
                border: 1px solid #dddddd;
                margin: 0 auto;
                margin-top:33px; 
                overflow: hidden;
            }
            .buyBtn{
                display: block;
                margin: 0 auto;
                margin-top: 24px;
                width: 209px;
                height: 43px;
                background: #ff9900;
                cursor: pointer;
                color: #ffffff;
                font-size: 18px;
                line-height: 43px;
                text-align: center;
            }
            .inputBox .mairu{
                display: inline-block;
                padding-left: 5px;
                height: 58px;
                width: 124px;
                line-height: 58px;
                text-align: center;
                font-size: 20px;
                color: #333333;
            }
            .inputBox input{
                width: 440px;
                height: 34px;
                vertical-align: middle;
                font-size: 20px;
                border:0;
                border-right: 1px solid #dddddd;
                outline: none;
                margin-top: -4px;
            }
            .inputBox .mairujine{
                display: inline-block;
                height: 58px;
                width: 80px;
                line-height: 58px;
                text-align: center;
                font-size: 14px;
                color: #666666;
            }
        </style>
    </head>
    <body>
    <div class="selectWindow">
           <form id="form1" name="form1" method="post" action="<%=request.getContextPath() %>/buyDetailPage.do">
                <div class="selectBox">
                    <li>
                        <span class="changeSpan onClick" name="changeSpan" onclick="changeClass(this);getExpectedReturn()" data="3" id="mounth3" title="定期3个月理财产品">
                            <div class="financeTitle">
                                定期3个月理财产品
                                <img src="<c:url value='/images/licaiIcon.jpg'/>"/>
                            </div>
                            <div class="profitBox">
                                <span class="baozhang">
                                    <p>100%本息</p>
                                    <p>保障计划</p>
                                </span>
                                <span class="moneyRate">
                                    <p style="line-height:20px">年化利率</p>
                                    <p style="font-size:32px;color:#ff9900">6<span style="color:#706968;font-size:22px">%起</span></p>
                                </span>
                            </div>
                            <em></em>
                        </span>
                        <span class="hook-pic click"></span>
                    </li>
                    <li>
                        <span class="changeSpan" name="changeSpan" onclick="changeClass(this);getExpectedReturn()" id="mounth6" data="6" title="定期6个月理财产品">
                            <div class="financeTitle">
                                定期6个月理财产品
                                <img src="<c:url value='/images/licaiIcon.jpg'/>"/>
                            </div>
                            <div class="profitBox">
                                <span class="baozhang">
                                    <p>100%本息</p>
                                    <p>保障计划</p>
                                </span>
                                <span class="moneyRate">
                                    <p style="line-height:20px">年化利率</p>
                                    <p style="font-size:32px;color:#ff9900">8<span style="color:#706968;font-size:22px">%起</span></p>
                                </span>
                            </div>
                            <em></em>
                        </span>
                        <span class="hook-pic"></span>
                    </li>
                    <li>
                        <span class="changeSpan" name="changeSpan" onclick="changeClass(this);getExpectedReturn()" id="mounth12" data="12" title="定期12个月理财产品">
                            <div class="financeTitle">
                                定期12个月理财产品

                                <img src="<c:url value='/images/licaiIcon.jpg'/>"/>
                            </div>
                            <div class="profitBox">
                                <span class="baozhang">
                                    <p>100%本息</p>
                                    <p>保障计划</p>
                                </span>
                                <span class="moneyRate">
                                    <p style="line-height:20px">年化利率</p>
                                    <p style="font-size:32px;color:#ff9900">10<span style="color:#706968;font-size:22px">%起</span></p>
                                </span>
                            </div>
                            <em></em>
                        </span>
                        <span class="hook-pic"></span>
                    </li>
                </div>
                <div class="inputBox">
                    <span class="mairu">买入金额：</span>
                    <input type="text" id="amount" name="amount" value="" placeholder="输入买入金额，为1000.00元整倍数"oninput="getExpectedReturn()" onpropertychange="getExpectedReturn()" onfocus="cleanInput()"/>
                    <span class="mairujine">预期收益：</span><span style="font-size:18px;color:#ff9900">￥<span id="expectedReturn"></span></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>(比银行定期存款收益高 <span style="color:#ff9900">4.6%</span>)</span>
                </div><div id="balanceDiv" style="display: none;position: absolute;top:196px;padding-left: 62px;"></div>
                <input type="hidden" id="mounth" name="mounth" value="" />
                <input type="hidden" id="source" name="source" />
                <input type="hidden" id="totalAm" name="totalAm" />
               <input type="hidden" name="boss-token" value="<%=request.getSession().getAttribute("boss-token")%>" /> 
                <a class="buyBtn" onclick="submitBuy()">立即买入</a>
               </form>
                    </div>
                </div>
           </form>
        </div>
    </body>
</html>