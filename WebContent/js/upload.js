var company_count = 25;
var company_row_count = 5;

function init(){
	$('#fileDiv').hide();
	buildHiddField();
	buildDialog();
	buildIconBtns();
}

function buildHiddField(){
	var context = $('#fileDiv');

	$('#deliveryMethod').remove();
	
	var input = document.createElement('INPUT');
	input.id = 'deliveryMethod';
	input.type = 'hidden';
	
	context.append(input)
}
//Store the platform in the map
var platform_map = new Object();

function getPlatformMap(){
	console.log('==================================================');
	console.log('getPlatformMap start');
	platform_map ={};
	var key = "";
	var value = "";
	$.ajax({
		type : "POST",
		url : "upload.do",
		data : {
			action :"select_way_of_platform"
		},
		success : function(result) {	
			var json_obj = $.parseJSON(result);
			$.each (json_obj, function (i,item) {
				key = item.throwfile_platform;
				value = "<input id='type-radio-"+i+"' type='radio' name='type-radio-group' value='"+item.throwfile_type+"' restrict='"+item.throwfile_fileextension+"'>" +
						"<label for='type-radio-"+i+"' ><span class='form-label'>"+item.throwfile_name+"</span></label>";
				if(platform_map[key] != null){
					value = platform_map[key] + value;
				}
				platform_map[key]=value;			
			});
			console.log('getPlatformMap end');
			console.log('==================================================\n\n');
		}
	});	
}

function buildIconBtns(){
	console.log('==================================================');
	console.log('buildIconBtns start');
	var iconBtns = document.getElementById('iconBtns');
	var detail = document.createElement('DIV');
	
	$.ajax({
		type : "POST",
		url : "upload.do",
		data : {
			action :"select_platform_kind" 
		},
		success : function(result) {
			var json_obj = $.parseJSON(result);
			$.each (json_obj, function (i,item) {
					if((i % company_row_count)== 0){
						detail = document.createElement('DIV');
					}
					
					detail.className = 'ec-radio-group-wrap';
					
					var input = document.createElement('INPUT');
					input.id = 'radio-' + item.throwfile_platform;
					input.type = 'radio';
					input.name = 'ec-radio-group';
					input.value = item.throwfile_platform;
					input.setAttribute('custom-restrict', item.throwfile_fileextension);
					
					var label = document.createElement('LABEL');
					label.htmlFor = 'radio-' + item.throwfile_platform;

					var img = document.createElement('IMG');
					img.src = 'images/' + ((item.icon.length>0) ? item.icon : "ec-logos/noname.png") ;

					var span = document.createElement('SPAN');
					var span_text = document.createTextNode(item.memo);
					span.appendChild(span_text);
					
					detail.appendChild(input);
					detail.appendChild(label);
					label.appendChild(img);
					label.appendChild(span);
					
					iconBtns.appendChild(detail);
			});
			
			getPlatformMap();
			console.log('platform_map ↓');
			console.log(platform_map);
			console.log('buildIconBtns end');
			console.log('==================================================\n\n');
		}
	});
}

