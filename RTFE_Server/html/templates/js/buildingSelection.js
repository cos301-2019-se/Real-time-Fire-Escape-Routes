$(()=>{
fetchCurrentBuilding();

});













function fetchCurrentBuilding()
{
	var button = '<button style="margin: 0; padding: 0; display: inline;" class="btn"><img id="img-drop" src="icons/white_arrow_down.png"></button>'
	$.ajax({
        url: "http://127.0.0.1:8080/database",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            type: "currentBuilding"
        }),
        success:function(res){
        	if(res.name!=null){
        		$("h1").html(res.name+button);
        	}
        	else{
        		$("h1").html("None"+button);
        	}
        }
    }); 
}

