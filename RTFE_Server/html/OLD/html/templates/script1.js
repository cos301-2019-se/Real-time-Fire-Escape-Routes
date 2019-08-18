var dbUsers = null;

$(()=>{
	/*if($("#top-stat").attr("data-stat") ==="superUser")
	{
		$('#main-body-superuser').show();
	}*/
	pull_user_data();
	$global_building_info = getInfoAboutCurrentBuilding(); // global variables

	let rowForBar = $("#top-bar");
	//fetchFromDb("getUsers");
	setInterval(function(){ anotherFetch('getUsers', '#table-body-SU'); }, 5000);
	rowForBar.append(echoTopBar2);
	let main1 =  $(".main");
	main1.append(`<div class="main-cards">`);
	$(".main-cards").append($('<div></div>')
			.append(echoBuildingCard())
			.append(echoContentTable_SuperUser())
		)
		.append($('<div></div>')
			.append(echoSimulationWindow())
			.append(echoBotCreator())	
			.append(echoFireEditor())
		)

	$("#toggler").on("click", ()=>{
		console.log($("#toggler").attr("aria-expanded"));

		if($("#toggler").attr("aria-expanded") === "true")
		{
			$("#toggler").attr("style", "margin-top: 2px;");
		}
		else
		{
			$("#toggler").attr("style", "margin-top: 100px;");
		}

	})

	/*$("form div div button").on("click", (e)=>{
		e.preventDefault();
	});*/

	if($("#admin-super-choice").length)
	{
		$('#super-user-view').on('click', ()=>{
			console.log($("#super-user-view").hasClass("active"));
			if($(this).hasClass("active"))
			{
				console.log("1");

			}
			else
			{
				$('#admin-view').removeClass("active");
				$("#super-user-view").addClass('active');
				main1.empty();
				main1.append(`<div class="main-cards">`);
					$(".main-cards").append($('<div></div>')
							.append(echoBuildingCard())
							.append(echoContentTable_SuperUser())
						)
						.append($('<div></div>')
							.append(echoSimulationWindow())
							.append(echoBotCreator())	
							.append(echoFireEditor())
						)
				
				
			}

		}); // done on click func
		$('#admin-view').on('click', ()=>{
			

			if($(this).hasClass("active"))
			{
				console.log("2");

			}
			else
			{
				$('#super-user-view').removeClass("active");
				$('#admin-view').addClass('active');
				main1.empty();
				main1.append(`<div class="dependant-card" id="dep"></div>`);
				$("#dep").append(echoBuildingCard());
				$("#dep").append(echoAdminTableView());
			}


		}); // done on click func
	}
	
	var button = '<select id="building-change" style="margin-left:50px;max-height:40px" class="btn btn-default btn-sm"><span style="margin-right:3px" class="glyphicon glyphicon-arrow-down"></span>Change</select>'
	$("#building-card").append(button);
	$("#building-change").change(function(){
		var name = $( this ).val();
		console.log("new val: "+name);
		changeBuilding(name);
	})
	addDropDown();

});

function echoTopBar()
{
	return `<div class="header__name">
		  		<h1 id="name-of-system" class="heading"><img class="icons" src="img/fire.png">
	  					Real-time FER</h1></div>
	  					<ul class="nav__list" id="admin-super-choice">
						    <li  id="super-user-view" class="active nav__list-item">Super-user view</li>
						    <li id="admin-view" class="nav__list-item">Admin view</li>
					  	</ul>
  			<div class="header__avatar"><img id="icons" src="icons/white_person.png"> <span>User Name</span></div>
  					`;

}

function echoTopBar2()
{
	return `<nav class="navbar navbar-expand-lg navbar-light" id="nav2">
				  <a class="navbar-brand header__name" href="#"><img class="" style="width: 220px;" src="img/fireG.png">
	  					</a>
				  <button class="navbar-toggler" style="" id="toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
				    <span class="navbar-toggler-icon"></span>
				  </button>

				  <div class="collapse navbar-collapse" id="navbarSupportedContent">
				    <ul class="navbar-nav mr-auto nav__list" id="admin-super-choice">
						    <li  id="super-user-view" class="active nav__list-item">Super-user view</li>
						    <li id="admin-view" class="nav__list-item">Admin view</li>
					  	</ul>
				     
				  </div>
				</nav>
  					`;

}

