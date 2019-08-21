function notify(msg, color){
    $('body').append($('<div></div>')
        .css({
            'position' : 'fixed',
            'margin' : '0 auto',
            'top' : '-50px',
            'left' : '0',
            'right' : '0',
            'text-align' : 'center',
        })
        .append($('<span></span>')
            .css({
                'font-family' : 'Ubuntu',
                'padding' : '10px 20px',
                'background-color' : color,
                'color' : 'white',
                'border-radius' : '7px',
                'box-shadow' : '0px 2px 3px 1px rgba(0,0,0,0.27)'
            })
            .text(msg)
        )
        .animate({
            top: "+=80px"
        }).delay(2000).animate({
            top: "-=80px"
        }, function(){
            $(this).remove();
        })
    )
}

function statusIdentifier(status)
{
    if(status === "Offline")
    {
        return `Offline <span class="online-off-indicator"><img src="icons/offline.png"/></span>`;
    }
    if(status === "Online")
    {
        return `Online <span class="online-off-indicator"><img src="icons/online.png"/></span>`;
    }

}


function populateTable(data,withEdit,addToSim)
{
    let str = "";
    data.forEach((element, index)=>{
        str += `<tr>
            <td scope ="row" data-label="Name">${element.name}</td>
            <td data-label="Email" class="email-column">${element.email}</td>`
            if(element.deviceID!=undefined){
                str += `<td data-label="Device_ID">${element.deviceID}</td>`
            }
            else{
                str += `<td data-label="Device_ID"></td>`
            }
        str+= `<td data-label="Type" class="type-column">${element.userType}</td>
            <td data-label="Status" class="hundred-column" id="${element.deviceID}">${fetchStatus(element.deviceID,element.deviceID)}</td>`
        
            if(withEdit){
                str +=`<td data-label="Edit" class="hundred-column align-center">
                        <button id="${element.email}-edit" onclick="displayOverlayWindow(editUser,'${element.email}', '${element.name}', '${element.userType}', '${element.deviceID}')" class="img-edit"><img src="icons/grey_pensil.png"></button><button class="img-edit">
                        <img class="img-edit" onclick="displayOverlayWindow(removeUser,'${element.email}', '${element.name}', '${element.userType}', '${element.deviceID}')" id="${element.email}-remove" src="icons/grey_duspan.png"></button>
                    </td>`
            }
            if(addToSim){
                str+=` <td class="content-aligned-center simulation-column"><button class="add-user-to-sim-but" data-device="${element.deviceID}" onclick="addUserToSim(this)">Add</button></td>`;
            }
        str+=`</tr>`;

    });
    return str; 
}
function search(searchBox,tableElement) {
  // console.log(searchBox,tableElement);
  var input, filter, table, tr, td, i, txtValue;
  input = searchBox
  filter = input.value.toUpperCase();
  // table = $("#"+tableElement);
  table = document.getElementById(tableElement);
  tr = table.getElementsByTagName("tr");
  for (i = 0; i < tr.length; i++) {
    // td = tr[i].getElementsByTagName("td")[0];
    var show= false;
    for (var col = 0; col < tr[i].getElementsByTagName("td").length; col++) {
      var column = tr[i].getElementsByTagName("td")[col];
      if(column){
        txtValue = column.textContent || column.innerText;
        if (txtValue.toUpperCase().indexOf(filter) < 0) {

        } else {
          show = true;
        }
      }
    }
    if (show) {
      tr[i].style.display = "";
    } else {
      tr[i].style.display = "none";
    }
  }
}

function displayOverlayWindow(contentFunc, user, name, type, device)
{
    //debugger;
    if(user != null)
    {
        $("#overlay-window").removeAttr("style");

        $("#overlay-window").append(`<div id="contentCard"  class="rtferCard">
                ${contentFunc(user, name, type, device)}
            </div>`);
    }
    else
    {
        $("#overlay-window").removeAttr("style");
-
        $("#overlay-window").append(`<div id="contentCard"  class="rtferCard">
                ${contentFunc()}
            </div>`);
    }
}


function addUserToSim(elem){
    var pos = [2,2];
    var ressponse  = addBot($.now(),pos,$(elem).attr("data-device"));
    $("#table-simulation").append(ressponse);
}
function addBotToSim(){

}

