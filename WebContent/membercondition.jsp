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
<title>會員分級</title>
<meta charset="utf-8">
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico" />
<link rel="stylesheet" href="css/styles.css" />
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
<link rel="stylesheet" href="css/multi-select.css">
<style>
.custom-header{
  text-align: center;
  padding: 3px;
  background: #000;
  color: #fff;
}
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">
		
<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery.multi-select.js"></script>

<script>
$(function() {
	$(".bdyplane").animate({"opacity":"1"});
	$("#new").click(function(){
		$("#membercondition-insert").dialog('open');
	});
	$(".btn-darkblue").click(function(){
		$("#membercondition-insert").dialog('open');
	});
	$(".btn-alert").click(function(){
		$("#membercondition-insert").dialog('open');
	});
	$("#membercondition-insert").dialog({
			draggable : false,//防止拖曳
			resizable : false,//防止縮放
			autoOpen : false,
			show : {
				effect : "blind",
				duration : 300
			},
			hide : {
				effect : "fade",
				duration : 300
			},
			width : "auto",
			height : "auto",
			modal : true,
			buttons : [{
						id : "insert",
						text : "新增",
						click : function() {
// 								if ($('#insert-dialog-form-post').valid()) {
								$.ajax({
									type : "POST",
									url : "membercondition.do",
									data : {
										action : "insert"
									},
									success : function(result) {
										console(result);
									}
								});
								$("#membercondition-insert").dialog("close");
								//$("#insert-dialog-form-post").trigger("reset");
// 								}
						}
					}, {
						text : "取消",
						click : function() {
							//$("#insert-dialog-form-post").trigger("reset");
							//validator_insert.resetForm();
							$("#membercondition-insert").dialog("close");
						}
					} ],
			close : function() {
				//validator_insert.resetForm();
				//$("#insert-dialog-form-post").trigger("reset");
			}
	});
	$("#membercondition-insert").show();
	
});
</script>
		<div class="input-field-wrap">
			<!-- 第一列 -->
			<div class="form-wrap">
				<div class="btn-row">
					<button id="new" class="btn btn-exec btn-wide">新增</button>
				</div>
				<div id="errormesg"></div>
			</div><!-- /.form-wrap -->
		</div><!-- /.input-field-wrap -->
		<div class="search-result-wrap">
			<div class="result-table-wrap">
				<table class="result-table">
					<thead>
						<tr>
							<th>級別</th>
							<th>限定時間內</th>
							<th>累計消費金額</th>
							<th>有效期限</th>
							<th style="width:30%">優惠內容</th>
							<th style="width:20%">備註</th>
							<th>功能</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>1.普通會員</td><td>無</td><td>滿1元</td><td>永久</td><td style="padding-left:20px"><li>購物滿1000元，商品99折</li></td><td>凡消費即可成為普通會員。請憑發票至櫃檯辦理，攜證件並準備100元辦卡費用</td><td><div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i><div class="table-function-list"><a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a><a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a></div></div></td>
						</tr>
						<tr>
							<td>2.銀卡會員</td><td>至今三個月內</td><td>滿3000元</td><td>一年</td><td style="padding-left:20px"><li>購物滿1000元，可獲折價券100元。</li></td><td>折價券不可當於當次消費使用。</td><td><div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i><div class="table-function-list"><a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a><a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a></div></div></td>
						</tr>
						<tr>
							<td>3.金卡會員</td><td>至今半年</td><td>滿10000元</td><td>一年</td><td style="padding-left:20px"><li>購物滿1000元，消費商品85折優待，</li></td><td>可免費獲得每季特色貼心贈品，來店領取。</td><td><div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i><div class="table-function-list"><a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a><a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a></div></div></td>
						</tr>
						<tr>
							<td>4.尊爵會員</td><td>無</td><td>滿100000元</td><td>永久</td><td style="padding-left:20px"><li>消費商品8折優待，不可合併促銷活動使用。</li></td><td>達成成就可終身享有尊爵會員優待。</td><td><div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i><div class="table-function-list"><a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a><a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a></div></div></td>
						</tr>
						<tr>
							<td>5.鈦金會員</td><td>一年內</td><td>滿30000元</td><td>一年</td><td style="padding-left:20px"><li>消費商品7折優待不可合併促銷活動使用。</li><li>購物滿20000元，可再折抵1000元。</li></td><td>可優先訂位，享有本公司最高級別待遇。</td><td><div href="#" class="table-row-func btn-in-table btn-gray"><i class="fa fa-ellipsis-h"></i><div class="table-function-list"><a href="#" class="btn-in-table btn-darkblue"><i class="fa fa-pencil"></i></a><a href="#" class="btn-in-table btn-alert"><i class="fa fa-trash"></i></a></div></div></td>
						</tr>
					</tbody>

				</table>
			</div>
		</div>
		</div>
	</div>
