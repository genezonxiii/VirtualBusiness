		var fileName;
		var fileBuffer=[];

//		var input = $('<input/>', {
//            type: 'file',
//            multiple: 'multiple',
//        });
		
		$(function(){
			$('#fileDiv').hide();

			status_dialog =
				$('#status').dialog({
					draggable : true,
					resizable : false,
					autoOpen : false,
					modal : true,
					height: 100,
					width : 200
					});
			message_dialog =
				$('#message').dialog({
					draggable : true,
					resizable : false,
					autoOpen : false,
					modal : true,
					show : {
						effect : "blind",
						duration : 300
					},
					hide : {
						effect : "fade",
						duration : 300
					},
					width : 200,
					buttons : 
							[{
								text : '確認',
								click : function() {
									$(this).dialog("close");
								}
							}]
					});
			
			$('select').on('change', function() {
				$('#file').val('');
				$("#download").html("");
				$('#filesEdit').html('');
				if(this.value != 0){
					$('#fileDiv').show();
				}else{
					$('#fileDiv').hide();
				}
			});
			
			$('#file').click(function(){
				$("#download").html("");
				$('#filesEdit').html('');
			});	
			
			$("#file").on("change", function (event) {
				var type = '';
				var name = '';
				var size = '';
				var files = event.originalEvent.target.files;
				Array.prototype.push.apply(fileBuffer, files);
				console.log(fileBuffer);
				$(fileBuffer).each(function( index ,item) {
					name = item.name;
					size = Math.floor(((item.size)/1024)*10)/10 + 'KB';
					type = /[^.]+$/.exec(item.name);
				  
					var data = 
						'<div class="master" id="' + name + '">' +
							'<div class="detail">' +
								'<div><img src="./images/file/' + type + '.png"></div>' +
							'</div>' +
							'<div class="jFiler-item-assets">' +
								'<div class="info">' +
									'<ul >'+ 
										'<li class="fileTitle">' + name + '\n' + size + '</li>' +
									'</ul>'+
								'</div>' +
							'</div>' +
							//delete button
// 							'<div name><ul class="pull-right">'+ 
// 								'<li><a class="icon-jfi-trash jFiler-item-trash-action btn_delete"></a></li>' +
// 							 '</ul>'+
							'</div>' +
						'</div>';
					$('#filesEdit').append(data);
				});
				//reset array
				fileBuffer.length = 0;
			});
			
			//delete
// 			$("#filesEdit").delegate(".btn_delete", "click", function(e) {
// 				e.preventDefault();
// 				var name = $(this).parents(".master").attr("id")
// 				$(fileBuffer).each(function( index ,item) {
// 					if(item.name == name){
// 						fileBuffer.splice(index, 1);
// 					}
// 				});
// 	            input.files = fileBuffer;
// 				$(this).parents(".master").remove();
// 			});	
		});
		
		function upload(){
			if($('#file').get(0).files.length === 0){
				$('#message').find("#text").val('').text("請選擇檔案");
				message_dialog.dialog("open");
				return false;
			}
			var form = document.getElementById("form");
			var action = "sfTransfer.do"
				+"?action=upload"
				+"&type=" + $('#select-type').val()
			$(form).attr("action",action);
			return true;
		}
		$('#form').ajaxForm({
		    beforeSend: function() {
				$('#status').find("#text").val('').html("轉檔開始!");
				status_dialog.dialog("open");
		    },
		    uploadProgress: function(event, position, total, percentComplete) {
		    	status_dialog.dialog("close");
				$('#status').find("#text").val('').html("轉檔進行中!");
				status_dialog.dialog("open");
		    },
		    success: function(result) {
		    	
		    	status_dialog.dialog("close");
		    	if(result=="false"){
					$('#message').find("#text").val('').html("轉檔失敗!<br/>請確認檔案是否正確!");
					message_dialog.dialog("open");
					$("#download").html("");
		    	}else{
					$('#message').find("#text").val('').html("轉檔成功!");
					message_dialog.dialog("open");
					$("#download").html("");
					$("#download").append("&nbsp;&nbsp;&nbsp;<a href='./sfTransfer.do?action=download&fileName="+fileName+"&downloadName="+$('#select-type').val()+"_"+result+"'class='btn btn-primary'>檔案下載</a>");
		    	}
		    }
		});	