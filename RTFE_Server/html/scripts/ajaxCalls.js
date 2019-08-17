URL = "http://127.0.0.1:8080/"

function anotherFetch(type, html,extenedTable,SimulationMode){
    $.ajax({
            url: URL+"database",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: type
            }),
            success: function(resp){
                if (resp.status){
                    $(html).html(populateTable(resp.data,extenedTable,SimulationMode))
                }
            }
    })
}
function fetchStatus(mac,identify)
{
    if(mac == null || mac == ""){
    }
    else{
        setInterval(function(){
            $.ajax({
                url: URL+"building",
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({
                    type: "personInfo",
                    device_id: mac
                }),
                success:function(res){
                    var result= false;
                    if (res.message!="Person is not in building")
                        result = true;
                    if(res.status=="failed")
                        result= false;
                    if(result){
                        result= statusIdentifier("Online")
                    }
                    else{
                        result= statusIdentifier("Offline")
                    }
                    $("#"+identify).html(result);
                }
            }); 
        }, 5000);
    }
    return 'Offline <span class="online-off-indicator"><img src="icons/offline.png"/></span>';
}

function docall(botID,location,floor,mode,HTMLelement){
//    $("#body").append(str);
    $.ajax({
        url:  URL+"building",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            type: "personUpdate",
            position: location,
            floor: floor,
            id : botID,
            mode: mode
        }),
        success:function(res){
            if(res.status){
                HTMLelement.checked = true;
            }
            else{
                HTMLelement.checked = false;
            }
        }
    }); 
}

function remove(botID,mode,html){
    $.ajax({
        url:  URL+"building",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            type: "removePerson",
            id : botID,
            mode: mode
        }),
        success:function(res){
            html.remove();
        }
    });
}


function bindUser(botID,device_id,mode){
    console.log("bind: "+device_id+" - "+botID)
    device_id =device_id.trim();
    $.ajax({
        url:  URL+"building",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            type: "bind",
            id : botID,
            device_id : device_id,
            mode: mode
        }),
        success:function(res){
            console.log(res);
        }
    });
}

function addFire(x, y, rad, floor)
{
    $.ajax({
        url: "http://127.0.0.1:8080/building",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            type: "fire",
            position: [x,y],
            radius: rad,
            floor: floor
        }),
        success: function(data){
            if (data.status){
                notify(data.message, "blue");
                    $.ajax({
                        url: "http://127.0.0.1:8080/building",
                        type: "POST",
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        data: JSON.stringify({
                            type: "assignPeople"
                        }),
                        success: function(data){
                            $("body").delay(1500);
                            notify("Affected people were notified","green")
                        }
                    });
            }else{
                
                notify("Failed to add fire simulation", "red");
               
               
            }
        }, fail: function(){
            console.log('fail to add fire');
        }
    });
}
function triggerAlarm(AlarmStatus,mode){
    $.ajax({
        url: "http://127.0.0.1:8080/building",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            type: "assignPeople",
            mode : mode,
            alarm: AlarmStatus
        }),
        success: function(data){
            $("body").delay(1500);
            if(data.emergency =="true"){
                notify("The Alarm has been triggered","green")
            }
            else{
                notify("Alarm cancelled","green");
            }
        }
    });
}

function updateInfo(type, typeOfUpdate, email, value)
{
    $.ajax({
        url: "http://127.0.0.1:8080/database",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            type: "update",
            typeOfUpdate: typeOfUpdate,
            email: email,
            value: value
        }),
        success: function(data){
            if (data.status){
                return "true";           
            }else{
                return "false";
            }
        }, fail: function(){
            console.log('fail');
        }
    });
}


function deleteUser(email, name)
{
    $.ajax({
        url: "http://127.0.0.1:8080/database",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            type: "remove",
            email: email
        }),
        success: function(data){
            if (data.status){
                console.log('Successfully added a new User');
                // updateTable();
                clearWindow();
                notify("The user "+name+" was succesfully removed from the system", 'green');
                
            }else{
                notify("Failed to remove the user", 'red');              
            }
        }, fail: function(){
            console.log('fail');
        }
    });
}


function addBuilding(file, BuildingName, image)
{
    $.ajax({
        url: "http://127.0.0.1:8080/dababase",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            file: file,
            type: "upload",
            BuildingName: BuildingName,
            image: image
        }),
        success: function(data){
            if (data.status){
                notify(data.message, "blue");
            }else{
                notify("Failed to add new building", "red");               
            }
        }, fail: function(){
            notify("Failed to add new building", "red");
        }
    });
}


function addUser(dataType, name, email, pass, userType)
{
    $.ajax({
        url: "http://127.0.0.1:8080/database",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            type: dataType,
            name: name,
            email: email,
            pass: pass,
            userType: userType
        }),
        success: function(data){
            if (data.status){
                // console.log('Successfully added a new User');
                // updateTable();
                notify("The user was successfully added!", 'green')
                
            }else{
                notify("Failed to add a user", 'red');
                console.log(data.message);
               
               
            }
        }, fail: function(){
            console.log('fail');
        }
    });

}