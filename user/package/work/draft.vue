<template>
	<view style="padding-bottom: 40rpx;">
		<view class="searchs flex align-center justify-center">
			<view class="searchs-box">
				<up-search placeholder="请输入文件名称" bgColor="#ffffff" :showAction="false" v-model="keyword"></up-search>
			</view>
		</view>
		<view class="lists flex align-center justify-center">
			<view class="lists-box">
				<view class="lists-box-i" v-for="(item,index) in 10" :key="index">
					<view class="lists-box-i-title flex align-center justify-between">
						<view class="lists-box-i-title-l">
							买卖合同
						</view>
						<view class="lists-box-i-title-r">
							待签署
						</view>
					</view>
					<view class="lists-box-i-txt">
						<text>合同名称：</text>
						<text>住宅租赁合同</text>
					</view>
					<view class="lists-box-i-txt flex align-center">
						<text>收款方：</text>
						<text>周洲子</text>
						<view class="lists-box-i-txt-tips">发起人</view>
					</view>
					<view class="lists-box-i-txt">
						<text>付款方：</text>
						<text>张丽丽</text>
					</view>
					<view class="lists-box-i-txt">
						<text>发起时间：</text>
						<text>2024-03-18</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';

	const keyword = ref('');
</script>

<style lang="scss">
	.searchs {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.searchs-box {
			width: 686rpx;
			height: 100%;

		}
	}

	.lists {
		width: 100%;
		height: auto;

		.lists-box {
			width: 686rpx;
			height: 100%;
		}

		.lists-box-i {
			width: calc(100% - 64rpx);
			height: 100%;
			padding: 32rpx;
			background: linear-gradient(to bottom, rgba(90, 187, 255, 0.2), #ffffff);
			margin-top: 20rpx;
			border-radius: 18rpx;
		}

		.lists-box-i-title-l {
			font-weight: 400;
			font-size: 26rpx;
			color: #333333;
		}

		.lists-box-i-title-r {
			font-weight: 400;
			font-size: 24rpx;
			color: #FFFFFF;
			background: #FFB109;
			border-radius: 4rpx;
			padding: 2rpx 4rpx;
		}

		.lists-box-i-txt {
			margin-top: 33rpx;

			text:nth-of-type(1) {
				font-size: 26rpx;
				color: #999999;
			}

			text:nth-of-type(2) {
				font-size: 30rpx;
				color: #333333;
			}

			.lists-box-i-txt-tips {
				background: #0096FF;
				border-radius: 4rpx;
				font-size: 24rpx;
				color: #FFFFFF;
				padding: 2rpx 6rpx;
				margin-left: 20rpx;
			}
		}
	}
</style>