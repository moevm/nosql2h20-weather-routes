function init() {
    $('#upload_btn').on('click', function () {
        $.ajax({
            method: "POST",
            data: $('#file')[0].files[0],
            url: "http://localhost:8080/api/import/map"
        }).done(function (json) {
        });
    });

    $('#export_btn').on('click', function () {
        $.ajax({
            url: "api/export"
        }).done(function (xml) {
            var xmlText = new XMLSerializer().serializeToString(xml);
            var blob = new Blob([xmlText], {type: 'text/plain'});
            var link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = "database.graphml";
            link.click();
        })
    });
}
