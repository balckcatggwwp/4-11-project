function loadComments() {
	$.ajax({
		url: '/movies/' + movieId + '/comments',
		method: 'GET',
		success: function (comments) {
			let html = '';
			if (comments.length === 0) {
				html = '<p class="text-white">目前尚無留言，成為第一位留言的人吧！</p>';
			} else {
				comments.forEach(comment => {
					html += `
						<div class="card mb-2">
							<div class="card-body">
								<h6 class="card-title">${comment.memberName} <small class="text-muted">${comment.createdAt}</small></h6>
								<p class="card-text">${comment.content}</p>
							</div>
						</div>
					`;
				});
			}
			$('#comment-list').html(html);
		}
	});
}

function postComment() {
	const content = $('#comment').val().trim();
	if (content === '') {
		Swal.fire({
			icon: 'warning',
			title: '請輸入留言內容',
			timer: 2000,
			showConfirmButton: false
		});
		return;
	}

	$.ajax({
		url: '/movies/' + movieId + '/comments',
		method: 'POST',
		contentType: 'application/json',
		data: JSON.stringify({ content: content }),
		success: function () {
			$('#comment').val('');
			loadComments();
			Swal.fire({
				icon: 'success',
				title: '留言成功',
				timer: 1500,
				showConfirmButton: false
			});
		}
	});
}

$(document).ready(function () {
	loadComments();
});