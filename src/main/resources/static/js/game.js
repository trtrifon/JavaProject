/**
 * 
 */

var game_id = 0;
var game_price = 0;

$(document).ready(function(){

    $( function () {
        if ( sessionStorage.getItem("showSnackbar") != null && sessionStorage.getItem("showSnackbar") == 'true') {
            showSnackbar2(sessionStorage.getItem("text"));
            sessionStorage.setItem("showSnackbar", 'false');
        }
    } )

    $('#tryButton').click(function() {
        $.ajax({
            type : "POST",
            url : "/user/game/try",
            data : 'gameId=' + game_id,
            dataType: "json",
            timeout : 100000,
            success : function(data) {
                var result = parseInt(data);

                if(result == 0){
                    var text = 'You have no tries left...Play the game now!';
                    showSnackbar(text);
                }
                else{
                    $('#gamePlay').show(500);

                    if($('#gameMessage').is(":visible")){
                        hidePage();
                    }

                    if(result == 1){
                        setTimeout(showWinPage, 3000);
                    }
                    else {
                        setTimeout(showLosePage, 3000);
                    }

                    $("html, body").animate({ scrollTop: $(document).height() }, "slow");
                }
            },
            error : function() {

            }
        });
    });

    $('#playButton').click(function() {
        $.ajax({
            type : "POST",
            url : "/user/game/play",
            data : 'gameId=' + game_id,
            dataType: "json",
            timeout : 100000,
            success : function(data) {
                if(data['enoughBalance']){
                    document.getElementById("balance").innerHTML = (data['oldBalance'] - game_price).toString();

                    $('#gamePlay').show(500);

                    if($('#gameMessage').is(":visible")){
                        hidePage();
                    }

                    setTimeout(function() {showGameResultAndUpdateBalance(data['result'], data['newBalance'])}, 3000);

                    $("html, body").animate({ scrollTop: $(document).height() }, "slow");
                }
                else{
                    var text = 'Your balance is not enough for this game. Increase your balance now or play another game!';
                    showSnackbar(text);
                }

            },
            error : function(status) {
                console.log(status.status);
            }
        });
    });

    $('#favouriteButton').click(function () {
        $.ajax({
            type : "POST",
            url : "/user/game/unFavourite",
            data : 'gameId=' + game_id,
            timeout : 100000,
            success : function() {
                var text = 'The game was removed from your favourites...';

                sessionStorage.setItem("text", text);
                sessionStorage.setItem("showSnackbar", 'true');

                window.location.href = "/user/game?id=" + game_id;
            },
            error : function() {
            }
        });

    });

    $('#unFavouriteButton').click(function () {
        $.ajax({
            type : "POST",
            url : "/user/game/favourite",
            data : 'gameId=' + game_id,
            timeout : 100000,
            success : function() {
                var text = 'The game was added to your favourites!';

                sessionStorage.setItem("text", text);
                sessionStorage.setItem("showSnackbar", 'true');

                window.location.href = "/user/game?id=" + game_id;
            },
            error : function() {
            }
        });
    });

    $('input').on('change', function () {
        game_id = getParameterByName('id');
        var rate = {};
        rate["id"] = game_id;
        rate["value"] = $('input').val();

        $.ajax({
            type : "POST",
            url : "/user/game/rate",
            contentType : "application/json",
            data : JSON.stringify(rate),
            timeout : 100000,
            success : function() {
                var text = 'Thank you for your vote!';

                sessionStorage.setItem("text", text);
                sessionStorage.setItem("showSnackbar", 'true');

                window.location.href = "/user/game?id=" + game_id;
            },
            error : function() {
            }
        });
    });
});


function showSnackbar(text){
    var x = document.getElementById("snackbar");
    x.innerHTML = text;
    x.className = "show";
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}

function showSnackbar2(text){
    var x = document.getElementById("snackbar2");
    x.innerHTML = text;
    x.className = "show";
    setTimeout(function(){ x.className = x.className.replace("show", ""); }, 3000);
}

function showWinPage() {
    document.getElementById("loader").style.display = "none";
    document.getElementById("gameMessage").style.display = "block";
    document.getElementById("gameResult").innerHTML = "You win!!!";

}

function showLosePage() {
    document.getElementById("loader").style.display = "none";
    document.getElementById("gameMessage").style.display = "block";
    document.getElementById("gameResult").innerHTML = "You lose..."

}

function showGameResultAndUpdateBalance(result, balance) {
    document.getElementById("loader").style.display = "none";
    document.getElementById("gameMessage").style.display = "block";

    if(result){
        document.getElementById("gameResult").innerHTML = "You win!!!"
    }
    else{
        document.getElementById("gameResult").innerHTML = "You lose..."
    }

    document.getElementById("balance").innerHTML = balance;
}

function hidePage() {
    document.getElementById("loader").style.display = "block";
    document.getElementById("gameMessage").style.display = "none";
}

function getInfo(gameID, gamePrice){
    game_id = gameID;
    game_price = gamePrice;
}

function getGameId(gameID){
    game_id = gameID;
}

function getParameterByName(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}