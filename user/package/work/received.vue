<template>
	<view class="approval">
		<view class="topBg"></view>
		<view class="noData">
			<!-- <view class="infoData flex align-center justify-center">
				<view class="infoData-box">
					<view class="infoData-box-title flex align-center">
						西安省钱兄网络科技有限公司
						<view class="infoData-box-title-status flex align-center">
							<image src="/static/image/public/gsIcon.png" mode=""></image>
							已认证
						</view>
					</view>
				</view>
			</view> -->
			<view class="list flex align-center justify-center">
				<view class="list-box">
					<contractItem @delete="deleteItem" :navType="navType" @tap="clickSelect(item)" :item="item"
						:index="index" :length="2" v-for="(item,index) in list" :key="index" />
				</view>
			</view>
		</view>
		<empty v-if="list.length == 0" contont="暂无合同" />

	</view>
</template>

<script setup>
	import contractItem from '@/components/contractItem/contractItem'
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
	const navType = ref(0); // 1:我收到的 2:我发起的 3:待我签署 4:草稿箱
	const list = reactive([]); //合同列表
	const page = ref(1);
	const limit = ref(10);
	const pages = ref(1);
	onLoad(option => {
		uni.showLoading({
			title: '加载中...'
		})
		navType.value = Number(option.navType)
		setTitleName();
	});
	onShow(() => {
		uni.hideTabBar()
	});
	onPullDownRefresh(() => {
		page.value = 1
		setTitleName();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			setTitleName();
		}
	});
	const deleteItem = (item) => {
		uni.showModal({
			title: '提示',
			content: '确认删除该草稿吗?',
			complete: (ret) => {
				if (ret.confirm) {
					let data = {
						contractId: item.id
					}
					uni.showLoading({
						title: '删除中...'
					})
					proxy.$http.postJson('/app/contract/deleteDraftBox', data).then(res => {
						uni.hideLoading()
						if (res.code == 0) {
							uni.showToast({
								title: '已删除'
							})
							page.value = 1
							setTitleName();
						} else {
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				}
			}
		})
		console.log(item)
	};
	//获取待我签署合同列表
	const selectSignWait = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/contract/selectSignWait', data).then(res => {
			uni.stopPullDownRefresh()
			uni.hideLoading()
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.records)
				} else {
					list.push(...res.data.records)
				}
			}
		})
	};
	//获取草稿箱的合同列表
	const selectDraftBox = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/contract/selectDraftBox', data).then(res => {
			uni.stopPullDownRefresh()
			uni.hideLoading()
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.records)
				} else {
					list.push(...res.data.records)
				}
			}
		})
	};
	//获取我收到的合同列表
	const selectMyReceive = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/contract/selectMyReceive', data).then(res => {
			uni.stopPullDownRefresh()
			uni.hideLoading()
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.records)
				} else {
					list.push(...res.data.records)
				}
			}
		})
	};
	//获取我发起的合同列表
	const selectMySigned = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/contract/selectMySigned', data).then(res => {
			uni.stopPullDownRefresh()
			uni.hideLoading()
			if (res.code == 0) {
				pages.value = res.data.pages
				if (page.value == 1) {
					list.splice(0, list.length, ...res.data.records)
				} else {
					list.push(...res.data.records)
				}
			}
		})
	};
	//设置页名称
	const setTitleName = () => {
		switch (navType.value) {
			case 1:
				uni.setNavigationBarTitle({
					title: '我收到的'
				})
				selectMyReceive();
				break;
			case 2:
				uni.setNavigationBarTitle({
					title: '我发起的'
				})
				selectMySigned();
				break;
			case 3:
				uni.setNavigationBarTitle({
					title: '待我签署'
				})
				selectSignWait();
				break;
			case 4:
				uni.setNavigationBarTitle({
					title: '草稿箱'
				})
				selectDraftBox();
				break;
			default:
				break;
		}
	};
	const clickSelect = (item) => {
		// uni.navigateTo({
		// 	url: '/package/work/sign'
		// })
		if (navType.value === 4) {
			if (item.contractType === 2) { //从微信创建
				uni.navigateTo({
					url: '/package/contract/wxContract?detailId=' + item.id
				})
			} else {
				uni.navigateTo({
					url: '/package/contract/index?id=' + item.templateId + '&detailId=' + item.id +
						'&templateName=' + item.contractName
				})
			}

		} else {
			uni.navigateTo({
				url: '/package/work/detail?id=' + item.id
			})
		}

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

	.list {
		width: 100%;
		height: auto;

		.list-box {
			width: 686rpx;
			height: 100%;
		}


	}
</style>