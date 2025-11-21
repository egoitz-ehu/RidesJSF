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