$(()=>{
    // $("body").append(echoContentTable_SuperUser());
    echoContentTable_SuperUser();
});
var URL = "http://127.0.0.1:8080/"


/*Status fetcher, fetches user's online/offline status*/
function fetchStatus(mac,identify)
{
	if(mac==null)
		mac ="-1"
	$.ajax({
        url: "http://127.0.0.1:8080/building",
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
        	if(result){
        		result= statusIdentifier("Online")
        	}
        	else{
        		result= statusIdentifier("Offline")
        	}
        	$("#"+identify).html(result);
        }
    }); 
}

function statusIdentifier(status)
{
    if(status === "Offline")
    {
        return `Offline <span class="online-off-indicator"><img src="icons/offline.png"/></span>`;
    }
    if(status === "Online")
    {
        return `Online <span class="online-off-indicator"><img src="icons/online.png"/></span>`;
    }
}
function anotherFetch(type, html){
    $.ajax({
            url: "http://127.0.0.1:8080/database",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: type
            }),
            success: function(resp){
                if (resp.status){
                    $(html).html(populateTable(resp.data))
                }
            }
    })
}

function echoContentTable_SuperUser()
{
    let info = anotherFetch("getUsers", '#table-body-SU')
    $("#table-body-SU>tbody").append(info);
    
}
function populateTable(data)
{
    console.log("populateTable: "+data);
    // data= JSON.parse(data);
    let str = "";
    if($("#table-body-SU").length)
    {
        data.forEach((element, index)=>{
            str += `<tr>
                <td scope ="row" data-label="Name">${element.name}</td>
                <td data-label="Email">${element.email}</td>
                <td data-label="Device_ID">${element.deviceID}</td>
                <td data-label="Type">${element.userType}</td>
                <td data-label="Status" id="${element.name+element.userType}">${fetchStatus(element.deviceID,element.name+element.userType)}</td>
                <td><button data-device="${element.deviceID}" onclick="addUserToSim(this)">Add to simulation</button></td>
                
            </tr>`;

        });
        return str;
    }
}
function addUserToSim(elem){
    var pos = [2,2];

    var ressponse  = addBot($.now(),pos,$(elem).attr("data-device"));
    $("#talbe-simulation").append(ressponse);
}

function addBot(botID,location,deviceID){
    var input ="<input type='number' min='0' max='100' name='floor' value='0'>"
        +"<input type='number' min='0' max='100' name='x' value='1'>"
        +"<input type='number' min='0' max='100' name='y' value='1'>"
    var str ="";
    if(deviceID == null){
        str +=  `<tr>`
        str +=      `<td scope ="row">botID - <span data-label="Name">${botID}</span></td>`
        str +=      `<td data-label="Location"> `+input+` </td>`;
        str +=      `<td data-label="Device_ID"> - </td>`
        str+=       `<td data-label="Type"> BOT</td>`
        str+=       `<td data-label="Status"><input  type="checkbox" id="botStatus-${botID}" onchange="checkBotStatus(this)"/></td>
                </tr>`;
    }
    else{
        str +=  `<tr>`
        str +=      `<td scope ="row">botID - <span data-label="Name">${botID}</span></td>`
        str +=      `<td data-label="Location"> `+input+` </td>`;
        str +=      `<td data-label="Device_ID"> ${deviceID} </td>`
        str+=       `<td data-label="Type"> Person</td>`
        str+=       `<td data-label="Status"><input  type="checkbox" id="botStatus-${botID}" onchange="checkBotStatus(this)"/></td>
                </tr>`;   
    }
    return str;
}

function checkBotStatus(input) {
  var parent = $(input).parent().parent();

  var location = parent.children('[data-label=Location]');
  var floor = location.children('[name=floor]').val();
  var x = parseInt(location.children('[name=x]').val());
  var y = parseInt(location.children('[name=y]').val());
  var device_id = parent.children('[data-label=Device_ID]').html();
  var bot_id = parseInt(parent.find('[data-label=Name]').html());
  var mode = 'simulation';

  if ($(input).is(':checked')){
    docall(bot_id,[x, y], floor, mode,$(input));
  }else{
    remove(bot_id, mode, parent);
  }
  if(device_id!="undefined" || device_id!="-"){

    bindUser(bot_id,device_id,mode)
  }
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

    