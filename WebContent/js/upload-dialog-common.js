$(function(){
	$('#fileDiv').hide();

	status_dialog =
		$('#status').dialog({
			draggable : true,
			resizable : false,
			autoOpen : false,
			modal : true,
			height: 170,
			width : 200,
		    closeOnEscape: false,
		    open: function(event, ui) {
		        $(".ui-dialog-titlebar-close", ui.dialog | ui).hide();
		    }
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
});