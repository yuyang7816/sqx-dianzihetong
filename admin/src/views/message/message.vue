<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="用户反馈" name="first">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="phone" label="联系方式">
				</el-table-column>
				<el-table-column prop="content" label="内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间" width="160">
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="服务投诉" name="second">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="投诉用户">
					<template slot-scope="scope">
						<div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.userId)">
							{{scope.row.userName}}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="被投诉用户">
					<template slot-scope="scope">
						<div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.byUserId)">
							{{scope.row.byUserName}}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="投诉内容" width="200">
				</el-table-column>
				<el-table-column prop="image" label="图片" width="150">
					<template slot-scope="scope">
						<div v-for="(item,index) in scope.row.image" :key="index"
							style="display: inline-block; margin: 3px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态">
					<template slot-scope="scope">
						<div v-if="scope.row.status==0">待审核</div>
						<div v-else>已审核</div>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="处理结果">
					<template slot-scope="scope">
						<div v-if="scope.row.status==0">待处理</div>
						<div v-if="scope.row.status==1">不处理</div>
						<div v-if="scope.row.status==2">已封号</div>
						<div v-if="scope.row.status==3">已下架</div>
					</template>
				</el-table-column>
				<el-table-column prop="auditContent" label="审核内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" v-if="scope.row.status==0"
							@click="refuseClick(scope.row)">处理
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="店铺投诉" name="secondDp">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="投诉用户">
					<template slot-scope="scope">
						<div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.userId)">
							{{scope.row.userName}}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="被投诉用户">
					<template slot-scope="scope">
						<div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.byUserId)">
							{{scope.row.byUserName}}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="投诉内容" width="200">
				</el-table-column>
				<el-table-column prop="image" label="图片" width="150">
					<template slot-scope="scope">
						<div v-for="(item,index) in scope.row.image" :key="index"
							style="display: inline-block; margin: 3px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态">
					<template slot-scope="scope">
						<div v-if="scope.row.status==0">待审核</div>
						<div v-else>已审核</div>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="处理结果">
					<template slot-scope="scope">
						<div v-if="scope.row.status==0">待处理</div>
						<div v-if="scope.row.status==1">不处理</div>
						<div v-if="scope.row.status==2">已封号</div>
						<div v-if="scope.row.status==3">已下架</div>
					</template>
				</el-table-column>
				<el-table-column prop="auditContent" label="审核内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" v-if="scope.row.status==0"
							@click="refuseClick(scope.row)">处理
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="聊天投诉" name="secondLt">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="投诉用户">
					<template slot-scope="scope">
						<div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.userId)">
							{{scope.row.userName}}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="被投诉用户">
					<template slot-scope="scope">
						<div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.byUserId)">
							{{scope.row.byUserName}}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="content" label="投诉内容" width="200">
				</el-table-column>
				<el-table-column prop="image" label="图片" width="150">
					<template slot-scope="scope">
						<div v-for="(item,index) in scope.row.image" :key="index"
							style="display: inline-block; margin: 3px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态">
					<template slot-scope="scope">
						<div v-if="scope.row.status==0">待审核</div>
						<div v-else>已审核</div>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="处理结果">
					<template slot-scope="scope">
						<div v-if="scope.row.status==0">待处理</div>
						<div v-if="scope.row.status==1">不处理</div>
						<div v-if="scope.row.status==2">已封号</div>
						<div v-if="scope.row.status==3">已下架</div>
					</template>
				</el-table-column>
				<el-table-column prop="auditContent" label="审核内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" v-if="scope.row.status==0"
							@click="refuseClick(scope.row)">处理
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="用户投诉" name="secondYh">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="投诉用户">
					<template slot-scope="scope">
						<div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.userId)">
							{{scope.row.userName}}
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="被投诉用户">
					<template slot-scope="scope">
						<div style="color: #0066CC;cursor: pointer;" @click="updataDetails(scope.row.byUserId)">
							{{scope.row.byUserName}}
						</div>
					</template>
				</el-table-column>
				<!-- <el-table-column prop="platform" label="投诉类型">
					<template slot-scope="scope">
						<div v-if="scope.row.platform==1">聊天投诉</div>
						<div v-if="scope.row.platform==2">用户投诉</div>
						<div v-if="scope.row.platform==3">动态投诉</div>
						<div v-if="scope.row.platform==4">服务投诉</div>
					</template>
				</el-table-column> -->
				<el-table-column prop="content" label="投诉内容" width="200">
				</el-table-column>
				<el-table-column prop="image" label="图片" width="150">
					<template slot-scope="scope">
						<div v-for="(item,index) in scope.row.image" :key="index"
							style="display: inline-block; margin: 3px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
								<img style="width: 300px; height: auto" :src="item" alt="">
							</el-popover>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="状态">
					<template slot-scope="scope">
						<div v-if="scope.row.status==0">待审核</div>
						<div v-else>已审核</div>
					</template>
				</el-table-column>
				<el-table-column prop="status" label="处理结果">
					<template slot-scope="scope">
						<div v-if="scope.row.status==0">待处理</div>
						<div v-if="scope.row.status==1">不处理</div>
						<div v-if="scope.row.status==2">已封号</div>
						<div v-if="scope.row.status==3">已下架</div>
					</template>
				</el-table-column>
				<el-table-column prop="auditContent" label="审核内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" v-if="scope.row.status==0"
							@click="refuseClick(scope.row)">处理
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</el-tab-pane>
		<el-tab-pane label="用户消息" name="fourth">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
				</el-table-column>
				<el-table-column prop="title" label="消息标题">
				</el-table-column>
				<el-table-column prop="content" label="消息内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</el-tab-pane>
		<!-- <el-tab-pane label="订单消息" name="fiveth">
			<el-table v-loading="tableDataLoading" :data="tableData.records">
				<el-table-column prop="id" label="编号" width="80">
				</el-table-column>
				<el-table-column prop="userName" label="用户名称">
				</el-table-column>
				<el-table-column prop="title" label="消息标题">
				</el-table-column>
				<el-table-column prop="content" label="消息内容">
				</el-table-column>
				<el-table-column prop="createAt" label="创建时间">
				</el-table-column>
				<el-table-column label="操作">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="updataDetails(scope.row)">用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
					layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
				</el-pagination>
			</div>
		</el-tab-pane> -->
		<!-- <el-tab-pane label="消息推送" name="seventh">
			<div style="float: right;margin-right:2%;">
				<el-button style='margin: 10px 0;' :disabled="!isAuth('message:push')" size="mini"  type="primary" icon="document" @click="magNotice">消息推送</el-button>
			</div>
			<el-table
			v-loading="tableDataLoading"
				:data="tableData.list">
			<el-table-column
				fixed
			 prop="id"
			 label="编号"
			 width="80">
			 </el-table-column>
			 <el-table-column
				prop="userName"
				label="用户名称">
				</el-table-column>
				<el-table-column
				 prop="title"
				 label="消息标题">
				 </el-table-column>
				<el-table-column
				prop="content"
				label="消息内容"
				width="280">
				</el-table-column>
				<el-table-column
				prop="createAt"
				label="创建时间">
				</el-table-column>
				<el-table-column
					label="操作"
				width="120">
					<template slot-scope="scope">
						<el-button
							size="mini"
							type="primary"
							@click="updataDetails(scope.row)">用户详情
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination
					@size-change="handleSizeChange"
					@current-change="handleCurrentChange"
					:page-sizes="[10, 20, 30, 50, 100]"
					:page-size="limit"
					:current-page="page"
					layout="total,sizes, prev, pager, next,jumper"
					:total="tableData.totalCount">
				</el-pagination>
			</div> -->
		<!-- 消息推送 -->
		<!-- <el-dialog title="消息推送" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">选择：</span>
					<el-select v-model="flag" placeholder="请选择类型" style="width:50%" >
					  <el-option v-for="item in flags" :key="item.value" :label="item.label" :value="item.value">
					  </el-option>
					</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				</div>
				<div style="margin-bottom: 10px;" v-if="flag == 1">
					<span style="width: 200px;display: inline-block;text-align: right;">用户手机号：</span>
					<el-input style="width: 50%;" v-model="phone" placeholder="请输入用户手机号"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">消息标题：</span>
					<el-input style="width: 50%;" v-model="title" placeholder="请输入消息标题"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;position: relative;top: -65px;">消息内容：</span>
					<el-input style="width: 50%;" type="textarea" rows="4" v-model="content" placeholder="请输入消息内容"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="magNoticeTo()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tab-pane> -->
		<el-dialog title="投诉处理" :visible.sync="dialogFormVisible" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核结果：</span>
				<el-radio-group v-model="radio">
					<el-radio :label="0">通过</el-radio>
					<el-radio :label="1">驳回</el-radio>
				</el-radio-group>
			</div>
			
			<div style="margin-bottom: 10px;" v-if="radio===0">
				<span style="width: 200px;display: inline-block;text-align: right;">处理结果：</span>
				<el-radio-group v-model="clStatus">
					<el-radio :label="0">封号</el-radio>
					<el-radio :label="1">下架</el-radio>
					<el-radio :label="2">不处理</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span
					style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">审核理由：</span>
				<el-input style="width:50%;" v-model="content" type="textarea" :rows="4" placeholder="请输入审核理由">
				</el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
	</el-tabs>
