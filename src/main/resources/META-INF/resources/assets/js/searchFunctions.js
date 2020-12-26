function init() {

    $.ajax({
        method: 'GET',
        url: '/api/object/all',
    })
        .done(function (obj) {
            $('#objects').find('tr:gt(0)').remove();

            for (let i = 0; i < obj.length; i++) {
                let info = '<tr><th>' + obj[i].name + '</th><td>' + obj[i].street + '</td><td>' + obj[i].houseNumber + '</td>'
                let point = '<td><div class="points"><table><tr><th>Широта</th><th>Долгота</th><th>Кол-во осадков</th></tr><tbody>'

                for (let j = 0; j < obj[i].points.length; j++) {
                    point += '<td>'
                    point += obj[i].points[j].latitude + '</td><td>'
                    point += obj[i].points[j].longitude + '</td><td>'
                    point += obj[i].points[j].precipitationValue + '</td></tr>'
                }
                point += '</tbody></table></div></td></tr>';

                $('#objects').append(info + point);
            }
        });

    $('#searchname').keyup(function () {
        val = $('#searchname').val()
        $.ajax({
            method: 'GET',
            url: '/api/object/filter/name/' + val,
        })
            .done(function (obj) {
                $('#objects').find('tr:gt(0)').remove();

                for (let i = 0; i < obj.length; i++) {
                    let info = '<tr><th>' + obj[i].name + '</th><td>' + obj[i].street + '</td><td>' + obj[i].houseNumber + '</td>'
                    let points = '<td> <div class="points">'

                    for (let j = 0; j < obj[i].points.length; j++) {
                        points += obj[i].points[j].latitude + ' ' + obj[i].points[j].longitude + ' '
                    }
                    points += '</div></td><tr>'

                    $('#objects').append(info + points);
                }
            });
    })

    $('#searchaddr').keyup(function () {
        val = $('#searchaddr').val()
        $.ajax({
            method: 'GET',
            url: '/api/object/filter/address/' + val,
        })
            .done(function (obj) {
                $('#objects').find('tr:gt(0)').remove();

                for (let i = 0; i < obj.length; i++) {
                    let info = '<tr><th>' + obj[i].name + '</th><td>' + obj[i].street + '</td><td>' + obj[i].houseNumber + '</td>'
                    let points = '<td> <div class="points">'

                    for (let j = 0; j < obj[i].points.length; j++) {
                        points += obj[i].points[j].latitude + ' ' + obj[i].points[j].longitude + ' '
                    }
                    points += '</div></td><tr>'

                    $('#objects').append(info + points);
                }
            });
    })

    $('#addbtn').on('click', function () {
        nameValue = $('#addname').val()
        streetValue = $('#addstr').val()
        numberValue = $('#addnum').val()
        pointsValue = $('#addpoints').val()

        if (nameValue == '' || streetValue == '' || numberValue == '' || pointsValue == '') {
            alert('Пожалуйста, заполните поля формы.')
            return
        }

        $.ajax({
            method: 'POST',
            url: hui, //TODO set REST resource URL
            data: ([{
                'name': nameValue,
                'street': streetValue,
                'number': numberValue,
                'points': pointsValue
            }]),
        })
    })
}
