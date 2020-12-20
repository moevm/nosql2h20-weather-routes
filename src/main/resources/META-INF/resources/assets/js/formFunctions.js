var from = null
var to = null

function init() {
    ymaps.ready(init);
        function init(){
            var myMap = new ymaps.Map("map_canvas", {
                center: [59.9386, 30.3141],
                zoom: 10
            });

            myMap.events.add('click', function (e) {
                var coords = e.get('coords');
                fromValue = $('#from').val()
                toValue = $('#to').val()
                if (fromValue == "") {
                    myMap.balloon.open(coords, {
                        contentHeader:'Откуда',
                    });
                    var myFromReverseGeocoder = ymaps.geocode(coords);
                    myFromReverseGeocoder.then(
                        function (res) {
                             $('#from').val(res.geoObjects.get(0).properties.get('name'));
                        },
                        function (err) {
                            alert('Ошибка');
                        }
                    );
                } else {
                    myMap.balloon.open(coords, {
                        contentHeader:'Куда',
                    });
                    var myToReverseGeocoder = ymaps.geocode(coords);
                    myToReverseGeocoder.then(
                        function (res) {
                             $('#to').val(res.geoObjects.get(0).properties.get('name'));
                        },
                        function (err) {
                            alert('Ошибка');
                        }
                    );
                }

            });

            $('#searchbtn').on('click', function(myMap) {
                fromValue = $('#from').val()
                toValue = $('#to').val()

                if (fromValue == "" || toValue == "") {
                    alert("Пожалуйста, заполните поля Откуда и Куда")
                    return
                }

                var fromGeocoder = ymaps.geocode(fromValue);
                var toGeocoder = ymaps.geocode(toValue);

                fromGeocoder.then(
                    function (res) {
                        var coordinates = res.geoObjects.get(0).geometry.getCoordinates();
                        from = coordinates
                    },
                    function (err) {
                        alert('Ошибка');
                    }
                );

                toGeocoder.then(
                    function (res) {
                        var coordinates = res.geoObjects.get(0).geometry.getCoordinates();
                        to = coordinates
                        window.localStorage.setItem("from", from)
                        window.localStorage.setItem("to", to)
                //        alert('Координаты откуда :' + from + ', координаты куда :' + to);
                        window.location.href = '/route';
                    },
                    function (err) {
                        alert('Ошибка');
                    }
                );
            })
    }
}

