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
            $(function(){
                getTransactiondetails();
            })
            /***********获得交易列表*******************/
            function getTransactiondetails(){
                var stParam = [];
                stParam.push("user_id=1");
                ajax({
                    url:"<c:url value="/getTradeForPhone.do"/>",
                    type:'post',//非必须.默认get
                    data:stParam.join("&"),
                    dataType:'json',//非必须.默认text
                    async:true,//非必须.默认true
                    cache:false,//非必须.默认false
                    timeout:30000,//非必须.默认30秒
                    success:getTransactiondetailsSuccess//非必须
                });
            }
            function getTransactiondetailsSuccess(data){
                if(data.returnCode != 0){
                    alert(data.messageInfo);return;
                }
                var st = data.data;
                var htm ="";
                for(var idx in st){
                    var newTime = new Date(st[idx].trade_time);
                    var date = newTime.getFullYear()+"-"+newTime.getMonth()+"-"+newTime.getDate();
                    htm += '<tr><td><span class="icon"></span>　　　'+st[idx].period+'个月</td>'+
                    '<td>￥<span style="color:#ff9900;font-size:30px">'+st[idx].trade_amount+'</span></td>'+
                    '<td>'+date+'</td></tr>';
                }
                $("#listBox").html(htm);
            }
        </script>
    </head>
    <body>
        <div class="container transactionDetails">
            <div class="header">
                定期理财
            </div>
            <table style="position: fixed;top:61px;z-index:10">
                <tr>
                    <th>定期时间</th>
                    <th>交易金额</th>
                    <th>交易时间</th>
                </tr>
            </table>  
            <div class="content">
                <table style="">
                    <tbody id="listBox">
                    </tbody>
                </table>
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