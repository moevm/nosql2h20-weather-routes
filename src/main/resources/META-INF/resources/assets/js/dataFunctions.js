function init() {
    $('#upload_btn').on('click', function () {
        event.stopPropagation(); // Остановка происходящего
        event.preventDefault();

        let file = $('#file').prop('files')[0]
        let data = new FormData();
        data.append('uploadedFile', file)

        $.ajax({
            url: '/api/import/map',
            method: 'POST',
            data: data,
            cache: false,
            processData: false,
            contentType: false
        }).done(function (json) {
        });
    });

    $('#export_btn').on('click', function () {
        $.ajax({
            url: '/api/export'
        }).done(function (xml) {
            let xmlText = new XMLSerializer().serializeToString(xml);
            let blob = new Blob([xmlText], {type: 'text/plain'});
            let link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = 'database.graphml';
            link.click();
        })
    });
}
