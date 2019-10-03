function getCanvasStarted()
{
    console.log("here");
    var buildingToDisplay = $("#buildingDropDownSim").val();
    console.log(buildingToDisplay);
    var canvas= $("canvas#canvas");
    console.log(canvas);
    var ctx=canvas.getContext("2d");
    var img=new Image();
    img.onload=start;
    img.src="http://127.0.0.1:8080/Buildings/"+buildingToDisplay+"/building.jpeg";
    var dimensions = getDimensions(buildingToDisplay);
    start();
}

function start(){
    var w=img.width;
    var h=img.height;
    canvas.width=w;
    canvas.height=h;
    // resize img to fit in the canvas
    // You can alternately request img to fit into any specified width/height
    var sizer=scalePreserveAspectRatio(w,h,canvas.width,canvas.height);
    ctx.drawImage(img,0,0,w,h,0,0,w*sizer,h*sizer);
    //var context = canvas.getContext('2d');
    canvas.addEventListener('mousemove', function(evt) {
        var mousePos = getMousePos(canvas, evt);
        var message = 'Mouse position: ' + parseFloat(mousePos.x).toFixed(2) + ',' + parseFloat(mousePos.y).toFixed(2);
        writeMessage(message);
    }, false);
    canvas.addEventListener('mousedown', function(evt) {
        var mousePos = getMousePos(canvas, evt);
        console.log('Mouse position: ' + parseFloat(mousePos.x).toFixed(2) + ',' + parseFloat(mousePos.y).toFixed(2));
    }, false);
}

function scalePreserveAspectRatio(imgW,imgH,maxW,maxH){
    return(Math.min((maxW/imgW),(maxH/imgH)));
}

function writeMessage(message){
    $("#message").html(message);
}

function getMousePos(canvas, evt) {
    var rect = canvas.getBoundingClientRect();
    var padding =  - 0.5;
    return {
        x: ((evt.clientX - rect.left) / ($("#canvas").width()) * dimensions.x)+padding,
        y:(Math.abs(evt.clientY - rect.bottom) / ($("#canvas").height()) * dimensions.z)+padding
    };
}

function getDimensions(buildingName){
    var maxX = 0;
    var maxZ = 0;
    var building;
    $.ajax({
        async: false,
        type: "GET",
        url: "http://127.0.0.1:8080/Buildings/"+buildingName+"/data.json",
        success: function (response) {
            var building = JSON.parse(response).floors[0].corners;
            for (var i = 0; i < building.length; i++) {
                var x = building[i][0];
                var z = building[i][1];
                if(x>maxX){
                    maxX = x;
                }
                if(z>maxZ){
                    maxZ = z;
                }
            }
            console.log(building);
        }
    });
    return {
        x: maxX,
        z: maxZ
    };
}