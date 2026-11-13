<template>
	<view class="login flex align-center justify-center">
		<!-- #ifndef H5 -->
		<up-navbar title="登录" safeAreaInsetTop :placeholder="false" :autoBack="true" bgColor="transparent">
		</up-navbar>
		<!-- #endif -->
		<view class="loginBg"></view>
		<!-- #ifndef MP-WEIXIN -->
		<view class="contonts">
			<view class="loginTitle flex align-center">
				<view @click="setTabs(0)" class="loginTitle-i" :class="current==0?'active':''">
					密码登录
				</view>
				<up-line direction="col" margin="0 20rpx" color="#D6D6D6" length="26rpx"></up-line>
				<view @click="setTabs(1)" class="loginTitle-i" :class="current==1?'active':''">
					验证码登录
				</view>
			</view>
			<view class="loginInp flex align-center justify-center">
				<view class="loginInp-box">
					<up-input style="height: 64rpx;" v-model="phone" maxlength="11" type="number" border="bottom"
						placeholder="请输入手机号" prefixIcon="/static/image/login/phone.png"></up-input>
				</view>
			</view>
			<view v-if="current == 0" class="loginInp flex align-center justify-center">
				<view class="loginInp-box">
					<up-input style="height: 64rpx;" type="password" v-model="password" border="bottom"
						placeholder="请输入账号密码" prefixIcon="/static/image/login/password.png"></up-input>
				</view>
			</view>
			<view v-if="current == 1" class="loginInp flex align-center justify-center">
				<view class="loginInp-box flex align-center" style="border-bottom: 0.5px solid #dadbde;">
					<up-input style="height: 64rpx;" v-model="captcha" border="bottom" placeholder="请输入图形验证码"
						prefixIcon="/static/image/login/password.png"></up-input>
					<view @click="getCaptcha()" class="loginInpCodeImg">
						<image :src="captchaImg" mode="widthFix"></image>
					</view>
				</view>
			</view>
			<view v-if="current == 1" class="loginInp flex align-center justify-center">
				<view class="loginInp-box flex align-center" style="border-bottom: 0.5px solid #dadbde;">
					<up-input style="height: 64rpx;" v-model="code" border="bottom" placeholder="请输入验证码"
						prefixIcon="/static/image/login/password.png"></up-input>
					<view @click="getCode()" :class="timer?'activeCode':''" class="loginInpCode">
						{{text}}
					</view>
				</view>
			</view>
			<view class="loginNo flex align-center justify-center">
				<view @click="goNav('/pages/public/resetPwd')" class="loginNo-box flex align-center justify-end">
					忘记密码?
				</view>
			</view>
			<view class="loginBtn flex align-center justify-center">
				<view @click="login()" class="loginBtn-box flex align-center justify-center">
					登录
				</view>
			</view>
			<!-- 协议 -->
			<view class="loginXy flex align-center justify-center">
				<view class="loginXy-box flex align-center justify-center">
					<up-checkbox activeColor="#267DFA" shape="circle" label="" name="agree" usedAlone
						v-model:checked="check">
					</up-checkbox>
					<view @click="check = !check" class="flex align-center">
						同意
						<text @click.stop.prevent="goNav('/my/setting/xieyi?type=177')">《用户服务协议》</text>
						及
						<text @click.stop.prevent="goNav('/my/setting/xieyi?type=176')">《隐私权政策》</text>
					</view>
				</view>
			</view>
		</view>
		<!-- #endif -->
		<!-- 小程序登录 -->
		<!-- #ifdef MP-WEIXIN -->
		<view class="contonts">
			<view class="mp_wxBox">
				<view>
					<view class="headers">
						<image src="../../static/logo.png" style="border-radius: 50%;"></image>
					</view>
					<view class="content">
						<view>申请获取以下权限</view>
						<text>获得你的公开信息(昵称，头像、地区等)</text>
					</view>
					<button v-if="weixinPhone == true || weixinPhone == 'true'"
						style="background: #0096FF;color: #FFFFFF;" class="bottom" open-type="getPhoneNumber"
						@getphonenumber="getPhoneNumber">
						授权手机号
					</button>
					<button v-else style="background: #0096FF;color: #FFFFFF;" class='bottom' bindtap="getUserProfile"
						@tap="loginWx()">
						授权登录
					</button>
				</view>
			</view>
			<view style="text-align: center;">
				<view class="footers">
					<up-checkbox activeColor="#267DFA" shape="circle" label="" name="agree" usedAlone
						v-model:checked="check">
					</up-checkbox>
					<text>同意</text>
					<navigator url="/my/setting/xieyi?type=176" open-type="navigate">《隐私政策》</navigator>
					和
					<navigator url="/my/setting/xieyi?type=177" open-type="navigate">《用户服务协议》</navigator>
				</view>
			</view>
		</view>
		<!-- #endif -->


	</view>
</template>

