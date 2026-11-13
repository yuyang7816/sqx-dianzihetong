<template>
	<view class="text-black padding-lr container">
		<view class="flex  padding-tb" @click="goNav('/my/setting/logOff')">
			<view class="flex-sub text-df" style="line-height: 50upx;">账号注销</view>
			<u-icon name="arrow-right" color="#333333" size="20"></u-icon>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/feedbackIndex/feedbackIndex')">
			<view class="flex-sub text-df" style="line-height: 50upx;">帮助中心</view>
			<u-icon name="arrow-right" color="#333333" size="20"></u-icon>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/xieyi?type=432')">
			<view class="flex-sub text-df" style="line-height: 50upx;">充值协议</view>
			<u-icon name="arrow-right" color="#333333" size="20"></u-icon>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/xieyi?type=177')">
			<view class="flex-sub text-df" style="line-height: 50upx;">用户协议</view>
			<u-icon name="arrow-right" color="#333333" size="20"></u-icon>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/xieyi?type=176')">
			<view class="flex-sub text-df" style="line-height: 50upx;">隐私政策</view>
			<u-icon name="arrow-right" color="#333333" size="20"></u-icon>
		</view>
		<view class="flex  padding-tb" @click="goNav('/my/setting/xieyi?type=293')">
			<view class="flex-sub text-df" style="line-height: 50upx;">用户注销协议</view>
			<u-icon name="arrow-right" color="#333333" size="20"></u-icon>
		</view>
		<view class="btn flex align-center justify-center">
			<view @click="outLogin" class="btn-box flex align-center justify-center">
				退出登录
			</view>
		</view>
	</view>
</template>

<script setup>
	//退出登录
	const outLogin = () => {
		uni.showModal({
			title: '提示',
			content: '确认退出登录吗？',
			confirmColor: '#0096FF',
			complete(ret) {
				if (ret.confirm) {
					uni.removeStorageSync('token')
					uni.removeStorageSync('avatar')
					uni.removeStorageSync('openId')
					uni.removeStorageSync('unionId')
					uni.removeStorageSync('userName')
					uni.removeStorageSync('phone')
					uni.removeStorageSync('invitationCode')
					uni.removeStorageSync('sex')
					uni.removeStorageSync('userId')
					uni.removeStorageSync('weixinPhone')
					uni.showToast({
						title: '已退出'
					})
					setTimeout(() => {
						uni.navigateBack()
					}, 1000)
				}
			}
		})
	};
	const goNav = (e) => {
		uni.navigateTo({
			url: e
		})
	};
</script>

<style lang="scss">
	.text-white {
		color: #fff;
	}

	.btn {
		width: 100%;
		height: auto;
		margin-top: 40rpx;

		.btn-box {
			width: 686rpx;
			height: 88rpx;
			background-color: #0096FF;
			border-radius: 44rpx;
			color: #ffffff;
			font-weight: bold;
		}
	}
</style>