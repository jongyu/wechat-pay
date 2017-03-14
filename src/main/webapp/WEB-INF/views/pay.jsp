<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>公众号支付</title>
    <link rel="stylesheet" href="http://res.wx.qq.com/open/libs/weui/1.1.1/weui.min.css">
    <script src="//cdn.bootcss.com/jquery/3.0.0/jquery.min.js"></script>
</head>
<body>
<!--BEGIN header-->
<div class="weui-flex">
    <div class="weui-flex__item">
        <div class="weui-cells__title">微信支付</div>
    </div>
</div>
<!--END header-->

<!--BEGIN base-->
<div class="weui-cells weui-cells_form">
    <div class="weui-cell">
        <div class="weui-cell__hd"><label class="weui-label">支付金额</label></div>
        <div class="weui-cell__bd">
            <input id="money" class="weui-input" type="text" placeholder="请输入支付金额"/>
        </div>
    </div>
</div>
<!--END base-->

<div class="weui-btn-area">
    <a id="inquire" class="weui-btn weui-btn_primary" href="javascript:">支付</a>
</div>

<div id="paying_button"></div>

<!--BEGIN dialog-->
<div class="js_dialog" id="alert" style="display: none;">
    <div class="weui-mask"></div>
    <div class="weui-dialog">
        <div class="weui-dialog__bd">请输入你要支付的金额</div>
        <div class="weui-dialog__ft">
            <a href="javascript:;" id="sure" class="weui-dialog__btn weui-dialog__btn_primary">知道了</a>
        </div>
    </div>
</div>
<!--END dialog-->

<script type="text/javascript">
    $(function () {
        $('#inquire').on('click', function () {
            var money = $('#money').val();
            if (money == null || money == "") {
                $('#alert').fadeIn(200);
                return;
            }
            $.ajax({
                async: true,
                cache: true,
                url: '${pageContext.request.contextPath}/wx_prepay',
                type: 'post',
                data: {
                    'money': money
                },
                dataType: 'json',
                success: function (data) {
                    if (null != data) {
                        appId = data.appId;
                        timeStamp = data.timeStamp;
                        nonceStr = data.nonceStr;
                        pg = data.pg;
                        signType = data.signType;
                        paySign = data.paySign;
                        if (typeof WeixinJSBridge == "undefined") {
                            if (document.addEventListener) {
                                document.addEventListener(
                                    'WeixinJSBridgeReady',
                                    onBridgeReady, false);
                            } else if (document.attachEvent) {
                                document.attachEvent(
                                    'WeixinJSBridgeReady',
                                    onBridgeReady);
                                document.attachEvent(
                                    'onWeixinJSBridgeReady',
                                    onBridgeReady);
                            }
                        } else {
                            WeixinJSBridge
                                .invoke(
                                    'getBrandWCPayRequest',
                                    {
                                        "appId": appId, //公众号名称，由商户传入
                                        "timeStamp": timeStamp, //时间戳，自1970年以来的秒数
                                        "nonceStr": nonceStr, //随机串
                                        "package": "prepay_id=" + pg,
                                        "signType": signType, //微信签名方式:
                                        "paySign": paySign //微信签名
                                    },

                                    function (res) {
                                        if (res.err_msg == "get_brand_wcpay_request:ok") {
                                            alert("支付成功");
                                        }
                                        if (res.err_msg == "get_brand_wcpay_request:cancel") {
                                            alert("交易取消");
                                        }
                                        if (res.err_msg == "get_brand_wcpay_request:fail") {
                                            alert("支付失败");
                                        }
                                    });
                        }
                    } else {
                        alert("登录超时,请重新登录!");
                    }

                },
                error: function (XMLHttpRequest, textStatus) {
                    alert("系统繁忙,请稍候再试!");
                }
            });
        });

        $('#sure').on('click', function () {
            $(this).parents('.js_dialog').fadeOut(200);
        });
    });
</script>
</body>
</html>
