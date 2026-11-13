<template>
	<view style="padding-bottom: 160rpx;">
		<view class="htTitle">{{templateName}}</view>


		<view class="trade-card">
			<!-- 切换标签 -->
			<view class="trade-tabs">
				<view class="tab " :class="tabIndex==1?'active':''" @click="clickTab(1)">
					<view class="triangle-up" v-if="tabIndex==1"></view>
					我是甲方
				</view>
				<view class="tab" :class="tabIndex==2?'active':''" @click="clickTab(2)">
					<view class="triangle-down" v-if="tabIndex==2"></view>
					我是乙方
				</view>
			</view>

			<view class="info-container">
				<!-- 信息 -->
				<view v-if="topInfoType===-1" @click="showTypes(1)"
					class="info-block flex align-center justify-between">
					<view class="info-content">
						<view :style="{backgroundColor:tabIndex==1?'#0096ff':'#ffb109'}" class="info-content-status">
							{{tabIndex==1?'甲方':'乙方'}}
						</view>
						<view class="name" style="font-size: 28rpx;">请填写</view>
					</view>
					<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
				</view>
				<!-- 个人 -->
				<view v-if="topInfoType===1" @click="showTypes(1)" class="info-block flex align-center justify-between">
					<view>
						<view class="info-content">
							<view :style="{backgroundColor:tabIndex==1?'#0096ff':'#ffb109'}"
								class="info-content-status">
								{{tabIndex==1?'甲方':'乙方'}}
							</view>
							<view class="name">{{myQueryInsert.name}}</view>
						</view>
						<view class="account">{{myQueryInsert.realPhone}} | {{myQueryInsert.idNumber}}</view>
					</view>
					<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
				</view>
				<!-- 企业 -->
				<view v-if="topInfoType===2" @click="showTypes(1)" class="info-block flex align-center justify-between">
					<view>
						<view class="info-content">
							<view :style="{backgroundColor:tabIndex==1?'#0096ff':'#ffb109'}"
								class="info-content-status">
								{{tabIndex==1?'甲方':'乙方'}}
							</view>
							<view class="name">{{enterprise[0].enterpriseName}}</view>
						</view>
						<view class="account">{{enterprise[0].legalPerson}}</view>
					</view>
					<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
				</view>
				<view class="inof-line"></view>
				<!-- 乙方信息 -->
				<view @click="showTypes(2)" v-if="downInfoType === -1"
					class="flex align-center justify-between padding-right">
					<view class="info-block">
						<view class="info-content">
							<view :style="{backgroundColor:tabIndex==2?'#0096ff':'#ffb109'}"
								class="info-content-status">
								{{tabIndex==2?'甲方':'乙方'}}
							</view>
							<view class="name" style="font-size: 28rpx;">请填写</view>
						</view>
					</view>
					<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
				</view>
				<!-- 个人 -->
				<view @click="showTypes(2)" v-if="downInfoType === 1"
					class="flex align-center justify-between padding-right">
					<view class="info-block">
						<view class="info-content">
							<view :style="{backgroundColor:tabIndex==2?'#0096ff':'#ffb109'}"
								class="info-content-status">
								{{tabIndex==2?'甲方':'乙方'}}
							</view>
							<view class="name">{{byUserInfo.signUserName}}</view>
						</view>
						<view class="account">{{byUserInfo.signUserPhone}} | {{byUserInfo.idCardNumber}}</view>
					</view>
					<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
				</view>
				<!-- 企业 -->
				<view @click="showTypes(2)" v-if="downInfoType === 2"
					class="flex align-center justify-between padding-right">
					<view class="info-block">
						<view class="info-content">
							<view :style="{backgroundColor:tabIndex==2?'#0096ff':'#ffb109'}"
								class="info-content-status">
								{{tabIndex==2?'甲方':'乙方'}}
							</view>
							<view class="name">{{byUserInfo.signUserName}}</view>
						</view>
						<view class="account">{{byUserInfo.signUserPhone}}</view>
					</view>
					<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
				</view>
			</view>
		</view>
		<view class="info_border">
			<view class="info_box" v-for="(item,index) in froms" :key="index">
				<view class="info_tit margin-right">{{item.fieldName}}</view>
				<view class="info_input">
					<!-- <input v-model="item[filedValue]" type="text" :placeholder="'请输入'+item.fieldName" /> -->
					<up-input inputAlign="right" v-model="item[item.filedValue]" :placeholder="'请输入'+item.fieldName"
						border="none"></up-input>
				</view>
			</view>
		</view>
		<view class="taber">
			<view @click="addDraftBox" class="taber_l">
				<image src="/package/static/caogao.png"></image>
				<view>存为草稿</view>
			</view>
			<view @click="submitQs()" class="taber_btn">创建合同</view>
		</view>
		<up-action-sheet round="24" zIndex="100" :closeOnClickAction="false" :safeAreaInsetBottom="true" :actions="list"
			:title="title" :show="show" @select="selectAction" @close="show = false"></up-action-sheet>
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
	const tabIndex = ref(1)
	const id = ref('');
	const templateName = ref('');
	const froms = reactive([]); //动态表单
	const enterprise = reactive([]); //企业认证信息
	const myQueryInsert = reactive({}); //个人认证信息
	const title = ref('类型');
	const list = ref([{
		name: '个人',
		fontSize: '12',
		type: 1
	}, {
		name: '企业',
		fontSize: '12',
		type: 2
	}]);
	const show = ref(false); //是否显示类型
	const isType = ref(1); //1:上 2:下
	const topInfoType = ref(-1); //上方信息 -1:没有选择 1:个人 2:企业
	const downInfoType = ref(-1); //下方信息 -1:没有选择 1:个人 2:企业
	const byUserInfo = reactive({}); //对方信息
	const detailId = ref(''); //详情id
	onLoad(option => {
		if (option.detailId) {
			detailId.value = option.detailId
		}
		queryInsert();
		enterpriseDetail();
		if (option.templateName) {
			templateName.value = option.templateName
		}
		if (option.id) {
			id.value = option.id
			selectContractTemplateList();
		}

	});
	onShow(() => {
		uni.$once('froms', data => {
			Object.keys(byUserInfo).forEach(key => delete byUserInfo[key])
			Object.assign(byUserInfo, data)
			downInfoType.value = Number(byUserInfo.type)
			delete byUserInfo.type
		})
	});
	//获取合同详情（草稿箱）
	const getDetailInfo = () => {
		let data = {
			id: detailId.value,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/contract/selectByContractDetail', data).then(res => {
			if (res.code == 0) {
				if (res.data.contractAndUsers.length > 0) {
					topInfoType.value = res.data.contractAndUsers[0].type == 1 ? 2 : 1
					if (res.data.contractAndUsers.length > 1) {
						downInfoType.value = res.data.contractAndUsers[1].agreementType ? 2 : 1
						if (downInfoType.value === 1) { //个人
							byUserInfo.signUserName = res.data.contractAndUsers[1].signUserName
							byUserInfo.signUserPhone = res.data.contractAndUsers[1].signUserPhone
							byUserInfo.idCardNumber = res.data.contractAndUsers[1].idCardNumber
						} else { //企业
							byUserInfo.signUserName = res.data.contractAndUsers[1].signUserName
							byUserInfo.signUserPhone = res.data.contractAndUsers[1].signUserPhone
						}
					}
				}
				let dataJson = JSON.parse(res.data.dataJson)
				// 遍历填充表单
				for (const [key, value] of Object.entries(dataJson)) {
					const formItem = froms.find(item => item.filedValue === key)
					if (formItem) {
						formItem[key] = value
					}
				}
			}
		})
	};
	//存为草稿
	const addDraftBox = () => {
		if (tabIndex.value === 1) { //我是甲方
			if (topInfoType.value === -1) {
				uni.showToast({
					title: '请输入甲方信息',
					icon: 'none'
				})
				return
			}
			if (downInfoType.value === -1) {
				uni.showToast({
					title: '请输入乙方信息',
					icon: 'none'
				})
				return
			}
		} else { //我是乙方
			if (topInfoType.value === -1) {
				uni.showToast({
					title: '请输入乙方信息',
					icon: 'none'
				})
				return
			}
			if (downInfoType.value === -1) {
				uni.showToast({
					title: '请输入甲方信息',
					icon: 'none'
				})
				return
			}
		}
		for (let i = 0; i < froms.length; i++) {
			if (!froms[i][froms[i].filedValue]) {
				uni.showToast({
					title: '请输入' + froms[i].fieldName,
					icon: 'none'
				})
				return
			}
		}
		// 提取动态表单数据
		const formData = {}
		froms.forEach(item => {
			formData[item.filedValue] = item[item.filedValue]
		})
		let data = {
			templateId: id.value,
			type: topInfoType.value === 2 ? 1 : 2, //我的身份 ：1企业 2个人
			isPrincipal: tabIndex.value, //我是 1:甲方 2:乙方
			userName: topInfoType.value === 1 ? myQueryInsert.name : enterprise[0]
				.enterpriseName, // topInfoType 1:个人 2:企业
			userPhone: topInfoType.value === 1 ? myQueryInsert.realPhone : '', //个人手机号
			idCard: topInfoType.value === 1 ? myQueryInsert.idNumber : '', //个人身份证号
			userLegalPerson: topInfoType.value === 2 ? enterprise[0].legalPerson : '',
			agreementType: downInfoType.value === 2 ? 1 : 2, //对方 1:企业 2:个人
			signUserName: byUserInfo.signUserName, //签署人姓名/企业名称
			idCardNumber: downInfoType.value === 1 ? byUserInfo.idCardNumber : '', //签署人身份证
			signUserPhone: downInfoType.value === 1 ? byUserInfo.signUserPhone : '', //签署人手机号
			signLegalPerson: downInfoType.value === 2 ? byUserInfo.signUserPhone : '', //签署人企业法人
			dataJson: JSON.stringify(formData), //动态字段
		}
		if (detailId.value) {
			data.contractId = detailId.value
		}
		uni.showModal({
			title: '提示',
			content: '确认存为草稿吗？',
			complete: (ret) => {
				if (ret.confirm) {
					uni.showLoading({
						title: '存草稿中...'
					})
					proxy.$http.postJson('/app/contract/addDraftBox', data).then(res => {
						if (res.code == 0) {
							uni.hideLoading()
							uni.redirectTo({
								url: '/package/work/received?navType=4'
							})
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
	//发起签署
	const submitQs = () => {
		for (let i = 0; i < froms.length; i++) {
			if (!froms[i][froms[i].filedValue]) {
				uni.showToast({
					title: '请输入' + froms[i].fieldName,
					icon: 'none'
				})
				break;
				return
			}
		}

		if (tabIndex.value === 1) { //我是甲方
			if (topInfoType.value === -1) {
				uni.showToast({
					title: '请输入甲方信息',
					icon: 'none'
				})
				return
			}
			if (downInfoType.value === -1) {
				uni.showToast({
					title: '请输入乙方信息',
					icon: 'none'
				})
				return
			}
		} else { //我是乙方
			if (topInfoType.value === -1) {
				uni.showToast({
					title: '请输入乙方信息',
					icon: 'none'
				})
				return
			}
			if (downInfoType.value === -1) {
				uni.showToast({
					title: '请输入甲方信息',
					icon: 'none'
				})
				return
			}
		}
		for (let i = 0; i < froms.length; i++) {
			if (!froms[i][froms[i].filedValue]) {
				uni.showToast({
					title: '请输入' + froms[i].fieldName,
					icon: 'none'
				})
				return
			}
		}
		// 提取动态表单数据
		const formData = {}
		froms.forEach(item => {
			formData[item.filedValue] = item[item.filedValue]
		})
		let data = {
			templateId: id.value,
			type: topInfoType.value === 2 ? 1 : 2, //只要我是企业，那么就是企业合同 ：1企业 2个人
			isPrincipal: tabIndex.value, //我是 1:甲方 2:乙方
			userName: topInfoType.value === 1 ? myQueryInsert.name : enterprise[0]
				.enterpriseName, // topInfoType 1:个人 2:企业
			userPhone: topInfoType.value === 1 ? myQueryInsert.realPhone : '', //个人手机号
			idCard: topInfoType.value === 1 ? myQueryInsert.idNumber : '', //个人身份证号
			userLegalPerson: topInfoType.value === 2 ? enterprise[0].legalPerson : '',
			agreementType: downInfoType.value === 2 ? 1 : 2, //对方 1:企业 2:个人
			signUserName: byUserInfo.signUserName, //签署人姓名/企业名称
			idCardNumber: downInfoType.value === 1 ? byUserInfo.idCardNumber : '', //签署人身份证
			signUserPhone: downInfoType.value === 1 ? byUserInfo.signUserPhone : '', //签署人手机号
			signLegalPerson: downInfoType.value === 2 ? byUserInfo.signUserPhone : '', //签署人企业法人
		}
		uni.showModal({
			title: '提示',
			content: '确认创建合同吗？',
			complete: (ret) => {
				if (ret.confirm) {
					uni.showLoading({
						title: '合同创建中...'
					})
					proxy.$http.postJson('/app/contract/initiatingContract', data).then(res => {
						if (res.code == 0) {
							// uni.hideLoading()
							let datas = {
								contractId: res.data.contractId,
								templatePath: res.data.templatePath,
								fieldValues: {
									...formData // 展开动态字段
								}
							}
							proxy.$http.postJson('/app/contract/generatePDF', datas).then(
								ree => {
									if (ree.code == 0) {
										let imgArr = ree.data
										let datasss = {
											contractId: datas.contractId,
											pdfImageUrl: imgArr.join(',')
										}
										let userId = uni.getStorageSync('userId')
										proxy.$http.postJson('/app/contract/modifyContract',
											datasss).then(ret => {
											if (ret.code == 0) {
												uni.hideLoading()
												uni.navigateTo({
													url: '/package/contract/preview?contractId=' +
														datas.contractId +
														'&type=' + data
														.type +
														'&contontUserId=' +
														userId +
														'&pdfImageUrl=' +
														imgArr.join(',')
												})
											} else {
												uni.hideLoading();
												uni.showToast({
													title: ret.msg,
													icon: 'none'
												})
											}
										})
									} else {
										uni.hideLoading()
										uni.showToast({
											title: ree.msg,
											icon: 'none'
										})
									}
								})
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
		console.log(data)
	};
	//选择类型
	const selectAction = (e) => {
		if (e.type === 1) { //个人
			if (isType.value === 1) { //上面的信息
				topInfoType.value = 1
			} else { //下面的 （跳转填写页面）
				uni.navigateTo({
					url: '/package/contract/add?type=1'
				})
			}
			show.value = false
		} else { //企业
			if (isType.value === 1) { //上面的信息
				if (enterprise.length == 0) {
					uni.showModal({
						title: '提示',
						content: '请进行企业认证',
						confirmText: '去认证',
						cancelText: '取消',
						complete: (ret) => {
							if (ret.confirm) {
								uni.navigateTo({
									url: '/package/enterprise/certification'
								})
							}
						}
					})
				} else if (enterprise[0].status === 1) { //认证通过
					topInfoType.value = 2
					show.value = false
				} else if (enterprise[0].status === 0) {
					uni.showToast({
						title: '企业认证审核中~',
						icon: 'none'
					})
				} else if (enterprise[0].status === 2) {
					uni.showModal({
						title: '提示',
						content: '企业认证未通过~',
						confirmText: '重新认证',
						cancelText: '取消',
						complete: (ret) => {
							if (ret.confirm) {
								uni.navigateTo({
									url: '/package/enterprise/certification?id=' + enterprise[0].id
								})
							}
						}
					})
				}
			} else { //下面的（跳转填写页面）
				uni.navigateTo({
					url: '/package/contract/add?type=2'
				})
				show.value = false
			}
		}
	};
	//显示类型
	const showTypes = (type) => {
		isType.value = type
		show.value = true
	};
	//查询认证信息 (个人)
	const queryInsert = () => {
		proxy.$http.getT('/app/userCertification/queryInsert').then(res => {
			if (res.code == 0) {
				if (res.data) { //提交了认证信息
					Object.assign(myQueryInsert, res.data)
				} else {
					Object.keys(myQueryInsert).forEach(key => delete myQueryInsert[key])
				}
			}
		})
	};
	//查询认证信息 (企业)
	const enterpriseDetail = () => {
		proxy.$http.getT('/app/enterprise/selectEnterpriseList').then(res => {
			if (res.code == 0) {
				if (res.data.records.length > 0) { //有企业认证
					enterprise.splice(0, enterprise.length, ...res.data.records)
				} else { //没有企业认证
					enterprise.splice(0, enterprise.length)
				}
			}
		})
	};
	//获取模版动态字段
	const selectContractTemplateList = () => {
		let data = {
			id: id.value
		}
		proxy.$http.getT('/app/contract/selectContractTemplateList', data).then(res => {
			if (res.code == 0) {
				res.data.map(item => {
					item[item.filedValue] = ''
				})
				froms.splice(0, froms.length, ...res.data)
				if (detailId.value) {
					getDetailInfo()
				}
			}
		})
	};
	const clickTab = (index) => {
		tabIndex.value = index
	};
	const change = (e) => {
		console.log('change', e);
	}
</script>

<style lang="scss">
	.htTitle {
		margin: 30rpx;
		height: 91rpx;
		background: #FFFFFF;
		box-shadow: 0rpx 6rpx 5rpx 0rpx rgba(34, 39, 43, 0.02);
		border-radius: 10rpx;
		font-weight: 500;
		font-size: 32rpx;
		color: #333333;
		display: flex;
		align-items: center;
		padding: 0 30rpx;
	}

	.trade-card {
		margin: 30rpx;
		background: #F5F5F5;
		box-shadow: 0rpx 6rpx 5rpx 0rpx rgba(34, 39, 43, 0.02);
		border-radius: 10rpx;

		/* 切换标签样式 */
		.trade-tabs {
			display: flex;
			background: #F5F5F5;
			height: 100rpx;

			.tab {
				flex: 1;
				text-align: center;
				font-size: 32rpx;
				font-weight: 500;
				color: #999999;
				border: 1rpx solid #EBEBEB;
				transition: all 0.3s;
				display: flex;
				align-items: center;
				justify-content: center;
				position: relative;

				&.active {
					background: #FFFFFF;
					font-weight: 800;
					font-size: 32rpx;
					color: #0096FF;
					border: 1rpx solid #FFFFFF;
				}

				.triangle-up {
					width: 0;
					height: 0;
					border-left: 0rpx solid transparent;
					border-right: 60rpx solid transparent;
					border-bottom: 100rpx solid #FFFFFF;
					position: absolute;
					right: -60rpx;
					z-index: 99;
				}

				/* 向下的三角形 */
				.triangle-down {
					width: 0;
					height: 0;
					border-left: 50rpx solid transparent;
					border-right: 0 solid transparent;
					border-top: 100rpx solid #FFFFFF;
					position: absolute;
					left: -50rpx;
				}

			}
		}

		/* 信息容器 - 左右对称布局 */
		.info-container {
			background: #FFFFFF;
			border-radius: 24rpx;
			margin-top: -10rpx;
			position: relative;
			z-index: 9;

			.info-block {
				padding: 40rpx 30rpx;

				.info-content {
					display: flex;

					.info-content-status {
						color: #FFFFFF;
						font-size: 24rpx;
						padding: 0rpx 10rpx;
						display: flex;
						align-items: center;
						justify-content: center;
						border-radius: 4rpx;
					}

					.name {
						font-weight: 500;
						font-size: 32rpx;
						color: #333333;
						margin-left: 15rpx;
					}

				}

				.account {
					font-weight: 400;
					font-size: 26rpx;
					color: #999999;
					margin-top: 10rpx;
				}
			}

			.inof-line {
				height: 1rpx;
				background: #F0F0F0;
				margin: 0 30rpx;
			}

			.switbox {
				width: 134rpx;
				height: 46rpx;
				position: relative;

				image {
					width: 134rpx;
					height: 46rpx;
				}

				.switbox_tit {
					width: 90rpx;
					height: 44rpx;
					font-weight: 500;
					font-size: 24rpx;
					color: #FFFFFF;
					position: absolute;
					top: 0;
					display: flex;
					align-items: center;
					justify-content: center;

				}

				.switbox_tith {
					width: 90rpx;
					height: 44rpx;
					font-weight: 500;
					font-size: 24rpx;
					color: #FFFFFF;
					position: absolute;
					top: 0;
					right: 13rpx;
					display: flex;
					align-items: center;
					justify-content: end;
				}
			}
		}
	}

	.info_border {
		background: #FFFFFF;
		border-radius: 24rpx;
		margin: 20rpx 30rpx;
		padding: 30rpx;

		.info_box {
			display: flex;
			align-items: center;
			border-bottom: 1rpx solid #F0F0F0;
			padding: 30rpx 0;
		}

		.info_tit {
			font-weight: 500;
			font-size: 26rpx;
			color: #333333;
		}

		.info_input {
			flex: 1;
		}
	}

	.input-placeholder {
		font-weight: 400;
		font-size: 24rpx;
		color: #999999;
	}


	.taber {
		background: #FFFFFF;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		z-index: 99;
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 30rpx;

		.taber_l {
			text-align: center;

			image {
				width: 36rpx;
				height: 36rpx;
			}

			view {
				font-weight: 500;
				font-size: 26rpx;
				color: #333333;
			}
		}

		.taber_btn {
			width: 549rpx;
			height: 89rpx;
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