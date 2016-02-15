//refer http://www.smashingmagazine.com/2012/02/beginners-guide-jquery-based-json-api-clients/#the-full-code
//var rootURL = "http://192.168.0.101:8080/moviemon/movies";
var rootURL = "http://localhost:8080/moviemon/movies";
var allMovies;

var showPosters = function() {

	$.each(allMovies, function(index, el) {
		var textToInsert = '';
		
		textToInsert += '<div class="col-lg-3 col-md-4 col-sm-6 col-xs-12"><div class="thumbnail fade"><img src="moviemon/posters/' + el.poster + '"  width="300" height="426">';
		textToInsert += '<a href="#">' + '<span class="glyphicon glyphicon-bookmark gi-3x bookmark" title="Add to watchlist" data-wl-id=' + el.id +'></span></a>';
		
		textToInsert += '<div class="caption">';
		textToInsert += '<span class="glyphicon glyphicon-star gi-1x gold" data-fav-id=' + el.id +'><span class="rating">' +' ' +el.imdbRating  +'<span class="ten">/10</span>' +'</span></span>';
		
		//textToInsert += '<a href="#">' + '<span class="glyphicon glyphicon-plus gi-2x"></span></a>';
		//textToInsert += '<a href="#" >' + '<span class="glyphicon glyphicon-heart gi-3x"></span></a>';
		
		textToInsert += '<h3>' + el.title  + '<span id="year"> (<a href="">' + el.year +'</a>)</span>' +'</h3>';
		textToInsert += '<div class="infoText">' + el.genre +'<span class="pipe">' + ' | ' + '</span>' +el.runTime +'</div>';
	//	textToInsert += '<h5><small>Director : ' + el.director + '</small></h5>';
	//	textToInsert += '<h6><small>' + el.plot + '</small></h6>';
		textToInsert +=  '</div></div></div>';
		$('#poster').append(textToInsert);

	});


}
var showScanButton = function () {
	var msg = '';
	msg += '<h1>MovieMon</h1>';
	msg += '<p>You seem to be running for the first time.</p>';
	msg += '<p>In order to update offline database with all the movie details, Please click scan now </p>';
	msg += '<br><br><a class="btn btn-lg btn-default" id="mainScanButton" >Scan now !</a>';
        
	$('#firstTimeMsg').append(msg);
}
/*var getAllMovies = function() {
		$.getJSON(rootURL, function(json) {
			if(json.length) {
				allMovies = json;
				showPosters();
			}	
			else {
			 showScanButton();
			}
		});
	}*/

var getAllMovies = function () {
	$.ajax({
		type: "GET",
		url: rootURL,
		datatype: 'json',
		//async: false,
		headers: {
		    "Authorization": "Basic " + btoa("gues" + ":" + "guest")
		  },
		//data:  data,
		success : function(data) {
			if(data.length) {
				allMovies = data;
				showPosters();
			}
			else {
				showScanButton();
			}
		},
		beforeSend: setHeader  
	});
}

function setHeader(xhr) {
  xhr.setRequestHeader('Authorization', 'Basic Z3Vlc3Q6Z3Vlc3Q=');
}


var getMovie = function() {
		var movieName = $('#searchTitle').val();

		if(movieName == '') {
			$('#poster').html("<h2 class='loading'>Haa ... Please enter something. </h2>");
		}
		else {
			$('#poster').html("<h2 class='loading'> Details on the way </h2>");

			$.getJSON(rootURL +"/" +movieName, function(json) {
					if(json != "[]") {

						$('#poster').html('<h2 class="loading"> Found the movie ' + json[0].title +' Rating = ' + json

[0].imdbRating +'! </h2>');
						console.log(json);
					}
					else {
						cosole.log(json);
					}

			});
		}
		return false;
	}	
	
	
$(document).ready(function(){
/*
   $('#term').focus(function(){
      var full = $("#poster").has("img").length ? true : false;
      if(full == false){
         $('#poster').empty();
      }
   });*/
   //load the movies when page loads ...
   //checkFirstRun();
	getAllMovies();

   $('#searchAll').click(getAllMovies);
   $('#search').click(getMovie);
   $('#searchTitle').keyup(function(event) {
   		if(event.keyCode == 13) {
   			getMovie();
   		}
   });

});
