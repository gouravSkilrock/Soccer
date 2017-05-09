$(document).ready(function(){
"use strict";
	$('*:last-child').addClass('last_Child');
	$('.middle-col').css('width', $(window).width()-462 + 'px');
	$('.draw-right').css('width', $('.draw').width()-40 + 'px');
	$('.setting-button BUTTON > SPAN').css('width', $('.setting-button BUTTON#setting-btn').width()-53 + 'px');
	$('.winning-button BUTTON > SPAN').css('width', $('.winning-button BUTTON').width()-53 + 'px');
});
