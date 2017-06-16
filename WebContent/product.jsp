<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="template.jsp" flush="true"/>

<!DOCTYPE html>
<html>
<head>
	<title>商品管理</title>
	
	<style type="text/css">
		table.form-table {
		 	border-spacing: 10px 8px !important; 
		}
		ul, li {
			margin: 0;
			padding: 0;
			list-style: none;
		}
		.abgne_tab {
			position:relative;
			top: -43px;
			left:5px;
			clear: left;
			width: 99.3%;
			margin: 10px 0;
	
		}
		ul.tabs {
			width: 100%;
			height: 32px;
		}
		ul.tabs li {
			float: left;
			height: 31px;
			line-height: 31px;
			overflow: hidden;
			position: relative;
			margin-bottom: -1px;	/* 讓 li 往下移來遮住 ul 的部份 border-bottom */
			border:0px solid #fff;
			border-left: none;
			background:#85b9fF;
		}
		ul.tabs li a {
			display: block;
			padding: 0 20px;
			color: #000;
			border: 1px solid #fff;
			text-decoration: none;
		}
		ul.tabs li a:hover {
			background: #5599FF;
		}
		ul.tabs li.active  {
			background: #EEF3F9;
		}
		ul.tabs li.active a:hover {
			background: #EEF3F9;
		}
	</style>	

	
	<!-- Generic page styles -->
	<link rel="stylesheet" href="css/photo/style.css">
	<!-- CSS to style the file input field as button and adjust the Bootstrap progress bars -->
	<link rel="stylesheet" href="css/photo/jquery.fileupload.css">
	<link rel="stylesheet" href="css/styles.css" />
	<link href="<c:url value="css/css.css" />" rel="stylesheet">
	<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<%-- 	<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet"> --%>
	<link rel="stylesheet" href="css/buttons.dataTables.min.css">
	
	<script src="js/photo/jquery.min.js"></script>
	<!-- The jQuery UI widget factory, can be omitted if jQuery UI is already included -->
	<script src="js/photo/vendor/jquery.ui.widget.js"></script>
	<!-- The Load Image plugin is included for the preview images and image resizing functionality -->
	<script src="js/photo/load-image.all.min.js"></script>
	<!-- The Canvas to Blob plugin is included for image resizing functionality -->
	<script src="js/photo/canvas-to-blob.min.js"></script>
	<!-- Bootstrap JS is not required, but included for the responsive demo navigation -->
	<script src="js/photo/bootstrap.min.js"></script>
	<!-- The Iframe Transport is required for browsers without support for XHR file uploads -->
	<script src="js/photo/jquery.iframe-transport.js"></script>
	<!-- The basic File Upload plugin -->
	<script src="js/photo/jquery.fileupload.js"></script>
	<!-- The File Upload processing plugin -->
	<script src="js/photo/jquery.fileupload-process.js"></script>
	<!-- The File Upload image preview & resize plugin -->
	<script src="js/photo/jquery.fileupload-image.js"></script>
	<!-- The File Upload audio preview plugin -->
	<script src="js/photo/jquery.fileupload-audio.js"></script>
	<!-- The File Upload video preview plugin -->
	<script src="js/photo/jquery.fileupload-video.js"></script>
	<!-- The File Upload validation plugin -->
	<script src="js/photo/jquery.fileupload-validate.js"></script>
	   
	<script type="text/javascript" src="js/jquery-1.11.4.js"></script>

	<script type="text/javascript" src="js/photo/jquery-ui.js"></script>
	<script type="text/javascript" src="js/jquery.validate.min.js"></script>
	<script type="text/javascript" src="js/additional-methods.min.js"></script>
	<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
	<script type="text/javascript" src="js/jquery.scannerdetection.js"></script>
	<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui.min.js"></script>
	
	<!-- data table buttons -->
	<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
	<script type="text/javascript" src="js/buttons.jqueryui.min.js"></script>
	<link rel="stylesheet" href="css/buttons.dataTables.min.css">
	<script>
	
	$(function(){
	    $('#sales').on('change', ':checkbox', function() {
	        $(this).is(":checked")?
	        	$(this).closest("tr").addClass("selected"):
	        	$(this).closest("tr").removeClass("selected");
	    });	
	});
	
	var c_product_id_tags=[];
	var product_name_tags=[];
	var new_or_edit=0;
	var scan_exist=0;
	var information;
	var table;
	var $dtMaster = null;
	var selectCount = 0; //全選按鈕計算用
	function draw_product(info){
		$("#sales-contain").css({"opacity":"0"});
		$("#packages-contain").css({"opacity":"0"});
		warning_msg("---讀取中請稍候---");
					
		$("#sales").dataTable().fnDestroy();
		
			$("#sales-contain").show();
			
			$dtMaster = 
				$("#sales").DataTable({
					dom : "frB<t>ip",
					lengthChange: false,
					pageLength: 20,
					scrollY:"250px",
			        "language": {
			            "url": "js/dataTables_zh-tw.txt"
			        },
			        initComplete: function(settings, json) {
					    $('div .dt-buttons').css({'float': 'left','margin-left':'10px'});
					    $('div .dt-buttons a').css('margin-left','10px');
					},
			        ajax: {
			            dataSrc: "",
			            type: "POST",
			            url: "product.do",
			            data: info
			        },
			        columnDefs: [{
			            targets: 10,
			            render: function(data, type, row) {
			                var tmp = (row.photo == null ?
			                    "" : (row.photo.length < 1) ?
			                    "無圖片" : "<img src=./image.do?picname=" + row.photo + " style='max-width:100px;max-height:100px'>");
			                return tmp;
			            }
			        }, {
			            targets: 11,
			            render: function(data, type, row) {
			                var tmp = (row.photo1 == null ?
			                    "" : (row.photo1.length < 1) ?
			                    "無圖片" : "<img src=./image.do?picname=" + row.photo1 + " style='max-width:100px;max-height:100px'>");
			                return tmp;
			            }
			        }, {
			            targets: 0,
			            searchable: false,
			            orderable: false,
			            render: function(data, type, row) {
			                var product_id = row.c_product_id;
			                var input = document.createElement("INPUT");
			                input.type = 'checkbox';
			                input.name = 'checkbox-group-select';
			                input.id = product_id;

			                var span = document.createElement("SPAN");
			                span.className = 'form-label';

			                var label = document.createElement("LABEL");
			                label.htmlFor = product_id;
			                label.name = 'checkbox-group-select';
			                label.style.marginLeft = '10%';
			                label.appendChild(span);

			                var options = $("<div/>").append(input, label);
			                return options.html();
			            }

			        }, {
			            targets: -1,
			            searchable: false,
			            orderable: false,
			            render: function(data, type, row) {
			                var ch =
			                    "<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>" +
			                    "	<div class='table-function-list' >" +
			                    "		<button class='btn-in-table btn-darkblue btn_update' title='修改' id = '" + row.product_id +
			                    "'value='" + row.product_id + "'>" +
			                    "<i class='fa fa-pencil'></i></button>" +
			                    "		<button class='btn-in-table btn-alert btn_delete' title='刪除' id = '" + row.product_id +
			                    "'value='" + row.product_id + "'>" +
			                    "<i class='fa fa-trash'></i></button>" +
			                    "	</div>" +
			                    "</div>";
			                return ch;
			            }

			        }],
			        columns: [{
			                "data": null,
			                "defaultContent": ""
			            },
			            {
			                "data": "c_product_id",
			                "defaultContent": ""
			            },
			            {
			                "data": "supply_name",
			                "defaultContent": ""
			            },
			            {
			                "data": "product_name",
			                "defaultContent": ""
			            },
			            {
			                "data": "description",
			                "defaultContent": ""
			            },
			            {
			                "data": "type_id",
			                "defaultContent": ""
			            },
			            {
			                "data": "unit_id",
			                "defaultContent": ""
			            },
			            {
			                "data": "cost",
			                "defaultContent": ""
			            },
			            {
			                "data": "price",
			                "defaultContent": ""
			            },
			            {
			                "data": "keep_stock",
			                "defaultContent": ""
			            },
			            {
			                "data": "photo",
			                "defaultContent": ""
			            },
			            {
			                "data": "photo1",
			                "defaultContent": ""
			            },
			            {
			                "data": "barcode",
			                "defaultContent": ""
			            },
			            {
			                "data": null,
			                "defaultContent": ""
			            },
			        ],
			        buttons: [{
			                text: '全選',
			                action: function(e, dt, node, config) {

			                    selectCount++;
			                    var $table = $('#sales');
			                    var $checkboxs = $table.find('input[name=checkbox-group-select]');

			                    selectCount % 2 != 1 ?
			                        $checkboxs.each(function() {
			                            $(this).prop("checked", false);
			                            $(this).removeClass("toggleon");
			                            $(this).closest("tr").removeClass("selected");
			                        }) :
			                        $checkboxs.each(function() {
			                            $(this).prop("checked", true);
			                            $(this).addClass("toggleon");
			                            $(this).closest("tr").addClass("selected");
			                        });
			                }
			            }, {
			                text: '順豐商品',
			                action: function(data, row) {
			                    var c_product_ids = '';

			                    var cells = $dtMaster.cells().nodes();


			                    var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');


			                    if ($checkboxs.length == 0) {
			                        alert('請至少選擇一筆資料');
			                        return false;
			                    }
			                    if ($checkboxs.length > 20) {
			                        alert('最多選擇二十筆資料');
			                        return false;
			                    }

			                    $checkboxs.each(function() {
			                        c_product_ids += this.id + '~';
			                    });
			                    c_product_ids = c_product_ids.slice(0, -1);
			                    console.log(c_product_ids);
			                    $.ajax({
			                        url: "product.do",
			                        type: "POST",
			                        cache: false,
			                        delay: 1500,
			                        data: {
			                            action: "send_data_by_c_productc_id",
			                            c_product_ids: c_product_ids

			                        },
			                        error: function(xhr) {},
			                        success: function(response) {
			                            var $mes = $('#message #text');
			                            $mes.val('').html('成功發送<br><br>執行結果為: '+response);
			                            $('#message')
			                                .dialog()
			                                .dialog('option', 'title', '提示訊息')
			                                .dialog('option', 'width', 'auto')
			                                .dialog('option', 'minHeight', 'auto')
			                                .dialog("open");
			                        }
			                    });

			                }
			            }, {
			                text: '順豐商品查詢',
			                action: function(data, row) {
			                    var c_product_ids = '';

			                    var cells = $dtMaster.cells().nodes();


			                    var $checkboxs = $(cells).find('input[name=checkbox-group-select]:checked');


			                    if ($checkboxs.length == 0) {
			                        alert('請至少選擇一筆資料');
			                        return false;
			                    }
			                    if ($checkboxs.length > 11) {
			                        alert('最多選擇十一筆資料');
			                        return false;
			                    }

			                    $checkboxs.each(function() {
			                        c_product_ids += this.id + '~';
			                    });
			                    c_product_ids = c_product_ids.slice(0, -1);

			                    console.log(c_product_ids);
			                    $.ajax({
			                        url: "product.do",
			                        type: "POST",
			                        cache: false,
			                        delay: 1500,
			                        data: {
			                            action: "get_data_by_c_productc_id",
			                            c_product_ids: c_product_ids

			                        },
			                        success: function(response) {
			                            var $mes = $('#message #text');
			                            $mes.val('').html('成功發送<br><br>執行結果為: '+response);
			                            $('#message')
			                                .dialog()
			                                .dialog('option', 'title', '提示訊息')
			                                .dialog('option', 'width', 'auto')
			                                .dialog('option', 'minHeight', 'auto')
			                                .dialog("open");
			                        }
			                    });

			                }
			            }

			        ]
			    }


			);
			
			tooltip('btn_update');
			tooltip('btn_delete');
			
			$("#sales").find("td").css("text-align","center");
			$("#sales").find("th").css("text-align","center");
			$("#sales tbody td:nth-child(2)").css("text-align", "left");
			$("#sales tbody td:nth-child(3)").css("text-align", "left");
			$("#sales tbody td:nth-child(4)").css("text-align", "left");
			$("#sales tbody td:nth-child(5)").css("text-align", "left");
			$("#sales tbody td:nth-child(11)").css("text-align", "left");
			$("#sale tbody tr").css("line-height", "20px");
			$("#sales-contain").animate({"opacity":"0.01"},1);
			$("#sales-contain").animate({"opacity":"1"},300);
			
			warning_msg("");
	}
	
	$(document).ready(function($) {
	    $(window).scannerDetection();
	    
	    $(window).bind('scannerDetectionComplete',function(e,data){
	    	e.preventDefault();
	    	
    		if(data.string=="success"){
    			return;
    		}
    		if(new_or_edit==1){
    			$("#new_barcode").val(data.string);
    		}
    		if(new_or_edit==2){
    			$("#edit_barcode").val(data.string);
    		}
    		if(new_or_edit==0){
    			$.ajax({url : "product.do", type : "POST", cache : false,
		            data : {
		            	action : "find_barcode",
		            	barcode : data.string,
		            },
		            success: function(result) {
		            	var json_obj2 = $.parseJSON(result);
		            	if(json_obj2.length>0){
		            		information={
									action : "search_name",
									product_name : json_obj2[0].product_name,
								};
		            		
		            		draw_product(information);
		            	}else{
		            		$("#sales-contain").hide();
							$(".validateTips").text("查無此結果");
		            	}
		            }
	    		});
    		}
        })
        .bind('scannerDetectionError',function(e,data){
            console.log('detection error '+data.string);
        })
        .bind('scannerDetectionReceive',function(e,data){
            console.log(data);
        });
	    
	    $(window).scannerDetection('success');
	        var table = $('#sales').DataTable();
	});
	
	
	$(function() {
		
		$(".bdyplane").animate({"opacity":"1"});
		
		var uuid = "";
		var c_product_id="";
		var unit_id="";
		var supply_name = "";
		var type_id= "";
		var supply_id= "";
		
		//=============自定義validator=============
		//字符最大長度驗證（一個中文字符長度為2）
		$.validator.addMethod("stringMaxLength", function(value, element, param) { 
			var length = value.length; 
			for ( var i = 0; i < value .length; i++) { 
				if (value.charCodeAt(i) > 127) { 
				length++; 
				} 
			} 
			return this.optional(element) || (length <= param); 
		}, $.validator.format("長度不能大於{0}!"));
		
		//字母數字
		$.validator.addMethod("alnum", function(value, element) {
		return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
		}, "只能包括英文字母和數字");
			
		//驗證
		var validator_insert = $("#insert-dialog-form-post").validate({
			rules : {
				c_product_id : {
					maxlength : 40,
					required : true
				},
				product_name:{
					maxlength : 80,
					required : true
				},
				supply_name : {
					maxlength : 40,
					required : true
				},
				type_id: {
					required : true
				},
				unit_id: {
					required : true
				},
				cost: {
					required : true
				},
				price: {
					required : true
				},
				keep_stock: {
					required : true
				},
				description : {
					stringMaxLength : 200
				},
				barcode : {
					stringMaxLength : 40
				}
			}
		});
		
		var validator_update = $("#update-dialog-form-post").validate({
			rules : {
				c_product_id : {
					maxlength : 40,
					required : true
				},
				product_name:{
					maxlength : 80,
					required : true
				},
				supply_name : {
					maxlength : 40,
					required : true
				},
				type_id: {
					required : true
				},
				unit_id: {
					required : true
				},
				cost: {
					required : true
				},
				price: {
					required : true
				},
				keep_stock: {
					required : true
				},
				description : {
					stringMaxLength : 200
				},
				barcode : {
					stringMaxLength : 40
				}
			}
		});
		
		$("#searh-name").click(function(e) {
			e.preventDefault();
			
			information={
				action : "search_name",
				product_name : $("#searh_name").val(),
			};
			
			draw_product(information);
		});
		
		//自訂商品ID查詢相關設定
		$("#searh-sale").click(function(e) {
			e.preventDefault();
			
			information={
				action : "search",
				supply_name : $("input[name='searh_product_name'").val(),
			};
			draw_product(information);
		});
		
		//新增Dialog相關設定
		insert_dialog = $("#dialog-form-insert").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
			buttons : [{
						id : "insert_enter",
						text : "新增",
						click : function() {
							if ($('#insert-dialog-form-post').valid()) {
								information={
										action : "insert",
										c_product_id : $("#dialog-form-insert input[name='c_product_id']").val(),
										product_name : $("#dialog-form-insert input[name='product_name']").val(),
										supply_id : supply_id,
										supply_name : $("#dialog-form-insert input[name='supply_name']").val(),
										type_id : $("#dialog-form-insert select[name='select_insert_type_id']").val(),
										unit_id : $("#dialog-form-insert select[name='select_insert_unit_id']").val(),
										cost : $("#dialog-form-insert input[name='cost']").val(),
										price : $("#dialog-form-insert input[name='price']").val(),
										current_stock : $("#dialog-form-insert input[name='current_stock']").val(),
										keep_stock : $("#dialog-form-insert input[name='keep_stock']").val(),
										photo : $("#photo0").val(),
										photo1 : $("#photo1").val(),
										description : $("#dialog-form-insert input[name='description']").val(),
										barcode : $("#dialog-form-insert input[name='barcode']").val(),
										ispackage : "0"
									};
								draw_product(information);
								
								insert_dialog.dialog("close");
								$("#insert-dialog-form-post").trigger("reset");
							}
							new_or_edit=0;
						}
					}, {
						text : "取消",
						click : function() {
							$("#insert-dialog-form-post").trigger("reset");
							validator_insert.resetForm();
							insert_dialog.dialog("close");
							new_or_edit=0;
						}
					} ],
			close : function() {
				validator_insert.resetForm();
				$("#insert-dialog-form-post").trigger("reset");
			}
		}).css("width", "10%");
		
		$("#dialog-form-insert").show();
		
		$("#dialog-form-insert input[name='product_name']").focusout(function() {
			var tmp= $(this).val();
			$.ajax({
				type : "POST",
				url : "product.do",
				async : false,
				data : {
					action : "is_duplicate",
					product_name : tmp,
				},
				success : function(result) {
					if(result=="true"){
						$("#insert_enter").attr("disabled",true);
						setTimeout(function(){
							$("#insert_enter").attr("disabled",false);
						}, 200);
						$("#warning").html("同名稱之商品已存在，請更改")
						$("#warning").val($("#dialog-form-insert input[name='product_name']"));
						$("#warning").dialog("open");
					}
				}
			});
		});
		
		$("#dialog-form-update input[name='product_name']").focusout(function() {
			var tmp= $(this).val();
			$.ajax({
				type : "POST",
				url : "product.do",
				async : false,
				data : {
					action : "is_duplicate",
					product_name : tmp,
				},
				success : function(result) {
					if(result=="true"&& tmp!=$("#dialog-form-update").val()){
						$("#update_enter").attr("disabled",true);
						setTimeout(function(){
							$("#update_enter").attr("disabled",false);
							//$("#dialog-form-update input[name='product_name']").focus();
						}, 200);
						$("#warning").html("同名稱之商品已存在，請更改商品名稱");
						$("#warning").val($("#dialog-form-update input[name='product_name']"));
						$("#warning").dialog("open");
					}
				}
			});
		});
		
		//確認Dialog相關設定(刪除功能)
		confirm_dialog = $("#dialog-confirm").dialog({
			draggable : true, resizable : false, autoOpen : false,
			height : "auto", width : "auto", modal : true,
			show : {effect : "blind",duration : 300},
			hide : {effect : "fade",duration : 300},
			open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
			buttons : {
				"確認刪除" : function() {
					information={
						action : "delete",
						product_id : uuid
					};
					draw_product(information);
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
			open : function(event, ui) {$(this).parent().children().children('.ui-dialog-titlebar-close').hide();},
			buttons : [{
				id : "update_enter",
				text : "修改",
				click : function() {
					if ($('#update-dialog-form-post').valid()) {
						information={
 							action : "update",
							product_id : product_id,
 							c_product_id : $("#dialog-form-update input[name='c_product_id']").val(),
							product_name : $("#dialog-form-update input[name='product_name']").val(),
							supply_id : $("#dialog-form-update input[name='supply_id']").val(),
							supply_name : $("#dialog-form-update input[name='supply_name']").val(),
							type_id : $("#dialog-form-update select[name='select_update_type_id']").val(),
							unit_id : $("#dialog-form-update select[name='select_update_unit_id']").val(),
							cost : $("#dialog-form-update input[name='cost']").val(),
							price : $("#dialog-form-update input[name='price']").val(),
							keep_stock : $("#dialog-form-update input[name='keep_stock']").val(),
							photo : ($("#photo0-update").val() === '' ? $(this).data("photo") : $("#photo0-update").val()),
							photo1 : ($("#photo1-update").val() === '' ? $(this).data("photo1") : $("#photo1-update").val()),
							description : $("#dialog-form-update input[name='description']").val(),
							barcode : $("#dialog-form-update input[name='barcode']").val(),
							ispackage : "0"
						};
						draw_product(information);
						$("#update-dialog-form-post").trigger("reset");
						update_dialog.dialog("close");
					}
					new_or_edit=0;
				}
			}, {
				text : "取消",
				click : function() {
					validator_update.resetForm();
					$("#update-dialog-form-post").trigger("reset");
					update_dialog.dialog("close");
					new_or_edit=0;
				}
			} ],
			close : function() {
				$("#update-dialog-form-post").trigger("reset");
				validator_update.resetForm();
			}
		});
		
		$("#dialog-form-update").show();
		
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#sales").delegate(".btn_delete", "click", function() {
			
			var row = $(this).closest('tr');
			
		    var data = $("#sales").dataTable().fnGetData(row);
		    
			uuid = $(this).val();
			product_id = $(this).attr("id");
			
			$("#dialog-confirm").html("");
			$("#dialog-confirm").html("<table class='dialog-table'>"+
				"<tr><td>商品名稱：</td><td><span class='delete_msg'>'"+ data.product_name +"'</span></td></tr>"+
				"<tr><td>進貨來源：</td><td><span class='delete_msg'>'"+ data.supply_name  +"'</span></td></tr>"+
				"</table>"
			);
			confirm_dialog.dialog("open");
		});
		
		//新增事件聆聽
		$("#create-sale").click( function() {
			new_or_edit=1;
			insert_dialog.dialog("open");
			$("#files").html('');
			$("#files2").html('');
			$("#photo0").val('');
			$("#photo1").val('');
			$("#new_barcode").focus();
			scan_exist=1;
			if(!scan_exist){$("#warning").dialog("open");};
		});
		
		//修改事件聆聽
		$("#sales").delegate(".btn_update", "click", function(e) {
			new_or_edit=2;
			e.preventDefault();
			
			var row = $(this).closest('tr');
			
		    var data = $("#sales").dataTable().fnGetData(row);

			$("#files-update").html('');
			$("#files2-update").html('');
			$("#photo0-update").val('');
			$("#photo1-update").val('');
			
			uuid = $(this).val();
			
			product_id = $(this).attr("id");
			
			$("#dialog-form-update").val(data.product_name);
			$("#dialog-form-update input[name='product_id']").val(data.product_id);
			$("#dialog-form-update input[name='c_product_id']").val(data.c_product_id);
			$("#dialog-form-update input[name='product_name']").val(data.product_name);
			$("#dialog-form-update input[name='supply_id']").val(data.supply_id);
			$("#dialog-form-update input[name='supply_name']").val(data.supply_name);
			$("#dialog-form-update select[name='select_update_type_id']").val(data.type_id);
			$("#dialog-form-update select[name='select_update_unit_id']").val(data.unit_id);
			$("#dialog-form-update input[name='tmp_cost']").val(data.cost);
			$("#dialog-form-update input[name='cost']").val(data.cost);
			
			$("#update_exchange_cost").html(
					currency_unit($("#update_currency").find("option:selected").text())+
					$("#dialog-form-update input[name='tmp_cost']").val()+" x "+$("#update_currency").val()+
					" = NT$"+$("#dialog-form-update input[name='cost']").val()
			);
			
			$("#dialog-form-update input[name='tmp_price']").val(data.price);
			$("#dialog-form-update input[name='price']").val(data.price);
			
			$("#update_exchange_price").html(
					currency_unit($("#update_currency").find("option:selected").text())+
					$("#dialog-form-update input[name='tmp_price']").val()+
					" x "+$("#update_currency").val()+" = NT$"+
					$("#dialog-form-update input[name='price']").val()
			);
			
			$("#dialog-form-update input[name='keep_stock']").val(data.keep_stock);
			$("#dialog-form-update input[name='photo']").val(data.photo);
			$("#dialog-form-update input[name='photo1']").val(data.photo1);
			$("#dialog-form-update input[name='description']").val(data.description);
			$("#dialog-form-update input[name='barcode']").val(data.barcode);
			
			if (data.photo != '') {
				$("#product-photo").attr("src", "./image.do?picname=" + data.photo);
				$("#product-photo").attr("style","max-width:150px;max-height:150px;");
				$("#product-photo").attr("alt",data.photo);
			}else{
				$("#product-photo").attr("src","");
				$("#product-photo").attr("style","max-width:150px;max-height:150px;");
				$("#product-photo").attr("alt","無");
			}
			if (data.photo1 != '') {
				$("#product-photo1").attr("src", "./image.do?picname=" + data.photo1);
				$("#product-photo1").attr("style","max-width:150px;max-height:150px;");
				$("#product-photo1").attr("alt",data.photo1);
			}else{
				$("#product-photo1").attr("src","");
				$("#product-photo1").attr("style","max-width:150px;max-height:150px;");
				$("#product-photo1").attr("alt","無");
			}
			
			var row = $(this).closest('tr');
			
		    var data = $("#sales").dataTable().fnGetData(row);
		    
			update_dialog
				.data("photo",data.photo)
				.data("photo1",data.photo1)
				.dialog("open");
			$("#edit_barcode").focus();
		});
		
		$("#insert_supply_name").autocomplete({
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
		
  		$("#insert_supply_name").bind('focus', function(){  
  	    	$(this).attr("placeholder","輸入供應商名稱");
  	    	var eve=$.Event("keydown");
  	    	eve.which=40;
  	      	$(this).trigger(eve);
  	    });
  		
	    $('#insert_supply_name').bind('autocompleteselect', function (e, ui) {
	    	supply_id = ui.item.supply_id;
	    });
	    
	    $("#update_supply_name").autocomplete({
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
	    
  		$("#update_supply_name").bind('focus', function(){ 
  	    	$(this).attr("placeholder","輸入供應商名稱");
  	    	var eve=$.Event("keydown");
  	    	eve.which=40;
  	      	$(this).trigger(eve);
  	    });
  		
	    $('#update_supply_name').bind('autocompleteselect', function (e, ui) {
	    	supply_id = ui.item.supply_id;
	    });
	  
		//處理初始的查詢autocomplete 查詢的是supply
		$("#searh_product_name").autocomplete({
			minLength: 0,
			source: function (request, response) {
				$.ajax({
				    url : "product.do",
				    type : "POST",
				    cache : false,
				    delay : 1500,
				    data : {
				    	action : "search_product_data",
				    	identity : "NAME",
				        term : request.term
				    },
				    success: function(data) {
				    	var json_obj = $.parseJSON(data);
				    	response($.map(json_obj, function (item) {
				            return {
				              label: item.supply_name,
				              value: item.supply_name,
				              supply_id : item.supply_id,
				              supply_name : item.supply_name,
				            }
				          }));
				    },
				    error: function(XMLHttpRequest, textStatus, errorThrown) {
				        alert(textStatus);
				    }
				});
			},
			change: function(event, ui) {
				var source = $(this).val();
				var temp = $(".ui-autocomplete li").map(function () { return $(this).text()}).get();
				var found = $.inArray(source, temp);

				if(found < 0) {
				    $(this).val('');
				    $(this).attr("placeholder","請輸入正確廠商名稱以供查詢!");
				}
			}     
		});
		
		$("#searh_name").autocomplete({
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
					$(this).attr("placeholder","輸入正確的商品名稱!");
				}
			}
		});
		
		$("#searh_name").dblclick(function(event){
			$("#searh_name").autocomplete({
				minLength: 0
			}); 
		});
		
		$("#searh_product_name").bind('focus', function(){   
			$(this).attr("placeholder","輸入供應商名稱");
			var eve=$.Event("keydown");
			eve.which=40;
			$(this).trigger(eve);
	    });
	    
		//處理新增修改的下拉選單unit_id
		$.ajax({
			url : "product.do",
		    type : "POST",
		    cache : false,
		    delay : 1500,
		    data : {
		    	action : "search_product_data",
		    	identity : "ID",
		        term : ''
		    },
		    success: function(data) {
				var json_obj = $.parseJSON(data);
				
				$.map(json_obj, function (item) {
					if (item.unit_name != '') {
						$("#select_insert_unit_id").append("<option value='" + item.unit_name + "'>" + item.unit_name + "</option>");
						$("#select_update_unit_id").append("<option value='" + item.unit_name + "'>" + item.unit_name + "</option>");
					}
                });
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
			    alert(textStatus);
			}
		});
		//處理新增修改的下拉選單type_id
		$.ajax({
			url : "product.do",
			type : "POST",
			cache : false,
			delay : 1500,
			data : {
				action : "search_product_data",
				identity : "ID2",
			    term : ''
			},
			success: function(data) {
				var json_obj = $.parseJSON(data);
				$.map(json_obj, function (item) {
					if (item.type_name != '') {
						$("#select_insert_type_id").append("<option value='" + item.type_name + "'>" + item.type_name + "</option>");
						$("#select_update_type_id").append("<option value='" + item.type_name + "'>" + item.type_name + "</option>");
					}
				});
			},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
			    alert(textStatus);
			}
		});
		
		//<!-- photo section jquery begin by Melvin -->
		'use strict';
	    var url = window.location.hostname === 'blueimp.github.io' ?
	                '//jquery-file-upload.appspot.com/' : '/VirtualBusiness/photo.do',
	                
			uploadButton = $('<button/>')
				.addClass('btn btn-primary')
			    .prop('disabled', true)
			    .text('處理中...')
			    .on('click', function (e) {
			    	e.preventDefault();
			    	
			    	var $this = $(this),
		            data = $this.data();
			    	
			        $this
			            .off('click')
			            .text('Abort')
			            .on('click', function () {
			                $this.remove();
			                data.abort();
			            });
			        
			        data.submit().always(function () {
			            $this.remove();
			        });
			});
	                
	    $('#fileupload').fileupload({
	        url: url,
	        dataType: 'json',
	        autoUpload: false,
	        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
	        maxFileSize: 600000,
	        disableImageResize: /Android(?!.*Chrome)|Opera/
	            .test(window.navigator.userAgent),
	        previewMaxWidth: 200,
	        previewMaxHeight: 200,
	        previewCrop: true
	    }).on('fileuploadadd', function (e, data) {
	        data.context = $('<div/>').appendTo('#files');
	        $.each(data.files, function (index, file) {
	            var node = $('<p/>')
	                    .append($('<span/>').text(file.name));
	            if (!index) {
	                node
	                    .append('<br>')
	                    .append(uploadButton.clone(true).data(data));
	            }
	            node.appendTo(data.context);
	        });
	    }).on('fileuploadprocessalways', function (e, data) {
	        var index = data.index,
	            file = data.files[index],
	            node = $(data.context.children()[index]);
	        
	        if (file.preview) {
	            node
	                .prepend('<br>')
	                .prepend(file.preview);
	        }
	        
	        if (file.error) {
	            node
	                .append('<br>')
	                .append($('<span class="text-danger"/>').text(file.error));
	        }
	        
	        if (index + 1 === data.files.length) {
	            data.context.find('button')
	                .text('上傳')
	                .prop('disabled', !!data.files.error);
	        }
	    }).on('fileuploadprogressall', function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .progress-bar').css(
	            'width',
	            progress + '%'
	        );
	    }).on('fileuploaddone', function (e, data) {
	        $.each(data.result.files, function (index, file) {
	        	$("#photo0").val(file.name);
	            if (file.url) {
	                var link = $('<a>')
	                    .attr('target', '_blank')
	                    .prop('href', file.url);
	                
	                $(data.context.children()[index]).wrap(link);
	                
	            }else if (file.error) {
	                var error = $('<span class="text-danger"/>').text(file.error);
	                $(data.context.children()[index])
	                    .append('<br>')
	                    .append(error);
	            }
	        });
	    }).on('fileuploadfail', function (e, data) {
	    	$.each(data.files, function (index) {
	            var error = $('<span class="text-danger"/>').text('File upload failed.');
	            $(data.context.children()[index])
	                .append('<br>')
	                .append(error);
	        });
	    }).prop('disabled', !$.support.fileInput)
	        .parent().addClass($.support.fileInput ? undefined : 'disabled');
	    
	    $('#fileupload2').fileupload({
	        url: url,
	        dataType: 'json',
	        autoUpload: false,
	        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
	        maxFileSize: 600000,
	        disableImageResize: /Android(?!.*Chrome)|Opera/
	            .test(window.navigator.userAgent),
	        previewMaxWidth: 200,
	        previewMaxHeight: 200,
	        previewCrop: true
	    }).on('fileuploadadd', function (e, data) {
	        data.context = $('<div/>').appendTo('#files2');
	        $.each(data.files, function (index, file) {
	            var node = $('<p/>')
	                    .append($('<span/>').text(file.name));
	            if (!index) {
	                node
	                    .append('<br>')
	                    .append(uploadButton.clone(true).data(data));
	            }
	            node.appendTo(data.context);
  	        });
	    }).on('fileuploadprocessalways', function (e, data) {
	        var index = data.index,
	            file = data.files[index],
	            node = $(data.context.children()[index]);
	        if (file.preview) {
	            node
	                .prepend('<br>')
	                .prepend(file.preview);
	        }
	        if (file.error) {
	            node
	                .append('<br>')
	                .append($('<span class="text-danger"/>').text(file.error));
	        }
	        if (index + 1 === data.files.length) {
	            data.context.find('button')
	                .text('上傳')
	                .prop('disabled', !!data.files.error);
	        }
	    }).on('fileuploadprogressall', function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .progress-bar').css(
	            'width',
	            progress + '%'
	        );
	    }).on('fileuploaddone', function (e, data) {
	        $.each(data.result.files, function (index, file) {
	        	$("#photo1").val(file.name);
	            if (file.url) {
	                var link = $('<a>')
	                    .attr('target', '_blank')
	                    .prop('href', file.url);
	                $(data.context.children()[index])
	                    .wrap(link);
	            } else if (file.error) {
	                var error = $('<span class="text-danger"/>').text(file.error);
	                $(data.context.children()[index])
	                    .append('<br>')
	                    .append(error);
	            }
	        });
	    }).on('fileuploadfail', function (e, data) {
	    	$.each(data.files, function (index) {
	            var error = $('<span class="text-danger"/>').text('File upload failed.');
	            $(data.context.children()[index])
	                .append('<br>')
	                .append(error);
	        });
	    }).prop('disabled', !$.support.fileInput)
	        .parent().addClass($.support.fileInput ? undefined : 'disabled');
        
	    $('#fileupload-update').fileupload({
	        url: url,
	        dataType: 'json',
	        autoUpload: false,
	        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
	        maxFileSize: 600000,
	        disableImageResize: /Android(?!.*Chrome)|Opera/
	            .test(window.navigator.userAgent),
	        previewMaxWidth: 200,
	        previewMaxHeight: 200,
	        previewCrop: true
	    }).on('fileuploadadd', function (e, data) {
	        data.context = $('<div/>').appendTo('#files-update');
	        $.each(data.files, function (index, file) {
	            var node = $('<p/>')
	                    .append($('<span/>').text(file.name));
	            if (!index) {
	                node
	                    .append('<br>')
	                    .append(uploadButton.clone(true).data(data));
	            }
	            node.appendTo(data.context);
	            $("#photo-update").val(file.name);
	        });
	    }).on('fileuploadprocessalways', function (e, data) {
	        var index = data.index,
	            file = data.files[index],
	            node = $(data.context.children()[index]);
	        if (file.preview) {
	            node
	                .prepend('<br>')
	                .prepend(file.preview);
	        }
	        if (file.error) {
	            node
	                .append('<br>')
	                .append($('<span class="text-danger"/>').text(file.error));
	        }
	        if (index + 1 === data.files.length) {
	            data.context.find('button')
	                .text('上傳')
	                .prop('disabled', !!data.files.error);
	        }
	    }).on('fileuploadprogressall', function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .progress-bar').css(
	            'width',
	            progress + '%'
	        );
	    }).on('fileuploaddone', function (e, data) {
	    	console.log("fileuploaddone");
	        $.each(data.result.files, function (index, file) {
	        	$("#photo0-update").val(file.name);
	            if (file.url) {
	                var link = $('<a>')
	                    .attr('target', '_blank')
	                    .prop('href', file.url);
	                $(data.context.children()[index])
	                    .wrap(link);
	            } else if (file.error) {
	                var error = $('<span class="text-danger"/>').text(file.error);
	                $(data.context.children()[index])
	                    .append('<br>')
	                    .append(error);
	            }
	        });
	    }).on('fileuploadfail', function (e, data) {
	    	$.each(data.files, function (index) {
	            var error = $('<span class="text-danger"/>').text('File upload failed.');
	            $(data.context.children()[index])
	                .append('<br>')
	                .append(error);
	        });
	    }).prop('disabled', !$.support.fileInput)
	        .parent().addClass($.support.fileInput ? undefined : 'disabled');       
	    
	    
	    $('#fileupload2-update').fileupload({
	        url: url,
	        dataType: 'json',
	        autoUpload: false,
	        acceptFileTypes: /(\.|\/)(gif|jpe?g|png)$/i,
	        maxFileSize: 600000,
	        disableImageResize: /Android(?!.*Chrome)|Opera/
	            .test(window.navigator.userAgent),
	        previewMaxWidth: 200,
	        previewMaxHeight: 200,
	        previewCrop: true
	    }).on('fileuploadadd', function (e, data) {
	        data.context = $('<div/>').appendTo('#files2-update');
	        $.each(data.files, function (index, file) {
	            var node = $('<p/>')
	                    .append($('<span/>').text(file.name));
	            if (!index) {
	                node
	                    .append('<br>')
	                    .append(uploadButton.clone(true).data(data));
	            }
	            node.appendTo(data.context);
	        });
	    }).on('fileuploadprocessalways', function (e, data) {
	        var index = data.index,
	            file = data.files[index],
	            node = $(data.context.children()[index]);
	        
	        if (file.preview) {
	            node
	                .prepend('<br>')
	                .prepend(file.preview);
	        }
	        if (file.error) {
	            node
	                .append('<br>')
	                .append($('<span class="text-danger"/>').text(file.error));
	        }
	        if (index + 1 === data.files.length) {
	            data.context.find('button')
	                .text('上傳')
	                .prop('disabled', !!data.files.error);
	        }
	    }).on('fileuploadprogressall', function (e, data) {
	        var progress = parseInt(data.loaded / data.total * 100, 10);
	        $('#progress .progress-bar').css(
	            'width',
	            progress + '%'
	        );
	    }).on('fileuploaddone', function (e, data) {
	    	console.log(data.result.files);
	    	
	        $.each(data.result.files, function (index, file) {
	        	
	        	$("#photo1-update").val(file.name); 
	            if (file.url) {
	                var link = $('<a>')
	                    .attr('target', '_blank')
	                    .prop('href', file.url);
	                $(data.context.children()[index])
	                    .wrap(link);
	            } else if (file.error) {
	                var error = $('<span class="text-danger"/>').text(file.error);
	                $(data.context.children()[index])
	                    .append('<br>')
	                    .append(error);
	            }
	        });
	    }).on('fileuploadfail', function (e, data) {
	    	$.each(data.files, function (index) {
	            var error = $('<span class="text-danger"/>').text('File upload failed.');
	            $(data.context.children()[index])
	                .append('<br>')
	                .append(error);
	        });
	    }).prop('disabled', !$.support.fileInput)
	        .parent().addClass($.support.fileInput ? undefined : 'disabled');
	    
	    //<!-- photo section jquery end by Melvin -->
	    $("#warning").dialog({
			title: "警告",
			draggable : true,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : "auto",
			width : "auto",
			modal : true,
			show : {effect : "fade",duration : 300},
			hide : {effect : "fade",duration : 300},
			buttons : {
				"確認" : function() {
					$(this).dialog("close");
					setTimeout(function(){
						$("#warning").val().focus();
					},500);
				}
			}
		});
	    
	    $("#warning").show();
	    
	    $("#insert_currency").change(function(e){
	    	$(".currency1").html("("+$("#insert_currency").find("option:selected").text()+")");
	    	
	    	$("#dialog-form-insert input[name='cost']").val(Math.round(
	    			$("#dialog-form-insert input[name='tmp_cost']").val()*$("#insert_currency").val()*10000) / 10000
	    	);
	    	
	    	$("#insert_exchange_cost").html(
	    			currency_unit($("#insert_currency").find("option:selected").text())+
	    			$("#dialog-form-insert input[name='tmp_cost']").val()+
	    			" x "+
	    			$("#insert_currency").val()+
	    			" = NT$"+$("#dialog-form-insert input[name='cost']").val()
	    	);
	    	
	    	$("#dialog-form-insert input[name='price']").val(
	    			Math.round(
	    					$("#dialog-form-insert input[name='tmp_price']").val()*$("#insert_currency").val()*10000
	    			)/10000
	    	);
	    	
	    	$("#insert_exchange_price").html(
	    			currency_unit($("#insert_currency").find("option:selected").text())+
	    			$("#dialog-form-insert input[name='tmp_price']").val()+
	    			" x "+
	    			$("#insert_currency").val()+
	    			" = NT$"+$("#dialog-form-insert input[name='price']").val()
	    	);
	    });
	    
	    $("#dialog-form-insert input[name='tmp_cost']").change(function(e){
	    	$("#dialog-form-insert input[name='cost']").val(
	    			Math.round(
	    					$("#dialog-form-insert input[name='tmp_cost']").val()*$("#insert_currency").val()*10000
	    			)/10000
	    	);
	    	$("#insert_exchange_cost").html(
	    			currency_unit($("#insert_currency").find("option:selected").text())+
	    			$("#dialog-form-insert input[name='tmp_cost']").val()+
	    			" x "+
	    			$("#insert_currency").val()+
	    			" = NT$"+$("#dialog-form-insert input[name='cost']").val()
	    	);
	    });
	    $("#dialog-form-insert input[name='tmp_price']").change(function(e){
	    	$("#dialog-form-insert input[name='price']").val(
	    			Math.round(
	    					$("#dialog-form-insert input[name='tmp_price']").val()*$("#insert_currency").val()*10000
	    			)/10000
	    	);
	    	$("#insert_exchange_price").html(
	    			currency_unit($("#insert_currency").find("option:selected").text())+
	    			$("#dialog-form-insert input[name='tmp_price']").val()+
	    			" x "+
	    			$("#insert_currency").val()+
	    			" = NT$"+$("#dialog-form-insert input[name='price']").val()
	    	);
	    });
	    
	    $("#update_currency").change(function(e){
	    	$(".currency2").html("("+$("#update_currency").find("option:selected").text()+")");
	    	$("#dialog-form-update input[name='cost']").val(
	    			Math.round(
	    					$("#dialog-form-update input[name='tmp_cost']").val()*$("#update_currency").val()*10000
	    			)/10000
	    	);
	    	$("#update_exchange_cost").html(
	    			currency_unit($("#update_currency").find("option:selected").text())+
	    			$("#dialog-form-update input[name='tmp_cost']").val()+
	    			" x "+
	    			$("#update_currency").val()+
	    			" = NT$"+$("#dialog-form-update input[name='cost']").val()
	    	);
	    	$("#dialog-form-update input[name='price']").val(
	    			Math.round(
	    					$("#dialog-form-update input[name='tmp_price']").val()*$("#update_currency").val()*10000
	    					)/10000
	    			);
	    	$("#update_exchange_price").html(
	    			currency_unit($("#update_currency").find("option:selected").text())+
	    			$("#dialog-form-update input[name='tmp_price']").val()+
	    			" x "+
	    			$("#update_currency").val()+
	    			" = NT$"+$("#dialog-form-update input[name='price']").val()
	    	);
	    });
	    $("#dialog-form-update input[name='tmp_cost']").change(function(e){
	    	$("#dialog-form-update input[name='cost']").val(
	    			Math.round(
	    					$("#dialog-form-update input[name='tmp_cost']").val()*$("#update_currency").val()*10000
	    			)/10000
	    	);
	    	$("#update_exchange_cost").html(
	    			currency_unit($("#update_currency").find("option:selected").text())+
	    			$("#dialog-form-update input[name='tmp_cost']").val()+
	    			" x "+
	    			$("#update_currency").val()+
	    			" = NT$"+$("#dialog-form-update input[name='cost']").val()
	    	);
	    });
	    $("#dialog-form-update input[name='tmp_price']").change(function(e){
	    	$("#dialog-form-update input[name='price']").val(
	    			Math.round(
	    					$("#dialog-form-update input[name='tmp_price']").val()*$("#update_currency").val()*10000
	    			)/10000
	    	);
	    	$("#update_exchange_price").html(
	    			currency_unit($("#update_currency").find("option:selected").text())+
	    			$("#dialog-form-update input[name='tmp_price']").val()+
	    			" x "+$("#update_currency").val()+
	    			" = NT$"+$("#dialog-form-update input[name='price']").val()
	    	);
	    });
	    
	    $.ajax({
			type : "POST",
			url : "exchange.do",
			data : {
				action : "search"
			},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				var result_select="<option value='1'>台幣</option>";
				
				$.each(json_obj,function(i, item) {
					result_select += "<option value='"+json_obj[i].exchange_rate+"'>"+json_obj[i].currency+"</option>";
				});
				$("#update_currency").html(result_select);
				$("#insert_currency").html(result_select);
			}
		});
		 
	    $.ajax({
			type : "POST",
			url : "exchange.do",
			data :{action : "search"},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				$.each (json_obj, function (i,item) {
					if(json_obj[i].c_product_id!=null){
						c_product_id_tags[i]=json_obj[i].c_product_id;
					}
					if(json_obj[i].product_name!=null){
						product_name_tags[i]=json_obj[i].product_name;
					}
				});
			}
		});

	    auto_complete("package_product_name",product_name_tags);
		
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
	
