<%--
  Created by IntelliJ IDEA.
  User: lenovo
  Date: 2014/11/26
  Time: 10:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Test01</title>
</head>
<body>
<script type="text/javascript" src="js/jquery-1.11.1.js" ></script>
<script type="text/javascript" >
  function query(a){
    $.ajax({
      type:"GET",
      url:"http://localhost:8080/jcyt_hhn/test01",
      dataType:"json",
      data:"id="+a,
      success: function (data) {
        alert(data);
      }
    })
  }
</script>
<button id="cart" name="cartTest" onclick="query(1)">查看数据</button>
</body>
</html>
