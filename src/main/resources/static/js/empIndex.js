// /js/empIndex.js

$(document).ready(function() {
    console.log("Employee Dashboard Initialized.");

    // Example: Add a subtle fade-in effect to cards on load
    $('.card').each(function(index) {
        $(this).hide().delay(index * 100).fadeIn(300);
    });


    // Add smooth scrolling for any internal links if needed
    // $('a[href^="#"]').on('click', function(event) {
    //     var target = $(this.getAttribute('href'));
    //     if( target.length ) {
    //         event.preventDefault();
    //         $('html, body').stop().animate({
    //             scrollTop: target.offset().top - 70 // Adjust offset if you have a fixed navbar
    //         }, 800);
    //     }
    // });

    // You could add event listeners for buttons if they trigger modals or AJAX requests
    // $('.btn-info').on('click', function(e) {
    //     e.preventDefault(); // Prevent default link behavior if needed
    //     alert('Loading leave request form...');
    //     // Potentially load content via AJAX or open a modal
    // });

});
