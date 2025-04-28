
  $(document).ready(function () {
    $.ajax({
      url: "/movies/latest",
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
        $("#latest-movies").html(html);
      },
      error: function () {
        $("#latest-movies").html("<p>載入最新上映失敗，請稍後再試。</p>");
      }
    });
  });

