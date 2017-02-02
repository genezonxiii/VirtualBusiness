//lookdown()//視覺效果
//auto_complete(name,[])
//current_time() 2016-01-01之類的
//currency_unit(str) 幣值單位
//sleep(500) 就sleep sleep(1500).then(() => { 

//warning_msg(str); 就警告紅字
//order_source_auto(name); 銷售平台的autocomplete
//isIE(); 是不是IE
//money(amount); 換成錢的表示
//draw_table(table_name,title); 畫 報表的datatable
//grows_up(str); str半形變全形

//vender_color(vender); X
//table_before(str);X
//get_week_day(number);X
function date_format(str) {
	if(str==null){
		return "";
	}
	var words=str.replace(","," ").split(" ");
	return words[3]+"-"+words[0].replace("十一月","11").replace("十二月","12")
								.replace("一月","1").replace("二月","2").replace("三月","3").replace("四月","4")
								.replace("五月","5").replace("六月","6").replace("七月","7").replace("八月","8")
								.replace("九月","9").replace("十月","10")
								.replace("Jan","1").replace("Feb","2").replace("Mar","3").replace("Apr","4")
								.replace("May","5").replace("Jun","6").replace("Jul","7").replace("Aug","8")
								.replace("Sep","9").replace("Oct","10").replace("Nov","11").replace("Dec","12")
								+"-"+words[1];
}
function lookdown(){
	var tmp = Math.floor(Math.random()*2000);
	var tmp2=tmp+1;
	$("body").append("<div id='godown_"+tmp+"' style='opacity:0.3;position:fixed;top:40%;left:50%;'><img src='./images/godown.png' /></div>");
	$("#godown_"+tmp).animate({opacity: '0.7'},100);
	$("#godown_"+tmp).animate({
	    top: '+=60%',
		opacity: '0.2'
	},1500,function(){
		$("#godown_"+tmp).remove();
//		$("body").append("<div id='godown_"+tmp2+"' style='opacity:0.7;position:fixed;top:0%;left:50%;'><img src='./images/godown.png' /></div>");
//		$("#godown_"+tmp2).animate({
//		    top: '+=100%',
//			opacity: '0.1'
//		},2000,function(){
//			$("#godown_"+tmp2).remove();
//		});
	});
	$('.content-wrap').animate({
		scrollTop: $('.content-wrap').height()
	}, 500);
}


function get_sensitive(askfor){
	var ret="?";
	$.ajax({
		type : "POST",
		async : false,
		url : "function.do",
		data : {
			action : "get_controversial",
			askfor : askfor
		},
		success : function(result) {
			ret=result;
		}
	});
	return ret;
}

function tooltip(clas){
	$("."+clas).mouseover(function(e){
		 this.newTitle = this.title;
		 this.title = "";
		 var tooltip = "<div id='tooltip'>"+ this.newTitle +"<\/div>";
		 $("body").append(tooltip);
		 $("#tooltip").css({"top": (e.pageY+20) + "px","left": (e.pageX+10)  + "px"}).show("fast");
	 }).mouseout(function(){
	         this.title = this.newTitle;
	         $("#tooltip").remove();
	 }).mousemove(function(e){
	         $("#tooltip").css({"top": (e.pageY+20) + "px","left": (e.pageX+10)  + "px"});
	 });
}

function current_time(){
	var d=new Date();
	return d.getFullYear()+"-"+((d.getMonth()+1)>9?"":"0")+(d.getMonth()+1)+"-"+((d.getDate())>9?"":"0")+(d.getDate());
}

function currency_unit(str){
	if(str=='台幣'){return 'NT＄';}
	if(str=='人民幣'){return 'RMB￥';}
	if(str=='日幣'){return '￥';}
	if(str=='美金'){return 'US＄';}
	if(str=='韓幣'){return 'KRW';}
}

//function sleep(time) { 備註 IE 不可用
//	  return new Promise((resolve) => setTimeout(resolve, time));
//}

function grows_up(str){
	if(!(str.length>0)){return "";}
	var tmp =  new Array();
	var a = str;
	var b = "";
	for(var i = 0; i < a.length; i++){
	  if(a.charCodeAt(i)<=172 && a.charCodeAt(i)>=33){
	  	tmp[i] = a.charCodeAt(i)+65248;//轉全形unicode +65248
	  }else if(a.charCodeAt(i)==32){
		  tmp[i] =12288;
	  }else{
	  	tmp[i] = a.charCodeAt(i)
	  }
		b += String.fromCharCode(tmp[i]);
	}
		return b;
}

