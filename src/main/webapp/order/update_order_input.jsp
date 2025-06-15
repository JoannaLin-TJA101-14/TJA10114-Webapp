<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="com.order.model.*"%>

<% 
   OrderVO orderVO = (OrderVO) request.getAttribute("orderVO");
%>
--<%= orderVO==null %>--${orderVO.memNo}-- 
<html>
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
<title>訂單資料修改 - update_order_input.jsp</title>

<style>
  table#table-1 {
	background-color: #CCCCFF;
    border: 2px solid black;
    text-align: center;
  }
  table#table-1 h4 {
    color: red;
    display: block;
    margin-bottom: 1px;
  }
  h4 {
    color: blue;
    display: inline;
  }
</style>

<style>
  table {
	width: 450px;
	background-color: white;
	margin-top: 1px;
	margin-bottom: 1px;
  }
  table, th, td {
    border: 0px solid #CCCCFF;
  }
  th, td {
    padding: 1px;
  }
</style>

</head>
<body bgcolor='white'>

<table id="table-1">
	<tr><td>
		 <h3>訂單資料修改 - update_order_input.jsp</h3>
		 <h4><a href="select_page.jsp"><img src="images/team_members.png" width="100" height="32" border="0">回首頁</a></h4>
	</td></tr>
</table>

<h3>資料修改:</h3>

<%-- 錯誤表列 --%>
<c:if test="${not empty errorMsgs}">
	<font style="color:red">請修正以下錯誤:</font>
	<ul>
		<c:forEach var="message" items="${errorMsgs}">
			<li style="color:red">${message}</li>
		</c:forEach>
	</ul>
</c:if>

<FORM METHOD="post" ACTION="order.do" name="form1">
<table>
	<tr>
		<td>訂單編號:<font color=red><b>*</b></font></td>
		<td><%=orderVO.getOrderNo()%></td>
	</tr>
	<tr>
		<td>會員編號:</td>
		<td><input type="TEXT" name="memNo" value="<%=orderVO.getMemNo()%>" size="45"/></td>
	</tr>
	<tr>
		<td>優惠票夾代碼:</td>
		<td><input type="TEXT" name="couponWalletNo"   value="<%=orderVO.getCouponWalletNo()%>" size="45"/></td>
	</tr>
	
	<tr>
    	<td>訂單日期時間:</td>
    	<td><%=orderVO.getOrderDatetime().format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))%></td>
	</tr>
	
	<tr>
		<td>訂購狀態:</td>
		<td>
		<select name="orderStatus" id="orderStatus">
		<option value="">請選擇狀態</option>
		<option value="已完成" <%= "已完成".equals(orderVO.getOrderStatus()) ? "selected" : "" %>>已完成</option>
		<option value="處理中" <%= "處理中".equals(orderVO.getOrderStatus()) ? "selected" : "" %>>處理中</option>
		<option value="已取消" <%= "已取消".equals(orderVO.getOrderStatus()) ? "selected" : "" %>>已取消</option>
		<option value="已出貨" <%= "已出貨".equals(orderVO.getOrderStatus()) ? "selected" : "" %>>已出貨</option>
		</select>
		</td>
	</tr>
	
	
	<tr>
		<td>訂單總額:</td>
		<td><input type="TEXT" name="orderTotal"   value="<%=orderVO.getOrderTotal()%>" size="45"/></td>
	</tr>
	<tr>
		<td>使用積分:</td>
		<td><input type="TEXT" name="pointUsed"  value="<%=orderVO.getPointUsed()%>" size="45"/></td>
	</tr>

	

</table>
<br>
<input type="hidden" name="action" value="update">
<input type="hidden" name="orderNo" value="<%=orderVO.getOrderNo()%>">
<input type="submit" value="送出修改"></FORM>
</body>



<!-- =========================================以下為 datetimepicker 之相關設定========================================== -->

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/datetimepicker/jquery.datetimepicker.css" />
<script src="<%=request.getContextPath()%>/datetimepicker/jquery.js"></script>
<script src="<%=request.getContextPath()%>/datetimepicker/jquery.datetimepicker.full.js"></script>

<style>
  .xdsoft_datetimepicker .xdsoft_datepicker {
           width:  300px;   /* width:  300px; */
  }
  .xdsoft_datetimepicker .xdsoft_timepicker .xdsoft_time_box {
           height: 151px;   /* height:  151px; */
  }
</style>


</html>