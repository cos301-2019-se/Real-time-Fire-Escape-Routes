//version 1
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

//version 2
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