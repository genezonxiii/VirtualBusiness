<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>電商平台</title>
	<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
	<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
	<link rel="stylesheet" href="vendor/css/jquery-ui.min.css">
	<link rel="stylesheet" href="//cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css" />
	<link rel="stylesheet" href="vendor/css/font-awesome.min.css">
	<link rel="stylesheet" href="css/styles.css">
	
	<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script>
function who(){
	switch(location.pathname.split("/")[2]){
//####交易處理############################
	case "upload.jsp":
		return "訂單拋轉作業";
		break;
//####後臺支援系統############################
	case "purchase.jsp":
		return "進貨管理";
		break;
	case "purchreturn.jsp":
		return "進貨退回管理";
		break;
	case "sale.jsp":
		return "銷貨管理";
		break;
	case "salereturn.jsp":
		return "銷貨退回管理";
		break;
	case "stock.jsp":
		return "庫存管理";
		break;
	case "producttype.jsp":
		return "商品類型管理";
		break;
	case "productunit.jsp":
		return "商品單位管理";
		break;
	case "product.jsp":
		return "商品管理";
		break;
	case "supply.jsp":
		return "供應商管理";
		break;
	case "user.jsp":
		return "用戶管理";
		break;
	case "group.jsp":
		return "公司管理";
		break;
	case "customer.jsp":
		return "客戶管理";
		break;
	case "accreceive.jsp":
		return "應收帳款管理";
		break;
	case "accpay.jsp":
		return "應付帳款管理";
		break;
	case "changepassword.jsp":
		return "密碼修改";
		break;
	case "tagprint.jsp":
		return "標籤列印";
		break;
//####報表管理############################
	case "salereport.jsp":
		return "訂單報表";
		break;
	case "distributereport.jsp":
		return "配送報表";
		break;
	case "salereturnreport.jsp":
		return "退貨報表";
		break;
	case "shipreport.jsp":
		return "出貨報表";
		break;
	case "purchreport.jsp":
		return "進貨報表";
		break;
	case "purchreturnreport.jsp":
		return "進貨退回報表";
		break;
//######分析圖表##########################
	case "salechart.jsp":
		return "出貨量統計圖";
		break;
	case "saleamountchart.jsp":
		return "銷售金額統計圖";
		break;
	case "saleamountstaticchart.jsp":
		return "銷售金額比例統計圖";
		break;
//######線上學院##########################
	case "onlinecourse.jsp":
		return "線上學院";
		break;
	case "template.jsp":
		return "暫時的";
		break;
	case "welcome.jsp":
		return "";
		break;
	default:
		if(location.pathname.split("/")[2].indexOf("upload")!=-1){return "訂單拋轉作業";}
		alert("default_page; "+location.pathname.split("/")[2]);
		//return "something_wrong?";
		return "";
		break;
	}
};
</script>
</head>
<body>
<script>
$(function() {
	$("#title").append(who());
	$("#logout").click(function(e) {
		$.ajax({
			type : "POST",
			url : "login.do",
			data : {
				action : "logout"
			},
			success : function(result) {
				top.location.href = "login.jsp";
			}
		});
	});
});
</script>
<div class="page-wrapper" >
	<div class="header" style="z-index:1;">
		<h1>智慧電商平台</h1>
		<div class="userinfo">
			<p>使用者<span><%= (request.getSession().getAttribute("user_name")==null)?"@_@?":request.getSession().getAttribute("user_name").toString() %></span></p>
			<a id="logout" class="btn-logout" >登出</a>
		</div>
	</div><!-- /.header -->
	<div id="sidenav-panel" style="position: fixed;top: 56px;width: 120px;height:100%;background: #3F7FAA;"></div>
	<div class="sidenav" style="z-index:2;">
		<ul>
			<li><img src="images/sidenav-transaction.svg" alt="">交易處理
				<ul>
					<li><a href="upload.jsp">訂單拋轉作業</a></li>
				</ul>
			</li>
			<li class="active"><img src="images/sidenav-support.svg" alt="">後臺支援系統
				<ul style="top: -156px;">
					<li><a href="purchase.jsp">進貨管理</a></li>
					<li><a href="purchreturn.jsp">進貨退回管理</a></li>
					<li><a href="sale.jsp">銷貨管理</a></li>
					<li><a href="salereturn.jsp">銷貨退回管理</a></li>
					<li><a href="stock.jsp">庫存管理</a></li>
					<li><a href="producttype.jsp">商品類型管理</a></li>
					<li><a href="productunit.jsp">商品單位管理</a></li>
					<li><a href="product.jsp">商品管理</a></li>
					<li><a href="supply.jsp">供應商管理</a></li>
					<li><a href="user.jsp">用戶管理</a></li>
					<li><a href="group.jsp">公司管理</a></li>
					<li><a href="customer.jsp">客戶管理</a></li>
					<li><a href="accreceive.jsp">應收帳款管理</a></li>
					<li><a href="accpay.jsp">應付帳款管理</a></li>
					<li><a href="changepassword.jsp">用戶帳密管理</a></li>
					<li><a href="tagprint.jsp">標籤列印</a></li>
				</ul>
			</li>
			<li><img src="images/sidenav-report.svg" alt="">報表管理
				<ul>
					<li><a href="salereport.jsp">訂單報表</a></li>
					<li><a href="shipreport.jsp">出貨報表</a></li>
					<li><a href="distributereport.jsp">配送報表</a></li>
					<li><a href="salereturnreport.jsp">退貨報表</a></li>
					<li><a href="purchreport.jsp">進貨報表</a></li>
					<li><a href="purchreturnreport.jsp">進貨退回報表</a></li>
				</ul>
			</li>
			<li><img src="images/sidenav-chart.svg" alt="">分析圖表
				<ul>
					<li><a href="salechart.jsp">出貨量統計圖</a></li>
					<li><a href="saleamountchart.jsp">銷售金額統計圖</a></li>
					<li><a href="saleamountstaticchart.jsp">銷售金額比例統計圖</a></li>
				</ul>
			</li>
			<li><img src="images/sidenav-school.svg" alt="">線上學院
				<ul>
					<li><a href="onlinecourse.jsp">線上學院</a></li>
				</ul>
			</li>
		</ul>
	</div><!-- /.sidenav -->
 	<h2 id="title" class="page-title" style="z-index:1">
 		<%= (("welcome.jsp".equals(request.getRequestURI().split("/")[2]))?("歡迎"+request.getSession().getAttribute("user_name")+"使用本系統"):("")) %>
 	</h2> 
