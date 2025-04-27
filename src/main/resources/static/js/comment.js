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
          html += `
            <div class="d-flex align-items-start mb-3">
              <div class="flex-grow-1 ms-3">
                <div class="p-3 rounded shadow-sm" style="background-color: #2c2c2c;">
                  <div class="d-flex justify-content-between">
                    <strong class="text-light">${comment.memberName}</strong>
                    <small class="text-muted">${formattedTime}</small>
                  </div>
                  <p class="mb-0 mt-2 text-light">${comment.content}</p>
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

$(document).ready(function () {
  loadComments();
});
