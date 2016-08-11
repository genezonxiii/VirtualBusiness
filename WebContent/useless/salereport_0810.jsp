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
<title>訂單報表</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />

<link href="<c:url value="css/css.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/jquery.dataTables.min.css" />
<link rel="stylesheet" href="css/buttons.dataTables.min.css" />
<link href="<c:url value="css/jquery.dataTables.min.css" />" rel="stylesheet">
<link href="<c:url value="css/1.11.4/jquery-ui.css" />" rel="stylesheet">
<link rel="stylesheet" href="css/styles.css" />
</head>
<body>
<%-- 	<jsp:include page="template.jsp" flush="true"/> --%>
	<div class="content-wrap" style="margin:56px 0px 28px 120px;">


<script type="text/javascript" src="js/jquery-1.12.4.js"></script>
<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/dataTables.buttons.min.js"></script>
<script type="text/javascript" src="js/buttons.flash.min.js"></script>
<script type="text/javascript" src="js/jszip.min.js"></script>
<script type="text/javascript" src="js/pdfmake.min.js"></script>
<script type="text/javascript" src="js/vfs_fonts.js"></script>
<script type="text/javascript" src="js/buttons.html5.min.js"></script>
<script type="text/javascript" src="js/buttons.print.min.js"></script>


<!-- <script type="text/javascript" src="js/jquery-1.10.2.js"></script> -->
<!-- <script type="text/javascript" src="js/jquery-1.11.4.js"></script> -->
<!-- <script type="text/javascript" src="js/jquery.dataTables.min.js"></script> -->
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-migrate-1.4.1.min.js"></script>
<script type="text/javascript" src="js/jquery.validate.min.js"></script>
<script type="text/javascript" src="js/additional-methods.min.js"></script>
<script type="text/javascript" src="js/messages_zh_TW.min.js"></script>
<script type="text/javascript" src="js/jquery.table2excel.js"></script>