function echoBuildingCard()
{
	let name = getBuildingInfo("name");
	if(name != undefined)
	{
		return `
				<div class="card1" id="building-card" style="cursor: pointer;">
		  				<h1 class="heading" id="building-name">${name}</h1>
	  				</div>`;
	}
	else
	{
		return `
				<div class="card1" id="building-card" style="cursor: pointer;">
		  				<h1 class="heading" id="building-name"></h1>
	  				</div>`;
	}
	
}

function getBuildingInfo(name)
{
	let objData;
	$.ajax({
            url: "http://127.0.0.1:8080/database",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type:"currentBuilding"
            }),
            success: function(data){
                if (data.status){
                	
                	objData = data;
                
                	if(name === "name")
					{
						$("#building-name").html(objData.name);
					}
					else if(name === "img")
					{
						$("#building-name").html(objData.name);
						$("#SU-simulation").append(`<img src="../Buildings/${objData.name}/building.jpeg" style="width:100%;"/>`);
					}
                	
                    
                }else{
                   $("#building-name").html("No buildings to display");
                   return;
                }
            }, fail: function(){
				$("#building-name").text("Failed to load building");
				return;
            }
        });

	
}


function echoContentTable_SuperUser()
{
	let info = anotherFetch("getUsers", '#table-body-SU')
	
	if(info != undefined)
	{
		return `			
	  				<div class="card1" id="inner-table-card" style="display: block;">
	  					
	  					<div class="table-heading" style="display: block;"><span class="table-name heading" style="text-align: left;">User Table</span><span class="search-span" style="text-align: right; margin-left: 65%;">
	  						<input type="text" id="search-input" class="searcher" placeholder="Search.." name="search">
     						<button type="submit" class="btn btn-light"><i class="fa fa-search"></i></button>
     						</span>
     						</div>
			  			<div  style="max-height: 350px; overflow-y: auto; display: block;">
	  				
			  				<table style="fit-content" id="tbh">
							   <thead>
								    <tr>
								      <th scope="col">Name</th>
								      <th scope="col">Email</th>
								      <th scope="col">Device_ID</th>
								      <th scope="col">Type</th>
								      <th scope="col">Status</th>
								    </tr>
								</thead>
							  <tbody id="table-body-SU">
							    ${info}
							  </tbody>
							</table>
						</div>
					</div>
			</div>`;
	}
	else return `			
	  				<div class="card1" id="inner-table-card" style="display: block;">
	  					
	  					<div class="table-heading" style="display: block;"><span class="table-name heading" style="text-align: left;">User Table</span>
	  					<span class="search-span" style="text-align: right; margin-left: 65%;">
	  						<input type="text" id="search-input" class="searcher" placeholder="Search.." name="search">
     						<button type="submit" class="btn btn-light"><i class="fa fa-search"></i></button>
     						</span>
     						</div>
			  			<div  style="max-height: 350px; overflow-y: auto; display: block;">
	  				
			  				<table style="fit-content" id="tbh">
							   <thead>
								    <tr>
								      <th scope="col">Name</th>
								      <th scope="col">Email</th>
								      <th scope="col">Device_ID</th>
								      <th scope="col">Type</th>
								      <th scope="col">Status</th>
								    </tr>
								</thead>
							  <tbody id="table-body-SU">
							    
							  </tbody>
							</table>
						</div>
					</div>
			</div>`;
	
}

function echoSimulationWindow()
{
	let img = getBuildingInfo("img");

	if(img != undefined)
	{
		return `<div class="card1" id="SU-simulation">
  					${img}
  			</div>
  		</div>`;
	}
	else
	{
		return `<div class="card1" id="SU-simulation">
  					
  			</div>
  		</div>`;
	}
}

function echoBotCreator()
{
	return `<div id="add-bot-window" class="card1">
  					<button class="btn" id="btn-add-fire" style="margin: 0; padding: 0; display: inline; width: 100%;" ><span style="margin-bottom: 2%;">Add bot</span>
  					<span class="arrow-span" style="margin-bottom: 0.5%;"><img class="icons_small" src="icons/baseline_add_black_48dp.png"></span></button>
  				</div>`;
}
//onclick="displayOverlayWindow(${fireWindow})"
function echoFireEditor()
{
	return `<div id="add-fire-window" class="card1" onclick="displayOverlayWindow(${fireWindow})">
  					<button  class="btn" id="btn-add-fire" style="margin: 0; padding: 0; display: inline; width: 100%;">
  						<span style="margin-bottom: 2%;">Add fire</span>
  						<span class="arrow-span" style="margin-bottom: 0.5%;"><img class="icons_small" src="icons/baseline_add_black_48dp.png"></span></button>
  				</div>`;
}


