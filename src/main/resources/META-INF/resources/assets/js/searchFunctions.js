function init(){

$.ajax({
  method: "GET",
  url: "http://localhost:8080/api/object/all",
})
  .done(function( json ) {
        var obj = jQuery.parseJSON(json)
        for(var i=0;i<obj.length;i++){
            var info = '<tr><th>' + obj[i].name + '</th><td>' + obj[i].street + '</td><td>' + obj[i].houseNumber + '</td>'
            var points = '<td> <div class="points">'
            for (var j=0; j<obj[i].points.length; j++) {
                points += obj[i].points[j].latitude + ' ' + obj[i].points[j].longitude + ' '
            }
            points += '</div></td><tr>'
            console.log(info + points)
            $('#objects').append(info+points);
        }
  });

    $('#searchname').keyup(function(){
        val = $('#searchname').val()
        $.ajax({
          method: "GET",
          url: "http://localhost:8080/api/object/filter/name/" + val,
        })
          .done(function( json ) {
                var obj = jQuery.parseJSON(json)
                for(var i=0;i<obj.length;i++){
                    var info = '<tr><th>' + obj[i].name + '</th><td>' + obj[i].street + '</td><td>' + obj[i].houseNumber + '</td>'
                    var points = '<td> <div class="points">'
                    for (var j=0; j<obj[i].points.length; j++) {
                        points += obj[i].points[j].latitude + ' ' + obj[i].points[j].longitude + ' '
                    }
                    points += '</div></td><tr>'
                    console.log(info + points)
                    $('#objects').append(info+points);
                }
          });
    })

    $('#searchaddr').keyup(function(){
          val = $('#searchaddr').val()
          $.ajax({
            method: "GET",
            url: "http://localhost:8080/api/object/filter/address/" + val,
          })
            .done(function( json ) {
                  var obj = jQuery.parseJSON(json)
                  for(var i=0;i<obj.length;i++){
                      var info = '<tr><th>' + obj[i].name + '</th><td>' + obj[i].street + '</td><td>' + obj[i].houseNumber + '</td>'
                      var points = '<td> <div class="points">'
                      for (var j=0; j<obj[i].points.length; j++) {
                          points += obj[i].points[j].latitude + ' ' + obj[i].points[j].longitude + ' '
                      }
                      points += '</div></td><tr>'
                      console.log(info + points)
                      $('#objects').append(info+points);
                  }
            });
    })

}