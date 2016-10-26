<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- <?xml version="1.0" encoding="UTF-8"?> -->
<!-- <html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en"> -->
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
    <title>電商平台討論區</title>
<style type="text/css">
input#chat {
/*  width: 410px */
    width:80%;
}

#console-container {
/*  width: 400px; */
    width:99%;
    height:100%;
}

#console {
/*    	max-height: 340px;   */
/*   	height: 0px;  */
/*     border: 1px solid #CCCCCC; */
    border-right-color: #999999;
    border-bottom-color: #999999;
    overflow-y: auto;
    padding: 5px;
}

#console p {
    padding-left: 10px;
    padding-top: 3px;
    margin: 0;
}
.name_list{
	float:right;
/* 	margin:10px; */
	width:100%;
	height:420px;
	overflow-y: scroll;
	border: 1px solid #CCCCCC;
}
.name_list li{
	border-bottom:1px solid #CCC;
	text-align:center;
}
.name_list li:hover{
	background:#ccc;
}

</style>
<link rel="stylesheet" href="css/1.11.4/jquery-ui.css">
<link rel="stylesheet" href="css/styles.css">
</head>
<body>
	<jsp:include page="template.jsp" flush="true"/>
	<div class="content-wrap" >
		<div class='bdyplane' style="opacity:0">

<script type="text/javascript" src="js/jquery-1.10.2.js"></script>
<script type="text/javascript" src="js/jquery-ui.min.js"></script>

    <script type="text/javascript">
//     document.write("application/javascript");   
        "use strict";
        var Chat = {};

        Chat.socket = null;
        Chat.connect = (function(host) {
            if ('WebSocket' in window) {
                Chat.socket = new WebSocket(host);
                console.log("host1:"+host);
            } else if ('MozWebSocket' in window) {
                Chat.socket = new MozWebSocket(host);
                console.log("host2:"+host);
            } else {
                Console.log('Error: WebSocket is not supported by this browser.');
                return;
            }

            Chat.socket.onopen = function () {
                Console.log('~<font color=gray>您進入了聊天室</font>~');
                document.getElementById('send_msg').onclick =function(event) {
                   	event.preventDefault();
                    Chat.sendMessage();
                };
                document.getElementById('chat').onkeydown = function(event) {
                    if (event.keyCode == 13) {
                    	event.preventDefault();
                        Chat.sendMessage();
                    }
                };
            };

            Chat.socket.onclose = function () {
                document.getElementById('chat').onkeydown = null;
                Console.log('~<font color=red>您離開了聊天室</font>~');
            };

            Chat.socket.onmessage = function (message) {
                if((message.data).indexOf("System(>_<):")>-1){
                	$("#online_n").html("<a href='chatsubject.jsp' style='float:right;padding-right:50px;'>返回主題列表</a><br>在線人數: "+(message.data).split(":")[1].split(",")[0]+"人。<br>");
                	var tmp = (message.data).split(":")[1].split(",");
                	var name_list="",i=0;
                	
                	for(i=1;i<tmp.length;i++){
                		name_list+="<li style='padding:2px 5px;'><b>"+"<img src='./images/online_icon.png' style='width:15px;padding-right:3px;'>"+tmp[i]+"</b></li>";
                	};
                	$("#online_list").html(name_list);
//                 	Console.log("更新某資料");
                }else{
                	Console.log(message.data);
                }
                //所有東西都從這邊經過 如果要加特殊處理人數 名單 都在這邊做
            };
        });

        Chat.initialize = function() {
            if (window.location.protocol == 'http:') {
                Chat.connect('ws://' + window.location.host + '/VirtualBusiness/websocket/chat/'+'<%=request.getSession().getAttribute("user_name")%>');
                console.log('ws://' + window.location.host + '/VirtualBusiness/websocket/chat/'+'<%=request.getSession().getAttribute("user_name")%>');
            } else {
                Chat.connect('wss://' + window.location.host + '/VirtualBusiness/websocket/chat/'+'<%=request.getSession().getAttribute("user_name")%>');
                console.log('wss://' + window.location.host + '/VirtualBusiness/websocket/chat/'+'<%=request.getSession().getAttribute("user_name")%>');
            }
        };

        Chat.sendMessage = (function() {
            var message = document.getElementById('chat').value;
            if (message != '') {
                Chat.socket.send(message);
                document.getElementById('chat').value = '';
            }
        });

        var Console = {};

        Console.log = (function(message) {
            var console = document.getElementById('console');
            var p = document.createElement('p');
            p.style.wordWrap = 'break-word';
            p.innerHTML = message;
            console.appendChild(p);
            while (console.childNodes.length > 40) {
                console.removeChild(console.firstChild);
            }
            console.scrollTop = console.scrollHeight;
        });

        Chat.initialize();

        document.addEventListener("DOMContentLoaded", function() {
        	
            // Remove elements with "noscript" class - <noscript> is not allowed in XHTML
            var noscripts = document.getElementsByClassName("noscript");
            for (var i = 0; i < noscripts.length; i++) {
                noscripts[i].parentNode.removeChild(noscripts[i]);
            }
        }, false);

    </script>
    <script>
    $(function(){
//     	$("#send_msg").click(function(){});
    	$(".bdyplane").animate({"opacity":"1"});
<%--     	var str = "<%=request.getSession().getAttribute("user_name")%>"; --%>
//     	alert(str); 
    });
    </script>
<!--     <div style="padding-right:20px;"><a href="chatsubject.jsp" style='position:absolute;right:150px;top:80px;'>返回</a></div> -->
		<div class="noscript"><h2 style="color: #ff0000">Seems your browser doesn't support Javascript! Websockets rely on Javascript being enabled. Please enable
		    Javascript and reload this page!</h2></div>
		<div>
		
		<table id='' style='width:90%;height:420px;border:3px solid #aaa;margin:30px auto;'>
			<caption id='online_n' style="padding-bottom:5px;"></caption>
			<tr>
				<td rowspan='2' style='border-right:3px solid #aaa;width:132px;'>
					<ul id='online_list' class="name_list">
<!-- 			    	<li style='background:gray;'>　</li> -->
			    	<li>讀取中...</li>
				</ul>
				</td>
				<td style='border-bottom:3px solid #aaa;' valign="bottom" colspan='2'>
<!-- 					<div id="console-container"> -->
				        <div id="console" style="max-height:327px;max-width:968px;">
				        </div>
<!-- 				    </div> -->
				</td>
			</tr>
			<tr>
				<td style='height:80px;'>
<!-- 					<input type="text" placeholder="type and press enter to chat" id="chat" /> -->
					<textarea placeholder="輸入發言" id="chat" style='width:98%;border:0px;margin:2px 5px;'></textarea>
				</td>
				<td style='width:100px;'><a id='send_msg' class='btn btn-darkblue' style='margin:10px;text-align:center;line-height:26px;'>發送</a></td>
			</tr>
		</table>
<!-- 		    <p> -->
<!-- 		        <input type="text" placeholder="type and press enter to chat" id="chat" /> -->
<!-- 		    </p> -->
<!-- 		    <div id="console-container"> -->
<!-- 		        <div id="console"></div> -->
<!-- 		    </div> -->
		</div>
		</div>
	</div>
</body>
</html>