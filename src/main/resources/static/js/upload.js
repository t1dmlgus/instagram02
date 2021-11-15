function upload(userId, evt){

    evt.preventDefault();
    console.log(userId);


    var formData = new FormData();
    formData.append("userId", userId);
    formData.append("caption", $('#caption').val());
    formData.append("file", $('#imageFile')[0].files[0]);

    for(let value of formData.values()){
        console.log(value);
    }

    $.ajax({
        type:"post",
        url:`/api/image/save`,
        processData: false,
        contentType: false,
        data: formData,
        dataType:'json'

    }).done(res=>{

        console.log(res);
        alert(res.message);
        console.log("upload 성2공");
        location.href=`/user/${userId}`;

    }).fail(error=>{
        console.log(error);
        alert(error.responseJSON.message);

    });

}


// (1) 스토리 이미지 업로드를 위한 사진 선택 로직
function imageChoose(obj) {
	let f = obj.files[0];
    console.log(f);

	if (!f.type.match("image.*")) {
		alert("이미지만 업로드 할 수 있습니다.");
		return;
	}

	let reader = new FileReader();
	reader.onload = (e) => {
		$("#imageUploadPreview").attr("src", e.target.result);
	}
	reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.
}