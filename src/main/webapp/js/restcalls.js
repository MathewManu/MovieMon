function postCall(url, requestParam) {
        $.post(
        url,
        requestParam,
        function(result){
          return result;
        }
     );
    }
	
	
	
function postAjaxCall(url, requestParam, idToBeUpdated) {
	//Used ajax here as there was no way to give a contentType for $.POST method.
   $.ajax({
  url:url,
  type:"POST",
  data:requestParam,
  contentType: "text/plain",
  success: function(result){
	  document.getElementById(idToBeUpdated).innerHTML = result;
    return result;
  }
});
    }