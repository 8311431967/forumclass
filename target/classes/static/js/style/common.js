$(function(){
    var userId = $("#user_id").val();
    // “显示全部”与“收起”***********************************************************
    $(document).on("click", ".expand", function(){
        var $parent = $(this).parent();
        $parent.addClass("hide");
        $parent.next().removeClass("hide");
    });
    $(document).on("click", ".pack-up", function(){
        var $parent = $(this).parent();
        $parent.addClass("hide");
        $parent.prev().removeClass("hide");
    });
    $(document).on("click", ".feed-summary", function(event){
        if($(event.target).hasClass("expand")){
            return true;
        }
        $(this).find(".expand").click();
    });

    // 显示评论******************************************************************8
    $(document).on("click", ".toggle-comment", function(event){
        event.preventDefault();

        // 取得对应回答id的评论
        // var $item = $(this).parents(".feed-item") == undefined ? $(this).parents(".feed-item") : $(this).parents(".List-item");
        if($(this).parents(".feed-item").html()) {
            var $item = $(this).parents(".feed-item");
        }else if($(this).parents(".List-item").html()) {
            var $item = $(this).parents(".List-item");
        }else {
            var $item = $(this).parents(".answer-item");
        }


        // 收起评论，不进行请求
        if(!$item.find(".comment-holder").hasClass("hide")) {
            // alert("收起评论");
            $(this).parent().next().toggleClass("hide");
            $(this).toggleClass("on");
            return true;
        }

        $item.find(".comment-box").empty();       // 清空评论区域
        var answerId = $item.attr("id").substr(4);
        var answerThread = $item.attr("data-answerThread");


        var questionId;
        if($item.find(".question-link").html()) {
            questionId = $item.find(".question-link").attr("id").slice(4);
        }else {
            var currentPath = window.location.pathname;   //    /questions/question/{id}
            questionId = currentPath.substr(20);
        }

        console.log("questionId: " + questionId);

        $.ajax({
            type: "GET",
            url: "/comments/q/" + questionId + "/child",
            contentType: "application/json",
            data: {
                uid: userId,
                thread: answerThread + answerId + '/'
            },
            dataType: "json",
            success: function (data) {
                console.log("answerId: " + answerId);
                console.log("newThread: " + answerThread + answerId + '/');
                if(data.length == 0){
                    return;
                }
                $.each(data, function(){

                    // 输出格式：01-23 18:55
                    var date = new Date(this.time);
                    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-',
                        D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ',
                        h = date.getHours() + ':',
                        m = date.getMinutes();
                    var time = M+D+h+m;

                    var str = '<div class="commentItem" data-commentId="'
                        + this.id + '" data-commentThread="'
                        + this.thread + '"><div class="commentItem-header"><a href="/users/u/'
                        + this.authorId + '" class="author-link" data-authorId="'
                        + this.authorId + '">'
                        + this.authorName + '</a>';
                    // 此为回复
                    if(this.replyId) {
                        str = str + ' 回复 <a href="/users/u/'
                            + this.replyId + '" class="author-link">'
                            + this.replyName + '</a>';
                    }
                    // 此为本人发起的评论或回复
                    if(this.authorId == userId) {
                        str += '<button type="button" class="btn commentBox-textButton delete-button">删除</button>';
                    }
                    str = str + '</div><div class="commentItem-content">'
                        + this.summary + '</div><div class="commentItem-footer"><span class="commentItem-likes"><span>'
                        + this.numOfSupport + '</span><span>赞</span></span><time>'    // 10-29&nbsp;17:23
                        + time + '</time><a class="commentItem-action action-reply unselectable"><span class="glyphicon glyphicon-comment"></span><span>回复</span></a>';
                    // 用户之前是否点赞过此评论或回复
                    if(this.support) {
                        str += '<a class="commentItem-action action-like unselectable on"><span class="glyphicon glyphicon-thumbs-up"></span><span>赞</span></a></div></div>';
                    }else {
                        str += '<a class="commentItem-action action-like unselectable"><span class="glyphicon glyphicon-thumbs-up"></span><span>赞</span></a></div></div>';
                    }
                    // 插入文档流
                    $item.find(".comment-box").append(str);
                });
            }
        });


        $(this).parent().next().toggleClass("hide");
        $(this).toggleClass("on");
    });

    // 具体评论中显示回复和点赞
    $(document).on("mouseenter", ".commentItem", function(event){
        $(this).find(".commentItem-action").css("visibility", "visible");
    });
    $(document).on("mouseleave", ".commentItem", function(event){
        $(this).find(".commentItem-action").css("visibility", "hidden");
    });

    // 蓝色方框的点赞（点赞回答）*******************************************************
    $(document).on("click", ".feed-vote", function(){
        // var $item = $(this).parents(".feed-item") == "undefined" ? $(this).parents(".feed-item") : $(this).parents(".List-item");
        if($(this).parents(".feed-item").html()) {
            var $item = $(this).parents(".feed-item");
        }else if ($(this).parents(".List-item").html()){
            var $item = $(this).parents(".List-item");
        }else {
            var $item = $(this).parents(".answer-item");
        }
        var answerId = $item.attr("id").substr(4);
        var $this = $(this);

        console.log("userId: " + userId + "     answerId: " + answerId);

        // 点赞
        if(!$this.hasClass("voted")){
            $.ajax({
                type: "POST",
                url: "/supports/support",
                contentType: "application/json",
                data: JSON.stringify({
                    userId: userId,
                    commentId: answerId
                }),
                dataType: "json",
                success: function (data) {
                    console.log("点赞结果: " + data.result);

                    var voteNum;
                    $this.toggleClass("voted");
                    voteNum = parseInt($this.html()) + 1;
                    $this.attr("title", "取消赞");
                    $this.html(voteNum);
                    if($this.siblings(".answer-head").find(".voteCount")){
                        $this.siblings(".answer-head").find(".voteCount").html(voteNum);
                    }
                }
            });
        }else {  // 取消赞
            $.ajax({
                type: "POST",
                url: "/supports/support/remove",
                data:{
                    userId: userId,
                    commentId: answerId
                },
                dataType: "json",
                success: function (data) {
                    console.log("取消赞结果: " + data.result);

                    var voteNum;
                    $this.toggleClass("voted");
                    voteNum = parseInt($this.html()) - 1;
                    $this.attr("title", "赞一个");
                    $this.html(voteNum);
                    if($this.siblings(".answer-head").find(".voteCount")){
                        $this.siblings(".answer-head").find(".voteCount").html(voteNum);
                    }
                }
            });
        }
    });

    // 对评论的点赞、回复*************************************************************
    $(document).on("click", ".commentItem-action", function(event){
        event.preventDefault();
        var $comment = $(this).parents(".commentItem");
        var commentId = $comment.attr("data-commentId");
        console.log("commentId: " + commentId);

        $(this).toggleClass("on");
        var $this = $(this);

        if($(this).hasClass("action-like")){
            if($this.hasClass("on")) {
                $.ajax({
                    type: "POST",
                    url: "/supports/support",
                    contentType: "application/json",
                    data: JSON.stringify({
                        userId: userId,
                        commentId: commentId
                    }),
                    dataType: "json",
                    success: function (data) {
                        var $numLikes = $this.siblings(".commentItem-likes").find("span").eq(0);
                        var newNumLikes = parseInt($numLikes.html()) + 1;
                        $numLikes.html(newNumLikes);
                    }
                });
            }else {
                $.ajax({
                    type: "POST",
                    url: "/supports/support/remove",
                    data: {
                        userId: userId,
                        commentId: commentId
                    },
                    dataType: "json",
                    success: function (data) {
                        var $numLikes = $this.siblings(".commentItem-likes").find("span").eq(0);
                        var newNumLikes = parseInt($numLikes.html()) - 1;
                        $numLikes.html(newNumLikes);
                    }
                });
            }
        }else if($(this).hasClass("action-reply")){
            var writeReply = '<div class="commentReply-expanded"><div class="commentReply-input"><textarea name="comment-reply" placeholder="写下你的评论" class="form-control"></textarea></div><div class="commentReply-actions"><button type="button" class="commentReply-submitButton btn btn-sm btn-primary">评论</button><button type="button" class="commentReply-cancelButton commentBox-textButton">取消</button></div></div>';
            var $writeReply = $(writeReply);
            if($(this).hasClass("on")){
                $(this).parents(".commentItem").append($writeReply);
            }else{
                $(this).parents(".commentItem").find(".commentReply-expanded").remove();
            }
        }
    });
    // 取消回复或确认回复
    $(document).on("click", ".commentReply-actions button", function(){
        if($(this).hasClass("commentReply-cancelButton")){
            $(this).parents(".commentItem").find(".action-reply").click();
        }else if($(this).hasClass("commentReply-submitButton")){
            var $this = $(this);
            // 回复评论后，添加到评论区
            // 取得原评论的信息
            var $item = $(this).parents(".commentItem"),
                commentId = $item.attr("data-commentId"),
                commentThread = $item.attr("data-commentThread");
			var $answerItem,
                questionId;

            if($item.parents(".feed-item").html()) {
                $answerItem = $(this).parents(".feed-item");
                questionId = $answerItem.find(".question-link").attr("id").substr(4);
            }else if($(this).parents(".List-item").html()) {
                $answerItem = $(this).parents(".List-item");
                questionId = $answerItem.find(".question-link").attr("id").substr(4);
            }else {
                $answerItem = $(this).parents(".answer-item");
                var currentPath = window.location.pathname;   //    /questions/question/{id}
                questionId = currentPath.substr(20);
            }

            var $author = $item.find(".commentItem-header").find(".author-link").eq(0),
                authorId = $author.attr("data-authorId"),
                authorName = $author.html();
            var $input = $item.find(".commentReply-input textarea").eq(0);
            if(checkEmpty($input)){
                alert("请输入你的回复！");
                return false;
            }else if(checkOver($input, 200)) {
                alert("回复不得超过200字！");
                return false;
            }

            // 回复评论******************************************************
            $.ajax({
                type: "POST",
                url: "/comments/comment",
                contentType: "application/json",
                data: JSON.stringify({
                    parentId: commentId,
                    question: {
                        id: questionId
                    },
                    authorId: userId,
                    authorName: $("#user_name").val(),
                    replyId: authorId,
                    replyName: authorName,
                    thread: commentThread + commentId + "/",
                    summary: $input.val()
                }),
                dataType: "json",
                success: function (data) {
                    var content = data.content;

                    // 输出格式：01-23 18:55
                    var date = new Date(content.time);
                    var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-',
                        D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ',
                        h = date.getHours() + ':',
                        m = date.getMinutes();
                    var time = M+D+h+m;

                    var newComment = '<div class="commentItem" data-commentId="'
                        + content.id + '"><div class="commentItem-header"><a href="/users/u/'
                        + content.authorId + '" class="author-link" data-authorId="'
                        + content.authorId + '">'
                        + content.authorName + '</a> 回复 <a href="/users/u/'
                        + content.replyId + '" class="author-link">'
                        + content.replyName + '</a><button type="button" class="btn commentBox-textButton delete-button">删除</button></div><div class="commentItem-content">'
                        + content.summary + '</div><div class="commentItem-footer"><span class="commentItem-likes"><span>'
                        + 0 + '</span> <span>赞</span></span><time title="'
                        + time + '">'
                        + time + '</time><a class="commentItem-action action-reply unselectable"><span class="glyphicon glyphicon-comment"></span><span>回复</span></a><a class="commentItem-action action-like unselectable"><span class="glyphicon glyphicon-thumbs-up"></span><span>赞</span></a></div></div>';


                    var $newComment = $(newComment);
                    $newComment.insertBefore($answerItem.find(".commentItem").eq(0));

                    // 更新总评论数
                    var newNum = parseInt($answerItem.find(".comment-num").html()) + 1;
                    $answerItem.find(".comment-num").html(newNum);
                    // 模拟“取消”被点击
                    $this.siblings(".commentReply-cancelButton").click();
                }
            });
        }
    });
    // 提交评论
    $(document).on("click", ".comment-box-actions .comment-box-submitButton", function(){
        var $input = $(this).parent().prev().find("textarea").eq(0);
        // var $item = $(this).parents(".feed-item") == "undefined" ? $(this).parents(".feed-item") : $(this).parents(".List-item");
        if($(this).parents(".feed-item").html()) {
            var $item = $(this).parents(".feed-item");
        }else if($(this).parents(".List-item").html()) {
            var $item = $(this).parents(".List-item");
        }else {
            var $item = $(this).parents(".answer-item");
        }
        var answerId = $item.attr("id").substr(4),
            answerThread = $item.attr("data-answerThread");
        var $authorLink = $item.find(".author-link"),
            authorId = $authorLink.attr("data-authorId"),
            authorName = $authorLink.html();

        var $questionLink = $item.find(".question-link");
        if($questionLink.html()) {
            questionId = $questionLink.attr("id").substr(4);
        }else {
            var currentPath = window.location.pathname;
            var questionId = currentPath.substr(20);
        }



        var $this = $(this);

        console.log(answerId);
        console.log(questionId);
        if(checkEmpty($input)){
            alert("请输入你的评论！");
            return false;
        }else if(checkOver($input, 200)) {
            alert("评论不得超过200字！");
            return false;
        }
        // 提交评论*******************************************************
        $.ajax({
            type: "POST",
            url: "/comments/comment",
            contentType: "application/json",
            data: JSON.stringify({
                parentId: answerId,
                question: {
                    id: questionId
                },
                authorId: userId,
                authorName: $("#user_name").val(),
                replyId: authorId,
                replyName: authorName,
                thread: answerThread + answerId + "/",
                summary: $input.val()
            }),
            dataType: "json",
            success: function (data) {
                var content = data.content;
                console.log("提交评论后：" + content);

                // 输出格式：01-23 18:55
                var date = new Date(content.time);
                var M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-',
                    D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ',
                    h = date.getHours() + ':',
                    m = date.getMinutes();
                var time = M+D+h+m;

                var newComment = '<div class="commentItem" data-commentId="'
                    + content.id + '"><div class="commentItem-header"><a href="/users/u/'
                    + content.authorId + '" class="author-link" data-authorId="'
                    + content.authorId + '">'
                    + content.authorName + '</a> 回复 <a href="/users/u/'
                    + content.replyId + '" class="author-link">'
                    + content.replyName + '</a><button type="button" class="btn commentBox-textButton delete-button">删除</button></div><div class="commentItem-content">'
                    + content.summary + '</div><div class="commentItem-footer"><span class="commentItem-likes"><span>'
                    + 0 + '</span> <span>赞</span></span><time title="'
                    + time + '">'
                    + time + '</time><a class="commentItem-action action-reply unselectable"><span class="glyphicon glyphicon-comment"></span><span>回复</span></a><a class="commentItem-action action-like unselectable"><span class="glyphicon glyphicon-thumbs-up"></span><span>赞</span></a></div></div>';


                var $newComment = $(newComment);
                // 更新总评论数
                var $ansActions = $this.parents(".answer-actions");
                var oldNum = parseInt($ansActions.find(".comment-num").html());
                $ansActions.find(".comment-num").html(oldNum + 1);
                // 插入DOM
                if(oldNum == 0){
                    $ansActions.find(".comment-box").append($newComment);
                    // $ansActions.find(".load-more").removeClass("hide");
                }else{
                    $newComment.insertBefore($ansActions.find(".comment-box .commentItem").eq(0));
                }
                // 清空输入框
                $input.val("");
            }
        });
    });
    // 删除回复或评论
    $(document).on("click", ".commentItem .delete-button", function(){
        var $commentItem = $(this).parents(".commentItem");
        var commentId = $commentItem.attr("data-commentId");

        // 更新总评论数
        var $ansActions = $(this).parents(".answer-actions");
        var oldNum = parseInt($ansActions.find(".comment-num").html());

        $.ajax({
            type: "POST",
            url: "/comments/comment/" + commentId + "/remove",
            data: {
                uid: $("#user_id").val()
            },
            success: function (data) {
                if(data) {
                    $ansActions.find(".comment-num").html(oldNum - data);
                    $commentItem.remove();
                    alert("删除成功");
                }else {
                    alert("删除失败");
                }
            }
        });
    });



    // 提问，先清除模态框中输入框的内容
    $(document).on("click", ".SearchBar-askButton", function(){
        $("#askModal textarea").val("");
    });




    // 搜索框不为空
    $("#searchBtn").click(function () {
        if(checkEmpty($(this).prev().find("input"))) {
            alert("请输入搜索关键字");
            return false;
        }else {
            var v = $.trim($(this).prev().find("input").val());
            $(this).prev().find("input").val(v);
        }
    });
});



// 为Date时间设置前导零
function setPreZero(value){
    var valString = value + "";
    var length = valString.length;
    if(value == 0 || valString == "00"){
        return "00";
    }else if(length == 1){
        return "0" + value;
    }else{
        return value;
    }
}
// 检查是否为空
function checkEmpty(target) {
    var value = target.val().replace(/\s+/g,"");  /*消除字符串所有空格*/
    if(value == ""){
        return true;
    }else{
        return false;
    }
}
// 检查是否超出200个字
function checkOver(target, number) {
    var value = target.val();
    if(value.length > number) {
        return true;
    }else {
        return false;
    }
}