<!-- 	<div class="content-wrap" style="display:none"> -->
<!-- 	<div> -->
 <!--################正文開始###############--> 
<!-- 		<div class="input-field-wrap"> -->
<!-- 			<div class="form-wrap"> -->
<!-- 				<div class="form-row"> -->
<!-- 					<label for=""> -->
<!-- 						<span class="block-label">自訂產品 ID 查詢</span> -->
<!-- 						<input type="text"> -->
<!-- 					</label> -->
<!-- 					<a href="" class="btn btn-darkblue">查詢</a> -->
<!-- 				</div> -->
<!-- 				<div class="form-row"> -->
<!-- 					<label for=""> -->
<!-- 						<span class="block-label">轉單起日</span> -->
<!-- 						<input type="text" class="input-date"> -->
<!-- 					</label> -->
<!-- 					<div class="forward-mark"></div> -->
<!-- 					<label for=""> -->
<!-- 						<span class="block-label">轉單迄日</span> -->
<!-- 						<input type="text" class="input-date"> -->
<!-- 					</label> -->
<!-- 					<a href="" class="btn btn-darkblue">查詢</a> -->
<!-- 				</div> -->
<!-- 				<div class="form-row"> -->
<!-- 					<label for=""> -->
<!-- 						<span class="block-label">配送起日</span> -->
<!-- 						<input type="text" class="input-date"> -->
<!-- 					</label> -->
<!-- 					<div class="forward-mark"></div> -->
<!-- 					<label for=""> -->
<!-- 						<span class="block-label">配送迄日</span> -->
<!-- 						<input type="text" class="input-date"> -->
<!-- 					</label> -->
<!-- 					<a href="" class="btn btn-darkblue">查詢</a>				 -->
<!-- 				</div> -->
<!-- 				<div class="btn-row"> -->
<!-- 					<a href="#" class="btn btn-exec btn-wide">新增銷售資料</a> -->
<!-- 				</div> -->
<!-- 			</div>/.form-wrap -->
<!-- 		</div>/.input-field-wrap -->

<!-- 		<div class="search-result-wrap"> -->
			
<!-- 			<div class="result-table-func-wrap"> -->
<!-- 				<div class="result-item-display"> -->
<!-- 					顯示 -->
<!-- 					<select name="" id=""> -->
<!-- 						<option value="">10</option> -->
<!-- 						<option value="">50</option> -->
<!-- 						<option value="">100</option> -->
<!-- 					</select> 項結果 -->
<!-- 				</div>/.result-item-display -->
<!-- 				<div class="result-item-search"> -->
<!-- 					<input type="text" placeholder="Search"> -->
<!-- 				</div>/.result-item-search -->
<!-- 			</div>/.result-table-func-wrap -->

