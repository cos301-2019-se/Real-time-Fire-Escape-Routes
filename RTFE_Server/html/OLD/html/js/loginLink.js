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
                    
                   // get_user_type(createObject(data));
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

//function that will check user's type
function get_user_type(user_data)
{

}

function dashboard(elem){
        elem.html('');
        elem.append($(`<div class="grid-container container-fluid">
                <header class="header" id="top-bar">
                    
                </header>
                <main class="main" onload="buildSUPage()">
                        
                </main>
            </div>`).fadeIn()
        )
    }