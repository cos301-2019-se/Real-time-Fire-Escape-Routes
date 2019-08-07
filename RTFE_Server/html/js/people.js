var URL = "http://127.0.0.1:8080/"

function addBot(botID,location){
    str += `<tr>
        <td scope ="row" data-label="Name">Bot - ${botID}</td>
        <td data-label="Email"> - </td>
        <td data-label="Device_ID"> - </td>
        <td data-label="Type"> BOT</td>
        <td data-label="Status"><input  type="checkbox" id="botStatus-${botID}" onchange="checkBotStatus(this)" checked/></td>
    </tr>`;


}


function checkBotStatus(input) {
  var x = e.checked
  document.getElementById("demo").innerHTML = "You selected: " + x;
}
function docall(botID,location,HTMLelement){
    $("#table-body-SU").append(str);
    $.ajax({
        url:  URL+"building",
        type: "POST",
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        data: JSON.stringify({
            type: "personUpdate",
            position: location,
            id : botID
        }),
        success:function(res){
            if(res.status){
                HTMLelement.checked = true;
            }
            else{
                HTMLelement.checked = false;
            }
        }
    }); 
}