/**
 * Created by Nerd on 2014/3/26 0026.
 */

function login() {
    var email = $('#email').val();
    var pwd = $('#pwd').val();
    var checked = $('input:checkbox').prop('checked') ? true : false;
    var submit = $('button[type=submit]');
    if (email != '' && pwd != '') {
        submit.html('<span class="glyphicon glyphicon-repeat spin-icon"></span>');
        $.ajax({
            url: '/login',
            type: 'POST',
            data: {email: email, password: pwd, remember: checked},
            success: function (d) {
                window.location.href = d;
            },
            error: function (rq, m) {
                submit.html('登录');
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