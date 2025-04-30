$(function() {
	$.get('/employeeApi/sessionInfoForTopbar', function(res) {
		if (res.status === 'success') {
			$('#topbarEmpName').text(res.name);
		} else {
			$('#topbarEmpName').text('');
		}
	});
});