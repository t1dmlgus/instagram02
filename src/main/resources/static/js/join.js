
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

// (1) 회원가입
function join(evt) {

    evt.preventDefault();

    //let data = $("#profileUpdate").serialize();
    //console.log(data);

    let data2 = $("#profileUpdate").serializeObject();
    console.log(data2);

    $.ajax({
        type:"post",
        url:`/api/auth/signup`,
        data: JSON.stringify(data2),
        contentType:'application/json; charset=utf-8',
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