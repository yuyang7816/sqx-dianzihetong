<template>
	<view style="padding-bottom: 40rpx;">

		<view v-if="myId" class="status flex align-center justify-center">
			<view class="status-box flex align-center justify-center">
				<text v-if="status === 0">审核中</text>
				<text v-if="status === 1">已通过</text>
				<text style="color: red;" v-if="status === 2">已拒绝</text>
			</view>
		</view>
		<view v-if="myId && status === 2 && remark" class="status flex align-center justify-center">
			<view class="status-box flex align-center justify-center">
				<text style="color: red;font-size: 24rpx;font-weight: normal;">拒绝理由：{{remark}}</text>
			</view>
		</view>
		<view class="froms flex align-center justify-center">
			<view class="froms-box">
				<up-form labelWidth="200" :labelStyle="labelStyle" labelPosition="top" :model="froms">
					<up-form-item label="姓名" :borderBottom="true">
						<up-input :placeholderStyle="placeholderStyle" v-model="froms.name" placeholder="请输入真实姓名"
							border="none"></up-input>
					</up-form-item>
					<up-form-item label="证件号码" :borderBottom="true">
						<up-input :placeholderStyle="placeholderStyle" v-model="froms.idNumber" disabledColor="#ffffff"
							placeholder="请选择证件号码" border="none"></up-input>
					</up-form-item>
					<up-form-item label="手机号" :borderBottom="true">
						<up-input :placeholderStyle="placeholderStyle" type="number" v-model="froms.phone"
							disabledColor="#ffffff" placeholder="请输入手机号" border="none"></up-input>
					</up-form-item>
					<up-form-item label="当前地址（选填）" :borderBottom="true">
						<up-input :placeholderStyle="placeholderStyle" v-model="froms.address" disabledColor="#ffffff"
							placeholder="请输入当前地址" border="none"></up-input>
					</up-form-item>

				</up-form>
			</view>
		</view>
		<view class="froms flex align-center justify-center">
			<view class="froms-box">
				<view class="froms-box-title">
					证件照片
				</view>
				<view v-if="!froms.front" @click="upLoudImg(1)"
					class="froms-box-image flex align-center justify-center">
					<up-icon name="camera" color="#282828" size="48"></up-icon>
					<view class="froms-box-image-txt">
						证件身份证正面
					</view>
				</view>
				<view v-else @click="upLoudImg(1)" style="border: none;"
					class="froms-box-image flex align-center justify-center">
					<image :src="froms.front" mode="aspectFill"></image>
				</view>
				<view v-if="!froms.back" @click="upLoudImg(2)" class="froms-box-image flex align-center justify-center">
					<up-icon name="camera" color="#282828" size="48"></up-icon>
					<view class="froms-box-image-txt">
						证件身份证反面
					</view>
				</view>
				<view v-else @click="upLoudImg(2)" style="border: none;"
					class="froms-box-image flex align-center justify-center">
					<image :src="froms.back" mode="aspectFill"></image>
				</view>
			</view>
		</view>
		<view v-if="status === 2 || status === -1" class="bottomBtn flex align-center justify-center">
			<view @click="submit()" class="bottomBtn-box flex align-center justify-center">
				提交认证
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		reactive,
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app'
	import config from '/common/config.js';
	const {
		proxy
	} = getCurrentInstance();
	const froms = reactive({
		name: '',
		idNumber: '',
		phone: '',
		front: '',
		back: '',
		address: ''
	});
	const labelStyle = reactive({
		color: '#000000',
		fontWeight: 800,
		fontSize: '28rpx',
	});
	const placeholderStyle = ref('fontSize:26rpx;');
	const status = ref(-1);
	const remark = ref('');
	const myId = ref('');
	onLoad(option => {
		if (option.isAuthentication && option.isAuthentication != -1) {
			queryInsert();
		}

	});
	//查询认证信息
	const queryInsert = () => {
		proxy.$http.getT('/app/userCertification/queryInsert').then(res => {
			if (res.code == 0 && res.data) {
				froms.name = res.data.name
				froms.idNumber = res.data.idNumber
				froms.phone = res.data.realPhone
				froms.address = res.data.address
				froms.front = res.data.front
				froms.back = res.data.back
				status.value = res.data.status
				remark.value = res.data.remark
				myId.value = res.data.id
			}
		})
	};
	//提交认证
	const submit = () => {
		if (!froms.name) {
			uni.showToast({
				title: '请输入真实姓名',
				icon: 'none'
			})
			return
		}
		if (!froms.idNumber) {
			uni.showToast({
				title: '请输入身份证号',
				icon: 'none'
			})
			return
		}
		if (!froms.phone) {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
			return
		}
		if (!froms.front) {
			uni.showToast({
				title: '请上传身份证正面',
				icon: 'none'
			})
			return
		}
		if (!froms.back) {
			uni.showToast({
				title: '请上传身份证反面',
				icon: 'none'
			})
			return
		}
		uni.showModal({
			title: '提示',
			content: '确认提交认证信息吗？',
			complete: (ret) => {
				if (ret.confirm) {
					let data = JSON.parse(JSON.stringify(froms));
					if (myId.value) {
						data.id = myId.value
					}
					uni.showLoading({
						title: '提交中...'
					})
					proxy.$http.post('/app/userCertification/insert', data).then(res => {
						if (res.code == 0) {
							uni.hideLoading()
							uni.showToast({
								title: '已提交'
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
				}
			}
		})
	};
	//上传图片
	const upLoudImg = (type) => {
		uni.chooseImage({
			count: 1, //默认9
			sizeType: ['original', 'compressed'], //可以指定是原图还是压缩图，默认二者都有
			sourceType: ['album'], //从相册选择
			success: function(res) {
				uni.showLoading({
					title: '上传中...'
				});
				uni.uploadFile({
					url: config.APIHOST + '/alioss/upload', //真实的接口地址
					// url: '' // 填写自己的上传接口地址
					filePath: res.tempFilePaths[0],
					header: {
						token: uni.getStorageSync('token')
					},
					name: 'file',
					success: uploadFileRes => {
						uni.hideLoading();
						let url = JSON.parse(uploadFileRes.data)
						if (url.code == 0) {
							if (type === 1) {
								froms.front = url.data
							} else {
								froms.back = url.data
							}
						} else {
							uni.showToast({
								title: url.msg,
								icon: 'none'
							})
						}
					}
				});
			}
		});
	};
</script>

<style lang="scss">
	.status {
		width: 100%;
		height: auto;
		margin-top: 30rpx;

		.status-box {
			width: calc(686rpx - 60rpx);
			height: 100%;
			background-color: #ffffff;
			border-radius: 24rpx;
			padding: 30rpx;
			color: #0096FF;
			font-size: 28rpx;
			font-weight: bold;
		}
	}

	.froms {
		width: 100%;
		height: auto;
		margin-top: 30rpx;

		.froms-box {
			width: calc(686rpx - 60rpx);
			height: 100%;
			background-color: #ffffff;
			border-radius: 24rpx;
			padding: 30rpx;
		}

		.froms-box-title {
			font-weight: 800;
			font-size: 28rpx;
			color: #000000;
		}

		.froms-box-image {
			width: 100%;
			height: 320rpx;
			border: 1px dashed #353535;
			flex-direction: column;
			margin-top: 30rpx;

			image {
				width: 100%;
				height: 100%;
			}

			.froms-box-image-txt {
				font-weight: 500;
				font-size: 20rpx;
				color: #353535;
				margin-top: 10rpx;
			}
		}
	}

	.bottomBtn {
		width: 100%;
		height: auto;
		margin-top: 56rpx;

		.bottomBtn-box {
			width: 686rpx;
			height: 98rpx;
			background: #0096FF;
			border-radius: 16rpx;
			font-weight: bold;
			font-size: 32rpx;
			color: #FFFFFF;
		}
	}
</style>