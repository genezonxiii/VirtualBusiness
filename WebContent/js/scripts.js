
//warning_msg(str); 就警告紅字
//order_source_auto(name); 銷售平台的autocomplete
//isIE(); 是不是IE
//money(amount); 換成錢的表示
//draw_table(table_name,title); 畫 報表的datatable
//grows_up(str); str半形變全形

//vender_color(vender); X
//table_before(str);X
//get_week_day(number);X

function currency_unit(str){
	if(str=='台幣'){return 'NT＄';}
	if(str=='人民幣'){return 'RMB￥';}
	if(str=='日幣'){return '￥';}
	if(str=='美金'){return 'US＄';}
	if(str=='韓幣'){return 'KRW';}
}

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
		$(".div_right_top").after("<div class='warning_msg'>"+str+"</div>");
	}else{
		$(".input-field-wrap").after("<div class='warning_msg'>"+str+"</div>");
	}
}

function order_source_auto(name) {
    var availableTags = [
      "yahoo","超級商城","Pchome","夠麻吉","樂天",
      "payeasy","博客來","ibon", "momo","愛買",
      "GoHappy","myfone","森森購物","九易","UDN",
      "17Life", "ASAP", "通用","國泰Tree","Line Mart"
    ];
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
	$("#"+table_name).dataTable().fnDestroy();
	var i=0;
	while($("#animate_table-"+i).length>0){i++;};
	var name="animate_table-"+i;
	var selector="#"+table_name;
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
	
	table_before(table_name);
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
	$(selector+" tr").css({"height":"56px","text-align": "center"});
	$("#"+name).animate({"opacity":"0.01"},1);
	$("#"+name).animate({"opacity":"1"});
}

function vender_color(vender){
	if(vender=="myfone"){return '#be1e2d';}
	if(vender=="九易"){return '#666699';}
	if(vender=="Pchome"){return '#92d5ea';}
	if(vender=="ASAP"){return '#ee8310';}
	if(vender=="GoHappy"){return '#8d10ee';}
	if(vender=="愛買"){return '#5a3b16';}
	if(vender=="momo"){return '#26a4ed';}
	if(vender=="yahoo"){return '#f45a90';}
	if(vender=="UDN"){return '#e9e744';}
	
	//已下還沒有特定過
	if(vender=="17Life"){return '#660066';}
	if(vender=="樂天"){return '#00BBBB';}
	if(vender=="國泰Tree"){return '#BBBBFF';}
	if(vender=="夠麻吉"){return '#BBFFBB';}
	if(vender=="通用"){return '#FFBBBB';}
	if(vender=="超級商城"){return '#6666FF';}
	if(vender=="博客來"){return '#66FF66';}
	if(vender=="payeasy"){return '#006666';}
	if(vender=="ibon"){return '#660066';}
	if(vender=="森森購物"){return '#666600';}
	if(vender=="Line Mart"){return '#333333';}
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