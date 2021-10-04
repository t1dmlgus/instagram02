// (1) 회원가입
function join(evt) {

    evt.preventDefault();
    let data = $("#profileUpdate").serialize();

    console.log(data);

    $.ajax({
        type:"post",
        url:`/api/auth/signup`,
        data: data,
        contentType:'application/x-www-form-urlencoded; charset=utf-8',
        dataType:'json'
    }).done(res=>{
        console.log(res);
        alert(res.message);
        location.href=`/`;
    }).fail(error=>{
        console.log(error.responseJSON);

        if(error.responseJSON.data != null){
            alert(JSON.stringify(error.responseJSON.data));
        }else{
            alert(error.responseJSON.message);
        }

    });

}