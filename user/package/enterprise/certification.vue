<template>
	<view style="padding-bottom: 40rpx;">
		<view v-if="compId" class="status flex align-center justify-center">
			<view class="status-box flex align-center justify-center">
				<text v-if="status === 0">审核中</text>
				<text v-if="status === 1">已通过</text>
				<text style="color: red;" v-if="status === 2">已拒绝</text>
			</view>
		</view>
		<view v-if="compId && status === 2 && auditReason" class="status flex align-center justify-center">
			<view class="status-box flex align-center justify-center">
				<text style="color: red;font-size: 24rpx;font-weight: normal;">拒绝理由：{{auditReason}}</text>
			</view>
		</view>
		<view class="froms flex align-center justify-center">
			<view class="froms-box">
				<up-form labelWidth="200" :labelStyle="labelStyle" labelPosition="top" :model="froms">
					<up-form-item label="企业名称" :borderBottom="true">
						<view class="textTop">
							<up-input @change="changeComp" :placeholderStyle="placeholderStyle"
								v-model="froms.enterpriseName" placeholder="请输入企业名称" border="none"></up-input>
							<view class="textTop-list" v-if="compList.length>0">
								<scroll-view scroll-y="true" style="width: 100%;max-height: 500rpx;">
									<view @click="selectCompItem(item)" class="textTop-list-item"
										v-for="(item,index) in compList" :key="index">
										{{item.companyName}}
									</view>
								</scroll-view>

							</view>
						</view>
					</up-form-item>
					<up-form-item @click="showCityFun()" label="公司所在地" :borderBottom="true">
						<up-input :placeholderStyle="placeholderStyle" v-model="froms.companyAddress" disabled
							disabledColor="#ffffff" placeholder="请选择公司所在地" border="none"></up-input>
						<template #right>
							<up-icon name="arrow-right"></up-icon>
						</template>
					</up-form-item>
					<up-form-item label="法人" :borderBottom="true">
						<up-input :placeholderStyle="placeholderStyle" v-model="froms.legalPerson"
							disabledColor="#ffffff" placeholder="请输入公司法人" border="none"></up-input>
					</up-form-item>
					<up-form-item label="税号" :borderBottom="true">
						<up-input :placeholderStyle="placeholderStyle" v-model="froms.creditCode"
							disabledColor="#ffffff" placeholder="请输入公司注册资金" border="none"></up-input>
					</up-form-item>
					<up-form-item label="注册资金" :borderBottom="true">
						<up-input :placeholderStyle="placeholderStyle" v-model="froms.registeredCapital"
							disabledColor="#ffffff" placeholder="请输入公司注册资金" border="none"></up-input>
					</up-form-item>
					<up-form-item @click="showTimeFun(1)" label="注册时间" :borderBottom="true">
						<up-input :placeholderStyle="placeholderStyle" v-model="froms.registrationTime" disabled
							disabledColor="#ffffff" placeholder="请选择公司注册时间" border="none"></up-input>
						<template #right>
							<up-icon name="arrow-right"></up-icon>
						</template>
					</up-form-item>
					<up-form-item @click="showTimeFun(2)" label="营业时间" :borderBottom="false">
						<up-input :placeholderStyle="placeholderStyle" v-model="froms.startTime" disabled
							disabledColor="#ffffff" placeholder="请选择公司营业时间" border="none"></up-input>
						<template #right>
							<up-icon name="arrow-right"></up-icon>
						</template>
					</up-form-item>
				</up-form>
			</view>
		</view>
		<view class="froms flex align-center justify-center">
			<view class="froms-box">
				<view class="froms-box-title">
					营业执照照片
				</view>
				<view @click="upLoudImg()" v-if="!froms.frontPhotoBusinessLicense"
					class="froms-box-image flex align-center justify-center">
					<up-icon name="camera" color="#282828" size="48"></up-icon>
					<view class="froms-box-image-txt">
						营业执照正面
					</view>
				</view>
				<view @click="upLoudImg()" v-else style="border: none;"
					class="froms-box-image flex align-center justify-center">
					<image :src="froms.frontPhotoBusinessLicense" mode="aspectFill"></image>
				</view>
			</view>
		</view>
		<view v-if="status === 2 || status === -1" class="bottomBtn flex align-center justify-center">
			<view @click="submitComp()" class="bottomBtn-box flex align-center justify-center">
				提交认证
			</view>
		</view>
		<!-- 城市选择 -->
		<up-cascader value-key="name" label-key="name" v-model:show="showCity" v-model="city" :data="areaData"
			@confirm="confirmCity"></up-cascader>
		<!-- 日期选择 -->
		<up-datetime-picker @cancel="closeTime" :show="showTime" v-model="time" mode="date" @confirm="confirmTime"
			@close="closeTime"></up-datetime-picker>
		<!-- 时间选择 -->
		<up-datetime-picker @cancel="closeTime" :show="showTimes" v-model="times" mode="time" @confirm="confirmTimes"
			@close="closeTime"></up-datetime-picker>
	</view>