<script>
var my_name='訂單報表';
function date_format(str) {
	if(str==null){
		return "";
	}
	var words=str.replace(","," ").split(" ");
	return words[3]+"-"+words[0].replace("一月","1").replace("二月","2").replace("三月","3").replace("四月","4").replace("五月","5").replace("六月","6").replace("七月","7").replace("八月","8").replace("九月","9").replace("十月","10").replace("十一月","11").replace("十二月","12").replace("Jan","1").replace("Feb","2").replace("Mar","3").replace("Apr","4").replace("May","5").replace("Jun","6").replace("Jul","7").replace("Aug","8").replace("Sep","9").replace("Oct","10").replace("Nov","11").replace("Dec","12")+"-"+words[1];
}
	$(function() {
// 		 $('#example').DataTable( {
// 		        dom: 'lfrB<t>ip',
// 		        buttons: [{
// 		            extend: 'csv',
// 		            text: '輸出為CSV檔',
// 		            title: '進貨報表',
// 		            exportOptions: {
// 		                modifier: {       
// 		                }
// 		            }
// 		        },{
// 		            extend: 'excel',
// 		            text: '輸出為xlsx檔',
// 		            title: '進貨報表',
// 		            exportOptions: {
// 		                modifier: {
// 		                	search: 'none'
// 		                }
// 		            }
// 		        },{
// 		            extend: 'pdf',
// 		            text: '輸出為PDF檔',
// 		            exportOptions: {
// 		                modifier: {
// 		                	search: 'none'
// 		                }
// 		            }
// 		        }
// 		    ]
// 		    } );
		//$( "#datepicker1" ).datepicker({dateFormat: 'yy/mm/dd'});
		//$( "#datepicker2" ).datepicker({dateFormat: 'yy/mm/dd'});
		//查詢相關設定
		$("#searh-productunit").click(function(e) {
			e.preventDefault();
			$.ajax({
				type : "POST",
				url : "salereport.do",
				data : {action :"searh",time1 : ($('#datepicker1').val().replace("-","/").replace("-","/")),time2 : ($('#datepicker2').val().replace("-","/").replace("-","/"))},
				success : function(result) {
					var json_obj = $.parseJSON(result);
					var result_table = "";
					$.each(json_obj,function(i, item) {
						result_table += "<tr><td>"+ json_obj[i].seq_no + "</td><td >"+ json_obj[i].order_no + "</td><td>" + json_obj[i].product_name + "</td><td>"+ json_obj[i].c_product_id + "</td><td>"+ json_obj[i].quantity + "</td><td>"+ json_obj[i].price + "</td><td>" + date_format(json_obj[i].trans_list_date) + "</td><td>"+ date_format(json_obj[i].dis_date) + "</td><td>"+ date_format(json_obj[i].sale_date) + "</td><td>"+ json_obj[i].order_source + "</td><td>"+ (json_obj[i].memo==null?"":json_obj[i].memo.replace("NULL","")) + "</td></tr>";
					});
					
// 					$("#my123").html("<tr class='noExl'><td></td></tr><tr><td>銷貨單號</td><td>訂單號</td><td>產品名稱</td><td>客戶自訂產品ID</td><td>銷貨數量</td><td>銷貨金額</td><td>轉單日</td><td>配送日</td><td>銷貨/出貨日期</td><td>銷售平台</td><td>備註</td></tr>"+result_table);
// 					$("#products").dataTable().fnDestroy();
					if(json_obj.length!=0){
						$("#products-contain").show();
						$("#products tbody").html(result_table);
// 						$("#products").dataTable({
// 							dom: 'lfrB<t>ip',
// 					        buttons: [{
// 					            extend: 'excel',
// 					            text: '輸出為xlsx檔',
// 					            title: my_name,
// 					            exportOptions: {
// 					                modifier: {
// 					                	search: 'none'
// 					                }
// 					            }
// 					        }],
// 							"language": {"url": "js/dataTables_zh-tw.txt"}
// 						});
// 						alert($("#products").html());
// 						$("#products").find("p").css({"word-break":"break-all","min-width":"120px","text-align":"center" });
// 						$("#products").find("th").css({"word-break":"break-all","max-width":"90px","text-align":"center" });
// 						$("#products").find("td").css({"word-break":"break-all","min-width":"90px","text-align":"center","white-space": "normal"});
						$(".validateTips").text("");
// 						$("#xls").show();
					}else{
						$("#products-contain").hide();
						$(".validateTips").text("查無此結果");
// 						$("#xls").hide();
					}
				}
			});
		});
		$("#dialog-confirm").html("<p>是否確認刪....</p>");
		$("#dialog-confirm").dialog({
			title: "你妳你妳你",
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			height : "auto",
			modal : true,
			show : {effect : "blind",duration : 300},
 			hide : {effect : "blind",duration : 300},
			buttons : {
				"確認刪除" : function() {
					alert("嘿嘿嘿~");
					$(this).dialog("close");
				},
				"取消刪除" : function() {
					$(this).dialog("close");
				}
			}
		});
		//刪除事件聆聽 : 因為聆聽事件動態產生，所以採用delegate來批量處理，節省資源
		$("#products").delegate(".btn_delete", "click", function() {
			uuid = $(this).val();
			confirm_dialog.dialog("open");
		});
		//修改事件聆聽
		$("#products").delegate(".btn_update", "click", function() {
			uuid = $(this).val();
			update_dialog.dialog("open");
			var text = $(this).attr("name");
			$("input[name='original_unit_name']").val(text);
		});
		//新增事件聆聽
		$("#create-productunit").button().on("click", function() {
			insert_dialog.dialog("open");
		});
		//預設表格隱藏
		$("#products-contain").hide();
// 		$("#xls").click(function(){
// 			$(".result2").table2excel({
// 				exclude: ".noExl",
// 				name: "Excel Document Name",
// 				filename: "訂單資料",
// 				fileext: ".xls",
// 				exclude_img: true,
// 				exclude_links: true,
// 				exclude_inputs: true
// 			});
// 		});
	});
	
</script>

