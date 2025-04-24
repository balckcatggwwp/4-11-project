
  $(document).ready(function () {
    $.ajax({
      url: "/movies/soon",
      method: "GET",
      success: function (data) {
        let html = "";
        data.forEach(function (movie) {
          html += `
            <div>
              <a href="/MovieInfo/${movie.id}">
                <img src="/movie/images/${movie.image}" alt="電影圖片" />
              </a>
              <h3>《${movie.name}》</h3>
              <p>${movie.description}</p>
            </div>
          `;
        });
        $("#upcoming-movies").html(html);
      },
      error: function () {
        $("#upcoming-movies").html("<p>載入即將上映失敗，請稍後再試。</p>");
      }
    });
  });

