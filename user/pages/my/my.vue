<template>
	<view style="padding-bottom: 40rpx;">
		<view class="userInfo flex align-center justify-center">
			<!-- goNav('/package/enterprise/renzheng?isAuthentication='+isAuthentication) -->
			<view @click="!token?goNav('/pages/public/login'):''" class="userInfo-box flex align-center">
				<!-- <image :src="avatar?avatar:'/static/logo.png'" mode="aspectFill"></image> -->
				<view class="userInfo-box-r">
					<view class="userInfo-box-r-name flex align-center">
						{{userName?userName:'登录'}}
						<view v-if="token" class="userInfo-box-r-name-status flex align-center">
							<up-icon name="account-fill" color="#ffffff" size="14"></up-icon>
							<text v-if="isAuthentication===-1">未认证</text>
							<text v-if="isAuthentication===0">审核中</text>
							<text v-if="isAuthentication===1">已通过</text>
							<text v-if="isAuthentication===2">已拒绝</text>
						</view>
					</view>
					<view v-if="phone" class="userInfo-box-r-phone">
						{{phone}}
					</view>
				</view>
			</view>
		</view>
		<view class="classify flex align-center justify-center">
			<view class="classify-box flex align-center justify-between">
				<view @click="goNavRz('/package/seal/index')" class="classify-box-i flex align-center justify-between">
					<view class="classify-box-i-l">
						<view class="classify-box-i-l-t">
							印章管理
						</view>
						<view class="classify-box-i-l-b flex align-center">
							去查看
							<up-icon name="arrow-right" color="#0096FF" size="16"></up-icon>
						</view>
					</view>
					<image src="/static/image/my/yzgl.png" mode=""></image>
				</view>
				<view @click="goNav('/package/vip/index')" class="classify-box-i flex align-center justify-between">
					<view class="classify-box-i-l">
						<view class="classify-box-i-l-t">
							套餐充值
						</view>
						<view class="classify-box-i-l-b flex align-center">
							{{freeTimes>0?freeTimes+'次':'去充值'}}
							<up-icon name="arrow-right" color="#0096FF" size="16"></up-icon>
						</view>
					</view>
					<image src="/static/image/my/cztc.png" mode=""></image>
				</view>
			</view>
		</view>
		<view class="tools flex align-center justify-center">
			<view class="tools-box">
				<view class="tools-box-title">
					其他服务
				</view>
				<view class="tools-box-i flex align-center justify-between" v-for="(item,index) in tools" :key="index"
					@click="goNav(item.url,item.name)">
					<view class="tools-box-i-l flex align-center">
						<image :src="item.src" mode=""></image>
						<text>{{item.name}}</text>
					</view>
					<up-icon name="arrow-right" color="#2C2C2C" size="16"></up-icon>
				</view>
			</view>
		</view>



		<tabbarComp :newValue="4" />
		<!-- 认证类型选择 -->
		<up-action-sheet :actions="typeList" :title="title" :show="show" :round="24" @select="select"
			@close="close"></up-action-sheet>
	</view>
</template>

