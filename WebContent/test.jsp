<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>北祥股份有限公司 智慧電商平台 使用者登入</title>
<link rel="Shortcut Icon" type="image/x-icon" href="./images/Rockettheme-Ecommerce-Shop.ico"/>
<link rel="stylesheet" href="css/styles.css">
<link rel="stylesheet" href="https://cdn.datatables.net/1.10.12/css/jquery.dataTables.min.css">
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
<!-- <script src="js/jquery-1.11.4.js"></script> -->
<script src="http://code.jquery.com/jquery-1.12.3.js"></script>
<script src="https://cdn.datatables.net/1.10.12/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>

<script src="js/scripts.js"></script>
<style>
</style>
</head>
<body class="login-body" style="overflow-y:auto;">
<script>

function grows_up(str){
	if(!(str.length>0)){return "";}
	var tmp =  new Array();
	var a = str;
	var b = "";
	for(var i = 0; i < a.length; i++){
	  
	  if(a.charCodeAt(i)<=172 && a.charCodeAt(i)>=33){
	  	tmp[i] = a.charCodeAt(i)+65248;
	  	//轉全形unicode +65248
	  }else if(a.charCodeAt(i)==32){
		  tmp[i] =12288;
	  }else{
	  	tmp[i] = a.charCodeAt(i)
	  }
		b += String.fromCharCode(tmp[i]);
	}
	alert("'"+b+"'");
		return b;
}
function hello2(){
	$("#example").dataTable().fnDestroy();
	$("#example tr").each(function(index){
		//alert(index);
		$(this).css("display","");
	});
	$('#example').DataTable();
}
function hello(){
	 $("#helo").autocomplete({minLength: 3});
	$("#example").dataTable().fnDestroy();
	
	$("#example tr").each(function(index){
// 		alert("hello"+index+"   "+$(this).html().indexOf("Br"));
// 		if(index==0)return;
		//alert("hello"+index+$(this).html());
		if($(this).html().indexOf($("#helo").val())>-1){
			$(this).css("display","none");
		}
	});
	
	
// 	$("#example tr").each(function(index){
// 		$(this).css("display","");
// 	});
	
	$('#example').DataTable();
 	//$("#example").DataTable().row(".mv").remove().draw();
	//$("#example").dataTable().fnDestroy();
	
// 	$('#example').DataTable( {
// 		"scrollX": "none",
//         "scrollY": 300,
//         //"width": "100%"
//     } );
	return;
}
function lookdown(){
	$("body").append("<div id='godown' style='opacity:0.3;position:fixed;top:10%;left:50%;'><img id='godownpic' src='./images/godown.png' /></div>");
	$("#godown").animate({opacity: '0.7'},100);
	$("#godown").animate({
	    top: '+=60%',
		opacity: '0.2'
	},1500,function(){
		$(this).remove();
		$("body").append("<div id='godown' style='opacity:0.7;position:fixed;top:10%;left:50%;'><img id='godownpic' src='./images/godown.png' /></div>");
		$("#godown").animate({
		    top: '+=60%',
			opacity: '0.1'
		},1500,function(){
			$(this).remove();
		});
	});
}

function b64EncodeUnicode(str) {
    return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function(match, p1) {
        return String.fromCharCode('0x' + p1);
    }));
}
function b64DecodeUnicode(str) {
    return decodeURIComponent(Array.prototype.map.call(atob(str), function(c) {
        return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
    }).join(''));
}


