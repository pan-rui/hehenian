<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
    String mounth = request.getParameter("mounth");
    String amount = request.getParameter("amount");
    amount = (amount==null || "".equals(amount))?"":amount;
%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <title>合和年在线</title>
    <link rel="stylesheet"  href="<c:url value='/css/base.css'/>">
    <link rel="stylesheet"  href="<c:url value='/css/common.css'/>">
    <link rel="stylesheet"  href="<c:url value='/css/public.css'/>">
    <script type="text/javascript" src="<c:url value='/js/jquery-1.11.1.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/common.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/js/module.js'/>"></script>
    <script type="text/javascript">
        $(function(){
            <% if (mounth==null || "".equals(mounth)){ %>
                $('#mounth').val(3);
            <% }else{ %>
                $('#mounth').val('<%=mounth %>');
                $('#mounth3').removeClass('onClick');
                $('#mounth3').siblings().removeClass('click');
                $('#mounth<%=mounth%>').addClass("onClick");
                $('#mounth<%=mounth%>').siblings().addClass('click');
            <%}%>
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
            var amount = $('#amount').val();
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
        // function changeClass(ob){
        //     var nodes = $(ob).siblings();
        //     for(var i = 0,j = nodes.length;i < j; i++){
        //         $(nodes[i]).removeClass('onClick');
        //         $(nodes[i]).find('span').removeClass('click');
        //     }
        //     $(ob).addClass("onClick");
        //     $(ob).find('span').addClass('click');
        //     $('#mounth').val($(ob).attr('title'));
        //     $("#productRateId").val($(ob).attr("rateId"));
        // }
        function changeClass(ob,b){
            var className = $(ob).attr('name');
            var nodes = $("."+className);
            for(var i = 0,j = nodes.length;i < j; i++){
                $(nodes[i]).removeClass('onClick');
                $(nodes[i]).siblings().removeClass('click');
            }
            $(ob).addClass("onClick");
            $(ob).siblings().addClass('click');
            $('#mounth').val(b);
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
            var Rate;
            var mounth = $('#mounth').val();
            if(mounth == 3){
                Rate = 6;
            }else if(mounth == 6){
                Rate = 8;
            }else if(mounth == 12){
                Rate = 10;
            }
            Rate = Rate/100;
            var expectedReturn = money*Rate/12*mounth;
            if(expectedReturn>10000){
                expectedReturn = Math.ceil(expectedReturn/10000)+'万';
                $('#expectedReturn').text(expectedReturn);
                return;
            }
            if(money == ""){
                expectedReturn = 0;
            }
            $('#expectedReturn').text(expectedReturn.toFixed(2));
        }
        function cleanInput(){
            $('#amount').val('');
            $('#amount').css('color','#666666');
        }

    </script>
</head>
<body>
<jsp:include page="./include/top.jsp" flush="true"/>
<div class="content conductFinancial">
    <!-- <div class="banner">
        <div class="banner-pic">
            <p>最安全的理财平台</p>
            <p class="small">预期年化收益率高达14%</p>
        </div>
    </div>
    <div class="selectWindow">
        <form id="form1" name="form1" method="post" action="<c:url value="/buyDetailPage.do"/>">
            <div class="selectBox">
                <span class="onClick" id="mounth3" onclick="changeClass(this)" title="3">
                    <div class="financeTitle">
                        定期3个月
                    </div>
                    <p class="yuqishouyi">预期收益</p>
                    <p class="shouyi">6%-8%</p>
                    <p class="benxibaozhang">100%本息保障计划</p>
                    <span class="hook-pic click"></span>
                </span>
                <span onclick="changeClass(this)" id="mounth6" title="6">
                    <div class="financeTitle">
                        定期6个月
                    </div>
                    <p class="yuqishouyi">预期收益</p>
                    <p class="shouyi">8%-12%</p>
                    <p class="benxibaozhang">100%本息保障计划</p>
                    <span class="hook-pic"></span>
                </span>
                <span onclick="changeClass(this)" id="mounth12" title="12">
                    <div class="financeTitle">
                        定期12个月
                    </div>
                    <p class="yuqishouyi">预期收益</p>
                    <p class="shouyi">12%-14%</p>
                    <p class="benxibaozhang">100%本息保障计划</p>
                    <span class="hook-pic"></span>
                </span>
            </div>
            <div class="inputBox">
                <span class="mairu">买入金额：</span>
                <input type="text" id="amount" name="amount" value="<%=amount%>" placeholder="输入买入金额，为1000.00元整倍数" onfocus="$('.mairujine').css('color','#666666')"/>
                <span class="mairujine">买入金额最低1000元</span>
                <div id="balanceDiv" style="display: none;padding-left: 5px;"></div>
            </div>
            <input type="hidden" id="mounth" name="mounth" value="<%=mounth%>" />
            <input type="hidden" id="source" name="source" />
            <input type="hidden" name="boss-token" value="<%=request.getSession().getAttribute("boss-token")%>" />
            <a class="buyBtn" onclick="submitBuy()">立即买入</a>
        </form>
    </div> -->
   <!--  新的banner及输入框 -->
   <div class="banner">
        <p class="title">最安全的理财平台</p>
        <div class="bannerPic"></div>
        <div class="selectWindow">
            <form id="form1" name="form1" method="post" action="<c:url value="/buyDetailPage.do"/>">
            <div class="selectBox">
                <li>
                    <span class="changeSpan onClick" name="changeSpan" onclick="changeClass(this,3);getExpectedReturn()" id="mounth3" title="定期3个月理财产品">
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
                    <span class="changeSpan" name="changeSpan" onclick="changeClass(this,6);getExpectedReturn()" id="mounth6" title="定期6个月理财产品">
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
                    <span class="changeSpan" name="changeSpan" onclick="changeClass(this,12);getExpectedReturn()" id="mounth12" title="定期12个月理财产品">
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
                <input type="text" id="amount" name="amount" value="<%=amount%>" placeholder="输入买入金额，为1000.00元整倍数"oninput="getExpectedReturn()" onpropertychange="getExpectedReturn()" onfocus="cleanInput()"/>
                <span class="mairujine">预期收益：</span><span style="font-size:18px;color:#ff9900">￥<span id="expectedReturn"></span></span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span>(比银行定期存款收益高 <span style="color:#ff9900">4.6%</span>)</span>
            </div><div id="balanceDiv" style="display: none;position: absolute;top:196px;padding-left: 62px;"></div>
            <input type="hidden" id="mounth" name="mounth" value="<%=mounth%>" />
            <input type="hidden" id="source" name="source" />
            <input type="hidden" id="totalAm" name="totalAm" />
            <input type="hidden" name="boss-token" value="<%=request.getSession().getAttribute("boss-token")%>" />
            <a class="buyBtn" onclick="submitBuy()">立即买入</a>
            </form>
        </div>
    </div>
    <!-- 新的banner及输入框结束 -->
    <p style="font-size:36px;color:#333333;text-align:center;margin-top:132px">4大优势 值得信赖</p>
    <p style="font-size:20px;color:#999999;text-align:center;margin-top:20px">你我的成功因相互信赖</p>
    <div class="advantageBox">
        <div class="swatch-container pt-7241c">
            <div class="inner">
                <div class="front"></div>
                <div class="back" style="background-position:0 -200px"></div>
            </div>
            <p>预期收益最高14%</p>
        </div>
        <div class="swatch-container pt-7241c">
            <div class="inner">
                <div class="front" style="background-position:-200px 0" ></div>
                <div class="back" style="background-position:-200px -200px"></div>
            </div>
            <p>购买次日立享收益</p>
        </div>
        <div class="swatch-container pt-7241c">
            <div class="inner">
                <div class="front" style="background-position:-400px 0"></div>
                <div class="back" style="background-position:-400px -199px"></div>
            </div>
            <p>在线快速查询领取</p>
        </div>
        <div class="swatch-container pt-7241c">
            <div class="inner">
                <div class="front" style="background-position:-600px 0"></div>
                <div class="back" style="background-position:-600px -199px"></div>
            </div>
            <p> 1000元即可投资</p>
        </div>
    </div>
</div>
<jsp:include page="./include/footer.jsp" flush="true" />
</body>
</html>