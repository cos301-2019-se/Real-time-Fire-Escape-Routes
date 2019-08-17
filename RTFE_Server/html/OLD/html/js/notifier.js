function notify(msg, color){
        $('body').append($('<div></div>')
            .css({
                'position' : 'fixed',
                'margin' : '0 auto',
                'top' : '-50px',
                'left' : '0',
                'right' : '0',
                'text-align' : 'center',
            })
            .append($('<span></span>')
                .css({
                    'font-family' : 'Ubuntu',
                    'padding' : '10px 20px',
                    'background-color' : color,
                    'color' : 'white',
                    'border-radius' : '7px',
                    'box-shadow' : '0px 2px 3px 1px rgba(0,0,0,0.27)'
                })
                .text(msg)
            )
            .animate({
                top: "+=80px"
            }).delay(2000).animate({
                top: "-=80px"
            }, function(){
                $(this).remove();
            })
        )
}