<div id="membercondition-insert" title="新增會員分級" style="display:none">
	<table style="border-collapse: separate;border-spacing: 10px 20px;">
		<tr>
			<td><p>優先順序</p></td>
			<td> 
				<select>
					<option value="">1</option>
					<option value="">2</option>
					<option value="">3</option>
					<option value="">4</option>
					<option value="">5</option>
					<option value="">6</option>
					<option value="">7</option>
					<option value="">8</option>
					<option value="">9</option>
					<option value="">10</option>
					<option value="">11</option>
					<option value="">12</option>
				</select> 
			</td>
			<td><p>級別名稱</p></td>
			<td><input type="text" name="name"  placeholder="普通會員"></td>
		</tr>
		<tr>
			<td><p>限定時間</p></td>
			<td><input type="text" name="name" style="width:72px" placeholder="數字">
				<select>
					<option value=""></option>
					<option value="">年</option>
					<option value="">月</option>
					<option value="">日</option>
					<option value="">季</option>
				</select> 
			</td>
			<td><p>類計金額</p></td>
			<td><input type="text" name="name"></td>
		</tr>
		<tr>
			<td><p>有效時限</p></td>
			<td>
				<input type="text" name="name" style="width:72px" placeholder="數字">
				<select>
					<option value=""></option>
					<option value="">年</option>
					<option value="">月</option>
					<option value="">日</option>
					<option value="">季</option>
				</select> 
			</td>
		</tr>
		<tr>
			<td><p>優惠內容</p></td>
			<td colspan=3><textarea  name="memo" style="width:96%"></textarea></td>
		</tr>
		<tr>
			<td><p>備註</p></td>
			<td colspan=3><textarea  name="memo" style="width:96%"></textarea></td>
		</tr>
	</table>
</div>
</body>
</html>


<script>
//	$('#my-select').multiSelect({
//	selectableHeader: "<div class='custom-header'>分級選項</div>",
//	selectionHeader: "<div class='custom-header'>已選擇</div>",
//	afterSelect: function(values){
//	  alert("Select value: "+values);
//	},
//	afterDeselect: function(values){
//	  alert("Deselect value: "+values);
//	}
//});
//$('#my-select').multiSelect('select_all');
//$('#my-select').multiSelect('deselect_all');
//$('#my-select').multiSelect('refresh');
//$('#my-select').multiSelect('addOption', { value: 'testv', text: 'test', index: 3, nested: 'optgroup_label' });

//$('#my-select').multiSelect('select', (['elem_1', 'elem_2']));
</script>
<!-- 			<div class="row search-result-wrap" align="center"> -->
<!-- 				<div id="products-serah-create-contain" class="ui-widget result-table-wrap"> -->
<!-- 				<form name="password-form-post" id="password-form-post" class="result-table"> -->
<!-- 						<table  id="password"> -->
<!-- 							<tbody> -->
<!-- 								<tr> -->
<!-- 									<td><h2>新密碼:</h2></td> -->
<!-- 									<td><input type="password" name="password"  placeholder="輸入新密碼"/></td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td><h2>新密碼確認:</h2></td> -->
<!-- 									<td><input type="password" name="password2"  placeholder="再輸入新密碼"/></td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td colspan="2"> -->
<!-- 										<button id="password_btn" class="btn btn-darkblue">更改密碼</button> -->
<!-- 									</td> -->
<!-- 								</tr> -->
<!-- 								<tr> -->
<!-- 									<td colspan="2"><div id="errormesg"></div></td> -->
								
<!-- 								</tr> -->
<!-- 							</tbody> -->
<!-- 						</table> -->
<!-- 						</form> -->
<!-- 				</div> -->
<!-- 			</div> -->
	<!-- 				<div class="btn-row"> -->
	<!-- 					<label for=""> -->
	<!-- 						<select multiple="multiple" id="my-select" name="my-select[]"> -->
	<!-- 					      <option value='elem_1'>消費滿XXX且</option> -->
	<!-- 					      <option value='elem_2'>時間</option> -->
	<!-- 					      <option value='elem_3'>elem 3</option> -->
	<!-- 					      <option value='elem_4'>elem 4</option> -->
	<!-- 					      <option value='elem_100'>elem 100</option> -->
	<!-- 					    </select> -->
	<!-- 				    </label> -->
	<!-- 				</div> -->