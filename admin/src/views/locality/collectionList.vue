<template>
	<div>
		<div style="display: inline-block;">
			<span>创建类型：</span>
			<el-select v-model="statusId" style="width:150px;margin-left: 10px;" @change="animeDat3(status)">
				<el-option v-for="item in statuss" :key="item.value" :label="item.title" :value="item.id">
				</el-option>
			</el-select>&nbsp;&nbsp;&nbsp;
			<div style="position: relative;display: inline-block;">
				<span>合同名称：</span>
				<el-input style="width: 200px;" @keydown.enter.native="phoneSelect" placeholder="请输入合同名称"
					v-model="search">
				</el-input>&nbsp;&nbsp;
			</div>
			<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="phoneSelect">查询
			</el-button>
			<el-button style="margin:10px;" size="mini" type="primary" icon="document" @click="cleans2">重置
			</el-button>&nbsp;&nbsp;
		</div>
		<div style="float: right;margin-right:2%;">
		</div>
		<el-table v-loading="tableDataLoading" :data="choicenData.records">
			<el-table-column prop="id" label="编号" width="80" fixed="left">
			</el-table-column>
			<el-table-column prop="userName" label="发起用户" fixed="left">
				<template slot-scope="scope">
					　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row.userId)"
						v-text="scope.row.userName"></span>
				</template>
			</el-table-column>
			<el-table-column prop="contractType" label="创建类型" width="100">
				<template slot-scope="scope">
					<span  v-if="scope.row.contractType === 1 ">模板创建</span>
					<span  v-if="scope.row.contractType === 2 ">微信创建</span>
					<span  v-if="scope.row.contractType === 3 ">草稿箱创建</span>
					<span  v-if="scope.row.contractType === 4 ">个人签署合同</span>
				</template>
			</el-table-column>
			<!-- <el-table-column prop="copyUserName" label="抄送人名称" width="100">
			</el-table-column>
			<el-table-column prop="signUserName" label="签署人名称" width="100">
			</el-table-column> -->
			<el-table-column prop="contractName" label="合同名称">
			</el-table-column>
			<!-- <el-table-column prop="nftTitleImg" label="封面图" width="100">
				<template slot-scope="scope">
					<img v-if="scope.row.nftTitleImg &&scope.row.nftTitleImg!=''" :src="scope.row.nftTitleImg"
						width="40" height="40" />
					<span v-else>暂无图片</span>
				</template>
			</el-table-column> -->
			<!-- <el-table-column prop="boxImg" label="轮播图" width="200">
				<template slot-scope="scope">
					<div v-for="(item,index) in scope.row.boxImg.split(',')" :key="index"
						style="display: inline-block; margin: 3px;">
						<el-popover placement="top-start" title="" trigger="hover">
							<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
							<img style="width: 300px; height: auto" :src="item" alt="">
						</el-popover>
					</div>
				</template>
			</el-table-column> -->
			<!-- <el-table-column prop="contractAndUsers" label="抄送人/签署人" width="200">
				<template slot-scope="scope">
					<div v-for="(item,index) in scope.row.contractAndUsers" :key="index"
						style="display: inline-block; margin: 3px;">
						<div>抄送人：{{item.copyUserName?item.copyUserName:'-'}}；签署人：{{item.signUserName?item.signUserName:'-'}}</div>
						
					</div>
				</template>
			</el-table-column> -->
			<el-table-column prop="contractNumber" label="合同编号">
			</el-table-column>
			<el-table-column prop="fileUrl" label="文件路径" width="300">
			</el-table-column>
			<el-table-column prop="lateSignDays" label="最晚签署天数" width="100">
			</el-table-column>
			<!-- 
			<el-table-column prop="soldCount" label="已售数量" width="100">
			</el-table-column> -->
			
			<el-table-column prop="createTime" label="创建时间" width="180">
			</el-table-column>
			<el-table-column prop="isComplete" label="是否已签署完成" width="100">
				<template slot-scope="scope">
					<span  v-if="scope.row.isComplete === 1 ">已完成</span>
					<span  v-if="scope.row.isComplete === 2 ">已审批</span>
					<span  v-if="scope.row.isComplete === 3 ">已过期</span>
					<span  v-if="scope.row.isComplete === 0 ">未完成</span>
				</template>
			</el-table-column>
			<el-table-column prop="draftStatus" label="是否加入草稿箱" width="100">
				<template slot-scope="scope">
					<span  v-if="scope.row.draftStatus === 1 ">已加入</span>
					<span  v-else>未加入</span>
				</template>
			</el-table-column>
			<el-table-column label="操作" prop="id" width="110" fixed='right'>
				<template slot-scope="scope">
					<!-- <el-button size="mini" type="primary" @click="userselect(scope.row)" style="margin: 5px;">合同数据值
					</el-button> -->
					<el-button size="mini" type="primary" @click="plCompile(scope.row)" style="margin: 5px;">发送列表
					</el-button>
					<!-- <el-button size="mini" type="primary" :disabled="!isAuth('locality:update')"
						@click="choiCompile(scope.$index, scope.row)">修改
					</el-button>

					<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
						@click="choidelete(scope.row)">删除
					</el-button> -->
				</template>
			</el-table-column>
		</el-table>
		<div style="text-align: center;margin-top: 10px;float:right">
			<el-pagination @size-change="handleSizeChange1" @current-change="handleCurrentChange1"
				:page-sizes="[10, 20, 30, 50, 100]" :page-size="size" :current-page="page"
				layout="total,sizes, prev, pager, next,jumper" :total="choicenData.total">
			</el-pagination>
		</div>
		<!-- </el-tab-pane> -->
		<!-- 添加盲盒弹框 -->
		<el-dialog :title="renwuTitles" customClass="customWidth" :close-on-click-modal="false"
			:visible.sync="dialogFormVisible5" @close="prev" center>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">盲盒名称：</span>
				<el-input v-model="boxName" style="width:65%;" placeholder="请输入盲盒名称"></el-input>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">主页图：</span>
				<div
					style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
					<el-upload class="avatar-uploader" v-model="nftTitleImg"
						:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
						:on-success="handleAvatarSuccess5">
						<img v-if="nftTitleImg!=''" :src="nftTitleImg" class="avatar"
							style="border-radius: 6px;width: 148px;height: 148px;" />
						<i v-else class="el-icon-plus avatar-uploader-icon iconss"></i>
					</el-upload>
				</div>
			</div>
			<div style="margin-bottom: 10px;display:flex;">
				<span style="width: 200px;display: inline-block;text-align: right;">轮播图：</span>
				<div class="imgs" v-for="(item,index) in boxImg" :key="index">
					<img width="100%" class="images" height="100%" :src="item" alt="">
					<span class="dels">
						<i class="el-icon-delete" @click="dels(index)"></i>
					</span>
				</div>
				<div class="imgs" style="width: 50%;">
					<el-upload :action="$http.adornUrlUp('alioss/upload')" list-type="picture-card"
						:show-file-list="false" :on-success="handleRemove" v-if="boxImg.length<5">
						<i class="el-icon-plus"></i>
					</el-upload>
					<el-dialog :visible.sync="dialogVisible">
						<img width="100%" :src="dialogImageUrl" alt="">
					</el-dialog>
				</div>
			</div>

			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">盲盒简介：</span>
				<el-input v-model="boxIntroduction" style="width:65%;" type="textarea" placeholder="请输入盲盒简介">
				</el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">单价：</span>
				<el-input v-model="price" type="number" style="width:65%;" min="0" placeholder="请输入单价"
					onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">发布人：</span>
				<el-select v-model="sUserId" style="width:65%;" placeholder="请选择发布人">
					<el-option v-for="item in userList" :key="item.userId" :label="item.userName" :value="item.userId">
					</el-option>
				</el-select>
				<!-- <el-input v-model="userName" type="text" style="width:65%;" placeholder="请选择发布人"></el-input> -->
			</div>
			<div style="margin-bottom: 10px;">
				<span style="width: 200px;display: inline-block;text-align: right;">选择资产：</span>
				<el-button type="primary" size="mini" @click="selectNftList">新增</el-button>
				<el-table :data="tableDataNft" :border="true" style="width: 65%;margin-left: 200px;margin-top: 10px;">
					<el-table-column prop="nftName" label="资产名称">
					</el-table-column>
					<el-table-column prop="nftTitleImg" label="图片">
						<template slot-scope="scope">
							<img :src="scope.row.nftTitleImg" style="width: 50px;height: 50px;" />
						</template>
					</el-table-column>
					<el-table-column prop="num" label="数量">
						<template slot-scope="scope">
							<el-input-number size="mini" :min="1" :max="scope.row.canCount" v-model="scope.row.num">
							</el-input-number>
						</template>
					</el-table-column>
					<el-table-column prop="quantityHeld" label="持有数量">
					</el-table-column>
					<el-table-column prop="canCount" label="最大上架数量">
					</el-table-column>
					<el-table-column prop="address" label="操作">
						<template slot-scope="scope">
							<el-button size="mini" type="danger" @click="deleteNft(scope.$index)">删除</el-button>
						</template>
					</el-table-column>
				</el-table>
			</div>
			<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">库存：</span>
					<el-input v-model="stock" type="number" style="width:65%;" min="0" placeholder="请输入库存"
						onkeyup="value=value.replace(/^\D*(\d*(?:\.\d{0,2})?).*$/g, '$1')"></el-input>
				</div> -->

			<div slot="footer" class="dialog-footer" style="margin-top: 30px;text-align: center;">
				<el-button @click="dialogFormVisible5 = false">取 消</el-button>
				<el-button type="primary" @click="addmissionNoticeTo()">确 定</el-button>
			</div>
		</el-dialog>
		<!-- 资产列表 -->
		<el-dialog title="合同字段" :visible.sync="dialogFormVisible3" center width="80%">
			<!-- <div style="margin:2% 0;display: inline-block;">
				<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入名称"
					v-model="nickName2"></el-input>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclick">
					查询
				</el-button>
				<el-button style='margin-left:15px;' size="mini" type="primary" icon="document" @click="userclose">
					重置
				</el-button>
			</div> -->
			<el-table v-loading="tableDataLoading4" :data="userIds">
				<el-table-column  label="编号" fixed width="80">
					<template slot-scope="scope">
						{{scope.$index+1}}
					</template>
				</el-table-column>
				<el-table-column prop="isRequired" label="是否必填" width="80">
					<template slot-scope="scope">
						<div v-if="scope.row.isRequired==1">是</div>
						<div v-else>否</div>
					</template>
				</el-table-column>
				<el-table-column prop="columnValue" label="字段名称">
				</el-table-column>
				<el-table-column prop="columnName" label="字段值" width="140">
				</el-table-column>
				<el-table-column prop="columnComment" label="字段描述">
				</el-table-column>
				<el-table-column prop="htmlType" label="字段类型" width="100">
					<template slot-scope="scope">
						<div v-if="scope.row.htmlType==1">文本框</div>
						<div v-if="scope.row.htmlType==2">文本域</div>
						<div v-if="scope.row.htmlType==3">下拉框</div>
						<div v-if="scope.row.htmlType==4">复选框</div>
						<div v-if="scope.row.htmlType==5">单选框</div>
						<div v-if="scope.row.htmlType==6">日期控件</div>
					</template>
				</el-table-column>
				
				<el-table-column label="选项值" prop="optionJson" min-width="200">
					<template slot-scope="scope">
						<div v-if="scope.row.optionJson&&scope.row.optionJson.length>0">
						<span v-for="(item,index) in scope.row.optionJson" :key="index">{{item}} ；</span>
						</div>
					</template>
				</el-table-column>
				<!-- <el-table-column label="操作" fixed='right'>
					<template slot-scope="scope">
						<el-button size="mini" type="primary" @click="confirm(scope.row)">确定
						</el-button>
					</template>
				</el-table-column> -->
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<!-- <el-pagination @size-change="handleSizeChange2" @current-change="handleCurrentChange2"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size1" :current-page="page1"
					layout="total,sizes, prev, pager, next,jumper" :total="userIds.total">
				</el-pagination> -->
			</div>
		</el-dialog>
		<!-- 发送列表 -->
		<el-dialog title="发送列表" :visible.sync="dialogFormVisible10" center width="80%">
			<el-table v-loading="tableDataLoading" :data="pinglunData">
				<el-table-column prop="id" label="编号" width="80" fixed="left">
				</el-table-column>
				<el-table-column prop="type" label="合同类型">
					<template slot-scope="scope">
						<div v-if="scope.row.type==1">企业</div>
						<div v-if="scope.row.type==2">个人</div>
					</template>
				</el-table-column>
				<el-table-column prop="isPrincipal" label="身份标识">
					<template slot-scope="scope">
						<div v-if="scope.row.isPrincipal==1">甲方</div>
						<div v-if="scope.row.isPrincipal==2">乙方</div>
					</template>
				</el-table-column>
				<el-table-column prop="userName" label="发起人人" width="140" fixed="left">
					<template slot-scope="scope">
						　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row.userId)" >{{scope.row.userName?scope.row.userName:'-'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="receivingUserName" label="抄送人" fixed="left" >
					<template slot-scope="scope">
						　<span style="color: #4f9dec;"
							v-text="scope.row.receivingUserName">{{scope.row.receivingUserName?scope.row.receivingUserName:'-'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="signUserName" label="签署/收件人" width="140" fixed="left">
					<template slot-scope="scope">
						　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row.signedBy)" >{{scope.row.signUserName?scope.row.signUserName:'-'}}</span>
					</template>
				</el-table-column>
				<el-table-column prop="receivingEmail" label="收件人邮箱" width="160">
				</el-table-column>
				<el-table-column prop="startTime" label="合同开始时间" width="140">
				</el-table-column>
				<el-table-column prop="endTime" label="合同结束时间" width="140">
				</el-table-column>
				<el-table-column prop="signUrl" label="签章/审批/公司章图片" width="200">
					<template slot-scope="scope">
						<div v-if="scope.row.signUrl">
							<div v-for="(item,index) in scope.row.signUrl.split(',')" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="signDate" label="签署日期" width="160">
				</el-table-column>
				<el-table-column prop="lastDate" label="最晚签署日期" width="160">
				</el-table-column>
				<el-table-column prop="documentsUrl" label="微信发起的合同文件" width="200">
					<template slot-scope="scope">
						<div v-if="scope.row.documentsUrl">
							<div v-for="(item,index) in scope.row.documentsUrl.split(',')" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="annexUrl" label="微信发起的附件" width="200">
					<template slot-scope="scope">
						<div v-if="scope.row.annexUrl">
							<div v-for="(item,index) in scope.row.annexUrl.split(',')" :key="index"
								style="display: inline-block; margin: 3px;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
									<img style="width: 300px; height: auto" :src="item" alt="">
								</el-popover>
							</div>
						</div>
					</template>
				</el-table-column>
				<el-table-column prop="createTime" label="创建时间" width="160">
				</el-table-column>
				<el-table-column prop="signOrApproved" label="签署类型" fixed='right'>
						<template slot-scope="scope">
							<div v-if="scope.row.signOrApproved==0" style="color:#ff4949;">签署</div>
							<div v-if="scope.row.signOrApproved==1" style="color:#4f9dec;">审批</div>
							<div v-if="scope.row.signOrApproved==2" style="color:#13ce66;">不签署</div>
						</template>
					</el-table-column>
				<el-table-column prop="status" label="状态" fixed='right'>
						<template slot-scope="scope">
							<div v-if="scope.row.status===0" style="color:#ff4949;">未签署</div>
							<div v-if="scope.row.status==1" style="color:#ff4949;">未审批</div>
							<div v-if="scope.row.status==2" style="color:#4f9dec;">已签署</div>
							<div v-if="scope.row.status==3" style="color:#13ce66;">已审批</div>
							<div v-if="scope.row.status==4">已过期</div>
						</template>
					</el-table-column>
				<!-- <el-table-column label="操作" prop="id" width="100" fixed='right' align="center">
					<template slot-scope="scope">
						<el-button size="mini" type="danger" :disabled="!isAuth('locality:delete')"
							@click="pinglundelete(scope.row)">删除
						</el-button>
					</template>
				</el-table-column> -->
			</el-table>
			<div style="text-align: center;margin-top: 10px;">
				<!-- <el-pagination @size-change="handleSizeChange3" @current-change="handleCurrentChange3"
					:page-sizes="[10, 20, 30, 50, 100]" :page-size="size2" :current-page="page2"
					layout="total,sizes, prev, pager, next,jumper" :total="pinglunData.total">
				</el-pagination> -->
			</div>
		</el-dialog>
		<!-- </el-tabs> -->
	</div>
</template>

<script>
	import {
		quillEditor
	} from 'vue-quill-editor'
	import 'quill/dist/quill.core.css'
	import 'quill/dist/quill.snow.css'
	import 'quill/dist/quill.bubble.css'
	import quillConfig from './quill-config.js'
	import axios from 'axios';
	import {
		jsonp
	} from 'vue-jsonp'
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	var cityOptions = []
	var geocoder, map, markersArray = [];
	export default {
		components: {
			quillEditor
		},
		data() {
			return {
				sUserId: '',
				userList: [],
				tableDataNft: [],
				size: 10,
				page: 1,
				size1: 10,
				page1: 1,
				size2: 10,
				page2: 1,
				state: '',
				limit: 10,
				openValue: 1,
				closeValue: 0,

				title: '',
				type: '',
				name: '',
				method: 'false',
				tableDataLoading: true,
				tableDataLoading4: false,
				dialogFormVisible3: false,
				dialogFormVisible5: false,
				dialogFormVisible10: false,
				dialogImageUrl: [],
				choicenData: {},
				pinglunData: [],
				boxIds: '',
				url: '',
				id: '',
				userId: '',
				search: '',
				classifyIds: '',
				statusId: '',
				statuss: [{
						id: '',
						title: '全部'
					},
					{
						id: 1,
						title: '模板创建'
					},
					{
						id: 2,
						title: '微信创建'
					},
					{
						id: 3,
						title: '草稿箱创建'
					},
					{
						id: 4,
						title: '个人签署合同'
					},
				],
				// 盲盒信息
				boxName: '', //名称
				boxImg: [], //轮播图
				nftTitleImg: '', //封面图
				userIdss: '', //创造人id
				boxIntroduction: '', //盲盒简介
				price: '', //单价
				stock: '', //库存
				status: '', //状态0待审核  1上架  2下架 3驳回 (修改添加不传)
				phone1: '',
				mangBoxId: '', //盲盒id
				nickName2: '',
				userIds: {}, //用户列表
				dialogVisible: false,
				renwuTitles: '添加藏品',
			}
		},
		methods: {
			//选择资产
			selectNftList() {
				if (this.sUserId === '') {
					this.$message({
						message: '请选择发布人后继续操作',
						type: 'warning'
					});
				} else {
					this.userClass();
					this.dialogFormVisible3 = true
				}

			},
			//获取发布管理员
			getUserList() {
				this.$http({
					url: this.$http.adornUrl('user/getAdminUser'),
					method: 'get',
					params: this.$http.adornParams({
						'page': 1,
						'limit': 9999,
						'typeUser': 1
					})
				}).then(({
					data
				}) => {
					this.userList = data.data.records
				})
			},
			//删除添加的资产
			deleteNft(index) {
				this.tableDataNft.splice(index, 1)
			},
			handleSizeChange1(val) {
				this.limit = val
				this.InformationSelect()
			},
			handleCurrentChange1(val) {
				this.page = val
				this.InformationSelect()
			},
			handleSizeChange2(val) {
				this.size1 = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.page1 = val
				this.userClass()
			},
			handleSizeChange3(val) {
				this.size2 = val
				this.pinglunSelect(this.boxIds)
			},
			handleCurrentChange3(val) {
				this.page2 = val
				this.pinglunSelect(this.boxIds)
			},
			//添加盲盒
			choiaddNotice() {
				this.sUserId = '';
				this.renwuTitles = '添加盲盒'
				this.dialogFormVisible5 = true
				this.boxName = ''
				this.boxImg = []
				this.nftTitleImg = ''
				this.boxIntroduction = ''
				this.price = ''
				this.stock = ''
				this.value1 = ''
				this.value2 = ''
				this.userIdss = ''
				this.nickName2 = ''
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 重置
			cleans2() {
				this.statusId = ''
				this.search = ''
				this.page = 1
				this.InformationSelect()
			},
			// 盲盒数据
			InformationSelect() {
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl('admin/contract/getAllContractList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'contractName': this.search,
						'contractType': this.statusId,
						'userName':'',
						'signUserName':'',
						'copyUserName':'',
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					// for (var i in data.data.list) {
					// 	data.data.list[i].boxImgs = data.data.list[i].boxImg.split(',')
					// }
					let returnData = data.data
					this.choicenData = returnData
				})
			},
			// 封面图片上传
			handleAvatarSuccess5(file) {
				this.nftTitleImg = file.data;
			},
			// 详情图片上传
			handleRemove(file, ) {
				this.boxImg.push(file.data);
			},
			// 删除详情图
			dels(index) {
				console.log(this.boxImg)
				this.boxImg.splice(index, 1);
			},
			// 获取用户列表弹框
			userselect(row) {
				this.userIds = row.dataJson
				this.dialogFormVisible3 = true
				// this.tableDataLoading4 = true
				// this.userClass()
			},
			// 获取资产列表
			userClass() {
				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl('admin/nftUserAssets/getAssetsList'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': this.sUserId,
						'page': this.page1,
						'limit': this.size1,
						'nftName':this.nickName2
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
			// 选择资产
			confirm(row) {
				//选择资产如果已经存在则添加数量否则直接添加
				let ind = -1
				this.tableDataNft.map((item, index) => {
					if (item.assetsId == row.assetsId) {
						ind = index
					}
				})
				if (ind != -1) {
					//存在则只增加数量
					this.tableDataNft[ind].num += 1
				} else {
					row.num = 1
					//解决table中input无法输入的问题
					this.tableDataNft.push(JSON.parse(JSON.stringify(row)))
				}
				this.dialogFormVisible3 = false
			},
			// 确定添加盲盒/修改
			addmissionNoticeTo() {
				if (this.boxName == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入盲盒名称',
						type: 'warning'
					});
					return
				}
				if (this.nftTitleImg == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传藏品封面图',
						type: 'warning'
					});
					return
				}
				if (this.boxImg.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请上传藏品轮播图',
						type: 'warning'
					});
					return
				}
				if (this.price == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入单价',
						type: 'warning'
					});
					return
				}

				if ((this.sUserId + '') == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择发布人',
						type: 'warning'
					});
					return
				}
				if (this.tableDataNft.length == 0) {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择资产',
						type: 'warning'
					});
					return
				}
				let urls = 'admin/nftBox/addBox'
				this.$http({
					url: this.$http.adornUrl(urls),
					method: 'post',
					data: this.$http.adornData({
						'boxName': this.boxName,
						'boxIntroduction': this.boxIntroduction,
						'price': this.price,
						'boxImg': this.boxImg.toString(),
						'nftTitleImg': this.nftTitleImg,
						'originalId': this.sUserId,
						'boxId': this.mangBoxId,
						'nftBoxRelationList': this.tableDataNft
					})
				}).then(({
					data
				}) => {
					console.log('提交结果', data)
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible5 = false
								this.InformationSelect()
							}
						})
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
			},
			// 关闭添加信息弹框
			prev() {
				this.boxName = ''
				this.boxImg = []
				this.nftTitleImg = ''
				this.boxIntroduction = ''
				this.price = ''
				this.stock = ''
				this.userIdss = ''
				this.nickName2 = ''
				this.sUserId = ''
				this.tableDataNft = []
			},
			// 修改盲盒
			choiCompile(index, row) {
				//查询盲盒详情
				this.$http({
					url: this.$http.adornUrl(`admin/nftBox/selectBoxInfo`),
					method: 'get',
					params: this.$http.adornParams({
						'boxId':row.boxId
					})
				}).then(({
					data
				}) => {
					if (data && data.code == 0) {
						//盲盒修改回显
						let obj = data.data
						this.renwuTitles = '修改盲盒'
						if (row) {
							this.boxName = obj.boxName
							this.boxImg = obj.boxImg.split(',')
							this.nftTitleImg = obj.nftTitleImg
							this.boxIntroduction = obj.boxIntroduction
							this.price = obj.price
							this.stock = obj.stock
							this.mangBoxId = obj.boxId
							this.sUserId = obj.originalId
							this.status = Number(obj.status)
							this.tableDataNft = obj.nftBoxRelationList
						}
						this.dialogFormVisible5 = true
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
						})
					}

				})

			},
			// 查看盲盒内资产
			plCompile(row) {
				console.log(row)
				this.pinglunData = row.contractAndUsers
				this.dialogFormVisible10 = true
				// this.pinglunSelect(row.boxId)
			},
			// 删除盲盒
			choidelete(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`admin/nftBox/deleteBox?boxId=${row.boxId}`),
						method: 'get',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if(data && data.code==0){
							this.$message({
								message: '删除成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.InformationSelect()
								}
							})
						}else{
							this.$message({
								message: data.msg,
								type: 'error',
								duration: 1500,
							})
						}

					})
				}).catch(() => {})
			},
			// 筛选信息
			animeDat2(state) {
				this.InformationSelect()
			},
			// 筛选信息
			animeDat4(state) {
				this.InformationSelect()
			},
			animeDat3(state) {
				this.InformationSelect()
			},
			// 盲盒上下架
			change3(val, row) {
				this.$http({
					url: this.$http.adornUrl('admin/nftBox/updateBox'),
					method: 'post',
					params: this.$http.adornParams({
						'status': val,
						'boxId': row.boxId
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
								this.InformationSelect()
							}
						})
					} else {
						this.$notify({
							title: '提示',
							duration: 1800,
							message: data.msg,
							type: 'warning'
						});
					}
				})
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
			// 盲盒资产列表
			pinglunSelect(id) {
				this.userId = this.$store.state.user.id
				this.$http({
					url: this.$http.adornUrl('admin/nftBox/getNftByBoxId'),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.page,
						// 'limit': this.limit,
						'boxId': id
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.pinglunData = returnData
				})
			},
			// 删除盲盒资产
			pinglundelete(row) {
				let delid = row.id
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/nftBoxRelation/deleteBoxAssets'),
						method: 'get',
						params: this.$http.adornParams({
							'relationId': row.relationId
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
									this.pinglunSelect(this.boxIds)
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
		},
		mounted() {
			this.InformationSelect()
			this.getUserList();
		}
	}
</script>

<style>
	.customWidth {
		width: 80% !important;
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
