
function loginAuthentication() {
    var loginName = $("#loginName").val();
    var loginPwd = $("#loginPwd").val();

    if(loginName.trim()==="" || loginPwd.trim()===""){
        $("#errorMsg").attr("style","display:block;");
        return;
    }

    loginName = loginName.trim();
    loginPwd = loginPwd.trim();

    loginPost(loginName,loginPwd);
}

function loginPost(loginName,loginPwd) {
    alert(loginName + "," + loginPwd)
    $.ajax({
        url:'/api/1.0/login',
        type:'POST',
        dataType:'json',
        data:{
            userName:loginName,
            userPwd:loginPwd
        },
        timeout:120000,
        success:function (data) {
            if(data.result === 'success'){
                alert("成功");
            }else{
                alert("失败");
            }

        },
        error:function (data) {
            alert(data.result);
        }
    });
}