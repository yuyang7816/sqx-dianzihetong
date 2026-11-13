<template>
	<view>
		<view class="topLine"></view>
		<view class="tabList flex align-center justify-between">
			<view class="tabList-l">
				<scroll-view scroll-y="true" style="width: 100%;height: 100%;">
					<view @click="leftCur = index" :class="leftCur == index?'tabList-l-i-active':''"
						class="tabList-l-i flex align-center justify-center" v-for="(item,index) in leftList"
						:key="index">
						{{item.typeName}}
					</view>
				</scroll-view>
			</view>
			<view class="tabList-r">
				<scroll-view
					v-if="leftList.length>0 &&  leftList[leftCur].contractTemplateList && leftList[leftCur].contractTemplateList.length >0"
					scroll-y="true" style="width: 100%;height: 100%;">
					<view class="tabList-r-i flex align-center justify-between"
						v-for="(item,index) in leftList[leftCur].contractTemplateList" :key="index"
						@click="goDetail(item)">
						<view class="tabList-r-i-l">
							<view class="tabList-r-i-l-t">
								{{item.templateName}}
							</view>
							<view v-if="item.remark" class="tabList-r-i-l-b">
								{{item.remark}}
							</view>
						</view>
						<image src="/static/image/public/htIcon.png" mode=""></image>
					</view>
				</scroll-view>
				<empty v-else contont="暂无合同" />
			</view>
		</view>
	</view>
</template>

<script setup>
	import empty from '@/components/empty/empty'
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();
	const leftCur = ref(0);
	const leftList = reactive([])
	onLoad(option => {
		getContractTemplateList()
	});
	//获取模版列表
	const getContractTemplateList = () => {
		proxy.$http.getT('/app/contract/getContractTemplateList').then(res => {
			if (res.code == 0) {
				leftList.splice(0, leftList.length, ...res.data)
			}
		})
	};
	const goDetail = (item) => {
		uni.navigateTo({
			url: '/package/template/detail?id=' + item.id
		})
	};
</script>

<style lang="scss" scoped>
	.topLine {
		width: 100%;
		height: 25rpx;
	}

	.tabList {
		width: 100%;
		height: calc(100vh - 25rpx);
		/* #ifdef H5 */
		height: calc(100vh - 25rpx - var(--window-top));
		/* #endif */

		.tabList-l {
			width: 200rpx;
			height: 100%;
		}

		.tabList-r {
			width: calc(550rpx - 60rpx);
			height: calc(100% - 60rpx);
			background-color: #ffffff;
			padding: 30rpx 30rpx;
		}
	}

	.tabList-l-i-active {
		background-color: #ffffff;
		color: #0096FF !important;
	}

	.tabList-l-i {
		width: 100%;
		height: 95rpx;
		// font-weight: 500;
		font-size: 32rpx;
		color: #333333;
		margin-bottom: 20rpx;
	}

	.tabList-r-i {
		width: calc(100% - 30rpx);
		height: calc(138rpx - 30rpx);
		background: #F4FBFF;
		border-radius: 16rpx;
		margin-bottom: 26rpx;
		padding: 15rpx;

		image {
			width: 96rpx;
			height: 83rpx;
		}

		.tabList-r-i-l-t {
			font-weight: 500;
			font-size: 30rpx;
			color: #1E1E1E;
		}

		.tabList-r-i-l-b {
			font-weight: 400;
			font-size: 24rpx;
			color: #999999;
			margin-top: 10rpx;
		}
	}
</style>