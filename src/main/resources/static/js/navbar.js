 let isLoggedIn = [[${session.memberDetail != null}]];

  document.getElementById("memberLink").addEventListener("click", function (e) {
    if (!isLoggedIn) {
      e.preventDefault();
      Swal.fire({
        title: "尚未登入",
        text: "請先登入會員",
        icon: "warning",
        confirmButtonText: "前往登入"
      }).then(() => {
        window.location.href = "/login";
      });
    }
  });
