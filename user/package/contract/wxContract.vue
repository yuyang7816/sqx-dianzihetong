<template>
	<view style="padding-bottom: 200rpx;">
		<view class="info_box">
			<view class="info_tit"><text>*</text>合同文件</view>
			<view class="info_img flex align-center justify-between flex-wrap">
				<image class="info_img-i" @click="prewImg(index,fileImgList)" :src="item" mode="widthFix"
					v-for="(item,index) in fileImgList" :key="index">
				</image>
				<view class="info_img-i" style="height: 0;"></view>
			</view>
			<view class="info_btn" v-if="fileImgList.length == 0" @click="addFile()">+添加合同文件</view>
		</view>
		<view class="info_box">
			<view class="info_tit"><text>*</text>签署人</view>
			<!-- 信息 -->
			<view v-if="topInfoType===-1" @click="showTypes(1)" class="info-block flex align-center justify-between">
				<view class="info-content">
					<view :style="{backgroundColor:'#0096ff'}" class="info-content-status">
						我
					</view>
					<view class="name" style="font-size: 28rpx;">请填写</view>
				</view>
				<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
			</view>
			<!-- 个人 -->
			<view v-if="topInfoType===1" @click="showTypes(1)" class="info-block flex align-center justify-between">
				<view>
					<view class="info-content">
						<view :style="{backgroundColor:'#0096ff'}" class="info-content-status">
							个人
						</view>
						<view class="name">{{myQueryInsert.name}}</view>
					</view>
					<view class="account">{{myQueryInsert.realPhone}} | {{myQueryInsert.idNumber}}</view>
				</view>
				<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
			</view>
			<view v-if="topInfoType===2" @click="showTypes(1)" class="info-block flex align-center justify-between">
				<view>
					<view class="info-content">
						<view :style="{backgroundColor:'#ffb109'}" class="info-content-status">
							企业
						</view>
						<view class="name">{{enterprise[0].enterpriseName}}</view>
					</view>
					<view class="account">{{enterprise[0].legalPerson}}</view>
				</view>
				<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
			</view>
			<!-- 对方信息 -->
			<!-- <view @click="showTypes(2)" v-if="downInfoType === -1"
				class="info-block flex align-center justify-between padding-right">
				<view>
					<view class="info-content">
						<view :style="{backgroundColor:tabIndex==2?'#0096ff':'#ffb109'}" class="info-content-status">
							{{tabIndex==2?'甲方':'乙方'}}
						</view>
						<view class="name" style="font-size: 28rpx;">请填写</view>
					</view>
				</view>
				<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
			</view> -->
			<view v-if="isShowTwo">
				<!-- 个人 -->
				<view @click="showTypes(2)" v-if="downInfoType === 1"
					class="info-block flex align-center justify-between padding-right">
					<view>
						<view class="info-content">
							<view :style="{backgroundColor:'#0096ff'}" class="info-content-status">
								个人
							</view>
							<view class="name">{{byUserInfo.signUserName}}</view>
						</view>
						<view class="account">{{byUserInfo.signUserPhone}} | {{byUserInfo.idCardNumber}}</view>
					</view>
					<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
				</view>
				<!-- 企业 -->
				<view @click="showTypes(2)" v-if="downInfoType === 2"
					class="info-block flex align-center justify-between padding-right">
					<view>
						<view class="info-content">
							<view :style="{backgroundColor:'#ffb109'}" class="info-content-status">
								企业
							</view>
							<view class="name">{{byUserInfo.signUserName}}</view>
						</view>
						<view class="account">{{byUserInfo.signUserPhone}}</view>
					</view>
					<up-icon name="arrow-right" color="#333333" size="20"></up-icon>
				</view>
			</view>
			<view class="info_btn" @click="showTypes(2)" v-if="isShowTwo == false">+添加签署方</view>
		</view>
		<view class="info_box">
			<view class="info_tit"><text>*</text>合同名称</view>
			<view class="info_Text">
				<up-textarea v-model="contractName" placeholder="请输入合同名称" border="none"
					placeholderStyle="color:#999999;font-size:26rpx;"></up-textarea>
			</view>
		</view>


		<view class="info_suer">
			<view class="infoIte" @click="showDay = true">
				<view class="infoIte_tit"><text>*</text>合同签署日期</view>
				<view class="infoIte_data">
					{{lateSignDays>0?'合同发起后' + lateSignDays + '天':'过期则无法签署'}}
					<up-icon name="arrow-right" color="#999999" size="15"></up-icon>
				</view>
			</view>

			<!-- <view class="infoIte padding-tb-xl">
				<view class="infoIte_tit"><text>*</text>附件</view>
				<view class="infoIte_data">
					<up-icon name="plus-circle" color="#0096FF" size="25"></up-icon>
				</view>
			</view> -->
			<!-- <view class="infoIte ">
				<view class="infoIte_tit"><text>*</text>收件人</view>
				<view class="infoIte_data">
					<up-icon name="plus-circle" color="#0096FF" size="25"></up-icon>
				</view>
			</view> -->
		</view>


		<view class="taber">
			<view @click="submitCg()" class="taber_l">
				<image src="/package/static/caogao.png"></image>
				<view>存为草稿</view>
			</view>
			<view class="taber_btn" @click="submits()">创建合同</view>
		</view>

		<up-action-sheet round="24" zIndex="100" :closeOnClickAction="false" :safeAreaInsetBottom="true" :actions="list"
			:title="title" :show="show" @select="selectAction" @close="show = false"></up-action-sheet>
		<!-- 日期 -->
		<up-picker :show="showDay" :columns="columns" @cancel="showDay = false" @close="showDay=false"
			@confirm="confirmDay"></up-picker>
	</view>
