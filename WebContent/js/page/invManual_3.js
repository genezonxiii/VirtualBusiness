/**
 * 
 */

		$.extend(jQuery.validator.messages, {
		    required: "必填欄位"
		});
		
		var validator_insert_invoice = $('#dialog-invoice').find('form').validate({
	        rules: {
	            'unicode': {
	                required: true
	            },
	            'title': {
	                required: true
	            },
	            'invoice_no': {
	                required: true
	            },
	            'invoice_date': {
	                required: true
	            },
	            'invoice-tax-type-radio-group': {
	                required: true
	            },
	            'amount': {
	                required: true,
	                number: true
	            },
	            'tax': {
	                required: true,
	                number: true
	            }
	        },
	        errorPlacement: function(error, element) {
	        	error.insertAfter(element.closest("td"));
	    	}
	    });
		
		var validator_insert_invoice_detail = $('#dialog-invoice-detail').find('form').validate({
	        rules: {
	        	'description': {
	                required: true
	            },
	            'price': {
	                required: true,
	                number:true
	            },
	            'quantity': {
	                required: true,
	                digits:true
	            }
	        }
	    });		
		
		var valid_cancel_form = $( "#dialog-invoice-cancel" ).find('form').validate({
			rules: {
				'invoice_cancel_reason': {
					required: true
				}
			}
		});