</head>
<body>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
			<!--對話窗樣式-確認 -->
			<div id="dialog-confirm" title="是否刪除此商品?" style="display:none;">
				<p>是否確認刪除該筆資料</p>
			</div>
			
			<!--對話窗樣式-修改 -->
			<div id="dialog-form-update" title="修改商品資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post" style="display:inline">
					<fieldset>
						<table class="form-table">
							<tbody>
								<tr>
									<td>自訂商品ID：</td>
									<td><input type="text" id="c_p_id2" name="c_product_id"/></td>
									<td>商品類別：</td><td><select id="select_update_type_id" name="select_update_type_id"></select></td>
								</tr><tr>
									<td>供應商名稱：</td><td><input type="text" id="update_supply_name" name="supply_name"/></td>
									<td>商品單位：</td><td><select id="select_update_unit_id" name="select_update_unit_id"></select></td>
								</tr><tr>
									<td>商品名稱：</td><td><input type="text" name="product_name"  ></td>
									<td>條碼：</td>
									<td><input type="text" id="edit_barcode" name="barcode"/></td>
									<td>
										<input id="same2" type="checkbox" style="position:static;"
											onclick="if($('#same2').prop('checked')){$('#edit_barcode').val($('#c_p_id2').val());}else{$('#edit_barcode').val('');}">同自定ID
									</td>
								</tr><tr>
									<td>商品說明：</td><td><input type="text" name="description"/></td>
									<td>幣別：</td><td><select id='update_currency'></select></td>
								</tr><tr>
									<td>成本：<a class='currency2'></a></td><td><input type="text" name="tmp_cost" /></td>
									<td>售價：<a class='currency2'></a></td><td><input type="text" name="tmp_price" /></td>
								</tr><tr>
									<td>折合台幣成本：</td>
									<td><a id='update_exchange_cost'>NT＄0 x 1 = NT$0</a><input type="hidden" name="cost" /></td>
									<td>折合台幣售價：</td>
									<td><a id='update_exchange_price'>NT＄0 x 1 = NT$0</a><input type="hidden" name="price" /></td>
								</tr><tr>
									<td>安全庫存：</td>
									<td><input type="text" name="keep_stock" /></td>
								</tr>
							</tbody>
						</table>
					</fieldset>
				</form>
				<!-- photo section begin by Melvin -->
				<table class="form-table">
					<tbody>
						<tr>
							<td>商品圖片：</td>
							<td>
								<span class="btn btn-success fileinput-button btn-primary" style="padding: 6px 12px;border-radius: 5px;">
									<span><font color="white">+&nbsp;</font>瀏覽<font color="red">(最大500K)</font></span>
									<input id="fileupload-update" type="file" name="files-update[]">								
									<br>
								</span>
								<div id="files-update" class="files" ></div>
							</td>
						</tr>	
						<tr>
							<td>商品圖片2：</td>
							<td>	
								<span class="btn btn-success fileinput-button btn-primary" style="padding: 6px 12px;border-radius: 5px;">
								    <span><font color="white">+&nbsp;</font>瀏覽<font color="red">(最大500K)</font></span>
								    <input id="fileupload2-update" type="file" name="files2-update[]">
									<br>
							    </span>
							    <div id="files2-update" class="files"></div>        
							</td>
						</tr>
					</tbody>
               	</table>		
               	<!-- photo section end by Melvin -->
               	<img id="product-photo" src="">
               	<img id="product-photo1" src="">
			</div>
			
			<!--對話窗樣式-新增 -->
			<div id="dialog-form-insert" title="新增商品資料" style="display:none;">
				<form name="insert-dialog-form-post" id="insert-dialog-form-post" style="display:inline">
					<fieldset>
						<table class="form-table">
							<tbody>
								<tr>
									<td>自訂商品ID：</td><td><input type="text" id="c_p_id"name="c_product_id"/></td>
									<td>商品類別：</td><td><select id="select_insert_type_id" name="select_insert_type_id"></select></td>
								</tr><tr>
									<td>供應商名稱：</td><td><input type="text" id="insert_supply_name" name="supply_name"/></td>
									<td>商品單位：</td><td><select id="select_insert_unit_id" name="select_insert_unit_id"></select></td>
								</tr><tr>
									<td>商品名稱：</td><td><input type="text" name="product_name"  ></td>
									<td>條碼：</td>
									<td><input type="text" id="new_barcode" name="barcode"/></td>
									<td>
										<input id="same" type="checkbox" style="position:static;" 
											onclick="if($('#same').prop('checked')){$('#new_barcode').val($('#c_p_id').val());}else{$('#new_barcode').val('');}">同自定ID
									</td>
								</tr><tr>
									<td>商品說明：</td><td><input type="text" name="description"/></td>
									<td>幣別：</td><td><select id='insert_currency'></select></td>
								</tr><tr>
									<td>成本：<a class='currency1'></a></td><td><input type="text" name="tmp_cost" /></td>
									<td>售價：<a class='currency1'></a></td><td><input type="text" name="tmp_price" /></td>
								</tr><tr>
									<td>折合台幣成本：</td><td><a id='insert_exchange_cost'>NT＄0 x 1 = NT$0</a><input type="hidden" name="cost" /></td>
									<td>折合台幣售價：</td><td><a id='insert_exchange_price'>NT＄0 x 1 = NT$0</a><input type="hidden" name="price" /></td>
								</tr><tr>
									<td>庫存量：</td><td><input type="text" name="current_stock" /></td>
									<td>安全庫存：</td><td><input type="text" name="keep_stock" /></td>
								</tr>
							</tbody>
						</table>		
					</fieldset>
				</form>
				<!-- photo section begin by Melvin -->
				<table class='form-table'>
					<tbody>
						<tr>
							<td>商品圖片：</td>
							<td>
								<span class="btn btn-success fileinput-button btn-primary" style="padding: 6px 12px;border-radius: 5px;">
								<span><font color="white">+&nbsp;</font>瀏覽<font color="red">(最大500K)</font></span>
								<input id="fileupload" type="file" name="files[]">
								<br>
								</span>
								<div id="files" class="files" ></div>
	               			</td>
	               		</tr>	
	               		<tr>
	              			 <td>商品圖片2：</td>
	              			 <td>	
	                             <span class="btn btn-success fileinput-button btn-primary" style="padding: 6px 12px;border-radius: 5px;">
						         <span><font color="white">+&nbsp;</font>瀏覽<font color="red">(最大500K)</font></span>
						         <input id="fileupload2" type="file" name="files2[]">
								 <br>
						         </span>
						         <div id="files2" class="files"></div>        
	               			</td>
						</tr>	
					</tbody>
				</table>
			</div>
			
			<div class="input-field-wrap">
				<div class="form-wrap">
					<div class="form-row">
						<label for="">
							<span class="block-label">供應商名稱查詢</span>
							<input type="text" id="searh_product_name" name="searh_product_name"></input>
						</label>
						<button class="btn btn-darkblue" id="searh-sale">查詢</button>
					</div>
					<div class="form-row">
						<label for="">
							<span class="block-label">商品名稱查詢</span>
							<input type="text" id="searh_name" name="searh_name"></input>
						</label>
						<button class="btn btn-darkblue" id="searh-name">查詢</button>
					</div>
					
					<font color='#6A5ACD' >掃條碼亦可取得商品資料</font>
					
					<div class="btn-row">
						<button class="btn btn-exec btn-wide" id="create-sale">新增商品資料</button>
					</div>
				</div>
			</div>
			
			<div class="row search-result-wrap" align="center">
				<div id="sales-contain" class="ui-widget" style="display:none;">
					<table id="sales" class="result-table" >
						<thead>
							<tr>
								<th>選項</th>
								<th>自訂商品ID</th>
								<th>供應商名稱</th>
								<th style="min-width:100px;">商品名稱</th>
								<th style="background-image: none !important;">商品說明</th>
								<th style="min-width:40px;">類別</th>
								<th style="min-width:40px;">單位</th>
								<th style="min-width:70px;">成本</th>
								<th style="min-width:70px;">售價</th>
								<th>安全庫存</th>
								<th style="max-width:100px;background-image: none !important;">圖片1</th>
								<th style="max-width:100px;background-image: none !important;">圖片2</th>
								<th>條碼</th>
								<th style="background-image: none !important;">功能</th>
							</tr>
						</thead>
						<tbody style="line-height:16px;">
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>
		</div>
	</div>
	
	<input type="text" id="photo0" style="display:none"/>
	<input type="text" id="photo1" style="display:none"/>
	<input type="text" id="photo0-update" style="display:none"/>
	<input type="text" id="photo1-update" style="display:none"/>
	<input type="text" id="bar_code_focus" style="display:none"/>
	
	<div id="warning" 
		style="	display:none;
				font-size:30px;
				color:red;font-family: '微軟正黑體',
				'Microsoft JhengHei', 'LiHei Pro',
				 Arial, Helvetica, sans-serif, \5FAE\8EDF\6B63\9ED1\9AD4,\65B0\7D30\660E\9AD4;">
	</div>

	<div id="message" align="center">
		<div id="text"></div>
	</div>
</body>
</html>