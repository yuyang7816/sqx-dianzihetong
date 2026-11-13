<template>
	<view style="padding-bottom: 40rpx;">
		<!-- #ifdef H5 -->
		<up-sticky bg-color="#F5F7FF" custom-nav-height="0">
		<!-- #endif -->
			<!-- #ifndef H5 -->
			<up-sticky bg-color="#F5F7FF">
			<!-- #endif -->

				<view class="searchs flex align-center justify-center">
					<view class="searchs-box">
						<up-search placeholder="请输入文件名称" bgColor="#ffffff" :showAction="false" v-model="keyword"
							@search="search" @clear="search"></up-search>
					</view>
				</view>
			</up-sticky>

			<view class="lists flex align-center justify-center">
				<view class="lists-box">
					<contractItemAll @tap="clickSelect(item)" :item="item" :index="index" :length="10"
						v-for="(item,index) in list" :key="index" />
				</view>
			</view>
			<empty v-if="list.length == 0" contont="暂无文件" />
	</view>
</template>

<script setup>
	import contractItemAll from '@/components/contractItem/contractItemAll'
	import empty from '@/components/empty/empty'
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onReachBottom,
		onPullDownRefresh
	} from '@dcloudio/uni-app'
	const {
		proxy
	} = getCurrentInstance();

	const keyword = ref('');
	const page = ref(1);
	const pages = ref(1);
	const limit = ref(10);
	const list = reactive([]);
	onLoad(option => {
		uni.showLoading({
			title: '加载中...'
		})
		selectAllContractList();
	});
	onPullDownRefresh(() => {
		page.value = 1
		selectAllContractList();
	});
	onReachBottom(() => {
		if (page.value < pages.value) {
			page.value += 1
			selectAllContractList();
		}
	});
	const clickSelect = (item) => {
		// uni.navigateTo({
		// 	url: '/package/work/sign'
		// })
		uni.navigateTo({
			url: '/package/work/detail?id=' + item.id
		})
	};
	//搜索
	const search = () => {
		page.value = 1
		uni.showLoading({
			title: '加载中...'
		})
		selectAllContractList();
	};
	//获取合同列表
	const selectAllContractList = () => {
		let data = {
			page: page.value,
			limit: limit.value,
			userId: uni.getStorageSync('userId'),
			contractName: keyword.value
		}
		proxy.$http.getT('/app/contract/selectAllContractList', data).then(res => {
			uni.hideLoading()
			uni.stopPullDownRefresh()
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
</script>

<style lang="scss">
	.searchs {
		width: 100%;
		height: auto;
		padding: 20rpx 0;

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
	}
</style>