function warning_msg(str){
	$(".warning_msg").remove();
	if(str.length<1){return;}
	if($(".div_right_top").length>0){
		$(".div_right_top:first").after("<div class='warning_msg'>"+str+"</div>");
	}else{
		$(".input-field-wrap:first").after("<div class='warning_msg'>"+str+"</div>");
	}
}

function warning_msg_last(str){
	$(".warning_msg").remove();
	if(str.length<1){return;}
	if($(".div_right_top").length>0){
		$(".div_right_top:last").after("<div class='warning_msg'>"+str+"</div>");
	}else{
		$(".input-field-wrap:last").after("<div class='warning_msg'>"+str+"</div>");
	}
}

function auto_complete(name,tags){
	$("#"+name).autocomplete({
    	minLength: 1,
    	source: tags,
    	position:  {my: "left top", at: "left bottom", collision: "flipfit"}
    });
    $("#"+name).dblclick(function(event){ 
    	event.preventDefault();
    	$("#"+name).autocomplete({minLength: 0}); 
    	
    	$("#"+name).click(function(){
        	var eve=jQuery.Event("keydown");
        	eve.which=40;
          	$(this).trigger(eve);
        });
    });
}


function order_source_auto(name) {
    var availableTags = [
      "yahoo","超級商城","Pchome","夠麻吉","樂天",
      "payeasy","博客來","ibon", "momo","愛買",
      "GoHappy","myfone","森森購物","九易","UDN",
      "17Life", "ASAP", "通用","國泰Tree","Line Mart"
    ];
    //{label:"", value:""}, {label:"車王電", value:"車王電"}];
//    var availableTags = [{label:"yahoo", val2:"1"},{label:"超級商城", val2:"2"},{label:"Pchome", val2:"3"},
//                         {label:"通用", value:"4"},{label:"夠麻吉", value:"5"},{label:"樂天", value:"6"},
//                         {label:"payeasy", value:"7"},{label:"博客來", value:"8"},{label:"ibon", value:"9"},
//                         {label:"momo", value:"10"},{label:"愛買", value:"11"},{label:"GoHappy", value:"12"},
//                         {label:"myfone", value:"13"},{label:"森森購物", value:"14"},{label:"九易", value:"15"},
//                         {label:"UDN", value:"16"},{label:"17Life", value:"17"},{label:"ASAP", value:"18"},
//                         {label:"國泰Tree", value:"19"},{label:"Line Mart", value:"20"}
//                       ];
    
    $( "#"+name).autocomplete({
    	minLength: 0,
    	source: availableTags,
    	position:  {my: "left top", at: "left bottom", collision: "flipfit"}
    });
    $( "#"+name).focus(function(){
    	var eve=jQuery.Event("keydown");
    	eve.which=40;
      	$(this).trigger(eve);
    });
}

function isIE(){
	return (window.navigator.userAgent.indexOf("MSIE ") > 0 || !!navigator.userAgent.match(/Trident.*rv\:11\./));	
}

function get_week_day(number){
	var i,j;
	for(i=1;i<13;i++){
		for(j=1;j<32;j++){
			if(j==31){
				if(i==2||i==4||i==6||i==9||i==11){continue;}
			}
			if(getYearWeek(2016,i,j)==number){
				return ((i>9)?i:"0"+i)+"/"+((j>9)?j:"0"+j)+"";
			}
		}
	}
}

function money(amount){
	 if(!(parseInt(amount)>-1)){return amount};
	 var num = amount.toString();
	 var pattern = /(-?\d+)(\d{3})/;
	 while(pattern.test(num))
	 {
		 num = num.replace(pattern, "$1,$2");
	 }
	 return "$ " + num + " 元";
}

function table_before(str){
	var i;
	var table_name=str;
	var selector="#"+table_name+" th";
	var leng =  $(selector).length;
	var tmp="<table><tr>";
	for(i=1;i<leng+1;i++){
		selector="#"+table_name+" th:nth-child("+i+")";
		tmp+="<td><input class='tog_col' checked id='col-"+i+"' type='checkbox' value='"+i+"'onclick='$(\"#"+table_name+"\").DataTable().column("+(i-1)+").visible(!$(\"#"+table_name+"\").DataTable().column("+(i-1)+").visible());'><label class='tog_col' for='col-"+i+"'><span class='form-label'>"+$(selector).text()+"</span></label></td>";
		if(i%5==0){tmp+="</tr><tr>";}
	}
// 	tmp+="<td><input class='tog_col' checked id='col-100' type='checkbox' value='1'onclick='"
// 		+"for(var i=0;i<20;i++)$(\"#"+table_name+"\").DataTable().column(i).visible(!$(this).val());$(\"#col-100\").val(0);"
// 		+"'><label class='tog_col' for='col-100'><span class='form-label'>全選</span></label></td>";
	tmp+="</tr></table>";
	
	selector="#"+table_name;
	$(selector).before(tmp);
}

