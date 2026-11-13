<template>
	<view style="padding-bottom: 40rpx;">
		<!-- #ifdef H5 -->
		<up-sticky bg-color="#F5F7FF" customNavHeight="0">
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
				<view class="tabss flex align-center justify-center">
					<view class="tabss-box">
						<up-tabs :scrollable="false" :current="current" :list="tabsList" @click="clickTab"></up-tabs>
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

			<tabbarComp :newValue="3" />
	</view>
</template>

<script setup>
	import contractItemAll from '@/components/contractItem/contractItemAll'
	import empty from '@/components/empty/empty'
	import tabbarComp from '@/components/tabbarComp/tabbarComp'
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad,
		onShow,
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
	const tabsList = reactive([{
			name: '全部',
			value: ''
		},
		{
			name: '待签署',
			value: 0
		},
		{
			name: '已完成',
			value: 1
		},
		{
			name: '已过期',
			value: 3
		}
	]);
	const current = ref(0);
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
	onShow(() => {
		uni.hideTabBar()
	});
	const clickTab = (e) => {
		console.log(e)
		current.value = e.index
		page.value = 1
		selectAllContractList();
	};
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
			contractName: keyword.value,
			isComplete: tabsList[current.value].value
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
		padding: 20rpx 0 0 0;

		.searchs-box {
			width: 686rpx;
			height: 100%;

		}
	}

	.tabss {
		width: 100%;
		height: auto;
		margin-bottom: 20rpx;

		.tabss-box {
			width: 100%;
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