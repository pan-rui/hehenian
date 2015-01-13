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
    <title>标的发布确认</title>
  <link rel=stylesheet type=text/css href="<c:url value="/css/css.css"/>"/>
  <script type=text/javascript src="<c:url value="/js/jquery-1.9.1.min.js"/>"></script>
  <script type=text/javascript src="<c:url value="/js/My97DatePicker/WdatePicker.js"/>"></script>
  <script type="text/javascript">
    function auditSign(id) {
      if (confirm("确认审核通过吗？")) {
        $.ajax({
          type: "POST",
          url: "<c:url value="/signAudit.do"/>",
          data: {productId: id},
          async: true,
          dataType: "json",
          success: function (data) {
            if (data) {
              alert("审核成功！");
              self.window.close();
              opener.document.form1.submit();
            }else{
              alert("审核失败！");
            }
          }
        });
      }
    }
    function dropSign(id) {
      if (confirm("确认要废弃吗？")) {
        $.ajax({
          type: "POST",
          url: "<c:url value="/dropAuditById.do"/>",
          data: {productId: id},
          async: true,
          dataType: "json",
          success: function (data) {
            if (data) {
              alert("废弃成功！");
              self.window.close();
              opener.document.form1.submit();
            }else{
              alert("废弃失败！");
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
          <tr>
            <td class="lftd">标的ID：</td><td class="ritd"><c:out value="${productDetail.product_id}" /></td>
          </tr>
        <tr>
          <td class="lftd">标的名称：</td><td class="ritd"><c:out value="${productDetail.product_name}" /></td>
        </tr>
        <tr>
          <td class="lftd">借款金额：</td><td class="ritd"><c:out value="${productDetail.invest_amount}" /></td>
        </tr>
        <tr>
          <td class="lftd">筹款用途：</td><td class="ritd"><c:out value="${productDetail.product_usage}" /></td>
        </tr>
        <tr>
          <td class="lftd">年利率：</td><td class="ritd"><c:out value="${productDetail.annual_rate}" />%</td>
        </tr>
        <tr>
          <td class="lftd">借款期限：</td><td class="ritd"><c:out value="${productDetail.loan_period}" />个月</td>
        </tr>
        <tr>
          <td class="lftd">还款方式：</td><td class="ritd">
          <c:if test="${productDetail.repay_type==1}">等本等息</c:if>
          <c:if test="${productDetail.repay_type==2}">一次付息到期还本</c:if>
          <c:if test="${productDetail.repay_type==3}">按月付息到期还本</c:if>
          <c:if test="${productDetail.repay_type==4}">等额本息</c:if>
          <c:if test="${productDetail.repay_type==5}">等本等息（集团贷）</c:if>
          </td>
        </tr>
        <tr>
          <td class="lftd">筹标期限：</td><td class="ritd"><c:out value="${productDetail.tender_day}" />天</td>
        </tr>
        <tr>
          <td class="lftd">借款类型：</td><td class="ritd">
          <c:if test="${productDetail.loan_type==0}">个人借款</c:if>
          <c:if test="${productDetail.loan_type==1}">公司借款</c:if></td>
        </tr>
        <tr>
          <td class="lftd">借款时间：</td><td class="ritd"><f:formatDate value="${productDetail.create_time}" pattern="yyyy-MM-dd"/></td>
        </tr>
        <tr>
          <td colspan="2">&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2" style="text-align: center;padding-bottom: 5px;"><input type="button" value="审核" onclick="auditSign('<c:out value="${productDetail.product_id}"/>')" />&nbsp;&nbsp;
            <input type="button" value="废弃" onclick="dropSign('<c:out value="${productDetail.product_id}"/>')" />&nbsp;&nbsp;<input type="button" value="取消" onclick="window.close();" /></td>
        </tr>
        </table>
  </div>
  </div>
</body>
</html>