function draw_table(table_name,title){
	$(".tog_col").remove();
	$("#"+table_name+" td").each(function(index){
		if($( this ).text().length>10)
		$( this ).html($( this ).html()+"&nbsp;") ;
	});
	//alert($("#"+table_name+" tbody").html());
	var i=0;
	while($("#animate_table-"+i).length>0){i++;};
	var name="animate_table-"+i;
	var selector="#"+table_name;
	//$(selector).dataTable().fnDestroy();
	
	var tmp="<tr><td width='100%'><table class='result-table' id='"+table_name+"'>"+$(selector).html()+"</table></td></tr>";
	$(selector).attr("id",name);
	$("#"+name).attr("class","");
	$("#"+name).html(tmp);
	$("#"+name).css("width","100%");
	//alert("#animate_table").html());
	//$(selector).before("<a id='animate_table'>111");
	//$(selector).after("222</a>");
	//$("#animate_table").css("display","none");
	$("#"+name).css("opacity","0");
	
	//table_before(table_name);
	
	$(selector).dataTable({
		"lengthMenu": [ [10, 25, 50, -1], [10, 25, 50, "全"] ],
		dom: 'lfrB<t>ip',
		buttons: [{
		    extend: 'excel',
		    text: '輸出為execl報表',
		    title: title ,
		    exportOptions: {
		    	columns: ':visible'
			}
		  }],
		"language": {"url": "js/dataTables_zh-tw.txt","zeroRecords": "<font size=3>---查無結果---</font>"}
	});
	//$("#animate_table").fadeIn(1000);
	$(selector+" tbody tr").css({"height":"48px","text-align": "center"});
	$("#"+name).animate({"opacity":"0.01"},1);
	$("#"+name).animate({"opacity":"1"});
}

function vender_color(vender){
	if(vender=="myfone"||vender==0){return '#be1e2d';}
	if(vender=="九易"||vender==1){return '#666699';}
	if(vender=="Pchome"||vender==2){return '#92d5ea';}
	if(vender=="ASAP"||vender==3){return '#ee8310';}
	if(vender=="GoHappy"||vender==4){return '#8d10ee';}
	if(vender=="愛買"||vender==5){return '#5a3b16';}
	if(vender=="momo"||vender==6){return '#26a4ed';}
	if(vender=="yahoo"||vender==7){return '#f45a90';}
	if(vender=="UDN"||vender==8){return '#e9e744';}
	
	//已下還沒有特定過
	if(vender=="17Life"||vender==9){return '#660066';}
	if(vender=="樂天"||vender==10){return '#00BBBB';}
	if(vender=="國泰Tree"||vender==11){return '#BBBBFF';}
	if(vender=="夠麻吉"||vender==12){return '#BBFFBB';}
	if(vender=="通用"||vender==13){return '#FFBBBB';}
	if(vender=="超級商城"||vender==14){return '#6666FF';}
	if(vender=="博客來"||vender==15){return '#66FF66';}
	if(vender=="payeasy"||vender==16){return '#006666';}
	if(vender=="ibon"||vender==17){return '#FF5566';}
	if(vender=="森森購物"||vender==18){return '#666600';}
	if(vender=="Line Mart"||vender==19){return '#333333';}
	
//	var x=255;
//	alert(x.toString(16));
	
	return '#553388';
}
/* 行事曆
--------------------------------------------------------------------- */
var opt = {
   //dayNames:["星期日","星期一","星期二","星期三","星期四","星期五","星期六"],
   dayNamesMin:["日","一","二","三","四","五","六"],
   monthNames:["1","2","3","4","5","6","7","8","9","10","11","12"],
   monthNamesShort:["1","2","3","4","5","6","7","8","9","10","11","12"],
   //monthNamesShort:["一月","二月","三月","四月","五月","六月","七月","八月","九月","十月","十一月","十二月"],
   prevText:"上月",
   nextText:"次月",
   weekHeader:"週",
   showMonthAfterYear:true,
   dateFormat:"yy-mm-dd",
   changeYear: true,
   changeMonth: true
   //"setDate" :new Date(),
   };
$(function(){
	$( ".input-date" ).datepicker(opt);
});