</template>

<script setup>
	import config from '/common/config.js';
	import cityCode from '/utils/cityCode'
	import {
		reactive,
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	import {
		timeFormat
	} from '@/uni_modules/uview-plus';
	const {
		proxy
	} = getCurrentInstance();
	const froms = reactive({
		enterpriseName: '', //企业名称
		companyAddress: '', //企业所在位置
		legalPerson: '', //法人
		creditCode: '', //税号
		registeredCapital: '', //注册资金
		registrationTime: '', //注册时间
		startTime: '', //营业时间
		frontPhotoBusinessLicense: '', //营业执照
	});
	const labelStyle = reactive({
		color: '#000000',
		fontWeight: 800,
		fontSize: '28rpx',
	});
	const placeholderStyle = ref('fontSize:26rpx;')
	const status = ref(-1); //-1:未认证 0:待审核 1:已通过 2:已拒绝
	const showCity = ref(false);
	const city = ref([]);
	const areaData = ref(cityCode);
	const showTime = ref(false);
	const time = ref(Date.now());
	const showTimes = ref(false);
	const times = ref();
	const compId = ref('');
	const auditReason = ref(''); //拒绝理由
	const compList = ref([]); //搜索到的公司列表

	onLoad(option => {
		if (option.id) {
			compId.value = option.id
			appGetEnterpriseDetail();
		}
	});
	const selectCompItem = (item) => {
		froms.enterpriseName = item.companyName //企业名称
		// froms.companyAddress = item. //企业所在位置
		froms.legalPerson = item.legalPersonName //法人
		froms.creditCode = item.creditCode //税号
		froms.registeredCapital = item.regCapital //注册资金
		froms.registrationTime = item.establishTime //注册时间
		compList.value = []
	};
	//输入回调
	const changeComp = (e) => {
		// 增加防抖
		if (e) {
			setTimeout(() => {
				searchComp(e)
			}, 500)
		} else {
			compList.value = []
		}
	};
	//搜索公司信息
	const searchComp = (e) => {
		console.log(e)
		let data = {
			enterpriseName: e
		}
		// let str =
		// 	'{"code":"0","msg":"成功","isFee":1,"seqNo":"7cilxzfr7n9m0imh4ev9sn6wsd1pa82f","data":{"total":45,"items":[{"regStatus1":"开业","regStatus2":"正常","establishTime":"2019-09-26","regCapital":"100万人民币","companyType":"有限责任公司(自然人投资或控股)","companyName":"西安省钱兄网络科技有限公司","type":1,"legalPersonName":"马晓东","regNumber":"610133100347025","creditCode":"91610133MA6X5GMW0G","orgNumber":"MA6X5GMW0","province":"SAX"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2018-05-22","regCapital":"200万人民币","companyType":"有限责任公司(自然人独资)","companyName":"西安惠省钱信息网络有限公司","type":1,"legalPersonName":"闫鹏","regNumber":"610116100106745","creditCode":"91610116MA6UWWN62M","orgNumber":"MA6UWWN62","province":"SAX"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2024-09-30","regCapital":"10万人民币","companyType":"其他有限责任公司","companyName":"西安省钱到家财税服务有限公司","type":1,"legalPersonName":"舒乙桁","regNumber":"610104100790997","creditCode":"91610104MAE125LYX9","orgNumber":"MAE125LYX","province":"SAX"},{"regStatus1":"存续","regStatus2":"正常","establishTime":"2023-11-08","regCapital":"","companyType":"有限责任公司分公司(自然人投资或控股)","companyName":"西安省钱兄网络科技有限公司杭州分公司","type":1,"legalPersonName":"马晓东","regNumber":"330102000809267","creditCode":"91330102MAD42E0K72","orgNumber":"MAD42E0K7","province":"ZJ"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2014-12-22","regCapital":"","companyType":"有限责任公司分公司(自然人投资或控股)","companyName":"青海省钱家玉珠宝有限责任公司西安咸阳国际机场分公司","type":1,"legalPersonName":"李雨","regNumber":"611100200000907","creditCode":"91611101305638988M","orgNumber":"305638988","province":"SAX"},{"regStatus1":"存续（在营、开业、在册）","regStatus2":"正常","establishTime":"2025-05-15","regCapital":"100万人民币","companyType":"其他有限责任公司","companyName":"宁夏省钱兄网络科技有限公司","type":1,"legalPersonName":"马晓宏","regNumber":"640302001040198","creditCode":"91640302MAEKLWR47G","orgNumber":"MAEKLWR47","province":"NX"},{"regStatus1":"存续（在营、开业、在册）","regStatus2":"正常","establishTime":"2010-08-09","regCapital":"56060.606061万人民币","companyType":"有限责任公司(自然人投资或控股)","companyName":"北京微梦创科网络技术有限公司","type":1,"legalPersonName":"曹菲","regNumber":"110108013111119","creditCode":"91110108560395709H","orgNumber":"560395709","province":"BJ"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2025-05-28","regCapital":"10万人民币","companyType":"有限责任公司(自然人投资或控股)","companyName":"财秦雄企（西安）科技孵化器有限公司","type":1,"legalPersonName":"舒乙桁","regNumber":"610104100839323","creditCode":"91610104MAEMD6FK9K","orgNumber":"MAEMD6FK9","province":"SAX"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2019-07-12","regCapital":"1000万人民币","companyType":"有限责任公司(自然人独资)","companyName":"陕西泽霈实业有限公司","type":1,"legalPersonName":"张飞","regNumber":"610104100296233","creditCode":"91610104MA6X1ALH3E","orgNumber":"MA6X1ALH3","province":"SAX"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2014-06-06","regCapital":"500万人民币","companyType":"有限责任公司(自然人独资)","companyName":"西安金牌搬家服务有限公司","type":1,"legalPersonName":"李卫团","regNumber":"610135100020630","creditCode":"916101353978510641","orgNumber":"397851064","province":"SAX"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2017-04-13","regCapital":"100万人民币","companyType":"有限责任公司(自然人独资)","companyName":"陕西浩鑫财务管理有限公司","type":1,"legalPersonName":"李凤霞","regNumber":"610133100126819","creditCode":"91610133MA6U3PR876","orgNumber":"MA6U3PR87","province":"SAX"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2017-06-13","regCapital":"100万人民币","companyType":"有限责任公司(自然人独资)","companyName":"西安柏瑞动力机械设备有限公司","type":1,"legalPersonName":"周宇","regNumber":"610116100073375","creditCode":"91610116MA6U5JK1XQ","orgNumber":"MA6U5JK1X","province":"SAX"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2015-05-22","regCapital":"5万人民币","companyType":"有限责任公司(自然人投资或控股)","companyName":"西安美福搬家服务有限公司","type":1,"legalPersonName":"雷迎锋","regNumber":"610135100030963","creditCode":"91610135333779868C","orgNumber":"333779868","province":"SAX"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2012-04-10","regCapital":"5万人民币","companyType":"有限责任公司(自然人投资或控股)","companyName":"西安龙盾搬家有限公司","type":1,"legalPersonName":"卢财斌","regNumber":"610135100013333","creditCode":"916101355922255449","orgNumber":"592225544","province":"SAX"},{"regStatus1":"注销","regStatus2":"注销","establishTime":"2024-03-06","regCapital":"10万人民币","companyType":"有限责任公司","companyName":"深圳省钱兄网络科技有限公司","type":1,"legalPersonName":"张迪","regNumber":"440300221765587","creditCode":"91440300MADBTK206Y","orgNumber":"MADBTK206","province":"GD"},{"regStatus1":"注销","regStatus2":"注销","establishTime":"2018-11-26","regCapital":"50万人民币","companyType":"其他有限责任公司","companyName":"宁夏德力智能仓储设备有限公司","type":1,"legalPersonName":"杨治国","regNumber":"640100001044842","creditCode":"91640100MA770H299D","orgNumber":"MA770H299","province":"NX"},{"regStatus1":"注销","regStatus2":"注销","establishTime":"2015-05-21","regCapital":"341.526368万人民币","companyType":"有限责任公司(自然人投资或控股)","companyName":"厦门可可家科技有限公司","type":1,"legalPersonName":"郑立斌","regNumber":"350203200559657","creditCode":"913502033032368628","orgNumber":"303236862","province":"FJ"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2021-02-23","regCapital":"","companyType":"个体工商户","companyName":"西安市莲湖区惠省钱便利店","type":1,"legalPersonName":"张珂","regNumber":"610104601497488","creditCode":"92610104MAB0QYAC34","orgNumber":"MAB0QYAC3","province":"SAX"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2019-04-16","regCapital":"","companyType":"个体工商户","companyName":"西安市碑林区省钱王商务信息咨询部","type":1,"legalPersonName":"刘伟","regNumber":"610103601386960","creditCode":"92610103MA6WNT212N","orgNumber":"MA6WNT212","province":"SAX"},{"regStatus1":"开业","regStatus2":"正常","establishTime":"2019-04-20","regCapital":"","companyType":"个体工商户","companyName":"西安市莲湖区省钱连萌网络工作室","type":1,"legalPersonName":"张升锋","regNumber":"610104601076137","creditCode":"92610104MA6WP8ND8B","orgNumber":"MA6WP8ND8","province":"SAX"}]}}'
		// let obj = JSON.parse(str)
		// compList.value = obj.data.items
		// console.log(obj)
		proxy.$http.get('/app/enterprise/getEnterpriseInfo', data).then(res => {
			if (res.code == 0) {
				let obj = JSON.parse(res.data)
				if (obj.data.items.length > 0) {
					compList.value = obj.data.items
				} else {
					compList.value = []
				}
			} else {
				compList.value = []
			}
		})
	};
	//获取企业认证信息
	const appGetEnterpriseDetail = () => {
		let data = {
			id: compId.value,
			userId: uni.getStorageSync('userId')
		}
		proxy.$http.getT('/app/enterprise/appGetEnterpriseDetail', data).then(res => {
			if (res.code == 0) {
				froms.enterpriseName = res.data.enterpriseName //企业名称
				froms.companyAddress = res.data.companyAddress //企业所在位置
				froms.legalPerson = res.data.legalPerson //法人
				froms.creditCode = res.data.creditCode
				froms.registeredCapital = res.data.registeredCapital //注册资金
				froms.registrationTime = res.data.registrationTime //注册时间
				froms.startTime = res.data.startTime //营业时间
				froms.frontPhotoBusinessLicense = res.data.frontPhotoBusinessLicense //营业执照
				auditReason.value = res.data.auditReason
				status.value = res.data.status
			}
		})
	};
	const confirmTimes = (e) => {
		froms.startTime = e.value
		closeTime();
	};
	const closeTime = () => {
		showTime.value = false
		showTimes.value = false
	};
	const showTimeFun = (type) => {
		if (type === 1) {
			showTime.value = true
		} else {
			showTimes.value = true
		}
	};
	const confirmTime = (e) => {
		froms.registrationTime = timeFormat(e.value, 'yyyy-mm-dd');
		closeTime();
	};
	const confirmCity = (e) => {
		froms.companyAddress = city.value[0] + '' + city.value[1] + '' + city.value[2]
	};
	const showCityFun = () => {
		showCity.value = true
	};
	const submitComp = () => {
		if (!froms.enterpriseName) {
			uni.showToast({
				title: '请输入企业名称',
				icon: 'none'
			})
			return
		}
		if (!froms.companyAddress) {
			uni.showToast({
				title: '请选择公司所在地',
				icon: 'none'
			})
			return
		}
		if (!froms.legalPerson) {
			uni.showToast({
				title: '请输入公司法人',
				icon: 'none'
			})
			return
		}
		if (!froms.creditCode) {
			uni.showToast({
				title: '请输入公司税号',
				icon: 'none'
			})
			return
		}
		if (!froms.registeredCapital) {
			uni.showToast({
				title: '请输入注册资金',
				icon: 'none'
			})
			return
		}
		if (!froms.registrationTime) {
			uni.showToast({
				title: '请选择注册时间',
				icon: 'none'
			})
			return
		}
		if (!froms.startTime) {
			uni.showToast({
				title: '请选择营业时间',
				icon: 'none'
			})
			return
		}
		if (!froms.frontPhotoBusinessLicense) {
			uni.showToast({
				title: '请上传营业执照',
				icon: 'none'
			})
			return
		}
		let data = JSON.parse(JSON.stringify(froms));
		data.userId = uni.getStorageSync('userId')
		if (compId.value) {
			data.id = compId.value
		}
		uni.showModal({
			title: '提示',
			content: '确认提交企业认证吗？',
			complete: (ret) => {
				if (ret.confirm) {
					uni.showLoading({
						title: '提交中...'
					})
					proxy.$http.postJson('/app/enterprise/addEnterprise', data).then(res => {
						if (res.code == 0) {
							uni.hideLoading()
							uni.showToast({
								title: '已提交'
							})
							uni.navigateBack()
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
							froms.frontPhotoBusinessLicense = url.data
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

	.textTop {
		width: 100%;
		position: relative;

		.textTop-list {
			width: 580rpx;
			max-height: 500rpx;
			background-color: #ffffff;
			// box-shadow: 2rpx 4rpx 4rpx 2rpx rgba(0, 0, 0, 0.2);
			// border: 1px solid #999999;
			box-shadow: 0 0 4rpx 2rpx rgba(0, 0, 0, 0.2);
			border-radius: 24rpx;
			position: absolute;
			top: 60rpx;
			z-index: 99;
			padding: 0 20rpx;
			padding-bottom: 20rpx;

			.textTop-list-item {
				margin-top: 20rpx;
				padding-bottom: 20rpx;
				border-bottom: 1px solid #cccccc;
			}
		}
	}
</style>