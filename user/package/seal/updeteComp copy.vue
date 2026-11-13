<template>
	<view>
		<view class="info-box">
			<view class="info-tit">企业印章</view>
			<view class="info-img" v-if="enterpriseName">
				<feng-seal ref="seal" :inCircle="true" imageType="png" size="200px" :companyText="enterpriseName"
					:sealCode="downText" :sealText="sealName"></feng-seal>
			</view>

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
	} from '@dcloudio/uni-app'
	const {
		proxy
	} = getCurrentInstance();
	const enterpriseName = ref('');
	const downText = ref('');
	const sealName = ref('合同专用章');
	const seal = ref(null);
	onLoad(option => {
		if (option.enterpriseName) {
			enterpriseName.value = option.enterpriseName
		}
	});
	//保存
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
						companySeal: imgUrl,
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
	//保存
	const saveSeal = () => {
		uni.showModal({
			title: '提示',
			content: '确认保存企业印章吗？',
			complete: (ret) => {
				if (ret.confirm) {
					seal.value.make().then(res => {
						console.log(res)
						if (res.tempFilePath) {
							let upFile = ''
							// #ifdef H5
							upFile = base64ToBlob(res.tempFilePath, 'image/png')
							// #endif
							// #ifndef H5
							upFile = res.tempFilePath
							// #endif
							imageGenerated(upFile)
						}
					})
				}
			}
		})
	};
	const base64ToBlob = (base64Data, contentType = 'image/png', sliceSize = 512) => {
		// 去掉 data:image/png;base64, 前缀
		const base64 = base64Data.split(',')[1];
		const byteCharacters = atob(base64);
		const byteArrays = [];

		for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
			const slice = byteCharacters.slice(offset, offset + sliceSize);
			const byteNumbers = new Array(slice.length);
			for (let i = 0; i < slice.length; i++) {
				byteNumbers[i] = slice.charCodeAt(i);
			}
			const byteArray = new Uint8Array(byteNumbers);
			byteArrays.push(byteArray);
		}

		return new Blob(byteArrays, {
			type: contentType
		});
	};
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