function buildDialog(){
	$("#choose-order-type").dialog({
		draggable : true,
		resizable : false,
		autoOpen : false,
		height : "auto",
		width : "auto",
		modal : true,
	    open: function(event, ui) {
	        $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
	    },
		show : {
			effect : "blind",
			duration : 300
		},
		hide : {
			effect : "fade",
			duration : 300
		},
		buttons : [{
					text : "確定",
					click : function() {
						var selectedRadio = $( "#choose-order-type > input[name='type-radio-group']:checked" );
						if(selectedRadio.length>0){
							$("#typeImg").remove();
							
							var type = selectedRadio.val();
							var id = $('input[name="ec-radio-group"]:checked').attr("id");
							var lab = $( "label[for='" + id + "']" );
//							var left = ((lab.offset().left)+ (lab.width()*3/5))+'px';
//							var top = (lab.offset().top)+'px';
							var left =  lab.offset().left - lab.width()*1/2.2 +"px";
							var top = $("#iconBtns").offset().top - lab.offset().top + 400 +'px';
							var hidden = $('#deliveryMethod');
							
							var img = document.createElement('IMG');
							img.id = 'typeImg'
							img.style.position = 'relative';
							img.style.left = left;
							img.style.bottom = top;
					    	img.src= './images/' + type + '.png';
					    	
					    	var iconBtns = document.getElementById('iconBtns');

					    	iconBtns.appendChild(img);
					    	
					    	hidden.val('').val(selectedRadio.val());
					    	
							$(this).dialog("close");
							$('#fileDiv').show();
						}else{
							$('#fileDiv').hide();
					    	$('#message').find("#text").val('').html("請選擇訂單類型!");
							message_dialog.dialog("open");
						}						
					}
				}, {
					text : "取消",
					click : function() {
						$( "input:checked[name='ec-radio-group']" ).prop("checked", false);
						$('#typeImg').remove();
						$(this).dialog('close');
					}
				} ]
	});
}
//var accept= ["csv","xls","xlsx"];
var sendNames = "";
var sendCount = 0; //要寄送幾次
var sendCountTime = 0; //實際寄送幾次
var sendStatus = 0; //寄送狀態，用來判斷是否有檔案失敗 (0:成功)
function sendFileToServer(formData,status){
	sendCountTime ++;
    var uploadURL ="upload.do"; //Upload URL
    var extraData ={}; //Extra Data.
    var jqXHR=$.ajax({
	            xhr: function() {
		            var xhrobj = $.ajaxSettings.xhr();
		            if (xhrobj.upload) {
		                    xhrobj.upload.addEventListener('progress', function(event) {
		                        var percent = 0;
		                        var position = event.loaded || event.position;
		                        var total = event.total;
		                        if (event.lengthComputable) {
		                            percent = Math.ceil(position / total * 100);
		                        }
		                        //Set progress
		                        status.setProgress(percent);

		                        if((sendCountTime == sendCount) && (percent==100)){
		            				$('#status').find("#text").val('').html("檔案已上傳完成<br><br>請稍後片刻<br><br>正在進行拋轉作業!");
		            				status_dialog.dialog("open");
		                        }
		                    }, false);
		            }
		            return xhrobj;
		        },
			    url: uploadURL,
			    type: "POST",
			    contentType:false,
			    processData: false,
		        cache: false,
		        data: formData,
		        success: function(result){
		        	console.log('result: '+result);
		        	
		        	var duplicate = [];
		        	
			    	if(result=="false"){
			        	sendNames += "<p alert='left'>["+formData.get('file').name+"]</p><br>";
			        	sendStatus ++;
			    	}else{
		            	var json_obj = $.parseJSON(result);
						$.each(json_obj,function(i, item) {
				        	console.log('item '+i);
				        	if(i == 'duplicate'){
				        		duplicate = item.split(',');
				        	}
				        	console.log('duplicate');
				        	console.log(duplicate);
						});
			    	}
			    	
			    	if ((sendCountTime == sendCount) && (sendStatus == 0)&&(duplicate.length==0)){
				    	status_dialog.dialog("close");
				    	$('#message').find("#text").val('').html("匯入成功!");
						message_dialog.dialog("open");
						$("#download").html("");
//						createDlBtn(result);
			    	}else if ((sendCountTime == sendCount)&& (sendStatus != 0)&&(duplicate.length==0)){
				    	status_dialog.dialog("close");
//			    		$(btnArea).find('#downloadBtn').remove();
						$('#message').find("#text").val('').html("拋轉失敗!<br/>請確認檔案!<br/><br/>"+sendNames+"<br/>是否正確!");
						message_dialog.dialog('option','width','auto').dialog("open");
			    	}
			    	if ((sendCountTime == sendCount) && (duplicate.length!=0)){
				    	status_dialog.dialog("close");
				    	
						var data = document.createElement("DIV");
							data.style.overflowY = 'auto';
							data.style.overflowX = 'hidden';
							data.style.maxHeight = '200px';
							
						var title = document.createElement("DIV");						
						
						var table = document.createElement("TABLE");
							table.className = 'duplicate_table'
								
						var tr = document.createElement("TR");
						var br = document.createElement("BR");
							
						var td ;
						var para;
						var text;
						
						for(var i =0; i<duplicate.length; i++){
							if( i%3 == 0 ){
								text = document.createTextNode(duplicate[i]);
								para = document.createElement("P");
								td = document.createElement("TD");
								tr = document.createElement("TR");
							}else{
								text = document.createTextNode(duplicate[i]);
								para = document.createElement("P");
								td = document.createElement("TD");
							}
							para.appendChild(text);
							td.appendChild(para);
							tr.appendChild(td);
							table.appendChild(tr);
						}

						para = document.createElement("P");
						text = document.createTextNode('拋轉失敗');
						
						para.appendChild(text);
						title.appendChild(para);
						title.appendChild(br);

						para = document.createElement("P");
						text = document.createTextNode('資料重複，請確認以下訂單編號↓');

						para.appendChild(text);
						title.appendChild(para);
						title.appendChild(br);

						data.appendChild(table);
						
						$('#message').find("#text").html('').append(title,data);;
						message_dialog.dialog('option','width','auto').dialog("open");
			    	} 
		        }
		    }); 
 
    status.setAbort(jqXHR);
}
 
