//refer http://www.smashingmagazine.com/2012/02/beginners-guide-jquery-based-json-api-clients/#the-full-code
var IP = "localhost";
//var IP = "192.168.0.102"	//replace ip with localip If u wanna test from tab/mobile
var rootURL = "http://" + IP + ":8080/moviemon/movies";
var loginURL = "http://" + IP + ":8080/moviemon/authentication/signin";
var favURL = "http://" + IP + ":8080/moviemon/movies/favorites/";
var watchListURL = "http://" + IP + ":8080/moviemon/movies/watchlist/";

var allMovies;

var unFavoriteIcon = "fa fa-heart-o fa-lg favIconEmpty";
var favoriteIcon = "fa fa-heart fa-lg favIcon";



var wlistSelected = "glyphicon glyphicon-bookmark gi-2x bookmark";
var wlistUnselected = "glyphicon glyphicon-bookmark gi-2x bookmark-none";


var imdbStarIcon = "glyphicon glyphicon-star gi-1x gold";
	
var token = "authToken";


var showPosters = function () {
	$("#listMovies").remove();
	$("#posterdiv").children().show();
	
	document.getElementById("all_Movies").innerHTML = "";
	
    var count = 0;
    $.each(allMovies, function (index, el) {

        var textToInsert = ''
            , genreString = ''
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

        count++; //can be removed, just used to limit the number of movies. testing purpose

        textToInsert += '<div class="col-lg-3  col-md-4 col-sm-6 col-xs-12"><div class="thumbnail fade"><img src="moviemon/posters/' + el.poster + '"  width="220" height="315">';

        textToInsert += '<a>' + '<span class="' + wlistIcon + '" onclick="tryWatchList(this , ' + el.id + ')"' + 'title="Add to watchlist" data-wl-id=' + el.id + '></span></a>';

        textToInsert += '<div class="caption">';

        textToInsert += '<a>' + '<span class= "' + favIconToBeUsed + '" onclick="tryFavoriting(this , ' + el.id + ')" data-fav-id=' + el.id + '></a><span class="rating">' + ' ' + el.imdbRating + '<span class="ten">/10</span>' + '</span></span>';

        //textToInsert += '<a href="#">' + '<span class="glyphicon glyphicon-plus gi-2x"></span></a>';

        textToInsert += '<h3>' + el.title + '<span id="year"> (<a href="">' + el.year + '</a>)</span>' + '</h3>';
        textToInsert += '<div class="infoText">' + genreLinks + '<span class="pipe">' + ' | ' + '</span>' + el.runTime + '</div>';

        textToInsert += '</div></div></div>';
        $('#all_Movies').append(textToInsert);
    });


};

var setCookie = function (cname, cvalue) {
    var d = new Date();
    d.setTime(d.getTime() + (1 * 24 * 60 * 60 * 1000));
    var expiry = "expires=" + d.toUTCString();
    document.cookie = cname + "=" + cvalue + ";" + expiry;
};

function getCookie(cname) {
    var name = cname + "=";
    var ca = document.cookie.split(';');
    for (i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length, c.length);
    }
    return "";
};

//set the http Authorization request header with token value
//token value is retrieved from cookie
function setHeader(xhr) {
    var bearertoken = getCookie(token);
    if (bearertoken) {
        var bearerHeader = 'Bearer ' + bearertoken;
        xhr.setRequestHeader('Authorization', bearerHeader);
    }
}

function tryFavoriting(param, id) {
	if (param.className === favoriteIcon) {
    //Delete request to remove favorites
		$.ajax({
			url : favURL + id,
			type : 'delete',
			statusCode : {
				200 : function (response) {
					param.className = unFavoriteIcon;
				}
			},
			beforeSend: setHeader
		});
	} else {
		//sending post request to add favorites
		$.ajax({
			url : favURL + id,
			type : "POST",
			statusCode : {
				200 : function (response) {
					param.className = favoriteIcon;
				}
			},
			beforeSend: setHeader 
		});
	}
};

function tryWatchList(param, id) {
	if (param.className === wlistSelected) {
		//Delete request to remove watchlist
		$.ajax({
			url : watchListURL + id,
			type : 'delete',
			statusCode : {
				200 : function (response) {
					param.className = wlistUnselected;
				}
			},
			beforeSend: setHeader 
		});
	} else { 
		//sending post request to add to watchlist
		$.ajax({
			url : watchListURL + id,
			type : "POST",
			statusCode : {
				200 : function (response) {
					param.className = wlistSelected;
				}
			},
			beforeSend: setHeader 
		});
	}
}

var showScanButton = function () {
	var msg = '';
	msg += '<h1>MovieMon</h1>';
	msg += '<p>You seem to be running for the first time.</p>';
	msg += '<p>In order to update offline database with all the movie details, Please click scan now </p>';
	msg += '<br><br><a class="btn btn-lg btn-default" id="mainScanButton" >Scan now !</a>';
        
	$('#firstTimeMsg').append(msg);
}


var getAllMovies = function () {
	$.ajax({
		type: "GET",
		url: rootURL,
		datatype: 'json',
		//async: false,
		//data:  data,
		success : function(data) {
			if(data.length) {
				allMovies = data;
				showPosters();
			}
			else {
				document.getElementById("all_Movies").innerHTML = "";
				//show no movies msg !
				showScanButton();
			}
		},
		beforeSend: setHeader  
	});
}
var getFavMovies = function () {
	$.ajax({
		type: "GET",
		url: favURL,
		datatype: 'json',
		//async: false,
		//data:  data,
		success : function(data) {
			if(data.length) {
				allMovies = data;
				alert($("#listOrTileId").attr('class'));
				if ($("#listOrTileId").attr('class') === 'glyphicon glyphicon-th') {
					alert("Going to show as list");
					showMoviesAsList(allMovies);
				} else {
					alert("Going to show posters");
					showPosters();
				}
			}
			else {
				document.getElementById("all_Movies").innerHTML = "";
				showScanButton();
			}
		},
		beforeSend: setHeader  
	});
}

var getWatchListMovies = function () {
	$.ajax({
		type: "GET",
		url: watchListURL,
		datatype: 'json',
		//async: false,
		//data:  data,
		success : function(data) {
			if(data.length) {
				allMovies = data;
				document.getElementById("all_Movies").innerHTML = "";
				showPosters();
			}
			else {
				document.getElementById("all_Movies").innerHTML = "";
				showScanButton();
			}
		},
		beforeSend: setHeader  
	});
}
//try to authenticate the user.
//upon success token is returned & it ll be saved in a cookie
function postLoginAjaxCall(username, password) {
	
	var cred = {
			'username': username,
			'password': password
	}
	
	$.ajax({
		type: "POST",
		url: loginURL,
		async: false,
		data : JSON.stringify(cred),
		contentType: 'application/json',		
		
		success : function(data) {
			    setCookie(token, data);
		}
	});
}


