var files;

function init() {

    $('input[type=file]').on('change', function(){
        files = this.files;
        console.log(files)
    });

    $('#upload_btn').on('click', function () {
        event.stopPropagation(); // Остановка происходящего
        event.preventDefault();

        var data = new FormData();
        $.each( files, function( key, value ){
            data.append( key, value );
            console.log(data)
        });


        $.ajax({
            url: "http://localhost:8080/api/import/map",
            method: 'POST',
            data: data,
            cache: false,
            dataType: 'xml',
            processData: false, // Не обрабатываем файлы (Don't process the files)
            contentType: false,
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
