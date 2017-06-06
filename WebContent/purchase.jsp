<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="tw.com.aber.purchase.controller.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.sql.DriverManager"%>
<%@ page import="java.sql.Connection"%>
<%@ page import="java.sql.Statement"%>
<%@ page import="java.sql.ResultSet"%>
<jsp:directive.page import="java.sql.SQLException" />
<!DOCTYPE html>
<html>
<head>
<title>進貨管理</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<style type="text/css">
	.warning_msg{
		position:relative;
		height:0px;
 		padding-top:0px ! important; 
		top:20px;
	}
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
		
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>
<script type="text/javascript" src="js/scripts.js"></script>

<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
<link rel="stylesheet" href="css/buttons.dataTables.min.css">
<script>

$(function(){
    $('#purchases').on('change', ':checkbox', function() {
        $(this).is(":checked")?
            $(this).closest("tr").addClass("selected"):
            $(this).closest("tr").removeClass("selected");
    });     
});

var $dtMaster = null;
var selectCount = 0;
function draw_purchase(parameter){
	$("#purchases_contain_row").css({"opacity":"0"});
	warning_msg("---讀取中請稍候---");
	$.ajax({
		type : "POST",
		url : "purchase.do",
		data : parameter,
		success : function(result) {
			console.log(result);
				var json_obj = $.parseJSON(result);
				var len=json_obj.length;
				//判斷查詢結果
				var resultRunTime = 0;
				$.each (json_obj, function (i) {
					resultRunTime+=1;
				});
				if(json_obj[resultRunTime-1].message=="驗證通過"){
					var result_table = "";
					$.each(json_obj,function(i, item) {
						if(i<len-1){
							if(json_obj[i].seq_no==null){
								json_obj[i].seq_no="";
							}
							if(json_obj[i].memo==null){
								json_obj[i].memo="";
							}
							if(json_obj[i].purchase_date==null){
								json_obj[i].purchase_date="";
							}
							if(json_obj[i].invoice==null){
								json_obj[i].invoice="";
							}
							if(json_obj[i].invoice_type==null||json_obj[i].invoice_type=="0"){
								json_obj[i].invoice_type="";
							}
							if(json_obj[i].invoice_type=="1"){
								json_obj[i].invoice_type="二聯式發票";
							}
							if(json_obj[i].invoice_type=="2"){
								json_obj[i].invoice_type="三聯式發票";
							}
							if(json_obj[i].amount==null){
								json_obj[i].amount="";
							}
							
							var purchase_id = json_obj[i].purchase_id;
							var input = document.createElement("INPUT");
							input.type = 'checkbox';
							input.name = 'checkbox-group-select';
							input.id = purchase_id;
							
							var span = document.createElement("SPAN");
							span.className = 'form-label';

							var label = document.createElement("LABEL");
							label.htmlFor = purchase_id;
							label.name = 'checkbox-group-select';
							label.style.marginLeft = '35%';
							label.appendChild(span);
							
							var options = $("<div/>").append(input, label);
							
							result_table 
							+= "<tr>"
							+ "<td>"+ options.html() +"</td>"
							+ "<td name='"+ json_obj[i].seq_no +"'>"+ json_obj[i].seq_no+ "</td>"
							+ "<td name='"+ json_obj[i].purchase_date +"'>"+ json_obj[i].purchase_date+ "</td>"
							+ "<td name='"+ json_obj[i].invoice +"'>"+ json_obj[i].invoice+ "</td>"
							+ "<td name='"+ json_obj[i].invoice_type +"'>"+ json_obj[i].invoice_type+ "</td>"
							+ "<td name='"+ json_obj[i].amount +"'>"+ money(json_obj[i].amount)+ "</td>"
							+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
							+ (isIE()?"<td><div class='table-row-func btn-in-table btn-gray' style='float:left;'><i class='fa fa-ellipsis-h'></i>":"<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>")
							+ "	<div class='table-function-list' >"
							+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改進貨單' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "' ><i class='fa fa-pencil'></i></button>"
							+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除進貨單' id='"+json_obj[i].seq_no+"'value='"+ json_obj[i].purchase_id + "'><i class='fa fa-trash'></i></button>"
							+ "		<button class='btn-in-table btn-primary btn_detail' title='顯示明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-list'></i></button>"
							+ "		<button class='btn-in-table btn-green btn_create' title='新增明細' value='"+ json_obj[i].purchase_id + "'><i class='fa fa-pencil-square-o'></i></button>"
							+ "	</div></div></td></tr>";	
						}
					});
				}
				var table = $("#purchases").dataTable().fnDestroy();

				if(resultRunTime!=0&&json_obj[resultRunTime-1].message=="驗證通過"){
					$("#purchases_contain_row").hide();
					$("#purchases tbody").html(result_table);
					$dtMaster = $("#purchases").dataTable({
						dom : "frB<t>ip",
						lengthChange: false,
						pageLength: 20,
						scrollY:"260px",
						initComplete: function(settings, json) {
						    $('div .dt-buttons').css({'float': 'left','margin-left':'10px'});
						    $('div .dt-buttons a').css('margin-left','10px');
						},
						buttons : [{
							text : '入庫接口',
							action : function(e, dt, node, config) {
								var $table =  $('#purchase');

							    var cells = $dtMaster.fnGetNodes();
								var idArr = '';
								
								var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');
								
								
								if($checkboxs.length == 0){
									alert('請至少選擇一筆資料');
									return false;
								}
								if($checkboxs.length > 20){
									alert('最多選擇二十筆資料');
									return false;
								}
								
								$checkboxs.each(function() {
									idArr += this.id + ',';
								});
								idArr = idArr.slice(0,-1);
								idArr = idArr.replace(/,/g,"','");
								idArr = "'" + idArr + "'";
								
								$.ajax({
									url: 'purchase.do', 
									type: 'post',
									data: {
										action: 'purchaseOrderService',
										purchase_ids: idArr
									},
									error: function (xhr) { },
									success: function (response) {
										var $mes = $('#message #text');
										$mes.val('').html('成功發送');
										$('#message')
											.dialog()
											.dialog('option', 'title', '提示訊息')
											.dialog('option', 'width', 'auto')
											.dialog('option', 'minHeight', 'auto')
											.dialog("open");
									}
								});		
								console.log('idArr: '+ idArr);		
							}
						},{
							text : '入庫取消接口',
							action : function(e, dt, node, config) {
								var $table =  $('#purchase');

							    var cells = $dtMaster.fnGetNodes();
								var idArr = '';
								
								var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');
								
								if($checkboxs.length > 20){
									alert('最多選擇二十筆資料');
									return false;
								}
								if($checkboxs.length == 0){
									alert('請至少選擇一筆資料');
									return false;
								}
								$checkboxs.each(function() {
									idArr += this.id + ',';
								});
								idArr = idArr.slice(0,-1);
								idArr = idArr.replace(/,/g,"','");
								idArr = "'" + idArr + "'";
								
								$.ajax({
									url: 'purchase.do', 
									type: 'post',
									data: {
										action: 'cancelPurchaseOrderService',
										purchase_ids: idArr
									},
									error: function (xhr) { },
									success: function (response) {
										var $mes = $('#message #text');
										$mes.val('').html('成功發送');
										$('#message')
											.dialog()
											.dialog('option', 'title', '提示訊息')
											.dialog('option', 'width', 'auto')
											.dialog('option', 'minHeight', 'auto')
											.dialog("open");
									}
								});		
								console.log('idArr: '+ idArr);		
							}
						},{
							text : '入庫明細查詢接口',
							action : function(e, dt, node, config) {
								var $table =  $('#purchase');

							    var cells = $dtMaster.fnGetNodes();
								var idArr = '';
								
								var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');
								
								if($checkboxs.length > 20){
									alert('最多選擇二十筆資料');
									return false;
								}
								if($checkboxs.length == 0){
									alert('請至少選擇一筆資料');
									return false;
								}
								$checkboxs.each(function() {
									idArr += this.id + ',';
								});
								idArr = idArr.slice(0,-1);
								idArr = idArr.replace(/,/g,"','");
								idArr = "'" + idArr + "'";
								
								$.ajax({
									url: 'purchase.do', 
									type: 'post',
									data: {
										action: 'PurchaseOrderQueryService',
										purchase_ids: idArr
									},
									error: function (xhr) { },
									success: function (response) {
										var $mes = $('#message #text');
										$mes.val('').html('成功發送');
										$('#message')
											.dialog()
											.dialog('option', 'title', '提示訊息')
											.dialog('option', 'width', 'auto')
											.dialog('option', 'minHeight', 'auto')
											.dialog("open");
									}
								});		
								console.log('idArr: '+ idArr);		
							}
							},{
										text : '全選',
										action : function(e, dt, node, config) {

											selectCount++;
											var $table =  $('#purchases');
											var $checkboxs = $table.find('input[name=checkbox-group-select]');
											
											selectCount %2 != 1 ?
													$checkboxs.each(function() {
														$(this).prop("checked", false);
														$(this).removeClass("toggleon");
											        	$(this).closest("tr").removeClass("selected");
													}): 
													$checkboxs.each(function() {
														$(this).prop("checked", true);
														$(this).addClass("toggleon");
														$(this).closest("tr").addClass("selected");
													});						
										}
									}
						   		],
						
						  autoWidth: false,
						  scrollX:  true,
				          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
					tooltip('btn_update');
					tooltip('btn_delete');
					tooltip('btn_detail');
					tooltip('btn_create');
					$("#purchases_contain_row").show();
					$("#purchases_contain_row").animate({"opacity":"0.01"},1);
					$("#purchases_contain_row").animate({"opacity":"1"},300);
					$("#purchases").find("td").css("text-align", "center");
					warning_msg("");
				}
				if(resultRunTime<2){
					$("#purchases_contain_row").hide();
					$("#purchase_detail_contain_row").hide();
					warning_msg("---查無此結果---");
				}
				if(json_obj[resultRunTime-1].message=="如要以日期查詢，完整填寫起日欄位"){
					$("#purchases_contain_row").hide();
					warning_msg("---如要以日期查詢，請完整填寫起日欄位---");
				}
				if(json_obj[resultRunTime-1].message=="如要以日期查詢，完整填寫訖日欄位"){
					$("#purchases_contain_row").hide();
					warning_msg("---如要以日期查詢，請完整填寫訖日欄位---");
				}						
				if(json_obj[resultRunTime-1].message=="起日不可大於訖日"){
					$("#purchases_contain_row").hide();
					warning_msg("---起日不可大於訖日---");
				}	
			}
		});
}

function draw_purchase_detail(parameter){
	$("#purchase_detail_contain_row").css({"opacity":"0"});
	warning_msg("---讀取中請稍候---");
	$.ajax({
		type : "POST",
		url : "purchase.do",
		data : parameter,
		success : function(result) {
			var json_obj = $.parseJSON(result);
			//判斷查詢結果
			var resultRunTime = 0;
			$.each (json_obj, function (i) {
				resultRunTime+=1;
			});
			var result_table = "";
			if(resultRunTime!=0){
				$.each(json_obj,function(i, item) {
					if(json_obj[i].c_product_id==null){
						json_obj[i].c_product_id="";
					}
					if(json_obj[i].product_name==null){
						json_obj[i].product_name="";
					}
					if(json_obj[i].quantity==null){
						json_obj[i].quantity="";
					}
					if(json_obj[i].cost==null){
						json_obj[i].cost="";
					}
					if(json_obj[i].memo==null){
						json_obj[i].memo="";
					}
					result_table 
					+= "<tr>"
					+ "<td name='"+ json_obj[i].c_product_id +"'>"+ json_obj[i].c_product_id+ "</td>"
					+ "<td name='"+ json_obj[i].product_name +"'>"+ json_obj[i].product_name+ "</td>"
					+ "<td name='"+ json_obj[i].quantity +"'>"+ json_obj[i].quantity+ "</td>"
					+ "<td name='"+ json_obj[i].cost +"'>"+ money(json_obj[i].cost)+ "</td>"
					+ "<td name='"+ json_obj[i].memo +"'>"+ json_obj[i].memo+ "</td>"
					+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
					+ "	<div class='table-function-list'>"
					+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' name='"+json_obj[i].product_id+"'id='"+json_obj[i].purchase_id+"'value='"+ json_obj[i].purchaseDetail_id + "' ><i class='fa fa-pencil'></i></button>"
					+ "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id='"+json_obj[i].purchase_id+"'value='"+ json_obj[i].purchaseDetail_id + "'><i class='fa fa-trash'></i></button>"
					+ "	</div></div></td></tr>";
				});
			}
			$("#purchase-detail-table").dataTable().fnDestroy();
			if(resultRunTime!=0){
				if($("#purchase_detail_err_mes").length){
    				$("#purchase_detail_err_mes").html("");
    			}
				$("#purchase_detail_contain_row").hide();
				$("#supply_name_err_mes").html("");
				warning_msg("");
				$("#purchase-detail-table tbody").html(result_table);
				$("#purchase-detail-table").dataTable({
					  autoWidth: false,
					  scrollX:  true,
			          scrollY:"300px","language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
				tooltip('btn_update');
				tooltip('btn_delete');
				$("#purchase-detail-table").find("td").css("text-align", "center");
				$("#purchase_detail_contain_row").show();
				$("#purchase_detail_contain_row").animate({"opacity":"0.01"},1);
				$("#purchase_detail_contain_row").animate({"opacity":"1"},300);
			}else{
				warning_msg("---查無該進貨單明細---");
// 				if(!$("#purchase_detail_err_mes").length){
//     				$("<p id='purchase_detail_err_mes'>查無明細</p>").appendTo($("#purchases-contain").parent());
//     			}else{
//     				$("#purchase_detail_err_mes").html("查無明細");
//     			}
				$("#purchase_detail_contain_row").hide();
			}
		}
	});			
}
//#########################################################################
//#########################################################################
//#########################################################################
//#########################################################################
	var new_or_edit=0;
	jQuery(document).ready(function($) {
	    $(window).scannerDetection();
	    $(window).bind('scannerDetectionComplete',function(e,data){
	    		if(data.string=="success"){return;}
	    		$.ajax({url : "product.do", type : "POST", cache : false,
		            data : {
		            	action : "find_barcode",
		            	barcode : data.string,
		            },
		            success: function(result) {
		            	var json_obj = $.parseJSON(result);
						if(json_obj.length!=0){
							if(new_or_edit==1){
								//new_or_edit=3;
								$("#insert_detail_c_product_id").val(json_obj[0].c_product_id);
								$("#insert_detail_product_name").val(json_obj[0].product_name);
								$("#insert_detail_product_n").val(json_obj[0].keep_stock);
								$("#insert_detail_product_cost").val(json_obj[0].cost);
								$("#insert_detail_product_price").val(json_obj[0].cost);
				    		}
				    		if(new_or_edit==2){
				    			//new_or_edit=3;
				    			$("#update_detail_c_product_id").val(json_obj[0].c_product_id);
				    			$("#update_detail_product_name").val(json_obj[0].product_name);
				    			$("#update_detail_product_n").val(json_obj[0].keep_stock);
								$("#update_detail_product_cost").val(json_obj[0].cost);
								$("#update_detail_product_price").val(json_obj[0].cost);
				    		}
						}
						if(json_obj.length==0){
							$("#warning").html("<h3>該條碼無產品存在</h3>請至'商品管理'介面&nbsp;定義該條碼。");
							$("#warning").dialog("open");
						}
		            }
	    		});
	        })
	        .bind('scannerDetectionError',function(e,data){console.log('detection error '+data.string);})
	        .bind('scannerDetectionReceive',function(e,data){console.log(data);});
	});
	$(function() {
		
		$(".bdyplane").animate({"opacity":"1"});
		var uuid = "";
		var seqNo = "";
		var supply_id ="";
		var purchase_id ="";
		var product_id = "";
		var c_product_id = "";
		var product_name = "";
		//=============自定義validator=============
		//字符最大長度驗證（一個中文字符長度為2）
		jQuery.validator.addMethod("stringMaxLength", function(value, element, param) { 
			var length = value.length; 
			for ( var i = 0; i < value .length; i++) { 
				if (value.charCodeAt(i) > 127) { 
				length++; 
				} 
			} 
			return this.optional(element) || (length <= param); 
		}, $.validator.format("長度不能大於{0}!"));
		//字母數字
		jQuery.validator.addMethod("alnum", function(value, element) {
		return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
		}, "只能包括英文字母和數字");
		//=========================================
		//驗證
		$("#purchase_date_form").validate({ rules : { purchase_start_date : { dateISO : true }, purchase_end_date:{ dateISO : true } }, messages:{ purchase_start_date : { dateISO : "日期格式錯誤" }, purchase_end_date : { dateISO : "日期格式錯誤" } } }); 
		var validator_insert = $("#insert-dialog-form-post").validate({ rules : { supply_id : { stringMaxLength : 40, required : true }, memo:{ stringMaxLength : 200 }, purchase_date : { dateISO : true, required : true }, invoice : { stringMaxLength : 12, alnum :true }, amount : { required : true, digits :true, min : 1 }, return_date : { dateISO : true } } }); 
		var validator_update = $("#update-dialog-form-post").validate({ rules : { supply_id : { stringMaxLength : 40, required : true }, memo:{ stringMaxLength : 200 }, purchase_date : { dateISO : true }, invoice : { stringMaxLength : 12, alnum :true }, amount : { required : true, digits :true, min : 1 }, return_date : { dateISO : true } } }); 
		var validator_detail_insert = $("#detail-insert-dialog-form-post").validate({ rules : { insert_detail_product_name : { stringMaxLength : 80, required : true }, memo:{ stringMaxLength : 200 }, quantity : { required : true, digits :true }, tmp_value : { required : true, number :true } , cost : { required : true, number :true } } }); 
		var validator_detail_update = $("#detail-update-dialog-form-post").validate({ rules : { update_detail_product_name : { stringMaxLength : 80, required : true }, memo:{ stringMaxLength : 200 }, quantity : { required : true, digits :true }, tmp_value : { required : true, number :true }, cost : { required : true, number :true } } }); 
		//進貨日查詢相關設定
		$("#search_purchase_date").click(function(e) {
			e.preventDefault();
			$("#purchase_detail_contain_row").hide();
			if($("#purchase_date_form").valid()){
				var tmp={
						action : "search_purchase_date",
						purchase_start_date: $("#purchase_start_date").val(),
						purchase_end_date: $("#purchase_end_date").val()
					};
				draw_purchase(tmp);
				
			}
		});		
		//供應商ID查詢相關設定
		$("#searh_supply_name").click(function(e) {
			e.preventDefault();
			if($("#searh_supply_name").attr("supply_error").length>0){
			    var tmp="查無供應商名稱: "+$("#searh_supply_name").attr("supply_error")+"\n將為您查詢所有進貨單";
			    if(!confirm(tmp,"繼續","取消") ){
			        return;
			    }
			}
			
			var tmp={
					action : "search",
					supply_name : $("input[name='searh_purchase_by_supply_name'").val()
				};
			draw_purchase(tmp);
		});
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
							purchase_id : uuid
						};
					draw_purchase(tmp);
					$(this).dialog("close");
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		$("#dialog-confirm").show();
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
											memo : $("#dialog-form-insert input[name='memo']").val(),
											purchase_date : $("#dialog-form-insert input[name='purchase_date']").val(),
											invoice : $("#dialog-form-insert input[name='invoice']").val(),
											invoice_type : $("#insert_select_invoice_type option:selected").val(),
											supply_id : supply_id,
											supply_name : $("#dialog-form-insert input[name='supply_id']").val(),
											amount : $("#dialog-form-insert input[name='amount']").val()
										};
									draw_purchase(tmp);
									insert_dialog.dialog("close");
									$("#insert-dialog-form-post").trigger("reset");
								}
							}
						}, {
							text : "取消",
							click : function() {
								validator_insert.resetForm();
								$("#insert-dialog-form-post").trigger("reset");
								insert_dialog.dialog("close");
							}
						} ],
				close : function() {
					validator_insert.resetForm();
					$("#insert-dialog-form-post").trigger("reset");
				}
			});
			$("#dialog-form-insert").show();
		    //這邊是用供應商名稱去自動查詢，然後得到ID
	        $("#searh_purchase_by_supply_name").autocomplete({
	            minLength: 0,
	            source: function (request, response) {
	                $.ajax({
	                    url : "purchase.do",
	                    type : "POST",
	                    cache : false,
	                    delay : 1500,
	                    data : {
	                    	action : "search_supply_name",
	                        term : request.term
	                    },
	                    success: function(data) {
	                    	var json_obj = $.parseJSON(data);
	                    	response($.map(json_obj, function (item) {
	                            return {
	                              label: item.supply_name,
	                              value: item.supply_name,
	                              supply_id: item.supply_id
	                            }
	                          }))
	                    }
	                });
	            },
	            change: function(event, ui) {
	    	        var source = $(this).val();
	    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
	    	        var found = $.inArray(source, temp);
	    	
	    	        if(found < 0) {
	    	        	$("#searh_supply_name").attr("supply_error",$(this).val());
	    	            $(this).val('');
	    	            $(this).attr("placeholder","輸入正確的供應商名稱!");
	    	            setTimeout(function(){$("#searh_supply_name").attr("supply_error","");}, 200);
	    	        }
	    	    }     
	         });
      	$("#searh_purchase_by_supply_name").bind('focus', function(){ 
        	$(this).attr("placeholder","輸入供應商名稱");
        	var eve=jQuery.Event("keydown");
        	eve.which=40;
          	$(this).trigger(eve);
        });
      	$('#searh_purchase_by_supply_name').bind('autocompleteselect', function (e, ui) {
        	supply_id = ui.item.supply_id;
        });
      	 
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
						//alert($("#dialog-form-update input[name='supply_id']").val());
						var tmp={
	 							action : "update",
	 							purchase_id : uuid,
	 							seq_no :seqNo,
								supply_id : supply_id,
								supply_name : $("#dialog-form-update input[name='supply_id']").val(),
								memo : $("#dialog-form-update input[name='memo']").val(),
								purchase_date : $("#dialog-form-update input[name='purchase_date']").val(),
								invoice : $("#dialog-form-update input[name='invoice']").val(),
								invoice_type : $("#update_select_invoice_type option:selected").val(),
								amount : $("#dialog-form-update input[name='amount']").val()
							};
						draw_purchase(tmp);
						$("#update-dialog-form-post").trigger("reset");
						update_dialog.dialog("close");
					}
				}
			}, {
				text : "取消",
				click : function() {
					validator_update.resetForm();
					$("#update-dialog-form-post").trigger("reset");
					update_dialog.dialog("close");
				}
			} ],
			close : function() {
				$("#update-dialog-form-post").trigger("reset");
				validator_update.resetForm();
			}
		});
		$("#dialog-form-update").show();
		//修改事件聆聽
		$("#purchases").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			uuid = $(this).val();
			seqNo = $(this).attr("id");
			$.ajax({
				type : "POST",
				url : "purchase.do",
				data : {
					action : "search",
					supply_name : ""
				},
				success : function(result) {
					//console.log(result);
						//TODOTODOTODO
						var json_obj = $.parseJSON(result);
						//判斷查詢結果
						var resultRunTime = 0;
						$.each (json_obj, function (i) {
							resultRunTime+=1;
						});
						var result_table = "";
						if(resultRunTime!=0){
							$.each(json_obj,function(i, item) {
								if(json_obj[i].purchase_id==uuid){
									var tmp;
									$.ajax({
										type : "POST",
										url : "purchase.do",
										async : false,
										data : {
											action : "get_supply_name",
											supply_id : json_obj[i].supply_id
										},success : function(result) {
											tmp=result;
										}
									});
									$("#dialog-form-update input[name='supply_id']").val(tmp);
									$("#update_select_invoice_type").val(json_obj[i].invoice_type);
									$("#dialog-form-update input[name='memo']").val(json_obj[i].memo);
									$("#dialog-form-update input[name='invoice']").val(json_obj[i].invoice);
									$("#dialog-form-update input[name='amount']").val(json_obj[i].amount);
									$("#dialog-form-update input[name='purchase_date']").val(json_obj[i].purchase_date);							
								}
							});
						}
					}
				});			
			update_dialog.dialog("open");
		});
		//新增  廠商欄位autocomplete
        $("#insert_supply_id").autocomplete({
            minLength: 0,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_supply_name",
                        term : request.term
                    },
                    success: function(data) {
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                    		supply_id = item.supply_id;
                            return {
                              label: item.supply_name,
                              value: item.supply_name,
                              supply_id : item.supply_id
                            }
                          }))
                    }
                });
            },
            change: function(event, ui) {
    	        var source = $(this).val();
    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
    	        var found = $.inArray(source, temp);
    	        if(found < 0) {
    	            $(this).val('');
    	            $(this).attr("placeholder","輸入正確的供應商名稱!");
    	        }
    	    }     
         });
  		$("#insert_supply_id").bind('click', function(){   
  	    	$(this).attr("placeholder","輸入供應商名稱");
  	    	var eve=jQuery.Event("keydown");
  	    	eve.which=40;
  	      	$(this).trigger(eve);
  	    } );
	    $('#insert_supply_id').bind('autocompleteselect', function (e, ui) {
	    	supply_id = ui.item.supply_id;
	    }); 		
		//修改  autocomplete
        $("#update_supply_id").autocomplete({
            minLength: 0,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_supply_name",
                        term : request.term
                    },
                    success: function(data) {
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                    		supply_id = item.supply_id;
                            return {
                              label: item.supply_name,
                              value: item.supply_name,
                              supply_id : item.supply_id
                            }
                          }))
                    }
                });
            },
            change: function(event, ui) {
    	        var source = $(this).val();
    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
    	        var found = $.inArray(source, temp);
    	
    	        if(found < 0) {
    	            $(this).val('');
    	            $(this).attr("placeholder","輸入正確的供應商名稱!");
    	        }
    	    }     
         });
  		$("#update_supply_id").bind('click', function(){  
  	    	$(this).attr("placeholder","輸入供應商名稱");
  	    	var eve=jQuery.Event("keydown");
  	    	eve.which=40;
  	      	$(this).trigger(eve);
  	     } );
	    $('#update_supply_id').bind('autocompleteselect', function (e, ui) {
	    	supply_id = ui.item.supply_id;
	    }); 
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#purchases").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			$("#dialog-confirm").html(
				"<table class='dialog-table'>"+
				"<tr><td>銷貨單號：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td:nth-child(1)").attr("name")+"'</span></td></tr>"+
				"<tr><td>交易日期：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td:nth-child(2)").attr("name")+"'</span></td></tr>"+
				"<tr><td>銷貨金額：</td><td><span class='delete_msg'>'"+$(this).parents("tr").find("td:nth-child(5)").html()+"'</span></td></tr>"+
				"</table>"
			);
			confirm_dialog.dialog("open");
		});
		//明細事件聆聽
		$("#purchases").delegate(".btn_detail", "click", function() {
			var tmp={
				action : "select_all_purchasedetail",
				purchase_id : $(this).val()
			};
			draw_purchase_detail(tmp);
			lookdown();
		});
		//明細新增Dialog相關設定
		detail_insert_dialog = $("#detail-dialog-form-insert").dialog(
		{
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : 800, modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{	
						text : "新增",
						click : function() {
							if ($('#detail-insert-dialog-form-post').valid()) {
								var tmp={
										action : "insert_detail",
			 							purchase_id :purchase_id,
			 							product_id : product_id,
			 							c_product_id : c_product_id,
			 							product_name : product_name,
										memo : $("#detail-dialog-form-insert input[name='memo']").val(),
										quantity : $("#detail-dialog-form-insert input[name='quantity']").val(),
										cost : $("#detail-dialog-form-insert input[name='cost']").val()
									};
								draw_purchase_detail(tmp);
								
								detail_insert_dialog.dialog("close");
								$("#detail-insert-dialog-form-post").trigger("reset");
								lookdown();
							}
						}
					}, {
						text : "取消",
						click : function() {
							validator_detail_insert.resetForm();
							$("#detail-insert-dialog-form-post").trigger("reset");
							detail_insert_dialog.dialog("close");
						}
					} ],
			close : function() {
				validator_detail_insert.resetForm();
				$("#detail-insert-dialog-form-post").trigger("reset");
			}
		});
		$("#detail-dialog-form-insert").show();
		//明細新增事件聆聽 
		$("#purchases").delegate(".btn_create", "click", function() {
			purchase_id = $(this).val();
			detail_insert_dialog.dialog("open");
			new_or_edit=1;
		});		
		///明細確認Dialog相關設定(刪除功能)
		confirm_detail_dialog = $("#dialog-detail-confirm").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認刪除" : function() {
					var tmp={
						action : "delete_detail",
						purchaseDetail_id : uuid,
						purchase_id : purchase_id
					};
					draw_purchase_detail(tmp);
					$(this).dialog("close");
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		$("#dialog-detail-confirm").show();
		//明細刪除事件聆聽 
		
		$("#purchase-detail-table").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			purchase_id= $(this).attr("id");
			$("#dialog-detail-confirm").html(
				"<table class='dialog-table'><tr>"+
				"<td>進貨 <span class='delete_msg'>'"+$(this).parents("tr").find("td:nth-child(2)").attr("name")+"'</span> X "+
				"<span class='delete_msg'>'"+$(this).parents("tr").find("td:nth-child(3)").attr("name")+"'</span><br></td>"+
				"</tr></table>"
			);
			confirm_detail_dialog.dialog("open");
		});
		//明細修改事件聆聽
		$("#purchase-detail-table").delegate(".btn_update", "click", function(e) {
			e.preventDefault();
			new_or_edit=2;
			uuid = $(this).val();
			purchase_id = $(this).attr("id");
			product_id = $(this).attr("name");
			$("#update_detail_c_product_id").autocomplete('search');
			$("#detail_dialog_form_update input[name='update_detail_c_product_id']").val($(this).parents("tr").find("td:nth-child(1)").attr("name"));
			$("#detail_dialog_form_update input[name='update_detail_product_name']").val($(this).parents("tr").find("td:nth-child(2)").attr("name"));
			$("#detail_dialog_form_update input[name='quantity']").val($(this).parents("tr").find("td:nth-child(3)").attr("name"));
			$("#detail_dialog_form_update input[name='cost']").val($(this).parents("tr").find("td:nth-child(4)").attr("name"));
			$("#update_tmp_value").val($(this).parents("tr").find("td:nth-child(4)").attr("name"));
			$("#update_exchange_msg").html(currency_unit($("#update_currency").find("option:selected").text())+$("#update_tmp_value").val()+" x "+$("#update_currency").val()+" = NT$"+$("#update_detail_product_price").val());
			$("#detail_dialog_form_update input[name='total']").val($(this).parents("tr").find("td:nth-child(3)").attr("name")*$(this).parents("tr").find("td:nth-child(4)").attr("name"));
			$("#detail_dialog_form_update input[name='memo']").val($(this).parents("tr").find("td:nth-child(5)").attr("name"));
			detail_update_dialog.dialog("open");
		});	
		//修改detail update Dialog相關設定
		detail_update_dialog = $("#detail_dialog_form_update").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : 800 , modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : [{
				text : "修改",
				click : function() {
					if ($('#detail-update-dialog-form-post').valid()) {
						var tmp={
	 							action : "update_detail",
	 							purchaseDetail_id : uuid,
	 							purchase_id :purchase_id,
	 							product_id : product_id,
	 							c_product_id : $("#detail_dialog_form_update input[name='update_detail_c_product_id']").val(),
	 							product_name : $("#detail_dialog_form_update input[name='update_detail_product_name']").val(),
								memo : $("#detail_dialog_form_update input[name='memo']").val(),
								quantity : $("#detail_dialog_form_update input[name='quantity']").val(),
								cost : $("#detail_dialog_form_update input[name='cost']").val()
							};
						draw_purchase_detail(tmp);
						detail_update_dialog.dialog("close");
						$("#detail-update-dialog-form-post").trigger("reset");
					}
				}
			}, {
				text : "取消",
				click : function() {
					validator_detail_update.resetForm();
					$("#detail-update-dialog-form-post").trigger("reset");
					detail_update_dialog.dialog("close");
				}
			} ],
			close : function() {
				$("#detail-update-dialog-form-post").trigger("reset");
				validator_detail_update.resetForm();
			}
		});
		$("#detail_dialog_form_update").show();
		//detail update autocomplete
        $("#update_detail_c_product_id").autocomplete({
            minLength: 1,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_product_data",
                        term : request.term,
                        identity : "ID"
                    },
                    success: function(data) {
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                            return {
	                        	 label: item.c_product_id,
	                             value: item.c_product_id,
	                             product_id: item.product_id,
	                             product_name: item.product_name,
	                             c_product_id: item.c_product_id,
	                             price: item.price,
	                             cost: item.cost
                            }
                          }))
                    }
                });
            },
            change: function(event, ui) {
            	//alert($(this).val());
    	        var source = $(this).val();
    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
    	        var found = $.inArray(source, temp);
    	        if(found < 0) {
    	            $(this).val('');
    	            $(this).attr("placeholder","輸入正確的自訂產品ID名稱!");
    	        }
    	    }     
         });
  		$("#update_detail_c_product_id").bind('focus', function(){ $(this).attr("placeholder","輸入自訂產品ID查詢"); } );
	    $('#update_detail_c_product_id').bind('autocompleteselect', function (e, ui) {
	    	product_id = ui.item.product_id;
	    	product_name = ui.item.product_name;
	    	c_product_id = ui.item.c_product_id;
	    	$("#update_detail_product_name").val(ui.item.product_name);
	    	$("#update_detail_product_n").val("1");
	    	$("#update_tmp_value").val(ui.item.cost);
	    	$("#update_detail_product_price").val(ui.item.cost);
	    	$("#update_detail_product_cost").val(ui.item.cost);
	    	$("#update_exchange_msg").html(currency_unit($("#update_currency").find("option:selected").text())+$("#update_tmp_value").val()+" x "+$("#update_currency").val()+" = NT$"+$("#update_detail_product_price").val());
	    });
        $("#update_detail_product_name").autocomplete({
            minLength: 1,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_product_data",
                        term : request.term,
                        identity : "NAME"
                    },
                    success: function(data) {
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                            return {
	                        	 label: item.product_name,
	                             value: item.product_name,
	                             product_id: item.product_id,
	                             product_name: item.product_name,
	                             c_product_id: item.c_product_id,
	                             price: item.price,
	                             cost: item.cost
                            }
                          }))
                    }
                });
            },
            change: function(event, ui) {
    	        var source = $(this).val();
    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
    	        var found = $.inArray(source, temp);
    	
    	        if(found < 0) {
    	            $(this).val('');
    	            $(this).attr("placeholder","輸入正確的產品名稱!");
    	        }
    	    }     
         });
  		$("#update_detail_product_name").bind('focus', function(){ $(this).attr("placeholder","輸入產品名稱以供查詢"); } );
	    $('#update_detail_product_name').bind('autocompleteselect', function (e, ui) {
	    	product_id = ui.item.product_id;
	    	product_name = ui.item.product_name;
	    	c_product_id = ui.item.c_product_id;
	    	$("#update_detail_c_product_id").val(ui.item.c_product_id);
	    	$("#update_detail_product_n").val("1");
	    	$("#update_tmp_value").val(ui.item.cost);
	    	$("#update_detail_product_price").val(ui.item.cost);
	    	$("#update_detail_product_cost").val(ui.item.cost);
	    	$("#update_exchange_msg").html(currency_unit($("#update_currency").find("option:selected").text())+$("#update_tmp_value").val()+" x "+$("#update_currency").val()+" = NT$"+$("#update_detail_product_price").val());
	    });
		//detail insert autocomplete
        $("#insert_detail_c_product_id").autocomplete({
            minLength: 1,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_product_data",
                        term : request.term,
                        identity : "ID"
                    },
                    success: function(data) {
                    	console.log(data);
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                            return {
	                        	 label: item.c_product_id,
	                             value: item.c_product_id,
	                             product_id: item.product_id,
	                             product_name: item.product_name,
	                             c_product_id: item.c_product_id,
	                             price: item.price,
	                             cost: item.cost
                            }
                          }))
                    }
                });
            },
            change: function(event, ui) {
    	        var source = $(this).val();
    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
    	        var found = $.inArray(source, temp);
    	
    	        if(found < 0) {
    	            $(this).val('');
    	            $(this).attr("placeholder","輸入正確的自訂產品ID名稱!");
    	        }
    	    }     
         });
  		$("#insert_detail_c_product_id").bind('focus', function(){ $(this).attr("placeholder","輸入自訂產品ID查詢"); } );
	    $('#insert_detail_c_product_id').bind('autocompleteselect', function (e, ui) {
	    	product_id = ui.item.product_id;
	    	product_name = ui.item.product_name;
	    	c_product_id = ui.item.c_product_id;
	    	$("#insert_detail_product_name").val(ui.item.product_name);
	    	$("#insert_detail_product_n").val("1");
	    	$("#insert_tmp_value").val(ui.item.cost);
	    	$("#insert_detail_product_price").val(ui.item.cost);
	    	$("#insert_detail_product_cost").val(ui.item.cost);
	    	$("#insert_exchange_msg").html(currency_unit($("#insert_currency").find("option:selected").text())+$("#insert_tmp_value").val()+" x "+$("#insert_currency").val()+" = NT$"+$("#insert_detail_product_price").val());
	    });
        $("#insert_detail_product_name").autocomplete({
            minLength: 1,
            source: function (request, response) {
                $.ajax({
                    url : "purchase.do",
                    type : "POST",
                    cache : false,
                    delay : 1500,
                    data : {
                    	action : "search_product_data",
                        term : request.term,
                        identity : "NAME"
                    },
                    success: function(data) {
                    	var json_obj = $.parseJSON(data);
                    	response($.map(json_obj, function (item) {
                            return {
	                        	 label: item.product_name,
	                             value: item.product_name,
	                             product_id: item.product_id,
	                             product_name: item.product_name,
	                             c_product_id: item.c_product_id,
	                             price: item.price,
	                             cost: item.cost
                            }
                          }))
                    }
                });
            },
            change: function(event, ui) {
    	        var source = $(this).val();
    	            var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
    	        var found = $.inArray(source, temp);
    	
    	        if(found < 0) {
    	            $(this).val('');
    	            $(this).attr("placeholder","輸入正確的產品名稱!");
    	        }
    	    }
         });
  		$("#insert_detail_product_name").bind('focus', function(){ $(this).attr("placeholder","輸入產品名稱以供查詢"); } );
	    $('#insert_detail_product_name').bind('autocompleteselect', function (e, ui) {
	    	product_id = ui.item.product_id;
	    	product_name = ui.item.product_name;
	    	c_product_id = ui.item.c_product_id;
	    	$("#insert_detail_c_product_id").val(ui.item.c_product_id);
	    	$("#insert_detail_product_n").val("1");
	    	$("#insert_tmp_value").val(ui.item.cost);
	    	$("#insert_detail_product_price").val(ui.item.cost);
	    	$("#insert_detail_product_cost").val(ui.item.cost);
	    	$("#insert_exchange_msg").html(currency_unit($("#insert_currency").find("option:selected").text())+$("#insert_tmp_value").val()+" x "+$("#insert_currency").val()+" = NT$"+$("#insert_detail_product_price").val());
	    });
		//新增事件聆聽
		$("#create-supply").click( function(e) {
			e.preventDefault();		
			insert_dialog.dialog("open");
			$("#insert_exchange_msg").html(currency_unit($("#insert_currency").find("option:selected").text())+$("#insert_tmp_value").val()+" x "+$("#insert_currency").val()+" = NT$"+$("#insert_detail_product_price").val());
			//@@@
		});
		$("#insert_tmp_value").change(function(e){//'折合台幣：'*4
			$("#insert_detail_product_price").val(Math.round($("#insert_tmp_value").val()*$("#insert_currency").val()*10000) / 10000);
			$("#insert_exchange_msg").html(currency_unit($("#insert_currency").find("option:selected").text())+$("#insert_tmp_value").val()+" x "+$("#insert_currency").val()+" = NT$"+$("#insert_detail_product_price").val());
			$("#insert_detail_product_cost").val($("#insert_detail_product_n").val()*$("#insert_detail_product_price").val());
		});
		$("#insert_currency").change(function(e){
			$(".currency1").html("("+$("#insert_currency").find("option:selected").text()+")");
			$("#insert_detail_product_price").val(Math.round($("#insert_tmp_value").val()*$("#insert_currency").val()*10000) / 10000);
			$("#insert_exchange_msg").html(currency_unit($("#insert_currency").find("option:selected").text())+$("#insert_tmp_value").val()+" x "+$("#insert_currency").val()+" = NT$"+$("#insert_detail_product_price").val());
			$("#insert_detail_product_cost").val($("#insert_detail_product_n").val()*$("#insert_detail_product_price").val());
		});
		$("#update_tmp_value").change(function(e){
			$("#update_detail_product_price").val(Math.round($("#update_tmp_value").val()*$("#update_currency").val()*10000) / 10000);
			$("#update_exchange_msg").html(currency_unit($("#update_currency").find("option:selected").text())+$("#update_tmp_value").val()+" x "+$("#update_currency").val()+" = NT$"+$("#update_detail_product_price").val());
			$("#update_detail_product_cost").val($("#update_detail_product_n").val()*$("#update_detail_product_price").val());
		});
		$("#update_currency").change(function(e){
			$(".currency2").html("("+$("#update_currency").find("option:selected").text()+")");
			$("#update_detail_product_price").val(Math.round($("#update_tmp_value").val()*$("#update_currency").val()*10000) / 10000);
			$("#update_exchange_msg").html(currency_unit($("#update_currency").find("option:selected").text())+$("#update_tmp_value").val()+" x "+$("#update_currency").val()+" = NT$"+$("#update_detail_product_price").val());
			$("#update_detail_product_cost").val($("#update_detail_product_n").val()*$("#update_detail_product_price").val());
		});
		
		$("#insert_detail_product_n").change(function(e){
			$("#insert_detail_product_cost").val($("#insert_detail_product_n").val()*$("#insert_detail_product_price").val());
			$("#insert_exchange_msg").html(currency_unit($("#insert_currency").find("option:selected").text())+$("#insert_tmp_value").val()+" x "+$("#insert_currency").val()+" = NT$"+$("#insert_detail_product_price").val());
		});
		$("#update_detail_product_n").change(function(e){
			$("#update_detail_product_cost").val($("#update_detail_product_n").val()*$("#update_detail_product_price").val());
			$("#update_exchange_msg").html(currency_unit($("#update_currency").find("option:selected").text())+$("#update_tmp_value").val()+" x "+$("#update_currency").val()+" = NT$"+$("#update_detail_product_price").val());
		});
		
