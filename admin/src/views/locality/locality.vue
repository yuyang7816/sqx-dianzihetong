<template>
	<el-tabs v-model="activeName" @tab-click="handleClick">
		<el-tab-pane label="模板分类" name="first">
			<div>
				<div style="position: relative;display: inline-block;margin:5px;">
					<span>分类名称：</span>
					<el-input v-model="search" placeholder="请输入空投名称" style="width: 200px;"
						@keydown.enter.native="phoneSelect">
					</el-input>&nbsp;&nbsp;
				</div>
				<div style="position: relative;display: inline-block;margin:5px;">
					<span>启用状态：</span>
					<el-select v-model="statusKt" style="width:150px;" @change="phoneSelect">
						<el-option v-for="item in statuss" :key="item.value" :label="item.title" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;
				</div>
				<el-button icon="document" size="mini" style="margin:5px;" type="primary" @click="phoneSelect">查询
				</el-button>
				<el-button icon="document" size="mini" style="margin:5px;" type="primary" plain @click="cleans">重置
				</el-button>&nbsp;&nbsp;
				<el-button :disabled="!isAuth('locality:add')" icon="document" size="mini" style="margin:5px;"
					type="primary" @click="openDialog(null,1)">添加
				</el-button>
			</div>
			<el-table v-loading="tableDataLoading" :data="classList.records">
				<el-table-column label="编号" prop="id" width="80">
				</el-table-column>
				<el-table-column label="分类名称" prop="typeName">
				</el-table-column>
				<!-- <el-table-column label="图标" prop="cardFront" width="100">
					<template slot-scope="scope">
						<div v-if="scope.row.classImg == null || scope.row.classImg == ''">
							暂无图片
						</div>
						<div v-if="scope.row.classImg" style="display:flex;flex-wrap: wrap;">
							<el-popover placement="top-start" title="" trigger="hover">
								<img slot="reference" :src="scope.row.classImg" alt=""
									style="width: 50px; height: 50px">
								<img :src="scope.row.classImg" alt="" style="width: 200px; height: 200px">
							</el-popover>
						</div>
					</template>
				</el-table-column> -->
				<!-- <el-table-column label="是否启用" prop="isEnable">
					<template slot-scope="scope">
						<el-switch v-model="scope.row.isEnable" :active-value="openValue"
							:disabled="!isAuth('userList:delete')" :inactive-value="closeValue" active-color="#13ce66"
							inactive-color="#ff4949" @change="change(scope.row.isEnable,scope.row.classId)">
						</el-switch>
					</template>
				</el-table-column> -->
				<el-table-column label="排序" prop="sort">
				</el-table-column>
				<el-table-column label="创建时间" prop="createTime" width="160">
				</el-table-column>
				<el-table-column align="center" fixed='right' label="操作" prop="id" width="240">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" plain @click="getTableByBtn(scope.row)">模板列表
						</el-button>
						<el-button :disabled="!isAuth('locality:update')" size="mini" type="primary"
							@click="openDialog(scope.row,2)">修改
						</el-button>
						<el-button :disabled="!isAuth('locality:delete')" size="mini" type="danger"
							@click="kongtoudelete(scope.row)">删除
						</el-button>
						
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 50, 100]"
					:total="classList.total" layout="total,sizes, prev, pager, next,jumper"
					@size-change="handleSizeChange" @current-change="handleCurrentChange">
				</el-pagination>
			</div>
		</el-tab-pane>
		<!--添加 修改 分类 弹窗-->
		<el-dialog :title="title" :visible.sync="dialogVisible" width="60%" @close="close" center>
			<el-form ref="dynamicValidateForm" :model="dynamicValidateForm" class="demo-dynamic" label-width="200px">
				<el-form-item label="分类名称" prop="typeName">
					<el-input v-model="typeName" placeholder="请输入分类名称" style="width: 70%;"></el-input>
				</el-form-item>
				<!-- <el-form-item label="启用" prop="isEnable">
					<el-radio-group v-model="dynamicValidateForm.isEnable">
						<el-radio :label="1">启用</el-radio>
						<el-radio :label="0">不启用</el-radio>
					</el-radio-group>
				</el-form-item>
				<el-form-item :rules="[
				  { required: true, message: '请上传图标', trigger: 'blur' },
			 ]" label="图标" prop="classImg">
					<el-upload :action="$http.adornUrlUp('alioss/upload')" :on-success="handleAvatarSuccess"
						:show-file-list="false" class="avatar-uploader">
						<img v-if="dynamicValidateForm.classImg" :src="dynamicValidateForm.classImg" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</el-form-item> -->
				<el-form-item label="排序" prop="sort">
					<el-input v-model="sort" placeholder="请输入排序" type="text" style="width: 70%;"></el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="submitForm()">确 定</el-button>
			</span>
		</el-dialog>
		<!--查看模板弹窗 :close-on-click-modal="false" -->
		<el-dialog :visible.sync="show" title="模板列表" width="80%" center>
			<el-button :disabled="!isAuth('locality:update')" size="mini" type="primary"
				@click="openDialogAdd()">添加
			</el-button>
			<el-table v-loading="tableDataLoading1" :data="mubanData1.records">
				<el-table-column label="编号" prop="id" width="80">
				</el-table-column>
				<el-table-column label="模板名称" prop="templateName">
				</el-table-column>
				<el-table-column label="合同文档" prop="contractUrl" width="300">
					<template slot-scope="scope">
						<div v-if="scope.row.contractUrl" style="display:flex;flex-wrap: wrap;">
							{{scope.row.contractUrl}}
						</div>
						<div v-else>-</div>
					</template>
				</el-table-column>
				<el-table-column prop="contractImg" label="上传文件图片" width="200">
					<template slot-scope="scope">
							<div v-if="scope.row.contractImg">
								<div v-for="(item,index) in scope.row.contractImg.split(',')" :key="index"
									style="display: inline-block; margin: 3px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: auto" :src="item" alt="">
									</el-popover>
								</div>
							</div>
					</template>
				</el-table-column>
				<el-table-column label="排序" prop="sort">
				</el-table-column>
				<el-table-column label="创建时间" prop="createTime" width="160">
				</el-table-column>
				<el-table-column align="center" fixed='right' label="操作" prop="id" width="240">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" plain @click="ziduanBtn(scope.row)">填充字段
						</el-button>
						<el-button :disabled="!isAuth('locality:update')" size="mini" type="primary"
							@click="openDialogAdd(scope.row)">修改
						</el-button>
						<el-button :disabled="!isAuth('locality:delete')" size="mini" type="danger"
							@click="deleteClass(scope.row)">删除
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 50, 100]"
					:total="mubanData1.total" layout="total,sizes, prev, pager, next,jumper"
					@size-change="handleSizeChangeM" @current-change="handleCurrentChangeM">
				</el-pagination>
			</div>
		</el-dialog>
		<!--查看模板弹窗-->
		<el-dialog :visible.sync="dialogVisibleM" :title="title" width="70%" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">模板名称：</span>
				<el-input style="width:50%;" v-model="templateName" type="text" placeholder="请输入模板名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">合同文档：</span>
				<el-input style="width:50%;" v-model="contractUrl" type="text" placeholder="请输入合同文档"></el-input>
				<el-upload class="upload-demo" v-model="contractUrl" :action="$http.adornUrlUp('alioss/upload')" style="display: inline-block;"
					:show-file-list="false" :on-success="videoSuccess1" :before-upload="beforeVideoUpload"
					:on-progress="handlePreviewV">
					<el-button icon="document" size="mini" style="margin:5px;" type="primary" plain @click="cleans">上传文档
					</el-button>
				</el-upload>
				<!-- <div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;display: inline-block;">
					<el-upload class="avatar-uploader" v-model="contractUrl"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess">
						<img v-if="contractUrl" :src="contractUrl" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</div> -->
			</div>
			<div style="margin-bottom: 10px;display: flex;align-items: center;">
				<span style="width: 200px;display: inline-block;text-align: right;">上传文件图片：</span>
				<div style="display: flex;align-items: center;flex-wrap: wrap;">
					<div class="imgs" v-for="(item,index) in contractImg" :key="index" style="margin: 3px;">
						<img width="100%" class="images" height="100%" :src="item" alt="">
						<span class="dels">
							<i class="el-icon-delete" @click="dels(index)"></i>
						</span>
					</div>
					<div class="imgs" style="">
						<el-upload :action="$http.adornUrlUp('alioss/upload')" list-type="picture-card"
							:show-file-list="false" :on-success="handleRemove">
							<i class="el-icon-plus"></i>
						</el-upload>
					</div>
				</div>
			
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
				<el-input style="width:50%;" v-model="sort" type="text" placeholder="请输入排序"></el-input>
			</div> 
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">填充字段：</span>
				<!-- <el-input style="width:50%;" v-model="sort" type="text" placeholder="请输入排序"></el-input> -->
				<el-button size="mini" style="margin-bottom: 10px" type="primary" @click="addRow">新增</el-button>
			</div> 
			<el-table :data="templateFields" border style="width: 90%">
				<el-table-column label="字段名称" prop="fieldName">
					<template slot-scope="scope">
						<el-input v-model="scope.row.fieldName" placeholder="请输入列名称"></el-input>
					</template>
				</el-table-column>
				<el-table-column label="字段值" prop="filedValue">
					<template slot-scope="scope">
						<el-input v-model="scope.row.filedValue" placeholder="请输入列描述"></el-input>
					</template>
				</el-table-column>
				
				<!-- <el-table-column label="是否必填" prop="isRequired">
					<template slot-scope="scope">
						<el-select v-model="scope.row.isRequired" placeholder="请选择">
							<el-option :key="1" :value="1" label="是"></el-option>
							<el-option :key="0" :value="0" label="否"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="显示类型" prop="htmlType">
					<template slot-scope="scope">
						<el-select v-model="scope.row.htmlType" placeholder="请选择">
							<el-option :key="1" :value="1" label="文本框"></el-option>
							<el-option :key="2" :value="2" label="文本域"></el-option>
							<el-option :key="3" :value="3" label="下拉框"></el-option>
							<el-option :key="4" :value="4" label="复选框"></el-option>
							<el-option :key="5" :value="5" label="单选框"></el-option>
							<el-option :key="6" :value="6" label="日期控件"></el-option>
						</el-select>
					</template>
				</el-table-column>
				<el-table-column label="选项值" prop="optionJson" width="80">
					<template slot-scope="scope">
						<el-button size="small" type="text"
							@click.native="insterOptionJson(scope.row,scope.row.$index)">编辑
						</el-button>
					</template>
				</el-table-column> -->
				<!-- <el-table-column label="展示类型" prop="showType" width="170">
					<template slot-scope="scope">
						<el-select v-model="scope.row.showType" placeholder="请选择">
							<el-option :key="1" :value="1" label="仅用户端展示"></el-option>
							<el-option :key="2" :value="2" label="仅管理端展示"></el-option>
							<el-option :key="3" :value="3" label="全部展示"></el-option>
						</el-select>
					</template>
				</el-table-column> -->
				<el-table-column label="排序" prop="sort">
					<template slot-scope="scope">
						<el-input v-model="scope.row.sort" placeholder="请输入排序号" type="number"></el-input>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="small" type="text" @click="deleteRow(scope.$index)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisibleM = false">取 消</el-button>
				<el-button type="primary" @click="addRows()">确 定</el-button>
			</span>
		</el-dialog>
		<!--增加选项值弹窗-->
		<el-dialog :visible.sync="show2" :close-on-click-modal="false" title="选项值" width="30%" @close="close2">
			<el-button size="mini" style="margin-bottom: 10px" type="primary" @click="addOptionJson">新增</el-button>
			<el-table :data="mubanData2" border style="width: 100%">
				<el-table-column label="选项值" prop="value">
					<template slot-scope="scope">
						<el-input v-model="scope.row.value" placeholder="请输入选项值"></el-input>
					</template>
				</el-table-column>
				<el-table-column fixed="right" label="操作" width="100">
					<template slot-scope="scope">
						<el-button size="small" type="text" @click="deleteoptionJson(scope.$index)">删除</el-button>
					</template>
				</el-table-column>
			</el-table>
			<span slot="footer" class="dialog-footer">
				<el-button @click="show2 = false">取 消</el-button>
				<el-button type="primary" @click="installSelectValue()">确 定</el-button>
			</span>
		</el-dialog>
		<!--填充字段列表 弹窗 -->
		<el-dialog :visible.sync="dialogVisibleEwzd" title="填充字段列表" width="70%" center>
			<el-table :data="templateFields">
				<el-table-column label="编号" prop="id" width="80">
				</el-table-column>
				<el-table-column label="字段名称" prop="fieldName">
				</el-table-column>
				<el-table-column label="字段值" prop="filedValue">
				</el-table-column>
				<!-- <el-table-column label="是否必填" prop="isRequired">
					<template slot-scope="scope">
						<div v-if="scope.row.isRequired==1">是</div>
						<div v-else>否</div>
					</template>
				</el-table-column>
				<el-table-column label="显示类型" prop="htmlType">
					<template slot-scope="scope">
						<div v-if="scope.row.htmlType==1">文本框</div>
						<div v-if="scope.row.htmlType==2">文本域</div>
						<div v-if="scope.row.htmlType==3">下拉框</div>
						<div v-if="scope.row.htmlType==4">复选框</div>
						<div v-if="scope.row.htmlType==5">单选框</div>
						<div v-if="scope.row.htmlType==6">日期控件</div>
					</template>
				</el-table-column>
				<el-table-column label="选项值" prop="optionJson" width="160">
					<template slot-scope="scope">
						<div v-if="scope.row.optionJson&&scope.row.optionJson.length>0">
						<span v-for="(item,index) in scope.row.optionJson" :key="index">{{item}} ；</span>
						</div>
					</template>
				</el-table-column> -->
				<el-table-column label="排序" prop="sort">
				</el-table-column>
				<!-- <el-table-column label="创建时间" prop="createTime" width="160">
				</el-table-column> -->
			</el-table>
		</el-dialog>
	</el-tabs>
