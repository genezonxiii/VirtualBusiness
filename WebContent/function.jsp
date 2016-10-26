<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.productunit.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>真●後臺系統</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<style>
h3.ui-list-title {
	border-bottom: 1px solid #ccc;
	color: #307CB0;
}
section {
	margin-bottom: 60px;
}
</style>
<%-- <link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet"> --%>
<!-- <link rel="stylesheet" href="css/1.11.4/jquery-ui.css"> -->
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" style="background-color: #FFF;" >
		<div class='bdyplane' style="opacity:0">
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<!-- <script type="text/javascript" src="js/jquery.dataTables.min.js"></script> -->
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<!-- <script type="text/javascript" src="js/jquery-ui.js"></script> -->
<!-- <script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script> -->
<!-- <script type="text/javascript" src="js/jquery.validate.min.js"></script> -->
<!-- <script type="text/javascript" src="js/additional-methods.min.js"></script> -->
<!-- <script type="text/javascript" src="js/messages_zh_TW.min.js"></script> -->
<script>

	$(function() {
		var sys;
		$(".bdyplane").animate({"opacity":"1"});
		$(".content-wrap a").click(function(){
			$("#dialog-confirm").html("<table class='dialog-table'>"+
				"<tr><td><span class='delete_msg'>'"+$(this).html()+"'</span></td></tr>"+
				"</table>"
			);
			$("#dialog-confirm").val($(this).attr("id"));
			$("#dialog-confirm").attr("val2",$("#"+$(this).attr("id")+"_n").val());
			$("#dialog-confirm").dialog("open");
			
			//alert($("#dialog-confirm").val()+" "+$("#dialog-confirm").attr("val2"));
		});
		$("#dialog-confirm").dialog({
			draggable : false, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認執行" : function() {
					warning_msg("");
					$.ajax({
						type : "POST",
						url : "function.do",
						timeout: 15000,
						data : {
							action : $(this).val(),
							n : $(this).attr("val2"),
							month : $("#month").val(),
							order_source : $("#order_source").val()
						},
						success : function(result) {
							//alert(result);
							if(result=="success"){
								warning_msg("執行成功");
							}else{
								warning_msg("執行失敗");
							}
						}, error:function(){
							alert('系統異常，跟哥說\n等等哥給你看看。');
						}
					});
					$(this).dialog("close");
				},
				"取消" : function() {
					$(this).dialog("close");
				}
			}
		});
		$("#dialog-confirm").show();
		order_source_auto("order_source");
		order_source_auto("order_source2");
	});
</script>
			<!-- 第一列 -->
		<div class="search-result-wrap">
			
			<section>
				<h3 class="ui-list-title">顯示設定</h3>
				<div class="form-wrap">
					<div class="form-row">
						<input id="checkbox-1" type="checkbox" checked>
						<label for="checkbox-1"><span class="form-label">抗鋸齒</span></label>
						<input id="checkbox-2" type="checkbox">
						<label for="checkbox-2"><span class="form-label">HDR</span></label>
						<input id="checkbox-3" type="checkbox">
						<label for="checkbox-3"><span class="form-label">積體光</span></label>
					</div>
				</div><!-- /.form-wrap -->
			</section>
			<section>
				<h3 class="ui-list-title">使用模式</h3>
				<div class="form-wrap">
					<div class="form-row">
						<input id="radio-4" type="radio" name="radio-group-2"><label for="radio-4"><span class="form-label">普通模式</span></label>
			          	<input id="radio-5" type="radio" name="radio-group-2" checked><label for="radio-5"><span class="form-label">經濟模式</span></label>
			          	<input id="radio-7" type="radio" name="radio-group-3" disabled><label for="radio-7"><span class="form-label">付費模式</span></label>
					</div>
				</div><!-- /.form-wrap -->
			</section>
			<section>
				<h3 class="ui-list-title">系統設置</h3>
				<div class="form-row">
					<button>性能優先</button>
					<button>均衡設置</button>
					<button>畫面優先</button>
				</div>
			</section>
			<section id="func" style="padding-top:10px;">
				<h3 class="ui-list-title" >內部功能</h3>
				<div class="form-row">
					<a href="#" class="btn btn-primary" id="customer_class_now">O立即執行會員分級</a><br><br>
					<a href="#" class="btn btn-exec" id="default_primary_data">O產生預設基礎資料<br>(含 unit type 供應商 )</a><br><br>
					<a href="#" class="btn btn-alert" id="default_senior_data">O產生進階預設資料<br>(含customer product)</a>&nbsp;<input type="text" placeholder='增加會員數' id="default_senior_data_n"><br><br>
					<a href="#" class="btn btn-darkblue" id="random_product_value">O匹配亂數基礎資料<br>(對現有product 隨機產生 unit type 供應商 圖片1)</a><br><br>
					<a href="#" class="btn btn-green" id="random_sale">O亂數產生訂單</a>&nbsp;<input type="text" placeholder='增加訂單筆數' id="random_sale_n">&nbsp;<input type="text" placeholder='月份' id="month">&nbsp;<input type="text" placeholder='平台' id="order_source"><br><br>
					<a href="#" class="btn btn-orange" id="random_purchase">亂數產生進貨單</a>&nbsp;<input type="text" placeholder='筆數' id="random_purchase_n"><br><br>
					<a href="#" class="btn btn-gray" id="init">O清除這頁製造的所有資料</a>
				</div>
			</section>
			<div class="input-field-wrap"></div>
			<section>
				<h3 class="ui-list-title">各種autocomplete</h3>
				<div class="form-wrap">
					<div class="form-row">
						<label for="">unit
							<input type="text">
						</label>
						<label for="">type
							<input type="text">
						</label>
						<label for="">產品c_product_id
							<input type="text">
						</label>
						<label for="">產品名
							<input type="text">
						</label>
					</div>
					<div class="form-wrap">
						<label for="">供應商
							<input type="text">
						</label>
						<label for="">客戶名
							<input type="text">
						</label>
						<label for="">銷售平台
							<input type="text" id="order_source2">
						</label>
					</div>
				</div><!-- /.form-wrap -->
			</section>
			<section>
				<h3 class="ui-list-title">各種select</h3>
				<div class="form-wrap">
					<label for="">
						<select name="" id="">
							<option value="">UNIT</option>
							<option value="">選項</option>
						</select>
					</label>
					<label for="">
						<select name="" id="">
							<option value="">TYPE</option>
							<option value="">選項</option>
						</select>
					</label>
					<label for="">
						<select name="" id="">
							<option value="">自訂ID</option>
							<option value="">選項</option>
						</select>
					</label>
					<label for="">
						<select name="" id="">
							<option value="">產品名</option>
							<option value="">選項</option>
						</select>
					</label>
					<label for="">
						<select name="" id="">
							<option value="">供應商</option>
							<option value="">選項</option>
						</select>
					</label>
					<label for="">
						<select name="" id="">
							<option value="">客戶名</option>
							<option value="">選項</option>
						</select>
					</label>
					<label for="">
						<select name="" id="">
							<option value="">銷售平台</option>
							<option value="">選項</option>
						</select>
					</label>
				</div>
			</section>
		</div>
	</div>
	</div>
	<div id="dialog-confirm" title="無法復原 ，是否執行?"></div>
</body>
</html>