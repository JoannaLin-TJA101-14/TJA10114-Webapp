<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.order.model.*"%>
<%-- 此頁暫練習採用 Script 的寫法取值 --%>

<%
  OrderVO orderVO = (OrderVO) request.getAttribute("orderVO"); //OrderServlet.java(Concroller), 存入req的orderVO物件
%>

<html>
<head>
<title>訂單資料 - listOneOrder.jsp</title>

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
	width: 600px;
	background-color: white;
	margin-top: 5px;
	margin-bottom: 5px;
  }
  table, th, td {
    border: 1px solid #CCCCFF;
  }
  th, td {
    padding: 5px;
    text-align: center;
  }
</style>

</head>
<body bgcolor='white'>

<h4>此頁暫練習採用 Script 的寫法取值:</h4>
<table id="table-1">
	<tr><td>
		 <h3>訂單資料 - listOneOrder.jsp</h3>
		 <h4><a href="select_page.jsp"><img src="images/team_members.png" width="100" height="32" border="0">回首頁</a></h4>
	</td></tr>
</table>

<table>
	<tr>
		<th>訂單編號</th>
		<th>會員編號</th>
		<th>優惠票夾代碼</th>
		<th>訂購時間</th>
		<th>訂購狀態</th>
		<th>訂單總額</th>
		<th>使用積分</th>
	</tr>
	<tr>
		<td><%=orderVO.getOrderNo()%></td>
		<td><%=orderVO.getMemNo()%></td>
		<td><%=orderVO.getCouponWalletNo()%></td>
		<td><%=orderVO.getOrderDatetime()%></td>
		<td><%=orderVO.getOrderStatus()%></td>
		<td><%=orderVO.getOrderTotal()%></td>
		<td><%=orderVO.getPointUsed()%></td>
	</tr>
</table>

</body>
</html>