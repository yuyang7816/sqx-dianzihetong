<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="公司列表" name="first">
				<div style="margin:5px;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userNameT"></el-input>
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phoneT"></el-input>
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>店铺名称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入店铺名称"
						v-model="shopNameT"></el-input>
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>城市:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入城市"
						v-model="cityT"></el-input>
				</div>
				<div style="margin:5px;display: inline-block;">
					<span>状态:</span>
					<el-select v-model="status" placeholder="请选择" @change="select">
						<el-option v-for="item in statusList" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>
				</div>
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.records">

					<el-table-column  prop="id" label="编号" fixed width="80"></el-table-column>
					<!-- <el-table-column fixed="left" prop="status" label="是否推荐">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isRecommend" @change="changeTj(scope.row.isRecommend,scope.row)"
								:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column> -->
					<el-table-column label="法人名称" prop="legalPerson">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">{{
									scope.row.legalPerson ? scope.row.legalPerson : '未绑定'
								}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号"></el-table-column>
					<el-table-column prop="enterpriseName" label="企业名称" width="160"></el-table-column>
					<el-table-column prop="companyAddress" label="企业所在位置" width="200"></el-table-column>
					<el-table-column prop="registeredCapital" label="注册资金" width="200"></el-table-column>
					<el-table-column prop="registrationTime" label="注册时间" width="100"></el-table-column>
					<el-table-column prop="startTime" label="开始营业时间" width="120"></el-table-column>
					<el-table-column prop="endTime" label="结束营业时间" width="120"></el-table-column>
					<el-table-column prop="frontPhotoBusinessLicense" label="营业执照正面照片" width="150">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.frontPhotoBusinessLicense" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.frontPhotoBusinessLicense" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="backPhotoBusinessLicense" label="营业执照反面照片" width="150">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.backPhotoBusinessLicense" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.backPhotoBusinessLicense" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="shopImg" label="店铺轮播图" width="200">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;" v-for="(item,index) in scope.row.shopImg.split(',')"
									:key="index">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="item" alt="" slot="reference">
										<img style="width: 300px; height: 300px" :src="item" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="province" label="省" width="100"></el-table-column>
					<el-table-column prop="city" label="市" width="100"></el-table-column>
					<el-table-column prop="district" label="区" width="100"></el-table-column>
					<el-table-column prop="shopAddress" label="店铺地址" width="200"></el-table-column>
					<el-table-column prop="shopAddress" label="认证信息" width="200">
						<template slot-scope="scope">
							<div>法人姓名：{{scope.row.realName}}</div>
							<div>法人身份证：{{scope.row.identityCardNumber}}</div>
						</template>
					</el-table-column>
					<el-table-column prop="businessLicense" label="营业执照" width="150">
						<template slot-scope="scope">
							<div style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.businessLicense" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.businessLicense"
											alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column> -->
					<el-table-column prop="remark" label="审核理由" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.remark?scope.row.remark:'暂无'}}</span>
						</template>
					</el-table-column>
					<!-- <el-table-column prop="banRemark" label="封禁原因" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.banRemark?scope.row.banRemark:'暂无'}}</span>
						</template>
					</el-table-column> -->
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column fixed="isReal" prop="status" label="实名认证状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.isReal" @change="change2(scope.row.isReal,scope.row)"
								:active-value="1" :inactive-value="0" active-color="#13ce66"
								inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="审核状态">
						<template slot-scope="scope">
							<span style="color: red;" v-if="scope.row.status==0">待审核</span>
							<span style="color: green;" v-if="scope.row.status==1">审核通过</span>
							<span style="color: green;" v-if="scope.row.status==2">审核拒绝</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" prop="signOrApproved" label="签署/审批">
						<template slot-scope="scope">
							<span style="color: green;" v-if="scope.row.signOrApproved==1">审批</span>
							<span style="color: green;" v-else>签署</span>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="160">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" :disabled="!isAuth('shopManage:updata')"
								@click="updateShop(scope.row)" style="margin: 3px;">修改</el-button>
							<!-- <el-button size="mini" type="danger" v-if="scope.row.status==2"
								:disabled="!isAuth('shopManage:ban')"
								@click="updateBans(scope.row)" style="margin: 3px;">封禁</el-button> -->
							<el-button size="mini" type="primary" v-if="scope.row.status==0"
								:disabled="!isAuth('shopManage:examine')"
								@click="updateExamine(scope.row)" style="margin: 3px;">审核</el-button>
							<!-- <el-button size="mini" type="danger" plain v-if="scope.row.status==4"
								:disabled="!isAuth('shopManage:ban')"
								@click="jiejinBtn(scope.row)" plain style="margin: 3px;">解禁</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
		</el-tabs>
		<el-dialog @close="closeUpdate()" title="修改" :visible.sync="dialogVisible" width="50%">
			<el-form label-width="auto" ref="form" :model="shopInfo">
				<el-form-item label="企业名称">
					<el-input v-model="shopInfo.enterpriseName" placeholder="请输入企业名称"></el-input>
				</el-form-item>
				<el-form-item label="注册资金">
					<el-input v-model="shopInfo.registeredCapital" placeholder="请输入注册资金"></el-input>
				</el-form-item>
				<el-form-item label="注册时间">
					<el-input v-model="shopInfo.registrationTime" placeholder="请输入注册时间"></el-input>
				</el-form-item>
				<el-form-item label="每天开始营业时间">
					<el-time-picker value-format="hh:mm:ss" :editable="false" placeholder="请选择每天开始营业时间"
						v-model="shopInfo.startTime" style="width: 100%;"></el-time-picker>
				</el-form-item>
				<el-form-item label="每天结束营业时间">
					<el-time-picker value-format="hh:mm:ss" :editable="false" placeholder="请选择每天结束营业时间"
						v-model="shopInfo.endTime" style="width: 100%;"></el-time-picker>
				</el-form-item>
				<!-- <el-form-item label="店铺标签">
					<el-tag :key="index" v-for="(item,index) in shopInfo.shopLabel" closable
						:disable-transitions="false" @close="handleClose(index)">
						{{item}}
					</el-tag>
					<el-input class="input-new-tag" v-if="inputVisible" v-model="inputValue" ref="saveTagInput"
						size="small" @keyup.enter.native="handleInputConfirm" @blur="handleInputConfirm">
					</el-input>
					<el-button v-else class="button-new-tag" size="small" @click="showInput">+ 添 加</el-button>
				</el-form-item> -->
				<!-- <el-form-item label="店铺介绍">
					<el-input type="textarea" :rows="3" placeholder="请输入店铺介绍" v-model="shopInfo.introduce">
					</el-input>
				</el-form-item> -->
				<el-form-item label="营业执照正面照片">
					<el-upload class="avatar-uploader" :action="this.$http.adornUrlUp('alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess">
						<img v-if="shopInfo.frontPhotoBusinessLicense" :src="shopInfo.frontPhotoBusinessLicense" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</el-form-item>
				<el-form-item label="营业执照反面照片">
					<el-upload class="avatar-uploader" :action="this.$http.adornUrlUp('alioss/upload')"
						:show-file-list="false" :on-success="handleAvatarSuccess2">
						<img v-if="shopInfo.backPhotoBusinessLicense" :src="shopInfo.backPhotoBusinessLicense" class="avatar">
						<i v-else class="el-icon-plus avatar-uploader-icon"></i>
					</el-upload>
				</el-form-item>
				<!-- <el-form-item label="店铺详情图">
					<div style="display:flex;flex-wrap: wrap;">
						<div class="imgs" v-for="(item,index) in shopInfo.shopImg" :key="index">
							<img width="100%" style="border-radius: 6px;" class="images" height="100%" :src="item"
								alt="">
							<span class="dels">
								<i class="el-icon-delete" @click="dels(index)"></i>
							</span>
						</div>
						<div class="imgs" style="width: 50%;">
							<el-upload :action="$http.adornUrlUp('alioss/upload')"
								list-type="picture-card" :show-file-list="false" :on-success="handleRemove"
								:on-progress="onprogress">
								<el-progress v-if="percentage>0 && percentage<100" type="circle"
									:percentage="percentage">
								</el-progress>
								<i v-else class="el-icon-plus"></i>
							</el-upload>
						</div>
					</div>
				</el-form-item> -->
				<!-- <el-form-item label="店铺地址">
					<el-cascader style="width:100%" size="large" :options="options" ref="cascaderAddr" v-model="cityss"
						:placeholder="citysss" @change="handleChange">
					</el-cascader>
				</el-form-item> -->
				<el-form-item label="详细地址">
					<el-input type="text" placeholder="请输入详细地址" v-model="shopInfo.companyAddress">
					</el-input>
				</el-form-item>
				<!-- <el-form-item label="经度">
					<el-input :disabled="true" v-model="shopInfo.shopLng" placeholder="请选择经度"></el-input>
				</el-form-item>
				<el-form-item label="纬度">
					<el-input :disabled="true" v-model="shopInfo.shopLat" placeholder="请选择纬度"></el-input>
				</el-form-item> -->
				<!-- <div class="map" id="maps" style="width: 100%;height: 500px;">
				</div> -->
			</el-form>
			<div slot="footer" class="dialog-footer">
				<!-- <div class="map" id="maps" style="width: 100%;height: 500px;">
				</div> -->
				<el-button @click="dialogVisible = false">取 消</el-button>
				<el-button type="primary" @click="uploudShop()">确 定</el-button>
			</div>
		</el-dialog>
		<el-dialog title="封禁店铺" :visible.sync="dialogVisibleBan" width="30%">
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
		<el-dialog title="审核弹框" :visible.sync="dialogVisibleExa" >
			<el-form ref="form" label-width="200px">
				<el-form-item label="审核类型">
					<el-radio v-model="res" :label="1">通过</el-radio>
					<el-radio v-model="res" :label="2">拒绝</el-radio>
				</el-form-item>
				<el-form-item label="审核理由">
					<el-input v-model="remarks" type="textarea" :rows="3" style="width: 70%;" placeholder="请输入审核理由"></el-input>
				</el-form-item>
			</el-form>
			<span slot="footer" class="dialog-footer">
				<el-button @click="dialogVisibleExa = false">取 消</el-button>
				<el-button type="primary" @click="updateExamines()">确 定</el-button>
			</span>
		</el-dialog>
	</div>
