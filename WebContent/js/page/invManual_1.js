/**
 * 
 */
		$(function(){
			$("#dialog-invoice .input-date").datepicker("option", "minDate", new Date());
			
			var $dialog = $('#dialog-invoice');
			var $title = $dialog.find('input[name=title]');
			var $unicode = $dialog.find('input[name=unicode]');
			var $address = $dialog.find('input[name=address]');

			$title.autocomplete({
		        minLength: 1,
		        source: function(request, response) {
		        	getInvBuyerData(request, response, "title");
		        },
		        open:function(event){
		            var target = $(event.target); 
		            var widget = target.autocomplete("widget");
		            widget.zIndex(target.zIndex() + 1); 
		        },
		        select: function (event, ui) {
		            var unicode = ui.item.unicode;
		            var address = ui.item.address;
		            
		            $unicode.val(unicode);
		            $address.val(address);
		        }
		    });
			
			$unicode.autocomplete({
		        minLength: 1,
		        source: function(request, response) {
		        	getInvBuyerData(request, response, "unicode");
		        },
		        open:function(event){
		            var target = $(event.target); 
		            var widget = target.autocomplete("widget");
		            widget.zIndex(target.zIndex() + 1); 
		        },
		        select: function (event, ui) {
		            var title = ui.item.title;
		            var address = ui.item.address;
		            
		            $title.val(title);
		          	$address.val(address);
		        }
		    });
		});
