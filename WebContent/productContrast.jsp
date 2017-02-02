<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>
<jsp:include page="template.jsp" flush="true"/>

<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
	<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui.min.js"></script>
	
	<title>產品對照資料管理</title>	
	
	<script>
	$(function() {
	
	});
	</script>
</head>
<body>
	<div class="content-wrap" >
		<!-- 查詢 -->
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">供應商名稱</span>
						<input type="text" id="" name=""></input>
					</label>
					<label for="">
						<span class="block-label">產品名稱</span>
						<input type="text" id="" name=""></input>
					</label>
					<button class="btn btn-darkblue" id="">查詢</button>
					<button class="btn btn-exec btn-wide" id="">新增商品資料</button>
				</div>
			</div>
		</div>
		<div class="row search-result-wrap" align="center">
			<div class="ui-widget" style="display:none">
				<table class="result-table">
					<thead>
						<tr>
							<th>平台名稱</th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
							<th></th>
						</tr>	
					</thead>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>	
	</div>
</body>
</html>