
/*Defining button actions */
$(document).ready(function(){
	
	//scan from navigation bar button action
    $("#scanfromnavbar").click(function(){
        openScanDirectoryPopup();
    });
	
	//save button from scan directory pop up
	$("#save-button").click(function(){
        var dir = getScanDirectory();
		if (dir !== undefined && dir !== ''){
			closeScanDirectoryPopup();
		postforscanAndDisplayStatus(dir);
		}			
    });	
	
	
});


  function openScanDirectoryPopup(inputId) {
	$("#myModal").modal();	
  }
  
  function closeScanDirectoryPopup(inputId) {
	$("#myModal").modal("hide");	
  }
  
  function getScanDirectory() {
	return $("#id_name").val();
  }
  
  
/*Action by the scan button to post requests */
  function postforscanAndDisplayStatus (folder) {
  
	postAjaxCall("http://localhost:8080/moviemon/movies", folder, "demo");
  
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
        
		if (data == 'SUCCESS' || data == 'FAILED') { //need to handle success and failure in an appropriate way.
		    displayScanProgressMessage(data);
			return;
		}
        setTimeout(pollSeverForStatus,5000);
    });
}
 
  function displayScanProgressMessage(message) {
	$("#demo").text(message + "...");
  }
  