var rowCount=0;
function createStatusbar(obj)
{
     rowCount++;
     var row="odd";
     if(rowCount %2 ==0) row ="even";
     this.statusbar = $("<div class='statusbar "+row+"'></div>");
     this.filename = $("<div class='filename'></div>").appendTo(this.statusbar);
     this.size = $("<div class='filesize'></div>").appendTo(this.statusbar);
     this.progressBar = $("<div class='progressBar'><div></div></div>").appendTo(this.statusbar);
     this.abort = $("<div class='abort'>Abort</div>").appendTo(this.statusbar);
     
     $('.statusbarDiv').append(this.statusbar);
     //obj.after(this.statusbar);
 
    this.setFileNameSize = function(name,size)
    {
        var sizeStr="";
        var sizeKB = size/1024;
        if(parseInt(sizeKB) > 1024)
        {
            var sizeMB = sizeKB/1024;
            sizeStr = sizeMB.toFixed(2)+" MB";
        }
        else
        {
            sizeStr = sizeKB.toFixed(2)+" KB";
        }
 
        this.filename.html(name);
        this.size.html(sizeStr);
    }
    this.setProgress = function(progress)
    {       
        var progressBarWidth =progress*this.progressBar.width()/ 100;  
        this.progressBar.find('div').animate({ width: progressBarWidth }, 10).html(progress + "% ");
        if(parseInt(progress) >= 100)
        {
            this.abort.hide();
        }
    }
    this.setAbort = function(jqxhr)
    {
        var sb = this.statusbar;
        this.abort.click(function()
        {
            jqxhr.abort();
            sb.hide();
        });
    }
}

//draw selected files
function handleFileUpload(files){
	if((files.length>1)||($('#filesEdit>div').length > 0)){
		fileBuffer = fileBuffer[0];
		$('#message').find("#text").val('').html("只能選擇一筆檔案拋轉!");
		message_dialog.dialog("open");
		return false;
	}
	var master, detail, img, assets, info, ul, li, para, name, size, br;
	console.log('==================================================');
	console.log('handleFileUpload start');
	console.log(files);
	$(files).each(function( index ,item) {
		name = item.name;
		size = Math.floor(((item.size)/1024)*10)/10 + 'KB';
		type = /[^.]+$/.exec(item.name);
		
		master = document.createElement("DIV");
		master.id = name;
		master.className = "master";
		
		detail = document.createElement("DIV");
		detail.className = "detail";
		
		img = document.createElement("IMG");
		img.src = "./images/file/" + type + ".png";
		
		assets = document.createElement("DIV");
		assets.className = "jFiler-item-assets";
		
		info = document.createElement("DIV");
		info.className = "info";
		
		ul = document.createElement("UL");
		
		li = document.createElement("LI");
		li.className = "fileTitle";
		
		para = document.createElement("P");
		name = document.createTextNode(name);
		size = document.createTextNode(size);
		
		br = document.createElement("BR");
		
		master.appendChild(detail);
		master.appendChild(assets);
		
		detail.appendChild(img);
		assets.appendChild(info);
		
		info.appendChild(ul);
		
		ul.appendChild(li);
		li.appendChild(para);
		
		para.appendChild(name);
		para.appendChild(br);
		para.appendChild(size);
		
		$('#filesEdit').append(master);
	});
	console.log('handleFileUpload end');
	console.log('==================================================\n\n');
}
function fileUpload(files,obj){
	console.log('==================================================');
	console.log('fileUpload start');
	console.log('fileBuffer ↓');
	console.log(fileBuffer);
	console.log('files ↓');
	console.log(files);
	
	//reset statusbarDiv
	$('.statusbarDiv').html('');

	if($('input[name="ec-radio-group"]:checked').length == 0){
		$('#message').find("#text").val('').html("請選擇平台!<br><br>才能進行拋轉動作!");
		message_dialog.dialog("open");
		return false;
	}
	if(fileBuffer.length === 0){
		$('#message').find("#text").val('').html("請選擇檔案!<br><br>才能進行拋轉動作!");
		message_dialog.dialog("open");
		return false;
	}
	//判斷副檔名是否存在	 false為不存在
	var ext_exist = false;
	var ext;
	var accept = $('input[name="type-radio-group"]:checked').attr('restrict').split(',');
	console.log('accept');
	console.log(accept);
	//判斷副檔名是否允許
	for (var i = 0; i < fileBuffer.length; i++){
        var filename = fileBuffer[i].name;
        ext = ""+(filename.slice((filename.lastIndexOf(".") - 1 >>> 0) + 2));

        for(var j= 0; j<accept.length; j++){
        	if(ext == accept[j]){
        		ext_exist = true;
        	}
        }
	}
    if(!ext_exist){
		$('#message').find("#text").val('').html("格式錯誤<br><br>您選擇拋轉的檔案格式為"+ext+"<br><br>可目前選擇的訂單類型，只允許"+accept+"類型的檔案");
		message_dialog.dialog( "option", "width", 'auto' ).dialog("open");
		return false;
    }
	
	//record send count
	sendCount = fileBuffer.length;

	var platform = $('input[name="ec-radio-group"]:checked').val();
	console.log('platform: '+platform);
	console.log($('input[name="ec-radio-group"]:checked'));
	var deliveryMethod = $('#deliveryMethod').val();
	
	for (var i = 0; i < fileBuffer.length; i++){
		console.log('第'+(1+i)+'筆');
        var fd = new FormData();
        
        fd.append('file', files[i]);
        fd.append('action', 'upload');
        fd.append('platform', platform);
        fd.append('deliveryMethod', deliveryMethod);
        
    	console.log('files['+i+']');
    	console.log(files[i]);
        var status = new createStatusbar(obj); //Using this we can set progress.
        status.setFileNameSize(files[i].name,files[i].size);
        
        sendFileToServer(fd,status);
   }
	console.log('fileUpload end');
	console.log('==================================================\n\n');
}

