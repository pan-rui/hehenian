<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page pageEncoding="UTF-8" language="java" %>
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
  <script type="text/javascript" src="<c:url value='/js/template.js'/>"></script>
  <script type="text/javascript">
    var g_CurrentPage = 1;
    var g_PageCount = 6;
    $(function(){
      getTransactiondetails();
    })
    /***********提现请求*******************/
    function withdrawCash(){
      var stParam = [];
      stParam.push("userId=1");
      stParam.push("amount=1000");
      ajax({
        url:"<%=request.getContextPath() %>/doWithdraw.do",
        type:'post',//非必须.默认get
        data:stParam.join("&"),
        dataType:'json',//非必须.默认text
        async:true,//非必须.默认true
        cache:false,//非必须.默认false
        timeout:30000,//非必须.默认30秒
        success:withdrawCashSuccess//非必须
      });
    }
    function withdrawCashSuccess(data){
      if(data.returnCode != 0){
        alert(data.messageInfo);return;
      }else{
        alert("提现成功！");
        document.location.reload();
      }
    }
    /***********获得交易列表*******************/
    function getTransactiondetails(){
      var stParam = [];
      stParam.push("pageNow="+g_CurrentPage);
      stParam.push("pageSize="+g_PageCount);
      ajax({
        url:"<%=request.getContextPath() %>/getProduct.do",
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
      if(data.returnCode == 1){
        alert(data.messageInfo);return;
      }else if(data.returnCode==2){
        self.location.href = data.data;
      }
      var st = data.data.productList;//list
      var iTotal = data.data.totalCount;//总数
      var iPage = parseInt(data.data.pageNow);//当前页
      g_CurrentPage = iPage;
      setPageInfo(iTotal);
      var htm ="";
      if(st!=null) {
        for (var idx = 0; idx<st.length; idx++) {
          if (st[idx] != '' && st[idx] != 'undefined') {
            htm += '<tr><td' + st[idx].product_id + '</td>' +
            '<td>' + st[idx].product_id + '</td>' +
            '<td>' + st[idx].product_name + '</td>' +
            '<td>￥' + st[idx].trade_amount + '</td>' +
            '<td>' + new Date(st[idx].invest_time).Format("yyyy-MM-dd hh:mm:ss") + '</td>'+
            '<td>' + st[idx].annual_rate + '%</td>' +
            '<td>￥' + st[idx].receiveAmount + '</td>';
          }
        }
      }else{
        htm += '<tr><td colspan="6" align="center">查无记录</td></tr>';
      }
      $("#listBox").html(htm);
    }
    /************************************ 分页 **************************************/
    function goPage(page){
      g_CurrentPage = page;
      getTransactiondetails();
    }

    function setPageInfo(allCount){
      var pageSum=Math.ceil(allCount/g_PageCount);
      var start=1;
      var end=0;
      var upPageStyle="";
      var downPageStyle="";
      var stHTML=[];
      if(g_CurrentPage>1){
        upPageStyle="visibility:visible";
        $('#upPageButton').show();
      }else {
        upPageStyle="visibility:hidden";
        $('#upPageButton').hide();
      }
      if(pageSum==g_CurrentPage){
        downPageStyle="visibility:hidden";
        $('#downPageButton').hide();
      }else {
        downPageStyle="visibility:visible";
        $('#downPageButton').show();
      }
      if(g_CurrentPage>4)start=g_CurrentPage-4;
      if(start+8<=pageSum)end=start+8;
      else {
        end=pageSum;
        start=end-8;
      }
      if(start<=0)start=1;
      stHTML.push('<span class="pageStyle" style="');
      stHTML.push(upPageStyle);
      stHTML.push('" onclick="goPage(--g_CurrentPage)"><a href="javascript:void(0)">上一页</a></span>');
      var stDetail=[]
      for(var i=start;i<=end;i++){
        var sClick=' onclick="goPage('+i+')" ';
        var sClass='';
        var sNum=i;
        if(i==g_CurrentPage)sClass=' onPage';
        stDetail.push('<span '+sClick+' class="pageStyle'+sClass+'" ><a href="javascript:void(0)">'+sNum+'</a></span>');
      }
      //显示开始和结束页码
      if(stDetail.length==9){
        if(start>1){
          stDetail[0]='<span class="pageStyle" onclick="goPage(1)"><a href="javascript:void(0)">1</a></span>';
          stDetail[1]='<span class="pageStyle"><a href="javascript:void(0)">...</a></span>';
        }
        if(end<pageSum){
          stDetail[8]='<span class="pageStyle" onclick="goPage('+pageSum+')"><a href="javascript:void(0)">'+pageSum+'</a></span>';
          stDetail[7]='<span class="pageStyle"><a href="javascript:void(0)">...</a></span>';
        }
      }
      stHTML.push(stDetail.join(''));
      stHTML.push('<span class="pageStyle" style="');
      stHTML.push(downPageStyle);
      stHTML.push('" onclick="goPage(++g_CurrentPage)"><a href="javascript:void(0)">下一页</a></span>');
      if(pageSum<2)$('#pageBox').hide();
      else $('#pageBox').html(stHTML.join('')).show();
    }
  </script>
</head>
<body>
<jsp:include page="./include/top.jsp" flush="true" />
<div class="user-all transactionDetails">
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
          <p style="font-size:20px;color:#666666;line-height:50px">投资记录</p>
          <div class="biaoge" id="biaoge_details">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
              <thead>
              <tr>
                <th>标的编号</th>
                <th>标的名称</th>
                <th>交易金额</th>
                <th>交易时间</th>
                <th>利率</th>
                <th>预期收益</th>
              </tr>
              </thead>
              <tbody id="listBox">
              </tbody>
            </table>
            <div class="proPage bProPage" id="pageBox" style="display:none">
              <span class="pageStyle" style="visibility:visible"><a href="javascript:void(0)">上一页</a></span>
              <span class="pageStyle"><a href="javascript:void(0)">1</a></span>
              <span class="pageStyle"><a href="javascript:void(0)">2</a></span>
              <span class="pageStyle onPage"><a href="javascript:void(0)">3</a></span>
              <span class="pageStyle"><a href="javascript:void(0)">4</a></span>
              <span class="pageStyle"><a href="javascript:void(0)">5</a></span>
              <span class="pageStyle"><a href="javascript:void(0)">6</a></span>
              <span class="pageStyle"><a href="javascript:void(0)">7</a></span>
              <span class="pageStyle"><a href="javascript:void(0)">...</a></span>
              <span class="pageStyle"><a href="javascript:void(0)">43</a></span>
              <span class="pageStyle" style="visibility:visible"><a href="javascript:void(0)">下一页</a></span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>
<jsp:include page="./include/footer.jsp" flush="true" />
</body>
</html>