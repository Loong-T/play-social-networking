/**
 * Created by Nerd on 2014/4/1 0001.
 */

function follow(userId) {
  var btn = $('#follow-btn');
  $.ajax({
    url: '/follow',
    data: 'uid=' + userId,
    error: function (rq, m) {
    },
    success: function (data) {
      btn.removeClass('btn-primary');
      btn.addClass('btn-info');
      btn.attr('onclick', 'unfollow(' + userId + ')');
      btn.html('<span class="glyphicon glyphicon-minus"></span> 已关注')
    }
  });
}

function unfollow(userId) {
  var btn = $('#follow-btn');
  $.ajax({
    url: '/unfollow',
    data: 'uid=' + userId,
    error: function (rq, m) {
    },
    success: function (data) {
      btn.removeClass('btn-info');
      btn.addClass('btn-primary');
      btn.attr('onclick', 'follow(' + userId + ')');
      btn.html('<span class="glyphicon glyphicon-plus"></span> 关注')
    }
  });
}
