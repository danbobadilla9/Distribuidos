$( document ).ready(function() { // Se carga el JS cuando el HTML se completo
    console.log( "ready!" );

    // find elements on the page
    var banner = $("#banner-message");
    var button = $("#submit_button");
    var searchBox = $("#search_text");
    var resultsTable = $("#results table tbody");
    var resultsWrapper = $("#results");
    var noResultsError = $("#no_results_error");

    // handle search click
    button.on("click", function(){//Añadimos el click
        banner.addClass("alt");

        // send request to the server
        $.ajax({//Peticion POST
          method : "POST",
          contentType: "application/json",//Decimos que es un JSON
          data: createRequest(),
          url: "procesar_datos", //Hacia donde va
          dataType: "json",
          success: onHttpResponse
          });
      });

    function createRequest() {
        var searchQuery = searchBox.val();//Obtenemos lo que hay dentro del textfield

        // Search request to the server
        var frontEndRequest = {//Creamos un objeto
            search_query: searchQuery,
        };

        return JSON.stringify(frontEndRequest);//Convertimos el objeto aJSON
    }
    // THEN DEL AJAX
    function onHttpResponse(data, status) {
        if (status === "success" ) {
            console.log(data);
            addResults(data);
        } else {
            alert("Error connecting to the server " + status);
        }
    }

    /*
        Add results from the server to the html or how an error message
     */
    function addResults(data) {
        for (let index = 0; index < data.length; index++) {
            var score = data[index].score;
            var title = data[index].title;
            noResultsError.hide();
            resultsWrapper.show();
            resultsTable.append("<tr><td>Datos recibidos: " + title + "</td><td> Numero de palabras: " + score + "</td></tr>"); 
        }
    }
});
