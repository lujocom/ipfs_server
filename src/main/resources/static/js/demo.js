$(function () {
    //上传图片事件
    $('.fileupload-upload').bind('click', function () {
        var pass=$('.fileupload-pass').val();
        if(pass==''||pass==null){
            $('.passError').show();
            $('.passErrorSpan').text('密码不能为空');
            return
        }else{
            PostFile(document.getElementById("inputFile").files[0],0);
         /*
         var formData = new FormData();
            console.log(document.getElementById("inputFile").files[0]);
            formData.append("myfile", document.getElementById("inputFile").files[0]);
            formData.append("pass", pass);
            console.log(formData);
            $.ajax({
                url: "testuploadimg",
                type: "POST",
                data: formData,
                contentType: false,
                processData: false,
                success: function (data) {
                    console.log('909090909');
                    console.log(data);
                    var orangeData=$('.showHash').html();
                    console.log(orangeData);
                    if(orangeData==''){
                        $('.showHash').html(data);
                    }else{
                        if(data!=orangeData){
                            $('.showHash').html(orangeData+","+data);
                        }
                    }
                },
                error: function () {
                }
            });//*/
        }
       function PostFile(file,i){
            var name = file.name,        //文件名
                size = file.size,        //总大小shardSize = 2 * 1024 * 1024,
                shardSize = 20 * 1024 * 1024,//以2MB为一个分片
                shardCount = Math.ceil(size / shardSize);  //总片数
            if(i >= shardCount){
                return;
            }
            //计算每一片的起始与结束位置
            var start = i * shardSize,
                end = Math.min(size, start + shardSize);
            //构造一个表单，FormData是HTML5新增的
            var form = new FormData();
            form.append("myfile", file.slice(start,end));  //slice方法用于切出文件的一部分
           /* form.append("lastModified", file.lastModified);  //slice方法用于切出文件的一部分*/
            form.append("name", name);
            form.append("total", shardCount);  //总片数
            form.append("index", i + 1);        //当前是第几片
            form.append("pass", pass);
            //Ajax提交
            $.ajax({
                url: "testuploadimg",
                type: "POST",
                data: form,
                async: true,        //异步
                processData: false,  //很重要，告诉jquery不要对form进行处理
                contentType: false,  //很重要，指定为false才能形成正确的Content-Type
                success: function(data){
                    if(data=="continue"){
                        i++;
                        console.log(i);
                        var num = Math.ceil(i*100 / shardCount);
                        $("#output").text(num+'%');
                        PostFile(file,i);
                    }
                    console.log(data);
                    console.log(i);
                    var orangeData=$('.showHash').html();
                    console.log(orangeData);
                    if(data == "continue")
                    {
                        ;
                    }
                    else if(orangeData=='')
                    {
                        $("#output").text('上传完成');
                        $('.showHash').html(data);
                    }else{
                        $("#output").text('上传完成');
                        if(data!=orangeData){
                            $('.showHash').html(orangeData+","+data);
                        }
                    }
                }
            });
        }


    });
    //点击获取hash事件
    $('.read-hash-btn').bind('click', function () {
        var _hashVal = $('#inputHash').val();
        var password = $('#password').val();

        if (_hashVal == '' || _hashVal == null) {
            console.log(3223);
            $('.hashError').show();
            $('.errorSpan').text('hash值不能为空');
            return
        }
        if (password == '' || password == null) {
            console.log(1111);
            $('.hashError').show();
            $('.errorSpan').text('密码不能为空');
            return
        } else {
            $('#typeBtn').text('正在下载');
            $('#typeBtn').attr('disabled','disabled');
            $('.hashError').hide();

            //成功后跳转地址
            // window.open('http://127.0.0.1:8087/dicomviewer?urls=["http://127.0.0.1:8087/3162a35b8b7446afe6da40d69e1f2305"]', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");

            $.ajax({
                url: "showImg/" + _hashVal + "/" + password,
                success: function (result) {
                    console.log(result.data);
                    $('#typeBtn').attr('disabled',false);
                    $('#typeBtn').text('确定');
                    console.log('http://127.0.0.1:8087/dicomviewer?'+result.data+'', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
                    window.open('http://127.0.0.1:8087/dicomviewer?'+result.data+'', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
                    //window.open('http://127.0.0.1:8087/dicomviewer?'+result.data+'', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
                    //console.log('http://127.0.0.1:8087/dicomviewer?urls=' + result.data + '', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
                    //window.open('http://127.0.0.1:8087/dicomviewer?urls=' + result.data + '', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
                    //window.open('http://127.0.0.1:8087/dicomviewer?urls=["http://127.0.0.1:8087/1519788792143"]', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
                },
            });
//                window.open('http://127.0.0.1:8087/dicomviewer?urls=["http://127.0.0.1:8087/1519788792143"]', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
        }

    });
    $('.uploadUrl').bind('click',function(){
        var fileUrl = $('.fileUrl').val();
        var pass=$('.fileUrlPass').val();
        if(fileUrl==''||fileUrl==null){
            $('.urlError').show();
            $('.urlErrorSpan').text('目录名不能为空');
            return
        }
        if(pass==''||pass==null){
            $('.urlError').show();
            $('.urlErrorSpan').text('密码不能为空');
            return
        }else{
            $('.uploadUrl').text('正在上传');
            $('.uploadUrl').attr('disabled','disabled');
            $('.urlError').hide();
        }
        console.log(fileUrl,pass);
        var form = new FormData();
        form.append('fileUrl',fileUrl);
        form.append('pass',pass);
        $.ajax({
            url: "testdiruploadimg",//接口
            type: "POST",
            data:form,
            // data: {//参数
            //     // fileUrl:fileUrl,
            //     // pass:pass
            //     "fileUrl="+fileUrl+"&pass="+pass;
            // },
            async: true,        //异步
            processData: false,  //很重要，告诉jquery不要对form进行处理
            contentType: false,  //很重要，指定为false才能形成正确的Content-Type
            success: function(data){
                console.log('90909090');
                $('.uploadUrl').attr('disabled',false);
                $('.uploadUrl').text('上传');

                console.log(data);
                var orangeData=$('.showHash').html();
                console.log(orangeData);
                if(data == "continue")
                {
                    ;
                }
                else if(orangeData=='')
                {
                    $("#output").text('上传完成');
                    $('.showHash').html(data);
                }else{
                    $("#output").text('上传完成');
                    if(data!=orangeData){
                        $('.showHash').html(orangeData+","+data);
                    }
                }

            }
        });

    });

    //点击获取hash事件
    $('#dirBtn').bind('click', function () {
        var _hashVal = $('#dirHashInput').val();
        var password = $('#dirPassInput').val();
        var _outVal = $('#dirOutInput').val();
        if (_hashVal == '' || _hashVal == null) {
            $('.dirHashError').show();
            $('.dirErrorSpan').text('hash值不能为空');
            return
        }
        if (password == '' || password == null) {
            console.log(1111);
            $('.dirHashError').show();
            $('.dirErrorSpan').text('密码不能为空');
            return
        }
        if (_outVal == '' || _outVal == null) {
            $('.dirHashError').show();
            $('.dirErrorSpan').text('输出目录不能为空');
            return
        }
        else {
            $('#dirBtn').text('正在下载');
            $('#dirBtn').attr('disabled','disabled');
            $('.dirHashError').hide();
            var form = new FormData();
            form.append("_hashVal", _hashVal);
            form.append("password", password);  //总片数
            form.append("_outVal", _outVal);
            //成功后跳转地址
            // window.open('http://127.0.0.1:8087/dicomviewer?urls=["http://127.0.0.1:8087/3162a35b8b7446afe6da40d69e1f2305"]', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
            $.ajax({
                url: "showdir",
                type: "POST",
                data: form,
                async: true,        //异步
                processData: false,  //很重要，告诉jquery不要对form进行处理
                contentType: false,  //很重要，指定为false才能形成正确的Content-Type
                success: function (result) {
                    $('#dirBtn').attr('disabled',false);
                    $('#dirBtn').text('确定');
                    //window.open('http://127.0.0.1:8087/dicomviewer?'+result.data+'', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
                    //console.log('http://127.0.0.1:8087/dicomviewer?urls=' + result.data + '', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
                    //window.open('http://127.0.0.1:8087/dicomviewer?urls=' + result.data + '', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
                    //window.open('http://127.0.0.1:8087/dicomviewer?urls=["http://127.0.0.1:8087/1519788792143"]', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
                },
            });
//                window.open('http://127.0.0.1:8087/dicomviewer?urls=["http://127.0.0.1:8087/1519788792143"]', 'dicom', "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, width=728, height=500");
        }

    });
});