<template>
	<view class="approval">
		<view class="topBg"></view>
		<!-- <view class="noData" v-if="!enterprise || !enterprise.id">
			<empty contont="暂无企业" />
			<view class="addBtn flex align-center justify-center">
				<view @click="goNav('/package/enterprise/certification')"
					class="addBtn-box flex align-center justify-center">
					<view class="addBtn-box-icon">
						<up-icon name="plus" color="#ffffff" size="20"></up-icon>
					</view>
					创建企业
				</view>
			</view>
		</view> -->
		<view class="noData">
			<!-- <view class="infoData flex align-center justify-center">
				<view class="infoData-box">
					<view class="infoData-box-title flex align-center">
						{{enterprise.enterpriseName}}
						<view class="infoData-box-title-status flex align-center">
							<image src="/static/image/public/gsIcon.png" mode=""></image>
							<text v-if="enterprise.status===0">审核中</text>
							<text v-if="enterprise.status===1">已认证</text>
							<text @click="goNav('/package/enterprise/certification?id='+enterprise.id)"
								v-if="enterprise.status===2">已拒绝</text>
						</view>
					</view>
				</view>
			</view> -->
			<view class="numData flex align-center justify-center">
				<view class="numData-box flex align-center justify-between flex-wrap">
					<view @click="goNav('/package/work/received')" class="numData-box-i">
						<text>0</text>
						<text>我收到的</text>
					</view>
					<view class="numData-box-i">
						<text>0</text>
						<text>我发起的</text>
					</view>
					<view class="numData-box-i">
						<text>0</text>
						<text>待我签署</text>
					</view>
					<view class="numData-box-i">
						<text>0</text>
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
	const isShop = ref(2); //是否是企业
	const enterprise = reactive({});
	onLoad(option => {

	});
	onShow(() => {
		if (uni.getStorageSync('token')) {
			enterpriseDetail();
		} else {
			Object.assign(enterprise, {})
		}
		uni.hideTabBar()
	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
	//查询认证信息 (企业)
	const enterpriseDetail = () => {
		proxy.$http.getT('/app/enterprise/selectEnterpriseList').then(res => {
			if (res.code == 0) {
				if (res.data.records.length > 0) {
					Object.assign(enterprise, res.data.records[0])
					console.log(enterprise, '11111')
				} else {
					Object.assign(enterprise, {})
					// compEnter.value = -1
				}
			}
		})
	};
	const goNav = (url) => {
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