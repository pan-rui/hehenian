<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2014/12/16
  Time: 18:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="f" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>提前还款</title>
  <link rel=stylesheet type=text/css href="<c:url value="/css/css.css"/>"/>
  <script type=text/javascript src="<c:url value="/js/jquery-1.9.1.min.js"/>"></script>
  <script type=text/javascript src="<c:url value="/js/My97DatePicker/WdatePicker.js"/>"></script>
  <script type="text/javascript">
    function aheadFun(id) {
      if (confirm("确认还款吗？")) {
          var bowAmount = $("#bowAmount").val();
          var ivestAmount = $("#ivestAmount").val();
        if(bowAmount==""){
            alert("还款金额不能为空！");
            return;
        }else{
            if(isNaN(bowAmount)){
                alert("输入金额有误！");
                return;
            }
        }
          if(ivestAmount==""){
              alert("还款利息不能为空！");
              return;
          }else{
              if(isNaN(ivestAmount)){
                  alert("输入还款利息有误！");
                  return;
              }
          }
        $.ajax({
          type: "POST",
          url: "<c:url value="/signAudit.do"/>",
          data: {productId: id,bowAmount:bowAmount,ivestAmount:ivestAmount},
          async: true,
          dataType: "json",
          success: function (data) {
            if (data) {
              alert("还款成功！");
            }else{
              alert("还款失败！");
            }
          }
        });
      }
    }
  </script>
</head>
<body>
<div id=right>
  <div style="padding-bottom: 0px; padding-left: 10px; padding-right: 10px; padding-top: 15px">
    <div></div>
    <table id="detail" cellspacing="0" cellpadding="0" style="border-collapse: collapse;border: 1px solid #DEE7EF;width: 100%">
     <thead>
      <tr>
        <th>ID</th>
        <th>标的名称</th>
        <th>借款人</th>
        <th>期次</th>
        <th>应还时间</th>
        <th>还款时间</th>
        <th>还款金额</th>
        <th>状态</th>
     </tr>
     </thead>
      <tbody>
      <c:set var="mon" value="0"/>
      <c:set var="non" value="0"/>
      <c:forEach items="${repayList}" var="list">
        <tr>
            <td><c:out value="${list.repayment_id}" /></td>
            <td><c:out value="${list.product_name}" /></td>
            <td><c:out value="${list.realName}" /></td>
            <td><c:out value="${list.repay_times}" /></td>
            <td><f:formatDate value="${list.repay_date}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><f:formatDate value="${list.update_time}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
            <td><c:out value="${list.repay_amount}" /></td>
            <td><c:if test="${list.repay_status=='REPAYMENTED'}">已还款</c:if>
                <c:if test="${list.repay_status=='NO_REPAYMENT'}">未还款</c:if>
                <c:if test="${list.repay_status=='PROXY_REPAY'}">已代赏</c:if>
                <c:if test="${list.repay_status=='AHEAD_REPAY'}">提前还款</c:if>
            </td>
        </tr>
          <c:set var="mon" value="${mon + list.repay_amount}" />
          <c:if test="${list.repay_status=='NO_REPAYMENT' or list.repay_status=='PROXY_REPAY'}">
              <c:set var="non" value="${non + list.repay_amount}" />
          </c:if>
      </c:forEach>
      </tbody>
        <tr>
            <td align="left" colspan="8"><font color="red">合计：<c:out value="${mon}" /></font></td>
        </tr>
    </table>
      <table id="money" cellspacing="0" cellpadding="0" style="border-collapse: collapse;border: 1px solid #DEE7EF;width: 100%">
        <tr>
            <td>还款金额：</td><td><input type="text" name="bowAmount" id="bowAmount" /></td>
            <td>还款利息：</td><td><input type="text" name="ivestAmount" id="ivestAmount" /></td>
        </tr>
      </table>
      <div align="center"><input type="button" value="确认还款" onclick="aheadFun('<c:out value="${productDetail.product_id}"/>')" />
              &nbsp;&nbsp;<input type="button" value="取消" onclick="window.close();" />
      </div>
  </div>
  </div>
</body>
</html>
