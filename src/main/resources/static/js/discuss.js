function like(btn, entityType, entityId,fromuserid) {
    $.post(
         "/like",
        {"entityType": entityType, "entityId": entityId,"fromuserid": fromuserid},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                //likecount很好理解就是帖子目前点赞数
                //likestatus应该是说点完后的帖子点赞状态
                $(btn).children("i").text(data.likeCount);
                $(btn).children("b").text(data.likeStatus==1?'已赞':'赞');
            } else {
                alert(data.msg);
            }
        }
    );
}