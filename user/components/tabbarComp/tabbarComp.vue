<template>
	<view>
		<up-tabbar activeColor="#0096FF" :value="newValue" @change="tabbarClick" :fixed="true" :placeholder="true"
			:safeAreaInsetBottom="true">
			<up-tabbar-item v-for="(item,index) in tabbarList" :key="index" :name="item.name" :text="item.title">
				<template #active-icon>
					<image :class="item.name===2?'tabbarIconC':''" class="tabbarIcon" :src="item.url_"></image>
				</template>
				<template #inactive-icon>
					<image :class="item.name===2?'tabbarIconC':''" class="tabbarIcon" :src="item.url"></image>
				</template>
			</up-tabbar-item>
		</up-tabbar>

		<!-- 弹窗选择 -->
		<up-popup :round="24" :show="show" @close="close" zIndex="99">
			<view class="show">
				<view class="show-list flex align-center justify-around">
					<view class="show-list-i" @click="goNav('/package/template/index')">
						<image src="/static/image/center/mbk.png" mode=""></image>
						<text>从模板库发起</text>
					</view>
					<!-- #ifdef MP-WEIXIN -->

					<!-- #endif -->
					<view class="show-list-i" @click="goNav('/package/contract/wxContract')">
						<image src="/static/image/center/wxwj.png" mode=""></image>
						<text>从文件发起</text>
					</view>

					<view class="show-list-i" @click="goNav('/package/work/received?navType=4')">
						<image src="/static/image/center/cgx.png" mode=""></image>
						<text>从草稿箱发起</text>
					</view>
				</view>
				<view class="show-close flex align-center justify-center">
					<image @click="close()" src="/static/image/center/close.png" mode=""></image>
				</view>
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import {
		reactive,
		ref,
		getCurrentInstance
	} from 'vue';
	const {
		proxy
	} = getCurrentInstance();
	defineProps({
		newValue: {
			type: Number,
			default: 0
		},
	});
	const show = ref(false);
	const tabbarList = reactive([{
			name: 0,
			title: '首页',
			url: '/static/image/tabbar/index.png',
			url_: '/static/image/tabbar/index_.png',
			src: '/pages/index/index'
		},
		{
			name: 1,
			title: '合同管理',
			url: '/static/image/tabbar/approval.png',
			url_: '/static/image/tabbar/approval_.png',
			src: '/pages/approval/approval'
		},
		{
			name: 2,
			title: '',
			url: '/static/image/tabbar/add.png',
			url_: '/static/image/tabbar/add.png',
			src: '/pages/index/index'
		},
		{
			name: 3,
			title: '全部合同',
			url: '/static/image/tabbar/contract.png',
			url_: '/static/image/tabbar/contract_.png',
			src: '/pages/contract/contract'
		},
		{
			name: 4,
			title: '我的',
			url: '/static/image/tabbar/my.png',
			url_: '/static/image/tabbar/my_.png',
			src: '/pages/my/my'
		}
	]);
	const isAuthentication = ref(-1);
	const goNav = (url) => {
		if (uni.getStorageSync('token')) {
			if (isAuthentication.value === -1) {
				uni.showModal({
					title: '提示',
					content: '请完成认证后使用~',
					confirmText: '去认证',
					cancelText: '取消',
					complete: (ret) => {
						if (ret.confirm) {
							uni.navigateTo({
								url: '/package/enterprise/renzheng'
							})
						}
					}
				})
				return
			}
			if (isAuthentication.value === 0) {
				uni.showModal({
					title: '提示',
					content: '认证审核中～',
					confirmText: '查看认证',
					cancelText: '取消',
					complete: (ret) => {
						if (ret.confirm) {
							uni.navigateTo({
								url: '/package/enterprise/renzheng?isAuthentication=' +
									isAuthentication.value
							})
						}
					}
				})
				return
			}
			if (isAuthentication.value === 2) {
				uni.showModal({
					title: '提示',
					content: '认证审核被拒',
					confirmText: '重新认证',
					cancelText: '取消',
					complete: (ret) => {
						if (ret.confirm) {
							uni.navigateTo({
								url: '/package/enterprise/renzheng?isAuthentication=' +
									isAuthentication.value
							})
						}
					}
				})
				return
			}
			uni.navigateTo({
				url
			})
			show.value = false
		} else {
			uni.navigateTo({
				url: '/pages/public/login'
			})
		}
	};
	const close = () => {
		show.value = false
	};
	const tabbarClick = (index) => {
		if (index === 2) {
			if (uni.getStorageSync('token')) {
				queryInsert();
			}
			show.value = true
		} else {
			if (index + '') {
				uni.switchTab({
					url: tabbarList[index].src
				})
			}
		}
	};
	//查询认证信息 (个人)
	const queryInsert = () => {
		proxy.$http.getT('/app/userCertification/queryInsert').then(res => {
			if (res.code == 0) {
				if (res.data) { //提交了认证信息
					isAuthentication.value = res.data.status
				} else {
					isAuthentication.value = -1
				}
			}
		})
	};
</script>

<style lang="scss" scoped>
	.tabbarIcon {
		width: 40rpx;
		height: 40rpx;
	}

	.tabbarIconC {
		width: 70rpx !important;
		height: 70rpx !important;
	}

	.show {
		width: 100%;
		height: 400rpx;
		background-color: #ffffff;
		border-radius: 24rpx 24rpx 0 0;

		.show-list {
			margin-top: 120rpx;
			padding: 0 36rpx;

			.show-list-i {
				display: flex;
				align-items: center;
				justify-content: center;
				flex-direction: column;

				image {
					width: 108rpx;
					height: 108rpx;
					border-radius: 50%;
				}

				text {
					font-weight: 400;
					font-size: 26rpx;
					color: #333333;
					margin-top: 20rpx;
				}
			}
		}

		.show-close {
			margin-top: 100rpx;

			image {
				width: 80rpx;
				height: 80rpx;
				border-radius: 50%;
			}
		}
	}
</style>