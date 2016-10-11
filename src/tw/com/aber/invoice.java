package tw.com.aber;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tw.com.aber.membercondition.MemberconditionVO;
import tw.com.aber.sale.controller.sale.SaleVO;

public class invoice extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		//System.out.println("ifdsoidsjfoewj;ofjoifefewjfew;oi");
		
		InvoiceService invoiceService = null;
		String group_id = request.getSession().getAttribute("group_id").toString();
		String user_id = request.getSession().getAttribute("user_id").toString();
		
		String action = request.getParameter("action");
		
		if ("search_invoice_false".equals(action) ){
			try {TimeUnit.MILLISECONDS.sleep(100);} catch (InterruptedException e) {}
			String invoice_false_start = request.getParameter("invoice_false_start");
			String invoice_false_end = request.getParameter("invoice_false_end");
			invoice_false_start = invoice_false_start.length()<3?"2000-01-01":invoice_false_start;
			invoice_false_end = invoice_false_end.length()<3?"3000-01-01":invoice_false_end;
			List<SaleVO> list =new ArrayList<SaleVO>();
			InvoiceDao invoicedao= new InvoiceDao();
			list=invoicedao.search_invoice_false(group_id, invoice_false_start, invoice_false_end);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			response.getWriter().write(jsonStrList);
		}else if("search_invoice_true".equals(action) ){
			try {TimeUnit.MILLISECONDS.sleep(100);} catch (InterruptedException e) {}
			String invoice_true_start = request.getParameter("invoice_true_start");
			String invoice_true_end = request.getParameter("invoice_true_end");
			invoice_true_start = invoice_true_start.length()<3?"2000-01-01":invoice_true_start;
			invoice_true_end = invoice_true_end.length()<3?"3000-01-01":invoice_true_end;
			List<SaleVO> list =new ArrayList<SaleVO>();
			InvoiceDao invoicedao= new InvoiceDao();
			list=invoicedao.search_invoice_true(group_id, invoice_true_start, invoice_true_end);
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			response.getWriter().write(jsonStrList);
		}else if("make_invoice_true".equals(action) ){
			//暫時沒有這個 叫兩個下面的代替
			int i;
			String sale_array = request.getParameter("sale_array");
			String[] sale_id = sale_array.split(",");
			List<String> list =new ArrayList<String>();
//			System.out.println(sale_id.length);
			for(i=1;i<sale_id.length;i++){
//				System.out.println(sale_id[i]);
				invoiceService = new InvoiceService();
				String tmp=invoiceService.generateInvoiceNo(group_id, sale_id[i]);
				list.add(sale_id[i]);
				try {
					TimeUnit.SECONDS.sleep(1);
//					TimeUnit.MILLISECONDS.sleep(800);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			Gson gson = new Gson();
			String jsonStrList = gson.toJson(list);
			response.getWriter().write(jsonStrList);
//			System.out.println("("+sale_array+")");
//			int j=0;if(j==0)return;
//			String invoice_no = invoiceService.generateInvoiceNo(group_id, sale_id[i]);
			
		}else if("make_invoice_false".equals(action) ){
			String invoice_id = request.getParameter("invoice_id");
			InvoiceDao invoicedao= new InvoiceDao();
			invoicedao.make_invoice_false(group_id, invoice_id);
			response.getWriter().write(invoice_id);
		}else if ("generateInvoiceNo".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String sale_id = request.getParameter("sale_id");
				
				/*************************** 2.開始查詢資料 ****************************************/
				invoiceService = new InvoiceService();
				String invoice_no = invoiceService.generateInvoiceNo(group_id, sale_id);
//				String result = invoiceService.updateInvoiceNo2Sale(group_id, sale_id, invoice_no);
//				if("Error".equals(invoice_no)){
//					
//					return;
//				}
				//System.out.println(invoice_no);
//				Gson gson = new Gson();
//				String jsonStrList = gson.toJson(list);
//				response.getWriter().write(jsonStrList);
				request.setCharacterEncoding("UTF-8");
				response.setCharacterEncoding("UTF-8");
				response.getWriter().write(invoice_no);
				return;
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("createInvoice".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String sale_id = request.getParameter("sale_id");
				
				
				/*************************** 2.開始查詢資料 ****************************************/
				invoiceService = new InvoiceService();
				List<InvoiceBean> list = invoiceService.getSearchAllDB(group_id, sale_id);
				
				request.setAttribute("action", "searchResults");
				request.setAttribute("list", list);
				
				Gson gson = new Gson();
				String jsonStrList = gson.toJson(list);
				response.getWriter().write(jsonStrList);
				
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if ("createInvoiceFile".equals(action)) {
			try {
				/*************************** 1.接收請求參數 ****************************************/
				String sale_id = request.getParameter("sale_id");
				
				
				/*************************** 2.開始查詢資料 ****************************************/
				invoiceService = new InvoiceService();
				List<InvoiceBean> list = invoiceService.getSearchAllDB(group_id, sale_id);
				
//				String invoicepath = getServletConfig().getServletContext().getInitParameter("invoicepath");
				InvoiceDao dao= new InvoiceDao();
				String invoicepath = dao.invoice_path_ingroup(group_id);
				if(invoicepath.length()<2)return;
				File f = new File(invoicepath + "/" + sale_id + ".txt");
				
				Writer objWriter = new BufferedWriter(new FileWriter(f));
				
				for(InvoiceBean master : list) {
					
					objWriter.write(master.getaMessageType());
					objWriter.write("\t");
					objWriter.write(master.getbMDFlag());	
					objWriter.write("\t");
					objWriter.write(master.getcInvoiceNo());	
					objWriter.write("\t");
					objWriter.write(master.getdInvoiceDate());	
					objWriter.write("\t");
					objWriter.write(master.geteRandomNumber());	
					objWriter.write("\t");
					objWriter.write(master.getfRefId());	
					objWriter.write("\t");
					objWriter.write(master.getgBuyerUniCode());
					objWriter.write("\t");
					objWriter.write(master.gethBueryName());	
					objWriter.write("\t");
					objWriter.write(master.getiTaxType());	
					objWriter.write("\t");
					objWriter.write(master.getjTaxRate());	
					objWriter.write("\t");
					objWriter.write(master.getkTaxTotal());	
					objWriter.write("\t");
					objWriter.write(master.getlDutyFreeTotal());	
					objWriter.write("\t");
					objWriter.write(master.getmZeroTaxTotl());	
					objWriter.write("\t");
					objWriter.write(master.getnBusinessTax());	
					objWriter.write("\t");
					objWriter.write(master.getoTatal());	
					objWriter.write("\t");
					objWriter.write(master.getpDonateName());	
					objWriter.write("\t");
					objWriter.write(master.getqDonateFlag());	
					objWriter.write("\t");
					objWriter.write(master.getrContainerType());	
					objWriter.write("\t");
					objWriter.write(master.getsContainerVcode());	
					objWriter.write("\t");
					objWriter.write(master.gettContainerNcode());	
					objWriter.write("\t");
					objWriter.write(master.getuPrintFlag());	
					objWriter.write("\t");
					objWriter.write(master.getvPrintFormat());	
					objWriter.write("\t");
					objWriter.write(master.getwMemo1());	
					objWriter.write("\t");
					objWriter.write(master.getxMemo2());	
					objWriter.write("\t");
					objWriter.write(master.getyDetailMemo1());	
					objWriter.write("\r\n");
					
					for(InvoiceDetailBean detail : master.getInvoiceDetail()) {
						objWriter.write(detail.getaMessageType());	
						objWriter.write("\t");
						objWriter.write(detail.getbMDFlag());	
						objWriter.write("\t");
						objWriter.write(detail.getcRefId());	
						objWriter.write("\t");
						objWriter.write(detail.getdSeqNo());	
						objWriter.write("\t");
						objWriter.write(detail.geteProductName());	
						objWriter.write("\t");
						objWriter.write(detail.getfQuantity());	
						objWriter.write("\t");
						objWriter.write(detail.getgUnit());	
						objWriter.write("\t");
						objWriter.write(detail.gethUnitPrice());	
						objWriter.write("\t");
						objWriter.write(detail.getiPrice());	
						objWriter.write("\t");
						objWriter.write(detail.getjTaxType());	
						objWriter.write("\t");
						objWriter.write(detail.getkMemo());	
						objWriter.write("\r\n");
					}
				}
				
				objWriter.flush();
				objWriter.close();
				
				response.getWriter().write("{}");
				
				/*************************** 其他可能的錯誤處理 **********************************/
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	/************************* 對應資料庫表格格式 **************************************/
	@SuppressWarnings("serial")
	public class SaleVO{
		public String sale_id;
		public String seq_no;
		public String group_id;
		public String order_no;
		public String user_id;
		public String product_id;
		public String product_name;
		public String c_product_id;
		public String customer_id;
		public String name;
		public String quantity;
		public String price;
		public String invoice;
		public String invoice_date;
		public String trans_list_date;
		public String dis_date;
		public String memo;
		public String sale_date;
		public String order_source;
		public String return_date;
		public String isreturn;
	}
	public class InvoiceBean implements java.io.Serializable {
		private String group_id;
		private String aMessageType;
		private String bMDFlag;
		private String cInvoiceNo;
		private String dInvoiceDate;
		private String eRandomNumber;	
		private String fRefId;
		private String gBuyerUniCode;
		private String hBueryName;
		private String iTaxType;
		private String jTaxRate;
		private String kTaxTotal;
		private String lDutyFreeTotal;
		private String mZeroTaxTotl;
		private String nBusinessTax;
		private String oTatal;
		private String pDonateName;
		private String qDonateFlag;
		private String rContainerType;
		private String sContainerVcode;
		private String tContainerNcode;
		private String uPrintFlag;
		private String vPrintFormat;
		private String wMemo1;
		private String xMemo2;
		private String yDetailMemo1;
		private List<InvoiceDetailBean> invoiceDetail;
		
		public String getGroup_id() {
			return group_id;
		}
		public void setGroup_id(String group_id) {
			this.group_id = group_id;
		}
		public String getaMessageType() {
			return aMessageType;
		}
		public void setaMessageType(String aMessageType) {
			this.aMessageType = aMessageType;
		}
		public String getbMDFlag() {
			return bMDFlag;
		}
		public void setbMDFlag(String bMDFlag) {
			this.bMDFlag = bMDFlag;
		}
		public String getcInvoiceNo() {
			return cInvoiceNo;
		}
		public void setcInvoiceNo(String cInvoiceNo) {
			this.cInvoiceNo = cInvoiceNo;
		}
		public String getdInvoiceDate() {
			return dInvoiceDate;
		}
		public void setdInvoiceDate(String dInvoiceDate) {
			this.dInvoiceDate = dInvoiceDate;
		}
		public String geteRandomNumber() {
			return eRandomNumber;
		}
		public void seteRandomNumber(String eRandomNumber) {
			this.eRandomNumber = eRandomNumber;
		}
		public String getfRefId() {
			return fRefId;
		}
		public void setfRefId(String fRefId) {
			this.fRefId = fRefId;
		}
		public String getgBuyerUniCode() {
			return gBuyerUniCode;
		}
		public void setgBuyerUniCode(String gBuyerUniCode) {
			this.gBuyerUniCode = gBuyerUniCode;
		}
		public String gethBueryName() {
			return hBueryName;
		}
		public void sethBueryName(String hBueryName) {
			this.hBueryName = hBueryName;
		}
		public String getiTaxType() {
			return iTaxType;
		}
		public void setiTaxType(String iTaxType) {
			this.iTaxType = iTaxType;
		}
		public String getjTaxRate() {
			return jTaxRate;
		}
		public void setjTaxRate(String jTaxRate) {
			this.jTaxRate = jTaxRate;
		}
		public String getkTaxTotal() {
			return kTaxTotal;
		}
		public void setkTaxTotal(String kTaxTotal) {
			this.kTaxTotal = kTaxTotal;
		}
		public String getlDutyFreeTotal() {
			return lDutyFreeTotal;
		}
		public void setlDutyFreeTotal(String lDutyFreeTotal) {
			this.lDutyFreeTotal = lDutyFreeTotal;
		}
		public String getmZeroTaxTotl() {
			return mZeroTaxTotl;
		}
		public void setmZeroTaxTotl(String mZeroTaxTotl) {
			this.mZeroTaxTotl = mZeroTaxTotl;
		}
		public String getnBusinessTax() {
			return nBusinessTax;
		}
		public void setnBusinessTax(String nBusinessTax) {
			this.nBusinessTax = nBusinessTax;
		}
		public String getoTatal() {
			return oTatal;
		}
		public void setoTatal(String oTatal) {
			this.oTatal = oTatal;
		}
		public String getpDonateName() {
			return pDonateName;
		}
		public void setpDonateName(String pDonateName) {
			this.pDonateName = pDonateName;
		}
		public String getqDonateFlag() {
			return qDonateFlag;
		}
		public void setqDonateFlag(String qDonateFlag) {
			this.qDonateFlag = qDonateFlag;
		}
		public String getrContainerType() {
			return rContainerType;
		}
		public void setrContainerType(String rContainerType) {
			this.rContainerType = rContainerType;
		}
		public String getsContainerVcode() {
			return sContainerVcode;
		}
		public void setsContainerVcode(String sContainerVcode) {
			this.sContainerVcode = sContainerVcode;
		}
		public String gettContainerNcode() {
			return tContainerNcode;
		}
		public void settContainerNcode(String tContainerNcode) {
			this.tContainerNcode = tContainerNcode;
		}
		public String getuPrintFlag() {
			return uPrintFlag;
		}
		public void setuPrintFlag(String uPrintFlag) {
			this.uPrintFlag = uPrintFlag;
		}
		public String getvPrintFormat() {
			return vPrintFormat;
		}
		public void setvPrintFormat(String vPrintFormat) {
			this.vPrintFormat = vPrintFormat;
		}
		public String getwMemo1() {
			return wMemo1;
		}
		public void setwMemo1(String wMemo1) {
			this.wMemo1 = wMemo1;
		}
		public String getxMemo2() {
			return xMemo2;
		}
		public void setxMemo2(String xMemo2) {
			this.xMemo2 = xMemo2;
		}
		public String getyDetailMemo1() {
			return yDetailMemo1;
		}
		public void setyDetailMemo1(String yDetailMemo1) {
			this.yDetailMemo1 = yDetailMemo1;
		}
		public List<InvoiceDetailBean> getInvoiceDetail() {
			return invoiceDetail;
		}
		public void setInvoiceDetail(List<InvoiceDetailBean> invoiceDetail) {
			this.invoiceDetail = invoiceDetail;
		}
	}

	@SuppressWarnings("serial")
	public class InvoiceDetailBean implements java.io.Serializable {
		private String aMessageType;
		private String bMDFlag;
		private String cRefId;
		private String dSeqNo;
		private String eProductName;	
		private String fQuantity;
		private String gUnit;	
		private String hUnitPrice;
		private String iPrice;
		private String jTaxType;
		private String kMemo;
		
		public String getaMessageType() {
			return aMessageType;
		}
		public void setaMessageType(String aMessageType) {
			this.aMessageType = aMessageType;
		}
		public String getbMDFlag() {
			return bMDFlag;
		}
		public void setbMDFlag(String bMDFlag) {
			this.bMDFlag = bMDFlag;
		}
		public String getcRefId() {
			return cRefId;
		}
		public void setcRefId(String cRefId) {
			this.cRefId = cRefId;
		}
		public String getdSeqNo() {
			return dSeqNo;
		}
		public void setdSeqNo(String dSeqNo) {
			this.dSeqNo = dSeqNo;
		}
		public String geteProductName() {
			return eProductName;
		}
		public void seteProductName(String eProductName) {
			this.eProductName = eProductName;
		}
		public String getfQuantity() {
			return fQuantity;
		}
		public void setfQuantity(String fQuantity) {
			this.fQuantity = fQuantity;
		}
		public String getgUnit() {
			return gUnit;
		}
		public void setgUnit(String gUnit) {
			this.gUnit = gUnit;
		}
		public String gethUnitPrice() {
			return hUnitPrice;
		}
		public void sethUnitPrice(String hUnitPrice) {
			this.hUnitPrice = hUnitPrice;
		}
		public String getiPrice() {
			return iPrice;
		}
		public void setiPrice(String iPrice) {
			this.iPrice = iPrice;
		}
		public String getjTaxType() {
			return jTaxType;
		}
		public void setjTaxType(String jTaxType) {
			this.jTaxType = jTaxType;
		}
		public String getkMemo() {
			return kMemo;
		}
		public void setkMemo(String kMemo) {
			this.kMemo = kMemo;
		}
	}
	/*************************** 制定規章方法 ****************************************/
	interface Invoice_interface {
		public String generateInvoiceNo(String group_id, String sale_id);
		public String updateInvoiceNo2Sale(String group_id, String sale_id, String invoice_no);
		public List<InvoiceBean> searchAllDB(String group_id, String sale_id);
	}

	/*************************** 處理業務邏輯 ****************************************/
	class InvoiceService {
		private Invoice_interface dao;

		public InvoiceService() {
			dao = new InvoiceDao();
		}
		
		public String generateInvoiceNo(String group_id, String sale_id) {
			return dao.generateInvoiceNo(group_id, sale_id);
		}

		public String updateInvoiceNo2Sale(String group_id, String sale_id, String invoice_no) {
			return dao.updateInvoiceNo2Sale(group_id, sale_id, invoice_no);
		}
	
		public List<InvoiceBean> getSearchAllDB(String group_id, String sale_id) {
			return dao.searchAllDB(group_id, sale_id);
		}
	}

	/*************************** 操作資料庫 ****************************************/
	class InvoiceDao implements Invoice_interface {
		// 會使用到的Stored procedure
		private static final String sp_get_sale_by_id = "call sp_get_sale_by_id(?, ?)";
		private static final String sp_insert_invoice = "call sp_insert_invoice(?, ?, ?)";
		private static final String update_invoiceno2sale = "update tb_sale set invoice = ?, invoice_date = DATE_FORMAT(NOW(),'%Y/%m/%d') where group_id = ? and sale_id = ?";
		
		private static final String sp_select_sale_invoice = "call sp_select_sale_invoice(?,?,?)";
		private static final String sp_select_sale_not_invoice = "call sp_select_sale_not_invoice(?,?,?)";
		private static final String sp_del_invoice = "call sp_del_invoice(?,?)";
		private final String dbURL = getServletConfig().getServletContext().getInitParameter("dbURL")
				+ "?useUnicode=true&characterEncoding=utf-8&useSSL=false";
		private final String dbUserName = getServletConfig().getServletContext().getInitParameter("dbUserName");
		private final String dbPassword = getServletConfig().getServletContext().getInitParameter("dbPassword");
		
		public String invoice_path_ingroup(String group_id){
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			String ret="";
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement("SELECT invoice_path FROM `tb_group` where group_id =?");
				pstmt.setString(1, group_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					ret=rs.getString("invoice_path");
				}
				return ret;
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
				// Clean up JDBC resources
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
			}
		}
		
		public void make_invoice_false(String group_id, String invoice_id) {
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_del_invoice);
				//System.out.println(group_id+"   "+invoice_id);
				pstmt.setString(1, group_id);
				pstmt.setString(2, invoice_id);
				rs = pstmt.executeQuery();
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			}
		}
		public List<SaleVO> search_invoice_false(String group_id, String invoice_false_start, String invoice_false_end) {
			List<SaleVO> list =new ArrayList<SaleVO>();
			SaleVO saleVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_sale_not_invoice);
				pstmt.setString(1, group_id);
				pstmt.setString(2, invoice_false_start);
				pstmt.setString(3, invoice_false_end);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.sale_id=rs.getString("sale_id");
					saleVO.seq_no=rs.getString("seq_no");
					saleVO.group_id=rs.getString("group_id");
					saleVO.order_no=rs.getString("order_no");
					saleVO.user_id=rs.getString("user_id");
					saleVO.product_id=rs.getString("product_id");
					saleVO.product_name=rs.getString("product_name");
					saleVO.c_product_id=rs.getString("c_product_id");
					saleVO.customer_id=rs.getString("customer_id");
					saleVO.quantity=rs.getString("quantity");
					saleVO.price=rs.getString("price");
					saleVO.invoice=rs.getString("invoice");
					saleVO.invoice_date=rs.getString("invoice_date");
					saleVO.trans_list_date=rs.getString("trans_list_date");
					saleVO.dis_date=rs.getString("dis_date");
					saleVO.sale_date=rs.getString("sale_date");
					saleVO.order_source=rs.getString("order_source");
					saleVO.return_date=rs.getString("return_date");
					saleVO.isreturn=rs.getString("isreturn");
					list.add(saleVO); 
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			}
			return list;
		}
		public List<SaleVO> search_invoice_true(String group_id, String invoice_true_start, String invoice_true_end) {
			List<SaleVO> list =new ArrayList<SaleVO>();
			SaleVO saleVO = null;
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_select_sale_invoice);
				pstmt.setString(1, group_id);
				pstmt.setString(2, invoice_true_start);
				pstmt.setString(3, invoice_true_end);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					saleVO = new SaleVO();
					saleVO.sale_id=rs.getString("sale_id");
					saleVO.seq_no=rs.getString("seq_no");
					saleVO.group_id=rs.getString("group_id");
					saleVO.order_no=rs.getString("order_no");
					saleVO.user_id=rs.getString("user_id");
					saleVO.product_id=rs.getString("product_id");
					saleVO.product_name=rs.getString("product_name");
					saleVO.c_product_id=rs.getString("c_product_id");
					saleVO.customer_id=rs.getString("customer_id");
					saleVO.quantity=rs.getString("quantity");
					saleVO.price=rs.getString("price");
					saleVO.invoice=rs.getString("invoice");
					saleVO.invoice_date=rs.getString("invoice_date");
					saleVO.trans_list_date=rs.getString("trans_list_date");
					saleVO.dis_date=rs.getString("dis_date");
					saleVO.sale_date=rs.getString("sale_date");
					saleVO.order_source=rs.getString("order_source");
					saleVO.return_date=rs.getString("return_date");
					saleVO.isreturn=rs.getString("isreturn");
					list.add(saleVO); 
				}
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
				// Clean up JDBC resources
			}
			return list;
		}
		@Override
		public String generateInvoiceNo(String group_id, String sale_id) {
			String invoice_no = "";
			
			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				CallableStatement cs = null;
//				System.out.println(group_id + "  " +sale_id);
//				group_id="cbcc3138-5603-11e6-a532-000d3a800878";
//				sale_id="0039ece7-68fa-11e6-897a-005056af760c";
				cs = con.prepareCall(sp_insert_invoice);
				cs.setString(1, group_id);
				cs.setString(2, sale_id);
				cs.registerOutParameter(3, Types.VARCHAR);
				
				cs.execute();
				invoice_no = cs.getString(3);
				
				//System.out.println("invoice_no:" + invoice_no);
			} catch (Exception se) {
				return "Error";
//				throw new RuntimeException("Exception. " + se.getMessage());
			} finally {
			
			}
			return invoice_no;
		}
		
		@Override
		public String updateInvoiceNo2Sale(String group_id, String sale_id, String invoice_no) {
			String result = "";

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			
			try {
				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				
				pstmt = con.prepareStatement(update_invoiceno2sale);
				pstmt.setString(1, invoice_no);
				pstmt.setString(2, group_id);
				pstmt.setString(3, sale_id);
				
				pstmt.executeUpdate();
				
				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("A database error occured. " + se.getMessage());
			} catch (ClassNotFoundException cnfe) {
				throw new RuntimeException("A database error occured. " + cnfe.getMessage());
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
			return result;
		}
		
		@Override
		public List<InvoiceBean> searchAllDB(String group_id, String sale_id) {
			List<InvoiceBean> list = new ArrayList<InvoiceBean>();
			
			InvoiceBean invoiceBean = null;
			InvoiceDetailBean invoiceDetailBean = null;

			Connection con = null;
			PreparedStatement pstmt = null;
			ResultSet rs = null;

			try {
//				Class.forName("com.mysql.jdbc.Driver");
				con = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
				pstmt = con.prepareStatement(sp_get_sale_by_id);
				pstmt.setString(1, group_id);
				pstmt.setString(2, sale_id);
				rs = pstmt.executeQuery();
				while (rs.next()) {
					invoiceBean = new InvoiceBean();
					invoiceBean.setGroup_id(rs.getString("group_id"));
					invoiceBean.setaMessageType("C0401");
					invoiceBean.setbMDFlag("M");
					invoiceBean.setcInvoiceNo(rs.getString("invoice"));
					
					SimpleDateFormat dt = new SimpleDateFormat("yyyyMMdd hh:mm:ss"); 
					Date current = new Date();
					invoiceBean.setdInvoiceDate(dt.format(current));
					
					int randomPIN = (int)(Math.random()*9000)+1000;
					String PINString = String.valueOf(randomPIN);
					invoiceBean.seteRandomNumber(PINString);
					
					invoiceBean.setfRefId(rs.getString("order_no"));
					invoiceBean.setgBuyerUniCode("20939790");
//					invoiceBean.setgBuyerUniCode("0000000000");
					invoiceBean.sethBueryName("0000");
					invoiceBean.setiTaxType("1");
					invoiceBean.setjTaxRate("0.05");
					
					BigDecimal price = rs.getBigDecimal("price");
					
					BigDecimal taxTotal, businessTax;
					taxTotal = price.divide(new BigDecimal("1.05"), BigDecimal.ROUND_HALF_DOWN);
					businessTax = price.subtract(taxTotal);
					invoiceBean.setkTaxTotal(taxTotal.toString());
					invoiceBean.setlDutyFreeTotal("0");
					invoiceBean.setmZeroTaxTotl("0");
					invoiceBean.setnBusinessTax(businessTax.toString());
					
					invoiceBean.setoTatal(price.toString());
					invoiceBean.setpDonateName("    ");
					invoiceBean.setqDonateFlag("0");
					invoiceBean.setrContainerType(" ");
					invoiceBean.setsContainerVcode(" ");
					invoiceBean.settContainerNcode(" ");
					invoiceBean.setuPrintFlag("Y");
					invoiceBean.setvPrintFormat("0");
					
					invoiceBean.setwMemo1("Memo1");
					invoiceBean.setxMemo2("Memo2");
					invoiceBean.setyDetailMemo1("Master-DetailMemo1");
					
					//Detail
					invoiceDetailBean = new InvoiceDetailBean();
					
					invoiceDetailBean.setaMessageType("C0401");
					invoiceDetailBean.setbMDFlag("D");
					invoiceDetailBean.setcRefId(rs.getString("order_no"));
					invoiceDetailBean.setdSeqNo("1");
					invoiceDetailBean.seteProductName(rs.getString("product_name"));
					invoiceDetailBean.setfQuantity(rs.getString("quantity"));
					invoiceDetailBean.setgUnit(" ");
					invoiceDetailBean.sethUnitPrice(rs.getString("price"));
					invoiceDetailBean.setiPrice(rs.getString("price"));
					invoiceDetailBean.setjTaxType("1");
					invoiceDetailBean.setkMemo(" ");
					
					List<InvoiceDetailBean> detailList = new ArrayList<InvoiceDetailBean>();
					detailList.add(invoiceDetailBean);
					
					invoiceBean.setInvoiceDetail(detailList);
					
					list.add(invoiceBean); // Store the row in the list
				}

				// Handle any driver errors
			} catch (SQLException se) {
				throw new RuntimeException("SQLExcept. " + se.getMessage());
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

	}

}
