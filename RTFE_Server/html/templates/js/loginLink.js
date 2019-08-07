function pull_user_data()
{
	//debugger;
	
	$.ajax({
            url: "http://127.0.0.1:8080/database",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: "getUsers"
            }),
            success: function(data){
                if (data.status){
                
                     notify("Everything was fetched!", 'green');
                     console.log(data);
                }else{
                   /*SOMETHING TO BE ADDED*/
                   
                   notify("Nothing was fetched!", 'red');
                }
            }, fail: function(){
				$(`#${id}`).append("");
				console.log("here");
            }
        });

}
