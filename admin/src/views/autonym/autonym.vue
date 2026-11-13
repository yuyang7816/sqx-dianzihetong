<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="合同套餐列表" name="first">
				<!-- <div style="display: inline-block;margin:5px">
					<span>状态：</span>
					<el-select v-model="status" style="width:150px;margin-left: 10px;" @change="select()">
						<el-option v-for="(item,index) in statesnum" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;margin:5px">
					<span>排序：</span>
					<el-select v-model="screen" style="width:150px;margin-left: 10px;" @change="select()">
						<el-option v-for="(item,index) in statesnum2" :key="index" :label="item.label"
							:value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;margin:5px">
					<span>分类：</span>
					<el-select v-model="classIdT" style="width:150px;margin-left: 10px;" @change="select()">
						<el-option v-for="(item,index) in classList" :key="index" :label="item.className"
							:value="item.classId">
						</el-option>
					</el-select>&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;margin:5px">
					<span>用户昵称：</span>
					<el-input v-model="userNameT" placeholder="请输入用户昵称" style="width: 200px;"
						@keydown.enter.native="select">
					</el-input>&nbsp;&nbsp;
				</div>
				<div style="display: inline-block;margin:5px">
					<span>手机号：</span>
					<el-input v-model="phoneT" placeholder="请输入手机号" style="width: 200px;"
						@keydown.enter.native="select">
					</el-input>&nbsp;&nbsp;
				</div> -->
				<div style="display: inline-block;margin:5px">
					<span>套餐名称：</span>
					<el-input v-model="dataTitleT" placeholder="请输入套餐名称" style="width: 200px;"
						@keydown.enter.native="select">
					</el-input>&nbsp;&nbsp;
				</div>
				<!-- <div style="margin:5px;display: inline-block;">
					<span>城市:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入城市"
						v-model="cityT"></el-input>
				</div> -->
				<div style="display: inline-block;margin:5px">
					<el-button icon="document" size="mini" style='margin-left:15px;' type="primary" @click="select">查询
					</el-button>
					<el-button icon="document" size="mini" style='margin-left:15px;' type="primary" @click="cleans">重置
					</el-button>
					<el-button icon="document" size="mini" style='margin-left:15px;' type="primary"
						:disabled="!isAuth('autonym:add')" @click="addBtn(1)">添加
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.records">
					<el-table-column label="编号" prop="payClassifyId" width="80" fixed="left">
					</el-table-column>
					<!-- <el-table-column label="推荐状态" prop="createTime" fixed="left">
						<template slot-scope="scope">
							<el-switch :disabled="scope.row.status===3" v-model="scope.row.isRecommend"
								active-color="#13ce66" inactive-color="#ff4949" :active-value="1" inactive-value="0"
								@change="change(e,scope.row.dataId)">
							</el-switch>
						</template>
					</el-table-column> -->
					<el-table-column label="套餐名称" prop="executed">
						<!-- <template slot-scope="scope">
							<span @click="updates(scope.row.userId)">{{
									scope.row.dataTitle ? scope.row.dataTitle : '暂无'
								}}</span>
						</template> -->
					</el-table-column>
					<!-- <el-table-column label="用户昵称" prop="realName">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">{{
									scope.row.userName ? scope.row.userName : '未绑定'
								}}</span>
						</template>
					</el-table-column>
					<el-table-column label="手机号" prop="phone">
						<template slot-scope="scope">
							<span v-if="scope.row.phone">{{ scope.row.phone }}</span>
						</template>
					</el-table-column>
					<el-table-column label="地址" prop="province">
						<template slot-scope="scope">
							<span>{{ scope.row.province + '' + scope.row.city + '' + scope.row.county }}</span>
						</template>
					</el-table-column>
					<el-table-column label="封面图" prop="cardFront" width="100">
						<template slot-scope="scope">
							<div v-if="scope.row.titleImg == null || scope.row.titleImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.titleImg" style="display:flex;flex-wrap: wrap;">
								<el-popover placement="top-start" title="" trigger="hover">
									<img slot="reference" :src="scope.row.titleImg" alt=""
										style="width: 50px; height: 50px">
									<img :src="scope.row.titleImg" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="轮播图" prop="cardFront" width="230">
						<template slot-scope="scope">
							<div v-if="scope.row.rotationImg == null || scope.row.rotationImg == ''">
								暂无图片
							</div>
							<div v-if="scope.row.titleImg">
								<el-popover v-for="(item,index) in scope.row.rotationImg.split(',')" :key="index"
									placement="top-start" style="margin-right: 5px" title="" trigger="hover">
									<img slot="reference" :src="item" alt=""
										style="width: 50px; height: 50px;margin: 3px;">
									<img :src="item" alt="" style="width: 200px; height: 200px">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column label="服务详情" prop="dataJson" width="300">
						<template slot-scope="scope">
							<div v-if="scope.row.dataJson.length>0">
								<el-popover placement="top-start" title="" trigger="hover">
									<div slot="reference"
										style="display: -webkit-box;-webkit-box-orient: vertical;-webkit-line-clamp: 3;overflow: hidden;max-height:80px;">
										<div v-for="(item,index) in scope.row.dataJson" :key="index">
											<div style="margin: 3px 0;">
												{{item.columnComment}}:{{item.columnValue}}
											</div>

										</div>
									</div>
									<div style="margin: 3px 0;width: 400px;">
										<div v-for="(item,index) in scope.row.dataJson" :key="index">
											<div style="margin: 3px 0;">
												{{item.columnComment}}:{{item.columnValue}}
											</div>
										</div>
									</div>
								</el-popover>
							</div>
						</template>
					</el-table-column> -->
					<el-table-column label="份数" prop="numberSign">
					</el-table-column>
					<el-table-column label="排序" prop="sort">
					</el-table-column>
					<!-- <el-table-column label="拒绝原因" prop="refuseReason">
						<template slot-scope="scope">
							<span>{{scope.row.refuseReason?scope.row.refuseReason:'暂无'}}</span>
						</template>
					</el-table-column> -->
					<el-table-column label="创建时间" prop="createTime">
					</el-table-column>
					<!-- <el-table-column label="VIP是否到期" prop="createTime">
						<template slot-scope="scope">
							<span>{{scope.row.state==2?'是':'否'}}</span>
						</template>
					</el-table-column>
					<el-table-column label="是否上下架" prop="createTime"  fixed="right">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isEnable" @change="changeTj(scope.row.isEnable,scope.row)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed='right' label="状态" prop="checkCertification" width="80">
						<template slot-scope="scope">
							<span v-if="scope.row.status == 0">待提交</span>
							<span v-if="scope.row.status == 1">待审核</span>
							<span v-if="scope.row.status == 2">上架中</span>
							<span v-if="scope.row.status == 3">已拒绝</span>
							<span v-if="scope.row.status == 4">已封禁</span>

						</template>
					</el-table-column> -->

					<el-table-column fixed='right' label="操作" width="180">
						<template slot-scope="scope">
							<!-- <el-button :disabled="scope.row.status !== 1 || !isAuth('autonym:jujue')" size="mini"
								style='margin:5px;' type="primary" @click="refuseClick(scope.row)">
								拒绝
							</el-button>
							<el-button :disabled="scope.row.status !== 1 || !isAuth('autonym:tongguo')" size="mini"
								style='margin:5px;' type="primary" @click="passClicks(scope.row)">通过
							</el-button> -->
							<el-button icon="document" size="mini" style='margin:5px;' type="primary"
								@click="addBtn(2,scope.row)" :disabled="!isAuth('autonym:update')">修改
							</el-button>
							<el-button icon="document" size="mini" plain style='margin:5px;' type="danger"
								@click="deleteBtn(scope.row)" :disabled="!isAuth('autonym:delete')">删除
							</el-button>
							<!-- <el-button size="mini" type="danger" style='margin:5px;'  v-if="scope.row.status == 2"
								:disabled="!isAuth('autonym:fengjin')" @click="updateBans(scope.row)">封禁</el-button>
							<el-button size="mini" type="danger" style='margin:5px;' plain v-if="scope.row.status == 4"
								:disabled="!isAuth('autonym:fengjin')" @click="updateBansJ(scope.row)">解封</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination :current-page="page" :page-size="limit" :page-sizes="[10, 20, 30, 50, 100]"
						:total="tableData.total" layout="total,sizes, prev, pager, next,jumper"
						@size-change="handleSizeChange" @current-change="handleCurrentChange">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 审核拒绝弹框 -->
			<el-dialog :visible.sync="dialogFormVisible" center title="审核拒绝">
				<div style="margin-bottom: 10px;">
					<span
						style="width: 200px;display: inline-block;text-align: right;position: relative;top: -70px;">拒绝理由：</span>
					<el-input v-model="content" :rows="4" placeholder="请输入拒绝理由" style="width:50%;" type="textarea">
					</el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible = false">取 消</el-button>
					<el-button type="primary" @click="refuseto()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 添加、修改弹框 -->
			<el-dialog :title="titles" :visible.sync="dialogFormVisibleR" center width="80%">
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">发布人：</span>
					<el-input v-model="nickName1" @focus="userselect" style="width:50%;" placeholder="请选择发布人">
					</el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">服务分类：</span>
					<el-select v-model="classId" style="width:50%;" @change="selectAu()">
						<el-option v-for="item in classListA" :key="item.classId" :label="item.className"
							:value="item.classId">
						</el-option>
					</el-select>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">套餐名称：</span>
					<el-input style="width:50%;" v-model="executed" type="text" placeholder="请输入套餐名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">联系电话：</span>
					<el-input style="width:50%;" v-model="phone" type="text" placeholder="请输入联系电话"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">服务地区：</span>
					<el-cascader style="width:50%" size="large" :options="options" ref="cascaderAddr" filterable remote
						v-model="storeAddress" :placeholder="storeAddre" @change="handleChange55" :value="storeAddre">
					</el-cascader>

				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">售价：</span>
					<el-input style="width:50%;" v-model="price" type="text"
						placeholder="请输入售价"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">份数：</span>
					<el-input style="width:50%;" v-model="numberSign" type="number" placeholder="请输入份数"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
					<el-input style="width:50%;" v-model="sort" type="number" placeholder="请输入排序"></el-input>
				</div>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">描述信息：</span>
					<el-input style="width:50%;" v-model="details" type="textarea" :rows="3"
						placeholder="请输入描述信息"></el-input>
				</div> -->

				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">标签：</span>
					<span class="bqList" v-for="(item,index) in label" :key="index">{{item}}
						<span class="delss">
							<i class="el-icon-delete" @click="clear2(index)"></i>
						</span>
					</span>
					<el-input v-model="bq" style="width:120px;" placeholder="请输入标签"
						onkeyup="this.value=this.value.replace(/[, ]/g,'')" @keydown.enter.native="btnTj"></el-input>
					<el-button type="primary" class="tj" size="mini" icon="el-icon-edit" circle @click="btnTj">
					</el-button>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">封面图：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="titleImg"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="titleImg" :src="titleImg" class="avatar" style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon"
								style="border: 1px dashed #c0ccda;line-height: 148px;width: 148px;height: 148px;font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div style="margin-bottom: 10px;display: flex;align-items: center;">
					<span style="width: 200px;display: inline-block;text-align: right;">轮播图：</span>
					<div style="display: flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in rotationImg" :key="index">
							<img width="100%" class="images" height="100%" :src="item" alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="clear1(index)"></i>
							</span>
						</div>
						<div class="imgs">
							<el-upload :action="$http.adornUrlUp('alioss/upload')"
								list-type="picture-card" :show-file-list="false" :on-success="handleUploadSuccess3"
								:on-progress="onprogress1">
								<el-progress v-if="percentage1>0 && percentage1<100" type="circle"
									:percentage="percentage1"></el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>
				</div>
				<div style="margin-bottom: 10px;" v-if="dataJson.length>0">
					<div v-for="(item,index) in dataJson" style="margin: 10px 0;">
						<span
							style="width: 200px;display: inline-block;text-align: right;">{{item.columnComment}}：</span>

						<el-input style="width:50%;display: none;" v-model="item.columnName" type="text"></el-input>
						<el-input style="width:50%;" v-if="item.htmlType==1" v-model="item.columnValue" type="text"
							:placeholder="item.columnComment"></el-input>
						<el-input style="width:50%;" v-if="item.htmlType==2" v-model="item.columnValue" type="textarea"
							:rows="3" :placeholder="item.columnComment"></el-input>
						<el-select v-model="item.columnValue" v-if="item.htmlType==3" style="width:50%;">
							<el-option v-for="(it,index2) in item.optionJson" :key="index2" :label="it" :value="it">
							</el-option>
						</el-select>
						<el-radio-group v-model="item.columnValue" v-if="item.htmlType==5">
							<el-radio v-for="(it,index3) in item.optionJson" :key="index3" :label="it">{{it}}</el-radio>
						</el-radio-group>
						<el-checkbox-group v-model="item.columnValueA" v-if="item.htmlType==4"
							style="display: inline-block;" @change="duoxian(item,index)">
							<el-checkbox v-for="(it,index1) in item.optionJson" :key="index1"
								:label="it">{{it}}</el-checkbox>
						</el-checkbox-group>
						<el-date-picker style="width: 50%;" v-if="item.htmlType==6" v-model="item.columnValue"
							align="right" type="date" format="yyyy年MM月dd日" value-format="yyyy年MM月dd日"
							placeholder="选择日期">
						</el-date-picker>
					</div>
				</div> -->
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleR = false">取 消</el-button>
					<el-button type="primary" @click="refusetoUp()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 用户列表 -->
			<el-dialog title="用户列表" :visible.sync="dialogFormVisible3" center>
				<div style="margin:2% 0;display: inline-block;">
					<el-input style="width: 150px;" @keydown.enter.native="userclick" clearable placeholder="请输入手机号"
						v-model="phone1"></el-input>&nbsp;&nbsp;&nbsp;&nbsp;
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
					<el-table-column fixed prop="nickName" width="220" label="昵称">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{scope.row.userName ? scope.row.userName : '未设置'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" width="220" label="手机号">
						<template slot-scope="scope">
							<span>{{scope.row.phone ? scope.row.phone : '未绑定'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" width="220" label="创建时间">
					</el-table-column>
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
						:page-sizes="[5, 10, 15, 20]" :page-size="limitU" :current-page="pageU"
						layout="total,sizes, prev, pager, next,jumper" :total="userIds.total">
					</el-pagination>
				</div>
			</el-dialog>
			<el-dialog title="封禁服务" :visible.sync="dialogVisibleBan" width="30%">
				<el-form ref="form" label-width="80px">
					<el-form-item label="封禁理由">
						<el-input v-model="banRemark" type="textarea" :rows="3" placeholder="请输入封禁理由"></el-input>
					</el-form-item>

				</el-form>
				<span slot="footer" class="dialog-footer">
					<el-button @click="dialogVisibleBan = false">取 消</el-button>
					<el-button type="primary" @click="updateBan()">确 定</el-button>
				</span>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	import {
		provinceAndCityData,
		regionData,
		provinceAndCityDataPlus,
		regionDataPlus,
		CodeToText,
		TextToCode
	} from 'element-china-area-data'
	export default {
		data() {
			return {
				limit: 10,
				page: 1,
				dataTitleT: '',
				userNameT: '',
				phoneT: '',
				content: '',
				status: '',
				cityT:'',
				screen: 1,
				statesnum2: [{
						value: 1,
						label: '发布时间'
					},
					{
						value: 2,
						label: '浏览次数'
					}
				],
				statesnum: [{
						value: '',
						label: '全部'
					},
					// {
					// 	value: 0,
					// 	label: '待提交'
					// }, 
					{
						value: 1,
						label: '待审核'
					}, {
						value: 2,
						label: '上架中'
					}, {
						value: 3,
						label: '已拒绝'
					}, {
						value: 4,
						label: '已封禁'
					}
				],
				classIdT: '',
				classList: [{
					classId: '',
					className: '全部'
				}],

				nickName: '',
				activeName: 'first',
				tableDataLoading: false,
				dialogFormVisible: false,
				tableData: {},
				checkBoxData: [], //多选框选择的值
				activeName: 'first',
				openValue: 1,
				closeValue: 0,
				// 添加、修改
				classListA: [],
				titles: '添加服务',
				dialogFormVisibleR: false,
				classId: '', //分类id
				titleImg: '', //首页图
				dataTitle: '', //标题
				phone: '', //联系方式
				rotationImg: [], //轮播图
				label: [], //标签
				province: '', //省
				city: '', //市
				county: '', //县
				priceInfo: '', //价格信息,这里不是仅填写货币,而是包括货币及单位
				browseCount: '', //浏览次数
				details: '', //描述信息
				dataJson: [], //分类动态字段
				percentage1: 0,
				storeAddress: [],
				storeAddre: '请选择城市',
				options: regionData,
				bq: '',
				dialogFormVisible3: false,
				tableDataLoading4: false,
				phone1: '',
				nickName2: '',
				limitU: 10,
				pageU: 1,
				userIds: {},
				nickName1: '',
				userIdss: '',
				dialogVisibleBan: false,
				banRemark: '',
				shopId: '',
				
				payClassifyId:'',
				price:'',
				executed:'',
				numberSign:'',
				sort:'',

			}
		},
		methods: {
			change(e, dataId) {
				this.$http({
					url: this.$http.adornUrl('admin/ScTableData/recommend'),
					method: 'get',
					params: this.$http.adornParams({
						'dataId': dataId,
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
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}

				})
			},
			handleClick(tab, event) {
				if (tab._props.label === '合同套餐列表') {
					this.page = 1
					this.limit = 10
					this.status = ''
					this.dataSelect()
				}
			},
			// 多选
			changeFun(val) {
				this.checkBoxData = val
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
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			handleSizeChange2(val) {
				this.limitU = val
				this.userClass()
			},
			handleCurrentChange2(val) {
				this.pageU = val
				this.userClass()
			},
			//单条任务通过
			passClicks(row) {
				this.status = 1
				this.content = '同意'
				this.$http({
					url: this.$http.adornUrl('admin/ScTableData/examine'),
					method: 'get',
					params: this.$http.adornParams({
						'res': this.status,
						'dataId': row.dataId,
						'refuseReason': ''
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
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					} else {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 1500,
							onClose: () => {
								this.content = ''
								this.status = ''
								this.dataSelect()
							}
						})
					}

				})
			},
			//审核拒绝
			refuseClick(rows) {
				this.status = 2
				this.helpTakeId = rows.dataId
				this.content = ''
				this.dialogFormVisible = true
			},
			refuseto() {
				if (this.content == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入拒绝理由',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl('admin/ScTableData/examine'),
					method: 'get',
					params: this.$http.adornParams({
						'res': this.status,
						'dataId': this.helpTakeId,
						'refuseReason': this.content
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
							this.content = ''
							this.status = ''
							this.dataSelect()
						}
					})
				})
			},
			// 查询
			select() {
				this.page = 1
				this.limit = 10
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.status = ''
				this.dataTitleT = ''
				this.cityT = ''
				this.userNameT = ''
				this.phoneT = ''

				this.classIdT = ''
				this.screen = 1
				this.dataSelect()
				// this.getClassList()
			},
			//获取分类列表
			getClassList() {
				this.$http({
					url: this.$http.adornUrl('admin/scTableClass/getClassList'),
					method: 'get',
					params: this.$http.adornParams({
						'className': '',
						'isEnable': 1
					})
				}).then(({
					data
				}) => {
					let returnData = data.data.records
					this.classList = [...this.classList, ...returnData]
					this.classListA = data.data.records
				})
			},
			// 获取服务数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/payClassify/queryPayClassifyList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'executed': this.dataTitleT,
						// 'classId': this.classIdT,
						// 'status': this.status,
						// 'province': '',
						// 'city': this.cityT,
						// 'county': '',
						// 'screen': this.screen,
						// 'userName': this.userNameT,
						// 'phone': this.phoneT,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 查询
			selectZ() {
				this.page = 1
				this.limit = 10
				this.dataSelectC()
			},
			// 重置
			cleansZ() {
				this.status = ''
				this.userName = ''
				this.nickName = ''
				this.identityCardNumber = ''
				this.dataSelectC()
			},
			// 是否推荐
			changeTj(val, row) {
				this.$http({
					url: this.$http.adornUrl('admin/ScTableData/showAndDown'),
					method: 'post',
					params: this.$http.adornParams({
						// data: this.$http.adornData({
						'dataId': row.dataId,
						'userId': row.userId,
						// 'isEnable': row.isEnable,
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
					} else {
						this.$message({
							message: data.msg,
							type: 'warning',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}
				})
			},
			// 添加、修改 服务
			addBtn(index, row) {
				if (index == 1) {
					this.titles = '添加合同套餐'
					this.payClassifyId = ''
					this.price = ''
					this.executed = ''
					this.numberSign = ''
					this.sort = ''
				} else {
					this.titles = '修改合同套餐'
					this.payClassifyId = row.payClassifyId
					this.price = row.price
					this.executed = row.executed
					this.numberSign = row.numberSign
					this.sort = row.sort
				}
				this.dialogFormVisibleR = true
			},
			// 确认 添加、修改
			refusetoUp() {
				
				if (this.executed === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入套餐名称',
						type: 'warning'
					})
					return
				}
				if (this.price === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入售价',
						type: 'warning'
					})
					return
				}
				if (this.numberSign === '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入份数',
						type: 'warning'
					})
					return
				}
				
				this.$http({
					url: this.$http.adornUrl('admin/payClassify/addPayClassify'),
					method: 'post',
					data: this.$http.adornData({
						'payClassifyId': this.payClassifyId,
						'price': this.price,
						'executed': this.executed,
						'numberSign': this.numberSign,
						'sort': this.sort,

					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						this.$message({
							type: 'success',
							message: '操作成功'
						})
						this.dataSelect()
						this.dialogFormVisibleR = false
					} else {
						this.$message({
							type: 'error',
							message: data.msg
						})
					}
				});
			},
			// 上传封面图
			handleAvatarSuccess1(file, fileList) {
				this.titleImg = file.data
			},
			//上传轮播图
			handleUploadSuccess3(file, fileList) {
				this.rotationImg.push(file.data)
				console.log('this.contentImg', this.rotationImg)
			},
			onprogress1(event, file, fileList) {
				this.percentage1 = parseInt(event.percent)
			},
			// 删除轮播图
			clear1(index) {
				this.rotationImg.splice(index, 1);
			},
			// 删除标签
			clear2(index) {
				this.label.splice(index, 1);
			},
			// 添加标签
			btnTj() {
				if (this.bq == '' || this.bq == ' ') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入标签',
						type: 'warning'
					});
					return
				} else {
					console.log('this.bq', this.bq)
					this.label.push(this.bq)
					this.bq = ''
				}
			},
			// 获取省市区
			handleChange55(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.province = value[0]
				this.city = value[1]
				this.county = value[2]

				if (this.city == '市辖区') {
					this.city = this.province
				}
				console.log(this.$refs['cascaderAddr'].currentLabels)
			},
			// 获取分类模板
			selectAu() {
				// this.classId = classId
				this.$http({
					url: this.$http.adornUrl('admin/scTableColumn/getTableByClassId'),
					method: 'get',
					params: this.$http.adornParams({
						'classId': this.classId
					})
				}).then(({
					data
				}) => {
					// if (data.code == 0) {
					if (data.data.length > 0) {
						for (var i in data.data) {
							if (data.data[i].htmlType == 4) {
								data.data[i].columnValueA = []
							}
						}
					}
					this.dataJson = data.data


					// this.show = true
					// } else {
					// 	this.$message({
					// 		type: 'error',
					// 		message: data.msg
					// 	})
					// }
				})
			},
			// 多选框
			duoxian(item, index) {
				this.$forceUpdate()
				console.log("------" + JSON.stringify(item))
				this.dataJson[index].columnValue = this.dataJson[index].columnValueA.toString()
				console.log(this.dataJson[index].columnValue)
				console.log('this.dataJson[index].columnValueA', this.dataJson[index].columnValueA)
				/* 
				// this.dataJson[index].columnValue = this.dataJson[index].columnValueA.toString()
				console.log(this.dataJson[index].columnValue )
				console.log('this.dataJson[index].columnValueA',this.dataJson[index].columnValueA) */
			},
			// 获取用户列表弹框
			userselect() {
				this.dialogFormVisible3 = true
				this.pageU = 1
				this.nickName2 = ''
				this.phone1 = ''
				this.userClass()
			},
			// 获取用户列表
			userClass() {

				this.tableDataLoading4 = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.pageU,
						'limit': this.limitU,
						'isVip': '',
						'userName': this.nickName2,
						'phone': this.phone1,
						'platform': '',
						'userType': '',
						'inviterCode': '',
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
				this.pageU = 1
				this.tableDataLoading4 = true
				this.userClass()
			},
			// 重置用户列表数据
			userclose() {
				this.pageU = 1
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
				this.dialogFormVisible3 = false
			},
			//打开封禁店铺弹窗
			updateBans(row) {
				this.shopId = row.dataId
				this.banRemark = ''
				this.dialogVisibleBan = true

			},
			//封禁店铺
			updateBan() {
				if (!this.banRemark) {
					this.$message({
						message: '请输入封禁理由',
						type: 'warning'
					});
					return
				}
				let that = this
				that.$confirm('确定封禁该服务吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					that.$http({
						url: that.$http.adornUrl('admin/ScTableData/banData'),
						method: 'post',
						params: that.$http.adornParams({
							'dataId': that.shopId,
							'refuseReason': that.banRemark
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							that.$message({
								message: '操作成功',
								type: 'success'
							});
							that.dialogVisibleBan = false
							that.dataSelect()
						} else {
							that.$message.error(data.msg);
						}
					})
				}).catch(() => {
					that.$message({
						type: 'info',
						message: '已取消'
					});
				});
			},
			// 解封服务
			updateBansJ(row){
				this.$confirm('确定解禁该服务吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/ScTableData/banData'),
						method: 'post',
						params: this.$http.adornParams({
							'dataId': row.dataId,
							'refuseReason': '解禁服务'
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success'
							});
							this.dataSelect()
						} else {
							this.$message.error(data.msg);
						}
					})
				}).catch(() => {
					this.$message({
						type: 'info',
						message: '已取消'
					});
				});
			},
			dataSelect1() {
				if (this.$route.query.status) {
					this.status = Number(this.$route.query.status)
				} else {
					this.status = ''
				}
				this.dataSelect()
			},
			// 删除
			deleteBtn(row){
				this.$confirm('确定删除该服务吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/payClassify/deletePayClassify'),
						method: 'post',
						params: this.$http.adornParams({
							'payClassifyId': row.payClassifyId
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '操作成功',
								type: 'success'
							});
							this.dataSelect()
						} else {
							this.$message.error(data.msg);
						}
					})
				}).catch(() => {
					that.$message({
						type: 'info',
						message: '已取消'
					});
				});
			}
		},
		mounted() {
			// if (this.$route.query.status) {
			// 	this.status = Number(this.$route.query.status)
			// } else {
			// 	this.status = ''
			// }
			//分类列表
			// this.getClassList()
			//服务列表
			this.dataSelect()
		},
		watch: {
			'$route': 'dataSelect1'
		}
	}
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