<template>
	<view>
		<view class="info-box">
			<view class="info-tit">个人印章</view>
			<SealSelector v-if="userName" @imageGenerated="imageGenerated" ref="sealSelector" :name="userName" />
		</view>

		<view class="taber">
			<view class="btn" @click="saveSeal()">保存</view>
		</view>
	</view>
</template>

<script setup>
	import config from '../../common/config';
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	import SealSelector from '@/components/SealSelector/SealSelector';
	const {
		proxy
	} = getCurrentInstance();
	const userName = ref('')
	const sealSelector = ref(null);
	onLoad(option => {
		queryInsert();
	});
	//查询认证信息
	const queryInsert = () => {
		proxy.$http.getT('/app/userCertification/queryInsert').then(res => {
			if (res.code == 0) {
				userName.value = res.data.name
			}
		})
	};
	const saveSeal = () => {
		if (sealSelector.value) {
			sealSelector.value.generateImage()
		}
	};
	const imageGenerated = (fileUrl) => {
		console.log(fileUrl)
		uni.showLoading({
			title: '保存中...'
		})
		let fileSrc = ''
		// #ifdef H5
		fileSrc = new File([fileUrl], 'seal.png', {
			type: 'image/png'
		});
		// #endif
		// #ifndef H5
		fileSrc = fileUrl
		// #endif
		uni.uploadFile({
			url: config.APIHOST + '/alioss/upload', //真实的接口地址
			// url: '' // 填写自己的上传接口地址
			// #ifdef H5
			file: fileSrc,
			filePath: 'seal.png',
			// #endif
			// #ifndef H5
			filePath: fileSrc,
			// #endif
			header: {
				token: uni.getStorageSync('token')
			},
			name: 'file',
			fail: (ree) => {
				console.log(ree)
			},
			success: uploadFileRes => {
				console.log(uploadFileRes)
				let url = JSON.parse(uploadFileRes.data)
				if (url.code == 0) {
					let imgUrl = url.data
					let data = {
						userId: uni.getStorageSync('userId'),
						sealUrl: imgUrl,
						signType: 2
					}
					proxy.$http.postJson('/app/contractSeal/addContractSeal', data).then(res => {
						if (res.code == 0) {
							uni.hideLoading();
							uni.showToast({
								title: '已保存'
							})
							setTimeout(() => {
								uni.navigateBack()
							}, 1000)
						} else {
							uni.hideLoading()
							uni.showToast({
								title: res.msg,
								icon: 'none'
							})
						}
					})
				} else {
					uni.hideLoading()
					uni.showToast({
						title: url.msg,
						icon: 'none'
					})
				}
			}
		});
	};
</script>

<style lang="scss">
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

		.btn {
			width: 100%;
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