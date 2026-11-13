<template>
	<view class="login flex align-center justify-center">
		<!-- #ifndef H5 -->
		<up-navbar title="重置密码" safeAreaInsetTop :placeholder="false" :autoBack="true" bgColor="transparent">
		</up-navbar>
		<!-- #endif -->

		<view class="loginBg"></view>
		<view class="contonts">
			<view class="logo flex align-center justify-center">
				<image src="/static/logo.png" mode=""></image>
			</view>
			<view class="loginInp flex align-center justify-center">
				<view class="loginInp-box">
					<up-input style="height: 64rpx;" v-model="phone" maxlength="11" type="number" border="bottom"
						placeholder="请输入手机号" prefixIcon="/static/image/login/phone.png"></up-input>
				</view>
			</view>
			<view class="loginInp flex align-center justify-center">
				<view class="loginInp-box flex align-center" style="border-bottom: 0.5px solid #dadbde;">
					<up-input style="height: 64rpx;" v-model="captcha" border="bottom" placeholder="请输入图形验证码"
						prefixIcon="/static/image/login/password.png"></up-input>
					<view @click="getCaptcha()" class="loginInpCodeImg">
						<image :src="captchaImg" mode="widthFix"></image>
					</view>
				</view>
			</view>
			<view class="loginInp flex align-center justify-center">
				<view class="loginInp-box flex align-center" style="border-bottom: 0.5px solid #dadbde;">
					<up-input style="height: 64rpx;" v-model="code" border="bottom" placeholder="请输入验证码"
						prefixIcon="/static/image/login/password.png"></up-input>
					<view @click="getCode()" :class="timer?'activeCode':''" class="loginInpCode">
						{{text}}
					</view>
				</view>
			</view>
			<view class="loginInp flex align-center justify-center">
				<view class="loginInp-box">
					<up-input style="height: 64rpx;" type="password" v-model="password" border="bottom"
						placeholder="请输入新密码" prefixIcon="/static/image/login/password.png"></up-input>
				</view>
			</view>
			<view class="loginBtn flex align-center justify-center">
				<view @click="rustPwd()" class="loginBtn-box flex align-center justify-center">
					重置密码
				</view>
			</view>
		</view>

	</view>
</template>