<script setup>
	import tabbarComp from '@/components/tabbarComp/tabbarComp'
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const tools = reactive([{
			name: '认证中心',
			src: '/static/image/my/tool/rzzx.png',
			url: '/package/enterprise/renzheng'
		},
		{
			name: '在线客服',
			src: '/static/image/my/tool/zxkf.png',
			url: ''
		},
		{
			name: '邀请好友',
			src: '/static/image/my/tool/yqhy.png',
			url: '/pages/my/invitationUser'
		},
		// {
		// 	name: '团队分享',
		// 	src: '/static/image/my/tool/tdfx.png',
		// 	url: ''
		// },
		{
			name: '问题反馈',
			src: '/static/image/my/tool/yjfk.png',
			url: '/my/feedback/feedback'
		},
		{
			name: '设置中心',
			src: '/static/image/my/tool/setting.png',
			url: '/my/setting/index'
		}
	]);
	const token = ref(''); //登录凭证
	const avatar = ref(''); //头像
	const userName = ref(''); //用户名
	const phone = ref(''); //手机号
	const isAuthentication = ref(-1); //是否认证 1:已认证 0:未认证
	const freeTimes = ref(0); //合同次数
	const title = ref('认证类型');
	const show = ref(false);
	const typeList = ref([{
			name: '个人认证',
			color: '#333333',
			fontSize: '14',
			id: 1
		},
		{
			name: '企业认证',
			color: '#333333',
			fontSize: '14',
			id: 2
		}
	]);
	const compEnter = ref(-1);
	const compId = ref(''); //企业认证id
	onLoad(option => {

	});
	onShow(() => {
		token.value = uni.getStorageSync('token')
		uni.hideTabBar()
		if (token.value) {
			getUserInfo();
			queryInsert();
			enterpriseDetail();
		} else {
			avatar.value = ''
			userName.value = ''
			phone.value = ''
			isAuthentication.value = -1
			compEnter.value = -1
			compId.value = ''
			freeTimes.value = 0
		}
	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});

	const select = (e) => {
		console.log(e.id)
		if (e.id === 1) { //个人
			if (isAuthentication.value != -1) {
				uni.navigateTo({
					url: '/package/enterprise/renzheng?isAuthentication=' + isAuthentication.value
				})
			} else {
				uni.navigateTo({
					url: '/package/enterprise/renzheng'
				})
			}
		} else { //企业
			if (compEnter.value != -1) {
				uni.navigateTo({
					url: '/package/enterprise/certification?id=' + compId.value
				})
			} else {
				uni.navigateTo({
					url: '/package/enterprise/certification'
				})
			}
		}
	};
	//查询认证信息 (企业)
	const enterpriseDetail = () => {
		proxy.$http.getT('/app/enterprise/selectEnterpriseList').then(res => {
			if (res.code == 0) {
				if (res.data.records.length > 0) {
					compId.value = res.data.records[0].id
					compEnter.value = res.data.records[0].status
				} else {
					compEnter.value = -1
				}
			}
		})
	};
	const close = () => {
		show.value = false
	};
	//联系客服
	const selectKeFu = () => {
		let SelKeFu = uni.getStorageSync('SelKeFu');
		if (SelKeFu + '' == '1') { //手机号
			uni.makePhoneCall({
				phoneNumber: uni.getStorageSync('kefuPhone')
			});
		} else if (SelKeFu + '' == '2') { //企业微信
			let that = this
			// #ifdef MP-WEIXIN
			wx.openCustomerServiceChat({
				extInfo: {
					url: uni.getStorageSync('kefu')
				},
				corpId: uni.getStorageSync('kefuAppId'),
				success(res) {}
			})
			// #endif
			// #ifdef H5
			window.location.href = that.$queue.getData('kefu');
			// #endif
			// #ifdef APP
			let kefu = that.$queue.getData('kefu')
			console.log(kefu)
			plus.runtime.openURL(kefu, function(res) {});
			// #endif
		} else { //客服二维码页面
			uni.navigateTo({
				url: '/my/setting/customer'
			})
		}
	};
	//查询认证信息
	const queryInsert = () => {
		proxy.$http.getT('/app/userCertification/queryInsert').then(res => {
			if (res.code == 0) {
				if (res.data) { //提交了认证信息
					isAuthentication.value = res.data.status
				} else {
					isAuthentication.value = -1
				}
			}
		})
	};
	//获取用户信息
	const getUserInfo = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/user/selectUserByUserId', data).then(res => {
			if (res.code == 0) {
				avatar.value = res.data.avatar
				userName.value = res.data.userName
				phone.value = res.data.phone
				freeTimes.value = res.data.freeTimes
			}
		})
	};
	//需要认证的跳转
	const goNavRz = (url) => {
		if (!uni.getStorageSync('token')) {
			uni.navigateTo({
				url: '/pages/public/login'
			})
			return
		}
		if (isAuthentication.value === -1) {
			uni.showModal({
				title: '提示',
				content: '请完成认证后使用~',
				confirmText: '去认证',
				cancelText: '取消',
				complete: (ret) => {
					if (ret.confirm) {
						uni.navigateTo({
							url: '/package/enterprise/renzheng'
						})
					}
				}
			})
			return
		}
		if (isAuthentication.value === 0) {
			uni.showModal({
				title: '提示',
				content: '认证审核中～',
				confirmText: '查看认证',
				cancelText: '取消',
				complete: (ret) => {
					if (ret.confirm) {
						uni.navigateTo({
							url: '/package/enterprise/renzheng?isAuthentication=' +
								isAuthentication.value
						})
					}
				}
			})
			return
		}
		if (isAuthentication.value === 2) {
			uni.showModal({
				title: '提示',
				content: '认证审核被拒',
				confirmText: '重新认证',
				cancelText: '取消',
				complete: (ret) => {
					if (ret.confirm) {
						uni.navigateTo({
							url: '/package/enterprise/renzheng?isAuthentication=' +
								isAuthentication.value
						})
					}
				}
			})
			return
		}
		uni.navigateTo({
			url: url
		})
	};
	const goNav = (url, name) => {
		if (!uni.getStorageSync('token')) {
			uni.navigateTo({
				url: '/pages/public/login'
			})
			return
		}
		if (name == '在线客服') {
			selectKeFu();
		} else if (name == '认证中心') {
			show.value = true
		} else {
			uni.navigateTo({
				url: url
			})
		}


	};