</template>

<script>
	export default {
		data() {
			return {
				activeName: 'first',
				mubanData2: [],
				mubanData: [],
				mubanData1:{},
				show: false,
				dynamicValidateForm: {
					isEnable: 1, //1:启用  2:不启用
					classImg: '', //图标

				},
				id: '',
				typeName: '', //分类名称
				sort: '', //排序
				title: '新增',
				dialogVisible: false,
				closeValue: 0,
				openValue: 1,
				statusKt: '',
				statuss: [{
						value: '',
						title: '全部'
					},
					{
						value: 0,
						title: '不启用'
					},
					{
						value: 1,
						title: '启用'
					}
				],
				search: '',
				page: 1,
				limit: 10,
				classList: {},
				tableDataLoading: false,
				show2: false,
				columnId: '',
				classId: '',
				pageM: 1,
				limitM: 10,
				typeId:'',
				tableDataLoading1:false,
				dialogVisibleM:false,
				
				templateName:'',
				contractUrl:'',
				templateFields:[],
				dialogVisibleEwzd:false,
				contractImg:[],
				tableDataLoading5:false,
				
				
			}
		},
		mounted() {
			this.getClassList()
		},
		methods: {
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '模板分类') {
					this.page = 1
					this.limit = 10
					this.dataSelect()
				}
				if (tab._props.label == '模版列表') {
					this.page = 1
					this.limit = 10
					// this.taskdataSelect()
				}
			},
			
			//获取分类列表数据
			getClassList() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/contractType/getContractTypeList'),
					method: 'get',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'page': this.page,
						'limit': this.limit,
						// 'className': this.search,
						// 'isEnable': this.statusKt
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {
						this.classList = data.data
					}
				})
			},
			//重置
			cleans() {
				this.page = 1
				this.statusKt = ''
				this.search = ''
				this.getClassList()
			
			},
			//查询
			phoneSelect() {
				this.page = 1
				this.getClassList()
			},
			//上传图标
			handleAvatarSuccess(res, file) {
				this.contractUrl = res.data
			},
			// 轮播图片上传
			handleRemove(file) {
				this.contractImg.push(file.data);
			},
			// 删除轮播图
			dels(index) {
				this.contractImg.splice(index, 1);
				console.log(this.img)
			},
			videoSuccess1(file) {
				console.log(file)
				this.contractUrl = file.data;
			
			
				this.tableDataLoading5 = false
			},
			// 验证只能上传文档
			beforeVideoUpload(file) {
				var testmsg = file.name.substring(file.name.lastIndexOf('.') + 1)
				const extension = testmsg === 'docx'
				if (!extension) {
					this.$message({
						message: "上传文件只能是docx格式！",
						type: 'error'
					})
				}
				return extension;
			},
			handlePreviewV(file) {
				this.tableDataLoading5 = true
			},
			// 添加、修改 分类 弹框
			openDialog(row, type) {
				if (type == 1) {
					this.title = '新增'
					this.id = ''
					this.typeName = ''
					this.sort = ''
				} else {
					this.title = '修改'
					this.id = row.id
					this.typeName = row.typeName
					this.sort = row.sort
				}
				this.dialogVisible = true
			},
			// 确认 添加/修改 分类
			submitForm() {
				if (this.typeName === '') {
					this.$message({
						type: 'error',
						message: '列名称不能为空'
					})
					return;
				}
				this.$http({
					url: this.$http.adornUrl('admin/contractType/addContractType'),
					method: 'post',
					data: this.$http.adornData({
						id: this.id,
						typeName: this.typeName, //分类名称
						sort: this.sort, //排序
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							type: 'success',
							message: '操作成功!'
						})
						this.dialogVisible = false
						this.getClassList()
					} else {
						this.$message({
							type: 'error',
							message: data.msg
						})
					}
				})
			},
			
			//删除分类
			kongtoudelete(row) {
				this.$confirm('确定要删除分类', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
				this.$http({
					url: this.$http.adornUrl('admin/contractType/deleteContractType'),
					method: 'post',
					params: this.$http.adornParams({
						'id': row.id,
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							type: 'success',
							message: '删除成功!'
						})
						this.getClassList()
					} else {
						this.$message({
							showClose: true,
							message: data.msg,
							type: 'error'
						})
					}
				})
				})
			},
			//分页
			handleSizeChange(val) {
				this.limit = val
				this.getClassList()
			},
			handleCurrentChange(val) {
				this.page = val
				this.getClassList()
			},
			handleSizeChangeM(val) {
				this.limitM = val
				this.getTableByClassId()
			},
			handleCurrentChangeM(val) {
				this.pageM = val
				this.getTableByClassId()
			},
			
			close2() {
				this.mubanData2 = []
			},
			//新增选项值
			addOptionJson() {
				let obj = {}
				this.mubanData2.push(obj)
				console.log(this.mubanData2)
			},
			//插入选项值
			insterOptionJson(row, index) {
				let arr = JSON.parse(JSON.stringify(row.optionJson))
				arr.map(item => {
					let obj = {
						value: item,
					}
					this.mubanData2.push(obj)
				})
				this.columnId = row.columnId
				this.show2 = true
			},
			//查看模板列表
			getTableByBtn(row) {
				this.typeId = row.id
				this.show = true
				this.getTableByClassId()
			},
			getTableByClassId() {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('admin/contract/selectContractTemplateList'),
					method: 'get',
					params: this.$http.adornParams({
						'typeId': this.typeId,
						'templateName':'',
						'page':1,
						'limit':10,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					if (data.code == 0) {
						this.mubanData1 = data.data
						
					} else {
						this.$message({
							type: 'error',
							message: data.msg
						})
					}
				})
			},
			
			//关闭弹窗后初始化组件
			close() {
				this.dynamicValidateForm = {
					className: '', //分类名称
					isEnable: 1, //1:启用  2:不启用
					classImg: '', //图标
					remarks: '', //备注
					classId: '',
				}
			},
			
			// 添加、修改 模板 弹框
			openDialogAdd(row) {
				if (row) {
					this.title = '修改模板'
					this.id = row.id
					this.templateName = row.templateName
					this.contractUrl = row.contractUrl
					this.sort = row.sort
					
					if(row.templateFields){
						this.templateFields = row.templateFields
					}else{
						this.templateFields = []
					}
					if(row.contractImg){
						this.contractImg = row.contractImg.split(',')
					}else{
						this.contractImg = []
					}
					
				} else {
					this.title = '新增模板'
					this.id = ''
					this.templateName = ''
					this.contractUrl = ''
					this.sort = ''
					this.templateFields = []
					this.contractImg = []
				}
				this.dialogVisibleM = true
			},
			//新增模板
			addRow() {
				let obj = {
					fieldName: '', //字段名
					filedValue: '', //字段描述
					columnLength: '', //字段长度
					isRequired: 1, //是否必填
					htmlType: 1, //显示类型
					optionJson: [], //选项值
					showType: 2, //展示类型
					sort: '', //排序
					
				}
				this.templateFields.push(obj)
			},
			//删除模板
			deleteRow(index) {
				console.log(index)
				this.templateFields.splice(index, 1)
			},
			//确认 添加、修改 模板
			addRows() {
				if (this.templateName === '') {
					this.$message({
						type: 'error',
						message: '模版名称不能为空'
					})
					return;
				}
				this.$http({
					url: this.$http.adornUrl('admin/contract/addContractTemplate'),
					method: 'post',
					data: this.$http.adornData({
						'typeId':this.typeId,
						'id': this.id,
						'templateName': this.templateName, //分类名称
						'contractUrl':this.contractUrl,
						'sort': this.sort, //排序
						'templateFields':this.templateFields,
						'contractImg':this.contractImg.toString(),
						
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						this.$message({
							type: 'success',
							message: '操作成功'
						})
						this.getTableByClassId()
						this.dialogVisibleM = false
					} else {
						this.$message({
							type: 'error',
							message: data.msg
						})
					}
				});
			},
			
			//删除 模板
			deleteClass(row) {
				this.$confirm('确定要删除模板?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/contract/deleteContractTemplate'),
						method: 'post',
						params: this.$http.adornParams({
							'id': row.id
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								type: 'success',
								message: '删除成功!'
							})
							this.getTableByClassId()
						} else {
							this.$message({
								type: 'error',
								message: '删除失败!'
							})
						}
					})

				}).catch(() => {
					this.$message({
						type: 'info', 
						message: '已取消删除'
					})
				})
			},
			//启用/不启用
			change(isEnable, classId) {
				this.$http({
					url: this.$http.adornUrl('admin/scTableClass/updateClass'),
					method: 'post',
					data: this.$http.adornData({
						'classId': classId,
						'isEnable': isEnable
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							showClose: true,
							message: '修改成功',
							type: 'success'
						})
						this.getClassList()
					} else {
						this.$message({
							showClose: true,
							message: data.msg,
							type: 'error'
						})
					}
				})
			},
			
			//插入选项值
			installSelectValue() {
				let arr = []
				this.mubanData2.map(item => {
					arr.push(item.value)
				})
				this.templateFields.map(item => {
					if (this.columnId === item.columnId) {
						item.optionJson = arr
					}
				})
				this.show2 = false
				console.log(this.templateFields)
			},
			//删除选项值
			deleteoptionJson(index) {
				this.mubanData2.splice(index, 1)
			},
			// 额外字段 弹框
			ziduanBtn(row){
				this.templateFields = row.templateFields
				this.dialogVisibleEwzd = true
			},
			

		},

	}
