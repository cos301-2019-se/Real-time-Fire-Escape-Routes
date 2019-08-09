function load_SU_LifeView()
{
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
}