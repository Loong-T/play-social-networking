/**
 * Created by Nerd on 2014/3/19 0019.
 * 注册页面所需要的JS功能
 */

function restrict(elem) {
    var tf;
    var rx=new RegExp();

    if('email'==elem) {
        tf=$('#email');
        rx=/[' "]/gi;
    }

    tf.value=tf.value.replace(rx, "")
}

function checkemail() {
    var elem=$('#email');
    var mail=elem.val();
    if(mail != '') {
        elem.popover({
            placement:'right',
            trigger:'manual',
            html:true,
            content:'<span class="glyphicon glyphicon-question-sign" style="color: red"></span> 检查Email中...'
        });
        elem.popover('show');
        $.ajax({
            url:'/emailcheck',
            data:'email='+mail,
            success:function(data) {
                elem.popover('destroy');
                elem.popover({
                    placement:'right',
                    trigger:'manual',
                    html:true,
                    content:'<span class="glyphicon glyphicon-info-sign" style="color: orange"></span> '+data
                })
                elem.popover('show');
                setInterval(function(){elem.popover('hide')}, 5000);
            }
        })
    }
}