</script>

<style scoped="scoped">
	.customWidth {
		width: 80% !important;
	}

	.avatar-uploader .el-upload {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}

	.avatar-uploader .el-upload:hover {
		border-color: #409EFF;
	}

	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 178px;
		height: 178px;
		line-height: 178px;
		text-align: center;
	}

	.avatar {
		width: 178px;
		height: 178px;
		display: block;
	}

	.adver_main.box {
		display: block;
		max-width: 100%;
		text-align: center;
		border: 1px dotted rgba(67, 79, 103, .4);
	}

	.cards {
		padding: 0 8px;
		margin-bottom: 15px;
	}

	.adver_main.box a {
		display: flex;
		justify-content: center;
		height: 150px;
		line-height: 150px;
		text-decoration: none
	}

	.bannerManin {
		border: 1px solid #e8e8e8;
		font-size: 14px;
		padding: 0 24px;
		display: flex;
		justify-content: center;
		align-items: center;
		height: 113px;
		color: rgba(0, 0, 0, .65);
	}

	.bannerManin span {
		display: inline-block;
		margin-left: 5px;
	}

	.bannerManin img {
		width: 48px;
		height: 48px;
		border-radius: 50%;
	}

	.bannerbtn {
		display: flex;
		border-top: none !important;
		border: 1px solid #e8e8e8;
		padding: 11px;
		font-size: 14px;
		color: #3E8EF7;
	}

	.bannerbtn a {
		flex: 1;
		text-align: center;
		color: #3E8EF7 !important;
		text-decoration: none;
	}

	.imgs {
		position: relative;
		border-radius: 6px;
		width: 148px;
		height: 148px;
		margin-right: 10px;
		display: inline-block;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 148px;
		padding-left: 58px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
	}

	.bqList {
		padding: 4px 14px;
		margin: 4px;
		border: 1px solid #efefef;
		font-size: 12px;
		color: #999;
		border-radius: 4px;
		margin-right: 15px;
	}

	.delss {
		display: none;
		position: relative;
	}

	.delss .el-icon-delete {
		position: absolute;
		top: 0;
	}

	.bqList:hover .delss {
		display: initial;
		opacity: 0.5;

	}

	.tj {
		padding: 6px !important;
		margin: 4px;
		font-size: 12px;
		border: 1px solid #ccc;
		border-radius: 4px;
	}
</style>