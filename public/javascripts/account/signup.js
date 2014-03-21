/**
 * Created by Nerd on 2014/3/19 0019.
 * 注册页面所需要的JS功能
 */

function restrict(elem) {
    var tf=$(elem);
    var rx=new RegExp();

    if('#username'==elem) {
        rx=/[\s]/gi;
    }

    tf.val(tf.val().replace(rx, ""));
}

function checkemail() {
    var elem=$('#email');
    var mail=elem.val();
    if(mail != '') {
        setpop(elem,'<span class="glyphicon glyphicon-question-sign" style="color: orange"></span> 检查Email中...')
        elem.popover('show');
        $.ajax({
            url:'/emailcheck',
            data:'email='+mail,
            error:function(rq,m){
                if(rq.status==400){
                    $('#submit').attr('disabled','disabled');
                    elem.popover('destroy');
                    setpop(elem,'<span class="glyphicon glyphicon-remove" style="color: red"></span> '+rq.responseText);
                    elem.popover('show');
                }
            },
            success:function(data) {
                $('#submit').removeAttr('disabled');
                elem.popover('destroy');
                setpop(elem,'<span class="glyphicon glyphicon-ok" style="color: dodgerblue"></span> '+data);
                elem.popover('show');
            }
        })
    }
}

function namehint(){
    var elem=$('#username');
    elem.popover('destroy');
    setpop(elem,'<span class="glyphicon glyphicon-info-sign" style="color: orange"></span> 用户名内不能包含空白符，长度在50字符以内')
    elem.popover('show');
}

function setpop(elem,showdata) {
    elem.popover({
        placement:'right',
        trigger:'manual',
        html:true,
        content:showdata
    });
}

function removepop(s) {
    $(s).popover('destroy');
}