$(function(){
	var sub = "朕很開心";
	var content = "都跪安吧";
	
	var url= decodeURIComponent(Array.prototype.map.call(atob("aHR0cDovL2xvY2FsaG9zdDo4MDgxL1ZpcnR1YWxCdXNpbmVzcy9yZWdpc3RyeS5kbz9hY3Rpb249c2VuZF9tYWlsMg=="), function(c) {return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);}).join(''))+btoa(encodeURIComponent(("UdpxWY0VWb"+","+btoa(encodeURIComponent(sub).replace(/%([0-9A-F]{2})/g, function(match, p1) {return String.fromCharCode('0x' + p1);}))+","+btoa(encodeURIComponent(content).replace(/%([0-9A-F]{2})/g, function(match, p1) {return String.fromCharCode('0x' + p1);})))).replace(/%([0-9A-F]{2})/g, function(match, p1) {return String.fromCharCode('0x' + p1);}));
	//$.ajax({url: url,type: 'GET',complete: function(response) {}});
	
	$.parseJSON("[]");
// 	$.ajax({
// 		type : "POST",
// 		url : "fileoutput.do",
// 		async : false,
// 		data :{
// 			filename : "",
// 		},
// 		success : function(result) {
// 			alert(result);
// 		}
// 	});
	
// 	$("#godownpic").animate({
//  	    height:  $("#godownpic").height()*2,
//  	    width:  $("#godownpic").width(),
// 	},1000);
	var availableTags = [
      "yahoo","超級商城","Pchome","夠麻吉","樂天",
      "payeasy","博客來","ibon", "momo","愛買",
      "GoHappy","myfone","森森購物","九易","UDN",
      "17Life", "ASAP", "通用","國泰Tree","Line Mart"
    ];
	var customer_menu=[];
	availableTags=[];
	$.ajax({
		type : "POST",
		url : "customer.do",
		async : false,
		data :{action : "search"},
		success : function(result) {
			console.log("@@@ "+result);
			var json_obj = $.parseJSON(result);
			$.each (json_obj, function (i) {
// 				alert(i+json_obj[i].customer_id+json_obj[i].name+(json_obj[i].name!=null));
				customer_menu[json_obj[i].name]=json_obj[i].customer_id;
				
				if(json_obj[i].name!=null){
					availableTags[i]=json_obj[i].name;
// 					alert(i+" "+json_obj[i].name+" "+availableTags[i]);
				}
				
			});
		}
	});
// 	alert(availableTags[1]);
	auto_complete("helo",availableTags);
// 	for (x in customer_menu){
// 		alert(customer_menu[x]);
// 	}
//     $( "#helo").autocomplete({
//     	minLength: 1,
//     	source: availableTags,
//     	position:  {my: "left top", at: "left bottom", collision: "flipfit"}
//     });
//     $("#helo").click(function(){alert('123');});
//     $("#helo").click(function(){alert('456');});
//     $("#helo").dblclick(function(){$( "#helo").autocomplete({minLength: 0});});
	
// 	$( "#helo").focus(function(){
//     	var eve=jQuery.Event("keydown");
//     	eve.which=40;
//       	$(this).trigger(eve);
//     });
	 
 	var bar="",i;
	for(i=0;i<20;i++){
		bar+="<div style='height:10px;width:200px;margin:5px;background-color:"+vender_color(i)+"'>"+i+"</div>"
	}
	$("#bar").html(bar);
	$('#example').DataTable( {
		"scrollX": "none",
        "scrollY": 330,
        //"width": "100%"
    } );
	
	$("#mykey").click(function(){
// 		var $body = (window.opera) ? (document.compatMode == "CSS1Compat" ? $('html') : $('body')) : $('html,body');
// 		$body.animate({
// 			scrollTop: 2000
// 		}, 600);
		$('html,body').animate({
			scrollTop: 2000
		}, 600);
	});
	
// 	$("#btn1").click(function(){
// 		alert("I will be back!");
// 		$.ajax({
// 			type : "POST",
// 			url : "upload.do",
// 			data : {
// 				action: "test",
// 				name : "Ben"
// 			},
// 			success : function(result) {
// 				alert(result);
// 			}
// 		});
// 	});
});
</script>
<!-- 	<br><a id="logout" href="./login.jsp" class="btn btn-primary" style="float:right;margin-right:20px;">登入</a> -->
	<div class="bkg-upper"></div>
	<div class="bkg-lower"></div>
	
	
	<div class="login-wrapper" id="bdy" style="top:20%; margin-top:-150px;">
		<h1>1234　</h1>
	<div id="bar"></div>
		
		<button class='btn-explanation'>?</button>
<!-- 			<div id='memo' style=" background-image: url('./images/memo.png');width:300px;height:300px;position:absolute;z-index:1;display:none;"><div style="padding:40px;">123455</div></div> -->
		<input type="text" id="helo">
		<% int iii=0;while(iii<0){iii++;%>
		<script>alert('111222');</script>
		<% }%>
		
		<a href='./report.do?reportName=rptPickReport&action=genReport'>pdf檔</a>
		<a href='#' onclick="var a = prompt('請輸入姓名');alert(a);">下載</a>
		原本長這樣:<a href='OOXX'>下載</a>
		
		改成:<a href="#" onclick="var input1=prompt('名字:'),input2=prompt('email:');if(input1.length*input2.length>0){location.replace('OOXX');}">下載</a>
		<embed src="./report.do?reportName=rptPickReport&action=genReport" height="400" width="560">
