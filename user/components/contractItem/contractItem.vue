<template>
	<view :style="{border:(index+1) == length?'none':''}" class="contractItem flex align-center justify-between">
		<!-- <image :src="item.userAvatar?item.userAvatar:'/static/logo.png'" mode="aspectFill"></image> -->
		<view class="contractItem-info">
			<view class="contractItem-info-t flex align-center justify-between">
				<view class="contractItem-info-t-txt">{{item.contractName}}</view>
				<view class="contractItem-info-t-txt">{{timeFormat(new Date(item.createTime), 'yyyy-mm-dd')}}</view>
			</view>
			<view class="contractItem-info-b flex align-center justify-between">
				发起人：{{item.userName?item.userName:''}}
				<view v-if="navType == 4" @click.stop.prevent="deleteItem"
					class="contractItem-info-b-d flex align-center justify-center">
					删除
				</view>
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
	const emit = defineEmits(['delete']);
	const {
		item,
		navType,
		index,
		length
	} = defineProps({
		item: {
			type: Object,
			default: () => ({})
		},
		//1:我收到的 2:我发起的 3:待我签署 4:草稿箱
		navType: {
			type: [Number, String],
			default: ''
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
	const deleteItem = () => {
		emit('delete', item);
	};
</script>

<style lang="scss" scoped>
	.contractItem {
		width: 100%;
		height: auto;
		padding: 46rpx 0;
		background-color: #ffffff;
		border-bottom: 1px solid #F5F5F5;

		image {
			width: 88rpx;
			height: 88rpx;
			border-radius: 50%;
		}

		.contractItem-info {
			// width: calc(100% - 17rpx - 88rpx);
			width: 100%;

			.contractItem-info-t {
				.contractItem-info-t-txt:nth-of-type(1) {
					width: 350rpx;
					font-weight: 500;
					font-size: 30rpx;
					color: #333333;
					overflow-wrap: break-word;
					word-break: break-all;
					/* 可选，根据需求决定是否激进断开 */
				}

				.contractItem-info-t-txt:nth-of-type(2) {
					font-weight: 400;
					font-size: 24rpx;
					color: #999999;
				}
			}

			.contractItem-info-b {
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
				margin-top: 8rpx;
			}

			.contractItem-info-b-d {
				width: 100rpx;
				height: 50rpx;
				background-color: #999999;
				color: #ffffff;
				font-size: 26rpx;
				border-radius: 16rpx;
			}
		}
	}
</style>