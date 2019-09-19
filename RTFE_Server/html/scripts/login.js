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

    function login() {
        $.ajax({
            url: "http://127.0.0.1:8080/database",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: 'login',
                email: $('#username').val(),
                password: $('#password').val()
            }),
            success: function (data) {
                setCookie($('#username').val(), data.apiKey, "");
                if (data.status == true) {
                    if (data.userType == "Agent") {
                        notify("You have insuffcient rank to use the page", 'orange');
                        return;
                    }
                    notify(data.msg, 'green');
                    username = $('#username').val();
                    $('#login-form').fadeOut('fast', function () {
                        $('#background').hide('slide', {direction: 'left'}, 500, function () {
                            $('#login-page').fadeOut('fast', function () {
                                $('#footer').css('width', '100%');
                                $('#dashboard').css('width', '100%');
                                $('#dashboard').fadeIn('fast');
                                $('#menu-user').text(username);

                                LoginSuccess(data.userType);

                            });
                        });
                    })
                } else {
                    notify(data.msg, 'red');
                }
            }
        })
    }


function LoginSuccess(userType){

    $('#navbar').load("ContentPages/navigation.html nav",()=>{
        if(userType == "admin"){
            $("nav>#options").append(`<div class="nav__list-item" id="su-view-life" href="#">Live view</div>`)
            $("nav>#options").append(`<div class="active nav__list-item" id="admin-view" href="#" >Administration</div>`)
            $("#result").html("");
            getBuildingInfo("#ActiveBuilding",'name','live');
            addDropDown("#buildingDropDown");
            $("#result").append(echoAdminTableView());
             $("#buildingDropDown").on("change",()=>{
            changeCookie("building_name",$("#buildingDropDown").val());
             anotherFetch("getUsersInBuilding", "#table-body-A",true,false);
            })
            $("#displayUsername").html(getCookie("email"));
            anotherFetch("getUsers", "#table-body-A",true);    
            $("#admin-view").on("click",()=>{
                intervals.forEach(clearInterval);
                $('#su-view-life').removeClass("active");
                $('#su-view-simulation').removeClass("active");
                $('#admin-view').addClass("active");
                $("#result").html("");
                $("#result").append(echoAdminTableView());
                 buildingInfo();
                $("#buildingDropDown").on("change",()=>{
                    changeCookie("building_name",$("#buildingDropDown").val());
                anotherFetch("getUsersInBuilding", "#table-body-A",true,false);
            })
                anotherFetch("getUsersInBuilding", "#table-body-A",true);
            })

        $("#su-view-life").on("click",()=>{
            intervals.forEach(clearInterval);
            $('#su-view-life').addClass("active");
            $('#su-view-simulation').removeClass("active");
            $('#admin-view').removeClass("active");
            $("#result").html("");
            $("#result").append(echoContentTable_SuperUser());
            buildingInfo();
            $("#buildingDropDown").on("change",()=>{
                changeCookie("building_name",$("#buildingDropDown").val());
            anotherFetch("getUsersInBuilding", "#table-body-SU",false,false);
        })
            anotherFetch("getUsersInBuilding", "#table-body-SU",false,false);

            }) ;
        }
        else {
            $("nav>#options").append(`<div class="active nav__list-item" id="su-view-life" href="#">Live view</div>`)
            $("nav>#options").append(`<div class="nav__list-item" id="su-view-simulation" href="#">Simulation view</div>`)
            $("nav>#options").append(`<div class="nav__list-item" id="admin-view" href="#" >Administration</div>`)
            $("#displayUsername").html(getCookie("email"));
            $("#result").html("");
            $("#result").append(echoContentTable_SuperUser());
            buildingInfo();    
            anotherFetch("getUsers", "#table-body-SU",false);

            $("#admin-view").on("click",()=>{
                
                intervals.forEach(clearInterval);
                $('#su-view-life').removeClass("active");
                $('#su-view-simulation').removeClass("active");
                $('#admin-view').addClass("active");
                $("#result").html("");
                $("#result").append(echoAdminTableView());
                buildingInfo();
                $("#buildingDropDown").on("change",()=>{
                    changeCookie("building_name",$("#buildingDropDown").val());
                    anotherFetch("getUsersInBuilding", "#table-body-A",true,false);
                })
                anotherFetch("getUsersInBuilding", "#table-body-A",true,false);

            })     
            
            $("#su-view-life").on("click",()=>{
                intervals.forEach(clearInterval);
                $('#su-view-life').addClass("active");
                $('#su-view-simulation').removeClass("active");
                $('#admin-view').removeClass("active");
                $("#result").html("");
                $("#result").append(echoContentTable_SuperUser());
                buildingInfo();
                $("#buildingDropDown").on("change",()=>{
                    changeCookie("building_name",$("#buildingDropDown").val());
                    anotherFetch("getUsersInBuilding", "#table-body-SU",false,false);
                })
                anotherFetch("getUsersInBuilding", "#table-body-SU",false,false);

            }) ;    
            
            $("#su-view-simulation").on("click",()=>{
                intervals.forEach(clearInterval);
                $('#su-view-life').removeClass("active");
                $('#su-view-simulation').addClass("active");
                $('#admin-view').removeClass("active");
                $("#result").html("");
                $("#result").append(echoTableBotview());
                buildingInfo(true);
                anotherFetch("getUsers", "#table-body-Sim",false,true);
                getPeople("table-body-Sim");
            })
            
        }

        $('#logout').click(function() {
            deleteCookie("email");
            $("#result").html("");
            $("#navbar").html("");
            $('#footer').css('width', '50%');
            $('#login-form').fadeIn('fast', function(){
                $('#background').show('slide', {direction: 'right'}, 500, function(){
                    $('#login-page').fadeIn('fast', function(){
                        intervals.forEach(clearInterval);
                        $('#dashboard').fadeOut('fast');
                        $("#result").html("");
                    });
                });
            })
        });
    });
}

function buildingInfo(isSimulation){
    addDropDown("#buildingDropDown");
    getBuildingInfo("#ActiveBuilding",'name','live');
    getBuildingInfo("#ActiveBuildingImage",'img','live');
    $("#buildingDropDown").on("change",()=>{
        changeBuilding("#ActiveBuilding","#ActiveBuildingImage",$("#buildingDropDown").val(),"live")
    })
    if(isSimulation == true){
        addDropDown("#buildingDropDownSim");
        getBuildingInfo("#ActiveBuildingSim",'name','simulation');       
        $("#buildingDropDownSim").on("change",()=>{
            changeBuilding("#ActiveBuildingSim","#ActiveBuildingImageSim",$("#buildingDropDownSim").val(),"simulation")
            buildingInfo(true);
        })
    }

}


























