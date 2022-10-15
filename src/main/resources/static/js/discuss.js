$(function () {
    $("#topBtn").click(setTop);
    $("#wonderfulBtn").click(setWonderful);
    $("#deleteBtn").click(setDelete);
});

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


// 置顶
function setTop() {
    $.post(
         "/discuss/top",
        {"id": $("#postId").val()},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                $("#topBtn").attr("disabled", "disabled");
            } else {
                alert(data.msg);
            }
        }
    )
}

// 加精
function setWonderful() {
    $.post(
         "/discuss/wonderful",
        {"id": $("#postId").val()},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                $("#wonderfulBtn").attr("disabled", "disabled");
            } else {
                alert(data.msg);
            }
        }
    )
}

// 拉黑（删除）
function setDelete() {
    $.post(
        "/discuss/delete",
        {"id": $("#postId").val()},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                location.href =  "/index";
            } else {
                alert(data.msg);
            }
        }
    )
}