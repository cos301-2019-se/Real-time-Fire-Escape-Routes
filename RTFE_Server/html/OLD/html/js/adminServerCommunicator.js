/*When one types in the info into input field this will be the function that will going to retrieve it*/
function getInfoFromInput(callFunc, email1)
{	
	if(callFunc === "addUser")
	{
		let dataType = "register";
		let name = $("#fullName-addUser").val();
		let email = $("#email-addUser").val();
		let pass = $("#setPassword-addUser").val();
		let userType = $("#type-addUser option:selected").val();
		console.log(dataType);
		console.log(name);
		console.log(email);
		console.log(pass);
		console.log(userType);
		addUser(dataType, name, email, pass, userType);
	}

	else if(callFunc === "addInfo")
	{
		let dataType = "update";
		let updateName, updateEmail, updatePass, updateType;
		let name = $("#fullName-edditUser").val();
		console.log(name+ " the name");
		if(name != undefined && name != "" && name != null && name != " ")
		{
			console.log("I am here");
			updateName = updateInfo(dataType, 'name', email1, name);
			if(updateName === "false")
			{
				notify("Failed to update user name", 'red');
				clearWindow();
				return;
			}
		}

		
		let pass = $("#setPassword-edditUser").val();
		if(pass != undefined || pass === "Do not fill in to keep it same" || pass != " " || pass != null)
		{
			updatePass = updateInfo(dataType, 'pass', email1, name);
			if(updatePass === "false")
			{
				notify("Failed to update user password", 'red');
				clearWindow();
				return;
			}
		}
		let userType = $("#type-edditUser option:selected").val();
		if(userType != undefined)
		{
			updateType = updateInfo(dataType, 'usertype', email1, name);
			if(updateType === "false")
			{
				notify("Failed to update user type", 'red');
				clearWindow();
				return;
			}
		}

		let email = $("#email-edditUser").val();
		if(email != undefined || email != " " || email != null)
		{
			updateEmail = updateInfo(dataType, 'email', email1, name);
			if(updateEmail === "false")
			{
				notify("Failed to update user email", 'red');
				clearWindow();
				return;
			}
		}

		notify("User's information was successfully changed", 'green');

		updateTable();
        clearWindow();

		//addInfo(dataType, name, email, pass, userType);
	}

	else if(callFunc === "addBuilding")
	{
		let name = $("#buildingName").val();
		let img = null;
		let file = null;
		addBuilding(file, name, img);
	}

	else if(callFunc === addFire)
	{
		let x = $("#x-coordinate").val();
		let y = $("#y-coordinate").val();
		let rad = $("#rad-number").val();
		let floor = $("#floor-number").val();
		addFire(x, y, rad, floor);
	}


		clearWindow();
}
///////////////////////////////////////////////////////////////////////////////////////////////////////



/****************************DATABASE COMMUNICATORS**********************************************/
/*Ajax call to server to add users*/
function addUser(dataType, name, email, pass, userType)
{
	//debugger;
	console.log('over here')
	$.ajax({
            url: "http://127.0.0.1:8080/database",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: dataType,
                name: name,
                email: email,
                pass: pass,
                userType: userType
            }),
            success: function(data){
                if (data.status){
                	console.log('Successfully added a new User');
                	updateTable();
                	notify("The user was successfully added!", 'green')
                    
                }else{
                	notify("Failed to add a user", 'red');
                	console.log(data.message);
                   
                   
                }
            }, fail: function(){
				console.log('fail');
            }
        });

}

//////////////////////////////////////////////////////////////////////////////////////////////////////
/*Ajax call to server to delete users*/

function deleteUser(email, name)
{
	$.ajax({
            url: "http://127.0.0.1:8080/database",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: "remove",
                email: email
            }),
            success: function(data){
                if (data.status){
                	console.log('Successfully added a new User');
                	updateTable();
                	clearWindow();
                	notify("The user "+name+" was succesfully removed from the system", 'green');
                    
                }else{
                	notify("Failed to remove the user", 'red');
                	
                   
                   
                }
            }, fail: function(){
				console.log('fail');
            }
        });

}
//////////////////////////////////////////////////////////////////////////////////////////////////////

/*Add new building*/
function addBuilding(file, BuildingName, image)
{
	$.ajax({
            url: "http://127.0.0.1:8080/dababase",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                file: file,
                type: "upload",
                BuildingName: BuildingName,
                image: image
            }),
            success: function(data){
                if (data.status){
                	notify(data.message, "blue");
                    
                }else{
                	
                	notify("Failed to add new building", "red");
                   
                   
                }
            }, fail: function(){
				notify("Failed to add new building", "red");
            }
        });
}

////////////////////////////////////////////////////////////////////////////////////////////////////

/*Ajax to fetch user info from db*/
function fetchAllUserData(dataType, id)
{
	//debugger;
	
	$.ajax({
            url: "http://127.0.0.1:8080/database",
            type: "POST",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            data: JSON.stringify({
                type: dataType
            }),
            success: function(data){
                if (data.status){
                	
                    
                }else{
                   
                   $(`#${id}`).append("");
                }
            }, fail: function(){
				$(`#${id}`).append("");
				console.log("here");
            }
        });

}