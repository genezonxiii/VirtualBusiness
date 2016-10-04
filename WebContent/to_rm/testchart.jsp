<?
//EDIT THIS PORTION!!
$articleTitle = "jQuery Visualize Plugin: Accessible Charts &amp; Graphs from Table Elements using HTML 5 Canvas";
$articleUrl = "/lab/jquery_visualize_plugin_accessible_charts_graphs_from_tables_html5_canvas/";
?>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html; charset=utf-8" />
		<title><? echo "Filament Group Lab Example From: $articleTitle"; ?></title>	
<!-- 		<link rel="shortcut icon" href="https://www.filamentgroup.com//images/favicon2.ico" type="image/x-icon" /> -->
<!-- 		<link href="https://www.filamentgroup.com//style/demoPages" media="screen" rel="Stylesheet" type="text/css" /> -->
		<link type="text/css" rel="stylesheet" href="../css/visualize.jQuery.css"/>
		<link type="text/css" rel="stylesheet" href="https://www.filamentgroup.com/examples/charting_v2/demopage.css"/>
		<script type="text/javascript" src="http://ajax.googleapis.com/ajax/libs/jquery/1.3.2/jquery.min.js"></script>
<!-- 		[if IE]><script type="text/javascript" src="excanvas.compiled.js"></script><![endif] -->
		<script type="text/javascript" src="../js/visualize.jQuery.js"></script>
		<script type="text/javascript">
		
			$(function(){
				$('#bar').visualize({
					'type':'bar',
					'barMargin':'1','barGroupMargin':'10',
					'width':'600','height':'300',
					'appendTitle':'true','title':'6',
					'appendKey':'true',
					'colors':['#e9e744','#666699','#92d5ea','#ee8310','#8d10ee','#5a3b16','#26a4ed','#f45a90','#be1e2d'],
					'textColors':'','parseDirection':'x'
				});
				//$('#bar').addClass('accessHide');
				$('#line').visualize({
					'type':'line',
					'lineWeight':'2',
					'width':'600','height':'300',
					'appendTitle':'true','title':'6',
					'appendKey':'true',
					'colors':['#e9e744','#666699','#92d5ea','#ee8310','#8d10ee','#5a3b16','#26a4ed','#f45a90','#be1e2d'],
					'textColors':'','parseDirection':'x'
				});
				$('#line').addClass('accessHide');
				$('#pie').visualize({
					'type':'pie',
					'pieMargin':'50','pieLabelPos':'outside',
					'width':'400','height':'300',
					'appendTitle':'true','title':'銷售金額比例統計圖',
					'appendKey':'true',
					'colors':['#e9e744','#666699','#92d5ea','#ee8310','#8d10ee','#5a3b16','#26a4ed','#f45a90','#be1e2d'],
					'textColors':'','parseDirection':'y'
				});
				$('#pie').addClass('accessHide');
				$(".visualize").css("border","0px");
			});
		</script>
<style>
.visualize-pie .visualize-info { top: 10px; border: 0; right: auto; left: 10px; padding: 0; background: none; }
.visualize-pie ul.visualize-title { font-weight: bold; border: 0; }
.visualize-pie ul.visualize-key li { float: none; }
.visualize-pie { margin:5px 10px; }
.visualize { margin: 0 auto;}
</style>
	</head>
	<body>
		<table id='bar' style='display:none'>
			<caption>出貨量統計圖</caption>
			<thead><tr><td></td>
				<th>6月</th>
				<th>7月</th>
			</tr></thead><tbody>
				<tr><th>ibon</th><td></td><td>19</td></tr>
				<tr><th>pchome</th><td>14</td><td>9</td></tr>
				<tr><th>91</th><td>54</td><td>39</td></tr>
		</tbody></table>
		<br><br>
		<hr>
		<table id="line">
			<caption>銷售金額統計圖 </caption>
			<thead>
				<tr>
				    <td></td>
				    <th></th>
					<th>5/30</th>
					<th>6/06</th>
					<th>6/13</th>
					<th>6/20</th>
					<th>6/27</th>
					<th>7/04</th>
					<th>7/11</th>
					<th></th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>ibon</th>
					<td></td>
					<td>9</td>
					<td>30</td>
					<td>14</td>
					<td>49</td>
					<td>14</td>
					<td>49</td>
					<td>35</td>
					<td></td>
				</tr>
				<tr>
					<th>Pchome</th>
					<td></td>
					<td>90</td>
					<td>78</td>
					<td>90</td>
					<td>78</td>
					<td>14</td>
					<td>9</td>
					<td>30</td>
					<td></td>
				</tr>
				<tr>
					<th>momo</th>
					<td></td>
					<td>19</td>
					<td>118</td>
					<td>19</td>
					<td>118</td>
					<td>19</td>
					<td>118</td>
					<td>14</td>
					<td></td>
				</tr>
			</tbody>
		</table>
		<br><br>
		<hr>
		<table id="pie">
			<caption>銷售金額比例統計圖</caption>
			<thead>
				<tr>
				    <td></td>
					<th>ibon</th>
					<th>Pchome</th>
					<th>九易</th>
				</tr>
			</thead>
			<tbody>
				<tr>
					<th>七月</th>
					<td>140</td>
					<td>9</td>
					<td>30</td>
				</tr>
			</tbody>
		</table>
	</body>
</html>
