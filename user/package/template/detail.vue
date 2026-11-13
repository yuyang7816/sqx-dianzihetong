<template>
	<view style="padding-bottom: 180rpx;">
		<view class="yulan flex align-center justify-center">
			<view class="yulan-box flex align-center justify-center">
				<image @click="prewImg(index,contractImg)" :src="item" v-for="(item,index) in contractImg" :key="index"
					mode="widthFix"></image>
			</view>
			<!-- <up-pdf-reader :src="pdfUrl" height="100vh"></up-pdf-reader> -->
		</view>
		<!-- <up-pdf-reader :src="pdfUrl" :base-url="pdfJsBaseUrl" height="100vh"></up-pdf-reader> -->

		<view class="taber" @click="goNav('/package/contract/index?id='+id+'&templateName='+templateName)">
			<view class="btn">创建合同</view>
		</view>
	</view>
</template>

<script setup>
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
	const id = ref('');
	const contractImg = reactive([]); //预览图片
	const templateName = ref('');
	onLoad(option => {
		if (option.id) {
			id.value = option.id
			getContractTemplate();
		}
	});
	//预览图片
	const prewImg = (index, imgs) => {
		uni.previewImage({
			urls: imgs,
			current: index
		})
	};
	//获取模版详情
	const getContractTemplate = () => {
		let data = {
			id: id.value
		}
		proxy.$http.getT('/app/contract/getContractTemplate', data).then(res => {
			if (res.code == 0) {
				templateName.value = res.data.templateName
				if (res.data.contractImg) {
					contractImg.splice(0, contractImg.length, ...res.data.contractImg.split(','))
				}
			}
		})
	};
	const goNav = (url) => {
		uni.navigateTo({
			url: url
		})
	}
</script>

<style lang="scss">
	.yulan {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.yulan-box {
			flex-direction: column;
			width: 686rpx;

			image {
				width: 100%;
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

		.btn {
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