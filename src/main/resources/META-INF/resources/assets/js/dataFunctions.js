function init() {
    $('#loader').addClass('hidden')

    $('#upload_btn').on('click', function () {
        event.stopPropagation(); // Остановка происходящего
        event.preventDefault();

        $('#loader').removeClass('hidden')
        $('#upload_btn').prop( "disabled", true );

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
            $('#loader').addClass('hidden');
            $('#upload_btn').prop( "disabled", false );
            alert('Файл успешно загружен');
        }).fail(function(data){
            $('#upload_btn').prop( "disabled", false );
            alert('При загрузке файла произошла ошибка')
        });
    });

    $('#export_btn').on('click', function () {
        $('#export_btn').prop( "disabled", true );
        $.ajax({
            url: '/api/export'
        }).done(function (xml) {
            $('#export_btn').prop( "disabled", false );
            let xmlText = new XMLSerializer().serializeToString(xml);
            let blob = new Blob([xmlText], {type: 'text/plain'});
            let link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = 'database.graphml';
            link.click();
        }).fail(function(data){
            $('#export_btn').prop( "disabled", false );
            alert('При экспорте базы данных  произошла ошибка')
        });
    });
}