function addBot(botID,location,deviceID){
    var input ="<input type='number' min='0' max='100' name='floor' value='0'>"
        +"<input type='number' min='0' max='100' name='x' value='1'>"
        +"<input type='number' min='0' max='100' name='y' value='1'>"
    var str ="";
    if(deviceID == null){
        str +=  `<tr>`
        str +=      `<td scope ="row">botID - <span data-label="Name">${botID}</span></td>`
        str +=      `<td data-label="Location"> `+input+` </td>`;
        str +=      `<td data-label="Device_ID"> - </td>`
        str+=       `<td data-label="Type"> BOT</td>`
        str+=       `<td data-label="Status"><input  type="checkbox" id="botStatus-${botID}" onchange="checkBotStatus(this)"/></td>
                </tr>`;
    }
    else{
        str +=  `<tr>`
        str +=      `<td scope ="row">botID - <span data-label="Name">${botID}</span></td>`
        str +=      `<td data-label="Location"> `+input+` </td>`;
        str +=      `<td data-label="Device_ID"> ${deviceID} </td>`
        str+=       `<td data-label="Type"> Person</td>`
        str+=       `<td data-label="Status"><input  type="checkbox" id="botStatus-${botID}" onchange="checkBotStatus(this)"/></td>
                </tr>`;   
    }
    return str;
}

function checkBotStatus(input) {
  var parent = $(input).parent().parent();

  var location = parent.children('[data-label=Location]');
  var floor = location.children('[name=floor]').val();
  var x = parseInt(location.children('[name=x]').val());
  var y = parseInt(location.children('[name=y]').val());
  var device_id = parent.children('[data-label=Device_ID]').html();
  var bot_id = parseInt(parent.find('[data-label=Name]').html());
  var mode = 'simulation';

  if ($(input).is(':checked')){
    docall(bot_id,[x, y], floor, mode,$(input));
  }else{
    remove(bot_id, mode, parent);
  }
  if(device_id!="undefined" || device_id!="-"){

    bindUser(bot_id,device_id,mode)
  }
}


function handleFileSelect(id)
  {               
    if (!window.File || !window.FileReader || !window.FileList || !window.Blob) {
      alert('The File APIs are not fully supported in this browser.');
      return;
    }   

    input = document.getElementById(id);
    if (!input) {
      alert("Could not find the file.");
    }
    else if (!input.files) {
      alert("This browser doesn't seem to support the `files` property of file inputs.");
    }
    else if (!input.files[0]) {
      alert("Please select a file before clicking 'Save'");               
    }
    else {
      file = input.files[0];
      fr = new FileReader();
      //fr.onload = receivedText;
     // fr.readAsDataURL(file);
    }
  }



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


function fireWindow()
{
    let maxX = 100;
    let maxY= 100;
    let maxFloor = 100;
    $()

    
    console.log(maxFloor);
    let maxRad = 360;
    // question how to get largest x, y, radius
    return `<div> 
                <h1 id='fire-head'>Simulate fire in the building</h1>
            </div>
            <div class='card-content'>
                <div class='form-group'>
                    <label for='x-coordinate'>X</label>
                    <input type='number' min='0' max='${maxX}' class='form-control' id='x-coordinate' value='0'>
                </div>
                <div class='form-group'>
                    <label for='y-coordinate'>Y</label>
                    <input type='number' min='0' max='${maxY}' class='form-control' id='y-coordinate' value='0'>
                </div>
                <div class='form-group'>
                    <label for='rad-number'>Radius</label>
                    <input type='number' min='0' max='${maxRad}' class='form-control' id='rad-number' value='0'>
                </div>
                <div class='form-group'>
                    <div class='form-group'>
                        <label for='floor-number'>Floors</label>
                        <input type='number' min='0' max='${maxFloor}' class='form-control' id='floor-number' value='0'>
                    </div>
                </div>
            </div>
            <div class='form-group row button-line'>
                    <div class='col-sm-12' style='text-align: right;'>
                      <button type= 'submit' class='btn btn-info' onclick='getInfoFromInput(addFire, null)'>Add fire</button>
                      <button onclick='clearWindow()' id='cancel-delete ' class='btn btn-danger'>Cancel</button>
                    </div>
                  </div>
        `;
}



