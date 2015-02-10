
$(function() {
    var show_the_answer = function() {
        $("#answer").parent().show();
        $("#answer").focus();
        $( "#open-button" ).hide();
        $( "#exhausted-button" ).hide();
        $("#next-button").parent().show();
    };

    var stop_time = function() {
        $( "#exhausted-button" ).parent().show();
        $( "#open-button" ).hide();
    };

    $(".hidden").hide();

    $( "#open-button" ).button().click(function( event ) {
        show_the_answer();
        event.preventDefault();
    });

    $( "#exhausted-button" ).button().click(function( event ) {
        show_the_answer();
        event.preventDefault();
    });

    $( "a#timer-button" ).button().click(function( event ) {
        $( "a#timer-button" ).hide();
        $( "#open-button" ).parent().show();
        setTimeout(stop_time, 60000);
        event.preventDefault();
    });

    $( "a#next-button" ).button();
});