$(document).ready(function(){
		$('.register-form').submit(function(e){
			e.preventDefault();
			let password = $('input[name="password"]').val();
			let password2 = $('input[name="password2"]').val();
			if (password !== password2) {
            Swal.fire({
                icon: 'warning',
                title: '密碼不一致',
                text: '請確認兩次密碼輸入相同'
            });
            return;
        	}
			$.ajax({
            url: '/member/register',
            type: 'POST',
            data: $('.register-form').serialize(),
            success: function(response){
                // 假設你從 controller 回傳 JSON 格式：
                // return ResponseEntity.ok(Map.of("status", "success", "name", member.getName()))
                
                if (response.status === 'success') {
                    Swal.fire({
                        icon: 'success',
                        title: '註冊成功',
                        html: `歡迎 <b>${response.name}</b> 加入我們！`,
                        confirmButtonText: '前往登入'
                    }).then(() => {
                        window.location.href = '/employee/MemberManger'; // 成功後導向登入頁
                    });
                } else {
                    Swal.fire({
                        icon: 'error',
                        title: '註冊失敗',
                        text: response.message || '請稍後再試'
                    });
                }
            },
            error: function(xhr){
                Swal.fire({
                    icon: 'error',
                    title: '伺服器錯誤',
                    text: '請稍後再試或聯絡客服'
                });
            }
        	});
			})
		});