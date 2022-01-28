$('#btn').click(function () {
    var url = '/blog/test';
    $.ajax({
        url: url,
        type: 'POST',
        success: function (data) {
            $(".article_type").html(data);
        }
    })
})