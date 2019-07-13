$(()=>{
	/*if($("#top-stat").attr("data-stat") ==="superUser")
	{
		$('#main-body-superuser').show();
	}*/
	let rowForBar = $("#top-bar");
	//fetchFromDb("getUsers");
	rowForBar.append(echoTopBar);
	let main1 =  $(".main");
	main1.append(`<div class="main-cards">`);
	let main = $(".main-cards");
	main.append(echoBuildingCard());
	main.append(echoContentTable_SuperUser());
	main.append(echoSimulationWindow());
	main.append(echoBotCreator());
	main.append(echoFireEditor());

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
				main = $(".main-cards");
				main.append(echoBuildingCard());
				main.append(echoContentTable_SuperUser());
				main.append(echoSimulationWindow());
				main.append(echoBotCreator());
				main.append(echoFireEditor());
				
				
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
	
});

function echoTopBar()
{
	return `<div class="header__name">
		  		<h1 id="name-of-system" class="heading"><img class="icons" src="img/grey.png">
	  					Real-time FER</h1></div>
	  					<ul class="nav__list" id="admin-super-choice">
						    <li id="super-user-view" class="active nav__list-item">Super-user view</li>
						    <li id="admin-view" class="nav__list-item">Admin view</li>
					  	</ul>
  			<div class="header__avatar"><img id="icons" src="icons/white_person.png"> User Name</div>
  					`;

}

function echoBuildingCard()
{
	return `
				<div class="card1" id="building-card" style="cursor: pointer;">
		  				<h1 class="heading">Building name <img class="icons_small" src="icons/white_arrow_down.png">
		  				</h1>
	  				</div>`;
}

