//refer http://www.smashingmagazine.com/2012/02/beginners-guide-jquery-based-json-api-clients/#the-full-code
//var rootURL = "http://192.168.0.101:8080/moviemon/movies";
var rootURL = "http://localhost:8080/moviemon/movies";
var getAllMovies = function() {
		$.getJSON(rootURL, function(json) {
			if(json.length) {
				$.each(json, function(index, el) {
					var textToInsert = '';
					textToInsert += '<div class="col-lg-3 col-md-4 col-sm-6 col-xs-12" ><img src="moviemon/posters/' + el.title + '.jpg" class="thumbnail" width="300" height="426">';
					textToInsert += '<p>' + el.title + " Rating : " + el.imdbRating + '</p>';
					textToInsert += '<button type="button" class="btn btn-info btn-xs" data-toggle="collapse" data-target="#' +el.title +'">More</button>';
					textToInsert += '<div id="' +el.title + '" class="collapse">';
					textToInsert += el.title +' ' + el.year +' ' + el.imdbRating +' ' +el.genre;
					textToInsert +=  '</div></div>';
					
					$('#poster').append(textToInsert);
			
				});
			}	
			else {
			
			 console.log("empty !!!, should show scan now button ");
			
			}
		});
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

						$('#poster').html('<h2 class="loading"> Found the movie ' + json[0].title +' Rating = ' + json[0].imdbRating +'! </h2>');
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
	getAllMovies();

   $('#searchAll').click(getAllMovies);
   $('#search').click(getMovie);
   $('#searchTitle').keyup(function(event) {
   		if(event.keyCode == 13) {
   			getMovie();
   		}
   });

});