<script>
$(function() {
	var value='<%=request.getParameter("action")%>';
	//alert($.url.param('action'));
	if(value=="today"){
		$.ajax({
			type : "POST",
			url : "salereport.do",
			data : {action :"today",time1 : $('#datepicker1').val(),time2 : $('#datepicker2').val()},
			success : function(result) {
				var json_obj = $.parseJSON(result);
				var result_table = "";
				$.each(json_obj,function(i, item) {
					result_table += "<tr><td>"+ json_obj[i].seq_no + "</td><td>"+ json_obj[i].order_no + "</td><td>" + json_obj[i].product_name + "</td><td>"+ json_obj[i].c_product_id + "</td><td>"+ json_obj[i].quantity + "</td><td>"+ json_obj[i].price + "</td><td>" + date_format(json_obj[i].trans_list_date) + "</td><td>"+ date_format(json_obj[i].dis_date) + "</td><td>"+ date_format(json_obj[i].sale_date) + "</td><td>"+ json_obj[i].order_source + "</td><td>"+ (json_obj[i].memo==null?"":json_obj[i].memo) + "</td></tr>";
				});
				
// 				$("#my123").html("<tr class='noExl'><td></td></tr><tr><td>銷貨單號</td><td>訂單號</td><td>產品名稱</td><td>客戶自訂產品ID</td><td>銷貨數量</td><td>銷貨金額</td><td>轉單日</td><td>配送日</td><td>銷貨/出貨日期</td><td>銷售平台</td><td>備註</td></tr>"+result_table);
				//$("#products").dataTable().fnDestroy();
				if(json_obj.length!=0){
					$("#products-contain").show();
					$("#products tbody").html(result_table);
// 					$("#products").dataTable({
// 						"language": {"url": "js/dataTables_zh-tw.txt"}
// 					});
// 					$("#products").find("th").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
					$("#products").find("td").css({"word-break":"break-all","min-width":"70px","text-align":"center" });
					$(".validateTips").text("");
// 					$("#xls").show();
				}else{
					$("#products-contain").hide();
					$(".validateTips").text("查無此結果");
// 					$("#xls").hide();
				}
			}
		});
	}
});
</script>
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">轉單起日</span>
						<input type="text" class="input-date" id="datepicker1">
					</label>
					<div class="forward-mark"></div>
					<label for="">
						<span class="block-label">轉單迄日</span>
						<input type="text" class="input-date" id="datepicker2">
					</label>
					<a class="btn btn-darkblue" id="searh-productunit">查詢</a>
<!-- 					<a class="btn btn btn-exec" id="xls" style="display:none" >產生報表</a> -->
				</div>
			</div><!-- /.form-wrap -->
		</div>

<!-- <div style="margin:20px;"> -->

<!-- 	<div class="panel-content"> -->
<!-- 		<div class="datalistWrap" > -->
<!-- 			 第一列  -->
<!-- 			<div class="row" style="display:none"> -->
<!-- 				<div id="products-serah-create-contain" style="width: 800px;margin:20px auto;" > -->
<!-- 					<table id="products-serah-create" class="ui-widget ui-widget-content"> -->
<!-- 						<tr class="ui-widget-header"> -->
<!-- 							<th > -->
<!-- 								<p style="width:120px;">轉單日</p> -->
<!-- 							</th><th> -->
<!-- 								<input type="text" id="datepicker1" placeholder="起"> -->
<!-- 							</th><th> -->
<!-- 								~ -->
<!-- 							</th><th> -->
<!-- 								<input type="text" id="datepicker2" placeholder="迄"> -->
<!-- 							</th><th> -->
<!-- 								<button id="searh-productunit" onclick="" style="width:80px;">查詢</button> -->
<!-- 							</th><th id="xls" style="display:none"> -->
<!-- 								<button onclick="export_xls()" style="width:100px;">產生報表</button> -->
<!-- 							</th> -->
<!-- 						</tr> -->
<!-- 					</table> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			 第二列 --> 
			<div class="search-result-wrap" >
				<div id="products-contain" class="result-table-wrap" style="width:100%;">
					<table id="products" class="result-table report">
						<thead>
							<tr class="ui-widget-header">
								<th>銷貨單號</th>
								<th>訂單號</th>
								<th>產品名稱</th>
								<th>客戶自訂產品ID</th>
								<th>銷貨數量</th>
								<th>銷貨金額</th>
								<th>轉單日</th>
								<th>配送日</th>
								<th>銷貨/出貨日期</th>
								<th>銷售平台</th>
								<th>備註</th>								
							</tr>
						</thead>
						<tbody>
				            <tr>
				                <td>Tiger Nixon</td>
				                <td>System Architect</td>
				                <td>Edinburgh</td>
				                <td>61</td>
				                <td>2011/04/25</td>
				                <td>$320,800</td>
				                <td>Tiger Nixon</td>
				                <td>System Architect</td>
				                <td>Edinburgh</td>
				                <td>61</td>
				                <td>2011/04/25</td>
				            </tr>
						</tbody>
					</table>
				</div>
				<div class="validateTips" align="center"> </div>
			</div>