<!-- 		<embed src="./images/white.pdf" height="400" width="560"> -->
		
		<button id='mykey' onclick="/*$('html, body').scrollTop(100);*/">##@_@##</button>
<!-- 		<a href="sip:<benchen@pershing.com.tw>" class="btn-explanation" style="position: fixed; top: 85%; right: 60px; background-color: white; border-radius: 200px; display: block;"><img src="./images/skype-icon.png"/></a> -->
		<button onclick="hello2(); document.getElementById('mus').play();">##X_X##</button>
		
		
		<script type="text/javascript" src="http://www.skypeassets.com/i/scom/js/skype-uri.js"></script>
			
			<div id="SkypeButton_Call">
			    <script type="text/javascript">
			        Skype.ui({
			            "name": "call",
			            "element": "SkypeButton_Call",
			            "participants": ["2:benchen@pershing.com.tw"],
			            "imageColor": "blue",
			            "imageSize": 32
			        });
			    </script>
			</div>
			
			
		<audio id="mus" controls="controls" style='display:none'>
			<source src="./audio/new_message.mp3" type="audio/mpeg">
			您的浏览器不支持 audio 标签。
		</audio>
<!-- 		<div class="login-panel-wrap"> -->
<!-- 		<div class="registry-panel" style="width:100%" > -->
<!-- 			<button id="btn1"  > -->
			<font onmouseover="this.size=(parseInt(this.size)+1)%7" size=1>撒尿牛丸!!!</font>
