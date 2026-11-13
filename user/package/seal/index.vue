<template>
	<view style="padding-bottom: 150rpx;">
		<view @click.stop.prevent="selectItem(item)" class="info-box" v-for="(item,index) in list" :key="index">
			<view class="info-tit flex align-center justify-between" v-if="item.sealUrl">
				个人印章
				<view class="info-tit-r" @click.stop.prevent="deleteSeal(item)">
					<up-icon name="trash" color="#999999" size="24"></up-icon>
				</view>
			</view>
			<view class="info-tit flex align-center justify-between" v-if="item.companySeal">
				公司印章
				<view class="info-tit-r" @click.stop.prevent="deleteSeal(item)">
					<up-icon name="trash" color="#999999" size="24"></up-icon>
				</view>
			</view>
			<view class="info-img" v-if="item.sealUrl">
				<image :src="item.sealUrl" mode="widthFix"></image>
			</view>
			<view class="info-img" v-if="item.companySeal">
				<image :src="item.companySeal" mode="widthFix"></image>
			</view>
		</view>
		<view class="taber">
			<view class="btn" @click="goNav('/package/seal/updete')">编辑个人印章</view>
			<view class="btn" v-if="isComp == false" style="margin-left: 10rpx;" @click="goComp()">添加企业印章</view>
		</view>
		<empty v-if="list.length==0" contont="暂无印章" />
	</view>
</template>

<script setup>
	import empty from '@/components/empty/empty'
	import {
		getCurrentInstance,
		reactive,
		ref
	} from 'vue'
	import {
		onLoad,
		onShow,
		onPullDownRefresh,
		onReachBottom
	} from '@dcloudio/uni-app'
	const {
		proxy
	} = getCurrentInstance()
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	const list = reactive([]);
	const compEnter = ref(-1); //企业认证  //-1:未认证 0:待审核 1:已通过 2:已拒绝
	const compId = ref('');
	const enterpriseName = ref(''); //公司名称
	const type = ref(-1); //是否是选择跳转 1:企业 2:个人 -1:不跳转
	const isComp = ref(false);
	const signType = ref(1); //1:签署印章 2:骑缝印章
	onLoad(option => {
		if (option.type) {
			type.value = Number(option.type)
			signType.value = Number(option.signType)
		}
	});
	onShow(() => {
		page.value = 1
		getList();
		enterpriseDetail();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			getList();
		}
	});
	onPullDownRefresh(() => {
		page.value = 1
		getList();
		enterpriseDetail();
	});
	//选择印章
	const selectItem = (item) => {
		if (type.value === 1) { //企业
			if (item.companySeal) { //当前选择是企业印章
				if (signType.value === 1) { //普通印章
					uni.$emit('selectSeal', item)
				} else { //骑缝印
					uni.$emit('selectSeamSeal', item)
				}
				uni.navigateBack()
			} else if (item.sealUrl) { //当前选择是个人印章
				uni.showToast({
					title: '请选择企业印章',
					icon: 'none'
				})
			}
		} else if (type.value === 2) { //个人
			if (item.companySeal) { //当前选择是企业印章
				uni.showToast({
					title: '请选择个人印章',
					icon: 'none'
				})
			} else if (item.sealUrl) { //当前选择是个人印章
				// uni.$emit('selectSeal', item)
				if (signType.value === 1) { //普通印章
					uni.$emit('selectSeal', item)
				} else { //骑缝印
					uni.$emit('selectSeamSeal', item)
				}
				uni.navigateBack()
			}
		}
		// else if (type.value == 'seam') { //骑缝印
		// 	uni.$emit('selectSeamSeal', item)
		// 	uni.navigateBack()
		// } 
		else { //不选择
			return
		}
	};
	//跳转企业印章
	const goComp = () => {
		if (compEnter.value === -1) { //未认证
			uni.navigateTo({
				url: '/package/enterprise/certification'
			})
		} else { //认证了
			if (compEnter.value === 0) {
				uni.showToast({
					title: '企业认证审核中...',
					icon: 'none'
				})
			} else if (compEnter.value === 1) { //已通过
				uni.navigateTo({
					url: '/package/seal/updeteComp?enterpriseName=' + enterpriseName.value
				})
			} else { //已拒绝
				uni.showModal({
					title: '提示',
					content: '企业认证未通过',
					confirmText: '重新提交',
					complete: (ret) => {
						if (ret.confirm) {
							uni.navigateTo({
								url: '/package/enterprise/certification?id=' + compId.value
							})
						}
					}
				})
			}
		}
	};
	//查询认证信息 (企业)
	const enterpriseDetail = () => {
		proxy.$http.getT('/app/enterprise/selectEnterpriseList').then(res => {
			if (res.code == 0) {
				if (res.data.records.length > 0) {
					compEnter.value = res.data.records[0].status
					compId.value = res.data.records[0].id
					enterpriseName.value = res.data.records[0].enterpriseName
				} else {
					compEnter.value = -1
				}
			}
		})
	};
	const deleteSeal = (item) => {
		uni.showModal({
			title: '提示',
			content: '确认删除印章吗？',
			complete: (ret) => {
				if (ret.confirm) {
					let data = {
						id: item.id,
						userId: uni.getStorageSync('userId')
					}
					uni.showLoading({
						title: '删除中...'
					})
					proxy.$http.postT('/app/contractSeal/deleteContractSeal', data).then(res => {
						if (res.code == 0) {
							uni.hideLoading()
							uni.showToast({
								title: '已删除'
							})
							page.value = 1
							getList();
							enterpriseDetail();
						} else {
							uni.hideLoading()
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				}
			}
		})
	};
	//获取印章列表
	const getList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/contractSeal/selectContractSealList', data).then(res => {
			uni.stopPullDownRefresh()
			if (res.code == 0) {
				pages.value = res.data.pages
				let indexs = res.data.records.findIndex(item => item.companySeal)
				if (indexs != -1) { //有企业印章
					isComp.value = true
				} else { //没有
					isComp.value = false
				}
				// item.companySeal
				if (page.value === 1) {
					list.splice(0, list.length, ...res.data.records)
				} else {
					list.push(res.data.records)
				}
			}
		})
	}
	const goNav = (url) => {
		uni.navigateTo({
			url
		})
	}
</script>

<style lang="scss" scoped>
	.info-box {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.info-tit {
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
		}

		.info-img {
			width: 624rpx;
			background: #F5F5F5;
			border-radius: 10rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			margin: 40rpx 0;
			position: relative;
			padding: 30rpx 0;

			.mricon {
				position: absolute;
				left: 0;
				top: 20rpx;
				width: 72rpx;
				height: 36rpx;
				background: #0096FF;
				border-radius: 0rpx 18rpx 18rpx 0rpx;
				font-weight: 400;
				font-size: 24rpx;
				color: #FFFFFF;
				display: flex;
				align-items: center;
				justify-content: center;
			}

			image {
				width: 200rpx;
			}
		}
	}

	.taber {
		background: #FFFFFF;
		padding: 30rpx;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		z-index: 99;
		display: flex;
		align-items: center;
		justify-content: space-between;

		.btn {
			// width: 340rpx;
			flex: 1;
			height: 88rpx;
			background: #0096FF;
			border-radius: 16rpx;
			font-weight: 500;
			font-size: 30rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
		}
	}
</style>