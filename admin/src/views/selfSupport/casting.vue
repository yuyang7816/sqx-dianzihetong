<template>
	<div>
		<div style="margin: 10px; display: inline-block;">
			<span>状态：</span>
			<el-select v-model="statusId" style="width:150px;margin-left: 10px;" @change="animeDat(statusId)">
				<el-option v-for="item in statesnum" :key="item.value" :label="item.label" :value="item.value">
				</el-option>
			</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
		</div>
		<div style="margin: 10px; display: inline-block;">
			<span>铸造人昵称：</span>
			<el-input style="width:150px;" @keydown.enter.native="selectW" v-model="search" placeholder="请输入铸造人昵称">
			</el-input>
		</div>
		<div style="margin: 10px; display: inline-block;">
			<span>NFT名称：</span>
			<el-input style="width:150px;" @keydown.enter.native="selectW" v-model="nftName" placeholder="请输入NFT名称">
			</el-input>
		</div>
		<div style="margin:10px;display: inline-block;">
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="selectW">查询
			</el-button>
			<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleansW">重置
			</el-button>
			<el-button style='margin-left:15px;' size="mini" :disabled="!isAuth('materialsList:add')" type="primary"
				icon="document" @click="addNoticewz(1)">管理员铸造</el-button>
			<el-button style='margin-left:15px;' size="mini" :disabled="!isAuth('materialsList:add')" type="danger"
				icon="document" @click="addNoticewz(2)">协助铸造</el-button>
		</div>
		<el-table v-loading="tableDataLoading" :data="tableData.records">
			<el-table-column prop="nftId" label="编号" width="80" fixed="left">
			</el-table-column>
			<el-table-column prop="originalName" label="铸造人昵称">
				<template slot-scope="scope">
					　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row.originalId)"
						v-text="scope.row.originalName"></span>
				</template>
			</el-table-column>
			<el-table-column prop="nftName" label="NFT名称" fixed="left">
			</el-table-column>
			<!-- <el-table-column prop="classifyId" label="分类">
				<template slot-scope="scope">
					<div v-if="scope.row.saleType==0">合集</div>
					<div v-if="scope.row.saleType==1">作品</div>
				</template>
			</el-table-column> -->
			<el-table-column prop="nftImg" label="封面图" width="100">
				<template slot-scope="scope">
					　　
					<img v-if="scope.row.nftTitleImg &&scope.row.nftTitleImg!=''" :src="scope.row.nftTitleImg"
						width="40" height="40" />
					<span v-else>暂无图片</span>

				</template>
			</el-table-column>
			<el-table-column prop="nftImg" label="轮播图" width="200">
				<template slot-scope="scope">
					<div v-for="(item,index) in scope.row.nftImg.split(',')" :key="index"
						style="display: inline-block; margin: 3px;">
						<el-popover placement="top-start" title="" trigger="hover">
							<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
							<img style="width: 300px; height: auto" :src="item" alt="">
						</el-popover>
					</div>
				</template>
			</el-table-column>
			<el-table-column prop="nftDetails" label="NFT简介" width="140">
			</el-table-column>
			<el-table-column prop="contractAddress" label="NFT地址" width="140">
			</el-table-column>
			<el-table-column prop="authNetwork" label="网络认证" width="140">
			</el-table-column>
			<el-table-column prop="nftAgreement" label="NFT标准" width="140">
			</el-table-column>
			<el-table-column prop="rights" label="权益类型">
				<template slot-scope="scope">
					<div v-if="scope.row.rights==1">版权品</div>
					<div v-if="scope.row.rights==2">衍生品</div>
				</template>
			</el-table-column>
			<el-table-column prop="nftRate" label="版权费" width="140">
			</el-table-column>
			<el-table-column prop="quantity" label="铸造数量" width="160">
			</el-table-column>
			<el-table-column prop="gasMoney" label="gas总费" width="80">
			</el-table-column>
			<el-table-column prop="createUserFollowCount" label="铸造人粉丝数" width="80">
			</el-table-column>
			<el-table-column prop="examineReason" label="审核说明" width="80">
			</el-table-column>
			<el-table-column prop="status" label="状态" fixed="right">
				<template slot-scope="scope">
					<div v-if="scope.row.status==1">待审核</div>
					<div v-if="scope.row.status==2">已通过</div>
					<div v-if="scope.row.status==3">已拒绝</div>
					<div v-if="scope.row.status==4">待付款</div>
				</template>
			</el-table-column>
			<el-table-column prop="endTime" label="结束时间" width="160">
			</el-table-column>
			<el-table-column prop="updateTime" label="更新时间" width="140">
			</el-table-column>
			<el-table-column prop="createTime" label="创建时间" width="160">
			</el-table-column>
			<el-table-column label="操作" width="100" fixed="right">
				<template slot-scope="scope">
					<el-button size="mini" type="primary" @click="shenhe(scope.row)" :disabled="scope.row.status!=1"
						style="margin: 5px;">审核
					</el-button>
					<!-- <el-button size="mini" type="primary" :disabled="!isAuth('materialsList:update')"
						@click="updateswz(scope.$index, scope.row)" style="margin: 5px;">编辑
					</el-button>
					<el-button size="mini" type="danger" :disabled="!isAuth('materialsList:delete')"
						@click="deleteswz(scope.row)" style="margin: 5px;">删除
					</el-button> -->
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;">
			<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
			</el-pagination>
		</div>
		<!-- 添加弹框 -->
		<el-dialog title="添加" :visible.sync="dialogFormVisible2" center width="80%">
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">创造人：</span>
				<el-input v-model="nickName1" :readonly="true" @click.native="userselect" style="width:80%;"
					placeholder="请选择创造人">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">分类：</span>
				<el-radio-group v-model="classifyId" style="display: inline-block;">
					<!-- <el-radio :label="0">合集</el-radio> -->
					<el-radio :label="1">作品</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">权益类型：</span>
				<el-radio-group v-model="rights" style="display: inline-block;">
					<el-radio :label="1">版权品</el-radio>
					<el-radio :label="2">衍生品</el-radio>
				</el-radio-group>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">作品名称：</span>
				<el-input style="width: 80%;" v-model="nftNameL" placeholder="请输入作品名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 100px;display: inline-block;text-align: right;">主页图：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="nftTitleImg"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess5" :on-progress="onprogress">
						<img v-if="nftTitleImg!=''&&percentage==100" :src="nftTitleImg" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
						<el-progress v-if="percentage>0&&percentage<100" type="circle" :percentage="percentage">
						</el-progress>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 100px;display: inline-block;text-align: right;">轮播图：</span>
				<div class="imgs" v-for="(item,index) in nftImg" :key="index">
					<img width="100%" class="images" height="100%" :src="item" alt="">
					<span class="dels">
						<i class="el-icon-delete" @click="dels(index)"></i>
					</span>
				</div>
				<div class="imgs" style="width: 50%;">
					<el-upload :action="$http.adornUrlUp('alioss/upload')" list-type="picture-card"
						:show-file-list="false" :on-success="handleRemove" v-if="nftImg.length<5">
						<i class="el-icon-plus"></i>
					</el-upload>
					<el-dialog :visible.sync="dialogVisible">
						<img width="100%" :src="dialogImageUrl" alt="">
					</el-dialog>
				</div>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">版税(%)：</span>
				<el-input-number v-model="nftRate" placeholder="请输入版税" controls-position="right" :min="1" :max="100"></el-input-number>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">铸造数量：</span>
				<!-- <el-input style="width: 80%;" v-model="quantity" placeholder="请输入铸造数量"></el-input> -->
				<el-input-number v-model="quantity" :min="1"  label="请输入铸造数量"></el-input-number>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 100px;display: inline-block;text-align: right;">作品简介：</span>
				<el-input style="width: 80%;" v-model="nftDetails" type="textarea" :rows="3" placeholder="请输入作品简介">
				</el-input>
				<!-- <quill-editor ref="myTextEditor" v-model="nftDetails" :options="quillOption"
					style="padding-bottom: 50px;height: 500px;width: 80%;display: inline-table;margin-bottom: 60px;">
				</quill-editor> -->
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible2 = false">取 消</el-button>
				<el-button type="primary" @click="releasNoticeTowz()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 服务审核弹框 -->
		<el-dialog title="审核" :visible.sync="dialogFormVisible8" center>
			<el-radio-group v-model="radio" style="width: 100%;margin-bottom: 30px;margin-left: 200px;">
				<el-radio :label="1">通过</el-radio>
				<el-radio :label="2">驳回</el-radio>
			</el-radio-group>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">审核理由：</span>
				<el-input style="width:50%;" v-model="contents" type="text" placeholder="请输入审核理由"></el-input>
			</div>
			<div slot="footer" class="dialog-footer">
				<el-button @click="dialogFormVisible8 = false">取 消</el-button>
				<el-button type="primary" @click="refuseto()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 用户列表 -->
		<el-dialog title="用户列表" :visible.sync="dialogFormVisible4" center width="60%">
			<div style="margin:2% 0;display: inline-block;">
				<el-select :disabled="true" v-model="userTypes" @change="userclick" placeholder="请选择用户类型">
					<el-option v-for="item in userType" :key="item.value" :label="item.label" :value="item.value">
					</el-option>
				</el-select>&nbsp;&nbsp;&nbsp;&nbsp;
				<!-- <el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
					v-model="phone1"></el-input> -->
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入昵称"
					v-model="nickName2"></el-input>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
					重置
				</el-button>
			</div>
			<el-table width="780px" v-loading="tableDataLoading4" :data="userIds.records">
				<el-table-column prop="userId" width="80" label="用户Id" fixed>
				</el-table-column>
				<el-table-column fixed prop="userName" label="用户昵称">
				</el-table-column>
				<el-table-column fixed prop="typeUser" label="用户类型">
					<template slot-scope="scope">
						<span>{{scope.row.typeUser==1?'平台用户':'普通用户'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="phone" label="手机号">
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间">
				</el-table-column>
				<el-table-column label="操作" fixed='right' width="120">
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
						</el-button>
					</template>
				</el-table-column>
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.total">
				</el-pagination>
			</div>
		</el-dialog>
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from '../mission/quill-config.js'
	export default {
		name: 'news',
		components: {
			quillEditor
		},
		data() {
			return {
				userTypes: '',
				userType: [{
						value: '',
						label: '全部用户'
					},
					{
						value: 1,
						label: '平台用户'
					},
					{
						value: 2,
						label: '普通用户'
					}
				],
				userList: [],
				openValue: 2,
				closeValue: 4,
				limit: 10,
				page: 1,
				size1: 10,
				page1: 1,
				state: 1,
				sort: 0,
				remark: '',
				createTime: '',
				helpClassifyName: '',
				helpClassifyId: '',
				classifyId: 1,
				classifyIds: [],

				helpWordContent: '',
				classifyIdFl: '',
				formwz: {
					helpWordId: '',
					picture: '',
					articleUrl: '',
					helpWordContent: '',
					helpWordTitle: '',
					helpClassifyId: '',
					createTime: '',
					sort: 0,
				},
				info: {
					stockDate: this.getNowTime(), //日期
				},
				picture: '',
				picture2: '',
				articleUrl: '',

				title: '',
				formLabelWidth: '200px',
				activeName: 'first',
				tableDataLoading: false,
				tableDataLoading4: false,
				dialogFormVisible1: false,
				dialogFormVisible: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				tableData: {},
				tableDataF: {},
				parentIdnum: '',
				helpClassifyNameT: '',

				value: [],
				quillOption: quillConfig,
				helpWordTitleT: '',
				dialogFormVisible8: false,
				radio: 1,
				contents: '',
				shenheId: '',
				nftName: '',
				search: '',
				nftTitleImg: '',
				nftImg: [],
				percentage: 0, //进度条
				percentage2: 0, //进度条
				nftNameL: '',
				nftDetails: '',
				classifyId: 1,
				rights: 1,
				originalId: 0,
				nftRate: '',
				quantity: '',
				phone1: '',
				nickName1: '',
				nickName2: '',
				userIds: {},
				userIdss: '',
				statusId: '',
				statesnum: [{
						value: '',
						label: '全部'
					},
					{
						value: 1,
						label: '待审核'
					},
					{
						value: 2,
						label: '已通过'
					},
					{
						value: 3,
						label: '已拒绝'
					},
					{
						value: 4,
						label: '待付款'
					},
				],
				dialogVisible: false,
				dialogImageUrl: '',
			}
		},
		methods: {
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
				var defaultDate = `${year}-${month}-${date} ${hh}:${mm}:${ss}`;
				return defaultDate;
				this.$set(this.info, "stockDate", defaultDate);
			},
			// 详情跳转
			updatesvideo(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			handleClick(tab, event) {
				this.page = 1
				if (tab._props.label == '分类管理') {}
			},
			handleSizeChange(val) {
				this.limit = val;
				this.articleSelect()
			},
			handleCurrentChange(val) {
				this.page = val;
				this.articleSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val;
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val;
				this.userClass()
			},
			handleAvatarSuccess(file) {
				this.picture = file.data;
			},
			handleAvatarSuccess2(file2) {
				this.formwz.picture = file2.data;
			},
			onprogress(event, file, fileList) {
				console.log('event, file, fileList', parseInt(event.percent))
				this.percentage = parseInt(event.percent)
			},
			// 封面图片上传
			handleAvatarSuccess5(file) {
				this.nftTitleImg = file.data;
			},
			// 详情图片上传
			handleRemove(file, ) {
				this.nftImg.push(file.data);
			},
			// 删除详情图
			dels(index) {
				console.log(this.nftImg)
				this.nftImg.splice(index, 1);

			},

			// 添加分类弹框
			addNotice() {
				this.helpClassifyName = ''
				this.sort = 0
				this.dialogFormVisible = true
			},
			// 添加分类
			releasNoticeTo() {
				if (this.helpClassifyName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择分类名称',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('help/insertHelpClassify'),
					method: 'post',
					data: this.$http.adornData({
						'state': this.state,
						'sort': this.sort,
						'remark': this.remark,
						'helpClassifyName': this.helpClassifyName,
						'createTime': this.info.stockDate,
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
			// 添加文章弹框
			addNoticewz(type) { //1:管理员铸造  2:协助铸造
				if (type == 1) {
					this.userTypes = 1
					this.nftTitleImg = ''
					this.nftImg = []
					this.nftNameL = ''
					this.nftDetails = ''
					this.classifyId = 1
					this.rights = 1
					this.nftRate = ''
					this.quantity = ''
					this.originalId = 0
					this.userIdss = ''
					this.nickName1 = ''
					this.dialogFormVisible2 = true
				} else {
					this.userTypes = 2
					this.$confirm('此操作为危险操作, 是否继续?', '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.nftTitleImg = ''
						this.nftImg = []
						this.nftNameL = ''
						this.nftDetails = ''
						this.classifyId = 1
						this.rights = 1
						this.nftRate = ''
						this.quantity = ''
						this.originalId = 0
						this.userIdss = ''
						this.nickName1 = ''
						this.dialogFormVisible2 = true
					}).catch(() => {
						this.$message({
							type: 'info',
							message: '已取消操作'
						});
					});
				}

			},
			// 添加文章
			releasNoticeTowz() {
				if (this.userIdss === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择创造人',
						type: 'warning'
					});
					return
				}
				if (this.nftNameL == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入作品名称',
						type: 'warning'
					});
					return
				}
				if (this.nftTitleImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传作品封面图',
						type: 'warning'
					});
					return
				}
				if (this.nftImg.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传作品内容图',
						type: 'warning'
					});
					return
				}
				if (this.nftRate == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入版税',
						type: 'warning'
					});
					return
				}
				if (this.quantity == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入铸造数量',
						type: 'warning'
					});
					return
				}
				if (this.nftDetails == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入作品详情介绍',
						type: 'warning'
					});
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/adminCastingNft'),
					method: 'post',
					params: this.$http.adornParams({
						'nftTitleImg': this.nftTitleImg,
						'nftImg': this.nftImg.toString(),
						'nftName': this.nftNameL,
						'nftDetails': this.nftDetails,
						'classifyId	': this.classifyId,
						'rights': this.rights,
						'nftRate': this.nftRate,
						'quantity': this.quantity,
						'originalId': this.userIdss
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible2 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.articleSelect()
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
			// 编辑文章弹框
			updateswz(index, rows) {
				this.formwz.picture = rows.picture;
				this.dialogFormVisible3 = true;
				this.formwz.helpWordId = rows.helpWordId;
				this.formwz.helpClassifyId = rows.helpClassifyId
				this.formwz.sort = rows.sort;
				this.formwz.helpWordContent = rows.helpWordContent;
				this.formwz.helpWordTitle = rows.helpWordTitle;
				this.formwz.createTime = rows.createTime;
			},
			// 修改文章
			amendNoticeTowz() {
				this.$http({
					url: this.$http.adornUrl('help/updateHelpWord'),
					method: 'post',
					params: this.$http.adornParams({
						'helpWordId': this.formwz.helpWordId,
						'helpWordContent': this.formwz.helpWordContent,
						'helpClassifyId': this.formwz.helpClassifyId,
						'helpWordTitle': this.formwz.helpWordTitle,
						'createTime': this.info.stockDate,
						'sort': this.formwz.sort
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.dialogFormVisible3 = false
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.articleSelect()
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
			// 删除文章
			deleteswz(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('help/deleteHelpWord'),
						method: 'post',
						params: this.$http.adornParams({
							'helpWordId': row.helpWordId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.name = '';
									this.articleSelect()
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
				}).catch(() => {})
			},
			// 获取分类管理数据
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('help/selectHelpClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'helpClassifyName': this.helpClassifyNameT
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableDataF = returnData
				})
			},

			// 获取铸造列表数据
			articleSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/getNftList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'originalName': this.search,
						'nftName': this.nftName,
						'status': this.statusId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tableData = returnData
				})
			},
			handleChange(value) {
				this.value.forEach(element => {
					this.parentId = element
				})
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},

			// 查询
			selectW() {
				this.page = 1
				this.articleSelect()
			},
			// 重置
			cleansW() {
				this.search = ''
				this.nftName = ''
				this.statusId = ''
				this.page = 1
				this.articleSelect()
			},
			manghe(row) {},
			// 上下架
			change3(val, row) {
				console.log('上下架')
				this.$http({
					url: this.$http.adornUrl('admin/nftBranch/shelfNft'),
					method: 'post',
					params: this.$http.adornParams({
						'branchId': row.branchId
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
								this.articleSelect()
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
			// 审核
			shenhe(row) {
				this.shenheId = row.nftId
				this.radio = 1
				this.content = ''
				this.dialogFormVisible8 = true
			},
			// 提交审核
			refuseto(row) {

				if (this.radio == 2 && this.contents == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入驳回理由',
						type: 'warning'
					})
					return
				}
				let ids = this.shenheId
				let status = this.radio
				let content = this.contents
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/examine'),
					method: 'get',
					params: this.$http.adornParams({
						'nftId': ids,
						'status': status,
						'examineReason': content
					})
				}).then(({
					data
				}) => {
					this.dialogFormVisible8 = false
					this.$message({
						message: '审核完成',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.radio = 1
							this.contents = ''
							this.articleSelect()
						}
					})
				})
			},
			// 获取用户列表弹框
			userselect() {
				this.dialogFormVisible4 = true
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 获取用户列表
			userClass() {
				this.$http({
					url: this.$http.adornUrl('user/getAdminUser'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.size1,
						'typeUser': this.userTypes,
						'userName': this.nickName2
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading4 = false
					let returnData = data.data
					this.userIds = returnData
				})
			},
			// 查询用户列表
			userclick() {
				this.page = 1
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.phone1 = ''
				this.nickName2 = ''
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 确定用户
			confirm(row) {
				this.userIdss = row.userId
				this.nickName1 = row.userName
				this.phone = row.phone
				this.dialogFormVisible4 = false
			},
			// select选择事件
			animeDat(state) {
				this.page = 1
				this.articleSelect()
			},
		},
		mounted() {
			this.articleSelect()
		}
	};
</script>

<style>
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
</style>
