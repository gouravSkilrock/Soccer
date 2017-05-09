$(document).ready(function(){
	Pace.on('done', function() {
		$('body').css('overflow-y','auto');
	    $('.preloader').fadeOut();	  
	});
	$(window).scroll(function() {
	    if ($(window).scrollTop() >120)
			{$('.pageWrap').addClass('titleFix');}
		else
			{$('.pageWrap').removeClass('titleFix');}
	});
});