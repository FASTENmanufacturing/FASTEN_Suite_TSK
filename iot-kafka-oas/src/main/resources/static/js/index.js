function enable_get_probability_distribuitions(){
	if ($('#enable_get_probability_distribuitions').hasClass('disabled')) {
        return false;
    } 
    $.post( "/enable_get_probability_distribuitions", function(data) {
    		$.notify("Enabled","success");
    }).always(function( jqXHR, status ) {
    		$('#enable_get_probability_distribuitions').addClass('disabled');
    		$('#disable_get_probability_distribuitions').removeClass('disabled');
	});
}

function disable_get_probability_distribuitions(){
	if ($('#disable_get_probability_distribuitions').hasClass('disabled')) {
        return false;
    }
    $.post( "/disable_get_probability_distribuitions", function(data) {
    		$.notify("Disabled","info");
    }).always(function( jqXHR, status ) {
    		$('#disable_get_probability_distribuitions').addClass('disabled');
		$('#enable_get_probability_distribuitions').removeClass('disabled');
	});
}

function enable_return_historical_demand(){
	if ($('#enable_return_historical_demand').hasClass('disabled')) {
        return false;
    } 
    $.post( "/enable_return_historical_demand", function(data) {
    		$.notify("Enabled","success");
    }).always(function( jqXHR, status ) {
    		$('#enable_return_historical_demand').addClass('disabled');
    		$('#disable_return_historical_demand').removeClass('disabled');
	});
}

function disable_return_historical_demand(){
	if ($('#disable_return_historical_demand').hasClass('disabled')) {
        return false;
    }
    $.post( "/enable_return_historical_demand", function(data) {
    		$.notify("Disabled","info");
    }).always(function( jqXHR, status ) {
    		$('#disable_return_historical_demand').addClass('disabled');
		$('#enable_return_historical_demand').removeClass('disabled');
	});
}