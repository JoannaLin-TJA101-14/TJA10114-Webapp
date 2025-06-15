<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="java.util.*"%>
<%@ page import="com.order.model.*"%>
<%-- 此頁練習採用 EL 的寫法取值 --%>

<%
    OrderService orderSvc = new OrderService();
    List<OrderVO> list = orderSvc.getAll();
    pageContext.setAttribute("list",list);
%>


<html>
<head>
<title>所有訂單資料 - listAllOrder.jsp</title>

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
	width: 800px;
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

<h4>此頁練習採用 EL 的寫法取值:</h4>
<table id="table-1">
	<tr><td>
		 <h3>所有訂單資料 - listAllOrder.jsp</h3>
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
		<th>修改</th>
		<th>刪除</th>
	</tr>
	<%@ include file="page1.file" %> 
	<c:forEach var="orderVO" items="${list}" begin="<%=pageIndex%>" end="<%=pageIndex+rowsPerPage-1%>">
		
		<tr>
			<td>${orderVO.orderNo}</td>
			<td>${orderVO.memNo}</td>
			<td>${orderVO.couponWalletNo}</td>
			<td>${orderVO.orderDatetime}</td>
			<td>${orderVO.orderStatus}</td>
			<td>${orderVO.orderTotal}</td> 
			<td>${orderVO.pointUsed}</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/order/order.do" style="margin-bottom: 0px;">
			     <input type="submit" value="修改">
			     <input type="hidden" name="orderNo"  value="${orderVO.orderNo}">
			     <input type="hidden" name="action"	value="getOne_For_Update"></FORM>
			</td>
			<td>
			  <FORM METHOD="post" ACTION="<%=request.getContextPath()%>/order/order.do" style="margin-bottom: 0px;">
			     <input type="submit" value="刪除">
			     <input type="hidden" name="orderNo"  value="${orderVO.orderNo}">
			     <input type="hidden" name="action" value="delete"></FORM>
			</td>
		</tr>
	</c:forEach>
</table>
<%@ include file="page2.file" %>

</body>
</html>