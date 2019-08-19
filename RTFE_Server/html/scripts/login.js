$(function() {
    $('#login').on('click', function(){
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
            success: function(data){
                
                if (data.status == true){
                    if(data.userType == "Agent"){
                        notify("You have insuffcient rank to use the page", 'orange');
                        return;
                    }
                    notify(data.msg, 'green');
                    username = $('#username').val();
                    $('#login-form').fadeOut('fast', function(){
                        $('#background').hide('slide', {direction: 'left'}, 500, function(){
                            $('#login-page').fadeOut('fast', function(){
                                $('#footer').css('width', '100%');
                                $('#dashboard').fadeIn('fast');
                                $('#menu-user').text(username);
                                
                                LoginSuccess(data.userType);

                            });
                        });
                    })
                }else{
                    notify(data.msg, 'red');
                }
            }
        })
    })
});


function LoginSuccess(userType){

    $('#navbar').load("ContentPages/navigation.html nav",()=>{
        if(userType == "admin"){
            $("nav>#options").append(`<div class="active nav__list-item" id="admin-view" href="#" >Administration</div>`)
            $("#result").append(echoAdminTableView());
            anotherFetch("getUsers", "#table-body-A",true);    
            $("#admin-view").on("click",()=>{
                console.log("Clicked on admin Views");
                $("#result").html("");
                $("#result").append(echoAdminTableView());
                anotherFetch("getUsers", "#table-body-A",true);        
            })        
        }
        else{
            $("nav>#options").append(`<div class="active nav__list-item" id="su-view-life" href="#">Live view</div>`)
            $("nav>#options").append(`<div class="nav__list-item" id="su-view-simulation" href="#">Simulation view</div>`)
            $("nav>#options").append(`<div class="nav__list-item" id="admin-view" href="#" >Administration</div>`)        
        
            $("#result").append(echoContentTable_SuperUser());
            anotherFetch("getUsers", "#table-body-SU",false);
            buildingInfo();    

            $("#admin-view").on("click",()=>{
                $('#su-view-life').removeClass("active");
                $('#su-view-simulation').removeClass("active");
                $('#admin-view').addClass("active");
                $("#result").html("");
                $("#result").append(echoAdminTableView());
                anotherFetch("getUsers", "#table-body-A",true,false);        
            })     
            
            $("#su-view-life").on("click",()=>{
                $('#su-view-life').addClass("active");
                $('#su-view-simulation').removeClass("active");
                $('#admin-view').removeClass("active");
                $("#result").html("");
                $("#result").append(echoContentTable_SuperUser());
                anotherFetch("getUsers", "#table-body-SU",false,false);  
                buildingInfo();   
            }) ;    
            
            $("#su-view-simulation").on("click",()=>{
                $('#su-view-life').removeClass("active");
                $('#su-view-simulation').addClass("active");
                $('#admin-view').removeClass("active");
                $("#result").html("");
                $("#result").append(echoTableBotview());
                anotherFetch("getUsers", "#table-body-Sim",false,true);        
            })     
            
        }
    });
}

function buildingInfo(){
    addDropDown("#buildingDropDown");
    getBuildingInfo("#ActiveBuilding",'name','live');
    getBuildingInfo("#ActiveBuildingImage",'img','live');
    
    $("#buildingDropDown").on("change",()=>{
        changeBuilding("#ActiveBuilding","#ActiveBuildingImage",$("#buildingDropDown").val(),"live")
    })
}


























