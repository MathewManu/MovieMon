var isListButtonSelected = false; //this global variable is used to track whether the list button is selected or not.

/*Defining button actions */
$(document).ready(function(){
	
	var deleteUrl = "http://localhost:8080/moviemon/movies";
	//this g.v  is used to track which button is selected from the left side panel, mainly for firing rest calls during listing.
	var trackLeftClickedButtons = "";
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
	
	//DeleteAll button
	$("#deleteAll").click(function(){
		openConfimrationMsgPopup("Deleting scan data will remove all your favorites and wishlist movies. Do you want to proceed?");        		
    });		
	
	//mainScanButton button
	$("#mainScanButton").click(function(){
        openScanDirectoryPopup();	
    });	
	
	//ok button from the confirm pop up 
	$("#confirm-button").click(function(){
        	deleteAjaXCall(deleteUrl, null, null);		
    });
	
	//cancel button in the confirm pop up 
	$("#cancel-button").click(function(){
        	$("#confirmationMsgmodel").modal("hide");		
    });
	
	$("#login").click(function(){
		openLoginPopup();
	});
	
	//Login box button
	$("#login-button").click(function() {
		
		var username = $("#username").val();
		var password = $("#password").val();
		
		//TODO: (manu) basic form validation
	
		if(username != '' && password != '') {
			closeLoginPopup();
			var result = postLoginAjaxCall(username, password);
		}
		else {
			//error
		}
			
	});
    $("#guest-login-button").click(function() {
    
        closeLoginPopup();
        postLoginAjaxCall("guest","guest");
    });

	$("#favMoviesBtn").click(function() {
		setTrackLeftClickedButtons("favMoviesBtn");
		getFavMovies();
		
	});
	
	$("#AllMoviesBtn").click(function() {
		setTrackLeftClickedButtons("AllMoviesBtn");
		getAllMovies();
	});
	
	$("#WatchListMoviesBtn").click(function() {
		setTrackLeftClickedButtons("WatchListMoviesBtn");
		getWatchListMovies();
	});
	
	$("#listOrTileId").click(function(){
		var listIcon = "glyphicon glyphicon-list";
		var tileIcon = "glyphicon glyphicon-th";
			
		//the fireRestCallsAndDisplay will select the appropriate rest call to fire based on the selected frame, and display in list/tile format.
		fireRestCallsAndDisplay(getTrackLeftClickedButtons());
		if ($("#listOrTileId").attr('class') === listIcon) {
			$("#listOrTileId").attr('class', tileIcon);
		} else {
			$("#listOrTileId").attr('class', listIcon);
		}
		
    });
	
	 //removes the focus after the button is selected in the sidebar.
    $(".btn").mouseup(function(){
    $(this).blur();
    });
	
});


  function openLoginPopup() {
	  $("#loginModal").modal("show");
  }
  
  function closeLoginPopup() {
	    $("#loginModal").modal("hide");
  }

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
  
 //open a confirmation popup.
  function openConfimrationMsgPopup(confirmationMsg) {
	  $("#confirmationMsgModalBody").text(confirmationMsg);	
	  $("#confirmationMsgmodel").modal("show");	
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
  
  function setTrackLeftClickedButtons(buttonId) {
	  trackLeftClickedButtons = buttonId;
  }
  
  function getTrackLeftClickedButtons() {
	  return trackLeftClickedButtons ;
  }
  
  function setListButtonValue(val) {
	  isListButtonSelected = val;
  }
  
  function getListButtonValue() {
	  return isListButtonSelected;
  }
  