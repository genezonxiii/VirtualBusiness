<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<title>發票資訊管理</title>
		<meta charset="utf-8">
		<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
		<link href="css/jquery.dataTables.min.css" rel="stylesheet">
		<link href="css/1.11.4/jquery-ui.css" rel="stylesheet">
	</head>
	<body>
		<jsp:include page="template.jsp" flush="true"/>
		<div class="content-wrap" >
			<div class='bdyplane' style="opacity:0">
				<div class="datalistWrap">
					<!--對話窗樣式-確認 -->
					<div id="dialog-confirm" title="刪除發票資訊" style="display:none;"></div>
					<!--對話窗樣式-修改 -->
					<div id="dialog-form-update" title="修改發票資訊" style="display:none;">
						<form name="update-dialog-form-post" id="update-dialog-form-post">
							<fieldset>
								<table class="form-table">
									<tr style="display:none">
										<td>發票類型：</td>
										<td>
											<select name="invoice_type">
												<option value="01">三聯式發票</option>
												<option value="02">二聯式發票</option>
												<option value="03">二聯式收銀機</option>
												<option value="04">特種稅額</option>
												<option value="05">電子計算機</option>
												<option value="06">三聯式收銀機</option>
												<option value="07" selected>一般稅額計算之電子發票</option>
												<option value="08">特種稅額計算之電子發票</option>
											</select>
										</td>
									</tr><tr>
										<td>發票期別：</td>
										<td><input type="text" name="year_month" placeholder="修改期別"></td>
										<td>發票字軌：</td>
										<td><input type="text" name="invoice_track" placeholder="修改字軌"></td>
									</tr><tr>
										<td>發票起號：</td>
										<td><input type="text" name="invoice_beginno" placeholder="修改發票起號"></td>
										<td>發票迄號：</td>
										<td><input type="text" name="invoice_endno" placeholder="修改發票迄號"></td>
									</tr><tr>
										<td>目前已用發票編號：</td>
										<td><input type="text" name="seq" placeholder="修改目前已用發票編號"></td>
									</tr>
								</table>
							</fieldset>
						</form>
					</div>			
					<!--對話窗樣式-新增 -->
					<div id="dialog-form-insert" title="新增發票資訊" style="display:none;">
						<form name="insert-dialog-form-post" id="insert-dialog-form-post">
							<fieldset>
								<table class="form-table">
									<tr style="display:none">
										<td>發票類型：</td>
										<td>
											<select name="invoice_type">
												<option value="01">三聯式發票</option>
												<option value="02">二聯式發票</option>
												<option value="03">二聯式收銀機</option>
												<option value="04">特種稅額</option>
												<option value="05">電子計算機</option>
												<option value="06">三聯式收銀機</option>
												<option value="07" selected>一般稅額計算之電子發票</option>
												<option value="08">特種稅額計算之電子發票</option>
											</select>
										</td>
									</tr><tr>
										<td>發票期別：</td>
										<td><input type="text" name="year_month" placeholder="輸入發票期別"></td>
										<td>發票字軌：</td>
										<td><input type="text" name="invoice_track" placeholder="輸入發票字軌"></td>
									</tr><tr>
										<td>發票起號：</td>
										<td><input type="text" name="invoice_beginno" placeholder="輸入發票起號"></td>
										<td>發票迄號：</td>
										<td><input type="text" name="invoice_endno" placeholder="輸入發票迄號"></td>
									</tr><tr>
										<td>目前已用發票編號：</td>
										<td><input type="text" name="seq" placeholder="輸入目前已用發票編號"></td>
									</tr>
								</table>
							</fieldset>
						</form>
					</div>
				<!-- 第一列 -->
				<div class="input-field-wrap">
					<button class='btn-explanation' onclick="$('#explain').dialog('open');" style="">?</button>
					<div class="form-wrap">
						<div class="btn-row">
							<button class="btn btn-exec btn-wide" id="create-exchange">新增發票資訊</button>
						</div>
					</div><!-- /.form-wrap -->
				</div>
					<div class="row search-result-wrap" align="center" id="products2_contain_row" style="display:none;">
						<div id="products2-contain" class="ui-widget">
							<table id="products2" class="result-table">
								<thead>
									<tr>
										<th>發票期別</th>
										<th>發票字軌</th>
										<th style="background-image: none !important;">發票起號</th>
										<th style="background-image: none !important;">發票迄號</th>
										<th>目前已用發票編號</th>
										<th style="background-image: none !important;">功能</th>
									</tr>
								</thead>
								<tbody>
								</tbody>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div id="explain" title="發票資訊規則詳述 (重要)" style="display:none;">
			<div style="padding:0 40px; font-family: Helvetica, Arial, '微軟正黑體', '新細明體', sans-serif;">
				<br><li>發票期別欄填入<font color=red size=4>5碼</font>代表的是開立發票期別(註2)的<font color=red size=4>民國</font>年加上月份<font color=red size=4>需補零</font>。
				並且，目前已用發票編號填入發票起號，發票號碼自發票起號開始累加至發票迄號結束。</li>
				<font size=2>
				<br>*註1：發票字軌為兩碼英文字母。
				<br><br>*註2：發票期別欄的發票檔期為兩個月一期，輸入<font color=red size=3>雙月</font>月份，即不論開立發票日期為105年1月或105年2月，期別欄皆填入10502。
				<br><br>*註3：發票起號及發票迄號需為八位數字。
			</div>
		</div>
		
		<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
		<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
		<script type="text/javascript" src="js/jquery-ui.min.js"></script>
		<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
		<script type="text/javascript" src="js/jquery.validate.min.js"></script>
		<script type="text/javascript" src="js/additional-methods.min.js"></script>
		<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
		<script>
		
			function draw_exchange(parameter){
				$.ajax({
					type : "POST",
					url : "invoicetrack.do",
					data : parameter,
					success : function(result) {
						var json_obj = $.parseJSON(result);
						var result_table = "";
						$.each(json_obj,function(i, item) {
							result_table 
							+="<tr><td name='year_month'>"+ item.year_month
							+ "</td><td name='invoice_track'>"+ item.invoice_track
							+ "</td><td name='invoice_beginno'>"+ item.invoice_beginno
							+ "</td><td name='invoice_endno'>"+ item.invoice_endno
							+ "</td><td name='seq'>"+ item.seq
							+ "</td><td>"
							+ "<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
							+ "	<div class='table-function-list'>"
							+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].invoice_id+ "'><i class='fa fa-pencil'></i></button>"
							+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' value='"+ json_obj[i].invoice_id+"' val2='"+json_obj[i].year_month+"'><i class='fa fa-trash'></i></button>"
							+ "	</div></div>"
							+ "</td></tr>";
						});
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
						$("#products2").dataTable().fnDestroy();
						if(resultRunTime!=0){
							$("#products2_contain_row").show();
							$("#products2_contain_row").css({"opacity":"0"});
							$("#products2 tbody").html(result_table);
							$("#products2").dataTable({
								"aaSorting": [],
								"language": {"url": "js/dataTables_zh-tw.txt", "zeroRecords": "沒有符合的結果"}
							});
							tooltip('btn_update');
							tooltip('btn_delete');
							$("#products2").find("td").css({"text-align":"center","height":"32px"});
							$("#products2_contain_row").animate({"opacity":"0.01"},1);
							$("#products2_contain_row").animate({"opacity":"1"},300);
							warning_msg("");
						}else{
							warning_msg("---查無此結果---");
							$("#products2_contain_row").hide();
						}
					}
				});
			}
			
			$(function() {
				$(".bdyplane").animate({"opacity":"1"});
				draw_exchange({action : "search"});
				
				var form_rule = {
						invoice_type : {required: true },
						year_month : {required: true, maxlength: 5, ming: true },
						invoice_track : {required: true, minlength: 2, maxlength: 2, alpha: true},
						invoice_beginno : {required: true, minlength: 8, maxlength: 8, number: true, lessthen: true },
						invoice_endno : {required: true, minlength: 8, maxlength: 8, number :true, lessthen: true },
						seq : {required: true, maxlength: 8, number: true, between: true }
					};
				
				//使用jquery.validate來做驗證  注意事項: 1.不能用選擇器方式批次設定，它只取最後一個參數 2.要調用resetForm()而不是reset()
				var validator_insert = $("#insert-dialog-form-post").validate({
					rules : form_rule
				});
				var validator_update = $("#update-dialog-form-post").validate({
					rules : form_rule
				});
				
				jQuery.validator.addMethod("alpha", function(value, element) {
				  	return this.optional(element) || /[A-Z]{2}/.test(value);
				}, "請輸入大寫英文字");
				jQuery.validator.addMethod("ming", function(value, element) {
					var d = new Date();
				    var n = d.getFullYear() - 1911;
				    return this.optional(element) || new RegExp( "(" + n + "|" + (n + 1) + ")" + "(02|04|06|08|10|12)" ).test(value);
				}, "請輸入發票期別，末兩碼應為02或12");
				jQuery.validator.addMethod("lessthen", function(value, element) {
					var begin = $(element).parents("table").find("input[name='invoice_beginno']").val();
					var end = $(element).parents("table").find("input[name='invoice_endno']").val();
					return this.optional(element) || (begin != '' && end != ''? end >= begin:true);
				}, "發票起號需小於等於發票迄號");
				jQuery.validator.addMethod("between", function(value, element) {
					var begin = $(element).parents("table").find("input[name='invoice_beginno']").val();
					var end = $(element).parents("table").find("input[name='invoice_endno']").val();
				  	return this.optional(element) || (begin <= value && value <= end? true:false);
				}, function (param, element){
					var begin = $(element).parents("table").find("input[name='invoice_beginno']").val();
					var end = $(element).parents("table").find("input[name='invoice_endno']").val();
					return jQuery.validator.format("目前已用發票編號需介在 {0} 和 {1} 之間", begin, end);
				});
				
				//新增Dialog相關設定
				insert_dialog = $("#dialog-form-insert").dialog({
					draggable : true, resizable : false, autoOpen : false,
					height : "auto", width : "auto", modal : true,
					show : {effect : "blind",duration : 300},
					hide : {effect : "fade",duration : 300},
					buttons : [{
						id : "insert",
						text : "新增",
						click : function() {
							if ($('#insert-dialog-form-post').valid()) {
								var tmp={
									action : "insert",
									invoice_type : $("#dialog-form-insert select[name='invoice_type']").val(),
									year_month : $("#dialog-form-insert input[name='year_month']").val(),
									invoice_track : $("#dialog-form-insert input[name='invoice_track']").val(),
									invoice_beginno : $("#dialog-form-insert input[name='invoice_beginno']").val(),
									invoice_endno : $("#dialog-form-insert input[name='invoice_endno']").val(),
									seq : $("#dialog-form-insert input[name='seq']").val()
								};
								draw_exchange(tmp);
								insert_dialog.dialog("close");
							}
						}
					}, {
						text : "取消",
						click : function() {
							validator_insert.resetForm();
							insert_dialog.dialog("close");
						}
					}],
					close : function() {
						validator_insert.resetForm();
					}
				});
				$("#dialog-form-insert").show();
				var uuid = "";
				//確認Dialog相關設定(刪除功能)
				confirm_dialog = $("#dialog-confirm").dialog({
					draggable : true, resizable : false, autoOpen : false,
					height : "auto", width : "auto", modal : true,
					show : {effect : "blind",duration : 300},
					hide : {effect : "fade",duration : 300},
					buttons : {
						"確認刪除" : function() {
							var tmp={
									action : "delete",
									invoice_id : $(this).val()
								};
							draw_exchange(tmp);
							$(this).dialog("close");
						},
						"取消刪除" : function() {
							$(this).dialog("close");
						}
					}
				});
				$("#dialog-confirm").show();
				//修改Dialog相關設定
				update_dialog = $("#dialog-form-update").dialog({
					draggable : true, resizable : false, autoOpen : false,
					height : "auto", width : "auto", modal : true,
					show : {effect : "blind",duration : 300},
					hide : {effect : "fade",duration : 300},
					buttons : [{
						text : "修改",
						click : function() {
							if ($('#update-dialog-form-post').valid()) {
								var tmp={
			 							action : "update",
			 							invoice_id : $(this).val(),
			 							invoice_type : $("#dialog-form-update select[name='invoice_type']").val(),
			 							year_month : $("#dialog-form-update input[name='year_month']").val(),
			 							invoice_track : $("#dialog-form-update input[name='invoice_track']").val(),
			 							invoice_beginno : $("#dialog-form-update input[name='invoice_beginno']").val(),
			 							invoice_endno : $("#dialog-form-update input[name='invoice_endno']").val(),
			 							seq : $("#dialog-form-update input[name='seq']").val()
									};
								draw_exchange(tmp);
								update_dialog.dialog("close");
							}
						}
					}, {
						text : "取消",
						click : function() {
							validator_update.resetForm();
							update_dialog.dialog("close");
						}
					} ],
					close : function() {
						validator_update.resetForm();
					}
				});
				$("#dialog-form-update").show();
				//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
				$("#products2").delegate(".btn_delete", "click", function() {
					$("#dialog-confirm").val($(this).val());
					$("#dialog-confirm").html("<div class='delete_msg'>刪除發票期別："+$(this).attr("val2")+"</div>");
					confirm_dialog.dialog("open");
				});
				//修改事件聆聽
				$("#products2").delegate(".btn_update", "click", function() {
					$("#dialog-form-update").val($(this).val());
					$("#update-dialog-form-post select[name='invoice_type']").val($(this).parents("tr").find("td[name='invoice_type']").html());
					$("#update-dialog-form-post input[name='year_month']").val($(this).parents("tr").find("td[name='year_month']").html());
					$("#update-dialog-form-post input[name='invoice_track']").val($(this).parents("tr").find("td[name='invoice_track']").html());
					$("#update-dialog-form-post input[name='invoice_beginno']").val($(this).parents("tr").find("td[name='invoice_beginno']").html());
					$("#update-dialog-form-post input[name='invoice_endno']").val($(this).parents("tr").find("td[name='invoice_endno']").html());
					$("#update-dialog-form-post input[name='seq']").val($(this).parents("tr").find("td[name='seq']").html());
					update_dialog.dialog("open");
				});
				//新增事件聆聽
				$("#create-exchange").click( function() {
					$("#dialog-form-insert select[name='invoice_type']").val('07');
					$("#dialog-form-insert input[name='year_month']").val('');
					$("#dialog-form-insert input[name='invoice_track']").val('');
					$("#dialog-form-insert input[name='invoice_beginno']").val('');
					$("#dialog-form-insert input[name='invoice_endno']").val('');
					$("#dialog-form-insert input[name='seq']").val('');
					insert_dialog.dialog("open");
				});
				$("#explain").dialog({
					draggable : true, resizable : false, autoOpen : false,
					width : 720 ,height : "auto", modal : false,
					show : {effect : "blind", duration : 300 },
					hide : { effect : "fade", duration : 300 },
					buttons : {
						"確定" : function() {$(this).dialog("close");}
					}
				});
			});
		</script>
	</body>
</html>