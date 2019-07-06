$(()=>{
	/*if($("#top-stat").attr("data-stat") ==="superUser")
	{
		$('#main-body-superuser').show();
	}*/
	$('#toggle-but').on("click", ()=>{
		if($("#top-stat").attr("data-stat") ==="superUser")
		{
			$("#img-toggle").attr('src', "icons/greyToggleOn.png");	
			$("#top-stat").attr("data-stat", "adminUser");
			$("#cur-stat").text("Admin"); 
			$("#main-body-superuser").css("display", "none");
			$("#main-body-admin").removeAttr("style");
		}
		
		else
		{
			$("#img-toggle").attr('src', "icons/greyToggleOff.png");
			$("#top-stat").attr("data-stat", "superUser");
			$("#cur-stat").text("Super user"); 
			$("#main-body-superuser").removeAttr("style");
			$("#main-body-admin").css("display", "none");
		}
		
	});
	
});