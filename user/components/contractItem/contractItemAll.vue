<template>
	<view :style="{border:(index+1) == length?'none':''}" class="contractItem flex align-center justify-between">
		<!-- <image :src="item.userAvatar?item.userAvatar:'/static/logo.png'" mode="aspectFill"></image> -->
		<view class="contractItem-info">
			<view class="contractItem-info-t flex align-center justify-between">
				<text>{{item.contractName?item.contractName:''}}</text>
				<view v-if="item.isComplete===0" class="contractItem-info-t-status">
					待签署
				</view>
				<view v-if="item.isComplete===1" style="background-color: #0096FF;" class="contractItem-info-t-status">
					已完成
				</view>
				<view v-if="item.isComplete===3" style="background-color: #999999;" class="contractItem-info-t-status">
					已过期
				</view>
			</view>
			<view class="contractItem-info-b flex align-center justify-between">
				<text>发起人：{{item.userName?item.userName:''}}</text>
				<text>{{timeFormat(new Date(item.createTime), 'yyyy-mm-dd')}}</text>
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		ref
	} from 'vue';
	import {
		timeFormat
	} from '@/uni_modules/uview-plus';
	defineProps({
		item: {
			type: Object,
			default: () => ({})
		},
		index: {
			type: Number,
			default: 0
		},
		length: {
			type: Number,
			default: 0
		}
	});
</script>

<style lang="scss" scoped>
	.contractItem {
		width: calc(100% - 64rpx);
		height: auto;
		padding: 46rpx 32rpx;
		background-color: #ffffff;
		border-bottom: 1px solid #F5F5F5;
		// margin-top: 20rpx;
		margin-bottom: 20rpx;
		border-radius: 24rpx;

		image {
			width: 88rpx;
			height: 88rpx;
			border-radius: 50%;
		}

		.contractItem-info {
			// width: calc(100% - 17rpx - 88rpx);
			width: 100%;

			.contractItem-info-t {
				text:nth-of-type(1) {
					font-weight: 500;
					font-size: 30rpx;
					color: #333333;
				}

				.contractItem-info-t-status {
					background: #FFB109;
					border-radius: 6rpx;
					font-size: 24rpx;
					color: #FFFFFF;
					padding: 2rpx 6rpx;
				}
			}

			.contractItem-info-b {
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
				margin-top: 8rpx;
			}
		}
	}
</style>