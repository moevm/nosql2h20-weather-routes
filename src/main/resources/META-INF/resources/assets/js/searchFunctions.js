//$(document).ready(function(){
//$.getJSON('GET_OBJECT.json', function(data) {
//        for(var i=0;i<data.objects.length;i++){
//            $('#objects').append('<tr><th>' + data.objects[i].name + '</th><td>' + data.objects[i].street +
//                '</td><td>' + data.objects[i].house_number + '</td><td>' + data.objects[i].points[0].lon + ' ' +  data.objects[i].points[0].lat + '</td><tr>');
//        }
//    });
//
//});
//
//}

function init(){
$.getJSON('GET_OBJECT.json', function(data) {
        for(var i=0;i<data.objects.length;i++){
            $('#objects').append('<tr><th>' + data.objects[i].name + '</th><td>' + data.objects[i].street +
                '</td><td>' + data.objects[i].house_number + '</td><td>' + data.objects[i].points[0].lon + ' ' +  data.objects[i].points[0].lat + '</td><tr>');
        }
    });


}