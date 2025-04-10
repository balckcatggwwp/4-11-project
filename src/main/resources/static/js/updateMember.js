$(document).ready(function () {

    $('input[name="password"]').on('input', function () {
        let password = $(this).val().trim();
        console.log("Password changed to:", password); // 看看有沒有抓到
        if (password !== "") {
            console.log("Password is not empty");
            // 顯示「確認密碼」標籤和輸入框
            $('#confirmPasswordLabel, #confirmPasswordInput').show();
        } else {
            // 隱藏並清空輸入值
            $('#confirmPasswordLabel, #confirmPasswordInput').hide().val('');
        }
    });


    const message = $('#message-holder').data('message');
    console.log("message:", message);
    if (message) {
        Swal.fire({
            icon: message === "修改成功" ? "success" : "error",
            title: message
        });
    }

    $('.member-update-form').submit(function (e) {
        e.preventDefault();
        let password = $('input[name="password"]').val();
        let password2 = $('input[name="password2"]').val();
        if (password !== "") {
            // 如果 password2 是空的
            if (password2 === "") {
                Swal.fire({
                    icon: 'warning',
                    title: '請再次輸入密碼',
                    text: '確認密碼不得為空',
                });
                return;
            }
    
            // 如果兩次密碼不相符
            if (password !== password2) {
                Swal.fire({
                    icon: 'warning',
                    title: '密碼不一致',
                    text: '請確認兩次密碼輸入相同',
                });
                return;
            }
        }
    
        this.submit(); // 這裡會繞過 preventDefault，繼續送出
    });
});
