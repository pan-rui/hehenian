<%@ page pageEncoding="UTF-8" language="java" %>
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
            var t1;
            $(function(){
                $(".payBtn").click(function(){
                    if($('.payBtn').css('background') == '#a1a1a1'){
                        return;
                    }
                    if($('#agreement-pic').attr('class') == "agreement-pic"){
                       alert('请同意协议');
                       return;
                    }
                    var source = getSource();
                    var param = [];
                    param.push("mounth=<c:out value="${mounth}" />");
                    param.push("amount=<c:out value="${amount}" />");
                    param.push("source="+source);
                    param.push("boss-token=<%=request.getSession().getAttribute("boss-token")%>");
                    param = param.join("&");
                    ajax({
                        url:"<%=request.getContextPath() %>/doInvest.do",
                        type:'post',//非必须.默认get
                        data:param,
                        dataType:'json',//非必须.默认text
                        async:true,//非必须.默认true
                        cache:false,//非必须.默认false
                        timeout:30000,//非必须.默认30秒
                        success: function (data) {
                            if(data.returnCode==0){
                                $('.payBtn').css('background','#a1a1a1');
                                var expectTime = data.data.expect_trade_time;
                                document.location.href = "<c:url value="/view/buy-success.jsp"/>?expectTime="+expectTime;
                            }else{
                                $('.payBtn').css('background','#a1a1a1');
                                alert(data.messageInfo);
                                t1 = setInterval('inter2pae()', 2000);
                            }
                        },
                        error:function (XMLHttpRequest, textStatus, errorThrown) {
                            alert(XMLHttpRequest.status);
                            alert(XMLHttpRequest.readyState);
                            alert(textStatus);
                        }
                    });
                });
            });

            function inter2pae(){
                clearInterval(t1);
                document.location.href = "<c:url value="/view/conductFinancial.jsp"/>";
            }
            function modifyMoney(){
               document.location.href = "<c:url value="/view/conductFinancial.jsp"/>?mounth=<c:out value="${mounth}"/>&amount=<c:out value="${amount}"/>";
            }
            function selectAgree(ob){
                if($(ob).attr('class') == "agreement-pic"){
                    $(ob).attr('class','agreement-pic onclick');
                }else{
                    $(ob).attr('class','agreement-pic');
                }
            } 
        </script>
    </head>
    <body>
    <jsp:include page="./include/top.jsp" flush="true" />
        <div class="content buy-confirm">
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
                    <div class="tableBox">
                        <table border="1">
                            <tr>
                                <th>产品</th>
                                <th>交易期限</th>
                                <th>年利率</th>
                                <th>买入金额</th>
                            </tr>
                            <tr>
                                <td>理财产品</td>
                                <td><c:out value="${mounth}" />个月</td>
                                <td><c:out value="${rate}" />%</td>
                                <td>￥<c:out value="${amount}" /></td>
                            </tr>
                            <tr>
                                <td colspan="4" style="text-align: right;padding-right: 15px;">预期收益：<c:out value="${rateMoney}" />元&nbsp;&nbsp;账户余额:<c:out value="${balance}" />元</td>
                            </tr>
                        </table>
                        <div>订单金额:　<span><c:out value="${amount}" /></span></div>
                    </div>
                    <div class="agreementBox">
                        <span class="agreement-pic" id="agreement-pic" onclick="selectAgree(this)"></span>
                        <span>同意<a>《xxxxxx协议》</a></span>
                    </div>
                    <a class="modifyBtn" href="javascript:void(0)" onclick="modifyMoney()">修改金额</a>&nbsp;&nbsp;<a class="payBtn" href="javascript:void(0)">立即付款</a>
                    <!--<div class="modifyBtn" onclick="modifyMoney()">修改价格</div>-->
                </div>
            </div>
        </div>
        <jsp:include page="./include/footer.jsp" flush="true" />
    </body>
</html>