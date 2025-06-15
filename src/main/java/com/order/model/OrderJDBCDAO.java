package com.order.model;

import java.util.*;

import com.order.model.OrderJDBCDAO;
import com.order.model.OrderVO;

import java.sql.*;
import java.time.LocalDateTime;

public class OrderJDBCDAO implements OrderDAO_interface {
	String driver = "com.mysql.cj.jdbc.Driver";
	String url = "jdbc:mysql://localhost:3306/pixel_tribe?serverTimezone=Asia/Taipei";
	String userid = "root";
	String passwd = "123456";
	
	private static final String INSERT_STMT =
			"INSERT INTO `ORDER` (MEM_NO,COUPON_WALLET_NO,ORDER_DATETIME,ORDER_STATUS,ORDER_TOTAL,POINT_USED) VALUES (?, ?, ?, ?, ?, ?)";
	private static final String GET_ALL_STMT =
			"SELECT ORDER_NO AS orderNo, MEM_NO AS memNo, COUPON_WALLET_NO AS couponWalletNo, ORDER_DATETIME AS orderDatetime, ORDER_STATUS AS orderStatus, ORDER_TOTAL AS orderTotal, POINT_USED AS pointUsed FROM `ORDER` ORDER BY ORDER_NO";
	private static final String GET_ONE_STMT =
			"SELECT ORDER_NO AS orderNo, MEM_NO AS memNo, COUPON_WALLET_NO AS couponWalletNo, ORDER_DATETIME AS orderDatetime, ORDER_STATUS AS orderStatus, ORDER_TOTAL AS orderTotal, POINT_USED AS pointUsed FROM `ORDER` WHERE ORDER_NO = ?";
	private static final String DELETE =
			"DELETE FROM `ORDER` where ORDER_NO = ?";
	private static final String UPDATE =
			"UPDATE `ORDER` set MEM_NO=?, COUPON_WALLET_NO=?, ORDER_STATUS=?, ORDER_TOTAL=?, POINT_USED=? where ORDER_NO = ?";
	private static final String GET_ORDERS_BY_MEM_NO =
			"SELECT ORDER_NO AS orderNo, MEM_NO AS memNo, COUPON_WALLET_NO AS couponWalletNo, ORDER_DATETIME AS orderDatetime, ORDER_STATUS AS orderStatus, ORDER_TOTAL AS orderTotal, POINT_USED AS pointUsed FROM `ORDER` WHERE MEM_NO = ?";
	
	
	
	@Override
	public void insert(OrderVO orderVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {
			
			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(INSERT_STMT);

			pstmt.setInt(1, orderVO.getMemNo());

			if (orderVO.getCouponWalletNo() != null) {
				pstmt.setInt(2, orderVO.getCouponWalletNo());
			} else {
				pstmt.setNull(2, java.sql.Types.INTEGER); // 若未使用優惠券
				}
			
			pstmt.setTimestamp(3, Timestamp.valueOf(orderVO.getOrderDatetime()));
			pstmt.setString(4, orderVO.getOrderStatus());
			pstmt.setInt(5, orderVO.getOrderTotal());
			pstmt.setInt(6, orderVO.getPointUsed());

			pstmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		
	}

	@Override
	public void update(OrderVO orderVO) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(UPDATE);
			
			pstmt.setInt(1, orderVO.getMemNo());
			
			if (orderVO.getCouponWalletNo() != null) {
			    pstmt.setInt(2, orderVO.getCouponWalletNo());
			} else {
			    pstmt.setNull(2, java.sql.Types.INTEGER);
			}
			
			pstmt.setString(3, orderVO.getOrderStatus());
			pstmt.setInt(4, orderVO.getOrderTotal());
			pstmt.setInt(5, orderVO.getPointUsed());
			pstmt.setInt(6, orderVO.getOrderNo());
			
			pstmt.executeUpdate();
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		
	}

	@Override
	public void delete(Integer orderNo) {
		
		Connection con = null;
		PreparedStatement pstmt = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(DELETE);

			pstmt.setInt(1, orderNo);

			pstmt.executeUpdate();
		
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			
		} finally {
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		
	}

	@Override
	public OrderVO findByPrimaryKey(Integer orderNo) {
		
		OrderVO orderVO = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ONE_STMT);

			pstmt.setInt(1, orderNo);

			rs = pstmt.executeQuery();
			
