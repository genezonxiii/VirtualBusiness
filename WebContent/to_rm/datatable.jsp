<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
  <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="description" content="">
    <meta name="author" content="">
    <link rel="shortcut icon" href="css/beagle/logo-fav.png">
    <title>Beagle</title>
    
    <link rel="stylesheet" type="text/css" href="css/beagle/perfect-scrollbar.min.css"/>

    <link rel="stylesheet" type="text/css" href="css/beagle/materialdesignicons.min.css"/>
    <link rel="stylesheet" type="text/css" href="css/beagle/material-design-iconic-font.min.css"/>
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
    <link rel="stylesheet" type="text/css" href="css/beagle/dataTables.bootstrap.min.css"/>
    <link rel="stylesheet" href="css/beagle/beagle.css" type="text/css"/>
    <link rel="stylesheet" href="css/1.12.0/jquery-ui.css">
  </head>
  <body>
    <div class="be-wrapper">
      <nav class="navbar navbar-default navbar-fixed-top be-top-header">
        <div class="container-fluid">
          <div class="navbar-header"><a href="index.html" class="navbar-brand">智慧電商平台</a></div>
          <div class="be-right-navbar">
            <ul class="nav navbar-nav navbar-right be-user-nav">
              <li class="dropdown"><a href="#" data-toggle="dropdown" role="button" aria-expanded="false" class="dropdown-toggle"><img src="assets/img/avatar.png"><span class="user-name">Arch</span></a>
                <ul role="menu" class="dropdown-menu">
                  <li>
                    <div class="user-info">
                      <div class="user-name">Arch</div>
                      <div class="user-position online">Available</div>
                    </div>
                  </li>
                  <li><a href="#"><span class="icon mdi mdi-power"></span> Logout</a></li>
                </ul>
              </li>
            </ul>
            <div class="page-title"><span>庫存管理</span></div>
          </div>
        </div>
      </nav>
      <div class="be-left-sidebar">
        <div class="left-sidebar-wrapper"><a href="#" class="left-sidebar-toggle">2222</a>
          <div class="left-sidebar-spacer">
            <div class="left-sidebar-scroll">
              <div class="left-sidebar-content">
                <ul class="sidebar-elements">
                  <li class="divider">Menu</li>
                  <li><a href="welcome.jsp"><i class="icon mdi mdi-home"></i><span>首頁</span></a>
                  </li>
                  <li class="parent"><a href="#"><i class="icon mdi mdi-repeat"></i><span>交易處理</span></a>
                    <ul class="sub-menu">
                      <li><a href="ui-alerts.html">訂單拋轉</a>
                      </li>
                    </ul>
                  </li>
                  <li class="parent"><a href="#"><i class="icon mdi mdi-autorenew"></i><span>後臺支援系統</span></a>
                    <ul class="sub-menu">
                      <li><a href="purchase.jsp">進貨管理</a>
                      </li>
                      <li><a href="purchreturn.jsp">進貨退回管理</a>
                      </li>
                      <li><a href="sale.jsp">銷貨管理</a>
                      </li>
                      <li><a href="salereturn.jsp">銷貨退回管理</a>
                      </li>
                      <li class="active"><a href="stock.jsp">庫存管理</a>
                      </li>
                      <li><a href="supply.jsp">供應商管理</a>
                      </li>
                      <li><a href="product.jsp">商品管理</a>
                      </li>
                      <li><a href="tagprint.jsp">標籤列印</a>
                      </li>
                      <li><a href="customer.jsp">客戶管理</a>
                      </li>
                      <li><a href="producttype.jsp">商品類型管理</a>
                      </li>
                      <li><a href="productunit.jsp">商品單位管理</a>
                      </li>
                      <li><a href="group.jsp">公司管理</a>
                      </li>
                      <li><a href="user.jsp">使用者管理</a>
                      </li>
                      <li><a href="accreceive.jsp">應收帳款管理</a>
                      </li>
                      <li><a href="accpay.jsp">應付帳款管理</a>
                      </li>
                      <li><a href="changepassword.jsp">使用者密碼管理</a>
                      </li>
