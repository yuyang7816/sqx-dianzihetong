<template>
	<view>
		<view class="froms flex align-center justify-center">
			<!-- 个人 -->
			<view class="froms-box" v-if="type === 1">
				<up-form labelPosition="left" labelWidth="100" :model="from1" ref="form1">
					<up-form-item label="姓名" :borderBottom="true">
						<up-input inputAlign="right" v-model="from1.signUserName" placeholder="请输入真实姓名"
							border="none"></up-input>
					</up-form-item>
					<up-form-item label="手机号" :borderBottom="true">
						<up-input inputAlign="right" v-model="from1.signUserPhone" placeholder="请输入手机号"
							border="none"></up-input>
					</up-form-item>
					<up-form-item label="身份证号" :borderBottom="true">
						<up-input inputAlign="right" v-model="from1.idCardNumber" placeholder="请输入身份证号"
							border="none"></up-input>
					</up-form-item>

				</up-form>
			</view>
			<!-- 企业 -->
			<view class="froms-box" v-if="type === 2">
				<up-form labelPosition="top" labelWidth="100" :model="from2" ref="form1">
					<up-form-item label="企业名称" :borderBottom="true">
						<view class="textTop">
							<up-input @change="changeComp" v-model="from2.signUserName" placeholder="请输入企业名称"
								border="none"></up-input>
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
					<up-form-item label="企业法人" :borderBottom="true">
						<up-input v-model="from2.signUserPhone" placeholder="请输入企业法人" border="none"></up-input>
					</up-form-item>
				</up-form>
			</view>
		</view>
		<view class="submitBtn flex align-center justify-center">
			<view @click="submit()" class="submitBtn-box flex align-center justify-center">
				确认
			</view>
		</view>
	</view>
</template>

<script setup>
	import {
		onLoad
	} from '@dcloudio/uni-app';
	import {
		reactive,
		ref
	} from 'vue';

	const type = ref(1); //1：个人 2:企业

	//个人
	const from1 = reactive({
		signUserName: '', //用户名称
		idCardNumber: '', //身份证号
		signUserPhone: '', //手机号
		type: 1
	})
	//企业
	const from2 = reactive({
		signUserName: '', //企业名称
		signUserPhone: '', //企业法人
		type: 2
	})
	const compList = ref([]);
	onLoad(option => {
		type.value = Number(option.type)
	})
	// const closeComp = () => {
	// 	compList.value = []
	// };
	const selectCompItem = (item) => {
		from2.signUserName = item.companyName //企业名称
		from2.signUserPhone = item.legalPersonName //企业法人
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
	//保存
	const submit = () => {
		if (type.value === 1) { //个人
			if (!from1.signUserName) {
				uni.showToast({
					title: '请输入真实姓名',
					icon: 'none'
				})
				return
			}
			if (!from1.signUserPhone) {
				uni.showToast({
					title: '请输入手机号',
					icon: 'none'
				})
				return
			}
			if (!from1.idCardNumber) {
				uni.showToast({
					title: '请输入身份证号',
					icon: 'none'
				})
				return
			}
			uni.$emit('froms', from1)
		} else { //企业
			if (!from2.signUserName) {
				uni.showToast({
					title: '请输入企业名称',
					icon: 'none'
				})
				return
			}
			if (!from2.signUserPhone) {
				uni.showToast({
					title: '请输入企业法人名称',
					icon: 'none'
				})
				return
			}
			uni.$emit('froms', from2)
		}
		uni.navigateBack()
	};
</script>

<style lang="scss" scoped>
	.froms {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.froms-box {
			width: calc(686rpx - 60rpx);
			height: 100%;
			padding: 30rpx;
			background-color: #ffffff;
			border-radius: 24rpx;
		}
	}

	.submitBtn {
		width: 100%;
		height: auto;
		margin-top: 60rpx;

		.submitBtn-box {
			width: 686rpx;
			height: 88rpx;
			background-color: #0096FF;
			border-radius: 16rpx;
			color: #ffffff;
			font-size: 30rpx;
			font-weight: bold;
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