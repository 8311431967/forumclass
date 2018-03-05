/**
 * Created by Administrator on 2017/1/1.
 */

$(document).ready(function(){
    var userId = $("#user_id").val();
    var currentSearch = window.location.search;  //   ?key=...
    var key = currentSearch.substr(5);  // 搜索关键字
    // key = decodeURI(key);
    key = decodeURIComponent(key);
    key = key.replace(/\+/g, ' ');

    console.log("key: " + key);

    $(".SearchBar-input input").val(key);

    if($("#Profile-questions").html()) {
        console.log("questions");
        $.ajax({
            type: "POST",
            url: "/searchQResult",
            data: {
                key: key
            },
            dataType: "json",
            success: function(data){
                console.log("count: " + data.count);
                if(data.count == 0){
                    $("#none-result-question").removeClass("hide");
                    return;
                }
                $.each(data.content, function(){
                    var str = '<div class="ask-item List-item" id="que-'
                        + this.id + '">';
                    if(this.authorId == userId) {
                        str += '<button class="delete del-ask commentBox-textButton" data-toggle="modal" data-target="#deleteModal">删除</button>';
                    }
                    str = str + '<h2 class="feed-title ask-title"><a href="/questions/question/'
                        + this.id + '" class="question-link" target="_blank">'
                        + this.title + '</a></h2><span class="author-link-line"><a href="/users/u/'
                        + this.authorId + '" class="author-link">'
                        + this.authorName + '</a></span><div class="feed-content">';
                    if(this.type) {
                        var s = this.summary.replace(/<br\/>/g, ' ');
                        str = str + '<div class="feed-summary">'
                            + s + '<span class="expand">显示全部</span></div><div class="feed-summary-whole clearfix hide"></div>';
                    }else {
                        str = str + '<div class="feed-summary">'
                            + this.summary + '</div>';
                    }
                    str = str + '</div></div>';

                    $("#Profile-questions .List-content").append(str);
                });

                // 全部加载完成后，标红关键字
                $(".question-link").each(function () {
                    var questionName = $(this).html().split('');
                    var queNLength = questionName.length;
                    var keyLength = key.length;
                    for(var i = 0; i < queNLength;i ++) {
                        var str = '';
                        for(var j = 0; j < keyLength; j ++) {
                            str += questionName[i + j];
                        }
                        if(str == key) {
                            for(var j = 0; j < keyLength; j ++) {
                                questionName[i + j] = '<span class="keyWord">' + questionName[i + j] + '</span>';
                            }
                        }
                    }
                    questionName = questionName.join('');
                    $(this).html(questionName);
                });
            }
        });
    }else {
        console.log("users");
        $.ajax({
            type: "POST",
            url: "/searchUResult",
            data: {
                key: key
            },
            dataType: "json",
            success: function (data) {
                console.log("count: " + data.count);
                if(data.count == 0) {
                    $("#none-result-user").removeClass("hide");
                    return;
                }
                $.each(data.content, function(){
                    var str = '<div class="List-item"><span class="author-link-line"><a href="/users/u/'
                            + this.id + '" class="author-link">'
                            + this.userName + '</a></span><div class="author-info"><span class="authorInfo-ans"><span>'
                            + this.numOfAnswer + '</span> 回答</span><span class="authorInfo-ask"><span>'
                            + this.numOfQuestion + '</span> 提问</span></div></div>';

                    $("#Profile-users .List-content").append(str);
                });

                // 全部加载完成后，标红关键字
                $(".author-link").each(function () {
                    var userName = $(this).html().split('');
                    console.log(userName);
                    var userNLength = userName.length;
                    var keyLength = key.length;
                    for(var i = 0; i < userNLength;i ++) {
                        var str = '';
                        for(var j = 0; j < keyLength; j ++) {
                            str += userName[i + j];
                        }
                        if(str == key) {
                            for(var j = 0; j < keyLength; j ++) {
                                userName[i + j] = '<span class="keyWord">' + userName[i + j] + '</span>';
                            }
                        }
                    }
                    console.log(userName);
                    userName = userName.join('');
                    $(this).html(userName);
                });
            }
        });
    }

    // 跳转到“用户”
    $("#searchUser").click(function () {
        // 重新获取key
        var $input = $(".SearchBar-input input");
        if(checkEmpty($input)) {
            alert("请输入搜索关键字");
            return false;
        }
        key = $.trim($input.val());
        window.location.href = "/searchUser?key=" + encodeURIComponent(key);
    });
    // 跳转到“问题”
    $("#searchQue").click(function () {
        // 重新获取key
        var $input = $(".SearchBar-input input");
        if(checkEmpty($input)) {
            alert("请输入搜索关键字");
            return false;
        }
        key = $.trim($input.val());
        window.location.href = "/searchQuestion?key=" + encodeURIComponent(key);
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

