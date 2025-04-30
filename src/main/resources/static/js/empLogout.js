$(document).ready(function () {
  $("#logoutLink").on("click", function (e) {
    e.preventDefault();

    Swal.fire({
      title: '確定要登出嗎？',
      text: '您將被導回登入頁面',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: '是的，登出',
      cancelButtonText: '取消'
    }).then((result) => {
      if (result.isConfirmed) {
        $.get("/employee/empLogout", function () {
          Swal.fire({
            icon: 'success',
            title: '已成功登出',
            showConfirmButton: false,
            timer: 1200
          }).then(() => {
            window.location.replace("/employee/empLogin");
          });
        }).fail(function () {
          Swal.fire("錯誤", "登出失敗，請稍後再試", "error");
        });
      }
    });
  });
});
