$(function () {
    initClickEvent();
});

function initClickEvent() {

    $('#file').change(function () {
        $('#uploadFile').attr('disabled', false);
    });

    $('#uploadFile').click(function () {
        uploadFile();
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

function uploadFile() {
    var formData = new FormData();

    var file = $('#file')[0].files[0];

    resetProgressBar();
    formData.append('file', file);
    formData.append('password', $('#password').val());
    formData.append('name', file.name);

    function onprogress(evt) {
        // 写要实现的内容
        var progressBar = $("#progressBar");
        if (evt.lengthComputable) {
            var completePercent = Math.round(evt.loaded / evt.total * 100);
            progressBar.width(completePercent + "%");
            $("#progressBar").html(completePercent + "%");
        }
    }

    var xhr_provider = function () {
        var xhr = jQuery.ajaxSettings.xhr();
        if (onprogress && xhr.upload) {
            xhr.upload.addEventListener('progress', onprogress, false);
        }
        return xhr;
    };
    $.ajax({
        url: getPath() + '/ipfs/api/upload',
        type: 'POST',
        cache: false,
        data: formData,
        processData: false,
        contentType: false,
        xhr: xhr_provider,
        success: function (data) {
            if (data.returnCode === '000') {
                alert("上传成功");
                $('#hashValue').val(data.data.hashValue)
            }
            // 进度条隐藏
            $("#progressBar").parent().hide();
        },
        error: function (data) {
            console.info(data);
        }
    })
}

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

var imgTpl = '<img src="${imagePath}" style="width: 150px; height: 200px;" class="rounded" alt="${hash}">';

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
                htmlArr.push(imgTpl.replace('${imagePath}', getPath() + "/" + qrCodeList[i]).replace("${hash}", data.data.hash))
            }
            $qrCodeModal.find(".modal-body").html(htmlArr.join(""));
            $qrCodeModal.modal({show: true})
        } else {
            $qrCodError.html(data.returnMsg).show()
        }
    });
}

function showQRCodeError(message) {
    $('#qrCodeError').html(message).show("slow")
}

function testSaveData() {
    $.post(getPath() + "/ipfs/api/save/dsadss21212121", function (data) {
        console.log(data);
    })
}

function testUploadData() {
    $.post(getPath() + "/ipfs/api/upload-test", function (data) {
        console.log(data);
    })
}