<!-- 		</div> -->
<!-- 	</div> -->
<!-- <div id="dialog-confirm"></div> -->
<table id="my123" class="result" style="display:none"><tr><td></td></tr></table>
<!-- 	<table id="example" class="display nowrap" cellspacing="0" width="100%" style="display:none"> -->
<!--         <thead> -->
<!--             <tr> -->
<!--                 <th>Name</th> -->
<!--                 <th>Position</th> -->
<!--                 <th>Office</th> -->
<!--                 <th>Age</th> -->
<!--                 <th>Start date</th> -->
<!--                 <th>Salary</th> -->
<!--             </tr> -->
<!--         </thead> -->
<!--         <tfoot> -->
<!--             <tr> -->
<!--                 <th>Name222</th> -->
<!--                 <th>Position</th> -->
<!--                 <th>Office</th> -->
<!--                 <th>Age</th> -->
<!--                 <th>Start date</th> -->
<!--                 <th>Salary</th> -->
<!--             </tr> -->
<!--         </tfoot> -->
<!--         <tbody> -->
<!--             <tr> -->
<!--                 <td>Tiger Nixon</td> -->
<!--                 <td>System Architect</td> -->
<!--                 <td>Edinburgh</td> -->
<!--                 <td>61</td> -->
<!--                 <td>2011/04/25</td> -->
<!--                 <td>$320,800</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Garrett Winters</td> -->
<!--                 <td>Accountant</td> -->
<!--                 <td>Tokyo</td> -->
<!--                 <td>63</td> -->
<!--                 <td>2011/07/25</td> -->
<!--                 <td>$170,750</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Ashton Cox</td> -->
<!--                 <td>Junior Technical Author</td> -->
<!--                 <td>San Francisco</td> -->
<!--                 <td>66</td> -->
<!--                 <td>2009/01/12</td> -->
<!--                 <td>$86,000</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Garrett Winters</td> -->
<!--                 <td>Accountant</td> -->
<!--                 <td>Tokyo</td> -->
<!--                 <td>63</td> -->
<!--                 <td>2011/07/25</td> -->
<!--                 <td>$170,750</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Ashton Cox</td> -->
<!--                 <td>Junior Technical Author</td> -->
<!--                 <td>San Francisco</td> -->
<!--                 <td>66</td> -->
<!--                 <td>2009/01/12</td> -->
<!--                 <td>$86,000</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Cedric Kelly</td> -->
<!--                 <td>Senior Javascript Developer</td> -->
<!--                 <td>Edinburgh</td> -->
<!--                 <td>22</td> -->
<!--                 <td>2012/03/29</td> -->
<!--                 <td>$433,060</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Airi Satou</td> -->
<!--                 <td>Accountant</td> -->
<!--                 <td>Tokyo</td> -->
<!--                 <td>33</td> -->
<!--                 <td>2008/11/28</td> -->
<!--                 <td>$162,700</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Brielle Williamson</td> -->
<!--                 <td>Integration Specialist</td> -->
<!--                 <td>New York</td> -->
<!--                 <td>61</td> -->
<!--                 <td>2012/12/02</td> -->
<!--                 <td>$372,000</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Herrod Chandler</td> -->
<!--                 <td>Sales Assistant</td> -->
<!--                 <td>San Francisco</td> -->
<!--                 <td>59</td> -->
<!--                 <td>2012/08/06</td> -->
<!--                 <td>$137,500</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Rhona Davidson</td> -->
<!--                 <td>Integration Specialist</td> -->
<!--                 <td>Tokyo</td> -->
<!--                 <td>55</td> -->
<!--                 <td>2010/10/14</td> -->
<!--                 <td>$327,900</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Colleen Hurst</td> -->
<!--                 <td>Javascript Developer</td> -->
<!--                 <td>San Francisco</td> -->
<!--                 <td>39</td> -->
<!--                 <td>2009/09/15</td> -->
<!--                 <td>$205,500</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Sonya Frost</td> -->
<!--                 <td>Software Engineer</td> -->
<!--                 <td>Edinburgh</td> -->
<!--                 <td>23</td> -->
<!--                 <td>2008/12/13</td> -->
<!--                 <td>$103,600</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Jena Gaines</td> -->
<!--                 <td>Office Manager</td> -->
<!--                 <td>London</td> -->
<!--                 <td>30</td> -->
<!--                 <td>2008/12/19</td> -->
<!--                 <td>$90,560</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Quinn Flynn</td> -->
<!--                 <td>Support Lead</td> -->
<!--                 <td>Edinburgh</td> -->
<!--                 <td>22</td> -->
<!--                 <td>2013/03/03</td> -->
<!--                 <td>$342,000</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Charde Marshall</td> -->
<!--                 <td>Regional Director</td> -->
<!--                 <td>San Francisco</td> -->
<!--                 <td>36</td> -->
<!--                 <td>2008/10/16</td> -->
<!--                 <td>$470,600</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Haley Kennedy</td> -->
<!--                 <td>Senior Marketing Designer</td> -->
<!--                 <td>London</td> -->
<!--                 <td>43</td> -->
<!--                 <td>2012/12/18</td> -->
<!--                 <td>$313,500</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Tatyana Fitzpatrick</td> -->
<!--                 <td>Regional Director</td> -->
<!--                 <td>London</td> -->
<!--                 <td>19</td> -->
<!--                 <td>2010/03/17</td> -->
<!--                 <td>$385,750</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Michael Silva</td> -->
<!--                 <td>Marketing Designer</td> -->
<!--                 <td>London</td> -->
<!--                 <td>66</td> -->
<!--                 <td>2012/11/27</td> -->
<!--                 <td>$198,500</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Paul Byrd</td> -->
<!--                 <td>Chief Financial Officer (CFO)</td> -->
<!--                 <td>New York</td> -->
<!--                 <td>64</td> -->
<!--                 <td>2010/06/09</td> -->
<!--                 <td>$725,000</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Gloria Little</td> -->
<!--                 <td>Systems Administrator</td> -->
<!--                 <td>New York</td> -->
<!--                 <td>59</td> -->
<!--                 <td>2009/04/10</td> -->
<!--                 <td>$237,500</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Bradley Greer</td> -->
<!--                 <td>Software Engineer</td> -->
<!--                 <td>London</td> -->
<!--                 <td>41</td> -->
<!--                 <td>2012/10/13</td> -->
<!--                 <td>$132,000</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Dai Rios</td> -->
<!--                 <td>Personnel Lead</td> -->
<!--                 <td>Edinburgh</td> -->
<!--                 <td>35</td> -->
<!--                 <td>2012/09/26</td> -->
<!--                 <td>$217,500</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Jenette Caldwell</td> -->
<!--                 <td>Development Lead</td> -->
<!--                 <td>New York</td> -->
<!--                 <td>30</td> -->
<!--                 <td>2011/09/03</td> -->
<!--                 <td>$345,000</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Yuri Berry</td> -->
<!--                 <td>Chief Marketing Officer (CMO)</td> -->
<!--                 <td>New York</td> -->
<!--                 <td>40</td> -->
<!--                 <td>2009/06/25</td> -->
<!--                 <td>$675,000</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Caesar Vance</td> -->
<!--                 <td>Pre-Sales Support</td> -->
<!--                 <td>New York</td> -->
<!--                 <td>21</td> -->
<!--                 <td>2011/12/12</td> -->
<!--                 <td>$106,450</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Doris Wilder</td> -->
<!--                 <td>Sales Assistant</td> -->
<!--                 <td>Sidney</td> -->
<!--                 <td>23</td> -->
<!--                 <td>2010/09/20</td> -->
<!--                 <td>$85,600</td> -->
<!--             </tr> -->
<!--             <tr> -->
<!--                 <td>Angelica Ramos</td> -->
<!--                 <td>Chief Executive Officer (CEO)</td> -->
<!--                 <td>London</td> -->
<!--                 <td>47</td> -->
<!--                 <td>2009/10/09</td> -->
<!--                 <td>$1,200,000</td> -->
<!--             </tr> -->
<!--         </tbody> -->
<!--     </table> -->
</div>
</body>
</html>