var fileBuffer =[];

function setFiles(files){
	console.log('==================================================');
	console.log('setFiles start');
	console.log('before fileBuffer ↓');
	console.log(fileBuffer);
	var tmp = [];
	Array.prototype.push.apply(fileBuffer, files);
	//fileBuffer.push(tmp);
	console.log('after fileBuffer ↓');
	console.log(fileBuffer);
	console.log('setFiles end');
	console.log('==================================================\n\n');
}
function getFiles(){
	if(fileBuffer.length>1){
		$('#message').find("#text").val('').html("只能選擇一筆檔案拋轉!");
		message_dialog.dialog("open");
		return false;
	}
	console.log('==================================================');
	console.log('getFiles start');
	var input= document.createElement('INPUT');
	
	input.type = "file";
	input.accept = ".csv,.xls,.xml,.pdf";
	//input.multiple = "multiple";
	var files = $(input).context.files;
	
	console.log('getFiles fileBuffer ↓');
	console.log(fileBuffer);
	
	for(var i=0;i<fileBuffer.length;i++){
		files[i] = fileBuffer[i];
	}
	console.log('getFiles files ↓');
	console.log(files);
	console.log('getFiles end');
	console.log('==================================================\n\n');
	return files;
}
function clearFiles(){
	fileBuffer = [];
//	$(btnArea).find('#downloadBtn').remove();
}
function clearAll(){
	fileBuffer = [];
	$('#filesEdit').html('');
	$('#statusbarDiv').html('');
}
var folderName = "";
//build upload button and return this object
function createUpBtn(){
	var uploadBtn= document.createElement('BUTTON');
	var text = document.createTextNode('拋轉');
	uploadBtn.id = "uploadBtn";
	uploadBtn.className = "btn btn-darkblue";
	uploadBtn.appendChild(text);
	
	$(btnArea).find('#uploadBtn').remove();
	$(btnArea).append(uploadBtn);
	
	$(uploadBtn).on("click", function (e) {
    	e.stopPropagation();
        e.preventDefault();
        
        //reset
        sendNames = "";
        sendCount = 0;
        sendCountTime = 0;
        sendStatus = 0;
        
        folderName = getFormatDate();
        var files = getFiles();
        var obj = $(".dragandrophandler");
	    fileUpload(files,obj);
	});
	return uploadBtn;
}
//build clear button and return this object
function createClBtn(){
	var clearBtn= document.createElement('BUTTON');
	var text = document.createTextNode('清除');
	clearBtn.id = "clearBtn";
	clearBtn.className = "btn btn-alert";
	clearBtn.appendChild(text);
	
	$(btnArea).find('#clearBtn').remove();
	$(btnArea).append(clearBtn);
	
	$(clearBtn).on("click", function (e) {
    	e.stopPropagation();
        e.preventDefault();
        clearFiles();
        var clearArr = ['#filesEdit','#statusbarDiv'];
        for(var i=0; i<clearArr.length; i++){
        	$(clearArr[i]).html('');
        }
	});
	return clearBtn;
}
//build the download button, depending on the type and return this object
//function createDlBtn(type){
//	var downloadBtn= document.createElement('A');
//	var text = document.createTextNode('範本下載');
//	downloadBtn.id = "downloadBtn";
//	downloadBtn.className = "btn btn-exec";
//	downloadBtn.href = "./basicDataImport.do?action=download&type=" + type
//	downloadBtn.appendChild(text);
//	
//	$(btnArea).find('#downloadBtn').remove();
// 	$(btnArea).append(downloadBtn);
//
//	return downloadBtn;
//}
function transDate(str){
	str = "" + str;
	if(str.length<2){
		str = "0" + str;
	}
	return str;
}
function getFormatDate(){
	var d = new Date();
	var year = ""+d.getFullYear();
	var month = transDate(d.getMonth() + 1);
	var date = transDate(d.getDate());
	var hours = transDate(d.getHours());
	var minutes = transDate(d.getMinutes());
	var seconds = transDate(d.getSeconds());
	var millisecond = d.getTime()

	return year+month+date+hours+minutes+seconds+millisecond;
}