<!--                       <li><a href="membercondition.jsp">Ã¦ÂÂÃ¥ÂÂ¡Ã¥ÂÂÃ§Â´ÂÃ¨Â¨Â­Ã¥Â®Â</a> -->
<!--                       </li> -->
                    </ul>
                  </li>
                  <li class="parent"><a href="#"><i class="icon mdi mdi-calculator"></i><span>報表管理</span></a>
                    <ul class="sub-menu">
                      <li><a href="salereport.jsp">訂單報表</a>
                      </li>
                      <li><a href="shipreport.jsp">Ã¥ÂÂºÃ¨Â²Â¨Ã¥Â Â±Ã¨Â¡Â¨</a>
                      </li>
<!--                       <li><a href="distributereport.jsp">Ã©ÂÂÃ©ÂÂÃ¥Â Â±Ã¨Â¡Â¨</a> -->
<!--                       </li> -->
                      <li><a href="salereturnreport.jsp">Ã©ÂÂÃ¨Â²Â¨Ã¥Â Â±Ã¨Â¡Â¨</a>
                      </li>
                      <li><a href="purchreport.jsp">Ã©ÂÂ²Ã¨Â²Â¨Ã¥Â Â±Ã¨Â¡Â¨</a>
                      </li>
                      <li><a href="purchreturnreport.jsp">Ã©ÂÂ²Ã¨Â²Â¨Ã©ÂÂÃ¥ÂÂÃ¥Â Â±Ã¨Â¡Â¨</a>
                      </li>
                      <li><a href="stockreport.jsp">Ã¥ÂºÂ«Ã¥Â­ÂÃ¥Â Â±Ã¨Â¡Â¨</a>
                      </li>
                      <li><a href="supplyreport.jsp">Ã¤Â¾ÂÃ¦ÂÂÃ¥ÂÂÃ¥Â Â±Ã¨Â¡Â¨</a>
                      </li>
                      <li><a href="productreport.jap">Ã¥ÂÂÃ¥ÂÂÃ¥Â Â±Ã¨Â¡Â¨</a>
                      </li>
                      <li><a href="customerreport.jsp">Ã¥Â®Â¢Ã¦ÂÂ¶Ã¥Â Â±Ã¨Â¡Â¨</a>
                      </li>
                      <li><a href="accreceivereport.jsp">Ã¦ÂÂÃ¦ÂÂ¶Ã¥Â¸Â³Ã¦Â¬Â¾Ã¥Â Â±Ã¨Â¡Â¨</a>
                      </li>
                      <li><a href="accpayreport.jsp">Ã¦ÂÂÃ¤Â»ÂÃ¥Â¸Â³Ã¦Â¬Â¾Ã¥Â Â±Ã¨Â¡Â¨</a>
                      </li>
                    </ul>
                  </li>
                  <li class="parent"><a href="#"><i class="icon mdi mdi-chart-bar"></i><span>分析圖表</span></a>
                    <ul class="sub-menu">
                      <li><a href="salechart.jsp">出貨量統計圖</a>
                      </li>
                      <li><a href="saleamountchart.jsp">Ã©ÂÂ·Ã¥ÂÂ®Ã©ÂÂÃ©Â¡ÂÃ§ÂµÂ±Ã¨Â¨ÂÃ¥ÂÂ</a>
                      </li>
                      <li><a href="saleamountstaticchart.jsp">Ã©ÂÂ·Ã¥ÂÂ®Ã©ÂÂÃ©Â¡ÂÃ¦Â¯ÂÃ¤Â¾ÂÃ§ÂµÂ±Ã¨Â¨ÂÃ¥ÂÂ</a>
                      </li>
                    </ul>
                  </li>
                  <li class="parent"><a href="#"><i class="icon mdi mdi-layers"></i><span>線上學院</span></a>
                    <ul class="sub-menu">
                      <li><a href="#">Coming Soon</a>
                      </li>
                    </ul>
                  </li>
                </ul>
              </div>
            </div>
          </div>
          <div class="progress-widget">
            <div class="progress-data"><span class="progress-value">20%</span><span class="name">Current storage.</span></div>
            <div class="progress">
              <div style="width: 40%;" class="progress-bar progress-bar-primary"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="be-content">
        <div class="page-head">
          <h2 class="page-head-title">Data Tables</h2>
          <ol class="breadcrumb page-head-nav">
            <li><a href="#">首頁</a></li>
            <li><a href="#">庫存管理</a></li>
          </ol>
        </div>
        <div class="main-content">
          <div class="row">
            <div class="col-sm-12">
              <div class="panel panel-default panel-table">
                <div class="panel-heading">庫存管理<div class="tools"><span class="icon mdi mdi-download"></span><span class="icon mdi mdi-more-vert"></span></div>
                </div>
            	<div class="form-group">
                  <label class="col-sm-3 control-label" style="text-align:right;font-size:24px;">商品名稱查詢</label>
                  <div class="col-sm-6">
                    <div class="input-group xs-mb-15">
                      <input class="form-control" type="text" id="searh_stock_name"><span class="input-group-btn">
                        <button type="button" id="searh_stock" class="btn btn-primary">Go!</button></span>
                    </div>
                  </div>
                </div>
                <div class="panel-body" id="products-contain">
                  <table id="products" class="table table-striped table-hover table-fw-widget">
						<thead>
							<tr>
								<th>商品名稱 </th>
								<th style="max-width:150px;">庫存數量</th>
								<th style="max-width:150px;">安全庫存</th>
								<th>功能</th>
							</tr>
						</thead>
						<tbody id="tbdy" style="text-align:center">
						</tbody>
				  </table>
                </div>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col-sm-12">
              <div class="panel panel-default panel-table">
                <div class="panel-heading">Custom Layout
                  <div class="tools"><span class="icon mdi mdi-download"></span><span class="icon mdi mdi-more-vert"></span></div><span class="panel-subtitle">This is a custom datable layout</span>
                </div>
                <div class="panel-body">
                  <table id="table2" class="table table-striped table-hover table-fw-widget">
                    <thead>
                      <tr>
                        <th>Rendering engine</th>
                        <th>Browser</th>
                        <th>Platform(s)</th>
                        <th>Engine version</th>
                        <th>CSS grade</th>
                      </tr>
                    </thead>
                    <tbody>
                      <tr class="odd gradeX">
                        <td>Trident</td>
                        <td>
                          Internet
                          Explorer 4.0
                        </td>
                        <td>Win 95+</td>
                        <td class="center"> 4</td>
                        <td class="center"></td>
                      </tr>
                      <tr class="even gradeC">
                        <td>Trident</td>
                        <td>
                          Internet
                          Explorer 5.0
                        </td>
                        <td>Win 95+</td>
                        <td class="center">5</td>
                        <td class="center">C</td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    		<div id="dialog-form-update" title="修改庫存資料" style="display:none;">
				<form name="update-dialog-form-post" id="update-dialog-form-post">
					<fieldset>
							<table border="0" height="120">
							<tbody>
							<tr><td><h6>庫存數量:</h6></td><td><input type="text" id="quant" name="quantity"placeholder="修改庫存數量"/></td>
							<td>
								&nbsp;<a class='btn-gray' onclick="$('#quant').val(parseInt($('#quant').val())+1);">&nbsp;+&nbsp;</a>&nbsp;<a class='btn-gray' onclick="$('#quant').val(parseInt($('#quant').val())-1);">&nbsp;-&nbsp;</a>
							</td></tr>
							<tr><td><h6>備註說明:</h6></td><td><input type="text" name="memo"placeholder="修改備註說明"/></td></tr>
							<tr><td><input type="hidden" name="stock_id" disabled="disabled"> 
							<input type="hidden" name="product_id" disabled="disabled"></td> </tr>
							</tbody>
							</table>	
					</fieldset>
				</form>
			</div>			
			<!-- 第二列 -->
			<div class="row search-result-wrap" align="center" style="margin:0px auto;">
				<div id="products-contain" class=" result-table-wrap" style="display:none;">
					<table id="products" class="ui-widget ui-widget-content result-table">
						<thead>
							<tr class="ui-widget-header">
								<th>商品名稱 </th>
								<th style="max-width:150px;">庫存數量</th>
								<th style="max-width:150px;">安全庫存</th>
								<th>功能</th>
							</tr>
						</thead>
						<tbody id="tbdy" style="text-align:center">
						</tbody>
					</table>
				</div>
				<span class="validateTips"> </span>
			</div>
    <div id="dia"></div>
    <script src="js/beagle/jquery.min.js" type="text/javascript"></script>
    <script src="js/beagle/perfect-scrollbar.jquery.min.js" type="text/javascript"></script>
    <script src="js/beagle/main.js" type="text/javascript"></script>
    <script src="js/beagle/bootstrap.min.js" type="text/javascript"></script>
    <script src="js/beagle/jquery.dataTables.min.js" type="text/javascript"></script>
    <script src="js/beagle/dataTables.bootstrap.min.js" type="text/javascript"></script>
    <script src="js/beagle/dataTables.buttons.js" type="text/javascript"></script>
    <script src="js/beagle/buttons.html5.js" type="text/javascript"></script>
    <script src="js/beagle/buttons.flash.js" type="text/javascript"></script>
    <script src="js/beagle/buttons.print.js" type="text/javascript"></script>
    <script src="js/beagle/buttons.colVis.js" type="text/javascript"></script>
    <script src="js/beagle/buttons.bootstrap.js" type="text/javascript"></script>

    <script type="text/javascript">
      $(document).ready(function(){
      	//initialize the javascript
      	App.init();
      	App.dataTables();
      });
      </script>
      
