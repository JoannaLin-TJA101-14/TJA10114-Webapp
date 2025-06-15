package com.order.controller;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


import jakarta.servlet.*;
import jakarta.servlet.http.*;

import com.order.model.OrderService;
import com.order.model.OrderVO;
import com.emp.model.EmpService;
import com.emp.model.EmpVO;
import com.order.model.*;

public class OrderServlet extends HttpServlet {
	
	public void doGet(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {
		doPost(req, res);
	}
	
	public void doPost(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		req.setCharacterEncoding("UTF-8");
		String action = req.getParameter("action");
	
		/* ======================================== 新增 ========================================*/
		if ("insert".equals(action)) {  //來自addOrder.jsp的請求
			List<String> errorMsgs = new LinkedList<String>();
			// 將此集合儲存在請求範圍中，以防我們需要傳送ErrorPage。
			req.setAttribute("errorMsgs", errorMsgs);
			
			/*************1.接收請求參數 - 輸入格式的錯誤處理*************/
			Integer orderNo = null; // 讓系統自動產生
			
			
			
			Integer memNo = null;
			String memNoStr = req.getParameter("memNo");
			if (memNoStr == null || memNoStr.trim().isEmpty()) {
			    errorMsgs.add("會員編號請勿空白.");
			} else {
			    try {
			        memNo = Integer.valueOf(memNoStr.trim());
			        if (memNo <= 1) {
			            errorMsgs.add("會員編號必須大於 1.");
			        }
			    } catch (NumberFormatException e) {
			        errorMsgs.add("會員編號請填數字.");
			    }
			}
			
			
			Integer couponWalletNo = null; //預設為沒有使用優惠券，不會有錯誤題是
			String couponWalletNoStr = req.getParameter("couponWalletNo");

			if (couponWalletNoStr != null && !couponWalletNoStr.trim().isEmpty()) {
			    try {
			        couponWalletNo = Integer.valueOf(couponWalletNoStr.trim());
			    } catch (NumberFormatException e) {
			        errorMsgs.add("優惠券票夾請填數字");
			    }
			}
			
			
			String orderStatus = req.getParameter("orderStatus").trim();
			if (orderStatus == null || orderStatus.trim().length() == 0) {
				errorMsgs.add("請選擇狀態");
			}
			
			
			Integer orderTotal = null;
			String orderTotalStr = req.getParameter("orderTotal");

			if (orderTotalStr == null || orderTotalStr.trim().isEmpty()) {
			    errorMsgs.add("金額請勿空白");
			} else {
			    try {
			        orderTotal = Integer.valueOf(orderTotalStr.trim());
			        if (orderTotal <= 0) {
			            errorMsgs.add("金額必須大於 0");
			        }
			    } catch (NumberFormatException e) {
			        errorMsgs.add("金額請填數字");
			    }
			}
			
			
			Integer pointUsed = 0;  //積分使用設定為空值就預設為0，不會有錯誤提醒
			String pointUsedStr = req.getParameter("pointUsed");

			if (pointUsedStr != null && !pointUsedStr.trim().isEmpty()) {
			    try {
			        pointUsed = Integer.valueOf(pointUsedStr.trim());
			    } catch (NumberFormatException e) {
			        pointUsed = 0;
			        errorMsgs.add("使用積分請填數字");
			    }
			}

			
			OrderVO orderVO = new OrderVO();
			orderVO.setOrderNo(orderNo);
			orderVO.setMemNo(memNo);
			orderVO.setCouponWalletNo(couponWalletNo);
			orderVO.setOrderDatetime(LocalDateTime.now());  // 自動設置為當前時間
			orderVO.setOrderStatus(orderStatus);
			orderVO.setOrderTotal(orderTotal);
			orderVO.setPointUsed(pointUsed);
			
			// 如果有錯誤，則回傳至表單(form)
			if (!errorMsgs.isEmpty()) {
				req.setAttribute("orderVO", orderVO); // 含有輸入格式錯誤的orderVO物件,也存入req
				RequestDispatcher failureView = req
						.getRequestDispatcher("/order/addOrder.jsp");
				failureView.forward(req, res);
				return;
			}
						
			/*********************2.開始新增資料*********************/
			OrderService orderSvc = new OrderService();
			orderVO = orderSvc.addOrder(memNo, couponWalletNo, orderStatus, orderTotal, pointUsed);

			
			/********3.新增完成,準備轉交(Send the Success view)********/
			String url = "/order/listAllOrder.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);  // 新增成功後轉交listAllOrder.jsp
			successView.forward(req, res);
			
		}
		
		/* ======================================== 修改 ========================================*/
		if ("getOne_For_Update".equals(action)) { // 來自listAllOrder.jsp的請求

			List<String> errorMsgs = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs);
		
			/*********************1.接收請求參數*********************/
			Integer orderNo = Integer.valueOf(req.getParameter("orderNo"));
		
			/*********************2.開查詢除資料*********************/
			OrderService orderSvc = new OrderService();
			OrderVO orderVO = orderSvc.getOneOrder(orderNo);
			
			if (orderVO == null) {
			    errorMsgs.add("查無此訂單編號：" + orderNo);
			    RequestDispatcher failureView = req.getRequestDispatcher("/order/select_page.jsp");
			    failureView.forward(req, res);
			    return; 
			}
		
			/********3.查詢完成,準備轉交(Send the Success view)********/
			req.setAttribute("orderVO", orderVO);         // 資料庫取出的orderVO物件,存入req
			String url = "/order/update_order_input.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url);// 成功轉交 update_order_input.jsp
			successView.forward(req, res);
		}
		
		/* ======================================== 查詢 & 更新 ========================================*/
		if ("update".equals(action)) { // 來自update_order_input.jsp的請求
			
			List<String> errorMsgs = new LinkedList<String>();
			req.setAttribute("errorMsgs", errorMsgs);
	
			
			/*********************1.接收請求參數*********************/
			Integer orderNo = Integer.valueOf(req.getParameter("orderNo").trim());
			
			Integer memNo = null;
			String memNoStr = req.getParameter("memNo");
			if (memNoStr == null || memNoStr.trim().isEmpty()) {
			    errorMsgs.add("會員編號請勿空白.");
			} else {
			    try {
			        memNo = Integer.valueOf(memNoStr.trim());
			        if (memNo <= 1) {
			            errorMsgs.add("會員編號必須大於 1.");
			        }
			    } catch (NumberFormatException e) {
			        errorMsgs.add("會員編號請填數字.");
			    }
			}
			
			
			Integer couponWalletNo = null; //預設為沒有使用優惠券，不會有錯誤題是
			String couponWalletNoStr = req.getParameter("couponWalletNo");
			if (couponWalletNoStr != null && !couponWalletNoStr.trim().isEmpty()) {
			    try {
			        couponWalletNo = Integer.valueOf(couponWalletNoStr.trim());
			    } catch (NumberFormatException e) {
			        errorMsgs.add("優惠券票夾請填數字");
			    }
			}
			
			
			String orderStatus = req.getParameter("orderStatus").trim();
			if (orderStatus == null || orderStatus.trim().length() == 0) {
				errorMsgs.add("請選擇狀態");
			}
			
			
			Integer orderTotal = null;
			String orderTotalStr = req.getParameter("orderTotal");
			if (orderTotalStr == null || orderTotalStr.trim().isEmpty()) {
			    errorMsgs.add("金額請勿空白");
			} else {
			    try {
			        orderTotal = Integer.valueOf(orderTotalStr.trim());
			        if (orderTotal <= 0) {
			            errorMsgs.add("金額必須大於 0");
			        }
			    } catch (NumberFormatException e) {
			        errorMsgs.add("金額請填數字");
			    }
			}
			
			
			Integer pointUsed = 0;  //積分使用設定為空值就預設為0，不會有錯誤提醒
			String pointUsedStr = req.getParameter("pointUsed");

			if (pointUsedStr != null && !pointUsedStr.trim().isEmpty()) {
			    try {
			        pointUsed = Integer.valueOf(pointUsedStr.trim());
			    } catch (NumberFormatException e) {
			        pointUsed = 0;
			        errorMsgs.add("使用積分請填數字");
			    }
			}
			
			// 如果有錯誤，需要重新取得原始訂單資料來顯示
		    if (!errorMsgs.isEmpty()) {
		        OrderService orderSvc = new OrderService();
		        OrderVO originalOrderVO = orderSvc.getOneOrder(orderNo); // 取得原始資料

			
			OrderVO orderVO = new OrderVO();
			orderVO.setOrderNo(orderNo);
			orderVO.setMemNo(memNo);
			orderVO.setCouponWalletNo(couponWalletNo);
			orderVO.setOrderDatetime(originalOrderVO.getOrderDatetime()); // 保持原始時間
			orderVO.setOrderStatus(orderStatus);
			orderVO.setOrderTotal(orderTotal);
			orderVO.setPointUsed(pointUsed);						
			
			// Send the use back to the form, if there were errors
			
				req.setAttribute("orderVO", orderVO); // 含有輸入格式錯誤的orderVO物件,也存入req
				RequestDispatcher failureView = req
						.getRequestDispatcher("/order/update_order_input.jsp");
				failureView.forward(req, res);
				return; //程式中斷
			}
			
			/*********************2.開始查詢資料*********************/
			
			OrderService orderSvc = new OrderService();
			OrderVO orderVO = orderSvc.updateOrder(orderNo, memNo, couponWalletNo, orderStatus, orderTotal, pointUsed);
			
			
			/********3.查詢完成,準備轉交(Send the Success view)********/
			req.setAttribute("orderVO", orderVO); // 資料庫update成功後,正確的的orderVO物件,存入req
			String url = "/order/listOneOrder.jsp";
			RequestDispatcher successView = req.getRequestDispatcher(url); // 修改成功後,轉交listOneOrder.jsp
			successView.forward(req, res);
			
		}	
		/* ======================================== 單一查詢  getOne_For_Display ====================================*/
		if ("getOne_For_Display".equals(action)) { // 來自select_page.jsp的請求

			List<String> errorMsgs1 = new LinkedList<String>();
			// Store this set in the request scope, in case we need to
			// send the ErrorPage view.
			req.setAttribute("errorMsgs", errorMsgs1);
				
			/*********************1.接收請求參數*********************/
			String str = req.getParameter("orderNo");
			if (str == null || (str.trim()).length() == 0) {
				errorMsgs1.add("請輸入訂單編號");
			}
			// Send the use back to the form, if there were errors
			if (!errorMsgs1.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/order/select_page.jsp");
				failureView.forward(req, res);
				return;//程式中斷
			}
			
			Integer orderNo1 = null;
			try {
				orderNo1 = Integer.valueOf(str);
			} catch (Exception e) {
				errorMsgs1.add("訂單編號格式不正確");
			}
			
			
			// Send the use back to the form, if there were errors
			if (!errorMsgs1.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/order/select_page.jsp");
				failureView.forward(req, res);
				return;//程式中斷
			}
			
			/*********************2.開始查詢資料*********************/
			OrderService orderSvc1 = new OrderService();
			OrderVO orderVO1 = orderSvc1.getOneOrder(orderNo1);
			if (orderVO1 == null) {
				errorMsgs1.add("查無資料");
			}
			// Send the use back to the form, if there were errors
			if (!errorMsgs1.isEmpty()) {
				RequestDispatcher failureView = req
						.getRequestDispatcher("/order/select_page.jsp");
				failureView.forward(req, res);
				return;//程式中斷
			}
			
			/********3.查詢完成,準備轉交(Send the Success view)********/	
			req.setAttribute("orderVO", orderVO1); // 資料庫取出的orderVO物件,存入req
			String url1 = "/order/listOneOrder.jsp";
			RequestDispatcher successView1 = req.getRequestDispatcher(url1); // 成功轉交 listOneOrder.jsp
			successView1.forward(req, res);
		}
	
		
		/* ======================================== 單一查詢  getOne_By_MemNo ====================================*/
		
		if ("getOne_By_MemNo".equals(action)) {
		    List<String> errorMsgs = new LinkedList<String>();
		    req.setAttribute("errorMsgs", errorMsgs);
		    
		    /*********************1.接收請求參數*********************/
		    String memNoStr = req.getParameter("memNo");
		    if (memNoStr == null || (memNoStr.trim()).length() == 0) {
		        errorMsgs.add("請輸入會員編號"); // 正確的錯誤訊息
		    }
		    
		    if (!errorMsgs.isEmpty()) {
		        RequestDispatcher failureView = req
		            .getRequestDispatcher("/order/select_page.jsp");
		        failureView.forward(req, res);
		        return;
		    }
		    
		    Integer memNo = null;
		    try {
		        memNo = Integer.valueOf(memNoStr);
		    } catch (Exception e) {
		        errorMsgs.add("會員編號格式不正確");
		    }
		    
		    if (!errorMsgs.isEmpty()) {
		        RequestDispatcher failureView = req
		            .getRequestDispatcher("/order/select_page.jsp");
		        failureView.forward(req, res);
		        return;
		    }
		    
		    /*********************2.開始查詢資料*********************/
		    OrderService orderSvc = new OrderService();
		    List<OrderVO> orderList = orderSvc.getOrdersByMemNo(memNo); // 需要這個方法
		    
		    if (orderList == null || orderList.isEmpty()) {
		        errorMsgs.add("查無該會員的訂單資料");
		        RequestDispatcher failureView = req
		            .getRequestDispatcher("/order/select_page.jsp");
		        failureView.forward(req, res);
		        return;
		    }
		    /********3.查詢完成,準備轉交(Send the Success view)********/	
		    req.setAttribute("orderList", orderList);
		    String url = "/order/listOrdersByMember.jsp"; // 需要建立這個頁面
		    RequestDispatcher successView = req.getRequestDispatcher(url);
		    successView.forward(req, res);
		}
		
		
		/* ======================================== 刪除 ========================================*/
		if ("delete".equals(action)) {   //來自listAllOrder.jsp
			List<String> errorMsgs1 = new LinkedList<String>();
			// 將此集合儲存在請求範圍內，以便在需要時傳送ErrorPage。
			req.setAttribute("errorMsgs", errorMsgs1);
			
			/*********************1.接收請求參數*********************/
			Integer orderNo1 = Integer.valueOf(req.getParameter("orderNo"));
			
			/*********************2.開始刪除資料*********************/
			OrderService orderSvc1 = new OrderService();
			orderSvc1.deleteOrder(orderNo1);
			
			/********3.刪除完成,準備轉交(Send the Success view)********/
			String url1 = "/order/listAllOrder.jsp";
			RequestDispatcher successView1 = req.getRequestDispatcher(url1); // 刪除成功後,轉交回送出刪除的來源網頁
			successView1.forward(req, res);
		}
		
	}

}