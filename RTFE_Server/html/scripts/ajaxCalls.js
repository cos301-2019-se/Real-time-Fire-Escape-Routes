var intervals= [];
URL = "http://127.0.0.1:8080/"
function anotherFetch(type, html,extenedTable,SimulationMode){
    $.ajax({
        url: URL+"database",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            key: getCookie("apiKey"),
            type: type,
            buildingName: getCookie("building_name")
        }),
        success: function(resp){
            if (resp.status){
                $(html).html("");
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
    var myInterval = setInterval(function(){
            $.ajax({
                url: URL+"building",
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data: JSON.stringify({
                    key: getCookie("apiKey"),
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
        intervals.push(myInterval);
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
            key: getCookie("apiKey"),
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
            key: getCookie("apiKey"),
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
            key: getCookie("apiKey"),
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
            key: getCookie("apiKey"),
            type: "fire",
            position: [x,y],
            radius: rad,
            floor: floor
        }),
        success: function(data){
            if (data.status){
                notify(data.message, "blue");
                alarm(true,true);    
            }else{  
                notify("Failed to add fire simulation", "red"); 
            }
        }, fail: function(){
            console.log('fail to add fire');
        }
    });
}
/*

$("body").delay(1500);
notify("Affected people were notified","green")
*/
function updateInfo(type, typeOfUpdate, email, value)
{
    $.ajax({
        url: "http://127.0.0.1:8080/database",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            key: getCookie("apiKey"),
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
            key: getCookie("apiKey"),
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
            key: getCookie("apiKey"),
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
            password: pass,
            userType: userType,
            buildingName: getCookie("building_name"),
            key: getCookie("apiKey")            
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



function getBuildingInfo(element,type,isSimulation)
{
    let objData;
    $.ajax({
        url: "http://127.0.0.1:8080/database",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            key: getCookie("apiKey"),
            type:"currentBuilding",
            mode: isSimulation
        }),
        success: function(data){
            if (data.status){  
                objData = data;
                if(type === "name")
                {
                    if(isSimulation!="simulation"){
                        changeCookie("building_name",data.name);
                    }
                    $(element).html(data.name);
                }
                else if(type === "img")
                {
                    var img= `<img src="Buildings/${data.name}/building.jpeg" />`
                    $(element).html(img);
                }
            }else{
               $(element).html("No buildings to display");
               return;
            }
        }, fail: function(){
            $(element).text("Failed to load building");
            return;
        }
    });   
}
function addDropDown(element){
    $.ajax({
        url:  URL+"database",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            key: getCookie("apiKey"),
            type: "getBuildings"
        }),
        success:function(res){
            if(res.status){
                console.log(res);
                // var mySelect = $('<select style="postion:relative" id="changeBuilding"></select>');
                for (var i = 0; i < res.data.length; i++) {
                    var bName = res.data[i].building_name
                    $(element).append(
                        $('<option></option>').val(bName).html(bName)
                    );
                }
            }
        }
    }); 
}

function changeBuilding(elementText,elementImage,buildingName,isSimulation){
    filePath = "/buildings/"+buildingName +"/data.json";
    $.ajax({
        url : filePath,
        success : function (DATA) {
            if(DATA == null){
                notify("Invalid Building", 'red');
                return;
            };
            DATA = JSON.parse(DATA);
            DATA.mode = isSimulation;
            DATA.key =  getCookie("apiKey"),
            $.ajax({
                url: URL+"buildingGeneration",
                type: "POST",
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                data : JSON.stringify(DATA),
                success:function(res){
                    if(res.status){
                        notify("Building Changed", 'green');
                        $(elementText).html(buildingName);
                        if(isSimulation!="simulation"){
                            if(buildingName!=undefined)
                                changeCookie("building_name",buildingName);
                        }
                    }
                    else{
                        notify(res.msg,"red");
                    }
                    getBuildingInfo(elementImage,"img",isSimulation);
                }   
            });
        },
        fail: function(data){
            console.log("failed: "+data);
            notify("Server offline","red");
        }
    });    
    
}

function alarm(status,isSimulation){

    $.ajax({
        url: URL+"building",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data:JSON.stringify({
            key: getCookie("apiKey"),
            type: "assignPeople",
            mode: isSimulation,
            alarm: status

        }),
        success:function(res){
            if(res.status){
                if(res.emergency =="true"){
                    notify("Alarm triggered","yellow");
                }
                else{
                    notify("Alarm disarmed","green");
                }
            }
            else{
                notify(res.msg,"red");
            }
        },
        fail: function(data){
            console.log("failed: "+data);
            notify("Server offline","red");
        }
    }); 
}

