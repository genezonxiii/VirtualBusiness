<%@ page language="java" contentType="text/html; charset=BIG5" pageEncoding="BIG5"%>
<jsp:include page="template.jsp" flush="true"/>

<!DOCTYPE html>
<html>
<head>
	<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
	<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
	<script type="text/javascript" src="js/jquery-ui.min.js"></script>
	
	<title>���~��Ӹ�ƺ޲z</title>	
	
	<script>
	$(function() {
	
	});
	</script>
</head>
<body>
	<div class="content-wrap" >
		<!-- �d�� -->
		<div class="input-field-wrap">
			<div class="form-wrap">
				<div class="form-row">
					<label for="">
						<span class="block-label">�����ӦW��</span>
						<input type="text" id="" name=""></input>
					</label>
					<label for="">
						<span class="block-label">���~�W��</span>
						<input type="text" id="" name=""></input>
					</label>
					<button class="btn btn-darkblue" id="">�d��</button>
					<button class="btn btn-exec btn-wide" id="">�s�W�ӫ~���</button>
				</div>
			</div>
		</div>
		<div class="row search-result-wrap" align="center">
			<div class="ui-widget" style="display:none">
				<table class="result-table">
					<thead>
						<tr>
							<th>���x�W��</th>
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