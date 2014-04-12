/**
 * Created by Nerd on 2014/4/12 0009.
 */

$('.post-img > a').click(function (event) {
    $('#modalImg').attr('src', $(this).attr('data-img-url'));
});
