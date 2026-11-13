<template>
	<view class="approval">
		<view class="topBg"></view>
		<view class="noData">
			<view class="numData flex align-center justify-center">
				<view class="numData-box flex align-center justify-between flex-wrap">
					<view @click="goNav('/package/work/received?navType=1')" class="numData-box-i">
						<text>{{myReceive || 0}}</text>
						<text>我收到的</text>
					</view>
					<view @click="goNav('/package/work/received?navType=2')" class="numData-box-i">
						<text>{{mySigned || 0}}</text>
						<text>我发起的</text>
					</view>
					<view @click="goNav('/package/work/received?navType=3')" class="numData-box-i">
						<text>{{WaitForSign || 0}}</text>
						<text>待我签署</text>
					</view>
					<view @click="goNav('/package/work/received?navType=4')" class="numData-box-i">
						<text>{{DraftBox || 0}}</text>
						<text>草稿箱</text>
					</view>
				</view>
			</view>
		</view>

		<tabbarComp :newValue="1" />
	</view>
</template>

<script setup>
	import tabbarComp from '@/components/tabbarComp/tabbarComp'
	import empty from '@/components/empty/empty'
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
	const WaitForSign = ref(0); //待我签署
	const DraftBox = ref(0); //草稿箱  
	const mySigned = ref(0); //我发起的
	const myReceive = ref(0); //我收到的
	onLoad(option => {

	});
	onShow(() => {
		if (uni.getStorageSync('token')) {
			selectMySignature();
		} else {
			WaitForSign.value = 0; //待我签署
			DraftBox.value = 0; //草稿箱  
			mySigned.value = 0; //我发起的
			myReceive.value = 0; //我收到的
		}
		uni.hideTabBar()
	});
	onPullDownRefresh(() => {
		if (uni.getStorageSync('token')) {
			selectMySignature();
		} else {
			setTimeout(() => {
				uni.stopPullDownRefresh()
			}, 1000)
		}
	});
	//获取统计数量
	const selectMySignature = () => {
		let data = {
			userId: uni.getStorageSync('userId')
		}
		uni.showLoading({
			title: '加载中...'
		})
		proxy.$http.getT('/app/contract/selectMySignature', data).then(res => {
			uni.stopPullDownRefresh();
			uni.hideLoading()
			if (res.code == 0) {
				WaitForSign.value = res.data.WaitForSign; //待我签署
				DraftBox.value = res.data.DraftBox; //草稿箱  
				mySigned.value = res.data.mySigned; //我发起的
				myReceive.value = res.data.myReceive; //我收到的
			} else {
				uni.hideLoading()
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
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
		uni.navigateTo({
			url: url
		})
	};
</script>

<style lang="scss">
	page {
		background-color: #ffffff;
	}

	.topBg {
		width: 100%;
		height: 50rpx;
		background-color: #0096FF;
	}

	.noData {
		width: 100%;
		background-color: #ffffff;
		border-radius: 24rpx 24rpx 0 0;
		position: relative;
		margin-top: -30rpx;
	}

	.addBtn {
		width: 100%;
		height: auto;
		margin-top: 80rpx;

		.addBtn-box {
			width: 460rpx;
			height: 110rpx;
			background: #0096FF;
			border-radius: 24rpx;
			color: #ffffff;
		}

		.addBtn-box-icon {
			margin-right: 10rpx;

			text {
				font-weight: 800 !important;
			}
		}
	}

	.infoData {
		width: 100%;
		height: auto;
		// margin-top: 32rpx;
		padding-top: 32rpx;

		.infoData-box {
			width: 686rpx;
			height: 100%;
		}

		.infoData-box-title {
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;

			.infoData-box-title-status {
				margin-left: 14rpx;
				padding: 4rpx 6rpx;
				background: #0096FF;
				border-radius: 6rpx;
				font-weight: 500;
				font-size: 20rpx;
				color: #FFFFFF;

				image {
					width: 18rpx;
					height: 20rpx;
					margin-right: 6rpx;
				}
			}
		}
	}

	.numData {
		width: 100%;
		height: auto;
		padding-top: 20rpx;

		.numData-box {
			width: 686rpx;
			height: 100%;
		}

		.numData-box-i {
			width: 333rpx;
			height: 131rpx;
			background: #F5FAFF;
			border-radius: 16rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;
			margin-top: 20rpx;

			text:nth-of-type(1) {
				font-weight: 500;
				font-size: 46rpx;
				color: #1E1E1E;
			}

			text:nth-of-type(2) {
				font-weight: 400;
				font-size: 24rpx;
				color: #1E1E1E;
			}
		}
	}
</style>