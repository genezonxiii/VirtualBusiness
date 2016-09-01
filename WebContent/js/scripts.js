
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
	var selector="#"+table_name;
	var tmp="<tr><td width='100%'><table class='result-table' id='"+table_name+"'>"+$(selector).html()+"</table></td></tr>";
	$(selector).attr("id","animate_table");
	$("#animate_table").attr("class","");
	$("#animate_table").html(tmp);
	$("#animate_table").css("width","100%");
	//alert("#animate_table").html());
	//$(selector).before("<a id='animate_table'>111");
	//$(selector).after("222</a>");
	//$("#animate_table").css("display","none");
	$("#animate_table").css("opacity","0");
	$(".tog_col").remove();
	$(selector+" td").each(function(index){
		if($( this ).text().length>10)
		$( this ).html($( this ).html()+"&nbsp;") ;
	});
	$(selector).dataTable().fnDestroy();
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
		"language": {"url": "js/dataTables_zh-tw.txt"}
	});
	//$("#animate_table").fadeIn(1000);
	$("#animate_table").animate({"opacity":"0.01"},1);
	$("#animate_table").animate({"opacity":"1"});
}

function vender_color(vender){
	if(vender=="ibon"){return '#FF0000';}
	if(vender=="九易"){return '#EEEE00';}
	if(vender=="Pchome"){return '#0000FF';}
	if(vender=="ASAP"){return '#FF6666';}
	if(vender=="GoHappy"){return '#FF00FF';}
	if(vender=="國泰Tree"){return '#00FFFF';}
	if(vender=="17Life"){return '#BBBBBB';}
	if(vender=="yahoo"){return '#BBBB00';}
	if(vender=="UDN"){return '#BB00BB';}
	if(vender=="樂天"){return '#00BBBB';}
	if(vender=="愛買"){return '#BBBBFF';}
	if(vender=="夠麻吉"){return '#BBFFBB';}
	if(vender=="通用"){return '#FFBBBB';}
	if(vender=="超級商城"){return '#6666FF';}
	if(vender=="博客來"){return '#66FF66';}
	if(vender=="momo"){return '#00FF00';}
	if(vender=="payeasy"){return '#006666';}
	if(vender=="myfone"){return '#660066';}
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