</template>

<script>
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
	var map;
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 0,
				cityss: '',
				options: regionData,
				percentage: 0,
				fileList: [],
				remark: '',
				dialogVisible: false,
				userNameT: '',
				phoneT:'',
				shopNameT:'',
				cityT:'',
				phone: '',
				limit: 10,
				page: 1,
				activeName: 'first',
				tableDataLoading: true,
				tableData: [],
				id: '',
				// 0待审核 1审核通过 2已拒绝
				statusList: [{
						label: '全部',
						value: ''
					},
					{
						label: '待审核',
						value: 0
					},
					{
						label: '审核通过',
						value: 1
					},
					{
						label: '审核拒绝',
						value: 2
					},
				],
				status: '', //状态 1待审核 2审核通过 3审核未通过 4已封禁
				shopInfo: {
					shopName: '', //店铺名称
					shopAddress: '', //店铺详细地址
					businessData: '', //营业时间
					statTime: '', //每天开始营业时间
					entTime: '', //每天结束营业时间
					introduce: '', //店铺介绍
					shopLabel: [], //店铺标签
					shopLng: '', //店铺经度坐标
					shopLat: '', //店铺纬度坐标
					province: '', //省
					city: '', //市
					district: '', //区
					shopTitleImg: '', //店铺主图
					shopImg: [], //店铺轮播图

				}, //店铺信息
				inputVisible: false,
				inputValue: '',
				citysss: '',
				dialogVisibleBan: false,
				banRemark: '',
				shopId: '',
				remarks: '', //审核原因
				dialogVisibleExa: false,
				res: 2, //审核类型
			}
		},
		methods: {
			// 详情跳转
			updates(row) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: row.userId
					}
				})
			},
			//审核
			updateExamines() {
				if (this.res == 2 && !this.remarks) {
					this.$message({
						message: '请输入拒绝理由',
						type: 'warning'
					});
					return
				}
				let that = this
				that.$http({
					url: that.$http.adornUrl('admin/enterprise/authEnterprise'),
					method: 'post',
					// params: that.$http.adornParams({
					data: that.$http.adornData({
						'id': this.shopId,
						'status': this.res,
						'remarks': this.remarks
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功' ,
							type: 'success'
						});
						that.dialogVisibleExa = false
						that.dataSelect()
					} else {
						this.$message.error(data.msg);
					}
				})
			},
			//打开店铺审核弹窗
			updateExamine(row) {
				this.shopId = row.id
				this.remarks = ''
				this.res = 1
				this.dialogVisibleExa = true
			},
			//打开封禁店铺弹窗
			updateBans(row) {
				this.shopId = row.shopId
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
				that.$confirm('确定封禁该店铺吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					that.$http({
						url: that.$http.adornUrl('admin/scShop/banShopEnable'),
						method: 'get',
						params: that.$http.adornParams({
							'shopId': that.shopId,
							'banRemark': that.banRemark
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
			//修改店铺
			uploudShop() {
				// if (!this.shopInfo.shopName) {
				// 	this.$message({
				// 		message: '请输入店铺名称',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (!this.shopInfo.businessData) {
				// 	this.$message({
				// 		message: '请输入营业时间',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (!this.shopInfo.statTime) {
				// 	this.$message({
				// 		message: '请选择每天开始营业时间',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (!this.shopInfo.entTime) {
				// 	this.$message({
				// 		message: '请选择每天结束营业时间',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.shopInfo.shopLabel.length == 0) {
				// 	this.$message({
				// 		message: '请输入店铺标签',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (!this.shopInfo.introduce) {
				// 	this.$message({
				// 		message: '请输入店铺介绍',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (!this.shopInfo.shopTitleImg) {
				// 	this.$message({
				// 		message: '请上传店铺主图',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (this.shopInfo.shopImg.length == 0) {
				// 	this.$message({
				// 		message: '请上传店铺轮播图',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				// if (!this.shopInfo.shopAddress) {
				// 	this.$message({
				// 		message: '请选择店铺详细地址',
				// 		type: 'warning'
				// 	});
				// 	return
				// }
				let datas = JSON.parse(JSON.stringify(this.shopInfo))
				// datas.shopLabel = datas.shopLabel.join(',')
				// datas.shopImg = datas.shopImg.join(',')
				let that = this
				this.$confirm('确定修改吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('admin/enterprise/addEnterprise'),
						method: 'post',
						// params: this.$http.adornParams({
						data: this.$http.adornData(datas)
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '修改成功',
								type: 'success'
							});
							that.dialogVisible = false
							that.dataSelect()
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
			selects() {
				let that = this
				// this.address = this.province+ this.city+this.district+this.address
				var addr = that.shopInfo.province + that.shopInfo.city + that.shopInfo.district + that.shopInfo.shopAddress

				jsonp('https://apis.map.qq.com/ws/geocoder/v1/?address=' + addr +
					'&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
						myCustomUrlParam: 'veryNice'
					}).then(response => {
					// handle success
					console.log('response', response)
					that.shopInfo.shopLng = response.result.location.lng; // 经度
					that.shopInfo.shopLat = response.result.location.lat; // 纬度
					// that.address = response.result.title
					console.log(that.shopInfo.shopLat, '1111111')
					that.setMap()
				}).catch(error => {
					// handle error
				}).then(() => {
					// always executed
				});
			},
			//初始化地图
			setMap() {
				let that = this
				//设置地图中心点
				var myLatlng = new qq.maps.LatLng(that.shopInfo.shopLat, that.shopInfo.shopLng);
				//定义map变量 调用 qq.maps.Map() 构造函数   获取地图显示容器
				var map = new qq.maps.Map(document.getElementById("maps"), {
					center: myLatlng, // 地图的中心地理坐标。
					zoom: 13, // 地图的中心地理坐标。
					mapTypeId: qq.maps.MapTypeId.ROADMAP //设置地图样式详情参见MapType
				});
				//给定位的位置添加图片标注
				var marker = new qq.maps.Marker({
					position: myLatlng,
					map: map
				});
				//添加监听事件
				qq.maps.event.addListener(map, 'click', function(event) {
					//获取点击的位置经纬度赋值给表单
					that.shopInfo.shopLat = event.latLng.lat
					that.shopInfo.shopLng = event.latLng.lng
					//给地图添加点击的点标注（只能添加一个）
					if (!marker) {
						marker = new qq.maps.Marker({
							position: event.latLng,
							map: map
						});
						return;
					}
					marker.setPosition(event.latLng);

					// 使用经纬度获取详细地址
					jsonp('https://apis.map.qq.com/ws/geocoder/v1/?location=' + event.latLng.getLat() + ',' + event
						.latLng
						.getLng() + '&key=ZBABZ-ZWECU-UQTVV-4LYDR-COK3F-5SF75&get_poi=1&output=jsonp', {
							myCustomUrlParam: 'veryNice'
						}).then(response => {
						that.shopInfo.province = response.result.address_component.province //省
						that.shopInfo.city = response.result.address_component.city //市
						that.shopInfo.district = response.result.address_component.district //区
						that.shopInfo.shopAddress = response.result.formatted_addresses.recommend
						that.citysss = that.shopInfo.province + '/' + that.shopInfo.city + '/' + that
							.shopInfo.district
						console.log(that.citysss, '11111111')
					}).catch(error => {
						// handle error
					}).then(() => {
						// always executed
					});

				});
				that.dialogVisible = true

			},
			handleChange(value) {
				value = this.$refs['cascaderAddr'].currentLabels
				this.shopInfo.province = value[0]
				this.shopInfo.city = value[1]
				this.shopInfo.district = value[2]
				if (this.shopInfo.city == '市辖区') {
					this.shopInfo.city = this.shopInfo.province
				}
			},
			// 删除详情图
			dels(index) {
				this.shopInfo.shopImg.splice(index, 1);
			},
			//上传详情图
			handleRemove(file) {
				this.shopInfo.shopImg.push(file.data);
			},
			//上传进度条
			onprogress(event, file, fileList) {
				console.log('详情图上传进度', parseInt(event.percent))
				this.percentage = parseInt(event.percent)
			},
			//上传详情图
			handleAvatarSuccessimgs(e) {
				this.shopInfo.shopImg.push(e.data)
			},
			//上传店铺主图
			handleAvatarSuccess(e) {
				this.shopInfo.frontPhotoBusinessLicense = e.data
			},
			handleAvatarSuccess2(e) {
				this.shopInfo.backPhotoBusinessLicense = e.data
			},
			//删除标签
			handleClose(index) {
				this.shopInfo.shopLabel.splice(index, 1);
			},
			//点击添加显示输入框
			showInput() {
				this.inputVisible = true;
				this.$nextTick(_ => {
					this.$refs.saveTagInput.$refs.input.focus();
				});
			},
			//确认标签
			handleInputConfirm() {
				let inputValue = this.inputValue;
				if (inputValue) {
					this.shopInfo.shopLabel.push(inputValue)
				}
				this.inputVisible = false;
				this.inputValue = '';
			},
			//关闭弹窗后还原数据
			closeUpdate() {
				this.shopInfo = {
					shopName: '', //店铺名称
					shopAddress: '', //店铺详细地址
					businessData: '', //营业时间
					statTime: '', //每天开始营业时间
					entTime: '', //每天结束营业时间
					introduce: '', //店铺介绍
					shopLabel: [], //店铺标签
					shopLng: '', //店铺经度坐标
					shopLat: '', //店铺纬度坐标
					province: '', //省
					city: '', //市
					district: '', //区
					shopTitleImg: '', //店铺主图
					shopImg: [], //店铺轮播图
				}
			},
			//储存店铺信息显示修改店铺弹窗
			updateShop(row) {
				this.shopInfo = {
					id:row.id,//
					enterpriseName:row.enterpriseName,//企业名称
					companyAddress:row.companyAddress,//企业所在位置
					legalPerson:row.legalPerson,//法人
					userId:row.userId,//
					registeredCapital:row.registeredCapital,//注册资金
					registrationTime:row.registrationTime,//注册时间
					startTime:row.startTime,//营业开始时间
					endTime:row.endTime,//营业结束时间
					frontPhotoBusinessLicense:row.frontPhotoBusinessLicense,//营业执照正面照片
					backPhotoBusinessLicense:row.backPhotoBusinessLicense,//营业执照反面照片
					
					
					shopName: '', //店铺名称
					shopAddress: '', //店铺详细地址
					businessData: '', //营业时间
					statTime: '', //每天开始营业时间
					entTime: '', //每天结束营业时间
					introduce: '', //店铺介绍
					shopLabel: [], //店铺标签
					shopLng: '', //店铺经度坐标
					shopLat: '', //店铺纬度坐标
					province: '', //省
					city: '', //市
					district: '', //区
					shopTitleImg: '', //店铺主图
					shopImg: [], //店铺轮播图
				}
				this.dialogVisible = true
				// this.shopInfo = JSON.parse(JSON.stringify(row))
				// if (this.shopInfo.shopLabel) {
				// 	this.shopInfo.shopLabel = this.shopInfo.shopLabel.split(',')
				// } else {
				// 	this.shopInfo.shopLabel = []
				// }
				// if (this.shopInfo.shopImg) {
				// 	this.shopInfo.shopImg = this.shopInfo.shopImg.split(',')
				// } else {
				// 	this.shopInfo.shopImg = []
				// }
				// this.citysss = this.shopInfo.province + '/' + this.shopInfo.city + '/' + this.shopInfo.district
				// this.setMap()

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
			//重置
			cleans() {
				this.page = 1
				this.limit = 10
				this.userNameT = ''
				this.phoneT = ''
				this.shopNameT = ''
				this.cityT = ''
				this.status = ''
				this.dataSelect()
			},
			//查询
			select() {
				this.dataSelect()
			},
			//
			handleClick() {

			},
			// 获取店铺数据列表
			dataSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/enterprise/getEnterpriseList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'legalPerson': this.userNameT,
						// 'phone': this.phoneT,
						'enterpriseName': this.shopNameT,
						'status': this.status,
						'companyAddress':this.cityT,

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			// 是否启用
			change2(val, row) {
				this.$http({
					url: this.$http.adornUrl('admin/scShop/updateGoods'),
					method: 'post',
					// params: this.$http.adornParams({
					data: this.$http.adornData({
						'shopId': row.shopId,
						'isEnable': row.isEnable,
						
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
			// 是否推荐
			changeTj(val, row){
				this.$http({
					url: this.$http.adornUrl('admin/scShop/recommendShop'),
					method: 'get',
					params: this.$http.adornParams({
					// data: this.$http.adornData({
						'shopId': row.shopId,
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
			// 解禁
			jiejinBtn(row){
				var that = this
				that.$confirm('确定解禁该店铺吗？', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					that.$http({
						url: that.$http.adornUrl('admin/scShop/banShopEnable'),
						method: 'get',
						params: that.$http.adornParams({
							'shopId': row.shopId,
							'banRemark':'解禁店铺'
						})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							that.$message({
								message: '操作成功',
								type: 'success'
							});
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
			dataSelect1(){
				if( this.$route.query.status){
					this.status = Number(this.$route.query.status) 
				}else{
					this.status =''
				}
				this.dataSelect()
			},
			
		},
		mounted() {
			if( this.$route.query.status){
				this.status = Number(this.$route.query.status) 
			}else{
				this.status =''
			}
			this.dataSelect()
		},
		watch: {
			'$route': 'dataSelect1'
		}
	}
</script>
<style>
	.el-upload--picture-card {
		width: 178px !important;
		height: 178px !important;
		line-height: 178px !important;
	}

	.el-dialog__body {
		padding: 30px 20px 0 20px !important;
	}
</style>
<style scoped="scoped">
	.imgs {
		position: relative;
		border-radius: 6px;
		width: 178px !important;
		height: 178px !important;
		margin-right: 10px;
		display: inline-block;
		margin-bottom: 10px;
		border-radius: 6px;
	}

	.dels {
		position: absolute;
		top: 0;
		left: 0;
		display: none;
	}

	.dels .el-icon-delete {
		line-height: 178px;
		padding-left: 78px;
		font-size: 25px;
		color: #fff;
	}

	.imgs:hover .dels {
		width: 100%;
		height: 100%;
		background: #000;
		display: block;
		opacity: 0.5;
		border-radius: 6px;
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

	.el-tag+.el-tag {
		margin-left: 10px;
	}

	.button-new-tag {
		margin-left: 10px;
		height: 32px;
		line-height: 30px;
		padding-top: 0;
		padding-bottom: 0;
	}

	.input-new-tag {
		width: 90px;
		margin-left: 10px;
		vertical-align: bottom;
	}

	.el-upload {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
	}

	.avatar-uploader {
		border: 1px dashed #d9d9d9;
		border-radius: 6px;
		cursor: pointer;
		position: relative;
		overflow: hidden;
		border-color: #409EFF;
		width: 178px;
		height: 178px;
	}

	.el-upload:hover {
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

	.el-button+.el-button {
		margin-left: 0 !important;
		margin-top: 5px !important;
	}
</style>