function init() {
     $('#upload_btn').on('click', function() {
            $.ajax({
            method: "POST",
            data: $('#file')[0].files[0],
            url: "http://localhost:8080/api/import/map"
            })
            .done(function( json ) {

            });
     });

      $('#export_btn').on('click', function() {
             $.ajax({
             method: "GET",
             url:  "http://localhost:8080/api/export"
             })
             .done(function( json ) {

             });
      });
}