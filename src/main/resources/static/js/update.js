jQuery.fn.serializeObject = function() {
  var obj = null;
  try {
      if(this[0].tagName && this[0].tagName.toUpperCase() == "FORM" ) {
          var arr = this.serializeArray();
          if(arr){ obj = {};
          jQuery.each(arr, function() {
              obj[this.name] = this.value; });
          }
      }
  }catch(e) {
      alert(e.message);
  }finally {}
  return obj;
}




// (1) 회원정보 수정
function update(userId, evt) {

    evt.preventDefault();

    let data = $("#profileUpdate").serializeObject();
    console.log(data);

    var json_str = JSON.stringify(data);
    console.log(json_str);


    $.ajax({
        type:"put",
        url:`/api/user/update/${userId}`,
        data: JSON.stringify(data),
        contentType:'application/json; charset=utf-8',
        dataType:'json'
    }).done(res=>{

        console.log(res);
        alert(res.message)
        console.log("update 성공");
        location.href=`/user/${userId}`;

    }).fail(error=>{

        console.log(error.responseJSON);
        if(error.data == null){
            alert(error.responseJSON.message);
        }else{
            alert(JSON.stringify(error.responseJSON.data));
        }
    });

}