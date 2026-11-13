<template>
	<div>
		<div style="display: inline-block;">
			<div style="display: inline-block;margin-bottom: 6px;">
				<span>状态：</span>
				<el-select v-model="status" style="width:150px;margin-left: 10px;" @change="timeDate2()">
					<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin-bottom: 6px;">
				<span>用户名：</span>
				<el-input style="width: 200px;" @keydown.enter.native="timeDate2" placeholder="请输入用户名"
					v-model="nickName"></el-input>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin-bottom: 6px;">
				<span>充值订单号：</span>
				<el-input style="width: 200px;" @keydown.enter.native="timeDate2" placeholder="请输入充值订单号"
					v-model="orderId"></el-input>&nbsp;&nbsp;
			</div>
			<div style="position: relative;display: inline-block;margin-bottom: 6px;">
				<span>手机号：</span>
				<el-input style="width: 200px;" @keydown.enter.native="timeDate2" placeholder="请输入手机号"
					v-model="phone"></el-input>&nbsp;&nbsp;
			</div>
			
			<!-- <div style="display: inline-block;margin-bottom: 6px;">
				<span>订单类型：</span>
				<el-select v-model="type" style="width:150px;margin-left: 10px;" @change="timeDate2()">
					<el-option v-for="item in classifyNum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			</div> -->
			<div style="display: inline-block;margin-bottom: 6px;">
				<span>支付类型：</span>
				<el-select v-model="classify" style="width:150px;margin-left: 10px;" @change="timeDate2()">
					<el-option v-for="item in typeNum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
			</div>
			<span>开始时间：</span>
			<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right" type="date"
				format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
			</el-date-picker>&nbsp;&nbsp;&nbsp;
			<span>截止时间：</span>
			<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right" type="date"
				format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
			</el-date-picker>
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="timeDate2">
				查询</el-button>
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleaes">
				重置</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData.records">
			<el-table-column prop="id" label="编号" width="50"></el-table-column>
			<el-table-column prop="userName" label="用户名">
				<template slot-scope="scope">
					<span style="color: #f56c6c;cursor:pointer"
						@click="updates(scope.row)">{{ scope.row.userName }}</span>
				</template>
			</el-table-column>
			<el-table-column prop="phone" label="手机号"></el-table-column>
			<el-table-column prop="orderId" label="充值订单号" width="200"></el-table-column>
			<el-table-column prop="money" label="金额"></el-table-column>
			<el-table-column prop="userId" label="会员编号"></el-table-column>
			<el-table-column label="充值类型">
				<template slot-scope="scope">
					<span v-if="scope.row.type == 1">充值次数</span>
					<span v-if="scope.row.type == 2">
						开通会员
						<span v-if="scope.row.parentId == 1">(月会员)</span>
						<span v-if="scope.row.parentId == 2">(季会员)</span>
						<span v-if="scope.row.parentId == 3">(年会员)</span>
						<span v-if="scope.row.parentId == 4">(周会员)</span>
					</span>
				</template>
			</el-table-column>
			<el-table-column label="支付类型">
				<template slot-scope="scope">
					<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 1">app微信</span>
					<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 2">微信公众号</span>
					<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 3">微信小程序</span>
					<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 4">app支付宝</span>
					<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 5">支付宝H5</span>
					<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 6">支付宝网页支付</span>
				</template>
			</el-table-column>
			<el-table-column label="状态">
				<template slot-scope="scope">
					<span  v-if="scope.row.state == 0">待支付</span>
					<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 1">支付成功</span>
					<span style="color: red;cursor: pointer;" v-if="scope.row.state == 2">支付失败</span>
				</template>
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="170">
			</el-table-column>
			<el-table-column prop="payTime" label="支付时间" width="170">
			</el-table-column>
			<el-table-column prop="refundStatus" label="是否退款" width="80">
				<template slot-scope="scope">
					<span style="color: red;" v-if="scope.row.refundStatus == 1">是</span>
					<span style="color: #999;" v-else>否</span>
				</template>
			</el-table-column>
			<el-table-column fixed="right" label="操作" width="80">
				<template slot-scope="scope">
					<el-button size="mini" type="primary"
						:disabled="isAuth('exchangeList:tuikuan')&&scope.row.refundStatus == 1" v-if="scope.row.state !== 0 "  @click="batch(scope.row)">
						退款
					</el-button>
				</template>
			</el-table-column>
		</el-table>
		<div style="color: #B94A48;font-size: 20px;margin-top: 10px;display: inline-block;">
			本页累计总交易统计：{{totalMoney.toFixed(2)}}元; </div>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
			</el-pagination>
		</div>
		<el-dialog :title="title" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;display: flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="memberImg"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess">
						<img v-if="memberImg" :src="memberImg" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
				<el-input style="width:50%;" v-model="memberName" placeholder="请输入名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" placeholder="请输入排序"
					onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="addNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: [],
				memberName: '',
				memberImg: '',
				memberId: '',
				sort: '',
				title: '添加',
				startTime: '',
				endTime: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				totalMoney: 0,
				status:'',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 0,
						label: '待支付'
					},
					{
						value: 1,
						label: '支付成功'
					},
					{
						value:2,
						label: '支付失败'
					},
					
				],
				type:'',
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: 'app微信'
					},
					{
						value: 2,
						label: '微信公众号'
					},
					{
						value: 3,
						label: '微信小程序'
					},
					{
						value: 4,
						label: 'app支付宝'
					},
					{
						value: 5,
						label: '支付宝H5'
					},
					{
						value: 6,
						label: '支付宝网页支付'
					},
					
				],
				nickName:'',
				orderId:'',
				phone:'',
				classify:'',
				classifyNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '充值余额'
					},
					{
						value: 2,
						label: '购买会员'
					},
					
				],
			}
		},
		methods: {
			//处理默认选中当前日期
			getNowTime() {
				var now = new Date()
				var year = now.getFullYear() //得到年份
				var month = now.getMonth() //得到月份
				var date = now.getDate() //得到日期
				month = month + 1
				month = month.toString().padStart(2, '0')
				date = date.toString().padStart(2, '0')
				var defaultDate = `${year}-${month}-${date}`
				return defaultDate
				this.$set(this.info, 'stockDate', defaultDate)
			},
			//处理默认选中当前日期
			getNowTime2() {
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
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			// 获取数据列表
			dataSelect() {
				this.totalMoney = 0
				// this.tableDataLoading = true
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				this.$http({
					url: this.$http.adornUrl('admin/payDetails/getPayDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'endTime': this.endTime,
						'startTime': this.startTime,
						'type':this.type,
						'state':this.status,
						'userName':this.nickName,
						'classify':this.classify,
						'orderId':this.orderId,
						'phone':this.phone,
					})
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						console.log('会员列表', data)
						this.tableDataLoading = false
						for (var i in data.data.records) {
							if (data.data.records[i].money) {
								this.totalMoney = this.totalMoney + data.data.records[i].money
							}
						}    
						let returnData = data.data;
						this.tableData = returnData;
					}

				})
			},
			timeDate2() {
				this.page = 1
				this.dataSelect()
			},
			cleaes(){
				this.page = 1
				this.endTime = ''
				this.startTime = ''
				this.type = ''
				this.status = ''
				this.nickName = ''
				this.classify = ''
				this.orderId = ''
				this.phone = ''
				this.dataSelect()
			},
			//上传成功
			handleAvatarSuccess(file) {
				this.memberImg = file.data
			},
			addNotice() {
				this.dialogFormVisible = true
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			// 添加banner图
			addNoticeTo() {
				if (this.memberImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传图片',
						type: 'warning'
					});
					return
				}
				if (this.memberName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入名称',
						type: 'warning'
					});
					return
				}
				if (this.title == '添加') {
					var url = 'member/insertMember'
				} else {
					var url = 'member/updateMember'
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData({
						'memberImg': this.memberImg,
						'memberId': this.memberId,
						'memberName': this.memberName,
						'sort': this.sort
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.memberImg = ''
							this.memberId = ''
							this.memberName = ''
							this.sort = ''
							this.title = '添加'
							this.dataSelect()
						}
					})
				})
			},
			// 修改
			amendBanner(index, rows, a) {
				if (a == 1) {
					this.pl = 1
				} else {
					this.pl = 0
				}
				this.dialogFormVisible = true
				this.id = rows.id
				this.memberImg = rows.memberImg
				this.memberId = rows.memberId
				this.sort = rows.sort
				this.memberName = rows.memberName
				this.title = '修改'
			},
			// 修改
			amendNoticeTo() {
				this.$http({
					url: this.$http.adornUrl(
						`?memberId=${this.memberId}&memberImg=${this.memberImg}&sort=${this.sort}&memberName=${this.memberName}`
						),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					this.dialogFormVisible = false
					this.$message({
						message: '操作成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				})
			},
			//删除一级
			deletes(row) {
				let delid = row.memberId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`member/deleteMemberById/?memberId=${delid}`),
						method: 'post',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			//退款
			batch(row) {
				this.$confirm(`确定要退款?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/payDetails/refundPayDetails'),
						method: 'post',
						params: this.$http.adornParams({
							'payId':row.id
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
							return
						}else {
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
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style>
</style>
