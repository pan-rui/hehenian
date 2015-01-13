<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2014/12/2
  Time: 16:38
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>标的导入</title>
  <link rel=stylesheet type=text/css href="<c:url value='/css/css.css'/>" />
  <script type=text/javascript src="<c:url value="/js/jquery-1.9.1.min.js"/>"></script>
  <script type="text/javascript">
    function upload(){
      var file=$("#file");
      if($.trim(file.val())==''){
        alert("请选择EXCEL文件！");
        return;
      }
      $("#form1").submit();
    }
  </script>
</head>
<body>
<div id=right>
  <div style="padding:15px 10px 0px 10px;">
    <div>
      <table border=0 cellspacing=0 cellpadding=0 width="100%">
        <tbody>
        <tr>
          <td class=main_alll_h2 height=28 width=120><a href="#">标的导入</a></td>
          <td width=2>&nbsp; </td>
          <td>&nbsp; </td>
        </tr>
        </tbody>
      </table>
      <div style="padding: 10px 10px; background-color: #fff;width: 1120px;">
        <form name="form1" id="form1" action="<c:url value="/exportSign.do"/>" method="post" enctype="multipart/form-data">
          借款人资料：<input type="file" name="file" id="file" title="请选择文件" />
          <!-- 借款人抵押资料：<input type="file" name="file" id="file" title="请选择文件" /><br/>
          借款项目资料：<input type="file" name="file" id="file" title="请选择文件" /><br/>
          借款人银行资料：<input type="file" name="file" id="file" title="请选择文件" /><br/> -->
          <input type="button" value="上传" onclick="upload()" />
        </form>
        </div>
      <c:if test="${not empty failRow}">
        <div id="dataInfo"><font color="red">第<c:out value="${failRow}" />行解析错误!</font></div>
      </c:if>
      <c:if test="${not empty existRow1}">
      <div id="dataInfo"><font color="red">
        <c:forEach items="${existRow1}" var="row">
          <c:out value="${row}" />,
        </c:forEach></font>
        </div>
      </c:if>
      <c:if test="${resultCode=='Y'}">
        <div id="dataInfo"><font color="red"><c:out value="${resultMsg}" /></font><br/>
        <c:if test="${not empty existRow2}">
          <c:forEach items="${existRow2}" var="list">
            <font><c:out value="${list}" /></font><br/>
          </c:forEach>
        </c:if>
        </div>
      </c:if>
      <c:if test="${resultCode=='N'}">
        <div id="dataInfo"><font color="red"><c:out value="${resultMsg}" /></font></div>
      </c:if>
      </div>
    </div>
  </div>
</body>
</html>