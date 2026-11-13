<template>
	<div>
		<div style="margin:2% 0;display: inline-block;">
			<span>类型：</span>
			<el-select v-model="signTypeT" placeholder="请选择类型" style="width:150px;" @change="selectTrigger()">
				<el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<div style="margin:2% 0;display: inline-block;">
			<span>公告标题：</span>
			<el-input v-model="title" clearable placeholder="请输入公告标题" style="width: 150px;"
				@keydown.enter.native="select"></el-input>
			&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<div style="display: inline-block;">
			<el-button icon="document" size="mini" style="margin-left:15px;" type="primary" @click="select">查询
			</el-button>
			<el-button icon="document" size="mini" style="margin-left:15px;" type="primary" @click="cleans">重置
			</el-button>
			<!-- <el-button :disabled="!isAuth('dynamic:add')" icon="document" size="mini" style="margin-left:15px;"
				type="primary" @click="addOrEditGongGao('新增',{})">新增
			</el-button> -->
		</div>
		<el-table v-loading="tableDataLoading" :data="gongGaoList.records">
			<el-table-column fixed label="编号" width="80" prop="id">
			</el-table-column>
			<el-table-column label="用户昵称" prop="userName">
				<template slot-scope="scope">
					<span style="color: #409EFF;cursor: pointer"
						@click="updates(scope.row)">{{ scope.row.userName }}</span>
				</template>
			</el-table-column>
			<el-table-column  prop="signType" label="类型">
				<template slot-scope="scope">
					<span style="color: red;" v-if="scope.row.signType==1">手动签名</span>
					<span style="color: green;" v-if="scope.row.signType==2">系统生成签名</span>
				</template>
			</el-table-column>
			<el-table-column label="个人印章" prop="sealUrl">
				<template slot-scope="scope">
					<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.sealUrl">
						<div style="margin: 2px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.sealUrl" alt=""
									slot="reference">
								<img style="width: 300px; height: 300px" :src="scope.row.sealUrl" alt="">
							</el-popover>
						</div>
					</div>
					<span v-else>-</span>
				</template>
			</el-table-column>
			<el-table-column label="个人签名" prop="signUrl">
				<template slot-scope="scope">
					<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.signUrl">
						<div style="margin: 2px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.signUrl" alt=""
									slot="reference">
								<img style="width: 300px; height: 300px" :src="scope.row.signUrl" alt="">
							</el-popover>
						</div>
					</div>
					<span v-else>-</span>
				</template>
			</el-table-column>
			<el-table-column label="公司印章" prop="companySeal">
				<template slot-scope="scope">
					<div style="display:flex;flex-wrap: wrap;" v-if="scope.row.companySeal">
						<div style="margin: 2px;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img style="width: 50px; height: 50px" :src="scope.row.companySeal" alt=""
									slot="reference">
								<img style="width: 300px; height: 300px" :src="scope.row.companySeal" alt="">
							</el-popover>
						</div>
					</div>
					<span v-else>-</span>
				</template>
			</el-table-column>
			<el-table-column label="创建时间" prop="createTime" width="150">
			</el-table-column>
			<!-- <el-table-column label="操作" width="150">
				<template slot-scope="scope">
					<el-button :disabled="!isAuth('dynamic:update')" size="mini" style="margin: 10px 0;" type="primary"
						@click="addOrEditGongGao('修改',scope.row)">修改
					</el-button>
					<el-button :disabled="!isAuth('dynamic:delete')" size="mini" type="danger"
						@click="advertdeletes(scope.row)">删除
					</el-button>
				</template>
			</el-table-column> -->
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 40]"
				:total="gongGaoList.total" layout="total,sizes, prev, pager, next,jumper"
				@size-change="handleSizeChange" @current-change="handleCurrentChange">
			</el-pagination>
		</div>
		<!--新增/修改弹窗-->
		<el-dialog @close="clearFrom" :title="dialogTitle" :visible.sync="dialogVisible" width="40%">
			<el-form ref="dynamicValidateForm" :model="from" class="demo-dynamic" label-width="100px">
				<el-form-item :rules="[
      					{ required: true, message: '请输入公告标题', trigger: 'blur' },
    				]" label="公告标题" prop="title">
					<el-input v-model="from.title"></el-input>
				</el-form-item>
				<el-form-item :rules="[
      					{ required: true, message: '状态', trigger: 'blur' },
    				]" label="状态" prop="status">
					<el-radio-group v-model="from.status">
						<el-radio :label='1'>已发布</el-radio>
						<el-radio :label='0'>未发布</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item :rules="[
      					{ required: true, message: '请输入公告内容', trigger: 'blur' },
    				]" label="公告内容" prop="content">
					<el-input type="textarea" :rows="4" placeholder="请输入公告内容" v-model="from.content">
					</el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="submitForm('dynamicValidateForm')">确 定</el-button>
			</span>
		</el-dialog>

	</div>
