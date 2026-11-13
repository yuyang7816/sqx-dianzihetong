<template>
	<view class="page container" style="background-color: #F7F7F7;">
		<view class="feedback-title">
			<text>问题和意见</text>
			<text @tap="chooseMsg">快速键入</text>
		</view>
		<view class="feedback-body"><textarea placeholder="请详细描述你的问题和意见..." v-model="sendDate.content"
				class="feedback-textare" /></view>
		<view class="feedback-title"><text>联系方式</text></view>
		<view class="feedback-body"><input class="feedback-input" :customStyle="customStyle" v-model="sendDate.contact"
				placeholder="方便我们联系你" /></view>

		<button style="" class="feedback-submit" @tap="send">提交</button>
	</view>
</template>

<script setup>
	import {
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app'
	const {
		proxy
	} = getCurrentInstance()
	const customStyle = reactive({
		color: '#333333'
	})
	const msgContents = reactive(['界面显示错乱', '启动缓慢，卡出翔了', 'UI无法直视，丑哭了', '偶发性崩溃'])
	const sendDate = reactive({
		score: 5,
		content: '',
		contact: ''
	})
	onLoad(() => {
		// #ifdef APP
		let deviceInfo = {
			appid: plus.runtime.appid,
			imei: plus.device.imei, //设备标识
			p: plus.os.name === 'Android' ? 'a' : 'i', //平台类型，i表示iOS平台，a表示Android平台。
			md: plus.device.model, //设备型号
			app_version: plus.runtime.version,
			plus_version: plus.runtime.innerVersion, //基座版本号
			os: plus.os.version,
			net: '' + plus.networkinfo.getCurrentType()
		};
		sendDate = Object.assign(deviceInfo, this.sendDate);
		// #endif


	});
	const chooseMsg = () => {
		//快速输入
		uni.showActionSheet({
			itemList: msgContents,
			success: res => {
				sendDate.content = msgContents[res.tapIndex];
			}
		});
	};
	const send = () => {
		//发送反馈

		if (!sendDate.content) {
			uni.showToast({
				icon: 'none',
				title: '请输入反馈内容'
			});
			return;
		}
		if (!sendDate.contact) {
			uni.showToast({
				icon: 'none',
				title: '请填写QQ或邮箱'
			});
			return;
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.postJson('/app/message/insertMessage', {
			title: sendDate.contact,
			content: JSON.stringify(sendDate),
			state: 2
		}).then(res => {
			if (res.code === 0) {
				uni.showToast({
					title: '提交成功'
				});
				setTimeout(function() {
					uni.navigateBack();
				}, 1000);
			} else {
				uni.hideLoading();
				uni.showToast({
					title: res.msg,
					iocn: 'none',
					duration: 1000
				})
			}
		});
	}
</script>

<style>
	@font-face {
		font-family: uniicons;
		font-weight: normal;
		font-style: normal;
		src: url('https://img-cdn-qiniu.dcloud.net.cn/fonts/uni.ttf') format('truetype');
	}

	view {
		font-size: 28upx;
	}


	/*问题反馈*/
	.feedback-title {
		display: flex;
		flex-direction: row;
		justify-content: space-between;
		align-items: center;
		padding: 20upx;
		color: #333333;
		font-size: 28upx;
	}

	.feedback-star-view.feedback-title {
		justify-content: flex-start;
		margin: 0;
	}

	.feedback-body {
		font-size: 32upx;
		padding: 16upx;
		margin: 16upx;
		border-radius: 16upx;
		/* background: #FFFFFF; */
		color: #000000;
	}

	.feedback-textare {
		width: calc(100% - 64rpx);
		border-radius: 8rpx;
		font-size: 28rpx;
		padding: 42rpx 32rpx;
		color: #333333;
		background: #FFFFFF;
	}

	.feedback-input {
		background: #FFFFFF;
		font-size: 28rpx;
		border-radius: 8rpx;
		padding: 32rpx;
		box-sizing: content-box;
		color: #333333;
	}


	.feedback-submit {
		background: #0096FF;
		color: #ffffff;
		margin: 20upx;
		margin-top: 32upx;
	}
</style>