<!--   <script src="https://code.jquery.com/jquery-1.12.4.js"></script> -->
  <script src="https://code.jquery.com/ui/1.12.0/jquery-ui.js"></script>
  <script type="text/javascript" src="js/jquery.validate.min.js"></script>
      <script>
      var bar_search=0;
      $(function(){
//     	  $("#dia").dialog({
//     			title: "你妳你妳你",
//     			draggable : false,//防止拖曳
//     			resizable : false,//防止縮放
//     			autoOpen : false,
//     			height : "auto",
//     			modal : true,
//     			show : {effect : "blind",duration : 1000},
//     			hide : {effect : "blind",duration : 1000},
//     			buttons : {
//     				"確認刪除" : function() {alert("嘿嘿嘿~");$(this).dialog("close");},
//     				"取消刪除" : function() {$(this).dialog("close");}
//     			}
//     	  });
//     	 $("#go2").click(function(){
//     		 alert("hello");
//     		 $("#dia").dialog("open");
//     		 $("#my").dataTable().fnDestroy();
//     		 $("#my tbody").html("<tr><td>1</td><td>456</td></tr><tr><td>3</td><td>4566</td></tr>");
//     		 $("#my").dataTable({});
//     	 });
    	 //$("#table1").dataTable({ "language": {"url": "js/dataTables_zh-tw.txt"}});
 		$(".bdyplane").animate({"opacity":"1"});
 		var validator_update = $("#update-dialog-form-post").validate({
 			rules : {
 				memo : {
 					required : true,
 					maxlength : 200
 				}
 			},
 			messages : {
 				group_name : {
 					maxlength : "長度不能超過200個字"
 				}
 			}
 		});
 		var group_name = $("#group_name");
 		$("#searh_stock").click(function(){
 			$.ajax({
 				type : "POST",
 				url : "stock.do",
 				data : {
 					action : "searh",
 					product_name : $("#searh_stock_name" ).val(),
 				},
 				success : function(result) {
 					//alert(result);
 					//console.log(result);
 					var json_obj = $.parseJSON(result);
 					var result_table = "";
 					$.each(json_obj,function(i, item) {
 						result_table += 
 								  "<tr><td>"+json_obj[i].product_name
 								+ "</td><td>" + json_obj[i].quantity
 								+ "</td><td>"+json_obj[i].keep_stock+"</td>"
 								+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
 								+ "	<div class='table-function-list'>"
 								+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].product_id+"'name='"+ json_obj[i].stock_id+"' ><i class='fa fa-pencil'></i></button>"
 								+ "	</div></div></td></tr>";	
 					});
 					
 					//判斷查詢結果
 					var resultRunTime = 0;
 					$.each (json_obj, function (i) {
 						resultRunTime+=1;
 					});
 					$("#products").dataTable().fnDestroy();
 					if(resultRunTime!=0){
 						$("#products-contain").show();
 						$("#products tbody").html(result_table);
 						$("#products").dataTable({ "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
 						//$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
 						$(".validateTips").text("");
 					}else{
 						$("#products-contain").hide();
 						$(".validateTips").text("查無此結果");
 					}
 				}
 			});
 		});
 		//修改Dialog相關設定
 		update_dialog = $("#dialog-form-update").dialog({
 			draggable : false,//防止拖曳
 			resizable : false,//防止縮放
 			autoOpen : false,
 			height : 300,
 			width : 350,
 			modal : true,
 			show : {effect : "blind",duration : 300},
 			hide : {effect : "fade",duration : 300},
 			buttons : [{
 				text : "修改",
 				click : function() {
  					if ($('#update-dialog-form-post').valid()) {
 						$.ajax({
 							type : "POST",
 							url : "stock.do",
 							data : {
 	 							action : "update",
 	 							unit_id : uuid,
 	 							product_id : $("#dialog-form-update input[name='product_id']").val(),
 	 							stock_id : $("#dialog-form-update input[name='stock_id']").val(),
 	 							quantity : $("#dialog-form-update input[name='quantity']").val(),
 	 							memo : $("#dialog-form-update input[name='memo']").val()
 							},				
 							success : function(result) {
 								var json_obj = $.parseJSON(result);
 								var result_table = "";
 								$.each(json_obj,function(i, item) {
 									if(bar_search==null||uuid==json_obj[i].product_id)
 									result_table += 
 										"<tr><td>"+json_obj[i].product_name
 											+"</td><td>"+json_obj[i].quantity
 											+"</td><td>"+json_obj[i].keep_stock+"</td>"
 											+ "<td><div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"
 											+ "	<div class='table-function-list'>"
 											+ "		<button class='btn-in-table btn-darkblue btn_update' title='修改' value='"+ json_obj[i].product_id+"'name='"+ json_obj[i].stock_id+"' ><i class='fa fa-pencil'></i></button>"
 											+ "	</div></div></td></tr>";	
 								});
 								//判斷查詢結果
 								var resultRunTime = 0;
 								$.each (json_obj, function (i) {
 									resultRunTime+=1;
 								});
 								$("#products").dataTable().fnDestroy();
 								if(resultRunTime!=0){
 									$("#products-contain").show();
 									$("#products tbody").html(result_table);
 									$("#products").dataTable({"language": {"url": "js/dataTables_zh-tw.txt"}});
//  									$("#products").dataTable({"bFilter": false, "bInfo": false, "paging": false, "language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "沒有符合的結果"}});
 									$(".validateTips").text("");
 								}else{
 									$("#products-contain").hide();
 								}
 							}
 						});
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
 		//修改事件聆聽		
 		$("#products").delegate(".btn_update", "click", function(e) {
 			e.preventDefault();
 			if($(this).attr("id")!=null){
 				bar_search=$(this).attr("id");
 			}else{
 				bar_search=null;
 			}
 			uuid = $(this).val();
 			$("input[name='search_product_id'").val("");
 			$.ajax({
 				type : "POST",
 				url : "stock.do",
 				data : {
 					action : "searh",
 					product_id : $("input[name='search_product_id'").val()
 				
 				},
 				success : function(result) {
 						var json_obj = $.parseJSON(result);
 						var len=json_obj.length;
 						
 						//判斷查詢結果
 						var resultRunTime = 0;
 						$.each (json_obj, function (i) {
 							resultRunTime+=1;
 						});
 							var result_table = "";
 							$.each(json_obj,function(i, item) {
 								if(json_obj[i].product_id==uuid){
 										$("#dialog-form-update input[name='product_id']").val(json_obj[i].product_id);
 										$("#dialog-form-update input[name='stock_id']").val(json_obj[i].stock_id);
 										$("#dialog-form-update input[name='quantity']").val(json_obj[i].quantity);
 										$("#dialog-form-update input[name='memo']").val(json_obj[i].memo);
 								}
 							});
 						} 

 				});			
 			update_dialog.dialog("open");
 		});		
      });
      
      
    </script>
    
  </body>
</html>