function echoAdminTableView()
{
	let info = fetchFromDb("getUsers", "table-body-A");
	if(info != undefined)
	{
		return `<div id="main-body-admin" class="card1">
					
	  			<div id="inner-table-card" style="display: block;">
	  					
	  					<div class="table-heading" style="display: block;"><span class="table-name heading" style="text-align: left;">User Table</span>
	  						<span class="search-span" style="text-align: right; margin-left: 76%;">
	  						<input type="text" id="search-input" class="searcher" placeholder="Search.." name="search">
     						<button class="btn btn-light" type="submit"><i class="fa fa-search"></i></button>
     						</span>
     						</div>
			  			<div  style="max-height: 350px; overflow-y: auto; display: block;">
	  				
			  	<table style="fit-content" id="tbh">
				  <thead>
				    <tr>
				      <th scope="col">Name</th>
				      <th scope="col">Email</th>
				      <th scope="col">Device_ID</th>
				      <th scope="col">Type</th>
				      <th scope="col">Status</th>
				      <th scope="col">Edit</th>
				    </tr>
				  </thead>
				  <tbody id="table-body-A">
				    ${info}
				  </tbody>
				</table>
	  			</div>

  					<div style="text-align: right; padding-top: 1%; width: 100%;">
  						<button class="btn btn-light" onclick="displayOverlayWindow(windowForNewUser, null, null, null)">
  							<img style=" width: 10px;" id="img-drop"  src="icons/baseline_add_black_48dp.png"> Add user 
  						</button>

  						<button class="btn btn-light" onclick="displayOverlayWindow(windowForNewBuilding, null, null, null)">
  							<img style=" width: 10px;" id="img-drop" src="icons/baseline_add_black_48dp.png"> Add building 
  						</button>
  					
			</div>`;
	}
	else
		return `<div id="main-body-admin" class="card1">
					
	  			<div id="inner-table-card" style="display: block;">
	  					
	  					<div class="table-heading" style="display: block;"><span class="table-name heading" style="text-align: left;">User Table</span>
	  					<span class="search-span" style="text-align: right; margin-left: 76%;">
	  						<input id="search-input" class="searcher" type="text" placeholder="Search.." name="search">
     						<button class="btn btn-light" type="submit"><i class="fa fa-search" ></i></button>
     						</span>
     						</div>
			  			<div  style="max-height: 350px; overflow-y: auto; display: block;">
	  				
			  	<table style="fit-content" id="tbh">
				  <thead>
				    <tr>
				      <th scope="col">Name</th>
				      <th scope="col">Email</th>
				      <th scope="col">Device_ID</th>
				      <th scope="col">Type</th>
				      <th scope="col">Status</th>
				      <th scope="col">Edit</th>
				    </tr>
				  </thead>
				  <tbody id="table-body-A">
				    
				  </tbody>
				</table>
	  			</div>

  					<div style="text-align: right; padding-top: 1%; width: 100%;">
  						<button class="btn btn-light" onclick="displayOverlayWindow(windowForNewUser, null, null, null)">
  							<img style=" width: 10px;" id="img-drop"  src="icons/baseline_add_black_48dp.png"> Add user 
  						</button>

  						<button class="btn btn-light" onclick="displayOverlayWindow(windowForNewBuilding, null, null, null)">
  							<img style=" width: 10px;" id="img-drop" src="icons/baseline_add_black_48dp.png"> Add building 
  						</button>
  					
  					</div>
			</div>`;
	
}

