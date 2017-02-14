<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="template.jsp" flush="true"/>

<!DOCTYPE html>
<html>
<head>	
	<title>產品對照資料管理</title>
	
	<style>
		.custom-combobox {
		  position: relative;
		  display: inline-block;
		}
		.custom-combobox-toggle {
		  position: absolute;
		  top: 0;
		  bottom: 0;
		  margin-left: -1px;
		  padding: 0;
		}
		.custom-combobox-input {
		  margin: 0;
		  padding: 5px 10px;
		}
		#dialog-select-table td {
		    text-align: center; /* center checkbox horizontally */
		    vertical-align: middle; /* center checkbox vertically */
		}
		#product-contrast-table td {
		    text-align: center; /* center checkbox horizontally */
		    vertical-align: middle; /* center checkbox vertically */
		}		
		#product-contrast-table label{
		    left:65px;
		}
	</style>
	
	<script type="text/javascript" src="js/jquery-1.11.4.js"></script>
	<script type="text/javascript" src="js/jquery-ui.min.js"></script>
	<script type="text/javascript" src="js/jquery.validate.min.js"></script>
	<script type="text/javascript" src="js/jquery.dataTables.min.js"></script>
	
	<script>
	$( function() {
		
		var table; // for search datatable
		var buttonCount = 0; //button clicks
	
		$.widget( "custom.combobox", {
		  _create: function() {
		    this.wrapper = $( "<span>" )
		      .addClass( "custom-combobox" )
		      .insertAfter( this.element );
		
		    this.element.hide();
		    this._createAutocomplete();
		    this._createShowAllButton();
		  },
		
		  _createAutocomplete: function() {
		  	var nowSelect = this.element.context.id;
		  	
		  	console.log("load: ");
		  	console.log(this.element.context.id);
		  	console.log("nowSelect: ");
		  	console.log(nowSelect);
		  	
			$.ajax({
			    url : "productContrast.do",
			    type : "POST",
			    cache : false,
			    delay : 0,
			    data : {
			    	action : this.element.context.id
			    },
			    success: function(data) {
			    	var json_obj = $.parseJSON(data);
					var resultRunTime = 0;
					$.each (json_obj, function (i) {
						resultRunTime+=1;
					});
					if(resultRunTime!=0){
						$.each(json_obj,function(i, item) {
							if(nowSelect === 'search-product-product-name'){
								$('#'+nowSelect).append($("<option></option>").attr("value", json_obj[i].product_name).text(json_obj[i].product_name));
							}else if(nowSelect === 'search-contrast-platform-name'){
								$('#'+nowSelect).append($("<option></option>").attr("value", json_obj[i].productNamePlatform).text(json_obj[i].productNamePlatform));
							}else if(nowSelect === 'search-platform-platform-name'){
								$('#'+nowSelect).append($("<option></option>").attr("value", json_obj[i].platform_name).text(json_obj[i].platform_name));
							}else if(nowSelect === 'dialog_platform_platform_name'){
								$('#'+nowSelect).append($("<option></option>").attr("value", json_obj[i].platform_id).text(json_obj[i].platform_name));
							}
						});
					}
			    }
			});
		  	
		    var selected = this.element.children( ":selected" ),
		      value = selected.val() ? selected.text() : "";
		
		    this.input = $( "<input>" )
		    	.attr('id', this.element.context.id + '_combobox')
		         .attr('name', this.element.context.name + '_combobox')
		      .appendTo( this.wrapper )
		      .val( value )
		      .attr( "title", "" )
		      .addClass( "custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left" )
		      .autocomplete({
		        delay: 0,
		        minLength: 0,
		        source: $.proxy( this, "_source" )
		      })
		      .tooltip({
		           position: {
		               of: this.button
		           },
		        classes: {
		          "ui-tooltip": "ui-state-highlight"
		        }
		      });
		
		    this._on( this.input, {
		      autocompleteselect: function( event, ui ) {
		        ui.item.option.selected = true;
		        this._trigger( "select", event, {
		          item: ui.item.option
		        });
		      },
		      autocompletechange: "_removeIfInvalid"
		    });
		  },
		
		  _createShowAllButton: function() {
		    var input = this.input,
		      wasOpen = false;
		
		    $( "<a>" )
		      .attr( "tabIndex", -1 )
		      .attr( "title", "查看全部" )
		      .tooltip()
		      .appendTo( this.wrapper )
		      .button({
		        icons: {
		          primary: "ui-icon-triangle-1-s"
		        },
		        text: false
		      })
		      .removeClass( "ui-corner-all" )
		      .addClass( "custom-combobox-toggle ui-corner-right" )
		      .on( "mousedown", function() {
		        wasOpen = input.autocomplete( "widget" ).is( ":visible" );
		      })
		      .on( "click", function() {
		        input.trigger( "focus" );
		
		        // Close if already visible
		        if ( wasOpen ) {
		          return;
		        }
		
		        // Pass empty string as value to search for, displaying all results
		        input.autocomplete( "search", "" );
		      });
		  },
		
		  _source: function( request, response ) {
			  
		    var matcher = new RegExp( $.ui.autocomplete.escapeRegex(request.term), "i" );
		    response( this.element.children( "option" ).map(function() {
		      var text = $( this ).text();
		      if ( this.value && ( !request.term || matcher.test(text) ) )
		        return {
		          label: text,
		          value: text,
		          option: this
		        };
		    }) );
		  },
		
		  _removeIfInvalid: function( event, ui ) {
		
		    // Selected an item, nothing to do
		    if ( ui.item ) {
		      return;
		    }
		
		    // Search for a match (case-insensitive)
		    var value = this.input.val(),
		      valueLowerCase = value.toLowerCase(),
		      valid = false;
		    this.element.children( "option" ).each(function() {
		      if ( $( this ).text().toLowerCase() === valueLowerCase ) {
		        this.selected = valid = true;
		        return false;
		      }
		    });
		
		    // Found a match, nothing to do
		    if ( valid ) {
		      return;
		    }
		
		    // Remove invalid value
		    this.input
		      .val( "" )
		      .attr( "title", value + " 並不存在" )
		      .tooltip( "open" );
		    this.element.val( "" );
		    this._delay(function() {
		      this.input.tooltip( "close" ).attr( "title", "" );
		    }, 2500 );
		    this.input.autocomplete( "instance" ).term = "";
		  },
		
		  _destroy: function() {
		    this.wrapper.remove();
		    this.element.show();
		  }
		});
		
		$( ".toggles" ).controlgroup({
		    direction: "vertical"
	  	});

		//search
		$( "#search-product-contrast" ).click(function(e) {
			e.preventDefault();
			var oData = {
					"action": "search-product-contrast",
					"product_name": $('#search-product-product-name ').val(),
					"product_name_platform": $('#search-contrast-platform-name').val(),
					"platform_name": $('#search-platform-platform-name').val()
				};

			$("#product-contrast-table").dataTable().fnDestroy();
			$("#product-contrast-table").show();
			
			table = $("#product-contrast-table").DataTable({
						dom: 'lfrB<t>ip',
						paging: true,
						ordering: false,
			       		info: false,
						language: {"url": "js/dataTables_zh-tw.txt"},
						ajax: {
								url : "productContrast.do",
								dataSrc: "",
								type : "POST",
								data : oData
						},
				        columnDefs: [
				                     {
			                    	   targets: 0,
			                           searchable: false,
			                           orderable: false,
			                           render: function ( data, type, row ) {
			                        	   var ch =
			                        		 	"<input type='checkbox' name='checkbox-group-select' id = '" + row.contrastId + "'>" +
			                        	   		"<label for='" + row.contrastId + "'><span class='form-label'>選取</span></label>";
			                        	   
					                       return ch;
			                           }
		        					},
		        		            {
		        		                targets:  8 ,
		        		                visible: false,
		        		                searchable: false
		        		            },
		        		            {
		        		                targets:  9 ,
		        		                visible: false,
		        		                searchable: false
		        		            },		        					
					        		{
				                    	   targets: -1,
				                           searchable: false,
				                           orderable: false,
				                           render: function ( data, type, row ) {
				                        	   var ch =
				                   				    "<div class='table-row-func btn-in-table btn-gray'><i class='fa fa-ellipsis-h'></i>"+
				                   					"	<div class='table-function-list' >"+
				                   					"		<button class='btn-in-table btn-darkblue btn_update' title='修改' id = '" + row.contrastId + "'>" +
				                   							"<i class='fa fa-pencil'></i></button>"+
				                   					"		<button class='btn-in-table btn-alert btn_delete' title='刪除' id = '" + row.contrastId + "'>" +
				                   					"<i class='fa fa-trash'></i></button>"+
				                   					"	</div>"+
				                   					"</div>";
				                        	   
						                       return ch;
				                           }
		        					}				        
				        ],
						columns: [
								  {
									  "width":"190",
									  "left":"50%",
									  "className": "selectBox",
									  "data": null,
									  "defaultContent": ""
								  },
						          {"data": "productName" ,"defaultContent":""},
						          {"data": "product_spec" ,"defaultContent":""},
						          {"data": "contrast_type" ,"defaultContent":""},
						          {"data": "platform_name" ,"defaultContent":""},
						          {"data": "productNamePlatform" ,"defaultContent":""},
						          {"data": "product_spec_platform" ,"defaultContent":""},
						          {"data": "amount" ,"defaultContent":""},
						          {"data": "platformId" ,"defaultContent":""},
						          {"data": "productId" ,"defaultContent":""},
						          {"data": null ,"defaultContent":""}
						]				      	      
					});
		});

		$("#product-contrast-table").hide();
		$("#search-product-product-name").combobox();
		$("#search-contrast-platform-name").combobox();
		$("#search-platform-platform-name").combobox();

		//insert_dialog - insert and update will be used
		insert_dialog =
			$("#dialog-form-insert").dialog({
				draggable : true,
				resizable : false,
				autoOpen : false,
				height : "auto",
				width : "auto",
				modal : true,
				show : {effect : "blind",duration : 300},
				hide : {effect : "fade",duration : 300},
				width : 800,
				buttons : [{
							id : "insert_dialog_btn_0",
							click : function(e) {
								
								e.preventDefault();
								
								var action = $(this).data("action");
								
								console.log("----------------action params----------------");
								console.log(action);
								console.log("---------------------------------------------");
								
								if ($('#dialog-form-post-insert').valid()) {
																
									if(action === "insert"){
										$.ajax({
										    url : "productContrast.do",
										    type : "POST",
										    cache : false,
										    delay : 0,
										    data : {
										    	action : action,
										    	product_id : $(this).data("product_id"),
										    	platform_id : $("#dialog_platform_platform_name").val(),
										    	contrast_type : $("input[name='radio-group-type']:checked").val(),
										    	product_name_platform : $("#dialog_contrast_platform_name").val(),
										    	product_spec_platform : $("#contrast_spec_platform_name").val(),
										    	amt : $("#dialog_amt").val()
										    	
										    },
										    success: function(data) {
										    	$( "#search-product-contrast" ).trigger( "click" );
												$("#dialog-form-post-insert").trigger("reset");
												insert_dialog.dialog("close");
										    }
										});										
									}else if(action === "update"){
										
						    			console.log("product_id: " + $(this).data("product_id"));
				    					console.log("contrast_id: " + $(this).data("contrast_id"));
		    							console.log("platform_id: " + 
		    									($("#dialog_platform_platform_name").val()==undefined ?
		    									$("#dialog_platform_platform_name").val():
		    										$(this).data("platform_id")));
   										console.log("contrast_type: " + $("input[name='radio-group-type']:checked").val());
										console.log("product_name_platform: " + $("#dialog_contrast_platform_name").val());
										console.log("product_spec_platform: " + $("#contrast_spec_platform_name").val());
										console.log("amt: " + $("#dialog_amt").val());
										console.log("---------------------------------------------");
										
										$.ajax({
										    url : "productContrast.do",
										    type : "POST",
										    cache : false,
										    delay : 0,
										    data : {
										    	action : action,
										    	product_id : $(this).data("product_id"),
										    	contrast_id : $(this).data("contrast_id"),
										    	platform_id :(
											    				$("#dialog_platform_platform_name").val()==undefined ?
											    				$(this).data("platform_id"):
					    										$("#dialog_platform_platform_name").val()
				    										),
										    	contrast_type : $("input[name='radio-group-type']:checked").val(),
										    	product_name_platform : $("#dialog_contrast_platform_name").val(),
										    	product_spec_platform : $("#contrast_spec_platform_name").val(),
										    	amt : $("#dialog_amt").val()
										    	
										    },
										    success: function(data) {
										    	$( "#search-product-contrast" ).trigger( "click" );
												$("#dialog-form-post-insert").trigger("reset");
												insert_dialog.dialog("close");
										    }
										});											
									}
				
								}
							}
						}, {
							text : "取消",
							click : function() {
								$("#dialog-form-post-insert").trigger("reset");
								insert_dialog.dialog("close");
							}
						} ],
				close : function() {
					$("#dialog-form-post-insert").trigger("reset");
				}
		});
		
		//select_dialog
		select_dialog =
			$("#dialog-form-select").dialog({
				title : "產品/組合包選擇",
				draggable : true,
				resizable : false,
				autoOpen : false,
				height : "auto",
				width : "auto",
				modal : true,
				show : {effect : "blind",duration : 300},
				hide : {effect : "fade",duration : 300},
				width : 800,
				buttons : [{
							id : "insert",
							text : "確認",
							click : function(e) {
								
								e.preventDefault();
								
								if($("input[name='radio-group-select']:checked").length === 0){
									$("#tip-text").text("請選擇一筆資料，才能進行步驟 !");
									$("#dialog-form-tips").dialog({
										title : "提醒訊息",
										resizable: false,
										 modal: true
									});
								}else{
									var product_id = $("input[name='radio-group-select']:checked").attr("id");
									var product_name = $("input[name='radio-group-select']:checked").val().toString().split(";")[0];
									var product_desc = $("input[name='radio-group-select']:checked").val().toString().split(";")[1];
									
									$("#dialog_product_product_name").val(product_name).attr("title",product_name).tooltip();
									$("#dialog_product_product_desc").val(product_desc).attr("title",product_desc).tooltip();
									
									$("#dialog-form-insert").data("product_id",product_id);
									
									select_dialog.dialog("close");
								}
							}
						}, {
							text : "取消",
							click : function() {
								    
								var $radios = $("input[name='radio-group-type']");
							    if($radios.is(':checked') === true) {
							    	$radios.prop('checked', false);
							    }							
								$("#dialog-form-post-select").trigger("reset");
								select_dialog.dialog("close");
							}
						} ],
				close : function() {
					select_dialog.dialog("close");
					$("#dialog-form-post-select").trigger("reset");
				}
		});
		
		//for insert and update
		function initGeneralEnvironment(){
			var checkType =  
				"<td>對照類別</td>" +
				"<td>" +
				"<section>" +
					"<input id='radio-1' type='radio' name='radio-group-type' value='PRD'>" +
					"<label for='radio-1'><span class='form-label'>產品</span></label>" +
					
					"<input id='radio-2' type='radio' name='radio-group-type' value='PKG'>" +
					"<label for='radio-2'><span class='form-label'>組合包</span></label>" +
				"</section>" +
				"</td>";

			var prodName =  
				"<td>產品名稱</td>"+ 
				"<td>"+ 
						"<input type='text' title='' id='dialog_product_product_name' name='dialog_product_product_name' placeholder='請選擇類別' disabled>"+
				"</td>";
			
			var prodSpec =  
				"<td>產品規格</td>"+ 
				"<td>"+ 
						"<input type='text' title='' id='dialog_product_product_desc' name='dialog_product_product_desc' placeholder='請選擇類別' disabled>"+
				"</td>";		
				
			var conPlatName =  
				"<td>平台用產品名稱</td>"+ 
				"<td>"+ 
						"<input type='text' id='dialog_contrast_platform_name' name='dialog_contrast_platform_name' placeholder='輸入平台用產品名稱'>"+
				"</td>";

			var specPlatName =  
				"<td>平台用產品規格</td>"+ 
				"<td>"+ 
						"<input type='text' id='contrast_spec_platform_name' name='contrast_spec_platform_name' placeholder='輸入平台用產品規格'>"+
				"</td>";
					
			var platName =  
				"<td>平台</td>"+ 
				"<td>"+ 
						"<select id='dialog_platform_platform_name' name='dialog_platform_platform_name' disabled>"+
							"<option value=''></option>"+
						"</select>"+
				"</td>";
							
			var amount =  
				"<td>應收金額</td>"+ 
				"<td>"+ 
						"<input type='text' id='dialog_amt' name='dialog_amt' placeholder='輸入應收金額' >"+
				"</td>";

			$("#dialog-insert-table").find('tr').remove();
			$("#dialog-insert-table").append($('<tr></tr>').val('').html(checkType));
			$("#dialog-insert-table").append($('<tr></tr>').val('').html(prodName));
			$("#dialog-insert-table").append($('<tr></tr>').val('').html(prodSpec));
			$("#dialog-insert-table").append($('<tr></tr>').val('').html(platName));
			$("#dialog-insert-table").append($('<tr></tr>').val('').html(conPlatName));
			$("#dialog-insert-table").append($('<tr></tr>').val('').html(specPlatName));
			$("#dialog-insert-table").append($('<tr></tr>').val('').html(amount));
			
			// for set inpit ui
			function setBoxUi(elements){
				
				$.each(elements, function(i, val) {
					$("#"+val).addClass("custom-combobox-input ui-widget ui-widget-content ui-state-default ui-corner-left").width(235);
				});
			}
		
			var elements = ["dialog_amt","dialog_contrast_platform_name","dialog_product_product_name","dialog_product_product_desc","contrast_spec_platform_name"];
			
			$("#dialog_platform_platform_name").combobox();
			
			setBoxUi(elements);

			$("#checkType").selectmenu();
			
			$.validator.addMethod("chkFloat", function(value, element, params) {
			    var val = $.trim(value);
			    if (val.length === 0) return true;
			    if (parseFloat(val) == val) {
			        var values = val.split('.');
			        if (values[0].length <= params[0]) {
			            if (values.length > 1) {
			                return values[1].length <= params[1];
			            }
			            return true;
			        }
			    }
			    return false;
			}, jQuery.validator.format("只接受{0}位正整數及{1}位小數點"));
			
			$.extend(jQuery.validator.messages, {
			    required: "必填欄位",		
			    number: "請輸入數字"
			});
			
			$( "#dialog-form-post-insert" ).validate({
			    errorPlacement: function(error, element) {
			    	element.before(error);
			  	},
			  	rules:{
			  		dialog_amt :{
			  			number:true,
				  		chkFloat: [6,2]
			  		}
			  	}
			 });
			
			$("#dialog-form-post-insert input[id^=dialog_]").each(function(){
				$(this).rules("add", {
				  	required: true
				});
			});	
			$("#dialog-form-post-insert input[name=radio-group-type]").each(function(){
				$(this).rules("add", {
				  	required: true
				});
			});
			
			$("input[name='radio-group-type']").on( "click", function(e) {
				
	
				var contrast_type = $("input[name='radio-group-type']:checked").val();
				
				$("#dialog-select-table").dataTable().fnDestroy();
				table = $("#dialog-select-table").DataTable({
						dom: 'lfrB<t>ip',
						paging: true,
						ordering: false,
			       		info: false,           
			       		bLengthChange: false,
			       		pageLength: 5,
						show : {effect : "blind",duration : 300},
						hide : {effect : "fade",duration : 300},
						language: {"url": "js/dataTables_zh-tw.txt"},
						ajax: {
								url : "productContrast.do",
								dataSrc: "",
								type : "POST",
								data : {
									"action": "search-product-type-list",
									"type": contrast_type
								}
						},
				        columnDefs: [{
			                    	   targets: 0,
			                           searchable: false,
			                           orderable: false,
			                           render: function ( data, type, row ) {
			                        	   var rad =
			                        		 	"<input type='radio' name='radio-group-select' value='" + row.name + ";" + row.desc + "' id = '" + row.id + "'>" +
			                        	   		"<label for='" + row.id + "'><span class='form-label'>選取</span></label>";
			                        	   
					                       return rad;
			                           },
				                           "targets": 0
				        }],
						columns: [
						         
								  {"data": null ,"defaultContent": ""},
						          {"data": "name" ,"defaultContent":""},
						          {"data": "desc" ,"defaultContent":""},
						]						      	      
					});
				select_dialog.dialog("open");
			});			
		} 
		initGeneralEnvironment();
		
		//insert	
		$("#create-product-contrast").click(function(e){
			e.preventDefault();		
			
		$("#insert_dialog_btn_0").button("option","label","新增");
		
		insert_dialog
				.data("action","insert")
				.dialog("option","title","新增資料")
				.dialog("open");
		});		
		
		//update
		$("#product-contrast-table").delegate(".btn_update", "click", function(e) {

			e.preventDefault();		
			var row = jQuery(this).closest('tr');
			
		    var data = $("#product-contrast-table").dataTable().fnGetData(row);
		    console.log(data);
			
			// get data from row
			var amount = data.amount;

			var contrastId = data.contrastId;

			var contrast_type = data.contrast_type;

			var platform_name = data.platform_name;

			var productName = data.productName;

			var productNamePlatform = data.productNamePlatform;

			var product_spec = data.product_spec;
			
			var product_spec_platform = data.product_spec_platform;
			
			var product_id = data.productId;

			var platform_id = data.platformId;
			
			//contrast_type	
			var $radios = $("input[name='radio-group-type']");
			
			$radios.each(function(i){
				if($(this).val() === contrast_type){
					$(this).prop('checked', true);
				}
			});
			//productName
			$("#dialog_product_product_name").val(productName);
			
			//product_spec
			$("#dialog_product_product_desc").val(product_spec);
			
			//platform_name
			$("#dialog_platform_platform_name_combobox").val(platform_name).attr('selected', true);
			
			
			//productNamePlatform
			$("#dialog_contrast_platform_name").val(productNamePlatform);
			
			//product_spec_platform
			$("#contrast_spec_platform_name").val(product_spec_platform);
			
			//amount
			$("#dialog_amt").val(amount);
			
			
			var contrast_id = $(this).attr("id");
			console.log(contrast_id);
			
			$("#insert_dialog_btn_0").button("option","label","修改");
			
			insert_dialog
			.data("action","update")
			.data("contrast_id",contrast_id)
			.data("platform_id",platform_id)
			.data("product_id",product_id)
			.dialog("option","title","修改資料")
			.dialog("open");			
		});
		
		// button listener
		$("button[id^='selectAll']").click(function(e){
			e.preventDefault();
			
			buttonCount++;
			
			if(buttonCount%2 === 1){
				$("input[name='checkbox-group-select']").each(function() {
				    $(this).prop("checked", true);
				});				
			}else{
				$("input[name='checkbox-group-select']").each(function() {
				    $(this).prop("checked", false);
				});
			}

		});
		
		$("button[id^='batchDel_']").click(function(e){
			e.preventDefault();
			
			var contrast_ids = "";
			
			//initialization
			contrast_ids.length = 0;
			
			if($("input[name='checkbox-group-select']:checked").length != 0){
				$("input[name='checkbox-group-select']:checked").each(function(i){
					contrast_ids += $(this).context.id + ";";
				});
				
				contrast_ids = contrast_ids.substring(0,contrast_ids.length - 1);
				console.log(contrast_ids);
				
				$.ajax({
				    url : "productContrast.do",
				    type : "POST",
				    dataType: "json",
				    cache : false,
				    delay : 0,
				    data : {
				    	action : "batchDelete",
				    	contrast_ids : contrast_ids
				    	
				    },
				    success: function(data) {
				    	
				    	if(data.message === "success"){
					    	table.ajax.reload();
				    	}
				    }
				});
			}
		});
		
		//delete
		$("#product-contrast-table").delegate(".btn_delete", "click", function(e) {
			
			e.preventDefault();
			
			var id = $(this).attr("id");
			console.log(id);
			delete_dialog.data('contrast_id', id).dialog("open");
		})
		
		delete_dialog =
			$("#dialog-form-delete").dialog({
				title : "確認刪除資料嗎?",
				draggable : true, resizable : false, autoOpen : false,
				height : "auto", width : "auto", modal : true,
				show : {effect : "blind",duration : 300},
				hide : {effect : "fade",duration : 300},
				width : 200,
				buttons : [{
							id : "delete",
							text : "確認",
							click : function(e) {
								
								e.preventDefault();
								
								if ($('#dialog-form-post-delete').valid()) {
									$.ajax({
									    url : "productContrast.do",
									    type : "POST",
									    cache : false,
									    delay : 0,
									    data : {
									    	action : "delete",
									    	contrast_id : $(this).data("contrast_id")
									    	
									    },
									    success: function(data) {
									    	table.ajax.reload();
									    	delete_dialog.dialog("close");
									    }
									});				
								}
							}
						}, {
							text : "取消",
							click : function() {
								$("#dialog-form-post-delete").trigger("reset");
								delete_dialog.dialog("close");
							}
						} ],
				close : function() {
					$("#dialog-form-post-delete").trigger("reset");
				}
		});	
	});
	