// 		$("#insert_detail_product_price").change(function(e){
// 			$("#insert_detail_product_cost").val($("#insert_detail_product_n").val()*$("#insert_detail_product_price").val());
// 		});
		
// 		$("#update_detail_product_price").change(function(e){
// 			$("#update_detail_product_cost").val($("#update_detail_product_n").val()*$("#update_detail_product_price").val());
// 		});
		
		

		$("#warning").dialog({
			title: "警告",
			draggable : true,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : "auto",
			modal : true,
			show : {effect : "bounce",duration : 1000},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認" : function() {$(this).dialog("close");}
			}
		});
		$.ajax({
			type : "POST",
			url : "exchange.do",
			data : {action : "search"},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				var result_select="<option value='1'>台幣</option>";// ="<select  name='' id=''><option value='1'>台幣</option>";
				$.each(json_obj,function(i, item) {
					result_select += "<option value='"+json_obj[i].exchange_rate+"'>"+json_obj[i].currency+"</option>";
				});
				$("#update_currency").html(result_select);
				$("#insert_currency").html(result_select);
			}
		});
		$(".input-field-wrap").append("<div class='div_right_bottom upup'><img src='./images/upup.png'></div>");
		$(".input-field-wrap").after("<div class='div_right_top downdown' style='display:none;'><img src='./images/downdown.png'></div>");
		$(".upup").click(function(){
			$(".input-field-wrap").slideToggle("slow");
			$(".downdown").slideToggle();
		});
		$(".downdown").click(function(){
			$(".input-field-wrap").slideToggle("slow");
			$(".downdown").slideToggle();
		});
	});
