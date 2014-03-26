/**
 * Created by Nerd on 2014/3/26 0026.
 */

function login() {
  var e = $('#email').val();
  var p = $('#pwd').val();
  if (e != '' && p != '') {
    $.ajax({
      url: '/login',
      type: 'POST',
      data: {email: e, password: p},
      success: function (d) {
      },
      error: function (rq, m) {
        if (rq.status == 401) {
          if ($('.alert')) {
            $('div').remove('.alert');
          }
          $('.jumbotron').append('<div class="alert alert-warning fade in"><button type="button" class="close" data-dismiss="alert" aria-hidden="true">×</button>'
            + rq.responseText + '</div>');
        }
      }
    });
  }
}