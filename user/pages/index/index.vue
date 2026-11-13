<template>
	<view style="padding-bottom: 40rpx;">
		<view class="topInfo flex align-center justify-center">
			<image src="/static/image/index/topBanner.png" mode="aspectFill"></image>
			<view class="topInfo-box">
				<view @click="goNav('/package/enterprise/renzheng?isAuthentication='+isAuthentication)"
					class="topInfo-box-title flex align-center">
					{{userName?userName:'登录'}}
					<view v-if="token" class="topInfo-box-title-status flex align-center">
						<up-icon name="account-fill" color="#ffffff" size="14"></up-icon>
						<text v-if="isAuthentication===-1">未认证</text>
						<text v-if="isAuthentication===0">审核中</text>
						<text v-if="isAuthentication===1">已通过</text>
						<text v-if="isAuthentication===2">已拒绝</text>
					</view>
				</view>
				<view class="topInfo-box-list flex align-center justify-between">
					<view @click="goNav('/package/work/received?navType=3')" class="topInfo-box-list-i">
						<text>{{WaitForSign || 0}}</text>
						<text>待我签署</text>
					</view>
					<view @click="goNav('/package/work/received?navType=2')" class="topInfo-box-list-i">
						<text>{{mySigned || 0}}</text>
						<text>我发起的</text>
					</view>
					<view @click="goNav('/package/work/received?navType=4')" class="topInfo-box-list-i">
						<text>{{DraftBox || 0}}</text>
						<text>草稿箱</text>
					</view>
				</view>
			</view>
		</view>
		<view class="classify flex align-center justify-center">
			<view class="classify-box flex align-center justify-between">
				<view class="classify-box-i" @click="goNav('/package/template/index')">
					<image src="/static/image/index/htfb.png" mode="aspectFill"></image>
				</view>
				<view class="classify-box-i" @click="goNav('/package/seal/index')">
					<image src="/static/image/index/yzgl.png" mode="aspectFill"></image>
				</view>
			</view>
		</view>
		<view class="fileBox flex align-center justify-center">
			<view class="fileBox-box">
				<view @click="goNav('/package/work/allFile')"
					class="fileBox-box-title flex align-center justify-between">
					<view class="fileBox-box-title-l flex align-center">
						<image src="/static/image/index/wjicon.png" mode=""></image>
						全部文件
					</view>
					<up-icon name="arrow-right" color="#3D3D3D" size="14"></up-icon>
				</view>
				<view class="fileBox-box-title-list">
					<contractItem @tap="goNav('/package/work/detail?id=' + item.id)" :item="item" :index="index"
						:length="2" v-for="(item,index) in contractList" :key="index" />
				</view>
			</view>
		</view>
		<view class="swipers flex align-center justify-center">
			<view class="swipers-box">
				<swiper class="swipers-box" :indicator-dots="false" :autoplay="true" :interval="3000" :duration="300">
					<swiper-item @tap="goNavSwiper(item.url)" class="swipers-box" v-for="(item,index) in swiperList"
						:key="index">
						<image class="swipers-box" :src="item.imageUrl" mode="aspectFill"></image>
					</swiper-item>
				</swiper>
			</view>
		</view>
		<!-- 自定义tabbar -->
		<tabbarComp :newValue="0" />
	</view>
</template>

