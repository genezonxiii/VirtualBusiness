<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>團購轉檔作業</title>
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
	<div class="content-wrap">
		<div class='bdyplane' style="opacity:0">
<!-- 	<h2 class="page-title">團購轉檔作業</h2> -->
		<div class="search-result-wrap" style="margin-bottom: 0px;">
			<div class="result-table-func-wrap" id='panel'>

<!-- 				<div class="ec-radio-group-wrap"> -->
<!-- 					<input id="radio-01" type="radio" name="ec-radio-group" value="pcone"><label for="radio-01"><img src="images/ec-logos/pcone.png" alt=""><span>松果購物</span></label> -->
<!-- 					<input id="radio-02" type="radio" name="ec-radio-group" value="popular"><label for="radio-02"><img src="images/ec-logos/popular.png" alt=""><span>小P大團購</span></label> -->
<!-- 					<input id="radio-03" type="radio" name="ec-radio-group" value="chinatime"><label for="radio-03"><img src="images/ec-logos/chinatime.png" alt=""><span>中時團購網</span></label> -->
<!-- 					<input id="radio-04" type="radio" name="ec-radio-group" value="sister"><label for="radio-04"><img src="images/ec-logos/sister.png" alt=""><span>姊妹購物網</span></label> -->
<!-- 					<input id="radio-05" type="radio" name="ec-radio-group" value="lifemarket"><label for="radio-05"><img src="images/ec-logos/lifemarket.png" alt=""><span>生活市集</span></label> -->
<!-- 				</div>/.ec-radio-group-wrap -->

<!-- 				<div class="ec-radio-group-wrap"> -->
<!-- 					<input id="radio-06" type="radio" name="ec-radio-group" value="healthmike"><label for="radio-06"><img src="images/ec-logos/healthmike.png" alt=""><span>健康賣客</span></label> -->
<!-- 					<input id="radio-07" type="radio" name="ec-radio-group" value="delicious"><label for="radio-07"><img src="images/ec-logos/delicious.png" alt=""><span>好吃宅配網</span></label> -->
<!-- 					<input id="radio-08" type="radio" name="ec-radio-group" value="chinaugo"><label for="radio-08"><img src="images/ec-logos/chinaugo.png" alt=""><span>中華優購</span></label> -->
<!-- 					<input id="radio-09" type="radio" name="ec-radio-group" value="ibon"><label for="radio-09"><img src="images/ec-logos/ibon.png" alt=""><span>IBON</span></label> -->
<!-- 					<input id="radio-10" type="radio" name="ec-radio-group" value="17life"><label for="radio-10"><img src="images/ec-logos/Life.png" alt=""><span>17Life</span></label> -->
<!-- 				</div>/.ec-radio-group-wrap -->
<!-- 				<div class="ec-radio-group-wrap"> -->
<!-- 					<input id="radio-11" type="radio" name="ec-radio-group" value="herbuy"><label for="radio-11"><img src="images/ec-logos/herbuy.png" alt=""><span>好買寶貝</span></label> -->
<!-- 					<input id="radio-12" type="radio" name="ec-radio-group" value="gomaji"><label for="radio-12"><img src="images/ec-logos/GOMAJI.png" alt=""><span>Gomaji</span></label> -->
<!-- 					<input id="radio-13" type="radio" name="ec-radio-group" value="shopline"><label for="radio-13"><img src="images/ec-logos/shopline.png" alt=""><span>Shopline</span></label> -->
<!-- 					<input id="radio-14" type="radio" name="ec-radio-group" value="charmingmall"><label for="radio-14"><img src="images/ec-logos/charmingmall.png" alt=""><span>俏美魔</span></label> -->
<!-- 					<input id="radio-15" type="radio" name="ec-radio-group" value="mintyday"><label for="radio-15"><img src="images/ec-logos/mintyday.png" alt=""><span>清新好日</span></label> -->
<!-- 				</div>/.ec-radio-group-wrap -->
				
				<hr class="hr-gray">
			</div><!-- /.result-table-func-wrap -->
			<div style="text-align:center; margin:0px auto;font-size:35px;"></div>
				<form action="upload.do" id="form1" method="post" enctype="multipart/form-data" style="margin:0px;">
					<input type="file" id="file" name="file" accept=".csv,.pdf,.xls,.xlsx" style="opacity:0;position:absolute;margin:6px;max-width:353px; " onmouseout='$("#upload_name").css("background-image","url(images/browse.png)");' onmouseover='$("#upload_name").css("background-image","url(images/browseon.png)");' />