$(document).ready(function() {

	init();
	
	$("#iconBtns").delegate(":radio[name='ec-radio-group']", "click", function(e) {
		
			var dialog = $("#choose-order-type");
			var key = $(this).val();
			var value = platform_map[key];
			
			if(value != null){
				dialog.html(value).dialog('option','title','選擇訂單類型').dialog("open");
			}else{
				$("#typeImg").remove();
				
				var hidden = $('#deliveryMethod');
				var id = $(this).attr('id');
				var lab = $( "label[for='" + id + "']" );
				var left =  lab.offset().left - lab.width()*1/2.2 +"px";
				var top = $("#iconBtns").offset().top - lab.offset().top + 400 +'px';

				var img = document.createElement('IMG');
				img.id = 'typeImg'
				img.style.position = 'relative';
				img.style.left = left;
				img.style.bottom = top;
		    	img.src= './images/general.png';
		    	
		    	var iconBtns = document.getElementById('iconBtns');

		    	iconBtns.appendChild(img);
		    	
		    	hidden.val('').val('general');
				$('#fileDiv').show();
			}
		});	
	var obj = $(".dragandrophandler");
	
//	var radios = $('input[name=ec-radio-group');
//	for (var i=0; i<radios.length; i++){
//	   if (radios[i].checked){
//		   $('#fileDiv').show();
//	   }else{
//		   $('#fileDiv').hide();
//	   }
//	}
	$('input[type=radio]').on('change', function() {
		clearAll();
		if(this.value != 0){
			$('#fileDiv').show();
			var type = $('#select-type').val();
//			createDlBtn(type);
		}else{
			$('#fileDiv').hide();
		}
	});
	
	obj.on('dragenter', function (e) 
	{
	    e.stopPropagation();
	    e.preventDefault();
	    //$(this).css('border', '2px solid #0B85A1');
	});
	obj.on('dragover', function (e) 
	{
	     e.stopPropagation();
	     e.preventDefault();
	});
	obj.on('drop', function (e) 
	{
	     //$(this).css('border', '2px dotted #0B85A1');
	     e.preventDefault();
	     var files = e.originalEvent.dataTransfer.files;
	     createUpBtn();
	     createClBtn();
	     console.log('drop files:');
	     console.log(files);
	     //need to send dropped files to Server
	     handleFileUpload(files);
	     setFiles(files);
	});
	obj.on('click', function (e) 
	{
	    e.stopPropagation();
	    e.preventDefault();
	
	    var fileInput= document.createElement('INPUT');
	    
	    fileInput.type = "file";
	    fileInput.accept = ".csv,.xls,.xml,.pdf";
	    //fileInput.multiple = "multiple";
	    
	    $(fileInput).trigger('click');
	
		var files;
	
	    $(fileInput).on("change", function (e) {
	    	e.stopPropagation();
	        e.preventDefault();
	
			if(fileInput.files.length!=0){
				files = $(fileInput).context.files;
				
				createUpBtn();
			    createClBtn();
	
			    handleFileUpload(files);
			    setFiles(files)
			}
	    });
	});
	$(document).on('dragenter', function (e) 
	{
	    e.stopPropagation();
	    e.preventDefault();
	});
	$(document).on('dragover', function (e) 
	{
	  e.stopPropagation();
	  e.preventDefault();
	  //obj.css('border', '2px dotted #0B85A1');
	});
	$(document).on('drop', function (e) 
	{
	    e.stopPropagation();
	    e.preventDefault();
	});	
});