


const seatingChart = document.getElementById('seating-chart');
const countElement = document.getElementById('count');
const totalElement = document.getElementById('total');
let ticketPrice = parseFloat($('#money').val()) || 0// 假設票價為 250 元'); // 假設票價為 250 元
$('#type').on('change', function () {
    // 當 #money 的值改變時，更新 ticketPrice

    setTimeout(function () {
        // 在延時後更新 ticketPrice 為 #money 的當前值
        ticketPrice = parseFloat($('#money').val()) || 0;
        // 重新更新總金額
        updateSummary();
    }, 500);
});

// 更新已選座位數量和總金額
function updateSummary() {
    const selectedSeats = seatingChart.querySelectorAll('.seat.selected');
    const selectedSeatsCount = selectedSeats.length;

    countElement.innerText = selectedSeatsCount;
    totalElement.innerText = selectedSeatsCount * ticketPrice;

}

const seatlist = []
const form = document.getElementById('yourFormId'); // 你的 form 元素的 ID
const hiddenInput = document.createElement('input');
hiddenInput.type = 'hidden';
hiddenInput.name = 'selectedSeatsJson';
hiddenInput.id = 'selectedSeat';

hiddenInput.value = JSON.stringify(seatlist);
form.appendChild(hiddenInput);
// 座位點擊事件處理 (使用事件委派)
seatingChart.addEventListener('click', (event) => {
    $('#selectedSeat').value = ''
    // 檢查點擊的是否是座位，且不是已被佔用的座位
    if (event.target.classList.contains('seat') && !event.target.classList.contains('occupied')) {
        // 切換 'selected' class
        event.target.classList.toggle('selected');

        // 同步 'available' class (如果移除 selected，要確保 available 存在)
        if (!event.target.classList.contains('selected')) {
            event.target.classList.add('available');
            seatlist.pop(event.target.dataset.seatId);
        } else {
            seatlist.push(event.target.dataset.seatId);
            event.target.classList.remove('available');

        }


        // 更新摘要資訊

        console.log(seatlist);
        updateSummary();
        hiddenInput.value = JSON.stringify(seatlist);
        form.appendChild(hiddenInput);

        $('#tota').val()
    }
});

// 初始載入時更新一次摘要 (顯示 0)
updateSummary();






$(function () {
    function loadseat() {
        let selecttime = $("#time").val(); // 確保 ID 為 time 的元素存在且有值
        let selecthall = $("#halls").val(); // 確保 ID 為 halls 的元素存在且有值
        const seatingChart = $('#seating-chart'); // 獲取座位區容器

        // --- (推薦) 在發送請求前，重置所有座位的狀態 ---
        seatingChart.find('.seat').each(function () {
            // 移除可能存在的 selected 和 occupied 狀態
            $(this).removeClass('selected occupied');
            // 確保所有座位預設是 available (除非它本來就不可用)
            // 如果有永久不可用的座位，它們應該在 HTML 中就沒有 available class
            $(this).addClass('available');
        });
        // 重置摘要信息
        updateSummary(); // 確保 updateSummary 函數可用

        // --- 清空舊的下拉選單 (如果不再需要，可以刪除這行和相關邏輯) ---
        // $("#seat").empty(); // 這行看起來是為下拉選單準備的，與視覺化座位圖無關

        // --- 發送 AJAX 請求 ---
        $.ajax({
            url: '/booktick/seat', // 請確認 URL 正確
            data: { showtimeid: selecttime, hallid: selecthall },
            dataType: 'json',
            type: 'GET',
            success: function (respones) { // 'response' 拼寫建議修正
                console.log("已售出座位:", respones);
                let soldSeatsIds = [];
                $.each(respones, function (i, e) {
                    if (e && e.seatid) { // 確保數據有效
                        soldSeatsIds.push(e.seatid);
                    }
                });

                // --- 根據返回的數據更新座位狀態 ---
                soldSeatsIds.forEach(function (seatId) {
                    // 使用 data-seat-id 屬性選擇器找到對應的座位元素
                    const seatElement = seatingChart.find(`.seat[data-seat-id="${seatId}"]`);

                    if (seatElement.length > 0) { // 確保找到了元素
                        // 標記為已佔用
                        seatElement.removeClass('available selected'); // 移除可選和已選狀態
                        seatElement.addClass('occupied');       // 添加已佔用狀態
                    } else {
                        console.warn("找不到對應的座位元素:", seatId); // 如果找不到，打印警告
                    }
                });

                // --- (舊邏輯，與視覺化座位圖無關，如果不需要請刪除) ---
                /*
                let allSeats = [];
                for (let row of ["A", "B", "C", "D", "E"]) {
                    for (let col = 1; col <= 10; col++) {
                        allSeats.push(row + col);
                    }
                }
                let availableSeats = allSeats.filter(seat => !soldSeatsIds.includes(seat));
                availableSeats.forEach(function(seat) {
                    $('#seat').append(`<option value="${seat}">${seat}</option>`);
                });
                */
                // --- 更新一次摘要 (可選，如果重置時已更新) ---
                // updateSummary();

            },
            error: function (jqXHR, textStatus, errorThrown) { // 添加更詳細的錯誤處理
                console.error('載入座位失敗:', textStatus, errorThrown);
                // 可以在這裡提示用戶錯誤信息
                alert('載入座位資訊失敗，請稍後再試。');
            }
        });
    };


    // 確保 updateSummary 函數存在且功能正確
    // function updateSummary() { ... }

    // --- 可能需要在頁面加載完成或選擇時間/影廳後調用 loadseat ---
    // 例如:
    // $(document).ready(function() {
    //     // 如果有預設值，可以在這裡調用一次
    //     // loadseat();
    //
    //     // 當時間或影廳改變時重新加載
    //     $("#time, #halls").on('change', loadseat);
    // });

    $('#halls').on('change', function () {
        loadseat();

    }
    )



})

