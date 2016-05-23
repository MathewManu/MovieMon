function fireRestCallsAndDisplay(leftButtonId) {
	if (leftButtonId === "favMoviesBtn") {
		getFavMovies();
	} else if (leftButtonId === "AllMoviesBtn") {
		getAllMovies();
	} else if ( leftButtonId ==="WatchListMoviesBtn") {
		getWatchListMovies();
	}
}

function showMoviesAsList(movies) {
	//clear existing div 
	//document.getElementById("rightPaneldiv").innerHTML = "";
	 var textToInsert = '<div class="list-group" id="listMovies"> <div class="container"> <h2>Movies..</h2> ';

	$.each(allMovies, function (index, el) {

        var  genreString = ''
            , genreLinks = '';
        var genreList = el.genre.split(",");
        for (i = 0; i < genreList.length; i++) {
            genreString += '<a href="#">' + genreList[i] + '</a>' + ', ';
        }
        genreLinks = genreString.substring(0, genreString.lastIndexOf(",")); //to remove the last extra comma. [ action, drama, ]

        var favIconToBeUsed;
        if (el.favorite === true) {
            favIconToBeUsed = favoriteIcon;
        } else {
            favIconToBeUsed = unFavoriteIcon;
        }
        var wlistIcon;
        if (el.inWatchList === true) {
            wlistIcon = wlistSelected;
        } else {
            wlistIcon = wlistUnselected;
        }
		
		textToInsert += '<a href="#" class="list-group-item "> <img src="moviemon/posters/' + el.poster + '"  width="220" height="315">';
		textToInsert += '<div>';
		textToInsert += '<span class="' + favIconToBeUsed + '"></span> ' + el.title;
		textToInsert += '</div> </a> ';
	})
	textToInsert = textToInsert + '</div> </div>';
	$("#posterdiv").children().hide();
	$('#rightPaneldiv').append(textToInsert);
}