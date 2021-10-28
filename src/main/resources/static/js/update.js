// (1) 회원정보 수정
function update(userId, evt) {

    evt.preventDefault();

    let data = $("#profileUpdate").serialize();
    console.log(data);


    $.ajax({
        type:"put",
        url:`/api/user/${userId}/update`,
        data: data,
        contentType:'application/x-www-form-urlencoded; charset=utf-8',
        dataType:'json'
    }).done(res=>{

        console.log(res);
        alert(res.message)
        console.log("update 성공");
        location.href=`/user/${userId}`;
    }).fail(error=>{
        console.log(error.responseJSON);
        alert(JSON.stringify(error.responseJSON.data));
        //console.log(error);

    });

}