function getInfoFromInput(callFunc, email1)
{   
    anotherFetch("getUsersInBuilding","table-body-A",true,false);

    if(callFunc === "addUser")
    {
        let dataType = "register";
        let name = $("#fullName-addUser").val();
        let email = $("#email-addUser").val();
        let pass = $("#setPassword-addUser").val();
        let confirmPass = $("#confirmPassword-addUser").val();
        let userType = $("#type-addUser").val();
        $("#fullName-addUser").attr("style", "");
        $("#email-addUser").attr("style", "");
        $("#setPassword-addUser").attr("style", "");
        $("#confirmPassword-addUser").attr("style", "");
        let valid = true;
        console.log(dataType);
        console.log(name);
        console.log(email);
        console.log(pass);
        console.log(userType);
        if(!validateEmail(email))
        {
            notify("Invalid email", 'red');
            $("#email-addUser").attr("style", "border: 1px solid red; box-shadow: 0px 0px 4px red;");
            valid = false;
        }
        if(pass == "") {
            notify("Passwords can't be empty", 'red');
            $("#setPassword-addUser").attr("style", "border: 1px solid red; box-shadow: 0px 0px 4px red;");
            valid = false;
        }
        if(name == "") {
            notify("Name can't be empty", 'red');
            $("#fullName-addUser").attr("style", "border: 1px solid red; box-shadow: 0px 0px 4px red;");
            valid = false;
        }
        if(email == "") {
            notify("Email can't be empty", 'red');
            $("#email-addUser").attr("style", "border: 1px solid red; box-shadow: 0px 0px 4px red;");
            valid = false;
        }
        if(pass != confirmPass){
            notify("Passwords don't match", 'red');
            $("#confirmPassword-addUser").attr("style", "border: 1px solid red; box-shadow: 0px 0px 4px red;");
            $("#setPassword-addUser").attr("style", "border: 1px solid red; box-shadow: 0px 0px 4px red;");
            valid = false;
        }
        if(valid == true) {
            addUser(dataType, name, email, pass, userType);
            clearWindow();
        }

    }


/////////////////////////////////////////////////////////////////////////
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

        // updateTable();
        // $("table-body-A").html(populateTable("table-body-A"))
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

        // clearWindow();
}


function selectType(type)
{
    if(type === "Agent")
        {return `
                <option selected>Agent</option>
                <option value="1">Admin</option>
                `} 
        else{ return `
                    <option>Agent</option>
                    <option value="1" selected>Admin</option>
                    `}
}


function removeUser(user, name, type, device)
{
        return `<div class="row">
                <div style="text-align: center; margin-bottom: 3%; margin-top: 3%;" class="col-sm-12">
                    <h1>Do you want to remove ${name} from your system?</h1>
                </div>
            </div>
            <div>
                  <div class="form-group">
                    <h3>Full name:</h3>
                    <p>${name}</p>
                  </div>
                  <div class="form-group">
                    <h3>Email:</h3>
                    <p>${user}</p>
                  </div>
                  <div class="form-group">
                    <h3>Device_ID:</h3>
                    <p>${device}</p>
                  </div>
                  <div class="form-group">
                    <h3>Type:</h3>
                    <p>${type}</p>
                  </div>
                  <div class="form-group row button-line">
                    <div class="col-sm-12" style="text-align: right;">
                      <button type="submit" class="btn btn-info" onclick="deleteUser('${user}', '${name}')">Delete user</button>
                      <button onclick="clearWindow()" id="cancel-delete" class="btn btn-danger">Cancel</button>
                    </div>
                  </div>
            </div>
            `;
}

function editUser(user, name, type, device)
{
    console.log(name);
    return `<div class="row">
        <div style="text-align: center;" class="col-sm-12">
            <h1>Eddit user ${name}</h1>
        </div>
    </div>
    <div>
        <div>
          <div class="form-group">
            <label for="fullName-edditUser">Change name</label>
            <input type="text" class="form-control" id="fullName-edditUser" value="${name}">
          </div>
          <div class="form-group">
            <label for="email-edditUser">Change email</label>
            <input type="email" class="form-control" id="email-edditUser" value="${user}">
          </div>
          <div class="form-group">
            <label for="setPassword-edditUser">Change password</label>
            <input type="password" class="form-control" id="setPassword-edditUser" placeholder="Do not fill in to keep it same">
          </div>
          <div class="form-group">
            <label for="type-edditUser">Change type</label>
            <select class="custom-select mr-sm-2" id="type-edditUser">
                ${selectType(type)}
            </select>
          </div>
          <div class="form-group row button-line">
            <div class="col-sm-12" style="text-align: right;">
              <button onclick="getInfoFromInput('addInfo', '${user}')" id="submit-new-user" class="btn btn-info">Change info</button>
              <button onclick="clearWindow()" id="cancel-new-user" class="btn btn-danger">Cancel</button>
            </div>
          </div>
        </div>
    </div>
    `;
}


