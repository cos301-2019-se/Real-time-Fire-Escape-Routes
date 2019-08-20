
function setCookie(name, value) {
    var d = new Date;
    d.setTime(d.getTime() + 24*60*60*1000);
    document.cookie = name + "=" + value + ";path=/;expires=" + d.toGMTString();
}

function getCookie(name) {
    var v = document.cookie.match('(^|;) ?' + name + '=([^;]*)(;|$)');
    return v ? v[2] : null;
}


function putCookie(form)
//this should set the UserName cookie to the proper value;
{
    setCookie("userName", form[0].userEmail.value);

    return true;
}
function deleteCookie(name) { setCookie(name, ''); }

