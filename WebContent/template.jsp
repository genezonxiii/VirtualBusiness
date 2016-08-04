<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Insert title here</title>
	<link rel="stylesheet" href="vendor/css/jquery-ui.min.css">
	<link rel="stylesheet" href="vendor/css/font-awesome.min.css">
	<link rel="stylesheet" href="css/styles.css">
	<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script>
function who(){
	switch(location.pathname.split("/")[2]){
//####交易處理############################
	case "upload.jsp":
		return "訂單拋轉作業";
		break;
//####後臺支援系統############################
	case "purchase.jsp":
		return "進貨管理";
		break;
	case "purchreturn.jsp":
		return "進貨退回管理";
		break;
	case "sale.jsp":
		return "銷貨管理";
		break;
	case "salereturn.jsp":
		return "銷貨退回管理";
		break;
	case "stock.jsp":
		return "庫存管理";
		break;
	case "producttype.jsp":
		return "商品類型管理";
		break;
	case "productunit.jsp":
		return "商品單位管理";
		break;
	case "product.jsp":
		return "商品管理";
		break;
	case "supply.jsp":
		return "供應商管理";
		break;
	case "user.jsp":
		return "用戶管理";
		break;
	case "group.jsp":
		return "公司管理";
		break;
	case "customer.jsp":
		return "客戶管理";
		break;
	case "accreceive.jsp":
		return "應收帳款管理";
		break;
	case "accpay.jsp":
		return "應付帳款管理";
		break;
	case "changepassword.jsp":
		return "用戶帳密管理";
		break;
	case "tagprint.jsp":
		return "標籤列印";
		break;
//####報表管理############################
	case "salereport.jsp":
		return "訂單報表";
		break;
	case "distributereport.jsp":
		return "配送報表";
		break;
	case "salereturnreport.jsp":
		return "退貨報表";
		break;
	case "shipreport.jsp":
		return "出貨報表";
		break;
	case "purchreport.jsp":
		return "進貨報表";
		break;
	case "purchreturnreport.jsp":
		return "進貨退回報表";
		break;
//######分析圖表##########################
	case "salechart.jsp":
		return "出貨量統計圖";
		break;
	case "saleamountchart.jsp":
		return "銷售金額統計圖";
		break;
	case "saleamountstaticchart.jsp":
		return "銷售金額比例統計圖";
		break;
//######線上學院##########################
	case "onlinecourse.jsp":
		return "線上學院";
		break;

	default:
		alert("default");
		return "something_wrong?";
		break;
	}
};
</script>
</head>
<body>


</body>
</html>