var activeSide = true; // true for super user view false for admin

$(()=>{
	/*if($("#top-stat").attr("data-stat") ==="superUser")
	{
		$('#main-body-superuser').show();
	}*/
	let rowForBar = $("#top-bar");

	rowForBar.append(echoTopBar);

	let rowForContent = $("#content");
	rowForContent.append(echoContentTable_SuperUser());
	rowForContent.append(echoSimulationWindow());
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
				rowForContent.empty();
				rowForContent.append(echoContentTable_SuperUser());
				rowForContent.append(echoSimulationWindow());
			}

		}); // done on click func
		$('#admin-view').on('click', ()=>{
			

			if($(this).hasClass("active btn"))
			{
				console.log("2");

			}
			else
			{
				$('#super-user-view').removeClass("active");
				$('#admin-view').addClass('active');
				rowForContent.empty();
				rowForContent.append(echoAdminTableView());
			}


		}); // done on click func
	}
	
});

function echoTopBar()
{
	return `<div class="col-4" style="padding: 0; padding-top: 1%; ">
  						<h1 id="sys-name" style="margin-left: 2%; ">
	  					<img id="img-logo" src="img/grey.png">
	  					Real-time FER
	  					</h1>
  					</div>
  					<div class="col-4" id="admin-super-choice" style="padding: 0; margin: 0; padding-top: 0.5%; padding-bottom: 0.5%;">
  							<button id="super-user-view" data-section="super-user-view" class="active btn">Super-user view</button> 
							<button class="btn" data-section="admin-view" id="admin-view">Admin view</button>
  					</div>
  					<div class="col-4" style="text-align: right; margin: 0; padding: 0; padding-top: 1%;">
  						
	  					<button style="margin: 0; margin-right: 2%; padding: 1%; padding-bottom: 0; display: inline; color: aliceblue;" class="btn"><img id="img-drop" src="icons/white_person.png"> User Name</button>
	  					
  					</div>
  					`;

}

function echoContentTable_SuperUser()
{
	return `<div class="col-md-5" id="SU-table" >
  				
	  			<div class="row">
	  				<div class="col-12" id="building-drop">
		  				<h1 style="color: white;">Building name
		  				<button style="margin: 0; padding: 0; display: inline;" class="btn"><img id="img-drop" src="icons/white_arrow_down.png"></button>
		  				</h1>
	  				</div>
	  			</div>
	  				<div class="rtferCard">
	  					<div class="row">
	  					<div class="col-5"><h3 style="color: white;">User Table</h3></div>
	  					<div class="col-7" id="search-input-div">
	  						<img id="img-drop" src="icons/baseline_search_black_18dp.png">
	  						<input id="search-input" class="form-control" type="search" placeholder="Search" aria-label="Search">
	  					</div>
	  				</div>
	  				<table class="table">
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
					    <tr>
					      <td scope="row">John Doe</td>
					      <td>doe@gmail.com</td>
					      <td>123456789</td>
					      <td>Admin</td>
					      <td>${statusIdentifier("Offline")}</td>
					      
					    </tr>
					    <tr>
					      <td scope="row">John Moe</td>
					      <td>moe@gmail.com</td>
					      <td>123456789</td>
					      <td>Agent</td>
					      <td>${statusIdentifier("Offline")}</td>
					      
					    </tr>
					    <tr>
					      <td scope="row">Larry the Bird</td>
					      <td>theBird@gmail.com</td>
					      <td>123456789</td>
					      <td>Agent</td>
					      <td>${statusIdentifier("Online")}</td>
					      
					    </tr>
					  </tbody>
					</table>
				</div>
			</div>`;
}