</template>

<script>
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				state: 2,
				url: '',
				title: '',
				type: '',
				id: '',
				types: [{
						value: 'word',
						label: '文字'
					},
					{
						value: 'url',
						label: '链接'
					}
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
				tableData: [],
				radio: '',
				clStatus: '',
				messageId: '',
				platform: '',
				platform1: '',
				platformList: [{
						id: '',
						name: '全部'
					},
					{
						id: 1,
						name: '聊天投诉'
					},
					{
						id: 2,
						name: '用户投诉'
					},
					{
						id: 3,
						name: '动态投诉'
					},
					{
						id: 4,
						name: '服务投诉'
					}
				]
			}
		},
		methods: {
			// 详情跳转
			updataDetails(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				});
			},
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
					this.platform1 = ''
					this.dataSelect()
				}
				if (tab._props.label == '服务投诉') {
					this.page = 1
					this.limit = 10
					this.state = 4
					this.platform1 = ''
					this.dataSelect()
				}
				if (tab._props.label == '店铺投诉') {
					this.page = 1
					this.limit = 10
					this.state = 5
					this.platform1 = ''
					this.dataSelect()
				}
				if (tab._props.label == '聊天投诉') {
					this.page = 1
					this.limit = 10
					this.state = 5
					this.platform1 = ''
					this.dataSelect()
				}
				if (tab._props.label == '用户投诉') {
					this.page = 1
					this.limit = 10
					this.state = 7
					this.platform1 = ''
					this.dataSelect()
				}
				
				if (tab._props.label == '用户消息') {
					this.page = 1
					this.limit = 10
					this.state = 6
					this.dataSelect()
				}
				// if (tab._props.label == '订单消息') {
				// 	this.page = 1
				// 	this.limit = 10
				// 	this.state = 4
				// 	this.dataSelect()
				// }
				if (tab._props.label == '消息推送') {
					this.page = 1
					this.limit = 5
					this.state = 8
					this.dataSelect()
				}
			},
			// 消息推送
			magNotice() {
				this.dialogFormVisible2 = true
			},
			// 消息推送
			magNoticeTo() {
				if (this.flag == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择类型',
						type: 'warning'
					});
					return
				}
				if (this.title == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标题',
						type: 'warning'
					});
					return
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入内容',
						type: 'warning'
					});
					return
				}
				if (this.flag == 1) {
					if (this.phone == '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请输入用户手机号',
							type: 'warning'
						});
						return
					}
					this.$http({
						url: this.$http.adornUrl('/cash/sendMsg'),
						method: 'get',
						params: this.$http.adornParams({
							'title': this.title,
							'content': this.content,
							'phone': this.phone,
							'flag': this.flag
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible2 = false
						if (data.code == -100) {
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.phone = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.phone = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
					})
				}
				if (this.flag == 2) {
					this.$http({
						url: this.$http.adornUrl('/cash/sendMsg'),
						method: 'get',
						params: this.$http.adornParams({
							'title': this.title,
							'content': this.content,
							'flag': this.flag,
						})
					}).then(({
						data
					}) => {
						this.dialogFormVisible2 = false
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.title = ''
									this.content = ''
									this.flag = ''
									this.dataSelect()
								}
							})
						}
					})
				}
			},
			// 获取数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl(`message/selectMessageByType`),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.state,
						'platform': this.platform1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					// if (this.state == 2) {
					// 	for (var i in data.data.list) {
					// 		var a = data.data.list[i].content.toString()
					// 		var b = JSON.parse(a)
					// 		data.data.list[i].content = b.content
					// 		console.log('key', b.content)
					// 	}
					// }
					for (var i in data.data.records) {
						if (data.data.records[i].image) {
							data.data.records[i].image = data.data.records[i].image.split(',')
						}

					}

					let returnData = data.data;
					this.tableData = returnData

				})
			},
			//审核弹框
			refuseClick(rows) {
				this.radio = ''
				this.clStatus = ''
				this.content = ''
				this.messageId = rows.id
				this.platform = rows.platform
				this.dialogFormVisible = true
			},
			//提交审核
			refuseto() {
				if (this.radio === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择审核结果',
						type: 'warning'
					});
					return
				}
				
				if (this.radio === 0) {
					if (this.clStatus === '') {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: '请选择处理结果',
							type: 'warning'
						});
						return
					}
				}
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入审核理由',
						type: 'warning'
					});
					return
				}
				var status = ''
				if (this.radio === 1) {
					status = 1
				} else {
					if (this.clStatus === 0) {
						status = 2
					}
					if (this.clStatus === 1) {
						status = 3
					}
					if (this.clStatus === 2) {
						status = 1
					}
				}

				this.$http({
					url: this.$http.adornUrl('message/auditMessage'),
					method: 'post',
					params: this.$http.adornParams({
						'messageId': this.messageId,
						'status': status,
						'auditContent': this.content
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
								this.content = ''
								this.status = -1
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {}
						})
					}

				})
			},
			animeDat4() {
				this.dataSelect()
			}
		},
		mounted() {
			this.dataSelect()
		}
	};
</script>

<style>

</style>
