<template>
	<view style="padding-bottom: 40rpx;">
		<view class="lists flex align-center justify-center">
			<view class="lists-box">
				<view class="lists-box-title">
					充值套餐
				</view>
				<view class="lists-box-list flex align-center justify-between flex-wrap">
					<view @click="payCur = index" :class="payCur===index?'listsActive':''"
						class="lists-box-list-i flex align-center justify-center" v-for="(item,index) in payList"
						:key="index">
						<view class="lists-box-list-i-t">
							<text>{{item.price}}</text>元
						</view>
						<view class="lists-box-list-i-b">
							合同签署套餐{{item.numberSign}}份
						</view>
					</view>
				</view>
			</view>
		</view>
		<!-- 充值方式 -->
		<view class="payAway flex align-center justify-center">
			<view class="payAway-box">
				<view class="payAway-box-title">
					充值方式
				</view>
				<up-radio-group activeColor="#0096FF" v-model="payAway" placement="column">
					<view @click="payAway = item.id" class="payAway-box-i flex align-center justify-between"
						v-for="(item,index) in payAwayList" :key="index">
						<view class="payAway-box-i-l flex align-center">
							<image :src="item.src" mode=""></image>
							<text>{{item.name}}</text>
						</view>
						<up-radio :name="item.id">
						</up-radio>
					</view>

				</up-radio-group>
			</view>
		</view>
		<view class="botomBtn flex align-center justify-center">
			<view @click="payClick()" class="botomBtn-box flex align-center justify-center">
				立即结算
			</view>
		</view>
		<view class="xieyi flex align-center justify-center">
			<view class="xieyi-box flex align-center">
				<up-checkbox-group size="16" shape="circle" v-model="isSelect" @change="changeSelect"
					placement="column">
					<up-checkbox :name="true">
					</up-checkbox>
				</up-checkbox-group>
				点击充值代表您已同意<text @tap="goNav('/my/setting/xieyi?type=432')">《充值协议》</text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		reactive,
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const payCur = ref(0);
	const payList = reactive([]);
	const payAway = ref(1);
	const payAwayList = reactive([{
			id: 1,
			name: '微信支付',
			src: '/static/image/public/wxPay.png'
		},
		{
			id: 2,
			name: '支付宝支付',
			src: '/static/image/public/zfbPay.png'
		}
	]);
	const isSelect = reactive([]);

	onLoad(() => {
		// #ifdef H5
		let ua = navigator.userAgent.toLowerCase();
		if (ua.indexOf('micromessenger') !== -1) { //微信公众号
			let payList = [{
				id: 1,
				name: '微信支付',
				src: '/static/image/public/wxPay.png'
			}]
			payAwayList.splice(0, payAwayList.length, ...payList)
		} else {
			let payList = [{
				id: 2,
				name: '支付宝支付',
				src: '/static/image/public/zfbPay.png'

			}]
			payAwayList.splice(0, payAwayList.length, ...payList)
			payAway.value = 2
		}
		// #endif
		// #ifdef MP-WEIXIN
		let payList = [{
			id: 1,
			name: '微信支付',
			src: '/static/image/public/wxPay.png'
		}]
		payAwayList.splice(0, payAwayList.length, ...payList);
		payAway.value = 1
		// #endif
		queryPayClassifyList();
	});
	const goNav = (url) => {
		uni.navigateTo({
			url: url
		})
	};
	const queryPayClassifyList = () => {
		proxy.$http.getT('/app/payClassify/queryPayClassifyList').then(res => {
			if (res.code == 0) {
				payList.splice(0, payList.length, ...res.data.records)
			}
		})
	};

	const payClick = () => {
		if (isSelect.length === 0) {
			uni.showToast({
				title: '请阅读并同意《充值协议》',
				icon: 'none'
			})
			return
		}
		if (payAway.value === 1) { //微信支付
			wxPayOrder();
		} else { //支付宝支付
			aliPayOrder();
		}
	};
	//微信支付
	const wxPayOrder = () => {
		let data = {
			userId: uni.getStorageSync('userId'),
			payClassifyId: payList[payCur.value].payClassifyId
		}
		// #ifdef H5
		data.classify = 2
		// #endif
		// #ifdef APP
		data.classify = 1
		// #endif
		// #ifdef MP-WEIXIN
		data.classify = 3
		// #endif
		uni.showLoading({
			title: '支付中...'
		})
		proxy.$http.postT('/app/wxPay/payApp', data).then(res => {
			if (res.code == 0) {
				// #ifdef H5
				callPay(res.data);
				// #endif
				// #ifdef MP-WEIXIN
				uni.requestPayment({
					provider: 'wxpay',
					timeStamp: res.data.timestamp,
					nonceStr: res.data.noncestr,
					package: res.data.package,
					signType: res.data.signType,
					paySign: res.data.sign,
					success: function(red) {
						uni.hideLoading()
						uni.showToast({
							title: '已支付'
						})
					},
					fail: function(err) {
						uni.hideLoading();
						that.$queue.showToast(
							'支付失败');
					}
				});
				// #endif
				// #ifdef APP
				setPayment('wxpay', JSON.stringify(res.data));
				// #endif
			} else {
				uni.hideLoading()
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	//公众号支付
	const callPay = (response) => {
		if (typeof WeixinJSBridge === "undefined") {
			if (document.addEventListener) {
				document.addEventListener('WeixinJSBridgeReady', onBridgeReady(response), false);
			} else if (document.attachEvent) {
				document.attachEvent('WeixinJSBridgeReady', onBridgeReady(response));
				document.attachEvent('onWeixinJSBridgeReady', onBridgeReady(response));
			}
		} else {
			onBridgeReady(response);
		}
	};
	const onBridgeReady = (response) => {
		if (!response.package) {
			return;
		}
		WeixinJSBridge.invoke(
			'getBrandWCPayRequest', {
				"appId": response.appid, //公众号名称，由商户传入
				"timeStamp": response.timestamp, //时间戳，自1970年以来的秒数
				"nonceStr": response.noncestr, //随机串
				"package": response.package,
				"signType": response.signType, //微信签名方式：
				"paySign": response.sign //微信签名
			},
			function(res) {
				if (res.err_msg === "get_brand_wcpay_request:ok") {
					// 使用以上方式判断前端返回,微信团队郑重提示：
					//res.err_msg将在用户支付成功后返回ok，但并不保证它绝对可靠。
					uni.hideLoading()
					uni.showToast({
						title: '已支付'
					})
				} else {
					uni.hideLoading();
				}
				WeixinJSBridge.log(response.err_msg);
			}
		);
	};
	//支付宝支付
	const aliPayOrder = () => {
		let data = {
			userId: uni.getStorageSync('userId'),
			payClassifyId: payList[payCur.value].payClassifyId
		}
		// #ifdef H5
		data.classify = 5
		// #endif
		// #ifdef APP
		data.classify = 4
		// #endif
		uni.showLoading({
			title: '支付中...'
		})
		proxy.$http.postT('/app/aliPay/payApp', data).then(res => {
			if (res.code == 0) {
				// #ifdef H5
				uni.hideLoading()
				const div = document.createElement('div')
				div.innerHTML = res.data //此处form就是后台返回接收到的数据
				document.body.appendChild(div)
				document.forms[0].submit()
				// #endif
				// #ifdef APP
				setPayment('alipay', res.data)
				// #endif
			} else {
				uni.hideLoading()
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	//app支付
	const setPayment = (name, order) => {
		uni.requestPayment({
			provider: name,
			orderInfo: order, //微信、支付宝订单数据
			success: function(res) {
				uni.hideLoading()
				uni.showToast({
					title: '已购买'
				})
			},
			fail: function(err) {
				uni.hideLoading();
				uni.showToast({
					title: '支付失败',
					icon: 'none'
				})
				console.log(err, 12)
			}
		});
	};
	const changeSelect = (e) => {
		isSelect.splice(0, isSelect.length, ...e)
	};
</script>

<style lang="scss">
	.listsActive {
		background: #0096FF !important;

		.lists-box-list-i-t {
			color: #ffffff !important;

			text {
				color: #ffffff !important;
			}
		}

		.lists-box-list-i-b {
			color: #ffffff !important;
		}
	}

	.lists {
		width: 100%;
		height: auto;
		background-color: #ffffff;
		margin-top: 20rpx;

		.lists-box {
			width: 686rpx;
			height: 100%;
			padding: 36rpx 0;
		}

		.lists-box-title {
			font-weight: bold;
			font-size: 28rpx;
			color: #333333;
		}

		.lists-box-list {

			.lists-box-list-i {
				width: 333rpx;
				height: 177rpx;
				background: #F3F8FF;
				border-radius: 18rpx;
				flex-direction: column;
				margin-top: 30rpx;

				.lists-box-list-i-t {
					color: #0096FF;
					font-size: 24rpx;

					text {
						font-weight: 500;
						font-size: 44rpx;
						color: #0096FF;
					}
				}

				.lists-box-list-i-b {
					font-weight: 500;
					font-size: 24rpx;
					color: #999999;
				}
			}
		}
	}

	.payAway {
		width: 100%;
		height: auto;
		margin-top: 20rpx;
		background-color: #ffffff;

		.payAway-box {
			width: 686rpx;
			height: 100%;
			padding: 36rpx 0;
		}

		.payAway-box-title {
			font-weight: bold;
			font-size: 28rpx;
			color: #333333;
		}

		.payAway-box-i {
			margin-top: 40rpx;

			.payAway-box-i-l {
				image {
					width: 42rpx;
					height: 42rpx;
					margin-right: 5rpx;
				}

				text {
					font-weight: 500;
					font-size: 24rpx;
					color: #999999;
				}
			}
		}
	}

	.botomBtn {
		width: 100%;
		height: auto;
		margin-top: 115rpx;

		.botomBtn-box {
			width: 686rpx;
			height: 80rpx;
			background: #0096FF;
			border-radius: 16rpx;
			font-weight: bold;
			font-size: 28rpx;
			color: #FFFFFF;
		}
	}

	.xieyi {
		width: 100%;
		height: auto;
		margin-top: 16rpx;

		.xieyi-box {
			width: 686rpx;
			height: 100%;
			color: #999999;
			font-size: 24rpx;

			text {
				color: #0096FF;
			}
		}
	}
</style>