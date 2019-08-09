//version 1 for the menu on top
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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//version 2 for the menu on top
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

////////////////////////////////////////////////////////////////////////////////////////////

/*Echoes card for building with building name and drop down */
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
///////////////////////////////////////////////////////////////////////////////////////////

/*Removing overlayed building*/
function clearWindow()
{
	$("#overlay-window").empty();
	$("#overlay-window").attr("style", "display: none;");
}
/////////////////////////////////////////////////////////////////////////////////////////



/*this will put the grey card with semi-transparent background. It will be overlay current screen. Used for creating, adding and other manipulations*/
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
/////////////////////////////////////////////////////////////////////////////

/*function will be load when div "main" will be loaded*/
function buildPage()
{
	buildSUPage();
}