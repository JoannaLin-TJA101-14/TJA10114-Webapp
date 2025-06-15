package com.order.model;

import java.util.*;

public interface OrderDAO_interface{
	public void insert(OrderVO orderVO);
	public void update(OrderVO orderVO);
	public void delete(Integer orderNo);
	public OrderVO findByPrimaryKey(Integer orderNo);
	public List<OrderVO> getAll();
	public List<OrderVO> getOrdersByMemNo(Integer memNo);
}