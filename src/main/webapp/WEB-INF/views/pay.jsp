<%--
  Created by IntelliJ IDEA.
  User: ZhongYu
  Date: 3/13/2017
  Time: 7:33 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <title>支付测试</title>
    <link rel="stylesheet" href="http://res.wx.qq.com/open/libs/weui/1.1.1/weui.min.css">
    <script src="//cdn.bootcss.com/jquery/3.0.0/jquery.min.js"></script>
</head>
<body>
<div class="weui-flex">
    <div class="weui-flex__item">
        <div class="weui-cells__title">微信公众平台支付</div>
    </div>
</div>

<div class="weui-cells weui-cells_form">
    <div class="weui-cell">
        <div class="weui-cell__hd"><label class="weui-label">用户编号</label></div>
        <div class="weui-cell__bd">
            <input id="openid" class="weui-input" type="text" value="${openid}" placeholder="请输入用户编号"/>
        </div>
    </div>
    <div class="weui-cell">
        <div class="weui-cell__hd"><label class="weui-label">支付金额</label></div>
        <div class="weui-cell__bd">
            <input id="money" class="weui-input" type="text" pattern="[0-20]*" placeholder="请输入支付金额"/>
        </div>
    </div>
</div>

<div class="weui-btn-area">
    <a id="payment-btn" class="weui-btn weui-btn_primary" href="javascript:">支付</a>
</div>

<div class="js_dialog" id="alert-window" style="display: none;">
    <div class="weui-mask"></div>
    <div class="weui-dialog">
        <div class="weui-dialog__bd">请输入您要支付的金额</div>
        <div class="weui-dialog__ft">
            <a href="javascript:;" id="alert-off" class="weui-dialog__btn weui-dialog__btn_primary">知道了</a>
        </div>
    </div>
</div>

<script>
    $(function () {
        $('#payment-btn').on('click', function () {
            var money = $('#money').val();
            if (money == null || money == "") {
                $('#alert-window').fadeIn(200);
                return;
            }
            $.ajax({
                async: true,
                cache: true,
                url: '${pageContext.request.contextPath}/order.html',
                type: 'post',
                data: {
                    'money': money
                },
                dataType: 'json',
                success: function (data) {
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
                                    "package": "prepay_id="
                                    + pg,
                                    "signType": signType, //微信签名方式:
                                    "paySign": paySign
                                    //微信签名
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
                }
            });
        });

        $('#alert-off').on('click', function () {
            $(this).parents('.js_dialog').fadeOut(200);
        });
    });


</script>
</body>
</html>
