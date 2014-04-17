/**
 * Created by Nerd on 2014/4/16 0016.
 */

$(document).ready(function () {
    if ($('#loginBadge')) {
        $.ajax({
            url: '/unread-count',
            success: function (data) {
                $('#unreadCommentBadge').html(data.unreadCommentCount);
                $('#unreadFollowerBadge').html(data.unreadFollowerCount);
                $('#unreadPostBadge').html(data.unreadPostCount);
                $('#unreadTotalBadge').html(data.unreadPostCount + data.unreadCommentCount + data.unreadFollowerCount);
            }
        });
    }
});