</script>
</head>
<body>
	<div class="content-wrap" >
		<!-- 查詢 -->
		<div class="input-field-wrap">
			<div class="form-wrap">			
				<div class="form-row">
					<label for="">
						<span class="block-label">產品名稱</span>
		 				<select id="search-product-product-name">
		 					<option value=""></option>
		 				</select>
					</label>
				</div>
				<div class="form-row">
					<label for="">
						<span class="block-label">平台用產品名稱</span>
		 				<select id="search-contrast-platform-name">
		 					<option value=""></option>
		 				</select>
					</label>
				</div>
				<div class="form-row">
					<label for="">
						<span class="block-label">平台</span>
		 				<select id="search-platform-platform-name">
		 					<option value=""></option>
		 				</select>
					</label>
				</div>
				<div class="form-row">
					<button class="btn btn-darkblue" id="search-product-contrast">查詢</button>
					<button class="btn btn-exec btn-wide" id="create-product-contrast">新增</button>
				</div>										
			</div>
		</div>

		<div class="row search-result-wrap" align="center">
			<div class="ui-widget">
				<table id="product-contrast-table" class="result-table">
					<thead>
						<tr>
							<th colspan="1">
								<div style="display:inline">
									<button class="btn btn-darkblue" id="selectAllHead">全選</button>
								</div>
								<div style="display:inline">
									<button class="btn btn-darkblue" id="batchDel_head">批次刪除</button>			
								</div>
							</th>						
							<th>產品名稱</th>
							<th>產品規格</th>
							<th>產品類別</th>
							<th>平台名稱</th>
							<th>平台用產品名稱</th>
							<th>平台用產品規格</th>
							<th>應收金額</th>
							<th>platformId</th>
							<th>productId</th>
							<th>功能</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th colspan="1">
								<div style="display:inline">
									<button class="btn btn-darkblue" id="selectAllFoot">全選</button>
								</div>
								<div style="display:inline">
									<button class="btn btn-darkblue" id="batchDel_foot">批次刪除</button>			
								</div>
							</th>						
							<th>產品名稱</th>
							<th>產品規格</th>
							<th>產品類別</th>
							<th>平台名稱</th>
							<th>平台用產品名稱</th>
							<th>平台用產品規格</th>
							<th>應收金額</th>
							<th>platformId</th>
							<th>productId</th>
							<th>功能</th>
						</tr>
					</tfoot>
					<tbody>
					</tbody>
				</table>
			</div>
		</div>	
	</div>
	<!--提醒對話窗 -->
	<div id="dialog-form-tips" style="display:none;">
		<div id="tip-text"></div>
	</div>	
	<!--新增對話窗 -->
	<div id="dialog-form-insert" style="display:none;">
		<form name="dialog-form-post-insert" id="dialog-form-post-insert">
			<fieldset>
				<table class="form-table" id="dialog-insert-table">
				</table>
			</fieldset>
		</form>
	</div>
	<!--修改對話窗 -->
	<div id="dialog-form-update" style="display:none;">
		<form name="dialog-form-post-update" id="dialog-form-post-update">
			<fieldset>
				<table class="form-table" id="dialog-update-table">
				</table>
			</fieldset>
		</form>
	</div>	
	<!--刪除對話窗 -->
	<div id="dialog-form-delete" style="display:none;">
		<form name="dialog-form-post-delete" id="dialog-form-post-delete">
			<fieldset>
				<table class="form-table" id="dialog-delete-table">
				</table>
			</fieldset>
		</form>
	</div>
	<!--選取商品對話窗 -->
	<div id="dialog-form-select" style="display:none;">
		<form name="dialog-form-post-select" id="dialog-form-post-select">
			<fieldset>
				<table class="form-table" id="dialog-select-table">
					<thead>
						<tr>
							<th></th>
							<th>產品名稱</th>
							<th>規格名稱</th>
						</tr>
					</thead>
					<tfoot>
						<tr>
							<th></th>
							<th>產品名稱</th>
							<th>規格名稱</th>
						</tr>
					</tfoot>
					<tbody>
					</tbody>
				</table>
			</fieldset>
		</form>
	</div>	
</body>
</html>