<script setup>
	import config from '../../common/config';
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	const {
		proxy
	} = getCurrentInstance();
	//手机号
	const phone = ref('');
	//密码
	const password = ref('');
	//验证码
	const code = ref('');
	//倒计时
	const countDown = ref(60);
	//倒计时定时器
	const timer = ref(null);
	const text = ref('获取验证码')
	//图形验证码
	const captcha = ref('');
	const captchaImg = ref(''); //图形验证码
	const uuid = ref('');
	onLoad(option => {
		getCaptcha();
	});
	//获取图形验证码
	const getCaptcha = () => {
		let data = {
			uuid: uuid.value = new Date().getTime(),
		}
		uni.request({
			url: config.APIHOST + '/captcha.jpg', // 需要引入 config
			method: 'GET',
			data: data,
			responseType: 'arraybuffer',
			success: (res) => {
				const base64 = arrayBufferToBase64(res.data);
				captchaImg.value = `data:image/jpeg;base64,${base64}`;
			}
		})
	};
	// 辅助函数：arrayBuffer 转 base64
	const arrayBufferToBase64 = (buffer) => {
		let binary = '';
		const bytes = new Uint8Array(buffer);
		for (let i = 0; i < bytes.byteLength; i++) {
			binary += String.fromCharCode(bytes[i]);
		}
		return uni.arrayBufferToBase64(new Uint8Array(buffer));
	};
	//跳转
	const goNav = (url) => {
		uni.navigateTo({
			url: url
		})
	}
	//获取验证码
	const getCode = () => {
		if (timer.value) {
			return
		}
		if (phone.value == '') {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
			return
		}
		if (captcha.value == '') {
			uni.showToast({
				title: '请输入图形验证码',
				icon: 'none'
			})
			return
		}
		uni.showLoading({
			title: '获取验证码...'
		})
		proxy.$http.get('/app/Login/sendMsg/' + phone.value + '/forget?uuid=' + uuid.value + '&captcha=' + captcha
			.value).then(res => {
			if (res.code == 0) {
				uni.hideLoading()
				uni.showToast({
					title: '验证码已发送',
					icon: 'none'
				})
				timer.value = setInterval(() => {
					countDown.value--;
					if (countDown.value == 0) {
						clearInterval(timer.value)
						timer.value = null;
						countDown.value = 60;
						text.value = '获取验证码'
					} else {
						text.value = countDown.value + 's后重新获取'
					}
				}, 1000)
			} else {
				getCaptcha();
				uni.hideLoading()
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})

	}
	//登录
	const validatePhone = (phone) => {
		// 中国手机号码正则表达式
		const regex = /^1[3-9]\d{9}$/;
		return regex.test(phone);
	};
	//重置密码
	const rustPwd = () => {
		if (!phone.value) {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
			return
		}
		if (!validatePhone(phone.value)) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			});
			return
		};
		if (!captcha.value) {
			uni.showToast({
				title: '请输入图形验证码',
				icon: 'none'
			});
			return
		}
		if (!code.value) {
			uni.showToast({
				title: '请输入验证码',
				icon: 'none'
			});
			return
		}
		if (!password.value) {
			uni.showToast({
				title: '请输入密码',
				icon: 'none'
			});
			return
		}
		let data = {
			phone: phone.value,
			msg: code.value,
			pwd: password.value
		}
		uni.showLoading({
			title: '重置中...'
		})
		proxy.$http.post('/app/Login/forgetPwd', data).then(res => {
			if (res.code == 0) {
				uni.hideLoading();
				uni.showToast({
					title: '已重置，请重新登录',
					icon: 'none'
				})
				setTimeout(() => {
					uni.navigateBack()
				}, 1000)
			} else {
				uni.hideLoading()
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
</script>

<style lang="scss" scoped>
	page {
		background-color: #ffffff;

	}

	.loginBg {
		width: 749rpx;
		height: 327rpx;
		background: #ffffff;
		position: relative;
	}

	.loginBg::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(to right, rgba(0, 150, 255, 0.6) 0%, #DCE7FF 49%, rgba(0, 150, 255, 0.5) 100%);
		z-index: 1;
		/* 确保在垂直渐变之上 */
	}

	.loginBg::after {
		content: "";
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(to bottom, transparent, #ffffff);
		/* 垂直渐变，结束颜色为透明 */
		z-index: 2;
		/* 确保在水平渐变之上 */
	}

	.contonts {
		position: relative;
		margin-top: -100rpx;
		z-index: 3;
	}

	.activeCode {
		background-color: #999999 !important;
	}

	.logo {
		width: 100%;

		image {
			width: 150rpx;
			height: 150rpx;
			border-radius: 50%;
		}
	}

	.login {
		width: 100%;
		// height: 100vh;
		flex-direction: column;
	}

	.loginTitle {
		width: calc(100% - 60rpx);
		padding: 0 30rpx;

		.loginTitle-i {
			color: #999999;
			font-size: 32rpx;
		}
	}

	.active {
		color: #333333 !important;
		font-weight: bold;
	}

	.loginInp {
		width: 100%;
		height: auto;
		margin-top: 70rpx;

		.loginInp-box {
			width: 686rpx;
		}

		.loginInpCodeImg {
			image {
				width: 200rpx;
				height: 80rpx;
			}
		}

		.loginInpCode {
			background-color: #0096FF;
			color: #ffffff;
			padding: 8rpx 20rpx;
			border-radius: 24rpx;
			font-size: 24rpx;

		}
	}

	.loginNo {
		width: 100%;
		height: auto;
		margin-top: 26rpx;

		.loginNo-box {
			width: 686rpx;
			color: #999999;
			font-size: 26rpx;
		}
	}

	.loginBtn {
		width: 100%;
		height: auto;
		margin-top: 200rpx;

		.loginBtn-box {
			width: 646rpx;
			height: 92rpx;
			background-color: #0096FF;
			border-radius: 46rpx;
			color: #FFFFFF;
			font-size: 30rpx;
			font-weight: bold;
		}
	}
</style>