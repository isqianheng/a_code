$(function() {
    //版本号文本的宽度自变化
    var value = "V1.1.0";
    //添加版本号  后期可以动态获取此参数value
    $(".version").text(value);
    $(".version").width(function () {
        var content = "<pre id=\"p1\" style=\"dispaly:none\">" + value + "</pre>";   //给<pre>设置不可见样式
        $("body").append(content);
        var width = $("#p1").width();
        $("#p1").remove();
        return width;
    });  //宽度自变化处理
      var userAgent = navigator.userAgent; //取得浏览器的userAgent字符串
      IETester(userAgent);
});
//校验函数
function login() {
    /* 接入统一认证，本地登录时，只校验空
    if ($("#InputUserName").val().length < 1 || $("#InputUserName").val().length > 20) {
        mainW(20);
        $('.textMsg').empty().append('用户名为字母、数字和_组成，长度为1至20位');
        $('#login-msg').show();
        return false;
    }
    if ($("#InputPassword").val().length < 6 || $("#InputPassword").val().length > 32) {
        mainW(20);
        $('.textMsg').empty().append('密码长度为6至32位');
        $('#login-msg').show();
        return false;
    }*/
    if ($("#InputUserName").val()== null || $("#InputUserName").val().trim().length < 1) {
        mainW(20);
        $('.textMsg').empty().append('用户名不能为空！');
        $('#login-msg').show();
        return false;
    }
    if ($("#InputPassword").val().length == null || $("#InputPassword").val().trim().length < 1) {
        mainW(20);
        $('.textMsg').empty().append('密码不能为空！');
        $('#login-msg').show();
        return false;
    }
    $.ajax({
        type: "POST",
        url: contextPath + "/getLogin",
        async: "false",
        data: {
            userId: $("#InputUserName").val(),
            password: $("#InputPassword").val()
        },
        dataType: "json",
        success: function (json) {
            var errMsg = json.errorMsg;
            if (errMsg !== "000000") {
            /*统一认证不做弱密码校验
                 if(errMsg=="密码存在超过90天必须修改密码"){
                    alert("密码存在超过90天必须修改密码");
                     location.href = contextPath + "/updlogin.jsp?name="+$("#InputUserName").val();
                 }else if(errMsg=="首次登陆必须修改密码"){
                    alert("首次登陆必须修改密码");
                    location.href = contextPath + "/updlogin.jsp?name="+$("#InputUserName").val();
                 }else if(errMsg=="系统升级，请修改密码"){
                    alert("系统升级，请修改密码");
                    location.href = contextPath + "/updlogin.jsp?name="+$("#InputUserName").val();
                 }
                 else{
                 */
                mainW(20);
                $('.textMsg').empty().append(errMsg);
                $('#login-msg').show();
//                }
            } else {
                location.href = contextPath + "/index.jsp";
            }
        }
    });
}

//提醒摇晃函数
function mainW(n) {
    $('#login-main').stop().css({'margin-left': -180});
    if (n > 0)
        $('#login-msg').animate({'margin-left': -180 - n}, 60).animate({'margin-left': -180 + n}, 120, function () {
        mainW(n - 5);
    });
    else
        $('#login-msg').animate({'margin-left': -180}, 50);
}

function keyLogin(event) {
    var code = event.keyCode;
    if (code === 13) {
        login();
    }
}
//校验浏览器的版本
    function IETester(userAgent){
        var UA =  userAgent || navigator.userAgent;
        if(/msie/i.test(UA)){
            $("#InputUserName").attr("disabled","true");
            $("#InputPassword").attr("disabled","true");
            $("#login-btn").attr("disabled","true");
            alert('IE浏览器版本过低，请升级后使用');
            return UA.match(/msie (\d+\.\d+)/i)[1];
        }else if(UA.toLowerCase().indexOf('trident') && UA.indexOf('rv')){
            return UA.match(/rv:(\d+\.\d+)/)[1];
        }
        return false;
    }