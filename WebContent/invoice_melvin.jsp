<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<title>發票開立</title>
	<meta charset="utf-8">
</head>
<body>
	<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
	<script type="text/javascript" src="js/jquery.session.js"></script>
	<script>
	$(function() {
	
		$("#getInvoice").click(function(e) {
			e.preventDefault();
			
			$.ajax({
				type : "POST",
				url : "invoice.do",
				data : {
					action : "createInvoice",
					sale_id : $("input[name='sale_id'").val(),
				},
				success : function(result) {
					var json_obj = $.parseJSON(result);
					console.log("success!!!!");
					console.log("Result:" + result);
					console.log("Result Length:" + json_obj.length);
					
					var wholeInvoiceData = '';
					$.each (json_obj, function (i) {
						console.log("***Master***");
						console.log(json_obj[i].group_id);
						console.log(json_obj[i].aMessageType);
						console.log(json_obj[i].cInvoiceNo);
// 						console.log(json_obj[i].invoiceDetail);
						
						wholeInvoiceData = json_obj[i].aMessageType + '\t' + json_obj[i].cInvoiceNo + '\t' + 'test'+ '\r\n'
						var json_obj_detail = json_obj[i].invoiceDetail;
						$.each (json_obj_detail, function (j) {
							console.log("***Detail new***");
							console.log(json_obj_detail[j].dSeqNo);
							console.log(json_obj_detail[j].eProductName);
							
							wholeInvoiceData += json_obj_detail[j].dSeqNo + '\t' + json_obj_detail[j].eProductName + '\t' + 'test'+ '\r\n'
						});
					});
					$.session.set('invoiceData', wholeInvoiceData);
					window.location.replace("createInvoice.jsp");
					
					
				}
			});
		});
		
		$("#getInvoice2").click(function(e) {
			e.preventDefault();

			console.log("getInvoice2");
			
			$.ajax({
				type : "POST",
				url : "invoice.do",
				data : {
					action : "createInvoiceFile",
					sale_id : $("input[name='sale_id'").val(),
				},
				success : function(result) {
					console.log('test');
				}
			});
		});
		
		$("#getInvoice3").click(function(e) {
			e.preventDefault();

			console.log("getInvoice3");
			
			$.ajax({
				type : "POST",
				url : "invoice.do",
				data : {
					action : "generateInvoiceNo",
					sale_id : $("input[name='sale_id'").val(),
				},
				success : function(result) {
					console.log('test');
				}
			});
		});
	});
	</script>


	<form name="invoice" id="invoice" style="display:inline">
		<fieldset>
			<table>
				<tbody>
					<tr>
						<td>訂單ID:</td>
						<td><input type="text" id="sale_id" name="sale_id" value="00077927-68fa-11e6-897a-005056af760c"/></td>
						<td><button id="getInvoice">取得發票</button></td>
						<td><button id="getInvoice2">取得發票(file)</button></td>
						<td><button id="getInvoice3">產生發票號碼</button></td>
					</tr>
					
				</tbody>
    		</table>		
		</fieldset>
	</form>


</body>
</html>