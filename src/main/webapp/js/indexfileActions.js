
/*Defining button actions */
$(document).ready(function(){
	
	//scan from navigation bar button action
    $("#scanfromnavbar").click(function(){
        openScanDirectoryPopup();
    });
	
	//save button from scan directory pop up
	$("#scan-button").click(function(){
        var dir = getScanDirectory();
		if (dir !== undefined && dir !== ''){
			closeScanDirectoryPopup();
			postforscanAndDisplayStatus(dir);
		}			
    });	
	
	
});


  function openScanDirectoryPopup() {
	$("#myModal").modal("show");	
  }
  
  function closeScanDirectoryPopup() {
	$("#myModal").modal("hide");	
  }
  
  function openScanDirectoryErrorPopup(errorMsg) {
	  $("#errorMsgModalBody").text(errorMsg);	
	  $("#errorMsgmodel").modal("show");	
  }
  
  function getScanDirectory() {
	return $("#dirUserInputId").val();
  }
  
  
  
/*Action by the scan button to post requests */
  function postforscanAndDisplayStatus (folder) {
  
	var result = postAjaxCall("http://localhost:8080/moviemon/movies", folder, "demo");
  
    //listening to SSE events here ----   
    /* This seems to be not working . need to debug further. Until then I am going with the stone-age way of polling the server 
  
    if(typeof(EventSource) !== "undefined") {
    var source = new EventSource("http://localhost:8080/moviemon/movies/scanstatus");
    source.onmessage = function(event) {
        document.getElementById("result").innerHTML = event.data;
    };
} else {
    document.getElementById("result").innerHTML = "Sorry, your browser does not support server-sent events...";
} */

	pollSeverForStatus();
}

 function pollSeverForStatus() {
 
    $.get('http://localhost:8080/moviemon/movies/scan/status', function(data) {
        
		if (data == 'FAILED') {
			//openScanDirectoryErrorPopup(data);
		} else 
		if (data == 'SUCCESS') { //need to handle success and failure in an appropriate way.
			//need to refresh the movies list here!
		    //displayScanProgressMessage(data);
		} else {
           setTimeout(pollSeverForStatus,5000);
		}
    });
}
 
  function displayScanProgressMessage(message) {
	$("#demo").text(message + "...");
  }
  