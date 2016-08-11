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
   changeMonth: true,
   //"setDate" :new Date(),
   };
//function f_report_name(){
//	switch(location.pathname.split("/")[2]){
//	case "salereport.jsp":
//		return "訂單報表";
//		break;
//	case "distributereport.jsp":
//		return "配送報表";
//		break;
//	case "salereturnreport.jsp":
//		return "退貨報表";
//		break;
//	case "shipreport.jsp":
//		return "出貨報表";
//		break;
//	case "purchreport.jsp":
//		return "進貨報表";
//		break;
//	case "purchreturnreport.jsp":
//		return "進貨退回報表";
//		break;
//	case "stockreport.jsp":
//		return "庫存管理報表";
//		break;
//	case "supplyreport.jsp":
//		return "供應商報表";
//		break;
//	case "productreport.jsp":
//		return "商品報表";
//		break;
//	case "customerreport.jsp":
//		return "客戶報表";
//		break;
//	case "accreceivereport.jsp":
//		return "應收帳款報表";
//		break;
//	case "accpayreport.jsp":
//		return "應付帳款報表";
//		break;
//	default:
//		return "輸出";
//		break;}
//};

$(function(){
//	var my_name = location.pathname.split("/")[2];
//	var my_name_CHT=f_report_name();
  $( ".input-date" ).datepicker(opt);
  //對result-table做修改
//  $( ".input-date" ).after("<h1>"+my_name_CHT+"</h1>"+location.pathname.split("/")[2]);
  //$(".report").before($(".report").html().replace(/<th>/g,""));
//  $(".reportXX").dataTable({
//	  "scrollY": "200px",
//	   autoWidth: false,
//      "paging": false,
//		dom: 'lfrB<t>ip',
//		buttons: [{
//		    extend: 'excel',
//		    text: '輸出為xlsx檔',
//		    title: my_name_CHT,
//		    exportOptions: {modifier: {search: 'none'}}
//		  }],
//		"language": {"url": "js/dataTables_zh-tw.txt"}
//	});
  //$(".report td").css({"word-break":"break-all","min-width":"90px","text-align":"center","white-space": "normal"});
});