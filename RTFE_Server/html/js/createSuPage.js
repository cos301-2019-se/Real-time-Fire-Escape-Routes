function buildSUPage()
{
	pull_user_data();
	$global_building_info = getInfoAboutCurrentBuilding(); // global variables

	let rowForBar = $("#top-bar");
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
}