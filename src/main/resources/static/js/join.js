
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

    let data = $("#profileUpdate").serializeObject();
    console.log(data);

    $.ajax({
        type:"POST",
        url:"/api/user/join",
        data: JSON.stringify(data),
        contentType:"application/json; charset=utf-8",
        dataType:"json"


    }).done(res=>{
        console.log(res);
        alert(res.message);
        location.href=`/`;
    }).fail(error=>{
        console.log(error);
        alert(JSON.stringify(error.responseJSON.data));

        if(error.responseJSON.data != null){
        }else{
            alert(error.responseJSON.message);
        }

    });

}