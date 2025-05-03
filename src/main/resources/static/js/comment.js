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
          let formattedTime = comment.createdAt.replace('T', ' ').substring(0, 16);
          let buttonsHtml = '';
          if (currentUser === comment.memberName) {
            buttonsHtml = `
              <div class="text-end mt-2">
                <button class="btn btn-sm btn-outline-light me-2" onclick="editComment(${comment.commentId}, \`${comment.content.replace(/`/g, '\\`')}\`)">編輯</button>
                <button class="btn btn-sm btn-outline-danger" onclick="deleteComment(${comment.commentId})">刪除</button>
              </div>
            `;
          }

          html += `
            <div class="d-flex align-items-start mb-3">
              <div class="flex-grow-1 ms-3">
                <div class="p-3 rounded shadow-sm" style="background-color: #2c2c2c;">
                  <div class="d-flex justify-content-between">
                    <strong class="text-light">${comment.memberName}</strong>
                    <small class="text-muted">${formattedTime}</small>
                  </div>
                  <p class="mb-0 mt-2 text-light" id="comment-content-${comment.commentId}">${comment.content}</p>
                  ${buttonsHtml}
                </div>
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

function editComment(commentId, currentContent) {
	Swal.fire({
		title: '編輯留言',
		input: 'textarea',
		inputLabel: '請輸入新的留言內容',
		inputValue: currentContent,
		showCancelButton: true,
		confirmButtonText: '更新',
		cancelButtonText: '取消',
		preConfirm: (newContent) => {
			if (!newContent) {
				Swal.showValidationMessage('內容不能為空');
			}
			return newContent;
		}
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				url: `/movies/${movieId}/comments/${commentId}`,
				method: 'PUT',
				contentType: 'application/json',
				data: JSON.stringify({ content: result.value }),
				success: function () {
					Swal.fire('成功', '留言已更新', 'success');
					loadComments();
				},
				error: function () {
					Swal.fire('錯誤', '更新留言時發生錯誤', 'error');
				}
			});
		}
	});
}

function deleteComment(commentId) {
	Swal.fire({
		title: '確定要刪除這則留言嗎？',
		icon: 'warning',
		showCancelButton: true,
		confirmButtonText: '刪除',
		cancelButtonText: '取消'
	}).then((result) => {
		if (result.isConfirmed) {
			$.ajax({
				url: `/movies/${movieId}/comments/${commentId}`,
				method: 'DELETE',
				success: function () {
					Swal.fire('已刪除', '留言已成功刪除', 'success');
					loadComments();
				},
				error: function () {
					Swal.fire('錯誤', '刪除留言時發生錯誤', 'error');
				}
			});
		}
	});
}
$(document).ready(function () {
  loadComments();
});