<!-- 					<div style="opacity:0.6;position:absolute;padding-top:7px;padding-left:5px;"><button id="btn" onclick="return false;">瀏覽...</button></div> -->
					<input type="text" id="upload_name" size="40" style='z-index:-1;padding-left:70px;background-color:#EEF3F9;background-image:url(images/browse.png);background-repeat: no-repeat;'  />
					
					&nbsp;<select id='deliver-type'>
						<option value="0">請選擇轉出格式</option>
<!-- 						<option value="1">中華郵政</option> -->
						<option value="2">黑貓宅急便</option>
<!-- 						<option value="3">台灣宅配通</option> -->
<!-- 						<option value="4">新竹物流</option> -->
<!-- 						<option value="5">嘉里大榮物流（大榮貨運）</option> -->
<!-- 						<option value="6">便利帶</option> -->
					</select>
					&nbsp;<select id='product-code'>
						<option value="0">請選擇產品代碼</option>
						<option value="O">LP28敏立清益生菌(原味)</option>
						<option value="A">LP28敏立清益生菌(紅蘋果)</option>
						<option value="M">LP28敏立清益生菌(蔓越莓)</option>
						<option value="G">LP28敏立清益生菌(青蘋果)</option>
						<option value="D">LP28敏立清益生菌(草莓)</option>
						<option value="OG">金敏立清-多多原味</option>
						<option value="MG">金敏立清-蔓越莓</option>
						<option value="OS">欣敏立清多多原味</option>
						<option value="GS">欣敏立清青蘋果多多</option>
						<option value="AS">欣敏立清紅蘋果多多</option>
						<option value="MS">欣敏立清蔓越莓多多</option>
						<option value="DS">欣敏立清草莓多多</option>
						<option value="Y">視立清-葉黃素</option>
						<option value="ME">美立妍</option>
						<option value="BW">B立威</option>
						<option value="UBG">優必固U!be Good</option>
						<option value="UW">優必威U!be Well</option>
						<option value="UST">優比獅壯U!be Strong</option>
						<option value="US">口腔益生菌U!be Smile</option>
						<option value="USK">優比思邁U!be Smile Kid</option>
						<option value="YK">小兒晶</option>
						<option value="GK">小兒立</option>
						<option value="LM">樂敏立清</option>
					</select>
					<br><br>
					<input type="submit" id="submitbtn" restrict="" onclick="return setV();" value="檔案轉換" class="btn btn-exec btn-wide" style="color: #fff;margin-left:20px"/>
					&nbsp;&nbsp;&nbsp;&nbsp;<span id='download' ></span>
					
					
					<div id="msg" style="color:red;margin:5px;display:none;">請稍候片刻...</div>
				</form>
			</div>
		</div><!-- /.search-result-wrap -->
	</div><!-- /.content-wrap -->
	<div id ="message" title="訊息" style='padding:30px 0;text-align:center;font-size:30px;font-family: "微軟正黑體", "Microsoft JhengHei", Arial, Helvetica, sans-serif, \5FAE\8EDF\6B63\9ED1\9AD4,\65B0\7D30\660E\9AD4;'></div>
	<div id ="choose-order-type" title="選擇訂單類型"></div>
