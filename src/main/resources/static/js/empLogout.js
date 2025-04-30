$(document).ready(function () {
   $("#logoutLink").on("click", function (e) {
     e.preventDefault();

     // 呼叫後端登出路徑
     $.get("/employee/empLogout", function () {
       Swal.fire({
         icon: 'success',
         title: '已成功登出',
         showConfirmButton: false,
         timer: 1200
       }).then(() => {
         window.location.replace("/employee/empLogin"); // 導向登入頁
       });
     }).fail(function () {
       Swal.fire("錯誤", "登出失敗，請稍後再試", "error");
     });
   });
 });