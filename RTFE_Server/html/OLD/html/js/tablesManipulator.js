/*Fetching data, used in su_table generator*/
function anotherFetchOfUserData(type, html){
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
//////////////////////////////////////////////
/*General table population function
 send id of the table that needs to be populated
*/
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
				
			</tr>`;

		});
		return str;
	}

	else if($("#table-body-A").length)
	{
		data.forEach((element, index)=>{
			str += `<tr>
				<td scope ="row" data-label="Name">${element.name}</td>
				<td data-label="Email">${element.email}</td>
				<td data-label="Device_ID">${element.mac}</td>
				<td data-label="Type">${element.type}</td>
				<td data-label="Status" id="${element.name+element.type}">${fetchStatus(element.mac,element.name+element.type)}</td>
				<td data-label="Edit" ><button id="${element.email}-edit" onclick="displayOverlayWindow(editUser,'${element.email}', '${element.name}', '${element.userType}', '${element.deviceID}')" class="img-edit"><img src="icons/grey_pensil.png"></button><button class="img-edit"><img class="img-edit" onclick="displayOverlayWindow(removeUser,'${element.email}', '${element.name}', '${element.type}', '${element.mac}')" id="${element.email}-remove" src="icons/grey_duspan.png"></button></td>
			</tr>`;

		});
		return str;
	}
}
/////////////////////////////////////////////////////////////////////////////

/*If user is online than green dot will be shown next to user, otherwise red*/
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
/////////////////////////////////////////////////////////////////////////////

/*Data represents the json string that going to be broken into an actual js objects, this is very specific, to specific type of string that is send from db*/
function createObject(data)
{
	let data1 = data.split("],");
	//console.log(data1);
	let objArray = new Array();

	data1.forEach((element, index)=>{
		element = element.replace(/(\[|\])/g, "");
		//console.log(element + ", "+index);
		let arr1 = new Array();
		let arr = new Array();
		arr1 = element.split(", ");

		//console.log(arr);
		arr1.forEach((e, i)=>{
			if(i === 0)
			{
				arr.id = e;
			}

			else if(i === 1)
			{
				arr.email = e;
			}

			else if(i === 2)
			{
				arr.name = e;
			}

			else if(i === 3)
			{
				arr.password = e;
			}

			else if(i === 4)
			{
				arr.type = e;
			}

			else if(i === 5)
			{
				arr.mac = e;
			}

		});

		
		objArray[index] = arr;
	});
	
	return populateTable(objArray);
}
/////////////////////////////////////////////////////////////////////////////

/*General table updates*/
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
////////////////////////////////////////////////////////////////////////////