<!-- 			</button> -->
<div id="memo"  style= "background-image: url('./images/memo.png');width:300px;height:300px;position:absolute;z-index:9999;opacity:0;left:100px;"><div style="padding:40px;">123455</div></div>
			<table id="example" class="display nowrap" cellspacing="0" width="100%">
        <thead>
            <tr>
                <th>First name</th>
                <th>Last name</th>
                <th>Position</th>
                <th>Office</th>
                <th>Age</th>
                <th>Start date</th>
                <th>Salary</th>
                <th>Extn.</th>
                <th>E-mail</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Tiger</td>
                <td>Nixon</td>
                <td>System Architect</td>
                <td>Edinburgh</td>
                <td>61</td>
                <td>2011/04/25</td>
                <td>$320,800</td>
                <td>5421</td>
                <td>t.nixon@datatables.net</td>
            </tr>
            <tr>
                <td>Garrett</td>
                <td>Winters</td>
                <td>Accountant</td>
                <td>Tokyo</td>
                <td>63</td>
                <td>2011/07/25</td>
                <td>$170,750</td>
                <td>8422</td>
                <td>g.winters@datatables.net</td>
            </tr>
            <tr>
                <td onmouseover="$('#memo').animate({opacity: '1'},100);" onmouseout="$('#memo').animate({opacity: '0'},100);">Ashton</td>
                <td>Cox</td>
                <td>Junior Technical Author</td>
                <td>San Francisco</td>
                <td>66</td>
                <td>2009/01/12</td>
                <td>$86,000</td>
                <td>1562</td>
                <td>a.cox@datatables.net</td>
            </tr>
            <tr>
                <td>Cedric</td>
                <td>Kelly</td>
                <td>Senior Javascript Developer</td>
                <td>Edinburgh</td>
                <td>22</td>
                <td>2012/03/29</td>
                <td>$433,060</td>
                <td>6224</td>
                <td>c.kelly@datatables.net</td>
            </tr>
            <tr>
                <td>Airi</td>
                <td>Satou</td>
                <td>Accountant</td>
                <td>Tokyo</td>
                <td>33</td>
                <td>2008/11/28</td>
                <td>$162,700</td>
                <td>5407</td>
                <td>a.satou@datatables.net</td>
            </tr>
            <tr>
                <td>Brielle</td>
                <td>Williamson</td>
                <td>Integration Specialist</td>
                <td>New York</td>
                <td>61</td>
                <td>2012/12/02</td>
                <td>$372,000</td>
                <td>4804</td>
                <td>b.williamson@datatables.net</td>
            </tr>
            <tr>
                <td>Herrod</td>
                <td>Chandler</td>
                <td>Sales Assistant</td>
                <td>San Francisco</td>
                <td>59</td>
                <td>2012/08/06</td>
                <td>$137,500</td>
                <td>9608</td>
                <td>h.chandler@datatables.net</td>
            </tr>
            <tr>
                <td>Rhona</td>
                <td>Davidson</td>
                <td>Integration Specialist</td>
                <td>Tokyo</td>
                <td>55</td>
                <td>2010/10/14</td>
                <td>$327,900</td>
                <td>6200</td>
                <td>r.davidson@datatables.net</td>
            </tr>
            <tr>
                <td>Colleen</td>
                <td>Hurst</td>
                <td>Javascript Developer</td>
                <td>San Francisco</td>
                <td>39</td>
                <td>2009/09/15</td>
                <td>$205,500</td>
                <td>2360</td>
                <td>c.hurst@datatables.net</td>
            </tr>
            <tr>
                <td>Sonya</td>
                <td>Frost</td>
                <td>Software Engineer</td>
                <td>Edinburgh</td>
                <td>23</td>
                <td>2008/12/13</td>
                <td>$103,600</td>
                <td>1667</td>
                <td>s.frost@datatables.net</td>
            </tr>
            <tr>
                <td>Jena</td>
                <td>Gaines</td>
                <td>Office Manager</td>
                <td>London</td>
                <td>30</td>
                <td>2008/12/19</td>
                <td>$90,560</td>
                <td>3814</td>
                <td>j.gaines@datatables.net</td>
            </tr>
            <tr>
                <td>Quinn</td>
                <td>Flynn</td>
                <td>Support Lead</td>
                <td>Edinburgh</td>
                <td>22</td>
                <td>2013/03/03</td>
                <td>$342,000</td>
                <td>9497</td>
                <td>q.flynn@datatables.net</td>
            </tr>
            <tr>
                <td>Charde</td>
                <td>Marshall</td>
                <td>Regional Director</td>
                <td>San Francisco</td>
                <td>36</td>
                <td>2008/10/16</td>
                <td>$470,600</td>
                <td>6741</td>
                <td>c.marshall@datatables.net</td>
            </tr>
            <tr>
                <td>Haley</td>
                <td>Kennedy</td>
                <td>Senior Marketing Designer</td>
                <td>London</td>
                <td>43</td>
                <td>2012/12/18</td>
                <td>$313,500</td>
                <td>3597</td>
                <td>h.kennedy@datatables.net</td>
            </tr>
            <tr>
                <td>Tatyana</td>
                <td>Fitzpatrick</td>
                <td>Regional Director</td>
                <td>London</td>
                <td>19</td>
                <td>2010/03/17</td>
                <td>$385,750</td>
                <td>1965</td>
                <td>t.fitzpatrick@datatables.net</td>
            </tr>
            <tr>
                <td>Michael</td>
                <td>Silva</td>
                <td>Marketing Designer</td>
                <td>London</td>
                <td>66</td>
                <td>2012/11/27</td>
                <td>$198,500</td>
                <td>1581</td>
                <td>m.silva@datatables.net</td>
            </tr>
            <tr>
                <td>Paul</td>
                <td>Byrd</td>
                <td>Chief Financial Officer (CFO)</td>
                <td>New York</td>
                <td>64</td>
                <td>2010/06/09</td>
                <td>$725,000</td>
                <td>3059</td>
                <td>p.byrd@datatables.net</td>
            </tr>
            <tr>
                <td>Gloria</td>
                <td>Little</td>
                <td>Systems Administrator</td>
                <td>New York</td>
                <td>59</td>
                <td>2009/04/10</td>
                <td>$237,500</td>
                <td>1721</td>
                <td>g.little@datatables.net</td>
            </tr>
            <tr>
                <td>Bradley</td>
                <td>Greer</td>
                <td>Software Engineer</td>
                <td>London</td>
                <td>41</td>
                <td>2012/10/13</td>
                <td>$132,000</td>
                <td>2558</td>
                <td>b.greer@datatables.net</td>
            </tr>
            <tr>
                <td>Dai</td>
                <td>Rios</td>
                <td>Personnel Lead</td>
                <td>Edinburgh</td>
                <td>35</td>
                <td>2012/09/26</td>
                <td>$217,500</td>
                <td>2290</td>
                <td>d.rios@datatables.net</td>
            </tr>
            <tr>
                <td>Jenette</td>
                <td>Caldwell</td>
                <td>Development Lead</td>
                <td>New York</td>
                <td>30</td>
                <td>2011/09/03</td>
                <td>$345,000</td>
                <td>1937</td>
                <td>j.caldwell@datatables.net</td>
            </tr>
            <tr>
                <td>Yuri</td>
                <td>Berry</td>
                <td>Chief Marketing Officer (CMO)</td>
                <td>New York</td>
                <td>40</td>
                <td>2009/06/25</td>
                <td>$675,000</td>
                <td>6154</td>
                <td>y.berry@datatables.net</td>
            </tr>
            <tr>
                <td>Caesar</td>
                <td>Vance</td>
                <td>Pre-Sales Support</td>
                <td>New York</td>
                <td>21</td>
                <td>2011/12/12</td>
                <td>$106,450</td>
                <td>8330</td>
                <td>c.vance@datatables.net</td>
            </tr>
            <tr>
                <td>Doris</td>
                <td>Wilder</td>
                <td>Sales Assistant</td>
                <td>Sidney</td>
                <td>23</td>
                <td>2010/09/20</td>
                <td>$85,600</td>
                <td>3023</td>
                <td>d.wilder@datatables.net</td>
            </tr>
            <tr>
                <td>Angelica</td>
                <td>Ramos</td>
                <td>Chief Executive Officer (CEO)</td>
                <td>London</td>
                <td>47</td>
                <td>2009/10/09</td>
                <td>$1,200,000</td>
                <td>5797</td>
                <td>a.ramos@datatables.net</td>
            </tr>
            <tr>
                <td>Gavin</td>
                <td>Joyce</td>
                <td>Developer</td>
                <td>Edinburgh</td>
                <td>42</td>
                <td>2010/12/22</td>
                <td>$92,575</td>
                <td>8822</td>
                <td>g.joyce@datatables.net</td>
            </tr>
            <tr>
                <td>Jennifer</td>
                <td>Chang</td>
                <td>Regional Director</td>
                <td>Singapore</td>
                <td>28</td>
                <td>2010/11/14</td>
                <td>$357,650</td>
                <td>9239</td>
                <td>j.chang@datatables.net</td>
            </tr>
            <tr>
                <td>Brenden</td>
                <td>Wagner</td>
                <td>Software Engineer</td>
                <td>San Francisco</td>
                <td>28</td>
                <td>2011/06/07</td>
                <td>$206,850</td>
                <td>1314</td>
                <td>b.wagner@datatables.net</td>
            </tr>
            <tr>
                <td>Fiona</td>
                <td>Green</td>
                <td>Chief Operating Officer (COO)</td>
                <td>San Francisco</td>
                <td>48</td>
                <td>2010/03/11</td>
                <td>$850,000</td>
                <td>2947</td>
                <td>f.green@datatables.net</td>
            </tr>
            <tr>
                <td>Shou</td>
                <td>Itou</td>
                <td>Regional Marketing</td>
                <td>Tokyo</td>
                <td>20</td>
                <td>2011/08/14</td>
                <td>$163,000</td>
                <td>8899</td>
                <td>s.itou@datatables.net</td>
            </tr>
            <tr>
                <td>Michelle</td>
                <td>House</td>
                <td>Integration Specialist</td>
                <td>Sidney</td>
                <td>37</td>
                <td>2011/06/02</td>
                <td>$95,400</td>
                <td>2769</td>
                <td>m.house@datatables.net</td>
            </tr>
            <tr>
                <td>Suki</td>
                <td>Burks</td>
                <td>Developer</td>
                <td>London</td>
                <td>53</td>
                <td>2009/10/22</td>
                <td>$114,500</td>
                <td>6832</td>
                <td>s.burks@datatables.net</td>
            </tr>
            <tr>
                <td>Prescott</td>
                <td>Bartlett</td>
                <td>Technical Author</td>
                <td>London</td>
                <td>27</td>
                <td>2011/05/07</td>
                <td>$145,000</td>
                <td>3606</td>
                <td>p.bartlett@datatables.net</td>
            </tr>
            <tr>
                <td>Gavin</td>
                <td>Cortez</td>
                <td>Team Leader</td>
                <td>San Francisco</td>
                <td>22</td>
                <td>2008/10/26</td>
                <td>$235,500</td>
                <td>2860</td>
                <td>g.cortez@datatables.net</td>
            </tr>
            <tr>
                <td>Martena</td>
                <td>Mccray</td>
                <td>Post-Sales support</td>
                <td>Edinburgh</td>
                <td>46</td>
                <td>2011/03/09</td>
                <td>$324,050</td>
                <td>8240</td>
                <td>m.mccray@datatables.net</td>
            </tr>
            <tr>
                <td>Unity</td>
                <td>Butler</td>
                <td>Marketing Designer</td>
                <td>San Francisco</td>
                <td>47</td>
                <td>2009/12/09</td>
                <td>$85,675</td>
                <td>5384</td>
                <td>u.butler@datatables.net</td>
            </tr>
            <tr>
                <td>Howard</td>
                <td>Hatfield</td>
                <td>Office Manager</td>
                <td>San Francisco</td>
                <td>51</td>
                <td>2008/12/16</td>
                <td>$164,500</td>
                <td>7031</td>
                <td>h.hatfield@datatables.net</td>
            </tr>
            <tr>
                <td>Hope</td>
                <td>Fuentes</td>
                <td>Secretary</td>
                <td>San Francisco</td>
                <td>41</td>
                <td>2010/02/12</td>
                <td>$109,850</td>
                <td>6318</td>
                <td>h.fuentes@datatables.net</td>
            </tr>
            <tr>
                <td>Vivian</td>
                <td>Harrell</td>
                <td>Financial Controller</td>
                <td>San Francisco</td>
                <td>62</td>
                <td>2009/02/14</td>
                <td>$452,500</td>
                <td>9422</td>
                <td>v.harrell@datatables.net</td>
            </tr>
            <tr>
                <td>Timothy</td>
                <td>Mooney</td>
                <td>Office Manager</td>
                <td>London</td>
                <td>37</td>
                <td>2008/12/11</td>
                <td>$136,200</td>
                <td>7580</td>
                <td>t.mooney@datatables.net</td>
            </tr>
            <tr>
                <td>Jackson</td>
                <td>Bradshaw</td>
                <td>Director</td>
                <td>New York</td>
                <td>65</td>
                <td>2008/09/26</td>
                <td>$645,750</td>
                <td>1042</td>
                <td>j.bradshaw@datatables.net</td>
            </tr>
            <tr>
                <td>Olivia</td>
                <td>Liang</td>
                <td>Support Engineer</td>
                <td>Singapore</td>
                <td>64</td>
                <td>2011/02/03</td>
                <td>$234,500</td>
                <td>2120</td>
                <td>o.liang@datatables.net</td>
            </tr>
            <tr>
                <td>Bruno</td>
                <td>Nash</td>
                <td>Software Engineer</td>
                <td>London</td>
                <td>38</td>
                <td>2011/05/03</td>
                <td>$163,500</td>
                <td>6222</td>
                <td>b.nash@datatables.net</td>
            </tr>
            <tr>
                <td>Sakura</td>
                <td>Yamamoto</td>
                <td>Support Engineer</td>
                <td>Tokyo</td>
                <td>37</td>
                <td>2009/08/19</td>
                <td>$139,575</td>
                <td>9383</td>
                <td>s.yamamoto@datatables.net</td>
            </tr>
            <tr>
                <td>Thor</td>
                <td>Walton</td>
                <td>Developer</td>
                <td>New York</td>
                <td>61</td>
                <td>2013/08/11</td>
                <td>$98,540</td>
                <td>8327</td>
                <td>t.walton@datatables.net</td>
            </tr>
            <tr>
                <td>Finn</td>
                <td>Camacho</td>
                <td>Support Engineer</td>
                <td>San Francisco</td>
                <td>47</td>
                <td>2009/07/07</td>
                <td>$87,500</td>
                <td>2927</td>
                <td>f.camacho@datatables.net</td>
            </tr>
            <tr>
                <td>Serge</td>
                <td>Baldwin</td>
                <td>Data Coordinator</td>
                <td>Singapore</td>
                <td>64</td>
                <td>2012/04/09</td>
                <td>$138,575</td>
                <td>8352</td>
                <td>s.baldwin@datatables.net</td>
            </tr>
            <tr>
                <td>Zenaida</td>
                <td>Frank</td>
                <td>Software Engineer</td>
                <td>New York</td>
                <td>63</td>
                <td>2010/01/04</td>
                <td>$125,250</td>
                <td>7439</td>
                <td>z.frank@datatables.net</td>
            </tr>
            <tr>
                <td>Zorita</td>
                <td>Serrano</td>
                <td>Software Engineer</td>
                <td>San Francisco</td>
                <td>56</td>
                <td>2012/06/01</td>
                <td>$115,000</td>
                <td>4389</td>
                <td>z.serrano@datatables.net</td>
            </tr>
            <tr>
                <td>Jennifer</td>
                <td>Acosta</td>
                <td>Junior Javascript Developer</td>
                <td>Edinburgh</td>
                <td>43</td>
                <td>2013/02/01</td>
                <td>$75,650</td>
                <td>3431</td>
                <td>j.acosta@datatables.net</td>
            </tr>
            <tr>
                <td>Cara</td>
                <td>Stevens</td>
                <td>Sales Assistant</td>
                <td>New York</td>
                <td>46</td>
                <td>2011/12/06</td>
                <td>$145,600</td>
                <td>3990</td>
                <td>c.stevens@datatables.net</td>
            </tr>
            <tr>
                <td>Hermione</td>
                <td>Butler</td>
                <td>Regional Director</td>
                <td>London</td>
                <td>47</td>
                <td>2011/03/21</td>
                <td>$356,250</td>
                <td>1016</td>
                <td>h.butler@datatables.net</td>
            </tr>
            <tr>
                <td>Lael</td>
                <td>Greer</td>
                <td>Systems Administrator</td>
                <td>London</td>
                <td>21</td>
                <td>2009/02/27</td>
                <td>$103,500</td>
                <td>6733</td>
                <td>l.greer@datatables.net</td>
            </tr>
            <tr>
                <td>Jonas</td>
                <td>Alexander</td>
                <td>Developer</td>
                <td>San Francisco</td>
                <td>30</td>
                <td>2010/07/14</td>
                <td>$86,500</td>
                <td>8196</td>
                <td>j.alexander@datatables.net</td>
            </tr>
            <tr>
                <td>Shad</td>
                <td>Decker</td>
                <td>Regional Director</td>
                <td>Edinburgh</td>
                <td>51</td>
                <td>2008/11/13</td>
                <td>$183,000</td>
                <td>6373</td>
                <td>s.decker@datatables.net</td>
            </tr>
            <tr>
                <td>Michael</td>
                <td>Bruce</td>
                <td>Javascript Developer</td>
                <td>Singapore</td>
                <td>29</td>
                <td>2011/06/27</td>
                <td>$183,000</td>
                <td>5384</td>
                <td>m.bruce@datatables.net</td>
            </tr>
            <tr>
                <td>Donna</td>
                <td>Snider</td>
                <td>Customer Support</td>
                <td>New York</td>
                <td>27</td>
                <td>2011/01/25</td>
                <td>$112,000</td>
                <td>4226</td>
                <td>d.snider@datatables.net</td>
            </tr>
        </tbody>
    </table>
    <p align='center'><a href="http://clterryart.tumblr.com/" target="_blank"> <img src="http://66.media.tumblr.com/282aaeea43d9a7ebd6f58e2270f1d7c5/tumblr_ockldgSWib1qzomoco1_540.gif" style="height:480px; max-width:100%;"> </a></p>
<!-- 		</div>/.login-panel -->
<!-- 		</div>/.login-panel-wrap -->
<!-- 		<div class="login-footer"> -->
<!-- 			北祥股份有限公司 <span>服務電話：+886-2-2658-1910 | 傳真：+886-2-2658-1920</span> -->
<!-- 		</div>/.login-footer -->
	</div><!-- /.login-wrapper -->
	
	<div id="my_msg"></div>
</body>
</html>
