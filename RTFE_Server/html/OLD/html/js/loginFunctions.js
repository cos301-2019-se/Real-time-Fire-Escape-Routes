function login()
{
	$('#login').on('click', function(){
        $.ajax({
            url: "http://127.0.0.1:8080/database",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: 'login',
                name: $('#username').val(),
                pass: $('#password').val()
            }),
            success: function(data){
                if (data.status){
                    username = $('#username').val();

                    $('#login-form').fadeOut('fast', function(){
                        $('#background').hide('slide', {direction: 'left'}, 500, function(){
                            $('#login-page').fadeOut('fast', function(){
                                $('#footer').css('width', '100%');
                                $('#dashboard').fadeIn('fast');
                                $('#menu-user').text(username);
                                dashboard($('#result'))
                            });
                        });
                    })
                }else{
                    notify('Error: Email or Password is incorrect!', 'red');
                }
            }
        })

    })
}

function logout()
{
	$('#out').on('click', function(){
        $('#dashboard').fadeOut('fast', function(){
            $('#footer').css('width', '50%');
            $('#login-page').fadeIn(function(){
                $('#background').show('slide', {direction: 'left'}, 500, function(){
                   $('#username').val('');
                   $('#password').val('');
                   $('#login-form').show();
                })
            })
        })
    })
}