<script setup>
	import contractItem from '@/components/contractItem/contractItem'
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
	const isAuthentication = ref(-1); //0:审核中 1:已认证 2:已拒绝
	const userName = ref('');
	const token = ref('');
	const swiperList = reactive([]); //轮播图
	const DraftBox = ref(0); //草稿箱
	const mySigned = ref(0); //我发起的
	const WaitForSign = ref(0); // //待我签署
	const contractList = ref([]); //合同列表
	onLoad(option => {
		// if (uni.getStorageSync('token')) {
		// 	selectAllContractList();
		// }
	});
	onShow(() => {
		getBannerList();
		token.value = uni.getStorageSync('token')
		uni.hideTabBar()
		if (uni.getStorageSync('token')) {
			selectAllContractList();
			getUserInfo();
			queryInsert();
			selectMySignature();
		} else {
			userName.value = ''
			isAuthentication.value = -1
		}
	});
	onPullDownRefresh(() => {
		if (uni.getStorageSync('token')) {
			getUserInfo();
			queryInsert();
			selectMySignature();
			selectAllContractList();
		} else {
			uni.stopPullDownRefresh()
		}
	});
	//顶部统计
	const selectMySignature = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/contract/selectMySignature', data).then(res => {
			if (res.code == 0) {
				WaitForSign.value = res.data.WaitForSign; //待我签署
				DraftBox.value = res.data.DraftBox; //草稿箱  
				mySigned.value = res.data.mySigned; //我发起的
			}
		})
	};
	//获取合同列表
	const selectAllContractList = () => {
		let data = {
			page: 1,
			limit: 3,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/contract/selectAllContractList', data).then(res => {
			uni.stopPullDownRefresh()
			if (res.code == 0) {
				contractList.value = res.data.records
			}
		})
	};
	//轮播图跳转
	const goNavSwiper = (url) => {
		console.log(url)
		if (uni.getStorageSync('sendMsg')) {
			console.log('授权+1')
			wx.requestSubscribeMessage({
				tmplIds: this.arr,
				success(re) {
					console.log(JSON.stringify(re), 111111111111)
					var datas = JSON.stringify(re);
					if (datas.indexOf("accept") != -1) {
						console.log(re)
					}
				},
				fail: (res) => {
					console.log(res)
				}
			})
		}
		// 判断是否是应用内链接
		if (url.indexOf('/pages/') !== -1 || url.indexOf('/package/') !== -1) {
			if (uni.getStorageSync('token')) {
				uni.navigateTo({
					url
				});
			} else {
				uni.navigateTo({
					url: '/pages/public/login'
				})
			}

		} else if (url.indexOf('http') !== -1) {
			//#ifndef H5
			uni.navigateTo({
				url: '/pages/webView/webView?url=' + url
			})
			//#endif
			//#ifdef H5
			window.location.href = url;
			//#endif
		} else if (url.indexOf('客服') !== -1) {
			selectKeFu();
		}
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
	//获取轮播图
	const getBannerList = () => {
		let data = {
			classify: 1
		}
		proxy.$http.getT('/app/banner/selectBannerList', data).then(res => {
			if (res.code == 0) {
				swiperList.splice(0, swiperList.length, ...res.data)
			}
		})
	};
	//查询认证信息 (个人)
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
	const getUserInfo = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/user/selectUserByUserId', data).then(res => {
			if (res.code == 0) {
				userName.value = res.data.userName
			}
		})
	};
	const goNav = (url) => {
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
</script>

<style lang="scss" scoped>
	.topInfo {
		width: 100%;
		height: auto;
		// flex-direction: column;
		position: relative;

		image {
			width: 100%;
			height: 410rpx;
		}

		.topInfo-box {
			width: calc(686rpx - 56rpx);
			// height: 266rpx;
			padding: 40rpx 28rpx;
			background-color: #ffffff;
			border-radius: 24rpx;
			position: absolute;
			bottom: 0;
			margin-bottom: -110rpx;

			.topInfo-box-title {
				font-weight: 500;
				font-size: 32rpx;
				color: #333333;

				.topInfo-box-title-status {
					font-weight: 400;
					font-size: 20rpx;
					color: #FFFFFF;
					background: #0096FF;
					border-radius: 4rpx;
					padding: 4rpx 6rpx;
					margin-left: 10rpx;
				}
			}

			.topInfo-box-list {
				margin-top: 28rpx;

				.topInfo-box-list-i {
					width: 190rpx;
					height: 134rpx;
					background-color: #F5FAFF;
					border-radius: 16rpx;
					display: flex;
					align-items: center;
					justify-content: center;
					flex-direction: column;

					text:nth-of-type(1) {
						font-weight: 500;
						font-size: 46rpx;
						color: #333333;
					}

					text:nth-of-type(2) {
						font-weight: 400;
						font-size: 24rpx;
						color: #333333;
					}
				}
			}
		}
	}

	.classify {
		width: 100%;
		height: auto;
		margin-top: 145rpx;

		.classify-box {
			width: 686rpx;
			height: 100%;

			.classify-box-i {
				width: 333rpx;
				height: 160rpx;
				border-radius: 16rpx;

				image {
					width: 100%;
					height: 100%;
					border-radius: 16rpx;
				}
			}
		}
	}

	.fileBox {
		width: 100%;
		height: auto;
		margin-top: 30rpx;

		.fileBox-box {
			width: calc(686rpx - 60rpx);
			height: 100%;
			background-color: #ffffff;
			border-radius: 24rpx;
			padding: 30rpx;
		}

		.fileBox-box-title {
			.fileBox-box-title-l {
				font-weight: 400;
				font-size: 26rpx;
				color: #333333;

				image {
					width: 44rpx;
					height: 48rpx;
				}
			}
		}
	}

	.swipers {
		width: 100%;
		height: 170rpx;
		margin-top: 30rpx;

		.swipers-box {
			width: 686rpx;
			height: 100%;
			border-radius: 24rpx;
		}
	}
</style>