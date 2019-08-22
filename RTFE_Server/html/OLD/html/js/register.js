 function register(elem){
        elem.html('');
        elem.append($('<div></div>')
            .css({
                'display' : 'none',
                'text-align' : 'center',
                'margin' : '50px 0'
            })
            .append($('<span></span>')
                .text('Register a User')
                .css({
                    'font-family' : '"Fjalla One"',
                    'font-size' : '23px'
                })
            )
            .append($('<div></div>')
                .css({
                    'width' : '90%',
                    'margin' : '0 auto'
                })
                .append($('<input>')
                    .attr({
                        'type': 'text',
                        'id' : 'reg-name',
                        'class' : 'login-input',
                        'placeholder' : 'email' 
                    })
                    )
                .append($('<input>')
                    .attr({
                        'type': 'text',
                        'id' : 'reg-pass',
                        'class' : 'login-input',
                        'placeholder' : 'password'
                    })
                    )
                .append($('<div></div>')
                    .addClass('btn-reg')
                    .text('Register')
                    .on('click', function(){
                        $.ajax({
                            url: "http://localhost:8080/database",
                            type: "POST",
                            contentType: "application/json; charset=utf-8",
                            dataType: "json",
                            data: JSON.stringify({
                                type: 'register',
                                name: $('#reg-name').val(),
                                pass: $('#reg-pass').val()
                            }),success: function(data){
                                if (data.status){
                                    notify('Register success', 'green')
                                }
                            }
                        })
                    })
                )
            ).fadeIn()
        )
    }