function echoSimulationWindow()
{
	return `<div class="col-md" id="SU-simulation">
  				<div id="simulation-window" class="rtferCard">
  					Simulation
  				</div>
  				<div id="add-bot-window" class="row rtferCard">
  					<!--<div class="col-sm-6">
  						Add bot
  					</div>
  					<div class="col-sm-6">
  						<img id="img-drop" src="icons/grey_small_arrow.png">
  					</div>-->
  					<span style="margin-bottom: 2%;">Add bot</span>
  					<span class="arrow-span" style="margin-bottom: 2%;"><button style="margin: 0; padding: 0; display: inline;" class="btn"><img id="img-drop" src="icons/grey_small_arrow.png"></button></span>
  				</div>
  				<div id="add-fire-window" class="row rtferCard">
  					<span style="margin-bottom: 2%;">Add fire</span>
  					<span class="arrow-span" style="margin-bottom: 2%;"><button style="margin: 0; padding: 0; display: inline;" class="btn"><img id="img-drop" src="icons/grey_small_arrow.png"></button></span>
  				</div>
  			</div>
  		</div>`;
}


function echoAdminTableView()
{
	return `<div id="main-body-admin">
	  			<div class="row">
	  				<div class="col-7" style="text-align: right; padding-bottom: 1%;">
	  				<h1 style="color: white;">Building name
	  				<button style="margin: 0; padding: 0; display: inline;" class="btn"><img id="img-drop" src="icons/white_arrow_down.png"></button>
	  				</h1>
  					</div>
  					<div class="col-5" style="text-align: right; padding-top: 1%;">
  						<button class="btn btn-light" onclick="displayOverlayWindow(windowForNewBuilding)">
  							<img style=" width: 10px;" id="img-drop" src="icons/baseline_add_black_48dp.png"> Add building 
  						</button>
  					</div>
	  			</div>
	  			<div class="rtferCard" id="adminView-table">
  				<div class="row">
  					<div class="col-6"><h3 style="color: white;">User Table</h3></div>
  					<div class="col-6" id="search-input-div">
  						<img id="img-drop" src="icons/baseline_search_black_18dp.png">
  						<input id="search-input" class="form-control" type="search" placeholder="Search" aria-label="Search">
  					</div>
  				</div>
  				
  				
  				<table class="table">
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
				    <tr>
				      <td scope="row">John Doe</td>
				      <td>doe@gmail.com</td>
				      <td>123456789</td>
				      <td>Admin</td>
				      <td>${statusIdentifier("Offline")}</td>
				      <td ><button class="img-edit"><img src="icons/grey_pensil.png"></button><button class="img-edit"><img class="img-edit" src="icons/grey_duspan.png"></button></td>
				    </tr>
				    <tr>
				      <td scope="row">John Moe</td>
				      <td>moe@gmail.com</td>
				      <td>123456789</td>
				      <td>Agent</td>
				      <td>${statusIdentifier("Offline")}</td>
				      <td ><button class="img-edit"><img src="icons/grey_pensil.png"></button><button class="img-edit"><img class="img-edit" src="icons/grey_duspan.png"></button></td>
				    </tr>
				    <tr>
				      <td scope="row">Larry the Bird</td>
				      <td>theBird@gmail.com</td>
				      <td>123456789</td>
				      <td>Agent</td>
				      <td>${statusIdentifier("Online")}</td>
				      <td><button class="img-edit"><img src="icons/grey_pensil.png"></button><button class="img-edit"><img class="img-edit" src="icons/grey_duspan.png"></button></td>
				    </tr>
				  </tbody>
				</table>
				<div class="row">
	  				<div class="col-7" style="text-align: right; padding-bottom: 1%;">
	  				
  					</div>
  					<div class="col-5" style="text-align: right; padding-top: 1%;">
  						<button class="btn btn-light" onclick="displayOverlayWindow(windowForNewUser)">
  							<img style=" width: 10px;" id="img-drop"  src="icons/baseline_add_black_48dp.png"> Add user 
  						</button>
  					</div>
	  			</div>
			</div>`;
}

function fetchFromDb(dataType)
{

}

function tablePopulation(data)
{
	if($("#table-body-SU").length)
	{

	}

	else if($("#table-body-A").length)
	{
		
	}
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