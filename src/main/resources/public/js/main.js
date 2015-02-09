
$(function() {
    var show_the_answer = function() {
        $(".hidden").show(); $("#answer").focus(); $( "#open-button" ).hide();
        var answer_id = $( ".question" ).attr("id");
//        $.getJSON('/do/not/show/answer/' + answer_id + '/to/user',function(res) {console.log(res);});
    };

    $(".hidden").hide();

    $( "#open-button" ).button().click(function( event ) {
        show_the_answer();
        event.preventDefault();
    });

    $( "a#timer-button" ).button().click(function( event ) {
        $( "a#timer-button" ).hide();
        $( "#open-button" ).parent().show();
        setTimeout(show_the_answer, 60000);
        event.preventDefault();
    });

    $( "a#next-button" ).button();
});