<!-- 			<div class="result-table-wrap"> -->
<!-- 				<table class="result-table"> -->
<!-- 					<thead> -->
<!-- 						<tr> -->
<!-- 							<th>銷貨單號</th> -->
<!-- 							<th>訂單號</th> -->
<!-- 							<th>產品名稱</th> -->
<!-- 							<th>自訂產品 ID</th> -->
<!-- 							<th>銷貨數量</th> -->
<!-- 							<th>銷貨金額</th> -->
<!-- 							<th>發票號碼</th> -->
<!-- 							<th>發票日期</th> -->
<!-- 							<th>轉單日</th> -->
<!-- 							<th>配送日</th> -->
<!-- 							<th>備註說明</th> -->
<!-- 							<th>銷貨/出貨日期</th> -->
<!-- 							<th>銷售平台</th> -->
<!-- 							<th>操作</th> -->
<!-- 						</tr> -->
<!-- 					</thead> -->
<!-- 					<tbody> -->
<!-- 						<tr> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>名稱</td> -->
<!-- 							<td>1234567890</td> -->
<!-- 							<td>1</td> -->
<!-- 							<td>1680</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>payeasy</td> -->
<!-- 							<td> -->
<!-- 								<div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i> -->
<!-- 									<div class="table-function-list"> -->
<!-- 										<a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a> -->
<!-- 										<a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱</td> -->
<!-- 							<td>1234567890</td> -->
<!-- 							<td>1</td> -->
<!-- 							<td>1680</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>payeasy</td> -->
<!-- 							<td> -->
<!-- 								<div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i> -->
<!-- 									<div class="table-function-list"> -->
<!-- 										<a id="my" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a> -->
<!-- 										<a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱</td> -->
<!-- 							<td>1234567890</td> -->
<!-- 							<td>1</td> -->
<!-- 							<td>1680</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>payeasy</td> -->
<!-- 							<td> -->
<!-- 								<div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i> -->
<!-- 									<div class="table-function-list"> -->
<!-- 										<a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a> -->
<!-- 										<a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱</td> -->
<!-- 							<td>1234567890</td> -->
<!-- 							<td>1</td> -->
<!-- 							<td>1680</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>payeasy</td> -->
<!-- 							<td> -->
<!-- 								<div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i> -->
<!-- 									<div class="table-function-list"> -->
<!-- 										<a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a> -->
<!-- 										<a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱</td> -->
<!-- 							<td>1234567890</td> -->
<!-- 							<td>1</td> -->
<!-- 							<td>1680</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>payeasy</td> -->
<!-- 							<td> -->
<!-- 								<div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i> -->
<!-- 									<div class="table-function-list"> -->
<!-- 										<a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a> -->
<!-- 										<a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱</td> -->
<!-- 							<td>1234567890</td> -->
<!-- 							<td>1</td> -->
<!-- 							<td>1680</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>payeasy</td> -->
<!-- 							<td> -->
<!-- 								<div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i> -->
<!-- 									<div class="table-function-list"> -->
<!-- 										<a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a> -->
<!-- 										<a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱</td> -->
<!-- 							<td>1234567890</td> -->
<!-- 							<td>1</td> -->
<!-- 							<td>1680</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>payeasy</td> -->
<!-- 							<td> -->
<!-- 								<div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i> -->
<!-- 									<div class="table-function-list"> -->
<!-- 										<a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a> -->
<!-- 										<a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 						<tr> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>2016060137878</td> -->
<!-- 							<td>名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱名稱</td> -->
<!-- 							<td>1234567890</td> -->
<!-- 							<td>1</td> -->
<!-- 							<td>1680</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>NULL</td> -->
<!-- 							<td>2016-06-01</td> -->
<!-- 							<td>payeasy</td> -->
<!-- 							<td> -->
<!-- 								<div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i> -->
<!-- 									<div class="table-function-list"> -->
<!-- 										<a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a> -->
<!-- 										<a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</td> -->
<!-- 						</tr> -->
<!-- 					</tbody> -->
<!-- 				</table> -->
<!-- 			</div>/.result-table-wrap -->
<!-- 		</div>/.search-result-wrap -->
<!-- 	</div> -->
<!-- 	</div>/.content-wrap -->
<!--################結束###############-->
	<footer class="footer" style="z-index:1;">
		北祥股份有限公司 <span>服務電話：+886-2-2658-1910  |  傳真：+886-2-2658-1920</span>
	</footer><!-- / .footer -->
</div><!-- /.page-wrapper -->

<script src="vendor/js/jquery-1.12.4.min.js"></script>
<script src="vendor/js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>

<script src="js/scripts.js"></script>

</body>
</html>