</script>
	<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">供應商名稱查詢</span>
						<input type="text" id="searh_purchase_by_supply_name" name="searh_purchase_by_supply_name">
					</label>
					<button class="btn btn-darkblue" id="searh_supply_name" supply_error="">查詢</button>
				</div>
				<div class="form-row">
				<form id="purchase_date_form" name="purchase_date_form">
					<label for="">
						<span class="block-label">進貨起日</span>
						<input type="text" class="input-date" id="purchase_start_date" name="purchase_start_date">
					</label>
					<div class="forward-mark"></div>
					<label for="">
						<span class="block-label">進貨迄日</span>
						<input type="text" class="input-date" id="purchase_end_date" name="purchase_end_date">
					</label>
					<button class="btn btn-darkblue" id="search_purchase_date">查詢</button>
				</form>
				</div>
				<div class="btn-row">
					<button class="btn btn-exec " id="create-supply">新增進貨資料</button>
				</div>
				<div id="supply_name_err_mes"></div>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
		
		
		<div class="datalistWrap">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="是否刪除此進貨紀錄?" style="display:none;"></div>
			<!--對話窗樣式-確認 -->
			<div id="dialog-detail-confirm" title="是否刪除此明細?" style="display:none;"></div>		
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改進貨資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
						<table class="form-table">
							<tr>
								<td>供應商：</td>
								<td><input type="text" name="supply_id" id="update_supply_id" placeholder="輸入供應商名稱已供查詢"></td>
							</tr>
							<tr>
								<td>進貨發票號碼：</td>
								<td><input type="text" name="invoice"  placeholder="輸入進貨發票號碼"></td>
								<td>發票金額：</td>
								<td><input type="text" name="amount"  placeholder="輸入發票金額"></td>
							</tr>
							<tr>
								<td>進貨日期：</td>
								<td><input type="text" name="purchase_date"  placeholder="輸入進貨日期" class="input-date"></td>
								<td>發票樣式：</td>
								<td><select name="invoice_type" id="update_select_invoice_type"><option value="0">選擇</option><option value="1">二聯式發票</option><option value="2">三聯式發票</option></select></td>
							</tr>
							<tr>
								<td>備註說明：</td>
								<td><input type="text" name="memo" placeholder="輸入備註說明"></td>
								<td></td>
								<td></td>
							</tr>
						</table>
						
					</fieldset>
				</form>
			</div>
			<!--對話窗樣式-detail-修改 -->
			<div id="detail_dialog_form_update" title="修改明細資料" style="display:none;">
				<form name="detail-update-dialog-form-post" id="detail-update-dialog-form-post">
					<font color=red style="padding-left:26px">掃條碼亦可取得商品資料</font>
					<fieldset>
						<table class="form-table">
							<tr>
								<td>自訂產品ID：</td>
								<td><input type="text" id="update_detail_c_product_id"name="update_detail_c_product_id"  placeholder="輸入自訂產品ID"></td>
								<td>產品名稱：</td>
								<td><input type="text" id="update_detail_product_name"name="update_detail_product_name" placeholder="輸入產品名稱"></td>
							</tr>
							<tr>
								<td>幣別：</td>
								<td><select id='update_currency'></select></td>
							</tr>
							<tr>
								<td>進貨單價：<a class='currency1'></a></td>
								<td><input type="text" id="update_tmp_value" name='tmp_value' placeholder="輸入單價"></td>
								<td>折合台幣單價：</td>
								<td><a id='update_exchange_msg'>NT＄0 x 1 = NT$0</a><input type="hidden" id="update_detail_product_price" name="cost" disabled></td>
							</tr>
							<tr>
								<td>進貨數量：</td>
								<td><input type="text" id="update_detail_product_n" name="quantity"  placeholder="輸入進貨數量"></td>
								<td>總價格：</td>
								<td><input type="text" id="update_detail_product_cost" name="total" disabled></td>
							</tr>
							<tr>
								<td>備註說明：</td>
								<td><input type="text" name="memo"  placeholder="輸入備註說明"></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>						
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增進貨資料" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post">
					<fieldset>
						<table class="form-table">
							<tr>
								<td>進貨單號：</td>
								<td><input id="title-dialog-post"type="text" name="original_seq_no" disabled="disabled" value="系統自動產生"></td>
								<td>供應商名稱：</td>
								<td><input type="text" name="supply_id" id="insert_supply_id" placeholder="輸入供應商名稱查詢"></td>
							</tr>
							<tr>
								<td>進貨發票號碼：</td>
								<td><input type="text" name="invoice" placeholder="輸入進貨發票號碼"></td>
								<td>發票金額：</td>
								<td><input type="text" name="amount" placeholder="輸入發票金額"></td>
							</tr>
							<tr>
								<td>進貨日期：</td>
								<td><input type="text" name="purchase_date" placeholder="輸入進貨日期" class="input-date"></td>
								<td>發票樣式：</td>
								<td><select name="invoice_type" id="insert_select_invoice_type"><option value="0">選擇</option><option value="1">二聯式發票</option><option value="2">三聯式發票</option></select></td>
							</tr>
							<tr>
								<td>備註說明：</td>
								<td><input type="text" name="memo" placeholder="輸入備註說明"></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>
			<!--對話窗樣式-明細新增 -->
			<div id="detail-dialog-form-insert" title="新增明細資料" style="display:none;">
				<form name="detail-insert-dialog-form-post" id="detail-insert-dialog-form-post"style="display:inline">
					<fieldset>
						<font color=red style="padding-left:26px">掃條碼亦可取得商品資料</font>
						<table class="form-table">
							<tr>
								<td>自訂產品ID：</td>
								<td><input type="text" id="insert_detail_c_product_id"name="insert_detail_c_product_id"  placeholder="輸入自訂產品ID"></td>
								<td>產品名稱：</td>
								<td><input type="text" id="insert_detail_product_name"name="insert_detail_product_name" placeholder="輸入產品名稱查詢"></td>
							</tr>
							<tr>
								<td>幣別：</td>
								<td><select id='insert_currency'></select></td>
							</tr>
							<tr>
								<td>進貨單價：<a class='currency1'></a></td>
								<td><input type="text" id="insert_tmp_value" name='tmp_value' placeholder="輸入單價"></td>
								<td>折合台幣單價：</td>
								<td><a id='insert_exchange_msg'>NT＄0 x 1 = NT$0</a><input  type="hidden" id="insert_detail_product_price" name="cost" disabled></td>
							</tr>
							<tr>
								<td>進貨數量：</td>
								<td><input type="text" id="insert_detail_product_n" name="quantity" placeholder="輸入進貨數量"></td>
								<td>總價格：</td>
								<td><input type="text" id="insert_detail_product_cost" name="total" disabled></td>
							</tr>
							<tr>
								<td>備註說明：</td>
								<td><input type="text" name="memo"  placeholder="輸入備註說明"></td>
							</tr>
						</table>
					</fieldset>
				</form>
			</div>			
			<!-- 第四列 -->
			<div class="row search-result-wrap" align="center" id ="purchases_contain_row" style="margin-bottom:30px;display:none;">
				<div id="purchases-contain" class="result-table-wrap">
					<table id="purchases" class="result-table">
						<thead>
							<tr>
								<th>批次請求</th>
								<th>進貨單號</th>
								<th>進貨日期</th>
								<th style="background-image: none !important;">進貨發票號碼</th>
								<th style="background-image: none !important;">發票樣式</th>
								<th>進貨發票金額</th>
								<th style="background-image: none !important;">備註說明</th>
								<th style="min-width:75px;background-image: none !important;">功能</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
				</div>
			</div>
			<!-- 第五列 -->
			
			<div class="row search-result-wrap" align="center" id="purchase_detail_contain_row" style="display:none;">
				<hr class="hr0" style="padding-top:40px;">
				<div id="purchase-detail-contain" class="ui-widget result-table-wrap">
					<table id="purchase-detail-table" class="ui-widget ui-widget-content result-table">
						<thead>
							<tr>
								<th style="background-image: none !important;">自訂產品ID</th>
								<th style="background-image: none !important;">產品名稱</th>
								<th>進貨數量</th>
								<th>進貨單價</th>
								<th style="background-image: none !important;">備註說明 </th>
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
<div id="warning"></div>
</body>
</html>