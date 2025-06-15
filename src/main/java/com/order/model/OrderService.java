package com.order.model;

import java.util.List;
import java.time.LocalDateTime;

import com.order.model.OrderVO;

public class OrderService {
	
	private OrderDAO_interface dao;

	public OrderService() {
		dao = new OrderJDBCDAO();
	}
	
	public OrderVO addOrder(Integer memNo, Integer couponWalletNo, String orderStatus,
			Integer orderTotal, Integer pointUsed) {
		
		OrderVO orderVO = new OrderVO();
		
		orderVO.setMemNo(memNo);
		orderVO.setCouponWalletNo(couponWalletNo);
		orderVO.setOrderDatetime(LocalDateTime.now());
		orderVO.setOrderStatus(orderStatus);
		orderVO.setOrderTotal(orderTotal);
		orderVO.setPointUsed(pointUsed);
		dao.insert(orderVO);
		
		return orderVO;
	}
	
	public OrderVO updateOrder(Integer orderNo, Integer memNo, Integer couponWalletNo, String orderStatus,
			Integer orderTotal, Integer pointUsed) {

		OrderVO orderVO = new OrderVO();
		
		orderVO.setOrderNo(orderNo);
		orderVO.setMemNo(memNo);
		orderVO.setCouponWalletNo(couponWalletNo);
		
		OrderVO originalOrder = dao.findByPrimaryKey(orderNo);
		orderVO.setOrderDatetime(originalOrder.getOrderDatetime());  //避免更新後，訂購時間被修改了
		
		orderVO.setOrderStatus(orderStatus);
		orderVO.setOrderTotal(orderTotal);
		orderVO.setPointUsed(pointUsed);
		dao.update(orderVO);
		
		return orderVO;
	
	}
	
	public void deleteOrder(Integer orderNo) {
		dao.delete(orderNo);
	}
	
	public OrderVO getOneOrder(Integer orderNo) {
		return dao.findByPrimaryKey(orderNo);
	}
	
	public List<OrderVO> getAll() {
		return dao.getAll();
	}
	
	// 新增：根據會員編號查詢訂單
	public List<OrderVO> getOrdersByMemNo(Integer memNo) {
		return dao.getOrdersByMemNo(memNo);
	}
	
}