function fetchFromDb(dataType, id)
{
	//debugger;
	
	$.ajax({
            url: "http://127.0.0.1:8080/database",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: dataType
            }),
            success: function(data){
                if (data.status){
                	//console.log(data.data);
                    //console.log(createObject(data.data));
                    // let obj = createObject(data.data);

                    // if(obj.length)
                    // {
                    	
                    // 	$(`#${id}`).append(obj);
                    // }
                    
                }else{
                   /*SOMETHING TO BE ADDED*/
                   
                   $(`#${id}`).append("");
                }
            }, fail: function(){
				$(`#${id}`).append("");
				console.log("here");
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
				<td data-label="Device_ID">${element.deviceID}</td>
				<td data-label="Type">${element.userType}</td>
				<td data-label="Status" id="${element.name+element.type}">${fetchStatus(element.deviceID,element.name+element.userType)}</td>
				<td data-label="Edit" ><button id="${element.email}-edit" onclick="displayOverlayWindow(editUser,'${element.email}', '${element.name}', '${element.userType}', '${element.deviceID}')" class="img-edit"><img src="icons/grey_pensil.png"></button><button class="img-edit"><img class="img-edit" onclick="displayOverlayWindow(removeUser,'${element.email}', '${element.name}', '${element.type}', '${element.mac}')" id="${element.email}-remove" src="icons/grey_duspan.png"></button></td>
			</tr>`;

		});
		return str;
	}
}

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

function displayOverlayWindow(contentFunc, user, name, type, device)
{
	//debugger;
	if(user != null)
	{
		$("#overlay-window").removeAttr("style");

		$("#overlay-window").append(`<div id="contentCard" style="background-color: lightgrey;" class="rtferCard">
  				${contentFunc(user, name, type, device)}
  			</div>`);
	}
	else
	{
		$("#overlay-window").removeAttr("style");

		$("#overlay-window").append(`<div id="contentCard" style="background-color: lightgrey;" class="rtferCard">
  				${contentFunc()}
  			</div>`);
	}
}

function windowForNewUser()
{
	return `<div class="row">
		<div style="text-align: center;" class="col-sm-12">
			<h1>Add new user</h1>
		</div>
	</div>
	<div>
		<div id="addUser">
		  <div class="form-group">
		    <label for="fullName-addUser">Full name</label>
		    <input type="text" class="form-control" id="fullName-addUser" placeholder="Full Name" required>
		  </div>
		  <div class="form-group">
		    <label for="email-addUser">Email</label>
		    <input type="email" class="form-control" id="email-addUser" placeholder="Email" required>
		  </div>
		  <div class="form-group">
		    <label for="setPassword-addUser">Set password</label>
		    <input type="password" class="form-control" id="setPassword-addUser" placeholder="password" required>
		  </div>
		  <div class="form-group">
		    <label for="type-addUser">Set type</label>
		    <select class="custom-select mr-sm-2" id="type-addUser">

		        <option selected>Agent</option>
		        <option value="1">Admin</option>
		    </select>
		  </div>
		  <div class="form-group row button-line">
		    <div class="col-sm-12" style="text-align: right;">
		      <button onclick="getInfoFromInput('addUser', null)" id="submit-new-user" class="btn btn-info">Send request</button>
		      <button onclick="clearWindow()" id="cancel-new-user" class="btn btn-danger">Cancel</button>
		    </div>
		  </div>
		</div>
	</div>
	`;
}

function windowForNewBuilding()
{
	return `<div class="row">
		<div style="text-align: center;" class="col-sm-12">
			<h1>Add new building</h1>
		</div>
	</div>
	<div>
		<div>
		  <div class="form-group">
		    <label for="buildingName">Building name</label>
		    <input type="text" class="form-control" id="buildingName" placeholder="Building1">
		  </div>
		  <div class="form-group">
		    <label for="buildingLocation">Building location</label>
		    <input type="text" class="form-control" id="buildingLocation" placeholder="Location...">
		  </div>
		  <div class="form-group">
		    <label for="numOfFloors">Number of Floors</label>
		    <input type="number" class="form-control" id="numOfFloors" placeholder="1" min="1">
		  </div>
		  <div class="form-group">
		  	<label for="fileupload">Upload plan</label>
		  	<div class="row" padding: 2%;>
		  	 <div class="col-sm-5 custom-file">
				<label style="width: 250px; margin-left: 3.3%;" class="custom-file-label" for="uploadImg"><img style="width: 25px; background-color: transparent;" src="icons/greyFolder.png"/>Upload img</label>
				<input type="file" class="custom-file-input" id="uploadImg">
		  	 </div>
		  	 <div class="col-sm-5 custom-file">
		  	 	<label style="width: 250px;" class="custom-file-label" for="uploadJson"><img style="width: 25px;" src="icons/jsonFile.png"/>Upload building</label>
				<input type="file" class="custom-file-input" id="uploadJson">
		  	 </div>
		  	</div>
		   
		  </div>
		  <div class="form-group row button-line" style="margin-top: 2%;">
		    <div class="col-sm-12" style="text-align: right;">
		      <button type="submit" onclick="getInfoFromInput('addBuilding', null)"  id="submit-new-building" class="btn btn-info">Save</button>
		      <button id="cancel-new-building" class="btn btn-danger"  onclick="clearWindow()">Cancel</button>
		    </div>
		  </div>
		</div>
	</div>
	`;
}


function clearWindow()
{
	$("#overlay-window").empty();
	$("#overlay-window").attr("style", "display: none;");
}

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

function editUser(user, name, type, device)
{
	console.log(name);
	return `<div class="row">
		<div style="text-align: center;" class="col-sm-12">
			<h1>Eddit user ${name}</h1>
		</div>
	</div>
	<div>
		<div>
		  <div class="form-group">
		    <label for="fullName-edditUser">Change name</label>
		    <input type="text" class="form-control" id="fullName-edditUser" value="${name}">
		  </div>
		  <div class="form-group">
		    <label for="email-edditUser">Change email</label>
		    <input type="email" class="form-control" id="email-edditUser" value="${user}">
		  </div>
		  <div class="form-group">
		    <label for="setPassword-edditUser">Change password</label>
		    <input type="password" class="form-control" id="setPassword-edditUser" placeholder="Do not fill in to keep it same">
		  </div>
		  <div class="form-group">
		    <label for="type-edditUser">Change type</label>
		    <select class="custom-select mr-sm-2" id="type-edditUser">
		    	${selectType(type)}
		    </select>
		  </div>
		  <div class="form-group row button-line">
		    <div class="col-sm-12" style="text-align: right;">
		      <button onclick="getInfoFromInput('addInfo', '${user}')" id="submit-new-user" class="btn btn-info">Change info</button>
		      <button onclick="clearWindow()" id="cancel-new-user" class="btn btn-danger">Cancel</button>
		    </div>
		  </div>
		</div>
	</div>
	`;
}

function removeUser(user, name, type, device)
{
		return `<div class="row">
				<div style="text-align: center; margin-bottom: 3%; margin-top: 3%;" class="col-sm-12">
					<h1>Do you want to remove ${name} from your system?</h1>
				</div>
			</div>
			<div>
				  <div class="form-group">
				    <h3>Full name:</h3>
				    <p>${name}</p>
				  </div>
				  <div class="form-group">
				    <h3>Email:</h3>
				    <p>${user}</p>
				  </div>
				  <div class="form-group">
				    <h3>Device_ID:</h3>
				    <p>${device}</p>
				  </div>
				  <div class="form-group">
				    <h3>Type:</h3>
				    <p>${type}</p>
				  </div>
				  <div class="form-group row button-line">
				    <div class="col-sm-12" style="text-align: right;">
				      <button type="submit" class="btn btn-info" onclick="deleteUser('${user}', '${name}')">Delete user</button>
				      <button onclick="clearWindow()" id="cancel-delete" class="btn btn-danger">Cancel</button>
				    </div>
				  </div>
			</div>
			`;
}

function selectType(type)
{
	if(type === "Agent")
		{return `
		        <option selected>Agent</option>
		        <option value="1">Admin</option>
		        `} 
		else{ return `
		        	<option>Agent</option>
		        	<option value="1" selected>Admin</option>
		        	`}
}

function getInfoFromInput(callFunc, email1)
{	
	if(callFunc === "addUser")
	{
		let dataType = "register";
		let name = $("#fullName-addUser").val();
		let email = $("#email-addUser").val();
		let pass = $("#setPassword-addUser").val();
		let userType = $("#type-addUser option:selected").val();
        console.log($global_building_info);
		let currentBuilding = $("#type-addUser option:selected").val();
		console.log(dataType);
		console.log(name);
		console.log(email);
		console.log(pass);
		console.log(userType);
		addUser(dataType, name, email, pass, userType);
	}


/////////////////////////////////////////////////////////////////////////
	else if(callFunc === "addInfo")
	{
		let dataType = "update";
		let updateName, updateEmail, updatePass, updateType;
		let name = $("#fullName-edditUser").val();
		console.log(name+ " the name");
		if(name != undefined && name != "" && name != null && name != " ")
		{
			console.log("I am here");
			updateName = updateInfo(dataType, "name", email1, name);
			if(updateName === "false")
			{
				notify("Failed to update user name", 'red');
				clearWindow();
				return;
			}
		}

		
		let pass = $("#setPassword-edditUser").val();
		if(pass != undefined || pass === "Do not fill in to keep it same" || pass != " " || pass != null)
		{
			updatePass = updateInfo(dataType, 'pass', email1, name);
			if(updatePass === "false")
			{
				notify("Failed to update user password", 'red');
				clearWindow();
				return;
			}
		}
		let userType = $("#type-edditUser option:selected").val();
		if(userType != undefined)
		{
			updateType = updateInfo(dataType, 'usertype', email1, name);
			if(updateType === "false")
			{
				notify("Failed to update user type", 'red');
				clearWindow();
				return;
			}
		}

		let email = $("#email-edditUser").val();
		if(email != undefined || email != " " || email != null)
		{
			updateEmail = updateInfo(dataType, 'email', email1, name);
			if(updateEmail === "false")
			{
				notify("Failed to update user email", 'red');
				clearWindow();
				return;
			}
		}

		notify("User's information was successfully changed", 'green');

		updateTable();
        clearWindow();

		//addInfo(dataType, name, email, pass, userType);
	}
///////////////////////////////////////////////////////////////////////////
	else if(callFunc === "addBuilding")
	{
		let name = $("#buildingName").val();
		let img = null;
		let file = null;
		addBuilding(file, name, img);
	}

	else if(callFunc === addFire)
	{
		let x = $("#x-coordinate").val();
		let y = $("#y-coordinate").val();
		let rad = $("#rad-number").val();
		let floor = $("#floor-number").val();
		addFire(x, y, rad, floor);
	}


		clearWindow();
}

function addUser(dataType, name, email, pass, userType, currentBuilding)
{
	//debugger;
	console.log('over here')
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
                userType: userType,
                buildingName: currentBuilding
            }),
            success: function(data){
                if (data.status){
                	console.log('Successfully added a new User');
                	updateTable();
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
                	updateTable();
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

function updateTable()
{
	$(".main").empty();
	$(".main").append(`<div class="dependant-card" id="dep"></div>`);
	$("#dep").append(echoBuildingCard());
	$("#dep").append(echoAdminTableView());

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

function fireWindow()
{
	let maxX = 100;
	let maxY= 100;
	let maxFloor;
	console.log($global_building_info);
	if($global_building_info != null && $global_building_info != 'No buildings found')
	{
		maxFloor = $global_building_info.numberFloors-1;
	}
	else
	{
		maxFloor = 1;
	}
	
	console.log(maxFloor);
	let maxRad = 360;
	// question how to get largest x, y, radius
	return `<div> 
				<h1 id='fire-head'>Simulate fire in the building</h1>
			</div>
			<div class='card-content'>
				<div class='form-group'>
					<label for='x-coordinate'>X</label>
		    		<input type='number' min='0' max='${maxX}' class='form-control' id='x-coordinate' value='0'>
				</div>
				<div class='form-group'>
					<label for='y-coordinate'>Y</label>
		    		<input type='number' min='0' max='${maxY}' class='form-control' id='y-coordinate' value='0'>
				</div>
				<div class='form-group'>
					<label for='rad-number'>Radius</label>
		    		<input type='number' min='0' max='${maxRad}' class='form-control' id='rad-number' value='0'>
				</div>
				<div class='form-group'>
					<div class='form-group'>
						<label for='floor-number'>Floors</label>
		    			<input type='number' min='0' max='${maxFloor}' class='form-control' id='floor-number' value='0'>
					</div>
				</div>
			</div>
			<div class='form-group row button-line'>
				    <div class='col-sm-12' style='text-align: right;'>
				      <button type= 'submit' class='btn btn-info' onclick='getInfoFromInput(addFire, null)'>Add fire</button>
				      <button onclick='clearWindow()' id='cancel-delete ' class='btn btn-danger'>Cancel</button>
				    </div>
				  </div>
		`;
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

function getInfoAboutCurrentBuilding()
{
	$.ajax({
            url: "http://127.0.0.1:8080/buildingGeneration",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: "buildingData"
            }),
            success: function(data){
                if (data.status){
                	//console.log(data);
                	return data;
                }else{
                	return "No buildings found"     
                }
            }, fail: function(){
				console.log('fail to get buildings');
				return null;
            }
        });
}


/*Function notify*/

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
            }).delay(3000).animate({
                top: "-=80px"
            }, function(){
                $(this).remove();
            })
        )
    }

function handleFileSelect(id)
  {               
    if (!window.File || !window.FileReader || !window.FileList || !window.Blob) {
      alert('The File APIs are not fully supported in this browser.');
      return;
    }   

    input = document.getElementById(id);
    if (!input) {
      alert("Could not find the file.");
    }
    else if (!input.files) {
      alert("This browser doesn't seem to support the `files` property of file inputs.");
    }
    else if (!input.files[0]) {
      alert("Please select a file before clicking 'Save'");               
    }
    else {
      file = input.files[0];
      fr = new FileReader();
      //fr.onload = receivedText;
     // fr.readAsDataURL(file);
    }
  }
