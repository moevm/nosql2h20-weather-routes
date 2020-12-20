function init() {
    ymaps.ready(init);
        function init(){
            var myMap = new ymaps.Map("map_canvas", {
                center: [59.9386, 30.3141],
                zoom: 10
            });
    from = window.localStorage.getItem("from")
    to = window.localStorage.getItem("to")
//    $.ajax({
//    method: "GET",
//    url: "ПОКА ХЗ",
//    })
//    .done(function( json ) {
//          var obj = jQuery.parseJSON(json)
//
//    });
}
}