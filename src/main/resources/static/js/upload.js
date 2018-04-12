$(function () {
    initClickEvent();
});

function initClickEvent() {
    $('#uploadFile').click(function () {

        UpladFile();
    });

    $('#cancel').click(function () {
        $('#errorInfo').empty().hide('slow');
    });

    $('#generateQRCode').click(function () {
        $('#qrCodeError').empty().hide();
        generateQRCode(getQECodeParameter());
    });

    $('#downLoad').click(function () {
        var parameter = getQECodeParameter();
        downLoadFile({
            url: getPath() + '/ipfs/api/export-qr-code/' + parameter.hashValue,
            data: {
                "password": parameter.password,
                "number": parameter.qrCodeNum
            }
        });
    });
}

function UpladFile() {
    var fileObj = $("#uploadFileInput").get(0).files[0]; // js 获取文件对象
    var $errorInfo = $('#errorInfo');
    if (!fileObj) {
        $errorInfo.html("请选择待上传的文件").show('slow');
        return;
    }
    resetProgressBar();
    $errorInfo.hide();
    /*var FileController = getPath() + "/ipfs/api/upload"; // 接收上传文件的后台地址
    // FormData 对象
    var form = new FormData();
    form.append("file", fileObj); // 文件对象
    form.append("password", $('#password').val()); // 密码
    // XMLHttpRequest 对象
    var xhr = new XMLHttpRequest();
    xhr.open("post", FileController, true);
    xhr.onload = function () {
        alert("上传完成");
        var $progressBar = $("#progressBar");
        $("#uploadFile").attr('disabled', false);
        $progressBar.parent().removeClass("active");
        $progressBar.parent().hide();
    };
    xhr.upload.addEventListener("progress", progressFunction, false);
    xhr.send(form);*/

}

function initImgUpload(imgId) {
    var uploader = new plupload.Uploader({
        runtimes: 'html5,flash,silverlight,html4',
        browse_button: imgId, //
        url: getPath() + '/ipfs/api/upload?password=' + $('#password').val(),
        filters: {
            max_file_size: '3024MB'
        },
        init: {
            PostInit: function () {
                $('#uploadFile').click(function () {
                    console.log("212121");
                    uploader.start();
                    return false;
                });
            },
            FilesAdded: function (up, files) {

            },
            UploadProgress: function (up, file) {
                //	alert("UploadProgress");
                console.log(up);
                console.log(file);
            },
            UploadComplete: function (up, file, result) {
            },
            FileUploaded: function (up, file, result) {
                alert(result.response);
                var $progressBar = $("#progressBar");
                $("#uploadFile").attr('disabled', false);
                $progressBar.parent().removeClass("active");
                $progressBar.parent().hide();
            }
            ,
            Error: function (up, err) {
                console.log("Error #" + err.code + ": " + err.message);
            }
        }
    });
    uploader.init();
}

function progressFunction(evt) {
    var progressBar = $("#progressBar");
    if (evt.lengthComputable) {
        var completePercent = Math.round(evt.loaded / evt.total * 100) + "%";
        progressBar.width(completePercent);
        progressBar.html(completePercent);
        $("#batchUploadBtn").val("正在上传 " + completePercent);
    }
};

function resetProgressBar() {
    $('#uploadFileProgressBar').show("slow");
    $('#progressBar').width('0%');
}


function getQECodeParameter() {
    return {
        "password": $('#getQRCodePassword').val(),
        "hashValue": $('#getQRCodeHash').val(),
        "qrCodeNum": $('#QRCodeNumber').val()
    }
}

var imgTpl = '<img src="${imagePath}" style="width: 150px; height: 150px;" class="rounded" alt="${hash}">';

function generateQRCode(parameter) {
    var $qrCodError = $('#qrCodeError');
    if (!parameter.hashValue) {
        showQRCodeError("hash值不能为空");
        return;
    }
    if (!$.isNumeric(parameter.qrCodeNum)) {
        showQRCodeError("二维码数量必须是数字");
        return;
    }
    $qrCodError.hide();
    $.post(getPath() + "/ipfs/api/qr-code/" + parameter.hashValue, {
        "password": parameter.password,
        "number": parameter.qrCodeNum
    }, function (data) {
        console.log(data);
        var $qrCodeModal = $('#qrCodeModal');
        if (data.returnCode === '000' && !!data.data) {
            var htmlArr = [];
            var qrCodeList = data.data.qrCodeList;

            for (var i = 0, len = qrCodeList.length; i < len; i++) {
                htmlArr.push(imgTpl.replace('${imagePath}', getPath() + qrCodeList[i]).replace("${hash}", data.data.hash))
            }
            $qrCodeModal.find(".modal-body").html(htmlArr.join(""));
            $qrCodeModal.modal({show: true})
        } else {
            $qrCodError.html("生成错误，稍后再试").show()
        }
    });
}

function showQRCodeError(message) {
    $('#qrCodeError').html(message).show("slow")
}