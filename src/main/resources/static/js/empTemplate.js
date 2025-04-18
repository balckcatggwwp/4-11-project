function showTime() {
    var date = new Date();
    var h = date.getHours(); // 0 - 23
    var m = date.getMinutes(); // 0 - 59
    var s = date.getSeconds(); // 0 - 59
    var session = "AM";

    if (h == 0) {
        h = 12;
    }

    if (h > 12) {
        h = h - 12;
        session = "PM";
    }

    h = (h < 10) ? "0" + h : h;
    m = (m < 10) ? "0" + m : m;
    s = (s < 10) ? "0" + s : s;

    var time = h + ":" + m + ":" + s + " " + session;
    document.getElementById("MyClockDisplay").textContent = time;

    setTimeout(showTime, 1000);

}

showTime();
$(document).ready(function () {
    $(".nav-pills .nav-link").on("mouseenter", function () {
      if (!$(this).hasClass("clicked")) {
        $(this).addClass("hovered");
      }
    });

    $(".nav-pills .nav-link").on("mouseleave", function () {
      if (!$(this).hasClass("clicked")) {
        $(this).removeClass("hovered");
      }
    });

    $(".nav-pills .nav-link").on("click", function () {
      $(".nav-pills .nav-link").removeClass("clicked hovered");
      $(this).addClass("clicked");
    });
	
});