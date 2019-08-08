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
                <td><button data-device="${element.deviceID}" onclick="add">Add to simulation</button></td>
                
            </tr>`;

        });
        return str;
    }
}


function addBot(botID,location){
    str += `<tr>
        <td scope ="row" data-label="Name">Bot - ${botID}</td>
        <td data-label="Email"> - </td>
        <td data-label="Device_ID"> - </td>
        <td data-label="Type"> BOT</td>
        <td data-label="Status"><input  type="checkbox" id="botStatus-${botID}" onchange="checkBotStatus(this)" checked/></td>
    </tr>`;


}


function checkBotStatus(input) {
  var x = e.checked
  document.getElementById("demo").innerHTML = "You selected: " + x;
}
function docall(botID,location,HTMLelement){
    $("#table-body-SU").append(str);
    $.ajax({
        url:  URL+"building",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            type: "personUpdate",
            position: location,
            id : botID
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