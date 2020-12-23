function init() {

    $.ajax({
        method: "GET",
        url: "http://localhost:8080/api/object/all",
    })
        .done(function (obj) {
            $('#objects').find("tr:gt(0)").remove();
            for (var i = 0; i < obj.length; i++) {
                var info = '<tr><th>' + obj[i].name + '</th><td>' + obj[i].street + '</td><td>' + obj[i].houseNumber + '</td>'
                var lat = '<td> <div class="points">'
                for (var j = 0; j < obj[i].points.length; j++) {
                    lat += obj[i].points[j].latitude + ' '
                }
                lat += '</div></td>'
                var lon = '<td> <div class="points">'
                    for (var j = 0; j < obj[i].points.length; j++) {
                        lon += obj[i].points[j].longitude + ' '
                }
                lon += '</div></td>'
                var perc = '<td> <div class="points">'
                    for (var j = 0; j < obj[i].points.length; j++) {
                        perc += obj[i].points[j].precipitationValue + ' '
                }
                perc += '</div></td><tr>'

                $('#objects').append(info + lat + lon + perc);
            }
        });
//
    $('#searchname').keyup(function () {
        val = $('#searchname').val()
        $.ajax({
            method: "GET",
            url: "http://localhost:8080/api/object/filter/name/" + val,
        })
            .done(function (obj) {
                $('#objects').find("tr:gt(0)").remove();

                for (var i = 0; i < obj.length; i++) {
                    var info = '<tr><th>' + obj[i].name + '</th><td>' + obj[i].street + '</td><td>' + obj[i].houseNumber + '</td>'
                    var points = '<td> <div class="points">'
                    for (var j = 0; j < obj[i].points.length; j++) {
                        points += obj[i].points[j].latitude + ' ' + obj[i].points[j].longitude + ' '
                    }
                    points += '</div></td><tr>'
                    console.log(info + points)
                    $('#objects').append(info + points);
                }
            });
    })

    $('#searchaddr').keyup(function () {
        val = $('#searchaddr').val()
        $.ajax({
            method: "GET",
            url: "http://localhost:8080/api/object/filter/address/" + val,
        })
            .done(function (obj) {
                $('#objects').find("tr:gt(0)").remove();
                for (var i = 0; i < obj.length; i++) {
                    var info = '<tr><th>' + obj[i].name + '</th><td>' + obj[i].street + '</td><td>' + obj[i].houseNumber + '</td>'
                    var points = '<td> <div class="points">'
                    for (var j = 0; j < obj[i].points.length; j++) {
                        points += obj[i].points[j].latitude + ' ' + obj[i].points[j].longitude + ' '
                    }
                    points += '</div></td><tr>'
                    console.log(info + points)
                    $('#objects').append(info + points);
                }
            });
    })
}
