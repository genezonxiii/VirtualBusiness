<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>網購拋轉作業</title>
	<link rel="stylesheet" href="vendor/css/jquery-ui.min.css">
	<link rel="stylesheet" href="vendor/css/font-awesome.min.css">
	<link rel="stylesheet" href="css/styles.css">
<style>

.on_it {
	background: #d8d8d8 !important;
}
</style>
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	
	<script src="./js/jquery.form.js"></script> 
	<div class="content-wrap" style='position:relative;'>
		<div class='bdyplane' style="opacity:0">
<!-- 	<h2 class="page-title">訂單拋轉作業</h2> -->
		<div class="search-result-wrap" style="margin-bottom: 0px;" >
			<div class="result-table-func-wrap" id='panel'>

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
				<div class="ec-radio-group-wrap">
					 <input id="radio-21" type="radio" name="ec-radio-group" value="babyhome"><label for="radio-21"><img src="images/ec-logos/babyhome.png" alt=""><span>寶貝家庭親子網</span></label>
					 <input id="radio-22" type="radio" name="ec-radio-group" value="friday"><label for="radio-22"><img src="images/ec-logos/friday.png" alt=""><span>friDay購物</span></label>
					 <input id="radio-23" type="radio" name="ec-radio-group" value="ihergo"><label for="radio-23"><img src="images/ec-logos/ihergo.png" alt=""><span>愛合購</span></label>
					 <input id="radio-24" type="radio" name="ec-radio-group" value="bigbuy"><label for="radio-24"><img src="images/ec-logos/bigbuy.png" alt=""><span>大買家</span></label>
					 <input id="radio-25" type="radio" name="ec-radio-group" value="momomall"><label for="radio-25"><img src="images/ec-logos/momomall.png" alt=""><span>MOMO摩天商城</span></label>
				</div><!-- /.ec-radio-group-wrap -->
				
				<hr class="hr-gray">
			</div><!-- /.result-table-func-wrap -->
			<div style="text-align:center; margin:0px auto;font-size:35px;"></div>
				<form action="upload.do" id="form1" method="post" enctype="multipart/form-data" style="margin:0px;">
					<input type="file" id="file" name="file" accept=".csv,.pdf,.xls,.xlsx" style="opacity:0;position:absolute;margin:6px;max-width:353px; " onmouseout='$("#upload_name").css("background-image","url(images/browse.png)");' onmouseover='$("#upload_name").css("background-image","url(images/browseon.png)");' />
<!-- 					<div style="opacity:0.6;position:absolute;padding-top:7px;padding-left:5px;"><button id="btn" onclick="return false;">瀏覽...</button></div> -->
					<input type="text" id="upload_name" size="40" style='z-index:-1;padding-left:70px;background-color:#EEF3F9;background-image:url(images/browse.png);background-repeat: no-repeat;'  />
					<input type="submit" id="submitbtn" restrict="" onclick="return setV();" value="檔案上傳" class="btn btn-exec btn-wide" style="color: #fff;margin-left:20px"/>
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<span id="msg" style="color:red;font-size:20px;line-height:40px;vertical-align:bottom;display:none;">資料傳輸中...請稍候!&nbsp;&nbsp;<img src="./images/loss.gif" style="vertical-align:middle;max-width:30px;max-height:50px;"/></span>
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
					
				</form>
			</div>
		</div><!-- /.search-result-wrap -->
	</div><!-- /.content-wrap -->
	<div id ="message" title="訊息" style='padding:30px 0;text-align:center;font-size:30px;font-family: "微軟正黑體", "Microsoft JhengHei", Arial, Helvetica, sans-serif, \5FAE\8EDF\6B63\9ED1\9AD4,\65B0\7D30\660E\9AD4;'></div>
	<div id ="choose-order-type" title="選擇訂單類型"></div>
	<audio id="stamp_voice" src="./audio/stamp_voice.wav" style='display:none;'></audio> 
