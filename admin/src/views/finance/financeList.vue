<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="充值统计" name="sixth">
			<div>
				<el-select v-model="flag" style="width:150px;margin-left: 10px;" @change="animeDat3">
					<el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<el-date-picker style="width: 200px;margin-left: 10px;" v-model="info1.stockDate1" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间"
					@change="animeSelect3">
				</el-date-picker>
			</div>
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
		<el-tab-pane label="充值记录" name="fourth">
			<div style="margin:2% 0;display: inline-block;">
				<span>状态：</span>
				<el-select v-model="state" placeholder="请选择状态" style="width:150px;" @change="selectTrigger(state)">
					<el-option v-for="item in states" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
			</div>
			<div style="display: inline-block;">
				<span>开始时间：</span>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
					type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
				</el-date-picker>&nbsp;&nbsp;&nbsp;
				<span>截止时间：</span>
				<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right" type="date"
					format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
				</el-date-picker>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="timeDate">查询
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="rechargeData.list">
				<el-table-column fixed prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
					<template slot-scope="scope">
						<span style="color: #4f9dec;cursor: pointer;"
							@click="updates(scope.row)">{{scope.row.userName ? scope.row.userName : 'null'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="orderId" label="充值订单号" width="250">
				</el-table-column>
				<el-table-column prop="money" label="充值金额" width="100">
				</el-table-column>
				<el-table-column label="支付方式" width="120">
					<template slot-scope="scope">
						<!-- <span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 1">微信</span>
						<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 2">支付宝</span> -->
						<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 1">app微信</span>
						<span style="color: #4f9dec;cursor: pointer;" v-else-if="scope.row.classify == 2">微信公众号</span>
						<span style="color: #4f9dec;cursor: pointer;" v-else-if="scope.row.classify == 3">微信小程序</span>
						<span style="color: #4f9dec;cursor: pointer;" v-else-if="scope.row.classify == 4">支付宝</span>
						<span style="color: #4f9dec;cursor: pointer;" v-else>暂无</span>
					</template>
				</el-table-column>
				<el-table-column label="状态" width="120">
					<template slot-scope="scope">
						<span style="color: #4f9dec;" v-if="scope.row.state == 0">待充值</span>
						<span style="color: #4f9dec;" v-if="scope.row.state == 1">充值成功</span>
						<span style="color: #4f9dec;" v-if="scope.row.state == 2">充值失败</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="170">
				</el-table-column>
				<el-table-column prop="payTime" label="支付时间" width="170">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
					:page-sizes="[5, 10, 15]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="rechargeData.totalCount">
				</el-pagination>
			</div>
		</el-tab-pane>
		
	</el-tabs>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				balance: -1,
				zhifubao: '',
				tkstatus: -1,
				state: 1,
				states: [{
						label: '全部',
						value: -1
					},
					{
						label: '待充值',
						value: 0
					},
					{
						label: '充值成功',
						value: 1
					},
					{
						label: '充值失败',
						value: 2
					}
				],
				state1: '',
				states1: [
					{
						label: '全部',
						value: ''
					},

					{
						label: '待提现',
						value: 0
					},
					{
						label: '提现成功',
						value: 1
					},
					{
						label: '拒绝提现',
						value: -1
					}
				],
				classify:'',
				classifyList:[
					{
							label: '全部',
							value: ''
						},
						{
							label: '支付宝',
							value: 1
						},
						{
							label: '微信',
							value: 2
						}
				],
				zhifubaoName: '',
				content: '',
				startTime: '',
				endTime: '',
				cashId: '',
				type: -1,
				totalnum: 0,
				totalnum2: 0,
				sqxTotal: '',
				userTotal: '',
				taunTotal: '',
				relationId: '',
				sumMoneyByTime: '',
				money: '',
				activeName: 'sixth',
				formLabelWidth: '200px',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				tableDataLoading: false,
				tableData: {},
				rechargeData: {},
				chongzhiData: {},
				withdrawData: {},
				depositData: [],
				MoneyData: {},
				checkBoxData: [], //多选框选择的值
				flag: 1,
				form: {
					cashId: '',
					content: '',
				},
				form2: {
					id: '',
					money: '',
					createTime: ''
				},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info1: {
					stockDate1: this.getNowTime1(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
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
				phone:'',
			}
		},
		methods: {
			// 会员详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 批量转账
			transferClcik(id) {
				this.$confirm('此操作将进行批量转账, 是否继续?', '提示', {
				          confirmButtonText: '确定',
				          cancelButtonText: '取消',
				          type: 'warning'
				        }).then(() => {
				          var ids = id ? [id] : this.checkBoxData.map(item => {
				          	return item.id
				          })
				          for (var i in ids) {
				          	this.$http({
				          		// url: this.$http.adornUrl(`cash/alipay/${ids[i]}`),
				          		// method: 'post',
				          		// data: this.$http.adornData({})
								url: this.$http.adornUrl(`cash/alipay`),
								method: 'post',
								params: this.$http.adornParams({
									'cashId':ids[i]
								})
				          	}).then(({
				          		data
				          	}) => {
				          		if (data.code == 0) {
				          			this.$notify({
				          				title: '提示',
				          				duration: 1800,
				          				message: data.msg,
				          				type: 'warning'
				          			})
				          			return
				          		}
				          		if (data.code == 9999) {
				          			this.$notify({
				          				title: '提示',
				          				duration: 1800,
				          				message: data.msg,
				          				type: 'error'
				          			})
				          			return
				          		}
				          	})
				          }
				        }).catch(() => {
				          this.$message({
				            type: 'info',
				            message: '已取消批量转账'
				          });
				        });

			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
			},
			//转账
			batch(row) {
				if (row.state == 1) {
					this.$message({
						message: '已转账，请勿重复操作！',
						type: 'error',
						duration: 1500
					})
					return
				}
				this.$confirm(`确定转账?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`cash/alipay`),
						method: 'post',
						params: this.$http.adornParams({
							'cashId':row.id
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: data.msg,
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}
						if (data.code == 9999) {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}
						if (data.code == 500) {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}
					})
				})
			},
			// 拒绝
			refund(row) {
				if (row.state == -1) {
					this.$message({
						message: '已拒绝，请勿重复操作！',
						type: 'error',
						duration: 1500
					})
				} else {
					this.dialogFormVisible = true
					this.form.cashId = row.id
				}
			},
			// 拒绝操作
			refundNoticeTo() {
				if (this.form.content == '') {
					this.$message({
						message: '请输入拒绝原因',
						type: 'error',
						duration: 1500
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl(`cash/refund`),
					method: 'post',
					params: this.$http.adornParams({
						'cashId':this.form.cashId,
						'content':this.form.content
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.form.content = ''
								this.dataSelect()
							}
						})
						return
					}
					if (data.code == 500) {
						this.dialogFormVisible = false
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.form.content = ''
								this.dataSelect()
							}
						})
						return
					}
					if (data.code == -100) {
						this.dialogFormVisible = false
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.form.content = ''
								this.dataSelect()
							}
						})
						return
					}
				})
			},
			timeDate() {
				this.rechargeSelect()
			},
			// 日期选择
			animeDat(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.incomeSelect()
				}
				if (vanumber === 2) {
					this.flag = 2
					this.incomeSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.incomeSelect()
				}
			},
			// 日期选择2
			animeDat2(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.withdrawSelect()
				}
				if (vanumber === 2) {

					this.flag = 2
					this.withdrawSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.withdrawSelect()
				}
			},
			// 日期选择3
			animeDat3(value) {
				let vanumber = value
				if (vanumber === 1) {
					this.flag = 1
					this.rechSelect()
				}
				if (vanumber === 2) {

					this.flag = 2
					this.rechSelect()
				}
				if (vanumber === 3) {
					this.flag = 3
					this.rechSelect()
				}
			},
			animeSelect() {
				this.incomeSelect()
			},
			animeSelect2() {
				this.withdrawSelect()
			},
			animeSelect3() {
				this.rechSelect()
			},
			//处理默认选中当前日期
			getNowTime1() {
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
				// var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			getNowTime2() {
				var now = new Date();
				var year = now.getFullYear(); //得到年份
				var month = now.getMonth(); //得到月份
				var date = now.getDate(); //得到日期
				month = month + 1;
				month = month.toString().padStart(2, "0");
				date = date.toString().padStart(2, "0");
				var defaultDate = `${year}-${month}-${date}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() - now.getMonth() //得到月份
				var date = now.getDate() - now.getDate() + 1 //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange1(val) {
				this.limit = val
				this.rechargeSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.rechargeSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '提现管理') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				if (tab._props.label == '收入统计') {
					this.flag = 1
					this.incomeSelect()
				}
				if (tab._props.label == '充值统计') {
					this.flag = 1
					this.rechSelect()
				}
				if (tab._props.label == '提现统计') {
					this.flag = 1
					this.withdrawSelect()
				}
				if (tab._props.label == '充值记录') {
					this.page = 1
					this.limit = 10
					this.rechargeSelect()
				}
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans2() {
				this.zhifubao = ''
				this.zhifubaoName = ''
				this.state1 = ''
				this.classify = ''
				this.startTime= ''
				this.endTime= ''
				this.phone = ''
				this.page = 1
				this.dataSelect()
			},
			
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cash/selectAdminHelpProfit'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'zhifubaoName': this.zhifubaoName,
						'zhifubao': this.zhifubao,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'phone':this.phone,
						'classify':this.classify
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
					this.totalnum = returnData.totalCount
				})
			},
			// 收入统计
			incomeSelect() {
				this.$http({
					url: this.$http.adornUrl('admin/order/incomeStatistics'),
					method: 'get',
					params: this.$http.adornParams({
						'dateTime': this.info1.stockDate1,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						let returnData = data.data
						this.MoneyData = returnData
					}
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
			// 提现统计
			withdrawSelect() {
				this.$http({
					url: this.$http.adornUrl('cash/statisticsCashMoney'),
					method: 'get',
					params: this.$http.adornParams({
						'time': this.info1.stockDate1,
						'flag': this.flag
					})
				}).then(({
					data
				}) => {
					let returnData = data.data
					this.withdrawData = returnData
				})
			},
			selectTrigger() {
				this.rechargeSelect()
			},
			// 获取充值记录
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info2.stockDate2
				}
				if (this.startTime == '') {
					this.startTime = this.info.stockDate
				}
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cash/selectUserRecharge'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'startTime': this.startTime,
						'endTime': this.endTime
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.rechargeData = returnData
					this.totalnum2 = returnData.totalCount
				})
			},
			// 导出
			exportBtn() {
				this.$http({
					url: this.$http.adornUrl('cash/exportExcel'),
					method: 'get',
					responseType: 'blob',
					params: this.$http.adornParams({
						// 'page': page,
						// 'size': this.size,
						'zhifubaoName': this.zhifubaoName,
						'zhifubao': this.zhifubao,
						'startTime': this.startTime,
						'endTime': this.endTime,
						'phone':this.phone,
						'classify':this.classify
					})
				}).then(({
					data
				}) => {
					let blob = new Blob([data], {
						type: 'application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
					})
					if (window.navigator.msSaveOrOpenBlob) {
						navigator.msSaveBlob(blob)
					} else {
						let url = window.URL.createObjectURL(blob)
						let elink = document.createElement('a')
						elink.download = '提现列表.xlsx'
						elink.style.display = 'none'
						elink.href = url
						document.body.appendChild(elink)
						elink.click()
						document.body.removeChild(elink)
					}
				})
			},
		},
		mounted() {
			this.rechSelect()
		}
	}
</script>

<style scoped="scoped">
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