<script setup>
	import config from '../../common/config';
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app'
	const {
		proxy
	} = getCurrentInstance();
	//登录类型
	const current = ref(0);
	//手机号
	const phone = ref('');
	//密码
	const password = ref('');
	//是否选择协议
	const check = ref(false);
	//验证码
	const code = ref('');
	//倒计时
	const countDown = ref(60);
	//倒计时定时器
	const timer = ref(null);
	const text = ref('获取验证码');
	//图形验证码
	const captcha = ref('');
	const captchaImg = ref(''); //图形验证码
	const uuid = ref('');
	const sysPhone = ref(''); //系统类型 1:安卓 2:ios
	const platform = ref(''); //来源平台 h5 app 小程序 
	const weixinPhone = ref(false);
	const sessionkey = ref('');
	const flag = ref('');
	const sendDataList = ref({});
	onLoad(option => {
		if (option.inviterCode) {
			uni.setStorageSync('inviterCode', option.inviterCode)
		}
		let sysInfo = uni.getSystemInfoSync();
		if (sysInfo.osName == 'ios') {
			sysPhone.value = 2
		} else {
			sysPhone.value = 1
		}
		// #ifdef H5
		platform.value = 'h5'
		// #endif
		// #ifdef APP
		platform.value = 'app'
		// #endif
		// #ifdef MP-WEIXIN
		platform.value = 'mp'
		// #endif
		// #ifndef MP-WEIXIN
		getCaptcha();
		// #endif

	});
	//小程序微信登录后获取手机号
	const setPhoneByInsert = (decryptData, iv) => {
		let data = {
			decryptData: decryptData,
			key: sessionkey.value,
			iv: iv
		};

		proxy.$http.postJson('/app/Login/selectPhone', data).then(res => {
			if (res.code == 0) {
				phone.value = res.data.phoneNumber;
				getWeixinInfo(sendDataList.value);
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none',
					duration: 2000
				});
			}
		})
	};
	//小程序微信登录后获取手机号
	const getPhoneNumber = (e) => {
		if (!check.value) {
			uni.showToast({
				title: '请阅读并同意《用户服务协议》与《隐私权政策》',
				icon: 'none'
			});
			return
		};
		if (e.detail.errMsg == 'getPhoneNumber:fail user deny') {
			console.log('用户拒绝提供手机号');
		} else {
			console.log('用户同意提供手机号');
			console.log(e)
			setPhoneByInsert(e.detail.encryptedData, e.detail.iv);
		}
	};
	//获取个人信息
	const getWeixinInfo = (sendData) => {
		uni.showLoading({
			title: '登录中...'
		});
		let postData = {
			openId: sendData.openId, //小程序openId
			unionId: sendData.unionId, //unionId
			userName: sendData.userName, //微信名称
			avatar: sendData.avatar, //头像
			sex: sendData.sex, //性别
			phone: phone.value,
			inviterCode: sendData.inviterCode ? sendData.inviterCode : '',
			type: 1
		};
		proxy.$http.postJson('/app/Login/insertWxUser', postData).then(async res => {
			if (res.code == 0) {
				uni.hideLoading()
				uni.setStorageSync('token', res.token)
				uni.setStorageSync('userName', res.user.userName)
				uni.setStorageSync('avatar', res.user.avatar)
				uni.setStorageSync('phone', res.user.phone)
				uni.setStorageSync('invitationCode', res.user.invitationCode)
				uni.setStorageSync('sex', res.user.sex)
				uni.setStorageSync('userId', res.user.userId)
				uni.setStorageSync('openId', res.user.openId)
				uni.hideLoading();
				uni.navigateBack();
			} else {
				uni.hideLoading()
				uni.showModal({
					title: '登录失败',
					content: res.msg,
					confirmColor: '#0096FF',
					success: function(res) {
						if (res.confirm) {
							console.log('用户点击确定');
						} else if (res.cancel) {
							console.log('用户点击取消');
						}
					}
				});
			}
		})
	};
	//登录(小程序)
	const loginWx = () => {
		if (!check.value) {
			uni.showToast({
				title: '请阅读并同意《用户服务协议》与《隐私权政策》',
				icon: 'none'
			});
			return
		};
		// 1.wx获取登录用户code
		uni.login({
			provider: 'weixin',
			success: function(loginRes) {
				let data = {
					code: loginRes.code,
					type: 1
				}
				uni.showLoading({
					title: '登录中...'
				});
				proxy.$http.get('/app/Login/wxLogin', data).then(res => {
					if (res.code == 0) {
						uni.hideLoading()
						uni.setStorageSync('openId', res.data.open_id)
						uni.setStorageSync('unionId', res.data.unionId)
						sessionkey.value = res.data.session_key;

						let inviterCode = '';
						if (uni.getStorageSync('inviterCode')) {
							inviterCode = uni.getStorageSync('inviterCode')
						}
						let sendData = {
							openId: uni.getStorageSync('openId'),
							unionId: uni.getStorageSync('unionId'),
							userName: '游客',
							avatar: '',
							sex: 1, //性别
							inviterCode: inviterCode //别人登录进来携带你的邀请码
						};
						Object.assign(sendDataList.value, sendData)
						flag.value = res.data.flag;
						// 第一次登录获取手机号
						if (flag.value == '1') {
							weixinPhone.value = true;
						} else {
							getWeixinInfo(sendData);
						}
					} else {
						uni.hideLoading()
						uni.showToast({
							icon: 'none',
							title: res.msg,
							duration: 2000
						});
					}
				})

			}
		});
	};
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
	//切换登录方式
	const setTabs = (index) => {
		current.value = index;
		//如果有定时器，清除定时器
		if (timer.value) {
			clearInterval(timer.value)
			timer.value = null;
			countDown.value = 60;
			text.value = '获取验证码'
		}
		password.value = ''
	}
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
		proxy.$http.get('/app/Login/sendMsg/' + phone.value + '/register?uuid=' + uuid.value + '&captcha=' + captcha
				.value)
			.then(res => {
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
	const login = () => {
		if (!phone.value) {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			});
			return
		};

		if (!validatePhone(phone.value)) {
			uni.showToast({
				title: '请输入正确的手机号',
				icon: 'none'
			});
			return
		};
		//密码登录
		if (current.value == 0) {
			if (!password.value) {
				uni.showToast({
					title: '请输入密码',
					icon: 'none'
				});
				return
			}
		}
		//验证码登录
		if (current.value == 1) {
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
		}
		if (!check.value) {
			uni.showToast({
				title: '请阅读并同意《用户服务协议》与《隐私权政策》',
				icon: 'none'
			});
			return
		};
		if (current.value == 0) {
			passwordLogin();
		} else { //验证码登录
			codeLogin();
		}

	};
	//验证码登录
	const codeLogin = () => {
		let data = {
			platform: platform.value, //平台类型
			sysPhone: sysPhone.value, //系统类型
			phone: phone.value, //手机号
			msg: code.value, //验证码
			openId: uni.getStorageSync('openId'),
			inviterCode: uni.getStorageSync('inviterCode') ? uni.getStorageSync('inviterCode') : ''
		}
		uni.showLoading({
			title: '登录中...'
		})
		proxy.$http.post('/app/Login/registerCode', data).then(res => {
			if (res.code == 0) {
				uni.hideLoading()
				uni.setStorageSync('token', res.token);
				uni.setStorageSync('userId', res.user.userId);
				uni.setStorageSync('phone', res.user.phone);
				uni.setStorageSync('userName', res.user.userName);
				uni.setStorageSync('invitationCode', res.user.invitationCode);
				uni.setStorageSync('avatar', res.user.avatar);
				uni.showToast({
					title: '登录成功'
				})
				setTimeout(() => {
					uni.navigateBack()
				}, 1000)
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	//密码登录
	const passwordLogin = () => {
		let data = {
			platform: platform.value, //平台类型
			sysPhone: sysPhone.value, //系统类型
			phone: phone.value, //手机号
			password: password.value, //密码
			openId: uni.getStorageSync('openId'),
			inviterCode: uni.getStorageSync('inviterCode') ? uni.getStorageSync('inviterCode') : ''
		}
		uni.showLoading({
			title: '登录中...'
		})
		proxy.$http.post('/app/Login/registerCode', data).then(res => {
			if (res.code == 0) {
				uni.hideLoading()
				uni.setStorageSync('token', res.token);
				uni.setStorageSync('userId', res.user.userId);
				uni.setStorageSync('phone', res.user.phone);
				uni.setStorageSync('userName', res.user.userName);
				uni.setStorageSync('invitationCode', res.user.invitationCode);
				uni.setStorageSync('avatar', res.user.avatar);
				uni.showToast({
					title: '登录成功'
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
		});
	}
</script>
<style>
	page {
		background-color: #ffffff;

	}
</style>
<style lang="scss" scoped>
	.activeCode {
		background-color: #999999 !important;
	}

	.login {
		width: 100%;
		// height: 100vh;
		flex-direction: column;
		// padding-top: 100rpx;

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

	.loginXy {
		width: 100%;
		margin-top: 26rpx;

		.loginXy-box {
			width: 646rpx;
			font-size: 24rpx;
			color: #999999;

			text {
				color: #0096FF;
			}
		}
	}

	.mp_wxBox {
		.headers {
			margin: 35% auto 50rpx;
			text-align: center;
			border-radius: 60rpx;
			width: 650rpx;
			height: 300rpx;
			line-height: 450rpx;

			image {
				width: 300rpx;
				height: 300rpx;
			}
		}

		.content {
			text-align: center;
		}

		text {
			display: block;
			color: #9d9d9d;
			margin-top: 40rpx;
		}

		.bottom {
			line-height: 80upx;
			border-radius: 80upx;
			margin: 70rpx 50rpx;
			height: 80upx;
			font-size: 35rpx;
		}
	}

	::v-deep .u-checkbox {
		display: block !important;
	}

	.footers {
		display: flex;
		justify-content: center;
		align-items: center;
		margin-top: 32upx;
		font-size: 24upx;
		color: #666666;
		text-align: center;
		// display: flex;
	}
</style>