<script>
var platform_dialog={};
$(function(){
	$('.bdyplane').animate({'opacity':'1'});
	$.ajax({
		type : "POST",
		url : "upload.do",
		data : {action :"select_platform_kind" },
		success : function(result) {
// 			alert(result);
			var json_obj = $.parseJSON(result);
			var platforms = '<div class="ec-radio-group-wrap">';
			$.each (json_obj, function (i) {
				var tmp = json_obj[i].throwfile_platform;
				
				$.ajax({
					type : "POST",
					url : "upload.do",
// 					async : false,
					data : {action :"select_way_of_platform", platform : tmp },
					success : function(result) {
// 						alert(result);
						var json_obj = $.parseJSON(result);
						var ordertype = "",dialogvalue="";
						$.each (json_obj, function (i) {
							if(json_obj[i].throwfile_type!="general"){
								ordertype+="<input id='type-radio-"+i+"' type='radio' name='ordertype' value='"+json_obj[i].throwfile_type+"' restrict='"+json_obj[i].throwfile_fileextension+"'><label for='type-radio-"+i+"' ><span class='form-label'>"+json_obj[i].throwfile_name+"</span></label>";
							}
						});
						dialogvalue+="<div align='center'>"+ordertype+"</div>";
						platform_dialog[tmp]=dialogvalue;
// 						alert(tmp+" ++ "+dialogvalue);
					}
				});

				if(i!=0 && i% 5==0){
					platforms+='</div><div class="ec-radio-group-wrap">';
				}
				platforms+= '<input id="radio-'+ i +'" type="radio" name="ec-radio-group" value="';
				platforms+= json_obj[i].throwfile_platform+'"><label for="radio-'+i+'" style="position:relative;"><img src="images/';
				platforms+= ((json_obj[i].icon.length>1) ? json_obj[i].icon : "ec-logos/noname.png");
				platforms+= '"alt=""><span>'+json_obj[i].memo+'</span></label>';
			});
			platforms += "</div><hr class='hr-gray'>";
			$("#panel").html(platforms);
		}
	});

	$('#form1').ajaxForm(function(result) {
		var str = result;
		if(str!=null){
			$("#msg").hide();
			if("success"==str){
				$("#message").html('匯入成功');
				$("#message").dialog("open");
			}else{
				var timer = setTimeout(function(){
					console.log(str);
					$("#message").html('匯入失敗。<br><div style="font-size:18px;">請確認檔案的格式是否遭到修改。</div>');
					$("#message").dialog("open");
				},500); 
				var json_obj = $.parseJSON(str);
				if(json_obj["success"]==true){
					clearTimeout(timer);
// 					alert('成功匯入　"'+json_obj["total"]+'"　筆資料');
					$("#message").html('成功匯入&nbsp;"'+json_obj["total"]+'"&nbsp;筆資料');
					$("#message").dialog("open");
				}
			}
		}
    });
// 	$("label").hover(function(){$(this).addClass("on_it");},function(){$(this).removeClass("on_it");});
	$('#panel').delegate('label','mouseenter mouseleave', function( event ) {
	    if( event.type === 'mouseenter' ){  
	    	$(this).addClass("on_it");
		}else{
	    	$(this).removeClass("on_it");
	    }
	});
	$("#file").change(function(){
		$("#upload_name").val($("#file").val());
	});
// 	$("#btn").click(function(){
// 		$("#file").trigger("click");
// 	});
	$("#upload_name").click(function(){
		$("#file").trigger("click");
	});
	$("#panel").delegate(":radio[name='ec-radio-group']", "click", function(e) {
// 	$(":radio[name='ec-radio-group']").click(function(){
		
// 		alert($(this).val()+" "+platform_dialog[$(this).val()]);
		$("#choose-order-type").html(platform_dialog[$(this).val()]);
		if(platform_dialog[$(this).val()].length>30){
			$("#choose-order-type").dialog("open");
		}else{
			$(".choosed").remove();
			$( "input:checked[name='ec-radio-group']" ).attr("value2","general");
			$("#submitbtn").attr("restrict","");
		}
	});
	$("#message").dialog({
		draggable : true, resizable : false, autoOpen : false,
		height : "auto", width : 400, modal : true,
		show : {effect : "size",duration : 300},
		hide : {effect : "fade",duration : 300},
		buttons : [{
					text : "確定",
					click : function() {
						$("#message").dialog("close");
					}
				}],
		close : function() {
			$("#message").dialog("close");
		}
	});
	$("#message").show();
	$("#choose-order-type").dialog({
		draggable : true, resizable : false, autoOpen : false,
		height : "auto", width : "auto", modal : true,
		show : {effect : "blind",duration : 300},
		hide : {effect : "fade",duration : 300},
		buttons : [{
					text : "確定",
					click : function() {
						if($( "input:checked[name='ordertype']" ).length>0){
							//instore-pickup 超取 <img class='choose' style="position:absolute;left:0;" src="./images/seal_1.png"/>
							//home-delivery 宅配
							//drop-shipping 第三方
							$(".choosed").remove();
							document.getElementById('stamp_voice').play();
							if($( "input:checked[name='ordertype']" ).val()=="instore-pickup"){
								$( ".content-wrap" ).append('<img class="choosed" style="position:absolute;left:'+($( "label[for='"+$( "input:checked[name='ec-radio-group']" ).attr("id")+"']" ).offset().left-90)+'px;top:'+($( "label[for='"+$( "input:checked[name='ec-radio-group']" ).attr("id")+"']" ).offset().top-80)+'px;" src="./images/seal_1.png"/>');
							}else if($( "input:checked[name='ordertype']" ).val()=="home-delivery"){
								$( ".content-wrap" ).append('<img class="choosed" style="position:absolute;left:'+($( "label[for='"+$( "input:checked[name='ec-radio-group']" ).attr("id")+"']" ).offset().left-90)+'px;top:'+($( "label[for='"+$( "input:checked[name='ec-radio-group']" ).attr("id")+"']" ).offset().top-80)+'px;" src="./images/seal_2.png"/>');
							}else if($( "input:checked[name='ordertype']" ).val()=="drop-shipping"){
								$( ".content-wrap" ).append('<img class="choosed" style="position:absolute;left:'+($( "label[for='"+$( "input:checked[name='ec-radio-group']" ).attr("id")+"']" ).offset().left-120)+'px;top:'+($( "label[for='"+$( "input:checked[name='ec-radio-group']" ).attr("id")+"']" ).offset().top-80)+'px;" src="./images/seal_3.png"/>');
							}
							$( "input:checked[name='ec-radio-group']" ).attr("value2",$( "input:checked[name='ordertype']" ).val());
							$("#submitbtn").attr("restrict",$( "input:checked[name='ordertype']" ).attr("restrict"));
							$("#choose-order-type").dialog("close");
// 							alert($( "input:checked[name='ordertype']" ).attr("restrict"));
						}else{
							$("#message").html("請選擇訂單類型!");
							$("#message").dialog("open");
// 							alert("請選擇訂單類型!");
						}
					}
				}, {
					text : "取消",
					click : function() {
						$( "input:checked[name='ec-radio-group']" ).prop("checked", false);
						$("#choose-order-type").dialog("close");
					}
				} ],
		close : function() {
			if($( "input:checked[name='ordertype']" ).length==0){
				$( "input:checked[name='ec-radio-group']" ).prop("checked", false);
			}
		}
	});
	$("#choose-order-type").show();
});
</script>
<script>
// var x,y,n=0,ny=0,rotINT,rotYINT
// function rotateDIV()
// {
// 	x=document.getElementById("test")
// 	clearInterval(rotINT)
// 	rotINT=setInterval("startRotate()",10)
// }
// function rotateYDIV(thi)
// {
// 	//alert(thi.parentElement);
// 	//y=document.getElementById(parent.attr("id"));
// 	y=thi.parentElement;
// 	clearInterval(rotYINT)
// 	rotYINT=setInterval("startYRotate()",10)
// }
// function startRotate()
// {
// 	n=n+1
// 	x.style.transform="rotate(" + n + "deg)"
// 	x.style.webkitTransform="rotate(" + n + "deg)"
// 	x.style.OTransform="rotate(" + n + "deg)"
// 	x.style.MozTransform="rotate(" + n + "deg)"
// 	if (n==180 || n==360)
// 	{
// 		clearInterval(rotINT)
// 		if (n==360){n=0}
// 	}
// }
// function startYRotate()
// {
// 	ny=ny+1
// 	y.style.transform="rotateY(" + ny + "deg)"
// 	y.style.webkitTransform="rotateY(" + ny + "deg)"
// 	y.style.OTransform="rotateY(" + ny + "deg)"
// 	y.style.MozTransform="rotateY(" + ny + "deg)"
// 	if (ny==180 || ny>=360)
// 	{
// 		clearInterval(rotYINT)
// 		if (ny>=360){ny=0}
// 	}
// }
function setV(){
	
	var errormsg="";
	if($("#file").val()<1){errormsg+="未選擇檔案<br>";}
	if($( "input:checked[name='ec-radio-group']" ).length==0){errormsg+="未選擇平台<br>";}
	if(errormsg.length>2){
		$("#message").html(errormsg);
		$("#message").dialog("open");
		return false;
	}
	var tmp = $("#file").val().split(".");
	if($("#submitbtn").attr("restrict").length >0 && tmp[1]!=$("#submitbtn").attr("restrict")){
		errormsg+="檔案格式不符合<br>";
		if($("#submitbtn").attr("restrict")!=null){
			errormsg+=($("#submitbtn").attr("restrict").length>2?("<div style='font-size:18px;'>您選擇的平台格式只接受&nbsp;"+$("#submitbtn").attr("restrict")+"&nbsp;檔案</div>"):"");
		}
		$("#message").html(errormsg);
		$("#message").dialog("open");
		return false;
	}
// 	$("#submitbtn").attr("restrict");
// 	var i=0;
// 	while(!document.getElementsByName("ec-radio-group")[i].checked){
// 		i++;
// 		if(i==20){alert("請選擇平台");return false;}
// 	}
	document.getElementById("form1").action="upload.do?vender="+$( "input:checked[name='ec-radio-group']" ).val()+"&ordertype="+$( "input:checked[name='ec-radio-group']" ).attr("value2");
	
	$("#msg").fadeIn();
	setTimeout( function() {$("#msg").fadeOut();}, 2600);
	
	return true;
};

var getAbsPos = function(o) {
    var pos = {x:0, y:0};
    while (o!=null)
    {
    pos.x += o.offsetLeft;
    pos.y += o.offsetTop;
    o = o.offsetParent; //若區塊外還有父元素，就繼續往外推
    }
    return pos;
}
var POS = getAbsPos(document.getElementById("demo")).x;
console.log("top: "+POS.y, "left: "+ POS.x);


</script>
</body>
</html>