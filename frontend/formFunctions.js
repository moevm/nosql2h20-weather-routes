/* When the user clicks on the button,
            toggle between hiding and showing the dropdown content */
function initMap() {
    var myLatlng = new google.maps.LatLng(-34.397, 150.644);
    var myOptions = {
        zoom: 8,
        center: myLatlng,
        mapTypeId: google.maps.MapTypeId.HYBRID
    }
    var map = new google.maps.Map(document.getElementById("map_canvas"), myOptions);
}
var marker = new google.maps.Marker({
    position: myLatlng,
    map: map,
    title:"Hello World!"
});