$(document).ready(function(){
	$('a[href=#]').click(function(){
		return false;
	});
	
	$('#new-tweet-textarea').focus(function() {
		$(this).height(5 * 15);
		$('#new-tweet-button').show();
		$(this)[0].style.color = "black";
		if ($(this).val() == "What's on your mind?") {
			$(this).val("");
		}
	});
	
	$('#new-tweet-textarea').blur(function() {
		var text = $('#new-tweet-textarea').val();
		if (text == '') {
			$('#new-tweet-button').hide();
			$(this).height(15);
			$(this)[0].style.color = "grey";
			$(this).val("What's on your mind?");
		}
	});
	
	$('#register-form').submit(function(){
		return true;
		$('#validation-errors').html("");
		$('#nickname').removeClass("validation-error");
		$('#password').removeClass("validation-error");
		$('#password2').removeClass("validation-error");
		$('#captcha').removeClass("validation-error");
		var errors = [];
		
		var error = false;
		if ($('#nickname').val().trim().length == 0)
		{
			$('#nickname').addClass("validation-error");
			errors.push("Nickname must not be empty");
			error = true;
		}
		
		if ($('#password').val() != $('#password2').val())
		{
			$('#password').addClass("validation-error");
			$('#password2').addClass("validation-error");
			errors.push("Passwords do not match");
			error = true;
		}
		
		if ($('#password').val().trim().length == 0)
		{
			$('#password').addClass("validation-error");
			$('#password2').addClass("validation-error");
			errors.push("Password must not be empty");
			error = true;
		}
		
		
		if ($('#captcha').val() != $('#captcha-result').val())
		{
			$('#captcha').addClass("validation-error");
			errors.push("Wrong captcha response");
			error = true;
		}
		
		if (error) {
			$('#validation-errors').show();
			$('#validation-errors').html(errors.join("<br>"));
		}
		else
		{
			$('#validation-errors').hide();
		}
		
		return !error;
	
	});
	
	$('#upload-image').hide();
	$('#upload-image-link').click(function() {
		$('#user-image').click();
	});
	
	$('#user-image').change(function() {
		$('#upload-image').submit();
	});
	

});