</template>

<script setup>
	import config from '../../common/config.js';
	// #ifdef APP
	const lemonjkFileSelect = uni.requireNativePlugin('lemonjk-FileSelect');
	// #endif
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
	const isType = ref(1); //1:上 2:下
	const enterprise = reactive([]); //企业认证信息
	const myQueryInsert = reactive({}); //个人认证信息
	const title = ref('类型');
	const show = ref(false); //是否显示类型
	const list = ref([{
		name: '个人',
		fontSize: '12',
		type: 1
	}, {
		name: '企业',
		fontSize: '12',
		type: 2
	}]);
	const topInfoType = ref(-1); //上方信息 -1:没有选择 1:个人 2:企业
	const downInfoType = ref(-1); //下方信息 -1:没有选择 1:个人 2:企业
	const byUserInfo = reactive({}); //对方信息
	const fileImgList = reactive([]); //上传的文件图片
	const contractName = ref(''); //合同名称
	const showDay = ref(false); //是否显示日期选择器
	const columns = reactive([]); //日期数组
	const lateSignDays = ref(0);
	const detailId = ref(''); //草稿id
	const isShowTwo = ref(false); //是否显示乙方
	onLoad(option => {
		if (option.detailId) {
			detailId.value = option.detailId
			getDetailInfo();
		}
		queryInsert();
		enterpriseDetail();
		getColumns();
	});
	onShow(() => {
		uni.$once('froms', data => {
			Object.keys(byUserInfo).forEach(key => delete byUserInfo[key])
			Object.assign(byUserInfo, data)
			downInfoType.value = Number(byUserInfo.type)
			delete byUserInfo.type
			isShowTwo.value = true
		})
	});
	onPullDownRefresh(() => {

	});
	onReachBottom(() => {

	});
	//预览图片
	const prewImg = (index, imgs) => {
		uni.previewImage({
			urls: imgs,
			current: index
		})
	};
	//获取合同详情（草稿箱）
	const getDetailInfo = () => {
		let data = {
			id: detailId.value,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/contract/selectByContractDetail', data).then(res => {
			if (res.code == 0) {
				fileImgList.splice(0, fileImgList.length, ...res.data.fileUrl.split(','))
				contractName.value = res.data.contractName
				lateSignDays.value = res.data.lateSignDays
				if (res.data.contractAndUsers.length > 0) {
					if (res.data.contractAndUsers.length == 2) { //多人
						topInfoType.value = res.data.contractAndUsers[0].type == 1 ? 2 : 1
						// downInfoType.value = res.data.contractAndUsers[1].type == 1 ? 2 : 1
						downInfoType.value = res.data.contractAndUsers[1].agreementType ? 2 : 1
						if (downInfoType.value === 1) { //个人
							byUserInfo.signUserName = res.data.contractAndUsers[1].signUserName
							byUserInfo.signUserPhone = res.data.contractAndUsers[1].signUserPhone
							byUserInfo.idCardNumber = res.data.contractAndUsers[1].idCardNumber
						} else { //企业
							byUserInfo.signUserName = res.data.contractAndUsers[1].signUserName
							byUserInfo.signUserPhone = res.data.contractAndUsers[1].signUserPhone
						}
						isShowTwo.value = true
					} else { //单人
						topInfoType.value = res.data.contractAndUsers[0].type == 1 ? 2 : 1
						isShowTwo.value = false
					}

				}
			}
		})
	};
	//创建存为草稿
	const submitCg = () => {
		if (fileImgList.length == 0) {
			uni.showToast({
				title: '请上传合同文件',
				icon: 'none'
			})
			return
		}
		if (topInfoType.value == -1) {
			uni.showToast({
				title: '请填写签署人',
				icon: 'none'
			})
			return
		}
		if (isShowTwo.value && downInfoType.value == -1) {
			uni.showToast({
				title: '请填写签署人',
				icon: 'none'
			})
			return
		}
		if (!contractName.value) {
			uni.showToast({
				title: '请填写合同名称',
				icon: 'none'
			})
			return
		}
		if (lateSignDays.value === 0) {
			uni.showToast({
				title: '请选择签署日期',
				icon: 'none'
			})
			return
		}
		let data = {}
		if (isShowTwo.value === true) {
			data = {
				pdfImageUrl: fileImgList.join(','),
				contractName: contractName.value, //合同名称
				lateSignDays: lateSignDays.value, //签署日期
				type: topInfoType.value === 2 ? 1 : 2, //只要我是企业，那么就是企业合同 ：1企业 2个人
				isPrincipal: 1, //我是 1:甲方 2:乙方
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
		} else {
			data = {
				pdfImageUrl: fileImgList.join(','),
				contractName: contractName.value, //合同名称
				lateSignDays: lateSignDays.value, //签署日期
				type: topInfoType.value === 2 ? 1 : 2, //只要我是企业，那么就是企业合同 ：1企业 2个人
				isPrincipal: 1, //我是 1:甲方 2:乙方
				userName: topInfoType.value === 1 ? myQueryInsert.name : enterprise[0]
					.enterpriseName, // topInfoType 1:个人 2:企业
				userPhone: topInfoType.value === 1 ? myQueryInsert.realPhone : '', //个人手机号
				idCard: topInfoType.value === 1 ? myQueryInsert.idNumber : '', //个人身份证号
				userLegalPerson: topInfoType.value === 2 ? enterprise[0].legalPerson : '',
			}
		}

		uni.showModal({
			title: '提示',
			content: '确认存为草稿吗？',
			complete: (ret) => {
				if (ret.confirm) {
					uni.showLoading({
						title: '存草稿中...'
					})
					let url = ''
					if (isShowTwo.value == true) {
						url = '/app/contract/addDraftBox'
					} else {
						url = '/app/contract/addSingleDraftBox'
					}
					proxy.$http.postJson(url, data).then(res => {
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
	//创建合同
	const submits = () => {
		if (fileImgList.length == 0) {
			uni.showToast({
				title: '请上传合同文件',
				icon: 'none'
			})
			return
		}
		if (topInfoType.value == -1) {
			uni.showToast({
				title: '请填写签署人',
				icon: 'none'
			})
			return
		}
		if (isShowTwo.value && downInfoType.value == -1) {
			uni.showToast({
				title: '请填写签署人',
				icon: 'none'
			})
			return
		}
		if (!contractName.value) {
			uni.showToast({
				title: '请填写合同名称',
				icon: 'none'
			})
			return
		}
		if (lateSignDays.value === 0) {
			uni.showToast({
				title: '请选择签署日期',
				icon: 'none'
			})
			return
		}
		let data = {}
		if (isShowTwo.value == true) { //多人
			data = {
				pdfImageUrl: fileImgList.join(','),
				contractName: contractName.value, //合同名称
				lateSignDays: lateSignDays.value, //签署日期
				type: topInfoType.value === 2 ? 1 : 2, //只要我是企业，那么就是企业合同 ：1企业 2个人
				isPrincipal: 1, //我是 1:甲方 2:乙方
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
		} else { //单人
			data = {
				pdfImageUrl: fileImgList.join(','),
				contractName: contractName.value, //合同名称
				lateSignDays: lateSignDays.value, //签署日期
				type: topInfoType.value === 2 ? 1 : 2, //只要我是企业，那么就是企业合同 ：1企业 2个人
				isPrincipal: 1, //我是 1:甲方 2:乙方
				userName: topInfoType.value === 1 ? myQueryInsert.name : enterprise[0]
					.enterpriseName, // topInfoType 1:个人 2:企业
				userPhone: topInfoType.value === 1 ? myQueryInsert.realPhone : '', //个人手机号
				idCard: topInfoType.value === 1 ? myQueryInsert.idNumber : '', //个人身份证号
				userLegalPerson: topInfoType.value === 2 ? enterprise[0].legalPerson : '',
			}
		}

		uni.showModal({
			title: '提示',
			content: '确认创建合同吗？',
			complete: (ret) => {
				if (ret.confirm) {
					uni.showLoading({
						title: '合同创建中...'
					})
					let url = ''
					if (isShowTwo.value == true) {
						url = '/app/contract/initiateByWx'
					} else {
						url = '/app/contract/initiateSingleContract'
					}
					proxy.$http.postJson(url, data).then(res => {
						if (res.code == 0) {
							uni.hideLoading()
							let userId = uni.getStorageSync('userId')
							uni.redirectTo({
								url: '/package/contract/preview?contractId=' +
									res.data + '&type=' + data
									.type + '&contontUserId=' + userId + '&pdfImageUrl=' +
									fileImgList.join(',')
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

	//选择日期
	const confirmDay = (e) => {
		console.log(e.indexs[0])
		lateSignDays.value = e.indexs[0] + 1
		showDay.value = false
	};
	//动态获取
	const getColumns = () => {
		// columns
		let arr = []
		for (let i = 0; i < 30; i++) {
			arr.push('合同发起后' + (i + 1) + '天')
		}
		columns.splice(0, columns.length, arr)
	};
	//从微信选择文件
	const addFile = () => {
		// #ifdef MP-WEIXIN
		wx.chooseMessageFile({
			count: 1,
			type: 'file',
			extension: ['doc', 'docx', 'pdf', 'xlsx', 'xls'],
			success: (ret) => {
				console.log(ret.tempFiles[0].path)
				// ret.tempFiles[0].name
				uni.showLoading({
					title: '上传中...'
				})
				uploadFiles(ret.tempFiles[0].path);
				contractName.value = ret.tempFiles[0].name
			},
			fail: (err) => {
				console.log(err)
			}
		})
		// #endif
		// #ifdef H5
		uni.chooseFile({
			count: 1,
			extension: ['doc', 'docx', 'pdf', 'xlsx', 'xls'],
			success: (ret) => {
				console.log(ret)
				uploadFiles(ret.tempFilePaths[0]);
				contractName.value = ret.tempFiles[0].name
			},
			fail: (err) => {
				console.log(err)
			}

		})
		// #endif
		// #ifdef APP
		uni.showLoading({
			title: '加载中...'
		})
		if (uni.getSystemInfoSync().osName == 'ios') {
			lemonjkFileSelect.showPicker({
				pathScope: "/Download",
				// mimeType: "text/plain",
				utisType: ["com.microsoft.word.doc", "com.microsoft.excel.xls"],
			}, result => {
				console.log(result);
				if (result.code == 0) { //文件读取成功
					// 拿到文件路径
					const filePath = result.files[0].filePath
					contractName.value = result.files[0].fileName.split('.')[0]
					uploadFiles(filePath);
				} else {
					if (result.code == 1 || result.code == 1001 || result.code == 1004) {
						uni.hideLoading()
						uni.showToast({
							title: '读取失败，请重试',
							icon: 'none'
						})
					}
				}
			})
		} else {
			plus.android.requestPermissions(['android.permission.WRITE_EXTERNAL_STORAGE'], function(e) {
				if (e.deniedAlways.length > 0) { //权限被永久拒绝
					// 弹出提示框解释为何需要读写手机储存权限，引导用户打开设置页面开启
					uni.showModal({
						title: '存储权限',
						content: '您拒绝了存储权限，请前往设置-应用开启存储权限。',
						success: function(res) {
							if (res.confirm) {
								// console.log('用户点击确定');
							} else if (res.cancel) {
								// console.log('用户点击取消');
							}
						}
					});
				}
				if (e.deniedPresent.length > 0) { //权限被临时拒绝
					// 弹出提示框解释为何需要读写手机储存权限，可再次调用plus.android.requestPermissions申请权限
					plus.android.requestPermissions(['android.permission.WRITE_EXTERNAL_STORAGE'])

				}
				if (e.granted.length > 0) { //权限被允许
					//调用依赖获取读写手机储存权限的代码
					getandFile();
				}
			});
		}
		// #endif
	};
	//安卓文件选择
	const getandFile = () => {
		lemonjkFileSelect.showNativePicker({
			pathScope: "/Download",
			mimeType: "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
			// utisType: ["public.data"],
			filterConfig: { //对象里配置的属性要同时满足   
				// fileName:['base.apk','config.txt','配置文件.yaml'],  //属性数组满足其中一项
				// fileSize:String(1*1024*1024),  // 单位：byte(字节)  //属性数组满足其中一项
				fileExtension: ['doc', 'docx', 'pdf', 'xlsx', 'xls'], //属性数组满足其中一项 
			}
		}, result => {
			console.log(result);
			// return
			if (result.code == 0) { //文件读取成功
				// 拿到文件路径
				const filePath = result.files[0].filePath
				contractName.value = result.files[0].fileName.split('.')[0]
				//上传文件到服务器
				uploadFiles(filePath);

			} else {
				if (result.code == 1 || result.code == 1001 || result.code == 1004) {
					uni.hideLoading()
					uni.showToast({
						title: '读取失败，请重试',
						icon: 'none'
					})
				}

			}
		})
	};
	//上传文件
	const uploadFiles = (fileSrc) => {
		console.log(fileSrc, '11111111')
		uni.showLoading({
			title: '上传中...'
		})
		uni.uploadFile({
			url: config.APIHOST + '/alioss/upload', //真实的接口地址
			// url: '' // 填写自己的上传接口地址
			filePath: fileSrc,
			header: {
				token: uni.getStorageSync('token')
			},
			name: 'file',
			fail: (ree) => {
				console.log(ree)
			},
			success: uploadFileRes => {

				let url = JSON.parse(uploadFileRes.data);
				console.log(url.data)
				if (url.code == 0) {
					let data = {
						filePath: url.data
					}
					proxy.$http.postT('/app/contract/convertFile', data).then(res => {
						if (res.code == 0) {
							uni.hideLoading()
							uni.showToast({
								title: '已上传'
							})
							fileImgList.splice(0, fileImgList.length, ...res.data)
						} else {
							uni.hideLoading();
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
				break;
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
</script>

<style lang="scss" scoped>
	.info_box {
		width: calc(100% - 60rpx);
		// height: 190rpx;
		background: #FFFFFF;
		padding: 30rpx;
		margin-bottom: 20rpx;

		.info-block {
			// width: calc(100% - );
			padding: 30rpx;
			background: rgba(0, 150, 255, 0.1);
			border-radius: 16rpx;
			margin-top: 20rpx;

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

		.info_img {
			.info_img-i {
				width: 210rpx;
				margin-top: 20rpx;
			}
		}

		.info_tit {


			text {
				color: #FF000E;
				margin-right: 6rpx;
			}

			font-weight: 500;
			font-size: 32rpx;
			color: #333333;
		}

		.info_btn {
			width: 346rpx;
			height: 80rpx;
			background: #0096FF;
			border-radius: 12rpx;
			font-weight: 400;
			font-size: 26rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
			margin: 30rpx auto 0;
		}

		.info_Text {
			width: 686rpx;
		}
	}

	.info_suer {
		background: #FFFFFF;
		padding: 30rpx;
		margin-bottom: 20rpx;

		.infoIte {
			display: flex;
			align-items: center;

			.infoIte_tit {
				text {
					color: #FF000E;
					margin-right: 6rpx;
				}

				font-weight: 500;
				font-size: 32rpx;
				color: #333333;
			}

			.infoIte_data {
				flex: 1;
				display: flex;
				align-items: center;
				justify-content: flex-end;
				font-weight: 400;
				font-size: 26rpx;
				color: #999999;
			}
		}
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