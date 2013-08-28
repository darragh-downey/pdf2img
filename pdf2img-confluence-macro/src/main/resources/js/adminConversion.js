$(document).ready(function(){
	$('#spaces').//fill
	$('#spaces').change(function() { fillOptions('pages', this);});
	$('#pages').change(function() { fillOptions('attachments', this);});
});

function fillOptions(ddId, callingElement){
	var dd = $('#' + ddId);
	$.getJSON('json/options?dd=' + ddId + '&val=' + $(callingElement).val(), function(opts){
		$('>option', dd).remove(); //clean old options first.
		if(opts){
			$.each(opts, function(key, value){
				dd.append($('<option/>').val(key).text(value));
			});
		}else{
			dd.append($('<option/>').text("Please select parent"));
		}
	});
}

function initFill(){
	var servletUrl = AJS.contextPath() + "/pdf2img-servlet"; //get base url + append servlet location
	$.get(servletUrl, function(responseJson){
		var $select = $("#selectSpaces");
		$select.find('option').remove();
		$.each(responseJson, function(key, value){
			$('<option>').val(key).text(value).appendTo($select);
		});
	});
}