/*Adding fire*/
function addFire(x, y, rad, floor)
{
	$.ajax({
            url: "http://127.0.0.1:8080/building",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: "fire",
				position: [x,y],
				radius: rad,
				floor: floor
            }),
            success: function(data){
                if (data.status){
                	notify(data.message, "blue");
                		$.ajax({
				            url: "http://127.0.0.1:8080/building",
				            type: "POST",
				            contentType: "application/json; charset=utf-8",
				            dataType: "json",
				            data: JSON.stringify({
				                type: "assignPeople"
				            }),
				            success: function(data){
				            	$("body").delay(1500);
				                notify("Affected people were notified","green")
				            }
				        });
                    
                }else{
                	
                	notify("Failed to add fire simulation", "red");
                   
                   
                }
            }, fail: function(){
				console.log('fail to add fire');
            }
        });
}