			while (rs.next()) {
				// orderVO 也稱為 Domain objects
				orderVO = new OrderVO();
				orderVO.setOrderNo(rs.getInt("orderNo"));
				orderVO.setMemNo(rs.getInt("memNo"));
				orderVO.setCouponWalletNo(rs.getInt("couponWalletNo"));
				orderVO.setOrderDatetime(rs.getTimestamp("orderDatetime").toLocalDateTime());
				orderVO.setOrderStatus(rs.getString("orderStatus"));
				orderVO.setOrderTotal(rs.getInt("orderTotal"));
				orderVO.setPointUsed(rs.getInt("pointUsed"));
			}
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return orderVO;
	}

	@Override
	public List<OrderVO> getAll() {
		List<OrderVO> list = new ArrayList<OrderVO>();
		OrderVO orderVO = null;

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			Class.forName(driver);
			con = DriverManager.getConnection(url, userid, passwd);
			pstmt = con.prepareStatement(GET_ALL_STMT);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				// orderVO 也稱為 Domain objects
				orderVO = new OrderVO();
				orderVO.setOrderNo(rs.getInt("orderNo"));
				orderVO.setMemNo(rs.getInt("memNo"));
				orderVO.setCouponWalletNo(rs.getInt("couponWalletNo"));
				orderVO.setOrderDatetime(rs.getTimestamp("orderDatetime").toLocalDateTime());
				orderVO.setOrderStatus(rs.getString("orderStatus"));
				orderVO.setOrderTotal(rs.getInt("orderTotal"));
				orderVO.setPointUsed(rs.getInt("pointUsed"));
				list.add(orderVO); // Store the row in the list
			}
			
			// Handle any driver errors
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Couldn't load database driver. "
					+ e.getMessage());
			// Handle any SQL errors
		} catch (SQLException se) {
			throw new RuntimeException("A database error occured. "
					+ se.getMessage());
			// Clean up JDBC resources
		} finally {
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (pstmt != null) {
				try {
					pstmt.close();
				} catch (SQLException se) {
					se.printStackTrace(System.err);
				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (Exception e) {
					e.printStackTrace(System.err);
				}
			}
		}
		return list;
	}
	
	
	@Override
	public List<OrderVO> getOrdersByMemNo(Integer memNo) {
	    List<OrderVO> list = new ArrayList<OrderVO>();
	    OrderVO orderVO = null;
	    
	    Connection con = null;
	    PreparedStatement pstmt = null;
	    ResultSet rs = null;
	    
	    try {
	        Class.forName(driver);
	        con = DriverManager.getConnection(url, userid, passwd);
	        pstmt = con.prepareStatement(GET_ORDERS_BY_MEM_NO);
	        pstmt.setInt(1, memNo);
	        rs = pstmt.executeQuery();
	        

	        
	        
	        while (rs.next()) {
	            orderVO = new OrderVO();
	            orderVO.setOrderNo(rs.getInt("orderNo"));
	            orderVO.setMemNo(rs.getInt("memNo"));
	            
	            // 處理可能為 NULL 的 couponWalletNo
	            int couponWalletNo = rs.getInt("couponWalletNo");
	            if (rs.wasNull()) {
	                orderVO.setCouponWalletNo(null);
	            } else {
	                orderVO.setCouponWalletNo(couponWalletNo);
	            }
	            
	            orderVO.setOrderDatetime(rs.getTimestamp("orderDatetime").toLocalDateTime());
	            orderVO.setOrderStatus(rs.getString("orderStatus"));
	            orderVO.setOrderTotal(rs.getInt("orderTotal"));
	            orderVO.setPointUsed(rs.getInt("pointUsed"));
	            list.add(orderVO);
	        }
	        
	    } catch (ClassNotFoundException e) {
	        throw new RuntimeException("Couldn't load database driver. " + e.getMessage());
	    } catch (SQLException se) {
	        throw new RuntimeException("A database error occured. " + se.getMessage());
	    } finally {
	        if (rs != null) {
	            try {
	                rs.close();
	            } catch (SQLException se) {
	                se.printStackTrace(System.err);
	            }
	        }
	        if (pstmt != null) {
	            try {
	                pstmt.close();
	            } catch (SQLException se) {
	                se.printStackTrace(System.err);
	            }
	        }
	        if (con != null) {
	            try {
	                con.close();
	            } catch (Exception e) {
	                e.printStackTrace(System.err);
	            }
	        }
	    }
	    return list;
	}
	
	
	
	public static void main(String[] args) {

		OrderJDBCDAO dao = new OrderJDBCDAO();

		// 新增
		OrderVO orderVO1 = new OrderVO();
		orderVO1.setMemNo(2);
		orderVO1.setCouponWalletNo(null);
		orderVO1.setOrderDatetime(LocalDateTime.now());  // 設定為當前時間
		orderVO1.setOrderStatus("處理中");
		orderVO1.setOrderTotal(990);
		orderVO1.setPointUsed(0);
		dao.insert(orderVO1);
		
		// 修改
		OrderVO orderVO2 = new OrderVO();
		orderVO2.setOrderNo(2025060002);
		orderVO2.setMemNo(23);
		orderVO2.setCouponWalletNo(1);
		orderVO2.setOrderDatetime(LocalDateTime.now());  // 設定為當前時間
		orderVO2.setOrderStatus("處理中");
		orderVO2.setOrderTotal(790);
		orderVO2.setPointUsed(10);
		dao.update(orderVO2);
		
		// 刪除
		dao.delete(2025060009);
		
		// 查詢
		OrderVO orderVO3 = dao.findByPrimaryKey(2025060001);
		System.out.print(orderVO3.getOrderNo() + ",");
		System.out.print(orderVO3.getMemNo() + ",");
		System.out.print(orderVO3.getCouponWalletNo() + ",");
		System.out.print(orderVO3.getOrderDatetime() + ",");
		System.out.print(orderVO3.getOrderStatus() + ",");
		System.out.print(orderVO3.getOrderTotal() + ",");
		System.out.print(orderVO3.getPointUsed());
		System.out.println("---------------------");
		
		// 查詢
		List<OrderVO> list = dao.getAll();
		for (OrderVO aOrder : list) {
			System.out.print(aOrder.getOrderNo() + ",");
			System.out.print(aOrder.getMemNo() + ",");
			System.out.print(aOrder.getCouponWalletNo() + ",");
			System.out.print(aOrder.getOrderDatetime() + ",");
			System.out.print(aOrder.getOrderStatus() + ",");
			System.out.print(aOrder.getOrderTotal() + ",");
			System.out.print(aOrder.getPointUsed());
			System.out.println();
		}
	}
	
	
}