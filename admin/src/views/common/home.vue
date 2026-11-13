<template>
	<div>
		<div class="record_date">
			<el-row>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">总用户数</div>
							<div class="text_color"><span>{{tableData.totalUsers ? tableData.totalUsers : 0}}</span>人
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">今日新增</div>
							<div class="text_color"><span>{{tableData.newToday ? tableData.newToday : 0}}</span>人</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本月新增</div>
							<div class="text_color"><span>{{tableData.newMonth ? tableData.newMonth : 0}}</span>人</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本年新增</div>
							<div class="text_color"><span>{{tableData.newYear ? tableData.newYear : 0}}</span>人</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">今日交易订单 </div>
							<div class="text_color">
								<span>{{tableData.totalRevenue ? tableData.totalRevenue : 0}}</span>元
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本月交易订单</div>
							<div class="text_color">
								<span>{{tableData.todayRevenue ? tableData.todayRevenue : 0 }}</span>元
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">今日收入 </div>
							<div class="text_color">
								<span>{{tableData.monthRevenue ? tableData.monthRevenue : 0}}</span>元
							</div>
						</div>
					</div>
				</el-col>
				<el-col :span="6" class="cards">
					<div class="box">
						<div class="box_num">
							<div class="box_color">本月收入</div>
							<div class="text_color"><span>{{tableData.yearRevenue ? tableData.yearRevenue : 0}}</span>元
							</div>
						</div>
					</div>
				</el-col>
			</el-row>
			<div>
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="orderfenxi">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info.stockDate" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间" @change="animeOrder">
				</el-date-picker>
			</div>
			<el-tabs v-model="activeName" @tab-click="handleClick" style="margin-left: 10px;">
				<el-tab-pane label="用户分析" name="third">
					<el-row>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">总用户统计</div>
									<div class="text_color"><span>{{userStat.totalNumber ? userStat.totalNumber : 0 }}</span>人</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">普通用户</div>
									<div class="text_color"><span>{{userStat.userNumber ? userStat.userNumber : 0}}</span>人</div>
								</div>
							</div>
						</el-col>
						
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">vip用户</div>
									<div class="text_color"><span>{{userStat.vipUserNumber ? userStat.vipUserNumber : 0}}</span>人</div>
								</div>
							</div>
						</el-col> 
					</el-row>
				</el-tab-pane>
				<el-tab-pane label="合同统计" name="first">
					<el-row>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">新增合同数</div>
									<div class="text_color"><span>{{rechgeData.count ? rechgeData.count : 0}}</span>
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">签署合同数</div>
									<div class="text_color"><span>{{rechgeData.count1 ? rechgeData.count1 : 0}}</span>
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">新增企业数</div>
									<div class="text_color">
										<span>{{rechgeDataQy.count ? rechgeDataQy.count : 0}}</span></div>
								</div>
							</div>
						</el-col>
					</el-row>
				</el-tab-pane>
				<el-tab-pane label="充值统计" name="sixth">
					<el-row>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">充值会员总金额</div>
									<div class="text_color"><span>{{chongzhiData.allMoney ? chongzhiData.allMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">小程序充值会员金额</div>
									<div class="text_color">
										<span>{{chongzhiData.wxAppMoney ? chongzhiData.wxAppMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">APP充值会员金额</div>
									<div class="text_color">
										<span>{{chongzhiData.appMoney ? chongzhiData.appMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">微信二维码充值会员金额</div>
									<div class="text_color">
										<span>{{chongzhiData.wxRwmMoney ? chongzhiData.wxRwmMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">支付宝APP充值会员金额</div>
									<div class="text_color">
										<span>{{chongzhiData.zfbMoney ? chongzhiData.zfbMoney : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">支付宝H5充值会员金额</div>
									<div class="text_color">
										<span>{{chongzhiData.zfbH5Money ? chongzhiData.zfbH5Money : 0}}</span>元
									</div>
								</div>
							</div>
						</el-col>
						
					</el-row>
				</el-tab-pane>
				<el-tab-pane label="待审核统计" name="seven">
					<el-row>
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">认证待审核</div>
									<div class="text_color"><span style="color: red;cursor: pointer;" @click="btnDsh(1)">{{chatNumRz ? chatNumRz : 0 }}</span>个</div>
								</div>
							</div>
						</el-col>
						<!-- <el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">服务待审核</div>
									<div class="text_color"><span style="color: red;cursor: pointer;" @click="btnDsh(2)">{{chatNumFw ? chatNumFw : 0}}</span>个</div>
								</div>
							</div>
						</el-col> -->
						
						<el-col :span="6" class="cards">
							<div class="box">
								<div class="box_num">
									<div class="box_color">企业待审核</div>
									<div class="text_color"><span style="color: red;cursor: pointer;" @click="btnDsh(3)">{{chatNumDp ? chatNumDp : 0}}</span>个</div>
								</div>
							</div>
						</el-col> 
					</el-row>
				</el-tab-pane>
			</el-tabs>
			<!-- 图表 -->
			<el-row type="flex" justify="space-between" style="margin-bottom: 30px;">
				<el-col :span="12">
					<div style="margin-left: 20px;margin-right: 5px;" class="grid-content bg-purple">
						<el-card class="box-card" shadow="never" :body-style="bodyStyle">
							<div slot="header"
								style="height: 15px;display: flex;justify-content: space-between;align-items: center;">
								<div style="height: 100%;display: flex;align-items: center;">合同增长折线图</div>
								<el-date-picker v-model="selectYear" :picker-options="pickerOptions" type="daterange"
									range-separator="至" start-placeholder="开始日期" @change="selectYears"
									end-placeholder="结束日期">
								</el-date-picker>
							</div>
							<div id="Chart1" class="Chart1" v-loading="tableDataLoading1">
							</div>
						</el-card>
					</div>
				</el-col>
				<el-col :span="12">
					<div style="margin-right: 10px;margin-left: 15px;" class="grid-content bg-purple-light">
						<el-card class="box-card" shadow="never" :body-style="bodyStyle">
							<div slot="header"
								style="height: 15px;display: flex;justify-content: space-between;align-items: center;">
								<div style="height: 100%;display: flex;align-items: center;">用户增长折线图</div>
								<el-date-picker v-model="selectDayDate" :picker-options="pickerOptions" type="daterange"
									range-separator="至" start-placeholder="开始日期" @change="selectTime"
									end-placeholder="结束日期">
								</el-date-picker>
							</div>
							<div id="Chart2" class="Chart1" v-loading="tableDataLoading2">
							</div>
						</el-card>
					</div>
				</el-col>
			</el-row>
			
		</div>
	</div>
</template>

<script>
	//按需加载echarts
	import * as echarts from 'echarts/core';
	import {
		ToolboxComponent,
		TooltipComponent,
		GridComponent,
		LegendComponent
	} from 'echarts/components';
	import {
		LineChart
	} from 'echarts/charts';
	import {
		UniversalTransition
	} from 'echarts/features';
	import {
		CanvasRenderer
	} from 'echarts/renderers';
	export default {
		data() {
			return {
				limit: 5,
				page: 1,
				tableData: {},
				tableData2: {
					list: [],
					totalCount: 0
				},
				colonelData: [],
				commodityData: [],
				userjfData: [],
				incomeData: [],
				activeName: 'third',
				tableDataLoading: true,
				flag: 1,
				usersumData: {},
				taskStat: {},
				data: '',
				time: '',
				platform: '',
				type: '',
				balance: -1,
				flag: 1,
				info: {
					stockDate: this.getNowTime(), //日期
				},
				flags: [{
					value: 1,
					label: '按天查询'
				}, {
					value: 2,
					label: '按月查询'
				}, {
					value: 3,
					label: '按年查询'
				}],
				rechgeData:{},
				rechgeDataQy:{},
				chongzhiData: {},
				userStat: {},
				
				chatNumRz: 0,
				chatNumDp: 0,
				chatNumFw: 0,
				// 图表----------
				tableDataLoading1: true,
				tableDataLoading2: true,
				orderEcharts: {}, //订单折线图
				increaseEcharts: {}, //用户增长折线图
				selectDayDate: '',
				pickerOptions: {
					onPick: ({
						maxDate,
						minDate
					}) => {
						// 把选择的第一个日期赋值给一个变量。
						this.choiceDate = minDate.getTime()
						// 如何你选择了两个日期了，就把那个变量置空
						if (maxDate) this.choiceDate = ''
					},
					disabledDate: time => {
						// 如何选择了一个日期
						if (this.choiceDate) {
							// 7天的时间戳
							const one = 6 * 24 * 3600 * 1000
							// 当前日期 - one = 7天之前
							const minTime = this.choiceDate - one
							// 当前日期 + one = 7天之后
							const maxTime = this.choiceDate + one
							return (
								time.getTime() < minTime ||
								time.getTime() > maxTime
							)
						} else {
							return false
						}
					}
				},
				bodyStyle: {
					padding: '0'
				},
				selectYear: '',
				qiehuan_type: 1,
				lirunStat: {},
				// ===========
			}
		},
		mounted() {
			// this.colonel()
			// this.taskData()
			this._getData() //用户统计
			this.userData()
			// this.incomeSelect()//收入
			this.getUserOrderEcharts();
			this.getUserIncreaseEcharts()
			
			setInterval(() => {
				if (this.$cookie.get('numberFw')) {
					this.chatNumFw = this.$cookie.get('numberFw')
				}
				if (this.$cookie.get('numberDp')) {
					this.chatNumDp = this.$cookie.get('numberDp')
				}
				if (this.$cookie.get('numberRz')) {
					this.chatNumRz = this.$cookie.get('numberRz')
					
				}
			
			}, 1000)
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			updates1(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId1
					}
				})
			},
			updates2(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId2
					}
				})
			},
			handleSizeChange(val) {
				this.limit = val;
				this.colonel();
			},
			handleCurrentChange(val) {
				this.page = val;
				this.colonel();
			},
			// 用户分析选择日期
			animeDat() {
				this._getData()
			},
			// 订单分析选择日期
			animeOrder() {
				// this.taskData()
				// this.colonel()
				this.userData()
				this.rechSelect()
			},
			// 订单分析年月日
			orderfenxi(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.taskData()
					// this.colonel()
					this.userData()
					this.rechSelect()
				}
				if (vanumber === 2) {
					this.flag = 2
					this.taskData()
					// this.colonel()
					this.userData()
					this.rechSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.taskData()
					// this.colonel()
					this.userData()
					this.rechSelect()
				}
			},
			// Tabs点击事件
			handleClick(tab, event) {
				if (tab._props.label == '合同统计') {
					this.taskData()
				}
				if (tab._props.label == '收入分析') {
					this.colonel()
				}
				if (tab._props.label == '用户分析') {
					this.info.stockDate = this.getNowTime()
					this.userData()
					
				}
				if (tab._props.label == '充值统计') {
					this.info.stockDate = this.getNowTime()
					this.rechSelect()
				}
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
				var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
				var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// 用户统计
			_getData() {
				this.$http({
					url: this.$http.adornUrl('user/homeMessage'),
					method: 'get',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data;
						this.tableData = returnData
					}
				})
			},
			// 收入统计
			//  incomeSelect(){
			//  this.$http({
			// url: this.$http.adornUrl('statistical/income'),
			// method: 'get',
			// params: this.$http.adornParams({
			// })
			//  }).then(({data}) => {
			//   if(data.code == 0){
			// 	  let returnData = data.data;
			// 	  this.incomeData = returnData
			//   }
			//  })
			//  },
			//合同统计
			taskData() {
				// 合同
				this.$http({
					url: this.$http.adornUrl('user/contractAnalysis'),
					method: 'get',
					params: this.$http.adornParams({
						'date': this.info.stockDate,
						'type': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.rechgeData = returnData
				})
				// 企业数量
				this.$http({
					url: this.$http.adornUrl('user/enterpriseAnalysis'),
					method: 'get',
					params: this.$http.adornParams({
						'date': this.info.stockDate,
						'type': this.flag,
					})
				}).then(({
					data
				}) => {
					let returnData = data.data;
					this.rechgeDataQy = returnData
				})
			},
			// 购买商品分析
			colonel() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/order/incomeStatistics'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'dateTime': this.info.stockDate,
						'flag': this.flag,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						let returnData = data.data
						this.tableData2 = returnData
					}
				})
			},
			// 用户分析
						userData(platform, type) {
							this.$http({ 
								url: this.$http.adornUrl('user/userMessage'),
								method: 'get',
								params: this.$http.adornParams({
									'data': this.info.stockDate,
									'type': this.flag,
								})
							}).then(({
								data
							}) => {
								let returnData = data.data;
								this.userStat = returnData
								
							})
			},
			// 充值统计
			rechSelect() {
				this.$http({  
					url: this.$http.adornUrl('user/payMessage'),
					method: 'get',
					params: this.$http.adornParams({
						'date': this.info.stockDate,
						'type': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.chongzhiData = returnData
					}
				})
			},
			// 待审核跳转
			btnDsh(index){
				if(index==1){
					this.$router.replace({
						name: 'authentication',
						query: {
							status: 0
						}
					})
				}
				if(index==2){
					this.$router.replace({
						name: 'autonym',
						query: {
							status: 1
						}
					})
				}
				if(index==3){
					this.$router.replace({
						name: 'shopManage',
						query: {
							status: 1
						}
					})
				}
				
				
			},
			
			//合同折线图
			getUserOrderEcharts(startTime, endTime) {
				this.tableDataLoading1 = true
				var times = this.info.stockDate.slice(0, 10)
				if (startTime) {
					var startTime = startTime
					var endTime = endTime
				} else {
					// var now = Date.parse(new Date())
					// var oldD = new Date(now)
					// // var year = oldD.getFullYear(); //得到年份
					// var year = this.info2.stockDate
					// // this.selectYear = year
					var now = Date.parse(new Date())
					// var choiceDate = (new Date(now)).getTime() / 1000;
					var choiceDate = Date.parse(new Date())
					var one = 6 * 24 * 3600 * 1000
					// 当前日期 - one = 7天之前
					var minTime = choiceDate - one
					var oldD = new Date(minTime)
					var year = oldD.getFullYear(); //得到年份
					var month = oldD.getMonth(); //得到月份
					var date = oldD.getDate(); //得到日期
					var hh = oldD.getHours() < 10 ? "0" + oldD.getHours() : oldD.getHours();
					var mm = oldD.getMinutes() < 10 ? "0" + oldD.getMinutes() : oldD.getMinutes();
					var ss = oldD.getSeconds() < 10 ? "0" + oldD.getSeconds() : oldD.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					var defaultDate = `${year}-${month}-${date}`;
			
					var startTime = defaultDate
					var endTime = times
					this.selectYear = [startTime, endTime]
				}
			
				this.$http({
					url: this.$http.adornUrl('user/selectContractCountByTime'),
					method: 'get',
					params: this.$http.adornParams({
						// 'time': year ? year : '',
						'startTime': startTime ? startTime : '',
						'endTime': endTime ? endTime : ''
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.orderEcharts = data.data
						//使用数据初始化图表
						this.setEcharts()
						this.tableDataLoading1 = false
					}
					// this.dataInfo = data.data
				})
			},
			//用户增长折线图
			getUserIncreaseEcharts(startTime, endTime) {
				this.tableDataLoading2 = true
				var times = this.info.stockDate.slice(0, 10)
				console.log('times', times)
				if (startTime) {
					var startTime = startTime
					var endTime = endTime
				} else {
					var now = Date.parse(new Date())
					// var choiceDate = (new Date(now)).getTime() / 1000;
					var choiceDate = Date.parse(new Date())
					var one = 6 * 24 * 3600 * 1000
					// 当前日期 - one = 7天之前
					var minTime = choiceDate - one
					var oldD = new Date(minTime)
					var year = oldD.getFullYear(); //得到年份
					var month = oldD.getMonth(); //得到月份
					var date = oldD.getDate(); //得到日期
					var hh = oldD.getHours() < 10 ? "0" + oldD.getHours() : oldD.getHours();
					var mm = oldD.getMinutes() < 10 ? "0" + oldD.getMinutes() : oldD.getMinutes();
					var ss = oldD.getSeconds() < 10 ? "0" + oldD.getSeconds() : oldD.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					var defaultDate = `${year}-${month}-${date}`;
			
					var startTime = defaultDate
					var endTime = times
					this.selectDayDate = [startTime, endTime]
				}
				this.$http({
					url: this.$http.adornUrl('user/selectUserCountStatisticsByTime'),
					method: 'get',
					params: this.$http.adornParams({
						'startTime': startTime ? startTime : '',
						'endTime': endTime ? endTime : ''
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						this.increaseEcharts = data.data
						//使用数据初始化图表
						this.setEcharts2()
						this.tableDataLoading2 = false
					}
					// this.dataInfo = data.data
				})
			},
			//使用数据初始化图表
			setEcharts() {
				echarts.use([
					ToolboxComponent,
					TooltipComponent,
					GridComponent,
					LegendComponent,
					LineChart,
					CanvasRenderer,
					UniversalTransition
				]);
			
				var chartDom = document.getElementById('Chart1');
				var myChart = echarts.init(chartDom);
				var option;
			
				option = {
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['赛事总量'],
						top: '5%'
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					toolbox: {
						top: '3%',
						feature: {
							saveAsImage: {},
			
						}
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: this.orderEcharts.year
					},
					yAxis: {
						type: 'value'
					},
					series: [{
							name: '合同总量',
							type: 'line',
							stack: 'Total',
							data: this.orderEcharts.contractCount
						},
						// {
						// 	name: '待支付单量',
						// 	type: 'line',
						// 	stack: 'Total',
						// 	data: this.orderEcharts.ordersDzfCountList
						// },
						// {
						// 	name: '待使用订单',
						// 	type: 'line',
						// 	stack: 'Total',
						// 	data: this.orderEcharts.ordersDsyCountList
						// },
						// {
						// 	name: '使用中订单',
						// 	type: 'line',
						// 	stack: 'Total',
						// 	data: this.orderEcharts.ordersSyzCountList
						// },
						// {
						// 	name: '已完成订单',
						// 	type: 'line',
						// 	stack: 'Total',
						// 	data: this.orderEcharts.ordersYwcCountList
						// },
						// {
						// 	name: '已取消订单',
						// 	type: 'line',
						// 	stack: 'Total',
						// 	data: this.orderEcharts.ordersYqxCountList
						// },
						// {
						// 	name: '待评价订单',
						// 	type: 'line',
						// 	stack: 'Total',
						// 	data: this.orderEcharts.ordersDpjCountList
						// },
			
					]
				};
			
				option && myChart.setOption(option);
			},
			//使用数据初始化图表
			setEcharts2() {
				echarts.use([
					ToolboxComponent,
					TooltipComponent,
					GridComponent,
					LegendComponent,
					LineChart,
					CanvasRenderer,
					UniversalTransition
				]);
				var chartDom = document.getElementById('Chart2');
				var myChart = echarts.init(chartDom);
				var option;
			
				option = {
					tooltip: {
						trigger: 'axis'
					},
					legend: {
						data: ['用户增长量'],
						top: '5%'
					},
					grid: {
						left: '3%',
						right: '4%',
						bottom: '3%',
						containLabel: true
					},
					toolbox: {
						top: '3%',
						feature: {
							saveAsImage: {},
			
						}
					},
					xAxis: {
						type: 'category',
						boundaryGap: false,
						data: this.increaseEcharts.year
					},
					yAxis: {
						type: 'value'
					},
					series: [{
						name: '用户增长量',
						type: 'line',
						stack: 'Total',
						data: this.increaseEcharts.userCountList
					}, ]
				};
			
				option && myChart.setOption(option);
			},
			//根据年份筛选
			selectYears(e) {
				console.log(e, '`````````', this.info.stockDate)
				if (e == null) {
					this.getUserOrderEcharts()
				} else {
					// var now = new Date();
					// // var year = now.getFullYear(); //得到年份
					// var year = this.info2.stockDate
					// let startTime = (new Date(e)).getTime() / 1000;
					// this.getUserOrderEcharts(year)
					var now = new Date(e[0]);
					var year = now.getFullYear(); //得到年份
					var month = now.getMonth(); //得到月份
					var date = now.getDate(); //得到日期
					var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
					var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
					var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
					var defaultDate = `${year}-${month}-${date}`;
			
					var now1 = new Date(e[1]);
					var year1 = now1.getFullYear(); //得到年份
					var month1 = now1.getMonth(); //得到月份
					var date1 = now1.getDate(); //得到日期
					var hh1 = now1.getHours() < 10 ? "0" + now1.getHours() : now1.getHours();
					var mm1 = now1.getMinutes() < 10 ? "0" + now1.getMinutes() : now1.getMinutes();
					var ss1 = now1.getSeconds() < 10 ? "0" + now1.getSeconds() : now1.getSeconds();
					month1 = month + 1;
					month1 = month.toString().padStart(2, "0");
					date1 = date1.toString().padStart(2, "0");
					var defaultDate1 = `${year1}-${month1}-${date1}`;
			
					let startTime = defaultDate
					let endTime = defaultDate1
					console.log(startTime, '`````````', endTime)
					this.getUserOrderEcharts(startTime, endTime)
				}
			},
			//根据日期区间筛选
			selectTime(e) {
				console.log(e)
				if (e == null) {
					this.getUserIncreaseEcharts()
				} else {
					var now = new Date(e[0]);
					var year = now.getFullYear(); //得到年份
					var month = now.getMonth(); //得到月份
					var date = now.getDate(); //得到日期
					var hh = now.getHours() < 10 ? "0" + now.getHours() : now.getHours();
					var mm = now.getMinutes() < 10 ? "0" + now.getMinutes() : now.getMinutes();
					var ss = now.getSeconds() < 10 ? "0" + now.getSeconds() : now.getSeconds();
					month = month + 1;
					month = month.toString().padStart(2, "0");
					date = date.toString().padStart(2, "0");
					// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
					var defaultDate = `${year}-${month}-${date}`;
			
					var now1 = new Date(e[1]);
					var year1 = now1.getFullYear(); //得到年份
					var month1 = now1.getMonth(); //得到月份
					var date1 = now1.getDate(); //得到日期
					var hh1 = now1.getHours() < 10 ? "0" + now1.getHours() : now1.getHours();
					var mm1 = now1.getMinutes() < 10 ? "0" + now1.getMinutes() : now1.getMinutes();
					var ss1 = now1.getSeconds() < 10 ? "0" + now1.getSeconds() : now1.getSeconds();
					month1 = month + 1;
					month1 = month.toString().padStart(2, "0");
					date1 = date1.toString().padStart(2, "0");
					var defaultDate1 = `${year1}-${month1}-${date1}`;
			
					let startTime = defaultDate
					let endTime = defaultDate1
			
					// let startTime = (new Date(e[0])).getTime() / 1000;
					// let endTime = (new Date(e[1])).getTime() / 1000;
					this.getUserIncreaseEcharts(startTime, endTime)
				}
			},
			// --------------------------------
		}
	}
</script>

<style scoped>
	.Chart1 {
		width: 100%;
		height: 300px;
	}
	.box {
		padding: 44px;
		border: 1px solid #eee;
		margin: 15px 10px;
	}

	.box_num {
		font-size: 14px;
		color: #66b1ff;
	}

	.box_num .box_color {
		color: #333;
		font-size: 14px;
		margin-bottom: 15px;
	}

	.box_num div span {
		font-size: 20px;
		margin-left: 5px;
	}

	.text_color {
		color: #4f9dec;
	}

	.text_color span {
		margin-right: 5px;
	}
</style>
