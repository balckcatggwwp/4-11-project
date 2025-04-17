// /js/empIndex.js

$(document).ready(function() {
    console.log("Employee Dashboard Initialized.");

    // Example: Add a subtle fade-in effect to cards on load
    $('.card').each(function(index) {
        $(this).hide().delay(index * 100).fadeIn(300);
    });

});
