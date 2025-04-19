$(document).ready(function () {
  // 初始化 DataTable
  $('#myTable').DataTable({
    language: {
      paginate: {
        previous: "上一頁",
        next: "下一頁"
      },
      lengthMenu: "每頁顯示 _MENU_ 筆資料",
      zeroRecords: "找不到符合的資料",
      info: "顯示第 _START_ 到 _END_ 筆資料，共 _TOTAL_ 筆",
      infoEmpty: "目前沒有資料",
      infoFiltered: "(從 _MAX_ 筆資料中過濾)",
      search: "搜尋："
    }
  });

  // 顯示訊息（成功/失敗）
  const message = $('#message-holder').data('message');
  if (message) {
    Swal.fire({
      icon: message.includes("成功") ? "success" : "error",
      title: message
    });
  }

  // 處理註冊表單送出
  $('.register-form').submit(function (e) {
    e.preventDefault();

    const form = this;
    const formData = new FormData(form);

    const password = formData.get("password");
    const password2 = formData.get("password2");

    if (password !== password2) {
      Swal.fire({
        icon: 'warning',
        title: '密碼不一致',
        text: '請確認兩次密碼輸入相同'
      });
      return;
    }

    formData.delete("password2"); // 移除不必要欄位

    $.ajax({
      url: '/member/register',
      type: 'POST',
      data: formData,
      processData: false,
      contentType: false,
      success: function (response) {
        if (response.status === 'success') {
          Swal.fire({
            icon: 'success',
            title: '註冊成功',
            html: `新增成功 會員= <b>${response.name}</b> ！`,
            confirmButtonText: '確定',
            timer: 3000,
            timerProgressBar: true
          }).then(() => {
            $('#createMemberModal').modal('hide');
            location.reload();
          });
        } else {
          Swal.fire({
            icon: 'error',
            title: '註冊失敗',
            text: response.message || '請稍後再試'
          });
        }
      },
      error: function () {
        Swal.fire({
          icon: 'error',
          title: '伺服器錯誤',
          text: '請稍後再試或聯絡客服'
        });
      }
    });
  });
});

// 刪除會員功能
function deleteMember(id) {
  console.log(id);
  Swal.fire({
    title: '確定要刪除這位會員嗎？',
    icon: 'warning',
    showCancelButton: true,
    confirmButtonText: '是的，刪除',
    cancelButtonText: '取消',
    reverseButtons: true
  }).then((result) => {
    if (result.isConfirmed) {
      fetch(`/member/deleteMember/${id}`, {
        method: 'DELETE'
      }).then(res => {
        if (res.ok) {
          Swal.fire('刪除成功', '', 'success').then(() => {
            location.reload();
          });
        } else {
          Swal.fire('刪除失敗', '', 'error');
        }
      }).catch(() => {
        Swal.fire('刪除錯誤', '', 'error');
      });
    }
  });
}
