$(function() {
    var $ansNum,
		$ansTab,
		$askTab;
	// 删除 “回答”
	$(document).on("click", ".del-answers", function() {
		var delModal = $("#deleteModal");
		var thisItem;
		if($(this).parents(".feed-item").html()) {
			thisItem = $(this).parents(".feed-item");
		}else {
			thisItem = $(this).parents(".answer-item");
			if(thisItem.hasClass("List-item")) {
				$ansTab = $(".Tabs-meta").eq(0);
			}else {
                $ansNum = $("#answers-num");
			}
		}

		var titleOfQue = thisItem.find(".feed-title").find(".question-link").html();

		if(!titleOfQue) {
			titleOfQue = $(".question-link").html();
		}
		// 设置模态框的 data-delId 属性为删除项的 id，方便后续删除
		delModal.attr("data-delId", thisItem.attr("id"));
		delModal.find(".modal-body").html('确定要删除你对问题 “<strong>' + titleOfQue + '</strong>” 的回答吗？');
	});
	// 删除“提问”
	$(document).on("click", ".del-ask", function() {
		var delModal = $("#deleteModal");
		var thisItem;
		if($(this).parents(".feed-item").html()) {
            thisItem = $(this).parents(".feed-item");
		}else {
			thisItem = $(this).parents(".ask-item");
            $askTab = $(".Tabs-meta").eq(1);
		}

		console.log(thisItem.html());

		var titleOfQue = thisItem.find(".question-link").html();

		console.log(titleOfQue);

		delModal.attr("data-delId", thisItem.attr("id"));
		delModal.find(".modal-body").html('确定要删除你提出的问题 “<strong>' + titleOfQue + '</strong>” 吗？');
	});


	// 确认删除
	$(document).on("click", ".confirm-del", function() {
		var delModal = $(this).parents(".modal");
		var delId = delModal.attr("data-delId");     //  ans-{id}或que-{id}
        // 删除的是“提问”还是“回答”，做不同处理，url不同
        var delType = delId.substr(0, 3),
			delItem = $("#" + delId),
			delIdNumber = delId.substr(4);

		if(delType == "ans") {
			$.ajax({
	        	type: "POST",
	        	url: "/comments/comment/" + delIdNumber + '/remove',
	        	data:{
        			uid: $("#user_id").val()
        		},
	        	success: function(data){
	        		console.log("返回值：" + data);
	        		if(data){
                        delModal.find(".close").click();
						delItem.remove();
                        alert("删除成功");
                        if($ansNum) {
                        	var oldNum = $ansNum.html().slice(0, -3);
                        	$ansNum.html(oldNum - 1 + "个回答");
						}
						if($ansTab) {
                        	var oldNum = $ansTab.html();
                        	$ansTab.html(oldNum - 1);
						}
	        		}else{
	        			alert("删除失败");
	        		}
	        	}
	        });
		}else {
			$.ajax({
	        	type: "POST",
	        	url: "/questions/question/" + delIdNumber + "/remove",
	        	data:{
        			uid: $("#user_id").val()
        		},
	        	success: function(data){
	        		if(data == "success"){
                        delModal.find(".close").click();
						delItem.remove();
                        alert("删除成功");
                        if($askTab) {
                            var oldNum = $askTab.html();
                            $askTab.html(oldNum - 1);
						}
	        		}else{
	        			alert("删除失败");
	        		}
	        	}
	        });
		}
	});
});