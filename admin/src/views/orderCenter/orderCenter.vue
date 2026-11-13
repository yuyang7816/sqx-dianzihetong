<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="订单中心" name="first">
			<div style="display: inline-block;">
				<span>订单类型：</span>
				<el-select v-model="type" style="width:150px;margin-left: 10px;" @change="animeDat()">
					<el-option v-for="item in typeNum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;
				<span>状态：</span>
				<el-select v-model="status" style="width:150px;margin-left: 10px;" @change="animeDat(status)">
					<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				<div style="position: relative;display: inline-block;">
					<span>订单号：</span>
					<el-input style="width: 200px;" @keydown.enter.native="select" placeholder="请输入订单号"
						v-model="orderNum"></el-input>&nbsp;&nbsp;
				</div>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
				</el-button>
				<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="orderId" label="编号" width="80" fixed="left">
				</el-table-column>
				<el-table-column prop="ordersUserName" label="订单用户" fixed="left">
					<template slot-scope="scope">
						<span style="color: #008000;cursor: pointer;"
							@click="updates(scope.row.buyerId)">{{ scope.row.buyerName }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="卖家用户">
					<template slot-scope="scope">
						<span style="color: #008000;cursor: pointer;"
							@click="updates(scope.row.lastOwnerId)">{{ scope.row.lastOwnerName }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="买家用户">
					<template slot-scope="scope">
						<span style="color: #008000;cursor: pointer;"
							@click="updates(scope.row.buyerId)">{{ scope.row.buyerName }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="铸造人">
					<template slot-scope="scope">
						<span style="color: #008000;cursor: pointer;" v-if="scope.row.box"
							@click="updates(scope.row.box.originalId)">{{ scope.row.box?scope.row.box.originalName:'' }}</span>
						<span style="color: #008000;cursor: pointer;" v-else
							@click="updates(scope.row.originalId)">{{ scope.row?scope.row.originalName:'' }}</span>
					</template>
				</el-table-column>
				<el-table-column prop="orderNumber" label="订单编号" width="180">
				</el-table-column>
				<el-table-column prop="orderType" label="订单类型">
					<template slot-scope="scope">
						<span v-if="scope.row.orderType==1">出售</span>
						<span v-if="scope.row.orderType==2">盲盒</span>
						<span v-if="scope.row.orderType==3">拍卖</span>
						<span v-if="scope.row.orderType==4">转赠</span>
						<span v-if="scope.row.orderType==5">预售</span>
					</template>
				</el-table-column>
				<el-table-column prop="collection.boxName" label="作品名称">
					<template slot-scope="scope">
						<span v-if="scope.row.orderType==2">{{scope.row.box.boxName}}</span>
						<span v-else>{{scope.row.collection?scope.row.collection.nftName:''}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="nftTitleImg" label="封面图" width="100">
					<template slot-scope="scope">
						<block v-if="scope.row.orderType==2">
							<img v-if="scope.row.box&&scope.row.box.nftTitleImg" :src="scope.row.box.nftTitleImg"
								width="40" height="40" />
							<span v-else>暂无图片</span>
						</block>
						<block v-else>
							<img v-if="scope.row.collection&&scope.row.collection.nftTitleImg"
								:src="scope.row.collection.nftTitleImg" width="40" height="40" />
							<span v-else>暂无图片</span>
						</block>
					</template>
				</el-table-column>
				<el-table-column prop="nftImg" label="轮播图" width="200">
					<template slot-scope="scope">
						<block v-if="scope.row.orderType==2">
							<div v-if="scope.row.box&&scope.row.box.boxImg">
								<div v-for="(item,index) in scope.row.box.boxImg.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</block>
						<block v-else>
							<div v-if="scope.row.collection&&scope.row.collection.nftImg">
								<div v-for="(item,index) in scope.row.collection.nftImg.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</block>
					</template>
				</el-table-column>
				<el-table-column prop="num" label="购买数量">
				</el-table-column>
				<el-table-column prop="price" label="价格">
				</el-table-column>
				<el-table-column prop="totalPrice" label="总价">
				</el-table-column>

				<el-table-column prop="payMoney" label="支付金额">
				</el-table-column>
				<el-table-column prop="nftRateMoney" label="版权费">
				</el-table-column>
				<el-table-column prop="serviceCharge" label="服务费">
				</el-table-column>
				<el-table-column prop="sellerAmount" label="卖方最终获得的金额">
				</el-table-column>
				<el-table-column prop="gasMoney" label="gas总费">
				</el-table-column>
				<el-table-column prop="payMoney" label="支付金额">
				</el-table-column>
				<el-table-column prop="payType" label="支付方式">
					<template slot-scope="scope">
						<span v-if="scope.row.payType==0">余额支付</span>
						<span v-if="scope.row.payType==1">微信支付</span>
						<span v-if="scope.row.payType==2">二维码支付</span>
						<span v-if="scope.row.payType==3">公众号支付</span>
						<span v-if="scope.row.payType==4">支付宝支付</span>
						<span v-if="scope.row.payType==5">h5支付宝支付</span>
					</template>
				</el-table-column>
				<el-table-column prop="remarks" label="备注">
				</el-table-column>
				<el-table-column prop="status" label="订单状态" fixed="right">
					<template slot-scope="scope">
						<span style="color: #f56c6c;" v-if="scope.row.status==0">待支付</span>
						<span style="color: #E87D0D;" v-if="scope.row.status==1">已支付</span>
						<span style="color: #009900;" v-if="scope.row.status==2">已完成</span>
						<span style="color: #999;" v-if="scope.row.status==3">已取消</span>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<!-- <el-table-column prop="createTime" fixed="right" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="mini" style="margin: 2px;" type="primary"
							:disabled="!isAuth('orderCenter:accomplish')" v-if="scope.row.state==1"
							@click="cancelOrder(scope.row,2)">完成</el-button>
						<el-button size="mini" style="margin: 2px;" type="warning"
							:disabled="!isAuth('orderCenter:refund')" v-if="scope.row.state==1"
							@click="cancelOrder(scope.row,3)">退款</el-button>
						<el-button size="mini" style="margin: 2px;" type="danger"
							:disabled="!isAuth('orderCenter:delete')" @click="deletes(scope.row)">删除</el-button>
					</template>
				</el-table-column> -->
			</el-table>
			<div style="color: #B94A48;font-size: 20px;margin-top: 10px;display: inline-block;">
				本页累计交易统计：{{totalMoney.toFixed(2)}}元</div>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</el-tab-pane>
	</el-tabs>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				state: 2,
				status: '',
				orderNum: '',
				type: '',
				id: '',
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
						label: '已支付'
					},
					{
						value: 2,
						label: '已完成'
					},
					{
						value: 3,
						label: '已取消'
					},
					// {
					// 	value: 4,
					// 	label: '已删除'
					// }
				],
				typeNum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '出售'
					},
					{
						value: 2,
						label: '盲盒'
					},
					{
						value: 3,
						label: '拍卖'
					},
					{
						value: 4,
						label: '转赠'
					},
					{
						value: 5,
						label: '预售'
					},
				],
				form: {
					id: '',
					title: '',
					url: '',
					type: '',
					state: '',
					types: [{
							value: 'word',
							label: '文字'
						},
						{
							value: 'url',
							label: '链接'
						}
					],
				},
				phone: '',
				content: '',
				flag: '',
				flags: [{
						value: 1,
						label: '单个用户'
					},
					{
						value: 2,
						label: '全部用户'
					}
				],
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible: false,
				tableData: {},
				totalMoney: 0,
			}
		},
		methods: {
			handleSizeChange(val) {
				this.limit = val;
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.dataSelect()
			},
			handleClick(tab, event) {
				if (tab._props.label == '用户反馈') {
					this.page = 1
					this.limit = 10
					this.state = 2
					this.dataSelect()
				}

			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.totalMoney = 0
				this.$http({
					url: this.$http.adornUrl('admin/order/getOrderList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'orderType': this.type,
						'status': this.status,
						'orderNumber': this.orderNum
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.records) {
						if (data.data.records[i].payMoney) {
							this.totalMoney = this.totalMoney + data.data.records[i].payMoney
						}
					}
					let returnData = data.data;
					this.tableData = returnData

				})
			},
			// 详情跳转
			updates(userId) {
				if (userId) {
					this.$router.push({
						path: '/userDetail',
						query: {
							userId: userId
						}
					})
				}

			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.userName = ''
				this.status = ''
				this.type = ''
				this.orderNum = ''
				this.dataSelect()
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.dataSelect()
			},
			// 删除订单
			deletes(row) {
				let delid = row.ordersId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/deleteOrders?id=${delid}`),
						method: 'post',
						params: this.$http.adornData({})
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
				})
			},
			cancelOrder(row, status) {
				let delid = row.ordersId
				let content = ''
				if (status == 2) {
					content = "确定完成此订单？"
				} else {
					content = "确定退款此订单？"
				}
				this.$confirm(content, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`orders/cancelOrder?id=${delid}&status=${status}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				})
			},

		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>
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
