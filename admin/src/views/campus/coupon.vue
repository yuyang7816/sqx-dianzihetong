<template>
	<div>
		<!--列表-->
		<div style="text-align: right;margin-right: 20px;">
			<el-button size="small" type="primary" @click="addVipType(1,{})" :disabled="!isAuth('coupon:add')">新增</el-button>
		</div>
		
		<el-table v-loading="tableDataLoading" :data="list.records" style="width: 100%">
			<el-table-column label="编号" prop="id" width="80">
			</el-table-column>
			<el-table-column label="名称" prop="vipName">
			</el-table-column>
			<el-table-column label="月数" prop="vipMonthCount">
			</el-table-column>
			<el-table-column label="会员价格" prop="money">
			</el-table-column>
			<!-- <el-table-column label="折扣价" prop="discountMoney">
			</el-table-column> -->
			<el-table-column label="时间" prop="updateTime">
			</el-table-column>
			<el-table-column fixed="right" label="是否启用">
				<template slot-scope="scope">
					<el-switch v-model="scope.row.isEnable" :active-value="1" :inactive-value="0" active-color="#13ce66"
						inactive-color="#ff4949" @change="e=>change(e,scope.row)" :disabled="!isAuth('coupon:update')">
					</el-switch>
				</template>
			</el-table-column>
			<el-table-column fixed="right" label="操作" width="160">
				<template slot-scope="scope">

					<el-button size="mini" type="primary" @click="addVipType(2,scope.row)" :disabled="!isAuth('coupon:update')">编辑</el-button>
					<el-button size="mini" type="danger"@click="deleteVipType(scope.row.id)" :disabled="!isAuth('coupon:delete')">删除</el-button>
				</template>
			</el-table-column>
		</el-table>
		<!--分页-->
		<el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 50, 100]" :total="list.total"
			layout="total,sizes, prev, pager, next,jumper" @size-change="handleSizeChange"
			@current-change="handleCurrentChange">
		</el-pagination>
		<!--新增弹窗-->
		<el-dialog :title="dialogVisibleTitle" :visible.sync="dialogVisible" width="60%" @close="resetForm('ruleForm')">
			<el-form ref="ruleForm" :model="ruleForm" :rules="rules" class="demo-ruleForm" label-width="100px">
				<el-form-item label="会员名称" prop="vipName">
					<el-input v-model="ruleForm.vipName" placeholder="请输入会员名称"></el-input>
				</el-form-item>
				<el-form-item label="会员价格" prop="money">
					<el-input v-model="ruleForm.money" placeholder="请输入会员价格" type="number"></el-input>
				</el-form-item>
				<!-- <el-form-item label="折扣价格" prop="discountMoney">
					<el-input v-model="ruleForm.discountMoney" placeholder="请输入折扣价格" type="number"></el-input>
				</el-form-item> -->
				<el-form-item label="月数" prop="vipMonthCount">
					<el-input-number v-model="ruleForm.vipMonthCount" :min="1" label="请输入月数"></el-input-number>
				</el-form-item>
				<el-form-item label="是否启用" prop="isEnable">
					<el-radio v-model="ruleForm.isEnable" :label="1">启用</el-radio>
					<el-radio v-model="ruleForm.isEnable" :label="0">不启用</el-radio>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="submitForm('ruleForm')">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				page: 1,
				limit: 10,
				list: {},
				dialogVisible: false,
				tableDataLoading: false,
				dialogVisibleTitle: '',
				ruleForm: {
					vipName: '',
					money: '',
					discountMoney: '',
					vipMonthCount: '',
					isEnable: 1,
					id: '',
				},
				rules: { //表单验证
					vipName: [{
						required: true,
						message: '请输入会员名称',
						trigger: 'blur'
					}, ],
					money: [{
						required: true,
						message: '请输入会员价格',
						trigger: 'blur'
					}, ],
					// discountMoney: [{
					// 	required: true,
					// 	message: '请输入折扣价格',
					// 	trigger: 'blur'
					// }, ],
					vipMonthCount: [{
						required: true,
						message: '请输入月数',
						trigger: 'blur'
					}, ],
					isEnable: [{
						required: true,
						message: '请选择',
						trigger: 'blur'
					}, ],
				}
			}
		},
		mounted() {
			this.dataSelect()
		},
		methods: {
			//删除
			deleteVipType(id) {
				this.$confirm('此操作将永久删除该分类, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/vipDetails/deleteVipDetails'),
						method: 'get',
						params: this.$http.adornParams({
							id: id
						})
					}).then(({
						data
					}) => {
						if (data && data.code === 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
							})
							this.dataSelect()
						} else {
							this.$message.error(data.msg)
						}

					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消删除'
					});
				});
			},
			//启用不启用
			change(e, row) {
				let obj = {
					vipName: row.vipName,
					money: row.money,
					discountMoney: row.discountMoney,
					vipMonthCount: row.vipMonthCount,
					isEnable: e,
					id: row.id,
				}
				this.$http({
					url: this.$http.adornUrl('admin/vipDetails/updateVipDetails'),
					method: 'post',
					data: this.$http.adornData(obj)
				}).then(({
					data
				}) => {
					if (data && data.code === 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
						})
						this.dialogVisible = false
						this.dataSelect()
					} else {
						this.dialogVisible = false
						this.$message.error(data.msg)
					}

				})
			},
			//重置表单
			resetForm(formName) {
				this.$refs[formName].resetFields()
				this.ruleForm = {
					vipName: '',
					money: '',
					discountMoney: '',
					vipMonthCount: '',
					isEnable: '1',
					id: '',
				}
			},
			//提交
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						if (this.ruleForm.id === '') {
							this.$http({
								url: this.$http.adornUrl('admin/vipDetails/insetVipDetails'),
								method: 'post',
								data: this.$http.adornData(this.ruleForm)
							}).then(({
								data
							}) => {
								if (data && data.code === 0) {
									this.$message({
										message: '操作成功',
										type: 'success',
									})
									this.dialogVisible = false
									this.dataSelect()
								} else {
									this.dialogVisible = false
									this.$message.error(data.msg)
								}

							})
						} else {
							this.$http({
								url: this.$http.adornUrl('admin/vipDetails/updateVipDetails'),
								method: 'post',
								data: this.$http.adornData(this.ruleForm)
							}).then(({
								data
							}) => {
								if (data && data.code === 0) {
									this.$message({
										message: '操作成功',
										type: 'success',
									})
									this.dialogVisible = false
									this.dataSelect()
								} else {
									this.dialogVisible = false
									this.$message.error(data.msg)
								}

							})
						}

					} else {
						console.log('error submit!!')
						return false
					}
				})
			},
			//新增弹窗
			addVipType(type, row) { // type:  1:新增,2:修改
				if (type === 1) {
					this.dialogVisibleTitle = '新增'
				} else {
					this.dialogVisibleTitle = '修改'
					this.ruleForm = {
						vipName: row.vipName,
						money: row.money,
						discountMoney: row.discountMoney,
						vipMonthCount: row.vipMonthCount,
						isEnable: row.isEnable,
						id: row.id,
					}
				}
				this.dialogVisible = true

			},
			//分页
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			//获取会员列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/vipDetails/getVipDetailsList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data && data.code === 0) {
						let returnData = data.data
						this.list = returnData
					}

				})
			},
		},

	}
</script>

<style>

</style>