function echoContentTable_SuperUser()
{
	let info = fetchFromDb("getUsers", "table-body-SU");
	console.log(info);
	if(info != undefined)
	{
		return `			
	  				<div class="card1" id="inner-table-card" style="display: block;">
	  					
	  					<div class="table-heading" style="display: block;"><span class="table-name heading" style="text-align: left;">User Table</span><span class="search-span" style="text-align: right; margin-left:45%;"><img class="icons" src="icons/baseline_search_black_18dp.png">
  						<input id="search-input" class="searcher" type="search" placeholder="Search..."  aria-label="Search"></span></div>
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
	  					
	  					<div class="table-heading" style="display: block;"><span class="table-name heading" style="text-align: left;">User Table</span><span class="search-span" style="text-align: right; "><img class="icons" src="icons/baseline_search_black_18dp.png">
  						<input id="search-input" class="searcher" type="search" placeholder="Search" aria-label="Search"></span></div>
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
	return `<div class="card1" id="SU-simulation">
  					Simulation
  			</div>
  		</div>`;
}

function echoBotCreator()
{
	return `<div id="add-bot-window" class="card1">
  					<span style="margin-bottom: 2%;">Add bot</span>
  					<span class="arrow-span" style="margin-bottom: 2%;"><button id="btn-add-bot" style="margin: 0; padding: 0; display: inline;" ><img class="icons_small" src="icons/grey_small_arrow.png"></button></span>
  				</div>`;
}

function echoFireEditor()
{
	return `<div id="add-fire-window" class="card1">
  					<span style="margin-bottom: 2%;">Add fire</span>
  					<span class="arrow-span" style="margin-bottom: 2%;"><button id="btn-add-fire" style="margin: 0; padding: 0; display: inline;" ><img class="icons_small" src="icons/grey_small_arrow.png"></button></span>
  				</div>`;
}


function echoAdminTableView()
{
	let info = fetchFromDb("getUsers", "table-body-A");
	if(info != undefined)
	{
		return `<div id="main-body-admin" class="card1">
					
	  			<div id="inner-table-card" style="display: block;">
	  					
	  					<div class="table-heading" style="display: block;"><span class="table-name heading" style="text-align: left;">User Table</span><span class="search-span" style="text-align: right; margin-left: 75%;"><img class="icons" src="icons/baseline_search_black_18dp.png">
  						<input id="search-input" class="searcher" type="search" placeholder="Search" aria-label="Search"></span></div>
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
  						<button class="btn btn-light" onclick="displayOverlayWindow(windowForNewUser)">
  							<img style=" width: 10px;" id="img-drop"  src="icons/baseline_add_black_48dp.png"> Add user 
  						</button>
  						<button class="btn btn-light" onclick="displayOverlayWindow(windowForNewBuilding)">
  							<img style=" width: 10px;" id="img-drop" src="icons/baseline_add_black_48dp.png"> Add building 
  						</button>
  					</div>
  					
			</div>`;
	}
	else
		return `<div id="main-body-admin" class="card1">
					
	  			<div id="inner-table-card" style="display: block;">
	  					
	  					<div class="table-heading" style="display: block;"><span class="table-name heading" style="text-align: left;">User Table</span><span class="search-span" style="text-align: right; margin-left: 75%;"><img class="icons" src="icons/baseline_search_black_18dp.png">
  						<input id="search-input" class="searcher" type="search" placeholder="Search" aria-label="Search"></span></div>
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
  						<button class="btn btn-light" onclick="displayOverlayWindow(windowForNewUser)">
  							<img style=" width: 10px;" id="img-drop"  src="icons/baseline_add_black_48dp.png"> Add user 
  						</button>

  						<button class="btn btn-light" onclick="displayOverlayWindow(windowForNewBuilding)">
  							<img style=" width: 10px;" id="img-drop" src="icons/baseline_add_black_48dp.png"> Add building 
  						</button>
  					
  					</div>
			</div>`;
	
}

function fetchFromDb(dataType, id)
{
	//debugger;
	console.log('over here')
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
                    let obj = createObject(data.data);

                    if(obj.length)
                    {
                    	
                    	$(`#${id}`).append(obj);
                    }
                    
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

function populateTable(data)
{
	let str = "";
	if($("#table-body-SU").length)
	{
		data.forEach((element, index)=>{
			str += `<tr>
				<td scope ="row" data-label="Name">${element.name}</td>
				<td data-label="Email">${element.email}</td>
				<td data-label="Device_ID">${element.mac}</td>
				<td data-label="Type">${element.type}</td>
				<td data-label="Status" id="${element.name+element.type}">${fetchStatus(element.mac,element.name+element.type)}</td>
				
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
				<td data-label="Edit" ><button id="${element.email}-edit" onclick="editUser(${element.email})" class="img-edit"><img src="icons/grey_pensil.png"></button><button class="img-edit"><img class="img-edit" onclick="removeUser(${element.email})" id="${element.email}-remove" src="icons/grey_duspan.png"></button></td>
			</tr>`;

		});
		return str;
	}
}

function fetchStatus(mac,identify)
{
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

function displayOverlayWindow(contentFunc)
{
	$("#overlay-window").removeAttr("style");

	$("#overlay-window").append(`<div id="contentCard" style="background-color: lightgrey;" class="rtferCard">
  				${contentFunc()}
  			</div>`);
	
}

function windowForNewUser()
{
	return `<div class="row">
		<div style="text-align: center;" class="col-sm-12">
			<h1>Add new user</h1>
		</div>
	</div>
	<div>
		<form>
		  <div class="form-group">
		    <label for="fullName">Full name</label>
		    <input type="text" class="form-control" id="fullName" placeholder="Full Name">
		  </div>
		  <div class="form-group">
		    <label for="email">Email</label>
		    <input type="text" class="form-control" id="email" placeholder="Email">
		  </div>
		  <div class="form-group">
		    <label for="setPassword">Set password</label>
		    <input type="password" class="form-control" id="setPassword" placeholder="password">
		  </div>
		  <div class="form-group">
		    <label for="type">Set type</label>
		    <select class="custom-select mr-sm-2" id="type">
		        <option selected>Agent</option>
		        <option value="1">Admin</option>
		    </select>
		  </div>
		  <div class="form-group row">
		    <div class="col-sm-12" style="text-align: right;">
		      <button type="submit" id="submit-new-user" class="btn btn-info">Send request</button>
		      <button onclick="clearWindow()" id="cancel-new-user" class="btn btn-danger">Cancel</button>
		    </div>
		  </div>
		</form>
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
		<form>
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
		  <div class="form-group row" style="margin-top: 2%;">
		    <div class="col-sm-12" style="text-align: right;">
		      <button type="submit" id="submit-new-building" class="btn btn-info">Save</button>
		      <button id="cancel-new-building" class="btn btn-danger"  onclick="clearWindow()">Cancel</button>
		    </div>
		  </div>
		</form>
	</div>
	`;
}


function clearWindow()
{
	$("#overlay-window").empty();
	$("#overlay-window").attr("style", "display: none;");
}

function droppingWindow()
{

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

function editUser(user)
{

}

function removeUser(user)
{

}


/*
{
	"type": "register",
    "name" : "John Little",
    "email" : "little@gmail.com",
    "pass" : "pass57",
    "userType" : "admin"
}

{
	"type": "getUsers",
    
}*/