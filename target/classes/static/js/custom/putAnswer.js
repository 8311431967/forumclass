/**
 * Created by Administrator on 2017/1/7.
 */
$(function () {
    var currentPath = window.location.pathname;   //    /questions/question/{id}
    var questionId = currentPath.substr(20);

    var userId = $("#user_id").val();
    var userName = $("#user_name").val();

    var $popModal = $("#popModal");

    // 回答问题，先清除模态框中输入框的内容
    $(document).on("click", ".btn-answer", function(){
        $("#answerModal textarea").val("");
    });

    // 提交回答
    $(document).on("click", ".btn-submit-answer", function(){
        var $textarea = $(this).parents(".modal-content").find("textarea").eq(0);
        var answerContent = $textarea.val();
        var $this = $(this);
        if(checkEmpty($textarea)){
            alert("请输入你的回答！");
            return false;
        }

        $.ajax({
            type: "POST",
            url: "/comments/comment",
            contentType: "application/json",
            data: JSON.stringify({
                question: {
                    id: questionId
                },
                authorId: userId,
                authorName: userName,
                summary: answerContent
            }),
            dataType: "json",
            success: function (data) {
                // 关闭模态框
                $this.parents(".modal-content").find(".close").click();
                $popModal.find(".modal-body").html("回答成功");
                $popModal.modal("show");

                // var date = new Date();
                // var Y = date.getFullYear() + '-',
                //     M = (date.getMonth()+1 < 10 ? '0'+(date.getMonth()+1) : date.getMonth()+1) + '-',
                //     D = (date.getDate() < 10 ? '0' + date.getDate() : date.getDate()) + ' ';
                // var time = Y+M+D;
                //
                // var content = data.content;
                //
                // var str = '<div class="answer-item" id="ans-'
                //     + content.id + '" data-answerThread="'
                //     + content.thread + '">';
                // if(content.authorId == userId) {
                //     str += '<button class="delete del-answers commentBox-textButton" data-toggle="modal" data-target="#deleteModal">删除</button>';
                // }
                // str = str + '<div class="answer-head "><span class="author-link-line"><a href="/users/u/'
                //     + content.authorId + '" class="author-link" data-authorId="'
                //     + content.authorId + '">'
                //     + content.authorName + '</a></span><br /><span class="voters"><span class="voteCount">'
                //     + '0</span>&nbsp;人赞同 </span></div><div class="feed-vote';
                //
                // str += '" title="赞一个">';
                //
                //
                // str = str + '0 </div><div class="answer-text">';
                // if(this.type) {
                //     var s = this.summary.replace(/<br\/>/g, ' ');
                //     str = str + '<div class="feed-summary">'
                //         + s + '<span class="expand">显示全部</span></div><div class="feed-summary-whole clearfix hide"></div>';
                // }else {
                //     str = str + '<div class="feed-summary">'
                //         + this.summary + '</div>';
                // }
                // str = str + '</div><div class="answer-actions"><div class="meta-panel"><span class="meta-item answer-date">编辑于'
                //     + time + '</span><a href="#" class="meta-item toggle-comment"><span class="glyphicon glyphicon-comment"></span><span class="comment-num">'
                //     + '0</span>条评论</a></div><div class="comment-holder hide"><div class="comment-wrapper"><i class="icon icon-spike"></i><div class="comment-box"></div><div class="comment-box-expanded"><div class="comment-box-input">'
                //     + '<textarea name="comment-publish" placeholder="写下你的评论" class="form-control"></textarea></div><div class="comment-box-actions clearfix">'
                //     + '<button type="button" class="comment-box-submitButton btn btn-sm btn-primary">评论</button></div></div>';
                //
                // // 插入文档流
                // if($("#answers-filter").find("li a").html() == "按时间排序") {  // 现在为“按赞数排序”
                //     $(".answers-wrap").append(str);
                // }else {  // 现在为“按时间排序”
                //     var $newAnswer = $(str);
                //     if($(".answers-wrap").find(".answer-item").eq(0).html()) {
                //         $newAnswer.insertBefore($(".answers-wrap").find(".answer-item").eq(0));
                //     }
                //     $(".answers-wrap").append(str);
                // }
            }
        });
    });

    $popModal.find(".modal-footer button").click(function () {
        $popModal.find(".close").click();
    });
});


// 检查是否为空
function checkEmpty(target) {
    var value = target.val().replace(/\s+/g,"");  /*消除字符串所有空格*/
    if(value == ""){
        return true;
    }else{
        return false;
    }
}
