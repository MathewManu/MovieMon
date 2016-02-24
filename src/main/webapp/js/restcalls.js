
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
	openScanDirectoryErrorPopup(result);
    return result;
  },
  beforeSend: setHeader
});
}

function deleteAjaXCall(url, requestparam, idToBeUpdated) {
$.ajax({
    url: url,
    type: 'DELETE',
    success: function(result) {
        // What to do? Nothing for now 
    },
	beforeSend: setHeader
});

}