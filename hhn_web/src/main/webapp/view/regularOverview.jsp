<%@ page pageEncoding="UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <title></title>
        <meta charset="utf-8">
        <link rel="stylesheet"  href="<c:url value='/css/base.css'/>" />
        <link rel="stylesheet"  href="<c:url value='/css/common.css'/>" />
        <link rel="stylesheet"  href="<c:url value='/css/public.css'/>" />
        <script type="text/javascript" src="<c:url value='/js/jquery-1.11.1.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/js/template.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/js/common.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/js/module.js'/>"></script>
        <script type="text/javascript">
        </script>
    </head>
    <body>
        <jsp:include page="./include/top.jsp" flush="true" />
        <div class="user-all regularOverview">
            <div class="user-center">
                <div style=" width:1170px;height:6px; margin:0px auto; padding-bottom:24px;position: relative;">
                    <img src="<c:url value='/images/detail_logo_bg.jpg'/>" style="height:6px;width:1170px;position: absolute;top: 0;" alt=""/>
                </div>
                <div style=" overflow:hidden">
                    <div class="u-left">
                        <jsp:include page="./include/leftMenu.jsp" flush="true"/>
                    </div>
                    <div class="u-right">
                        <div class="user-right">
                            <!--账户总览-->
                            <p style="border-bottom:1px solid #dddddd;height: 50px;">
                                <a style="font-size: 20px;color: #666666;line-height: 50px;">定期理财</a>&nbsp;&nbsp;&nbsp;
                                <%--<a style="display: inline-block;font-size: 20px;color: #4fbde8;line-height: 50px;font-weight: bold;border-bottom: 3px solid #4fbde8;">定期理财概览</a>--%>
                            </p>
                            <div class="account-box">
                                <ul class="clearfix">
                                    <li>
                                        <span class="t1">
                                            可用余额
                                        </span>
                                        <span class="t2 t-red">
                                            ￥<c:out value="${balance}" />
                                        </span>
                                        <span class="t3">
                                            <a style="left:65px;" class="bt_blue" href="<c:url value="/doUserCharge.do" />" target="_blank">
                                                <span class="bt_blue_lft">
                                                </span>
                                                <strong>
                                                    充值
                                                </strong>
                                                <span class="bt_blue_r">
                                                </span>
                                            </a>
                                            <a style="left:155px;" class="bt_green" href="#">
                                                <span class="bt_green_lft">
                                                </span>
                                                <strong>
                                                    提现
                                                </strong>
                                                <span class="bt_green_r">
                                                </span>
                                            </a>
                                        </span>
                                    </li>
                                    <li>
                                        <span class="t1">
                                            投资金额
                                        </span>
                                        <span class="t2 t-red">
                                            ￥<c:out value="${principal}" />
                                        </span>
                                        <span class="t3">

                                        </span>
                                    </li>
                                    <li>
                                        <span class="t1">
                                            赎回金额
                                        </span>
                                        <span class="t2 t-red">
                                            ￥<c:out value="${freeze}" />
                                        </span>
                                        <span class="t3">

                                        </span>
                                    </li>
                                    <li>
                                        <span class="t1 t-normal" style="background:#a5d8ed">
                                            待收利息
                                        </span>
                                        <span class="t2 t-yellow">
                                            ￥<c:out value="${interested}" />
                                        </span>
                                        <span class="t3">

                                        </span>
                                    </li>
                                    <li>
                                        <span class="t1 t-normal" style="background:#a5d8ed">
                                            已收利息
                                        </span>
                                        <span class="t2 t-dark">
                                            ￥<c:out value="${interest}" />
                                        </span>
                                        <span class="t3">

                                        </span>
                                    </li>
                                    <li>
                                        <span class="t1 t-normal" style="background:#a5d8ed">
                                            其他收益
                                        </span>
                                        <span class="t2 t-yellow">
                                            ￥<c:out value="${otherInterest}" />
                                        </span>
                                        <span class="t3">

                                        </span>
                                    </li>
                                </ul>
                            </div>
                            <!--一键投标-->
                            <%--<p style="font-size: 20px;color: #666666;line-height: 50px;">--%>
                                <%--E计划--%>
                            <%--</p>--%>
                            <%--<div class="biaoge">--%>
                                <%--<table width="100%">--%>
                                    <%--<tbody>--%>
                                        <%--<tr>--%>
                                            <%--<th></th><th>投资金额</th><th>已赚利息</th><th>已提取</th><th>持有数量</th>--%>
                                        <%--</tr>--%>
                                        <%--<tr>--%>
                                            <%--<td>1月</td><td>￥5000.00</td><td>￥50.00</td><td>￥500.00</td><td></td>--%>
                                        <%--</tr>--%>
                                        <%--<tr>--%>
                                            <%--<td>2月</td><td>￥5000.00</td><td>￥50.00</td><td>￥500.00</td><td></td>--%>
                                        <%--</tr>--%>
                                        <%--<tr>--%>
                                            <%--<td>3月</td><td>￥5000.00</td><td>￥50.00</td><td>￥500.00</td><td></td>--%>
                                        <%--</tr>--%>
                                    <%--</tbody>--%>
                                <%--</table>--%>
                            <%--</div>--%>
                            <%--<p style="font-size: 20px;color: #666666;line-height: 50px;">--%>
                                <%--个人信息--%>
                            <%--</p>--%>
                            <%--<div style=" background:#f0f4f7; overflow:hidden; padding:20px; line-height:28px; ">--%>
                                <%--<div style=" width:250px; float:left; border-right:1px solid #d0d5d9;  padding:0px 10px; height:150px;">--%>
                                    <%--<ul>--%>
                                        <%--<li>--%>
                                            <%--用户名：qwertyuiop--%>
                                        <%--</li>--%>
                                        <%--<li>--%>
                                            <%--电子邮箱：--%>
                                            <%--<a href="updatexgmm.do?j=2" style="color: #E94718;font-size: 12px;">--%>
                                                <%--您还没有绑定邮箱,点击绑定--%>
                                            <%--</a>--%>
                                            <%--<input value="" id="usrCustId" type="hidden">--%>
                                        <%--</li>--%>
                                        <%--<li>--%>
                                            <%--真实姓名：--%>
                                            <%--<a href="owerInformationInit.do" style="color: #E94718;font-size: 12px;">--%>
                                                <%--您还未填写个人真实信息,点击填写--%>
                                            <%--</a>--%>
                                        <%--</li>--%>
                                    <%--</ul>--%>
                                <%--</div>--%>
                                <%--<div style=" width:200px; float:left; border-right:1px solid #d0d5d9;  padding:0px 30px 0px 30px;height:150px;">--%>
                                    <%--<form id="flr" target="_blank" action="" method="post">--%>
                                        <%--<p>--%>
                                            <%--汇付编号:--%>
                                            <%--<span style="color:#e94718;font-size: 12px;">--%>
                                                <%--您还不是汇付天下会员--%>
                                            <%--</span>--%>
                                            <%--<br>--%>
                                            <%--<a id="registerChinaPnr" class="bt_green">--%>
                                                <%--<span class="bt_green_lft">--%>
                                                <%--</span>--%>
                                                <%--<strong>--%>
                                                    <%--点击注册--%>
                                                <%--</strong>--%>
                                                <%--<span class="bt_green_r">--%>
                                                <%--</span>--%>
                                            <%--</a>   --%>
                                        <%--</p>--%>
                                    <%--</form>--%>
                                <%--</div>--%>
                                <%--<div style="float:left; width:280px;   padding:0px 0px 0px 30px;height:150px;">--%>
                                    <%--<div>--%>
                                        <%--我的推广ID：164741--%>
                                        <%--<!-- <a class="tooltips" href="#tooltips"> --%>
                                        <%--<img src="/images/wenhao.png" width="13" height="13"  alt=""/>--%>
                                        <%--<span>新用户在注册时，在推荐人栏里填入您的推广ID号，在他首次投资后，您即可获得60元现金红包。 </span></a>--%>
                                        <%--<style type="text/css">-->--%>
                                        <%--<!--/*Tooltips*/ .tooltips{ position:relative; /*这个是关键*/ z-index:2; }--%>
                                        <%--.tooltips:hover{ z-index:3; background:none; /*没有这个在IE中不可用*/ } .tooltips--%>
                                        <%--span{ display: none; } .tooltips:hover span{ /*span 标签仅在 :hover 状态时显示*/--%>
                                        <%--display:block; position:absolute; line-height:16px; width:15em; border:1px--%>
                                        <%--solid black; background-color:#fbf7e9; padding:8px; color:black; } </style>-->--%>
                                    <%--</div>--%>
                                    <%--<div>--%>
                                        <%--推广链接：--%>
                                        <%--<a id="copy_button">--%>
                                            <%--点击复制链接--%>
                                        <%--</a>--%>
                                    <%--</div>--%>
                                    <%--<div>--%>
                                        <%--<a id="link">--%>
                                            <%--http://www.hehenian.com/account/reg.do?param=812f3853b878d3d4--%>
                                        <%--</a>--%>
                                    <%--</div>--%>
                                    <%--<div>--%>
                                        <%--我的推荐人: 暂无--%>
                                    <%--</div>--%>
                                <%--</div>--%>
                            <%--</div>--%>
                            <!--其他-->
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <jsp:include page="./include/footer.jsp" flush="true" />
    </body>
</html>