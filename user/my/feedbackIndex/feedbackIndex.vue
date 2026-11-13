<!-- 帮助反馈页面 -->
<template>
	<view class="container" style="padding-bottom: 140rpx;">
		<view class="bg-list filter">
			<view v-for="(item,index) in helpClassifyList" :key="index" :title="item.helpClassifyName"
				class="list-title padding-bottom">
				<view class="flex align-center justify-between" @click.stop="openList(item)">
					<view class="text-title">{{item.helpClassifyName}}</view>
					<view @click.stop="openList(item)">
						<!-- <image src="../static/up.png" style="width: 21rpx;height: 15rpx;" >
						</image> -->
						<up-icon v-if="item.parentId==0" name="arrow-up" color="#333333" size="14"></up-icon>
						<up-icon v-else name="arrow-down" color="#333333" size="14"></up-icon>
						<!-- <image src="../static/dowm.png" style="width: 21rpx;height: 15rpx;" v-else></image> -->
					</view>
				</view>
				<view v-for="(problemItem,problemIndex) in item.helpWordList" :key="problemIndex" class="list-question"
					hover-class="hover" @click="onClick(problemItem)" v-if="item.parentId==0">
					<view class="text-item">{{problemItem.helpWordTitle}}</view>
					<view class="line" v-if="problemIndex!=item.helpWordList.length-1"></view>
				</view>
			</view>
		</view>
		<view class="bg-box">
			<view class="bg-white-box filter">
				<!-- #ifdef MP-WEIXIN -->
				<view v-if="SelKeFu==3">
					<button type="default" open-type="contact">
						<view class="flex align-center">
							<image src="../static/icon-letter.png" class="image"></image>
							<view class="text-feedback" hover-class="hover">联系客服</view>
						</view>
					</button>
				</view>
				<view v-else class="flex align-center">
					<image src="../static/icon-letter.png" class="image"></image>
					<view class="text-feedback" hover-class="hover" @click="selectKeFu">联系客服</view>
				</view>
				<!-- #endif -->
				<!-- #ifndef MP-WEIXIN -->
				<view class="flex align-center">
					<image src="../static/icon-letter.png" class="image"></image>
					<view class="text-feedback" hover-class="hover" @click="selectKeFu">联系客服</view>
				</view>
				<!-- #endif -->
				<view class="vertical-line"></view>

				<image src="../static/icon-edit.png" class="image"></image>
				<view class="text-feedback" hover-class="hover" @click="toFeedback">我要反馈</view>
			</view>

		</view>
		<empty v-if="helpClassifyList.length == 0" contont="暂无数据" />
	</view>
</template>

<script setup>
	import empty from '@/components/empty/empty'
	import {
		reactive,
		ref,
		getCurrentInstance
	} from 'vue'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	const {
		proxy
	} = getCurrentInstance()
	const helpClassifyList = reactive([]);
	const SelKeFu = ref('1')
	onLoad(option => {
		SelKeFu.value = uni.getStorageSync('SelKeFu')
		getlist()
	});
	const openList = (item) => {
		let oldhelpClassifyList = item
		if (oldhelpClassifyList.parentId == 1) {
			item.parentId = 0
		} else {
			item.parentId = 1
		}
	};
	const getlist = () => {
		proxy.$http.get("/app/help/selectHelpList ").then(res => {
			if (res.code == 0) {
				helpClassifyList.splice(0, helpClassifyList.length, ...res.data)
				helpClassifyList.map(item => {
					item.parentId = 1
				})
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});
	};
	const onClick = (item) => {
		uni.navigateTo({
			url: '/my/helpDetail/helpDetail?helpWordId=' + item.helpWordId
		})

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
	const toFeedback = () => {
		uni.navigateTo({
			url: '/my/feedback/feedback'
		});
	};
</script>

<style>
	button::after {
		border: none;
		background-color: none;
	}

	button {
		display: flex;
		align-items: center;
		position: relative;
		display: block;
		margin-left: auto;
		margin-right: auto;
		padding-left: 0px;
		padding-right: 0px;
		box-sizing: border-box;
		text-decoration: none;
		/* line-height: 96rpx; */
		overflow: hidden;
		color: #FFFFFF;
		/* background-color: #fff; */
		background-color: rgba(255, 255, 255, 0) !important;
		font-size: 32rpx;
		width: 100%;
		height: 100%;
		color: #333333;
	}



	.bg-box {
		/* background-color: #FFFFFF; */
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		border: none !important;
	}

	.bg-list {
		margin-bottom: 100rpx;
		/* background-color: #FFFFFF; */
		padding: 30rpx
	}

	.bg-white-box {
		background-color: #FFFFFF;
		margin: 30rpx;
		display: flex;
		flex-direction: row;
		justify-content: center;
		align-items: center;
		padding: 10rpx;
		border-radius: 20rpx;
		color: #000;
		font-size: 32rpx;
	}

	.vertical-line {
		height: 20rpx;
		background-color: #cecece;
		width: 2rpx;
		margin-left: 30rpx;
		margin-right: 30rpx;
	}

	.line {
		width: 100%;
		height: 1rpx;
		background-color: #d3d3d3;
	}



	.text-title {
		color: #333333;
		font-size: 32rpx;
		font-weight: bold;
	}

	.text-item {
		color: #999999;
		font-size: 28rpx;
		padding: 24rpx 0rpx;
	}

	.list-title {
		margin-bottom: 30rpx;
	}

	.list-question {
		color: #000;
		font-size: 28rpx;
	}

	.hover {
		/* background-color: #ffffff; */
		opacity: 0.6;
	}

	.image {
		width: 40rpx;
		height: 40rpx;
		margin-left: 20rpx;
	}

	.text-feedback {
		padding: 20rpx;
		color: #333333;
		/* color: #000000; */
	}

	.text-top {
		margin: 30rpx;
		color: #000;
		font-size: 34rpx;
	}
</style>