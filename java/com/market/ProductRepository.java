package com.market;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import javax.sql.DataSource;
import javax.servlet.ServletConfig;

/**
 * ������: DB�� �ִ� ��� ��ǰ ����� �ҷ��´�.
 *       DB ������ ���� *.JSP���� ServletConfig�� ProductRepository �����ڿ� ���ڷ� �Ѱ��־�� ��
 *    (��) ProductRepository repo = new ProductRepository(getServletConfig());
 */
public class ProductRepository {

	private ArrayList<Product> listOfProducts = new ArrayList<Product>();
	private static ProductRepository instance = new ProductRepository();
	private ServletConfig sc;
	private ProductRepository() { }
	
	// DB�� �ִ� ��ǰ ������ ���� �ҷ��´�.
	public ProductRepository(ServletConfig config) {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		setConfig(config);
		
		String q_getProductData = "select code, name, company, price, stock, description, image from product";

		try {
			DataSource ds  = (DataSource)sc.getServletContext().getAttribute("dataSource");
			con = ds.getConnection();
			stmt = con.createStatement();
			rs = stmt.executeQuery(q_getProductData);
			
			while(rs.next()) {
				Product item = new Product();
				item.setCode(rs.getInt("code"));
				item.setName(rs.getString("name"));
				item.setPrice(rs.getInt("price"));
				item.setDescription(rs.getString("description"));
				item.setCompany(rs.getString("company"));
				item.setStock(rs.getInt("stock"));
				item.setImage(rs.getString("image"));	
				
				listOfProducts.add(item);
			}
			if(stmt != null) stmt.close();
			if(rs != null) rs.close();
			if(con != null) rs.close();
		} catch(Exception e){ e.printStackTrace(); }
	}
	
	public static ProductRepository getInstance() {
		return instance;
	}
	
	public ArrayList<Product> getAllProducts() {
		return listOfProducts;
	}
	
	/* ArrayList�� ��ǰ�� �߰��Ѵ�. */
	public void addProduct(Product item) {
		listOfProducts.add(item);
	}
	
	/** DB�� ��ǰ�� �߰��Ѵ�. 
	 *  �� �Լ��� ����ϱ� ���� ProductRepository�� �޼��� setConfig�� ServletConfig�� �Ѱ��־�� �Ѵ�.
	 *  (��) repo.setConfig(getServletConfig());
	 *      repo.uploadProduct(item);
	 */
	public void uploadProduct(Product item) {
		Connection con = null;
		String q_setProductData = "insert into product "
				+"(code, name, company, price, stock, description, image) "
				+"values (?,?,?,?,?,?,?)";

		try {
			DataSource ds  = (DataSource)sc.getServletContext().getAttribute("dataSource");
			con = ds.getConnection();
			PreparedStatement pstmt = con.prepareStatement(q_setProductData);
			pstmt.setInt(   1, item.getCode());
			pstmt.setString(2, item.getName());
			pstmt.setString(3, item.getCompany());
			pstmt.setInt(   4, item.getPrice());
			pstmt.setInt(   5, item.getStock());
			pstmt.setString(6, item.getDescription());
			pstmt.setString(7, item.getImage());
			pstmt.executeUpdate();
			
			if(pstmt != null) pstmt.close();
			if(con != null) con.close();
		} catch(Exception e){ e.printStackTrace(); }
	}
	
	public void setConfig(ServletConfig config) {
		if(sc == null) sc = config;
	}
}
