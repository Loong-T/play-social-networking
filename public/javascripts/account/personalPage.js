/**
 * Created by Nerd on 2014/4/7 0007.
 */

$(document).ready(function () {
  $('input[type=file]').bootstrapFileInput();
});

$('#post-form').on('submit', function(event) {
  event.preventDefault();
  var formData = new FormData();
  var postPic = $('#post-pic');
  var picName = postPic.prev('span');
  var postContent = $('#post-content');
  var submitBtn = $('#post-submit');

  submitBtn.attr('disabled', 'disabled');
  submitBtn.html("<span class='glyphicon glyphicon-repeat spin-icon'></span>");

  formData.append('content', postContent.val());
  formData.append('pic', postPic[0].files[0]);

  var xhr = new XMLHttpRequest();

  xhr.open('POST', '/new-post');

  xhr.onreadystatechange = function() {
    if (xhr.readyState == 4) {
      submitBtn.removeAttr("disabled");
      submitBtn.html('发布');

      if (xhr.status == 200) {
        // OK
        postContent.val('');
        picName.addClass('glyphicon glyphicon-picture');
        picName.html(' 添加照片');
        alert(xhr.responseText);
        setInterval(function() {progressDiv.addClass('hidden')}, 800);
      }
      else {
        alert('失败');
      }
    }
    else {
      submitBtn.attr('disabled', 'disabled');
      submitBtn.html("<span class='glyphicon glyphicon-repeat spin-icon'></span>");
    }
  };

  var progressDiv = $('.progress');
  var progressBar = $('.progress-bar');
  // progress回调
  xhr.upload.onprogress = function(event) {
    var progress;
    if (event.lengthComputable) {
      progress = Math.floor(event.loaded / event.total * 100 | 0);
      progressBar.attr('aria-valuenow', progress);
      progressBar.css('width', progress + '%');
    }
  };

  progressDiv.removeClass('hidden');
  xhr.send(formData);
});

/**
 *
 * jquery.charcounter.js version 1.2
 * requires jQuery version 1.2 or higher
 * Copyright (c) 2007 Tom Deater (http://www.tomdeater.com)
 * Licensed under the MIT License:
 * http://www.opensource.org/licenses/mit-license.php
 *
 */

(function($) {
  /**
   * attaches a character counter to each textarea element in the jQuery object
   * usage: $("#myTextArea").charCounter(max, settings);
   */

  $.fn.charCounter = function (max, settings) {
    max = max || 100;
    settings = $.extend({
      container: "<span></span>",
      classname: "charcounter",
      format: "%1",
      pulse: true,
      delay: 0
    }, settings);
    var p, timeout;

    function count(el, container) {
      el = $(el);
      if (el.val().length > max) {
        el.val(el.val().substring(0, max));
        if (settings.pulse && !p) {
          pulse(container, true);
        };
      };
      if (settings.delay > 0) {
        if (timeout) {
          window.clearTimeout(timeout);
        }
        timeout = window.setTimeout(function () {
          container.html(settings.format.replace(/%1/, (max - el.val().length)));
        }, settings.delay);
      } else {
        container.html(settings.format.replace(/%1/, (max - el.val().length)));
      }
    };

    function pulse(el, again) {
      if (p) {
        window.clearTimeout(p);
        p = null;
      };
      el.animate({ opacity: 0.1 }, 100, function () {
        $(this).animate({ opacity: 1.0 }, 100);
      });
      if (again) {
        p = window.setTimeout(function () { pulse(el) }, 200);
      };
    };

    return this.each(function () {
      var container;
      if (!settings.container.match(/^<.+>$/)) {
        // use existing element to hold counter message
        container = $(settings.container);
      } else {
        // append element to hold counter message (clean up old element first)
        $(this).next("." + settings.classname).remove();
        container = $(settings.container)
          .insertAfter(this)
          .addClass(settings.classname);
      }
      $(this)
        .unbind(".charCounter")
        .bind("keydown.charCounter", function () { count(this, container); })
        .bind("keypress.charCounter", function () { count(this, container); })
        .bind("keyup.charCounter", function () { count(this, container); })
        .bind("focus.charCounter", function () { count(this, container); })
        .bind("mouseover.charCounter", function () { count(this, container); })
        .bind("mouseout.charCounter", function () { count(this, container); })
        .bind("paste.charCounter", function () {
          var me = this;
          setTimeout(function () { count(me, container); }, 10);
        });
      if (this.addEventListener) {
        this.addEventListener('input', function () { count(this, container); }, false);
      };
      count(this, container);
    });
  };

})(jQuery);

$(function() {
  $(".counted").charCounter(140,{container: "#counter"});
});

