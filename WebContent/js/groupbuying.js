var company_count = 25;
var company_row_count = 5;

function init(){
	$('#fileDiv').hide();
	$('#selectDiv').hide();
	buildHiddField();
	buildDialog();
	buildIconBtns();
	buildSelect();
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
		url : "groupbuying.do",
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
	
	$(iconBtns).delegate('label','mouseenter mouseleave', function( event ) {
	    if( event.type === 'mouseenter' ){  
	    	$(this).addClass("on_it");
		}else{
	    	$(this).removeClass("on_it");
	    }
	});
	
	$.ajax({
		type : "POST",
		url : "groupbuying.do",
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

function buildSelect(){
	var type_select, code_select, option, para, text;
	var type_count = 2;
	var type_values_arr = ['0','2'];
	var type_texts_arr = ['請選擇轉出格式','黑貓宅急便'];
	
	type_select = document.createElement('SELECT');
	for(var i=0; i<type_count; i++){
		option = document.createElement('OPTION');
		option.value = type_values_arr[i];
		para = document.createElement('P');
		text = document.createTextNode(type_texts_arr[i]);
		para.appendChild(text);
		option.appendChild(para);
		type_select.appendChild(option);
	}
	var code_count = 24;
	var code_values_arr = ['0','O','A','M','G','D','OG','MG','OS',
	                       'GS','AS','MS','DS','Y','ME','BW','UBG', 
	                       'UW','UST','US','USK','YK','GK','LM'];
	var code_texts_arr = ['請選擇商品代碼','LP28敏立清益生菌(原味)',
	                      'LP28敏立清益生菌(紅蘋果)','LP28敏立清益生菌(蔓越莓)',
	                      'LP28敏立清益生菌(青蘋果)','LP28敏立清益生菌(草莓)',
	                      '金敏立清-多多原味','金敏立清-蔓越莓','欣敏立清多多原味',
	                      '欣敏立清青蘋果多多','欣敏立清紅蘋果多多','欣敏立清蔓越莓多多',
	                      '欣敏立清草莓多多','視立清-葉黃素','美立妍','B立威',
	                      '優必固U!be Good','優必威U!be Well','優比獅壯U!be Stron',
	                      '口腔益生菌U!be Smil','優比思邁U!be Smile','小兒晶','小兒立','樂敏立清'];
	
	code_select = document.createElement('SELECT');
	for(var i=0; i<code_count; i++){
		option = document.createElement('OPTION');
		option.value = code_values_arr[i];
		para = document.createElement('P');
		text = document.createTextNode(code_texts_arr[i]);
		para.appendChild(text);
		option.appendChild(para);
		code_select.appendChild(option);
	}

	$('#selectDiv').html('').append(type_select,code_select);
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
							var top = $("#iconBtns").offset().top - lab.offset().top + 240 +'px';
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
							$('#selectDiv').show();
							$('#fileDiv').show();
						}else{
							$('#fileDiv').hide();
							$('#selectDiv').hide();
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
    var uploadURL ="groupbuying.do"; //Upload URL
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
		            				$('#status').find("#text").val('').html("檔案已上傳完成<br><br>請稍後片刻<br><br>正在進行轉檔作業!");
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
		        	var message = '';
		        	
		        	var duplicate = [];
		        	
			    	if(result=="false"){
			        	sendNames += "<p alert='left'>["+formData.get('file').name+"]</p><br>";
			        	sendStatus ++;
			    	}else{
		            	var json_obj = $.parseJSON(result);
						$.each(json_obj,function(i, item) {
				        	console.log('item '+i);
				        	if(i == 'duplicate'){
				        		if( item.length == 0 ){
					        		duplicate = "";
				        		}else{
					        		duplicate = item.split(',');
				        		}
				        	}
				        	console.log('duplicate');
				        	console.log(duplicate);
						});
			    	}
			    	if ((sendCountTime == sendCount) && (sendStatus == 0)){
			    		var json_obj = $.parseJSON(result);

			    		message += json_obj.successDB ? '<br>產生下載檔: 成功<hr>': '<br>產生下載檔: 失敗<hr>';
			    		message += json_obj.success ? '<br>拋轉: 成功<hr>': '<br>拋轉: 失敗<hr>';
			    	}
			    	
				    if ((sendCountTime == sendCount) && (sendStatus == 0)){
			    		message += ( duplicate.length != 0 ) ? '<br>資料重複，請確認以下訂單編號↓<br><br>': '';
			    		
			    		if(duplicate.length != 0){
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
								if( i%2 == 0 ){
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
							text = document.createTextNode('轉檔失敗');
							
							para.appendChild(text);
							title.appendChild(para);
							title.appendChild(br);
	
							para = document.createElement("P");
							text = document.createTextNode('資料重複，請確認以下訂單編號↓');
	
							para.appendChild(text);
							title.appendChild(para);
							title.appendChild(br);
	
							data.appendChild(table);
							
							$('#message').find("#text").html('').append(message,data);			    			
			    		}else{
					    	$('#message').find("#text").val('').html(message);
			    		}

				    	status_dialog.dialog("close");
						message_dialog.dialog( "option", "width", 'auto' ).dialog("open");
						
						if( $.parseJSON(result).success ){
							$("#download").html("");
			            	var json_obj = $.parseJSON(result);
			            	var file_path = json_obj.download;
			            	var file_name = $('input[name=ec-radio-group]:checked').next('label').find('span').text();
							createDlBtn(file_path,file_name);
						}
			    	}
			    	console.log('duplicate uploadURL ');
			    	console.log(duplicate);
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
		$('#message').find("#text").val('').html("只能選擇一筆檔案轉檔!");
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
		$('#message').find("#text").val('').html("請選擇平台!<br><br>才能進行轉檔動作!");
		message_dialog.dialog("open");
		return false;
	}
	if(fileBuffer.length === 0){
		$('#message').find("#text").val('').html("請選擇檔案!<br><br>才能進行轉檔動作!");
		message_dialog.dialog("open");
		return false;
	}
	//判斷副檔名是否存在	 false為不存在
	var ext_exist = false;
	var ext;
	var accept = $('input[name="ec-radio-group"]:checked').attr('custom-restrict').split(',');
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
		$('#message').find("#text").val('').html("格式錯誤<br><br>您選擇轉檔的檔案格式為"+ext+"<br><br>可目前選擇的訂單類型，只允許"+accept+"類型的檔案");
		message_dialog.dialog( "option", "width", 'auto' ).dialog("open");
		return false;
    }
	
	//record send count
	sendCount = fileBuffer.length;

	var platform = $('input[name="ec-radio-group"]:checked').val();
	console.log('platform: '+platform);
	console.log($('input[name="ec-radio-group"]:checked'));
	var deliveryMethod = $('#deliveryMethod').val();
	var tdpl, pdcd , selectArr=[];
	$('#selectDiv select option:selected').each(function(i,item){
		selectArr[i] = item.value;
	});
	tdpl = selectArr[0];
	pdcd = selectArr[1];
	
	for (var i = 0; i < fileBuffer.length; i++){
		console.log('第'+(1+i)+'筆');
        var fd = new FormData();
        
        fd.append('file', files[i]);
        fd.append('action', 'upload');
        fd.append('platform', platform);
        fd.append('deliveryMethod', deliveryMethod);
        fd.append('tdpl', tdpl);
        fd.append('pdcd', pdcd);
        
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
		$('#message').find("#text").val('').html("只能選擇一筆檔案轉檔!");
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
	var text = document.createTextNode('轉檔');
	uploadBtn.id = "uploadBtn";
	uploadBtn.className = "btn btn-darkblue";
	uploadBtn.appendChild(text);
	
	$(btnArea).find('#uploadBtn').remove();
	$(btnArea).append(uploadBtn);
	
	$(uploadBtn).on("click", function (e) {
    	e.stopPropagation();
        e.preventDefault();
        
        //check type code selected
	    if(!selectedExist()){
			return false;
	    }        
        //reset
        sendNames = "";
        sendCount = 0;
        sendCountTime = 0;
        sendStatus = 0;
        
        folderName = getFormatDate();
        var files = getFiles();
        var obj = $(".dragandrophandler");
	    fileUpload(files,obj);
	    $('#clearBtn').trigger('click');
	});
	return uploadBtn;
}
//build download button and return this object
function createDlBtn(file_path, file_name){
	var downloadBtn= document.createElement('A');
	var text = document.createTextNode('下載');
	downloadBtn.id = "downloadBtn";
	downloadBtn.className = "btn btn-exec";
	downloadBtn.href = "./groupbuying.do?action=download&file_path=" + file_path+"&file_name="+file_name;
	downloadBtn.appendChild(text);
	
	$(btnArea).find('#downloadBtn').remove();
	$(btnArea).append(downloadBtn);
	
	return downloadBtn;
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
        $('#downloadBtn').remove();
	});
	return clearBtn;
}
function transDate(str){
	str = "" + str;
	if(str.length<2){
		str = "0" + str;
	}
	return str;
}
function selectedExist(){
    var selected = false;
    var mes = '';
    
    $('#selectDiv select option:selected').each(function(){
    	if($(this).val() == '0'){
    		selected = false;
    		mes += $(this).text()+ '<br>';
    	}
	});
    
    if((!selected) && mes.length !=0){
    	$('#message').find("#text").val('').html(mes);
		message_dialog.dialog("open");
    }else{
    	selected = true;
    }
    return selected;
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
				var top = $("#iconBtns").offset().top - lab.offset().top + 240 +'px';

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
				$('#selectDiv').show();
			}
		});	
	var obj = $(".dragandrophandler");

	$('input[type=radio]').on('change', function() {
		clearAll();
		if(this.value != 0){
			$('#fileDiv').show();
			$('#selectDiv').show();
			var type = $('#select-type').val();
//			createDlBtn(type);
		}else{
			$('#fileDiv').hide();
			$('#selectDiv').hide();
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