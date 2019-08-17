/*Ajax gets info about buildings*/
function getBuildingInfo(name)
{
	let objData;
	$.ajax({
            url: "http://127.0.0.1:8080/database",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type:"currentBuilding"
            }),
            success: function(data){
                if (data.status){
                	
                	objData = data;
                
                	if(name === "name")
					{
						$("#building-name").html(objData.name);
					}
					else if(name === "img")
					{
						$("#building-name").html(objData.name);
						$("#SU-simulation").append(`<img src="../Buildings/${objData.name}/building.jpeg" style="width:100%;"/>`);
					}
                	
                    
                }else{
                   $("#building-name").html("No buildings to display");
                   return;
                }
            }, fail: function(){
				$("#building-name").text("Failed to load building");
				return;
            }
        });
}
////////////////////////////////////////////////////////////////////////////////////////

/*Get info about currently active building*/
function getInfoAboutCurrentBuilding()
{
	$.ajax({
            url: "http://127.0.0.1:8080/buildingGeneration",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: "buildingData"
            }),
            success: function(data){
                if (data.status){
                	//console.log(data);
                	return data;
                    
                }else{
                	
                	return "No buildings found"
                   
                   
                }
            }, fail: function(){
				console.log('fail to get buildings');
				return null;
            }
        });
}