<script>
var platform_dialog={};
$(function(){
	$('.bdyplane').animate({'opacity':'1'});
	$.ajax({
		type : "POST",
		url : "groupbuying.do",
		data : {action :"select_platform_kind" },
		success : function(result) {
// 			alert(result);
			var json_obj = $.parseJSON(result);
			var platforms = '<div class="ec-radio-group-wrap">';
			$.each (json_obj, function (i) {
				var tmp = json_obj[i].throwfile_platform;
				
				$.ajax({
					type : "POST",
					url : "groupbuying.do",
// 					async : false,
					data : {action :"select_way_of_platform", platform : tmp },
					success : function(result) {
						var json_obj = $.parseJSON(result);
						var ordertype = "",dialogvalue="";
						$.each (json_obj, function (i) {
							if(json_obj[i].throwfile_type!="general"){
								ordertype+="<input id='type-radio-"+i+"' type='radio' name='ordertype' value='"+json_obj[i].throwfile_type+"' restrict='"+json_obj[i].throwfile_fileextension+"'><label for='type-radio-"+i+"'><span class='form-label'>"+json_obj[i].throwfile_name+"</span></label>";
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
				platforms+= json_obj[i].throwfile_platform+'"><label for="radio-'+i+'"><img src="images/';
				platforms+= ((json_obj[i].icon.length>1) ? json_obj[i].icon : "ec-logos/noname.png");
				platforms+= '"alt=""><span>'+json_obj[i].memo+'</span></label>';
			});
			platforms += "</div><hr class='hr-gray'>";
			$("#panel").html(platforms);
		}
	});
	
	
	$('#form1').ajaxForm(function(result) {
		
		if(result=="false"){
			$("#download").html("");
			$("#message").html("轉檔失敗<br><div style='font-size:18px;'>請確認檔案的格式是否遭到修改。</div>");
	 		$("#message").dialog("open");
		}else{
			$("#download").append("&nbsp;&nbsp;&nbsp;<a href='./fileoutput.do?fileforgroupbuy="+result+"'class='btn btn-primary'>檔案下載</a>");
		}
		
		$("#submitbtn").attr("onclick","return setV();");
		$("#submitbtn").addClass("btn-exec");
	    $("#submitbtn").removeClass("btn-gray");
			
// 		var str = result;
// 		console.log(str);
// 		$("#message").html(str);
// 		$("#message").dialog("open");

    });
	
	$("#file").change(function(){
		$("#upload_name").val($("#file").val());
	});
	$("#upload_name").click(function(){
		$("#file").trigger("click");
	});
	$('#panel').delegate('label','mouseenter mouseleave', function( event ) {
	    if( event.type === 'mouseenter' ){  
	    	$(this).addClass("on_it");
		}else{
	    	$(this).removeClass("on_it");
	    }
	});
	$("#panel").delegate(":radio[name='ec-radio-group']", "click", function(e) {
		$("#choose-order-type").html(platform_dialog[$(this).val()]);
		if(platform_dialog[$(this).val()].length>30){
			$("#choose-order-type").dialog("open");
		}else{
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
						$( "input:checked[name='ec-radio-group']" ).attr("value2",$( "input:checked[name='ordertype']" ).val());
						$("#submitbtn").attr("restrict",$( "input:checked[name='ordertype']" ).attr("restrict"));
						$("#choose-order-type").dialog("close");
					}else{
						$("#message").html("請選擇訂單類型!");
						$("#message").dialog("open");
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
function setV(){
	var errormsg="";
	if($("#file").val()<1){errormsg+="未選擇檔案<br>";}
	if($( "input:checked[name='ec-radio-group']" ).length==0){errormsg+="未選擇平台<br>";}
	if($("#deliver-type").val()==0){errormsg+="未選擇欲轉出格式<br>";}
	if($("#product-code").val()==0){errormsg+="未選擇產品代碼<br>";}
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
	
	
	document.getElementById("form1").action = "groupbuying.do"
			+"?vender="+$( "input:checked[name='ec-radio-group']" ).val()
			+"&ordertype="+$( "input:checked[name='ec-radio-group']" ).attr("value2")
			+"&delivertype="+$( "#deliver-type").val()
			+"&productcode="+$( "#product-code" ).val();
	
	$("#submitbtn").attr("onclick","return false;");
	$("#submitbtn").removeClass("btn-exec");
	$("#submitbtn").addClass("btn-gray");
	
	setTimeout( function() {$("#download").html("&nbsp;=");}, 300);
	setTimeout( function() {$("#download").append("&nbsp;=");}, 600);
	setTimeout( function() {$("#download").append("&nbsp;=");}, 900);
	setTimeout( function() {$("#download").append("&nbsp;>");}, 1200);
	
	return true;
};




</script>
</body>
</html>