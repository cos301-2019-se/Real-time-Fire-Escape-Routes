/*Echoes table in admin view*/
function echoAdminTableView()
{
	let info = fetchAllUserData("getUsers", "table-body-A");
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
///////////////////////////////////////////////////////////////////////////////////

/*Populates newly created overlay window with html elements for adding new user to the system*/
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

///////////////////////////////////////////////////////////////////////////////////////////////////

/*Populates newly created overlay window with html elements for adding new building to the system*/
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
/////////////////////////////////////////////////////////////////////////////////////////////////

/*Layout of the card for edditing current user*/
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
///////////////////////////////////////////////////////////////////////////////////////////////////

/*Layout that will be showed when one pressed on dustbian*/
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
///////////////////////////////////////////////////////////////////////////////////////////////////

/*dependig on what the user is that what will be selected by default in the edit user window*/
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
/////////////////////////////////////////////////////////////////////////////////////////////////

/*Automatically updates table after table values were changed*/
function updateTable()
{
	$(".main").empty();
	$(".main").append(`<div class="dependant-card" id="dep"></div>`);
	$("#dep").append(echoBuildingCard());
	$("#dep").append(echoAdminTableView());

}
//////////////////////////////////////////////////////////////