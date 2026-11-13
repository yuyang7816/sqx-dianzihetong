<template>
	<view>
		<view class="topInfo flex align-center justify-center">
			<view class="topInfo-box">
				<view class="topInfo-box-title flex align-center justify-between">
					<text>
						住宅租赁合同
					</text>
					<text>
						2024-03-18
					</text>
				</view>
				<view class="topInfo-box-name">
					发起人：张丽丽
				</view>
				<view class="topInfo-box-list flex align-center justify-between flex-wrap">
					<image src="/static/logo.png" mode="widthFix"></image>
					<image src="/static/logo.png" mode="widthFix"></image>
					<image src="/static/logo.png" mode="widthFix"></image>
					<image src="/static/logo.png" mode="widthFix"></image>
				</view>
			</view>
		</view>
		<view class="users flex align-center justify-center">
			<view class="users-box">
				<view class="users-box-title">
					收件人
				</view>
				<view class="users-box-item flex align-center justify-between">
					<view class="users-box-item-l flex align-center">
						<image src="/static/logo.png" mode="aspectFill"></image>
						<view class="users-box-item-l-r">
							<view class="users-box-item-l-r-n">
								8829447272@qq.com
							</view>
							<view class="users-box-item-l-r-b">
								未签署
							</view>
						</view>
					</view>
					<view @click="show = true" class="users-box-item-r flex align-center justify-center">
						立即签署
					</view>
				</view>
				<view class="users-box-item flex align-center justify-between">
					<view class="users-box-item-l flex align-center">
						<image src="/static/logo.png" mode="aspectFill"></image>
						<view class="users-box-item-l-r">
							<view class="users-box-item-l-r-n">
								8829447272@qq.com
							</view>
							<view class="users-box-item-l-r-b">
								已签署
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		<up-popup round="24" :show="show" @close="close" closeable>
			<view class="sign">
				<view class="sign-title">
					请输入签名
				</view>
				<view class="sign-contont">
					<up-signature ref="signature" :width="686" :height="120" bg-color="#ffffff" :show-toolbar="false"
						@confirm="onConfirm" @error="onError"></up-signature>
				</view>
				<view class="sign-btn flex align-center justify-center">
					<view @click="cleanSign()"
						style="border-color: #999999;color: #999999;background-color: transparent;margin-right: 34rpx;"
						class="sign-btn-i flex align-center justify-center">
						重新输入
					</view>
					<view class="sign-btn-i flex align-center justify-center">
						确定签名
					</view>
				</view>
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';

	const show = ref(false);
	const signature = ref(null);
	const close = () => {
		show.value = false
	};
	const onConfirm = (tempFilePath) => {
		console.log('签名图片路径:', tempFilePath);
	};

	const onError = (err) => {
		console.error('签名导出错误:', err);
		uni.showToast({
			title: '签名导出失败',
			icon: 'none'
		});
	};
	//清空签名
	const cleanSign = () => {
		console.log(signature.value)
		signature.value.clearCanvas()
	};
</script>

<style lang="scss">
	.topInfo {
		width: 100%;
		height: auto;
		background: linear-gradient(to bottom, #0096FF, #F5F7FF);
		padding-top: 20rpx;

		.topInfo-box {
			width: calc(686rpx - 64rpx);
			height: 100%;
			background-color: #ffffff;
			border-radius: 24rpx;
			padding: 32rpx;
		}

		.topInfo-box-title {
			text:nth-of-type(1) {
				font-weight: 500;
				font-size: 30rpx;
				color: #333333;
			}

			text:nth-of-type(2) {
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
			}
		}

		.topInfo-box-name {
			font-weight: 400;
			font-size: 24rpx;
			color: #999999;
			margin-top: 10rpx;
		}

		.topInfo-box-list {
			margin-top: 10rpx;

			image {
				width: 194rpx;
				height: auto;
				margin-top: 20rpx;
			}
		}
	}

	.users {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.users-box {
			width: calc(686rpx - 64rpx);
			height: 100%;
			padding: 32rpx;
			background-color: #ffffff;
			border-radius: 24rpx;
		}

		.users-box-title {
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
		}

		.users-box-item {
			margin-top: 50rpx;

			.users-box-item-l {
				image {
					width: 60rpx;
					height: 60rpx;
					border-radius: 50%;
					margin-right: 14rpx;
				}

				.users-box-item-l-r-n {
					font-weight: 400;
					font-size: 28rpx;
					color: #333333;
				}

				.users-box-item-l-r-b {
					font-weight: 400;
					font-size: 24rpx;
					color: #0096FF;
				}
			}

			.users-box-item-r {
				width: 160rpx;
				height: 66rpx;
				background: #0096FF;
				border-radius: 10rpx;
				font-weight: 400;
				font-size: 26rpx;
				color: #FFFFFF;
			}
		}
	}

	.sign {
		width: 100%;

		.sign-title {
			width: 200rpx;
			padding-left: 32rpx;
			margin-top: 32rpx;
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
		}

		.sign-contont {
			width: 686rpx;
			// padding: 0 32rpx;
			border: 1px solid #999999;
			margin: 60rpx auto;
		}

		.sign-btn {
			width: 100%;
			height: auto;
			margin-bottom: 44rpx;

			.sign-btn-i {
				width: 280rpx;
				height: 77rpx;
				background: #0096FF;
				border-radius: 10rpx;
				border: 1px solid #0096FF;
				font-size: 26rpx;
				color: #FFFFFF;
			}
		}
	}
</style>