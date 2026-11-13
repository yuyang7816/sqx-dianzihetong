<template>
	<view>
		<view class="topInfo flex align-center justify-center">
			<view class="topInfo-box">
				<view class="topInfo-box-title flex align-center justify-between">
					<view class="topInfo-box-title-txt">
						{{contractInfo.contractName?contractInfo.contractName:''}}
					</view>
					<view class="topInfo-box-title-txt">
						{{timeFormat(new Date(contractInfo.createTime), 'yyyy-mm-dd')}}
					</view>
				</view>
				<view class="topInfo-box-name">
					发起人：{{contractInfo.userName?contractInfo.userName:''}}
				</view>
				<view class="topInfo-box-name">
					合同编号：{{contractInfo.contractNumber?contractInfo.contractNumber:''}}
				</view>
				<view class="topInfo-box-name">
					合同状态：
					<text v-if="contractInfo.isComplete===0">待签署</text>
					<text v-if="contractInfo.isComplete===1">已完成</text>
					<text v-if="contractInfo.isComplete===3">已过期</text>
				</view>
				<view v-if="contractInfo.isComplete!=1 && userList.length>0" style="color: red;"
					class="topInfo-box-name">
					过期时间：{{userList[0].lastDate?userList[0].lastDate:''}}
				</view>
				<view class="topInfo-box-list flex justify-between flex-wrap">
					<image class="topInfo-box-list-img" @click="prewImg(index,fileUrl)" :src="item"
						v-for="(item,index) in fileUrl" :key="index" mode="widthFix"></image>
					<view class="topInfo-box-list-img" style="height:0"></view>
				</view>
			</view>
		</view>

		<view class="infoBot">
			<view class="infoBot_title">收件人</view>
			<view class="infoBot_ite" v-for="(item,index) in userList" :key="index">
				<view class="flex justify-between" style="width:100%" v-if="!item.receivingUserName">
					<view class="flex">
						<!-- <image
							:src="item.userAvatar?item.userAvatar:(item.signUserHeadImg?item.signUserHeadImg:'/static/logo.png')"
							mode="aspectFill">
						</image> -->
						<view class="infoBot_ite_cot">
							<view class="infoBot_ite_cotTit">{{item.userName?item.userName:item.signUserName}}</view>
							<view class="infoBot_ite_cotState" :style="item.isSign==1?'':'color:#FEB10A;'">
								{{item.isSign==1?'已签署':'未签署'}}
							</view>
						</view>
					</view>

					<view v-if="item.signDate" class="infoBot_iteData">
						{{timeFormat(new Date(item.signDate), 'yyyy-mm-dd')}}
					</view>
				</view>
				<view class="flex justify-between" style="width:100%" v-else>
					<view class="flex align-center">
						<image :src="item.copyUserHeadImg?item.copyUserHeadImg:'/static/logo.png'" mode="aspectFill">
						</image>
						<view class="infoBot_ite_cot">
							<view class="infoBot_ite_cotTit">{{item.receivingUserName?item.receivingUserName:''}}</view>
						</view>
					</view>
					<view class="infoBot_iteData" style="color:#0096FF">
						抄送人
					</view>
				</view>

			</view>
		</view>
		<view v-if="signUser && contractInfo.isComplete!=3" class="btn" @click="show = true">+添加收件人</view>
		<view class="btn" v-if="isShowSing && contractInfo.isComplete==0" @click="selectShow()">签署</view>
		<view class="btn"
			v-if="contractInfo.userId == userId && contractInfo.isComplete!=3 && contractInfo.isComplete!=2 && userList.length>1"
			@click="backContont()">撤回
		</view>



		<up-popup :show="show" @close="show = false" round="12" closeable>
			<view class="popup">
				<view class="popupTit">添加收件人</view>
				<view class="popupIte">
					<view class="popupIte_tit"><text>*</text>收件人名称</view>
					<view class="popupIte_input">
						<input type="text" v-model="name" placeholder="请输入收件人姓名" />
					</view>
				</view>

				<view class="popupIte">
					<view class="popupIte_tit"><text>*</text>收件人手机号</view>
					<view class="popupIte_input">
						<input type="text" v-model="phone" placeholder="请输入收件人手机号" />
					</view>
				</view>
				<view class="popupBtn" @click="addUser()">确定添加</view>
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import config from '../../common/config.js';
	import {
		timeFormat
	} from '@/uni_modules/uview-plus';
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
	const show = ref(false);
	const name = ref('');
	const phone = ref('');
	const value = ref(true);
	const id = ref(''); //合同id
	const contractInfo = ref({});
	const userList = reactive([]);
	const fileUrl = reactive([]); //合同图片
	const signUser = ref(false); //是否是签署人
	const isShowSing = ref(false); //是否显示签署按钮
	const type = ref(2); //自己的签署类型 1:企业 2:个人
	const userId = ref(''); //我的userId
	onLoad(option => {
		userId.value = uni.getStorageSync('userId')
		uni.showLoading({
			title: '加载中...'
		})
		id.value = option.id
		selectByContractDetail()
	});
	const backContont = () => {
		uni.showModal({
			title: '提示',
			content: '确认撤回合同吗？撤回的合同在草稿箱可重新发起',
			confirmText: '确认撤回',
			cancelText: '取消',
			complete: (ret) => {
				if (ret.confirm) {
					let data = {
						contractId: id.value
					}
					uni.showLoading({
						title: '撤回中...'
					})
					proxy.$http.postJson('/app/contract/retractContract', data).then(res => {
						uni.hideLoading()
						if (res.code == 0) {
							uni.showToast({
								title: '已撤回'
							})
							uni.redirectTo({
								url: '/package/work/received?navType=4'
							})
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
	};
	//区分合同类型
	const selectShow = () => {
		uni.navigateTo({
			url: '/package/contract/preview?contractId=' + id.value + '&type=' + type.value +
				'&contontUserId=' + contractInfo.value.userId + '&pdfImageUrl=' +
				fileUrl.join(',')
		})

	};
	//添加收件人
	const addUser = () => {
		if (!name.value) {
			uni.showToast({
				title: '请输入收件人名称',
				icon: 'none'
			})
			return
		}
		if (!phone.value) {
			uni.showToast({
				title: '请输入手机号',
				icon: 'none'
			})
			return
		}
		uni.showModal({
			title: '提示',
			content: '确认添加吗？',
			complete: (ret) => {
				if (ret.confirm) {
					let data = {
						contractId: id.value,
						name: name.value,
						phone: phone.value
					}
					uni.showLoading({
						title: '添加中...'
					})
					proxy.$http.postJson('/app/contract/selectRecipients', data).then(res => {
						uni.hideLoading()
						if (res.code == 0) {
							uni.showToast({
								title: '已添加',
							})
							selectByContractDetail();
							show.value = false
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


	};
	const prewImg = (index, imgs) => {
		uni.previewImage({
			urls: imgs,
			current: index
		})
	};
	//获取合同详情
	const selectByContractDetail = () => {
		let data = {
			id: id.value,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/contract/selectByContractDetail', data).then(res => {
			uni.hideLoading();
			uni.stopPullDownRefresh()
			if (res.code == 0) {
				contractInfo.value = res.data
				userList.splice(0, userList.length, ...res.data.contractAndUsers);
				// signUser
				let indexs = userList.findIndex(item => item.copyUser == uni.getStorageSync('userId'))
				if (indexs != -1) { //我是抄送人
					signUser.value = false
				} else { //我不是抄送人
					signUser.value = true
				}
				if (res.data.fileUrl) { //有签署的合同文件
					fileUrl.splice(0, fileUrl.length, ...(res.data.fileUrl).split(','));
				} else { //没有就显示未签署的
					fileUrl.splice(0, fileUrl.length, ...(res.data.templateUrl).split(','));
				}
				//拿出需要签署的列表
				let signList = []
				userList.map(item => {
					if (item.isSign != 1 && !item.copyUser) {
						signList.push(item)
					}
				})
				let indexs1 = signList.findIndex(item => item.userId == uni.getStorageSync('userId'))
				let indexs2 = signList.findIndex(item => item.signedBy == uni.getStorageSync('userId'))
				if (indexs1 != -1 || indexs2 != -1) {
					isShowSing.value = true
				} else {
					isShowSing.value = false
				}
				//找到自己的信息 (自己是签署人)
				if (isShowSing.value) {
					let myUserInfo = indexs1 != -1 ? signList[indexs1] : signList[indexs2]
					type.value = myUserInfo.agreementType == null || myUserInfo.agreementType == '' ?
						myUserInfo.type : myUserInfo.agreementType
				}
				console.log(type.value, '我的签署类型')
			} else {
				uni.hideLoading();
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		})
	};
	const change = (e) => {
		console.log('change', e);
	};
</script>

<style lang="scss" scoped>
	.topInfo {
		width: 100%;
		height: auto;
		background: linear-gradient(to bottom, #0096FF, #F5F7FF);
		padding-top: 20rpx;

		.topInfo-box {
			width: calc(686rpx - 64rpx);
			height: 100%;
			background-color: #ffffff;
			border-radius: 24rpx;
			padding: 32rpx;
		}

		.topInfo-box-title {
			.topInfo-box-title-txt:nth-of-type(1) {
				width: 450rpx;
				font-weight: 500;
				font-size: 30rpx;
				color: #333333;
				overflow-wrap: break-word;
				word-break: break-all;
				/* 可选，根据需求决定是否激进断开 */
			}

			.topInfo-box-title-txt:nth-of-type(2) {
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
			}
		}

		.topInfo-box-name {
			font-weight: 400;
			font-size: 24rpx;
			color: #999999;
			margin-top: 10rpx;
		}

		.topInfo-box-list {
			margin-top: 10rpx;

			.topInfo-box-list-img {
				width: 194rpx;
				height: auto;
				margin-top: 20rpx;
			}
		}
	}

	.infoBot {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 30rpx;
		padding: 30rpx;

		.infoBot_title {
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
		}

		.infoBot_ite {
			display: flex;
			padding: 30rpx 0 0;

			image {
				width: 60rpx;
				height: 60rpx;
				border-radius: 55%;
			}

			.infoBot_ite_cot {
				flex: 1;
				// margin-left: 20rpx;

				.infoBot_ite_cotTit {
					font-weight: 400;
					font-size: 28rpx;
					color: #333333;
				}

				.infoBot_ite_cotState {
					font-weight: 400;
					font-size: 24rpx;
					color: #0096FF;
				}
			}

			.infoBot_iteData {
				font-weight: 400;
				font-size: 24rpx;
				color: #999999;
			}
		}
	}

	.btn {
		width: 600rpx;
		height: 88rpx;
		background: #0096FF;
		border-radius: 10rpx;
		font-weight: 400;
		font-size: 26rpx;
		color: #FFFFFF;
		display: flex;
		align-items: center;
		justify-content: center;
		margin: 50rpx auto 0;
	}



	.popup {
		padding: 42rpx 30rpx;

		.popupTit {
			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
		}

		.popupIte {
			padding-top: 40rpx;

			.popupIte_tit {
				text {
					color: #FF000E;
					margin-right: 6rpx;
				}

				font-weight: 500;
				font-size: 32rpx;
				color: #333333;
			}

			.popupIte_input {
				height: 89rpx;
				border-radius: 10rpx;
				border: 1px solid #E6E6E6;
				padding: 0 20rpx;
				display: flex;
				align-items: center;
				margin-top: 15rpx;
			}
		}

		.popupBtn {
			width: 666rpx;
			height: 88rpx;
			background: #0096FF;
			border-radius: 10rpx;
			font-weight: 400;
			font-size: 26rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
			margin-top: 40rpx;
		}
	}
</style>