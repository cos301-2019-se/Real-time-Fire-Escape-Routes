function echoAdminTableView()
{
	// let info = fetchFromDb("getUsers", "table-body-A",true);
	var table = `
		<div id="main-body-admin" class="card1" style="width:60%; margin:auto">
			<div class="pad">
				<h1>Active Building: <span id="ActiveBuilding">loading...</span></h1>
				<h3>Change Building <select id="buildingDropDown"></select></h3>
			</div>
			<hr/>
			<div>
				<div id="inner-table-card" style="display: block;">
					<div id="admin-table-header" style="display: block;">

					<!--<span class="table-name heading" style="text-align: left;">User Table</span>-->

						<span class="search-span">
							<div class="search-button" id="admin-search-button" onclick="highlightSearchField()"><img class="search-icon" src="icons/search_grey.png"><i class="fa fa-search"></i>
							</div>
							<input type="text" id="search-input" class="searcher" placeholder="Search.." name="search"  onkeyup="search(this,'table-body-A')">
						</span>
					</div>
				<div>	
				<table id="tbh" class="fixed_header">
					<thead>
						<tr>
						
							<td scope="col" class="status-column" style="color:transparent">S</td>
							<td scope="col" class="name-column">Name</td>
							<td scope="col" class="email-column">Email</td>
							<td scope="col" class="type-column">Type</td>
							<td scope="col" class="device-column">Device ID</td>
							<td scope="col" class="edit-column align-center">Edit</td>
						</tr>
					</thead>
					<tbody id="table-body-A">
					</tbody>
				</table>
			</div
		</div>

		<div style="text-align: right; padding-top: 1%; width: 100%;">
			<button id = "addButton" class="btn btn-light" onclick="displayOverlayWindow(windowForNewUser, null, null, null)">
				<img style=" width: 10px;" id="img-drop"  src="icons/baseline_add_black_48dp.png"> Add user 
			</button>
			<button id = "addButton"  class="btn btn-light" onclick="displayOverlayWindow(windowForNewBuilding, null, null, null)">
				<img style=" width: 10px;" id="img-drop" src="icons/baseline_add_black_48dp.png"> Add building 
			</button>
		</div>`;
	return table;	
}


function echoContentTable_SuperUser()
{
	var table =`
	<div>
		<div class="half" id="inner-table-card">				
			<div class="table-heading" style="display: block;">

				<!--<span class="table-name heading" style="text-align: left;">User Table</span>-->
				<div class="search-button" onclick="highlightSearchField()">
						<img class="search-icon" src="icons/search_grey.png">
						<i class="fa fa-search"></i>
					</div>
				<span class="search-span" style="text-align: right; margin-left: 50%;">
					<input type="text" id="search-input" class="searcher" onkeyup="search(this,'table-body-SU')" placeholder="Search.." name="search">
					
				</span>
			</div>
			<div >
  				<table class="fixed_header" style="fit-content" id="tbh">
				   <thead>
					    <tr>
					    	<td scope="col" class="status-column"></td>
							<td scope="col" class="name-column">Name</td>
							<td scope="col" class="email-column">Email</td>
							<td scope="col" class="type-column">Type</td>
							<td scope="col" class="device-column">Device ID</td>
							
					    </tr>
					</thead>
				  	<tbody id="table-body-SU">
				  	</tbody>
				</table>
			</div>
		</div>
		<div class="half">
			<div class="pad">
				<button onclick="alarm(true,'live')">Trigger Alarm</button>
				<button onclick="alarm(false,'live')">Reset Alarm</button>
			</div>
			<hr/>
			<div class="pad">
				<h1>Active Building: <span id="ActiveBuilding">loading...</span></h1>
				<h3>Change Building <select id="buildingDropDown"></select></h3>
			</div>
			<div id="ActiveBuildingImage">
				<img id="buildingPicture" src="img/default.jpg">
			</div>
		</div>
	</div>
	`;
	return table;	
}
function echoTableBotview(){
	var table =`
	<div class="half">

			<div class="pad">
				<h1>All users</h1>
				<h1>Active Building: <span id="ActiveBuilding">loading...</span></h1>
			</div>
			<hr/>
		<h1 id="liveHeader">Simulation Building</h1>
		<table class="fixed_header" id="table-body-SU">
			<thead>
				<td scope="col" class="status-column"></td>
				<td scope="col" class="name-column">Name</td>
				<td scope="col" class="email-column">Email</td>
				<td scope="col" class="type-column">Type</td>
				<td scope="col" class="device-column">Device ID</td>				
				<td class="special-long-td-text simulation-column">Add to<br/> simulation</td>
			</thead>
			<tbody id="table-body-Sim"></tbody>
		</table>
	</div>
	<div class="half">
			<div class="pad">
				<button onclick="alarm(true,'simulation')">Trigger Alarm</button>
				<button onclick="alarm(false,'simulation')">Reset Alarm</button>
				<button onclick="">Add Bot</button>
				<button onclick="displayOverlayWindow(fireWindow)">Add Fire</button>
			</div>
			<div class="pad">
				<h1>Simulation</h1>
				<h1>Active Building: <span id="ActiveBuildingSim">loading...</span></h1>
				<h3>Change Building <select id="buildingDropDownSim"></select></h3>
			</div>
			<hr/>
		<table id="table-simulation">
			<thead>
				<td scope="col" class="bot-simul">Bot ID</td>
				<td scope="col" class="location-simul">Location(Floor,x,z)</td>
				<td scope="col" class="device-simul">Device ID</td>
				<td scope="col" class="type-simul">Type</td>
				<td scope="col" class="status-simul">Status</td>
			</thead>
			<tbody></tbody>
		</table>
	</div>
	`
	return table;
}