/*echoes table with users that is on your left*/
function echoContentTable_SuperUser()
{
	let info = anotherFetchOfUserData("getUsers", '#table-body-SU')
	
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*Echoes simulation window containers, that are on your right*/
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/* two following functions are for buttons to add new bot or fire on your right*/
function echoBotCreator()
{
	return `<div id="add-bot-window" class="card1">
  					<button class="btn" id="btn-add-fire" style="margin: 0; padding: 0; display: inline; width: 100%;" ><span style="margin-bottom: 2%;">Add bot</span>
  					<span class="arrow-span" style="margin-bottom: 0.5%;"><img class="icons_small" src="icons/baseline_add_black_48dp.png"></span></button>
  				</div>`;
}

/*******************************/

function echoFireEditor()
{
	return `<div id="add-fire-window" class="card1" onclick="displayOverlayWindow(${fireWindow})">
  					<button  class="btn" id="btn-add-fire" style="margin: 0; padding: 0; display: inline; width: 100%;">
  						<span style="margin-bottom: 2%;">Add fire</span>
  						<span class="arrow-span" style="margin-bottom: 0.5%;"><img class="icons_small" src="icons/baseline_add_black_48dp.png"></span></button>
  				</div>`;
}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

/*displayed when fire editor is opened*/
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
//////////////////////////////////////////////////////////////////////////////////////////////////////////////

