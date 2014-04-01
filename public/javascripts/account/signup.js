/**
 * Created by Nerd on 2014/3/19 0019.
 * 注册页面所需要的JS功能
 */

var global = this;

window.onload = function () {
  // 用来检查登录按钮是否可以按下
  global['validatedEmail'] = false;
  global['validatedName'] = false;
};

/**
 * 会将用户名中输入的空格去掉
 */
function restrict(elem) {
  var tf = $(elem);
  var rx = new RegExp();

  if ('#username' == elem) {
    rx = /[\s]/gi;
  }

  tf.val(tf.val().replace(rx, ""));
}

/**
 * 检查Email是否合法
 */
function checkEmail() {
  var elem = $('#email');
  var mail = elem.val();
  if (mail != '') {
    setPop(elem, '<i class="fa fa-spinner fa-spin" style="color: orange"></i> 检查Email中...');
    elem.popover('show');
    $.ajax({
      url: '/emailcheck',
      data: 'email=' + mail,
      error: function (rq, m) {
        if (rq.status == 400) {
          global['validatedEmail'] = false;
          $('#submit').attr('disabled', 'disabled');
          elem.popover('destroy');
          setPop(elem, '<span class="glyphicon glyphicon-remove" style="color: red"></span> ' + rq.responseText);
          elem.popover('show');
        }
      },
      success: function (data) {
        global['validatedEmail'] = true;
        if (global['validatedName']) {
          $('#submit').removeAttr('disabled');
        }
        elem.popover('destroy');
        setPop(elem, '<span class="glyphicon glyphicon-ok" style="color: dodgerblue"></span> ' + data);
        elem.popover('show');
      }
    });
  }
}

/**
 * 检查用户名是否合法
 */
function checkName() {
  var elem = $('#username');
  var name = elem.val();
  elem.popover('destroy');
  if (name != '') {
    setPop(elem, '<i class="fa fa-info" style="color: orange"></i> 检查用户名中...');
    elem.popover('show');
    $.ajax({
      url: '/namecheck',
      data: 'username=' + name,
      error: function (rq, m) {
        if (rq.status == 400) {
          global['validatedName'] = false;
          $('#submit').attr('disabled', 'disabled');
          elem.popover('destroy');
          setPop(elem, '<span class="glyphicon glyphicon-remove" style="color: red"></span> ' + rq.responseText);
          elem.popover('show');
        }
      },
      success: function (data) {
        global['validatedName'] = true;
        if (global['validatedEmail']) {
          $('#submit').removeAttr('disabled');
        }
        elem.popover('destroy');
        setPop(elem, '<span class="glyphicon glyphicon-ok" style="color: dodgerblue"></span> ' + data);
        elem.popover('show');
      }
    });
  }
}

function nameHint() {
  var elem = $('#username');
  elem.popover('destroy');
  setPop(elem, '<i class="fa fa-info" style="color: orange"></i> 用户名内不能包含空白符，长度在50字符以内');
  elem.popover('show');
}

/**
 * 设置弹出框的样式和内容
 */
function setPop(elem, showData) {
  elem.popover({
    placement: 'right',
    trigger: 'manual',
    html: true,
    content: showData
  });
}

/**
 * 移除弹出框
 */
function rmPop(s) {
  $(s).popover('destroy');
}