</template>

<script>
	export default {
		data() {
			return {
				from: {
					title: '',
					content: '',
					status: 1,
					noticeId: '',
				},
				dialogTitle: '',
				dialogVisible: false,
				page: 1,
				limit: 10,
				title: '',
				status: '',
				signTypeT:3,
				statusList: [{
						label: '全部',
						value: 3
					},
					{
						label: '手动签名',
						value: 1
					}, {
						label: '系统生成签名',
						value: 2
					}
				],
				gongGaoList: {},
				tableDataLoading: false,
			}
		},
		mounted() {
			this.getGongGaoList()
		},
		methods: {
			//删除公告
			advertdeletes(item) {
				this.$confirm('此操作将删除该公告, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/notice/deleteNoticeById'),
						method: 'get',
						params: this.$http.adornParams({
							noticeId: item.noticeId
						})
					}).then(({
						data
					}) => {
						if (data && data.code === 0) {
							this.$message({
								message: '删除成功',
								type: 'success'
							});
							this.getGongGaoList()
						} else {
							this.$message.error(data.msg);
							this.getGongGaoList()
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			//关闭弹窗后重置表单
			clearFrom() {
				this.$refs['dynamicValidateForm'].resetFields();
				this.from.noticeId = ''
			},
			//提交公告(修改/删除)
			submitForm(formName) {
				let url = ''
				if (this.dialogTitle == '修改') {
					url = 'admin/notice/updateNotice'
				} else {
					url = 'admin/notice/insertNotice'
				}
				this.$refs[formName].validate((valid) => {
					if (valid) {
						this.$http({
							url: this.$http.adornUrl(url),
							method: 'post',
							data: this.$http.adornData(this.from)
						}).then(({
							data
						}) => {
							if (data && data.code === 0) {
								this.$message({
									message: '提交成功',
									type: 'success'
								});
								this.getGongGaoList()
							} else {
								this.$message.error(data.msg);
								this.getGongGaoList()
							}
							this.dialogVisible = false
						})
					} else {
						console.log('error submit!!');
						return false;
					}
				});
			},
			//新增/修改公告
			addOrEditGongGao(title, row) {
				this.dialogTitle = title
				if (JSON.stringify(row) !== '{}') {
					this.from = {
						title: row.title,
						content: row.content,
						status: row.status,
						noticeId: row.noticeId,
					}
				}
				this.dialogVisible = true
			},
			//分页
			handleSizeChange(val) {
				this.limit = val
				this.getGongGaoList()
			},
			handleCurrentChange(val) {
				this.page = val
				this.getGongGaoList()
			},
			//重置查询条件
			cleans() {
				this.signTypeT = 3
				this.title = ''
				this.page = 1
				this.getGongGaoList()
			},
			//点击查询按钮查询
			select() {
				this.page = 1
				this.getGongGaoList()
			},
			//状态筛选
			selectTrigger(status) {
				this.page = 1
				this.getGongGaoList()
			},
			// 获取公告数据
			getGongGaoList() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/contractSeal/selectContractSealList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						// 'title': this.title,
						'signType': this.signTypeT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					this.gongGaoList = data.data
				})
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
		},

	}
</script>

<style>
	.customWidth {
		width: 80% !important;
	}
</style>
