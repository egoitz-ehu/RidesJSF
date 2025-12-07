function egunakAztertu(date) {
	const availableDates = document.getElementById('searchForm:activeDatesData');
	let availableDatesStr = availableDates.value;
	availableDatesStr = availableDatesStr.replace(/[\[\]]/g, '');
	const availableDatesArray = availableDatesStr
		? availableDatesStr.split(',').map(date => date.trim()).filter(date => date.length > 0)
		: [];
	const formattedDate = $.datepicker.formatDate('yy-mm-dd', date);
	const isEnabled = availableDatesArray.includes(formattedDate);
	return [isEnabled];
}
function reloadCalendarDates() {
    console.log("=== Reloading calendar dates ===");

    requestAnimationFrame(function() {
        setTimeout(function() {
            var activeDatesStr = $('#searchForm\\:activeDatesData').val();
            console.log("Active dates string:", activeDatesStr);

            var calendarId = 'searchForm:rideDate_inline';
            var calendarElement = $('#' + calendarId.replace(/:/g, '\\:'));
            
            if (calendarElement.length) {
                calendarElement.datepicker('refresh');
                
                calendarElement.datepicker('option', 'beforeShowDay', egunakAztertu);
                
                console.log("Calendar dates reloaded");
            }
        }, 100);
    });
}