$(function () {
    $("#datepicker").datepicker({ dateFormat: 'yy-mm-dd' });
    $("#from-datepicker").on("change", function () {
        var fromdate = $(this).val();
        alert(fromdate);
    });
    //點選日期改時間
    $("#datepicker").on("change", function () {
        let selectdate = $("#datepicker").val();
        console.log(selectdate)
        $("#time").empty();
        $.ajax({
            url: '/booktick/time/' + selectdate,

            dataType: 'json',
            type: 'post',
            contentType: 'application/json',
            success: function (respones) {
                $('#time').append(`<option value="">場次</option>`);
                $.each(respones, function (i, e) {

                    $('#time').append(`<option value="${e.showtimeid}">${e.starttime}</option>`)
                })
            }

        })
    });
    //ready畫面加載預設時間

    ///
    //廳
    function name() {
        $('#moviename').empty();
        let hall = $("#halls").val();
        $.ajax({
            url: '/booktick/name',
            data: { hallid: hall },
            dataType: 'json',
            type: 'GET',
            success: function (respones) {


                $('#moviename').append(`<option value="${respones.movieid}">${respones.moviename}</option>`)


            }
        })
    }

    $("#halls").on("change", function () {
        name();
        $('#moviename').empty();
    })


    $(document).ready(function () {

        $.ajax({
            url: '/booktick/hall',
            dataType: 'json',
            type: 'GET',
            success: function (respones) {
                $.each(respones, function (i, e) {
                    //console.log(e.hallid);
                    $('#halls').append(`<option value="${e.hallid}" >${e.hallid}</option>`)



                    //console.log(hall)

                });
                let hall = $("#halls").val();
                /////

                /////


                /////////////////////////////////////////////


                ////////////////////////////////////////	
            }
        })
    });

    //////






    let money = [];

    function moneyse() {
        let tp = $('#type').val();
        let time = $('#time').text();
        //console.log(time)
        let mf = 0
        if (tp == 1) {

            mf = money[tp - 1];
            $("#money").val(mf)
            console.log()
        }
        else if (tp == 2) {

            mf = money[tp - 1];
            $("#money").val(mf)
        }
        else if (tp == 3) {

            mf = money[tp - 1];
            $("#money").val(mf)
        } else if (tp == 4) {

            mf = money[tp - 1];
            $("#money").val(mf)
        }
    }


    $(document).ready(function () {

        $.ajax({
            url: '/booktick/type',
            dataType: 'json',
            type: 'GET',
            success: function (respones) {
                $.each(respones, function (i, e) {


                    //console.log(ticktyp)
                    let se = e.tickettypeid

                    //console.log(ticktyp)
                    //console.log(se)
                    //console.log(selectty)
                    $('#type').append(`<option value="${e.tickettypeid}">${e.tickettype}</option>`)
                    money.push(e.moneytype)
                    moneyse();
                })
            }
        })
    });

    $('#type').on("change", function () {
        moneyse();

    })


    /////
});
