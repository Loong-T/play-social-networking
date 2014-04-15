/**
 * Created by Nerd on 2014/4/9 0009.
 */



$(document).ready(function () {

    $('.post-img > a').click(function (event) {
        $('#modalImg').attr('src', $(this).attr('data-img-url'));
    });

    var cmtContainers = $('.comment-container');
    var cmtBtn = $('.comment-btn');
    cmtContainers.hide();

    cmtBtn.click(function () {
        var currentBtn = $(this);
        var dataFor = currentBtn.attr('data-for');
        var idFor = $(dataFor);
        var chevron = currentBtn.find('.chevron');

        idFor.slideToggle(400, function () {
            if (idFor.is(':visible')) {
                chevron.html('<span class="glyphicon glyphicon-chevron-up"></span>');
            }
            else {
                chevron.html('<span class="glyphicon glyphicon-chevron-down"></span>');
            }
        });
    });

    var addCommentBtns = $('.add-comment-btn');
    addCommentBtns.click(function () {
        var currentBtn = $(this);
        var postId = currentBtn.attr('data-post-id');
        var commentInput = $('#input-comment-' + postId)

        $.ajax({
            url: 'comment',
            type: 'POST',
            data: {post: postId, comment: commentInput.val()},
            error: function (rq, m) {
                alert(rq.responseText)
            },
            success: function (data) {
                commentInput.val('');
                $('#comment-group-to-post-' + postId).append('<li class="list-group-item text-muted"> <a href="'
                    + data.authorUrl + '">' + data.author + ' </a>：' + data.comment + '</li>');
            }
        })

    });
});
