
$(function(){
		
		 document.querySelector(".register-form").addEventListener("submit", function (event) {
	            let password1 = document.querySelector("[name='password']").value;
	            let password2 = document.querySelector("[name='password2']").value;
	            if (password1 !== password2) {
	                alert("兩次輸入的密碼不一致，請重新輸入！");
	                event.preventDefault();
	            }
	        });

	});  


  $(".register-form").on("submit", function (e) {
    e.preventDefault();

    // 取得表單資料
    const formData = $(this).serialize();

    // 發送 AJAX 請求
    $.post("/employee/empRegister", formData, function (response) {
        Swal.fire({
            icon: response.status === "success" ? "success" : "error",
            title: response.message,
            text: response.status === "success" ? "歡迎，" + response.name : ""
        }).then(() => {
          if (response.status === "success") {
              window.location.href = "/employee/empLogin"; // 跳轉到登入頁
          }
        



    }).fail(function (xhr) {
        Swal.fire({
            icon: "error",
            title: "註冊失敗",
            text: xhr.responseJSON.message
        });
    });
  })
});
