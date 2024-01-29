function openDialog(rowId) {
  var content = 'Load data for row ' + rowId + ' and populate the form fields';
  var itemKey = $('#map .tr[data-rowid="' + rowId + '"] .td:first-child').text();

  $('#dialog').dialog({
    title: itemKey + '- - -',
    modal: true,
    buttons: {
      Save: function() {
        var editedData = {
          price: $('#value').val(),
        };

        $.ajax({
          url: 'http://localhost:8081/dental/main/product-map/' + rowId,
          type: 'PUT',
          data: 'price=' + editedData.price,
          contentType: 'application/json',
          success: function(response) {
            console.log('PUT request successful');
            console.log(response);
          },
          error: function(xhr, status, error) {
            console.log('PUT request failed');
            console.log(xhr.responseText);
          }
        });

      },
      Delete: function() {
        if (confirm('delete the product item?')) {
          $.ajax({
            url: 'http://localhost:8081/dental/main/product-map/' + rowId,
            type: 'DELETE',
            success: function(response) {
              console.log('DELETE request successful');
              console.log(response);
            },
            error: function(xhr, status, error) {
              console.log('DELETE request failed');
              console.log(xhr.responseText);
            }
          });

        }
      }
    },
  });
}