function clearWindow()
{
        anotherFetch("getUsers","table-body-A",true,false);

    $("#overlay-window").empty();
    $("#overlay-window").attr("style", "display: none;");
}



function windowForNewBuilding()
{
    return `<div class="row">
        <div style="text-align: center;" class="col-sm-12">
            <h1>Add new building</h1>
        </div>
    </div>
    <div>
        <div>
          <div class="form-group">
            <label for="buildingName">Building name</label>
            <input type="text" class="form-control" id="buildingName" placeholder="Building1">
          </div>
          <div class="form-group">
            <label for="buildingLocation">Building location</label>
            <input type="text" class="form-control" id="buildingLocation" placeholder="Location...">
          </div>
          <div class="form-group">
            <label for="numOfFloors">Number of Floors</label>
            <input type="number" class="form-control" id="numOfFloors" placeholder="1" min="1">
          </div>
          <div class="form-group">
            <label for="fileupload">Upload plan</label>
            <div class="row" padding: 2%;>
             <div class="col-sm-5 custom-file">
                <label style="width: 250px; margin-left: 3.3%;" class="custom-file-label" for="uploadImg"><img style="width: 25px; background-color: transparent;" src="icons/greyFolder.png"/>Upload img</label>
                <input type="file" class="custom-file-input" id="uploadImg">
             </div>
             <div class="col-sm-5 custom-file">
                <label style="width: 250px;" class="custom-file-label" for="uploadJson"><img style="width: 25px;" src="icons/jsonFile.png"/>Upload building</label>
                <input type="file" class="custom-file-input" id="uploadJson">
             </div>
            </div>
           
          </div>
          <div class="form-group row button-line" style="margin-top: 2%;">
            <div class="col-sm-12" style="text-align: right;">
              <button type="submit" onclick="getInfoFromInput('addBuilding', null)"  id="submit-new-building" class="btn btn-info">Save</button>
              <button id="cancel-new-building" class="btn btn-danger"  onclick="clearWindow()">Cancel</button>
            </div>
          </div>
        </div>
    </div>
    `;
}

function windowForNewUser()
{
    return `<div class="row">
        <div style="text-align: center;" class="col-sm-12">
            <h1 style = "font-family: 'Fjalla One'" >Add new user</h1>
        </div>
    </div>
    
        
        <div id="addUser">
        
          <div class="form-group" id = "addUserLabels">
            <label for="fullName-addUser">Full Name</label>
            <label for="email-addUser">Email</label>
            <label for="setPassword-addUser">Password</label>
            <label for="confirmPassword-addUser">Confirm Password</label>
             <label for="type-addUser">User Type</label>
            
          </div>
          <div class="form-group" id = "addUserInputs">
            <input type="text" class="form-control" id="fullName-addUser" placeholder="Full Name" required>
            <input type="email" class="form-control" id="email-addUser" placeholder="Email" required>
            <input type="password" class="form-control" id="setPassword-addUser" placeholder="password" required>
            <input type="password" class="form-control" id="confirmPassword-addUser" placeholder="password" required>
            <div id = #select>
             <select class="custom-select mr-sm-2" id="type-addUser">
                <option selected>Agent</option>
                <option value="admin">Admin</option>
            </select>
            </div>
          </div>
          
          <div class="form-group row button-line">
            <div class="col-sm-12" style="text-align: right;">
              <button onclick="getInfoFromInput('addUser', null)" id="addButton" class="btn btn-info">Add user</button>
              <button onclick="clearWindow()" id="addButton" class="btn btn-danger">Cancel</button>
            </div>
          </div>
        </div>
    
    `;
}

//function to highlight search field when searchbutton was pressed
function highlightSearchField()
{
    $(".search-button").on('click', ()=>{
        console.log("I was clicked");
        $('#search-input').select();
        
    });
}

function validateEmail(email) {
    var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
    return re.test(String(email).toLowerCase());
}

