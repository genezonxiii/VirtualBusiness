<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>訂單拋轉</title>
	<link rel="stylesheet" href="vendor/css/jquery-ui.min.css">
	<link rel="stylesheet" href="vendor/css/font-awesome.min.css">
	<link rel="stylesheet" href="css/styles.css">
<style>

.up_icon,.up_input {position: absolute;top: 10px;left: 10px;width: 64px;height: 64px;z-index: 2}
.up_icon {overflow: hidden;font-size: 0;line-height: 99em;background: url(http://sfault-image.b0.upaiyun.com/346/967/3469672357-54250d7235406_articlex) no-repeat 0 0;z-index: 1;}
.on_it {
	background: #d8d8d8 !important;
}
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap">
		<div class='bdyplane' style="opacity:0">
	<h2 class="page-title">訂單拋轉作業</h2>
		<div class="search-result-wrap" style="margin-bottom: 0px;">
			<div class="result-table-func-wrap">

				<div class="ec-radio-group-wrap">
<!-- 				<input id="radio-11" type="radio" name="ec-radio-group" value="yahoo"><label for="radio-11" id="test"><img src="images/ec-logos/yahoo.png" alt="" onmouseover="rotateYDIV(this)"><span>Yahoo 購物中心</span></label> -->
					<input id="radio-11" type="radio" name="ec-radio-group" value="yahoo"><label for="radio-11" id="test"><img src="images/ec-logos/yahoo.png" alt=""><span>Yahoo 購物中心</span></label>
					<input id="radio-19" type="radio" name="ec-radio-group" value="rakuten"><label for="radio-19"><img src="images/ec-logos/rakuten.png" alt=""><span>樂天</span></label>
					<input id="radio-06" type="radio" name="ec-radio-group" value="momo"><label for="radio-06"><img src="images/ec-logos/momo.png" alt=""><span>momo</span></label>
					<input id="radio-17" type="radio" name="ec-radio-group" value="umall"><label for="radio-17"><img src="images/ec-logos/Umall.png" alt=""><span>森森</span></label>
					<input id="radio-01" type="radio" name="ec-radio-group" value="asap"><label for="radio-01"><img src="images/ec-logos/ASAP.png" alt=""><span>ASAP</span></label>
				</div><!-- /.ec-radio-group-wrap -->

				<div class="ec-radio-group-wrap">
					<input id="radio-16" type="radio" name="ec-radio-group" value="yahoomall"><label for="radio-16"><img src="images/ec-logos/supermall.png" alt=""><span>Yahoo 超級商城</span></label>
					<input id="radio-08" type="radio" name="ec-radio-group" value="payeasy"><label for="radio-08"><img src="images/ec-logos/payeasy.png" alt=""><span>PayEasy</span></label>
					<input id="radio-18" type="radio" name="ec-radio-group" value="amart"><label for="radio-18"><img src="images/ec-logos/AMart.png" alt=""><span>愛買</span></label>
					<input id="radio-10" type="radio" name="ec-radio-group" value="udn"><label for="radio-10"><img src="images/ec-logos/udn.png" alt=""><span>UDN</span></label>
					<input id="radio-20" type="radio" name="ec-radio-group" value="etmall"><label for="radio-20"><img src="images/ec-logos/EHS.png" alt=""><span>東森購物</span></label>
				</div><!-- /.ec-radio-group-wrap -->
				<div class="ec-radio-group-wrap">
					<input id="radio-09" type="radio" name="ec-radio-group" value="pchome"><label for="radio-09"><img src="images/ec-logos/pchome.png" alt=""><span>PCHome</span></label>
					<input id="radio-15" type="radio" name="ec-radio-group" value="books"><label for="radio-15"><img src="images/ec-logos/books.png" alt=""><span>博客來</span></label>
					<input id="radio-02" type="radio" name="ec-radio-group" value="gohappy"><label for="radio-02"><img src="images/ec-logos/GoHappy.png" alt=""><span>GoHappy</span></label>
					<input id="radio-12" type="radio" name="ec-radio-group" value="91mai"><label for="radio-12"><img src="images/ec-logos/91.png" alt=""><span>九易</span></label> 
					<input id="radio-13" type="radio" name="ec-radio-group" value="treemall"><label for="radio-13"><img src="images/ec-logos/treemall.png" alt=""><span>國泰 Tree</span></label>
				</div><!-- /.ec-radio-group-wrap -->
				<div class="ec-radio-group-wrap">
					 <input id="radio-14" type="radio" name="ec-radio-group" value="gomaji"><label for="radio-14"><img src="images/ec-logos/GOMAJI.png" alt=""><span>夠麻吉</span></label>
					 <input id="radio-03" type="radio" name="ec-radio-group" value="ibon"><label for="radio-03"><img src="images/ec-logos/ibon.png" alt=""><span>ibon</span></label>
					 <input id="radio-07" type="radio" name="ec-radio-group" value="myfone"><label for="radio-07"><img src="images/ec-logos/myfone.png" alt=""><span>myfone</span></label>
					 <input id="radio-04" type="radio" name="ec-radio-group" value="17life"><label for="radio-04"><img src="images/ec-logos/Life.png" alt=""><span>17Life</span></label>
					 <input id="radio-05" type="radio" name="ec-radio-group" value="linemart"><label for="radio-05"><img src="images/ec-logos/Line_Mart.png" alt=""><span>Line Mart</span></label>
				</div><!-- /.ec-radio-group-wrap -->
				
				<hr class="hr-gray">
			</div><!-- /.result-table-func-wrap -->
			<div style="text-align:center; margin:0px auto;font-size:35px;"></div>
				<form action="upload.do" id="form1" method="post" enctype="multipart/form-data" style="margin:0px;">
					<input type="file" id="file" name="file" accept=".csv,.pdf,.xls,.xlsx" style="opacity:0;position:absolute;margin:6px;max-width:353px;"/>
					<div style="opacity:0.6;position:absolute;padding-top:7px;padding-left:5px;"><button id="btn" onclick="return false;">瀏覽...</button></div>
					<input type="text" id="upload_name" size="40" style="z-index:-1;padding-left:70px;" />
					<input type="submit" onclick="return setV();" value="檔案上傳" class="btn btn-exec btn-wide" style="color: #fff;margin-left:20px"/>
					<br><br>
					
					<table width="100%" >
					<tr><td style="text-align:center">
							<a href='./salereport.jsp?action=upload' style="font-size:25px;">訂單報表</a>
						</td><td style="text-align:center">
							<a href='./shipreport.jsp?action=upload' style="font-size:25px;">出貨報表</a>
				<!-- 		</td><td style="text-align:center"> -->
				<!-- 			<a href='./distributereport.jsp' style="font-size:25px;">配送報表</a> -->
						</td><td style="text-align:center">
							<a href='./salereturnreport.jsp?action=upload' style="font-size:25px;" >退貨報表</a>
						</td><tr>
					</table>
					<div id="msg" style="color:red;margin:5px;display:none;">請稍候片刻...</div>
				</form>
			</div>
		</div><!-- /.search-result-wrap -->
	</div><!-- /.content-wrap -->
<%
String str=(String)request.getAttribute("action");
if(str!=null){
	if("success".equals(str)){
		out.println("<script>alert('匯入成功');window.location.href = './upload.jsp';</script>");
	}else{
		out.println("<script>console.log('"+str+"');</script>");
		out.println("<script>alert('匯入失敗：\\n　　請確認檔案的格式是否遭到修改。');window.location.href = './upload.jsp';</script>");
	}
}
%>
<script>
$(function(){
	$(".bdyplane").animate({"opacity":"1"});
	$("label").hover(function(){$(this).addClass("on_it");},function(){$(this).removeClass("on_it");});
	$("#file").change(function(){
		$("#upload_name").val($("#file").val());
	});
	$("#btn").click(function(){
		$("#file").trigger("click");
	});
	$("#upload_name").click(function(){
		$("#file").trigger("click");
	});
});
</script>
<script>
var x,y,n=0,ny=0,rotINT,rotYINT
function rotateDIV()
{
	x=document.getElementById("test")
	clearInterval(rotINT)
	rotINT=setInterval("startRotate()",10)
}
function rotateYDIV(thi)
{
	//alert(thi.parentElement);
	//y=document.getElementById(parent.attr("id"));
	y=thi.parentElement;
	clearInterval(rotYINT)
	rotYINT=setInterval("startYRotate()",10)
}
function startRotate()
{
	n=n+1
	x.style.transform="rotate(" + n + "deg)"
	x.style.webkitTransform="rotate(" + n + "deg)"
	x.style.OTransform="rotate(" + n + "deg)"
	x.style.MozTransform="rotate(" + n + "deg)"
	if (n==180 || n==360)
	{
		clearInterval(rotINT)
		if (n==360){n=0}
	}
}
function startYRotate()
{
	ny=ny+1
	y.style.transform="rotateY(" + ny + "deg)"
	y.style.webkitTransform="rotateY(" + ny + "deg)"
	y.style.OTransform="rotateY(" + ny + "deg)"
	y.style.MozTransform="rotateY(" + ny + "deg)"
	if (ny==180 || ny>=360)
	{
		clearInterval(rotYINT)
		if (ny>=360){ny=0}
	}
}
function setV(){
	//alert("hello".indexOf('he')!=-1);
	if($("#file").val()<1){alert("請選擇檔案");return false;}
	var i=0;
	//alert("跳轉upload.do前");
	while(!document.getElementsByName("ec-radio-group")[i].checked){
		i++;
		if(i==20){alert("請選擇平台");return false;}
	}
	console.log("為何進來了?");
	document.getElementById("form1").action+="?vender="+document.getElementsByName("ec-radio-group")[i].value;
	return true;
};
</script>
</body>
</html>