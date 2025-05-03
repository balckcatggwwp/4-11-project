$(document).ready(function () {
  $.get("/employeeApi/currentInfo", function (res) {
    if (res.error || !res.empId) {
      // ❗確保觸發 logout modal 前就跳轉
      window.location.replace("/employee/noEmpPermission");
    }
  }).fail(function () {
    window.location.replace("/employee/noEmpPermission");
  });
});