</script>

<style lang="scss" scoped>
	.userInfo {
		width: 100%;
		height: auto;
		background-color: #0096FF;
		padding-top: 50rpx;
		padding-bottom: 120rpx;

		.userInfo-box {
			width: 686rpx;
			height: 100%;

			image {
				width: 88rpx;
				height: 88rpx;
				border-radius: 50%;
				margin-right: 20rpx;
			}
		}

		.userInfo-box-r {
			.userInfo-box-r-name {
				font-weight: 500;
				font-size: 34rpx;
				color: #FFFFFF;

				.userInfo-box-r-name-status {
					font-weight: 400;
					font-size: 20rpx;
					color: #FFFFFF;
					background: #4EB6FF;
					border-radius: 4rpx;
					padding: 4rpx 6rpx;
					margin-left: 10rpx;
				}
			}

			.userInfo-box-r-phone {
				font-weight: 500;
				font-size: 32rpx;
				color: #FFFFFF;
			}
		}
	}

	.classify {
		width: 100%;
		height: auto;
		position: relative;
		margin-top: -63rpx;

		.classify-box {
			width: 686rpx;
			height: 100%;
		}

		.classify-box-i {
			width: calc(333rpx - 60rpx);
			height: 160rpx;
			background-color: #FFFFFF;
			border-radius: 24rpx;
			// padding: 0 20rpx;
			padding: 0 30rpx;

			image {
				width: 96rpx;
				height: 96rpx;
			}

			.classify-box-i-l-t {
				font-weight: 500;
				font-size: 32rpx;
				color: #333333;
			}

			.classify-box-i-l-b {
				font-weight: 400;
				font-size: 26rpx;
				color: #0096FF;
				margin-top: 14rpx;
			}
		}
	}

	.tools {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.tools-box {
			width: calc(686rpx - 60rpx);
			height: 100%;
			background-color: #FFFFFF;
			border-radius: 24rpx;
			padding: 60rpx 30rpx;
		}

		.tools-box-title {
			font-weight: 500;
			font-size: 32rpx;
			color: #2C2C2C;
		}

		.tools-box-i {
			margin-top: 60rpx;

			.tools-box-i-l {
				image {
					width: 38rpx;
					height: 38rpx;
					margin-right: 10rpx;
				}

				text {
					font-weight: 400;
					font-size: 26rpx;
					color: #2C2C2C;
				}
			}
		}
	}
</style>