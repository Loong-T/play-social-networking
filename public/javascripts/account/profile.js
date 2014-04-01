/**
 * Created by Nerd on 2014/4/1 0001.
 */

function follow(userId) {
  var btn = $('#follow-btn');
  var icon = $('#follow-btn > span');
  $.ajax({
    url: '/follow',
    data: 'uid=' + userId,
    error: function (rq, m) {
    },
    success: function (data) {
      // 将btn的点击事件换掉
      btn.empty();
      btn.append('<span class="glyphicon glyphicon-minus"></span> 已关注');
    }
  });
}
