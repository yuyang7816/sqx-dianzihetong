<template>
	<div>
		<div style="display: inline-block;font-size:18px;margin-bottom: 15px;">
			<a href="#" @click="prev" style="text-decoration:none;font-size: 14px;">
				<icon-svg name="jiantou" style="width: 1.2em;height: 1.2em;position: relative;top: 0.3em;"></icon-svg>
				返回
			</a>
			<span style="display: inline-block;margin: 0 15px;color: #D9D9D9;">|</span>
			<span>用户详情</span>
		</div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="用户详情" name="first">
				<div class="detailtabel">
					<div class="table_main">
						<table>
							<tbody>
								<!-- <tr>
									<th>本月接单数量</th>
									<td>{{ tablenum.takeOrdersCount ? `${tablenum.takeOrdersCount}笔` : '0笔' }}</td>
									<th>本月充值金额</th>
									<td>{{ tablemoney.consume ? `${tablemoney.consume}元` : '0元' }}</td>
									<th>本月邀请数量</th>
									<td class="border-rt">{{ tablemoney.count ? `${tablemoney.count}人` : '0人' }}
									</td>
								</tr>
								<tr>
									<th>本月订单数量</th>
									<td>{{ tablemoney.myOrdersCount ? `${tablemoney.myOrdersCount}笔` : '0笔' }}</td>
									<th>本月提现金额</th>
									<td>{{ tablemoney.income ? `${tablemoney.income}元` : '0元' }}</td>
									<th>可提现金额</th>
									<td class="border-rt">{{tablemoney.money}}参数不对
									</td>
									
								</tr>
								<tr>
									<th>陪玩官佣金比例</th>
									<td>{{ tableData.rate ? `${tableData.rate}` : '0' }}</td>
									<th>一级佣金比例</th>
									<td>{{ tableData.zhiRate ? `${tableData.zhiRate}` : '0' }}</td>
									<th>二级佣金比例</th>
									<td class="border-rt">{{ tableData.feiRate ? tableData.feiRate : '0' }}</td>
								</tr> -->
								<tr>
									<th>邀请人邀请码</th>
									<td>
										{{ tableData.inviterCode }}
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="ageChangeU(tableData)">修改</el-button>
									</td>
									<th>当前收益</th>
									<td style="color: rgb(245, 108, 108)">
										{{ tablemoney.money ? `${tablemoney.money}元` : '0元' }}
										<!-- <el-button size="mini" :disabled="!isAuth('userList:updatejf')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="rechargenone(tableData.userId)">修改金额</el-button> -->
									</td>
									<th>总收入</th>
									<td class="border-rt">{{tableData.balance?tableData.balance:'0元'}}</td>
									<!-- <th></th>
									<td class="border-rt"></td> -->
								</tr>
								<tr>
									<th>openId</th>
									<td>
										{{ tableData.openId ? tableData.openId : '未绑定' }}
									</td>
									<!-- <th>邀请人数</th>
									<td>
										{{ tablemoney.count ? `${tablemoney.count}人` : '0人' }}
										{{ tableData.rate ? `${tableData.rate}` : '0' }}
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="ageChange(7,tableData.userId,tableData.rate)">修改</el-button>
									</td> -->
									<th>直属佣金比例</th>
									<td>{{ tableData.zhiRate ? `${tableData.zhiRate}` : '0' }}
									<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
										@click="ageChangeYj(tableData.zhiRate,4)">修改</el-button>
									</td>
									<th>非直属佣金比例</th>
									<td class="border-rt">{{ tableData.feiRate ? tableData.feiRate : '0' }}
									<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
										@click="ageChangeYj(tableData.feiRate,5)">修改</el-button>
									</td>
								</tr>
								<tr>
									<th>创建时间</th>
									<td>{{ tableData.createTime }}</td>
									<th>收款二维码</th>
									<td>
										<div v-if="tableData.wxImg == null || tableData.wxImg == ''">
											暂无收款二维码
										</div>
										<div v-else style="display:flex;flex-wrap: wrap;">
											<div style="margin: 2px;">
												<el-popover placement="top-start" title="" trigger="hover">
													<img style="width: 50px; height: 50px" :src="tableData.wxImg" alt=""
														slot="reference">
													<img style="width: 300px; height: 300px" :src="tableData.wxImg"
														alt="">
												</el-popover>
											</div>
										</div>
									</td>
									<th>图像</th>
									<td class="border-rt">
										<img v-if="tableData.avatar==null" src="~@/assets/img/avatar.png" alt=""
											width="80" height="80">
										<img v-else :src="tableData.avatar" width="80" height="80" />
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="ageChangeU(tableData)">修改</el-button>
									</td>
								</tr>
								<tr>
									<th>微信名称</th>
									<td>
										{{ tableData.userName }}
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="ageChangeU(tableData)">修改</el-button>
									</td>
									<th>手机号</th>
									<td>
										{{ tableData.phone ? tableData.phone : '未绑定' }}
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="ageChangeU(tableData)">修改</el-button>
									</td>
									<th>支付宝姓名</th>
									<td class="border-rt">{{ tableData.zhiFuBaoName ? tableData.zhiFuBaoName : '未绑定' }}
									</td>
								</tr>
								<tr>
									<th>邀请码</th>
									<td>
										{{ tableData.invitationCode }}
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="ageChangeU(tableData)">修改</el-button>
									</td>
									<th>支付宝账号</th>
									<td>{{ tableData.zhiFuBao ? tableData.zhiFuBao : '未绑定' }}</td>
									<th>用户状态</th>
									<td class=" border-rt">
										<span v-if="tableData.status == 1 ">正常</span>
										<span v-if="tableData.status == 2 " style="color: #f56c6c;">禁用</span>
										<el-button size="mini" :disabled="!isAuth('userList:updateStatus')"
											style="color: #4f9dec;background: #fff;border: none;"
											@click="stateChange(tableData.userId)">更改状态</el-button>
									</td>
								</tr>
								<tr>
									<th>是否实名</th>
									<td>
										<span v-if="tableData.isAuthentication==1">待审核</span>
										<span v-else-if="tableData.isAuthentication==2">已实名</span>
										<span v-else-if="tableData.isAuthentication==3">审核未通过</span>
										<span v-else>未实名</span>
									</td>
									<th>性别</th>
									<td>
										<span v-if="tableData.sex==0">未设置</span>
										<span v-if="tableData.sex==1">男</span>
										<span v-if="tableData.sex==2">女</span>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="sexChange(tableData.userId,tableData.sex)">修改</el-button>
									</td>
									<th>年龄</th>
									<td class="border-rt">
										<span v-if="tableData.age">{{tableData.age}}</span>
										<span v-else>未设置</span>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="ageChange(tableData.userId,tableData.age)">修改</el-button>
									</td>
								</tr>
								<tr>
									<th>自动回复</th>
									<td>
										<span>{{tableData.msg?tableData.msg:'暂无'}}</span>
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="msgChange(tableData.userId,tableData.msg)">修改</el-button>
									</td>
									<th>是否是会员</th>
									<td>
										<span v-if="tableData.isVip==1">是</span>
										<span v-else>否</span>
									</td>
									<th>会员到期时间</th>
									<td class="border-rt">
										{{tableData.vipEndTime}}
									</td>
								</tr>
								<tr>
									<th class="border-bt">合同数量</th>
									<td class="border-bt">
										{{tableData.freeTimes?tableData.freeTimes:0}}
										<el-button size="mini" style="color: #4f9dec;background: #fff;border: none;"
											@click="ageChangeU(tableData)">修改</el-button>
									</td>
									<th class="border-bt"></th>
									<td class="border-bt">
										
									</td>
									<th class="border-bt"></th>
									<td class="border-bt border-rt">
									</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
			</el-tab-pane>
			<el-tab-pane label="提现记录" name="sixth">
				<el-table v-loading="tableDataLoading" :data="withdrawData.list">
					<el-table-column fixed prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="zhifubao" label="支付宝账号">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates(scope.row.userId)">{{scope.row.zhifubao ? scope.row.zhifubao : 'null'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhifubaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{scope.row.zhifubaoName ? scope.row.zhifubaoName : 'null'}}</span>
						</template>
					</el-table-column>
					<el-table-column prop="classify" label="提现方式" width="100">
						<template slot-scope="scope">
							<span v-if="scope.row.classify == 1">支付宝</span>
							<span v-if="scope.row.classify == 2">微信小程序</span>
							<span v-if="scope.row.classify == 3">微信公众号</span>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="提现金额" width="100">
					</el-table-column>
					<el-table-column prop="state" label="状态" width="100" fixed="right">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 0">待转账</span>
							<span v-if="scope.row.state == 1">已转账</span>
							<span v-if="scope.row.state == -1">已退款</span>
						</template>
					</el-table-column>
					<el-table-column prop="refund" label="退款原因" width="220">
					</el-table-column>
					<el-table-column prop="createAt" label="申请时间" width="170">
					</el-table-column>
					<el-table-column prop="outAt" label="转账/退款时间" width="180">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeTx" @current-change="handleCurrentChangeTx"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="withdrawData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="钱包明细" name="eighth">
				<el-table v-loading="tableDataLoading" :data="walletData.records">
					<el-table-column prop="id" label="编号" width="100"></el-table-column>
					<el-table-column prop="title" label="标题"></el-table-column>
					<el-table-column prop="content" label="内容"></el-table-column>
					<el-table-column prop="money" label="金额" width="100">
						<template slot-scope="scope">
							<span style="color: #f56c6c;" v-if="scope.row.type==2">- {{ scope.row.money }}</span>
							<span style="color: #006600;" v-if="scope.row.type==1">+ {{ scope.row.money }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180"></el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeQbmx" @current-change="handleCurrentChangeQbmx"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="walletData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="充值明细" name="seventh">
				<div style="display: inline-block;">
					<span>开始时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="startTime" align="right"
						type="date" format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择开始时间">
					</el-date-picker>&nbsp;&nbsp;&nbsp;
					<span>截止时间：</span>
					<el-date-picker style="width: 160px;margin-left: 10px;" v-model="endTime" align="right" type="date"
						format="yyyy-MM-dd" value-format="yyyy-MM-dd" placeholder="选择截止时间">
					</el-date-picker>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="timeDate2">
						查询</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="rechargeData.list">
					<el-table-column prop="id" label="编号" width="50"></el-table-column>
					<el-table-column prop="orderId" label="充值订单号" width="200"></el-table-column>
					<el-table-column prop="money" label="充值金额"></el-table-column>
					<el-table-column prop="userId" label="会员编号"></el-table-column>
					<el-table-column label="分类">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 1">app微信</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 2">微信公众号</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 3">微信小程序</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.classify == 4">支付宝</span>
						</template>
					</el-table-column>
					<el-table-column label="状态">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 0">待支付</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 1">支付成功</span>
							<span style="color: #4f9dec;cursor: pointer;" v-if="scope.row.state == 2">支付失败</span>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="170">
					</el-table-column>
					<el-table-column prop="payTime" label="支付时间" width="170">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeCzmx" @current-change="handleCurrentChangeCzmx"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="rechargeData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			
			<el-tab-pane label="我的团队" name="tuandui">
				<div>
					<span>等级：</span>
					<el-select v-model="types" style="width:150px;margin-left: 10px;" @change="animeDat(types)">
						<el-option v-for="item in typesList" :key="item.type" :label="item.name" :value="item.type">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>
				<el-table v-loading="tableDataLoading" :data="tuanduiData.records">
					<el-table-column fixed prop="userId" label="id" width="80">
					</el-table-column>
					<el-table-column prop="userName" label="用户昵称" width="150">
						<template slot-scope="scope">
							<div style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{scope.row.userName ? scope.row.userName : '未绑定'}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像">
						<template slot-scope="scope">
							<div style="margin-top: 5px;" v-if="scope.row.avatar">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.avatar" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.avatar" alt="">
								</el-popover>
							</div>
							<div v-else><img style="width: 50px; height: 50px" src="~@/assets/img/avatar.png" alt=""
									slot="reference"></div>
						</template>
					</el-table-column>
					<el-table-column prop="money" label="收益(单位:元)">
					</el-table-column>
					<!-- <el-table-column prop="createTime" label="创建时间" width="160">
					</el-table-column> -->
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeTd" @current-change="handleCurrentChangeTd"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tuanduiData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- <el-tab-pane label="团队收入" name="tuanduishouru">
				<el-table v-loading="tableDataLoading" :data="fensiData.records">
					<el-table-column prop="userId" label="编号" width="80"></el-table-column>
					<el-table-column prop="userName" label="用户名">
						<template slot-scope="scope">
							<span style="color: #f56c6c;cursor:pointer"
								@click="updates(scope.row)">{{ scope.row.userName }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="avatar" label="用户头像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="updateTime" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeFs" @current-change="handleCurrentChangeFs"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="fensiData.total">
					</el-pagination>
				</div>
			</el-tab-pane> -->
			<el-tab-pane label="我发起的合同" name="message">
				<el-table v-loading="tableDataLoading" :data="userDataLl.records">
					<el-table-column prop="id" label="编号" width="80" fixed="left">
					</el-table-column>
					<!-- <el-table-column prop="userName" label="发起用户" fixed="left">
						<template slot-scope="scope">
							　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row.userId)"
								v-text="scope.row.userName"></span>
						</template>
					</el-table-column> -->
					<el-table-column prop="contractType" label="创建类型" width="100">
						<template slot-scope="scope">
							<span  v-if="scope.row.contractType === 1 ">模板创建</span>
							<span  v-if="scope.row.contractType === 2 ">微信创建</span>
							<span  v-if="scope.row.contractType === 3 ">草稿箱创建</span>
							<span  v-if="scope.row.contractType === 4 ">个人签署合同</span>
						</template>
					</el-table-column>
					<el-table-column prop="contractName" label="合同名称">
					</el-table-column>
					<el-table-column prop="contractNumber" label="合同编号">
					</el-table-column>
					<el-table-column prop="fileUrl" label="文件路径" width="300">
					</el-table-column>
					<el-table-column prop="lateSignDays" label="最晚签署天数" width="100">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="130">
						<template slot-scope="scope">
						<!-- 	<el-button size="mini" type="primary" @click="userselectSz(scope.row)" style="margin: 5px;">合同数据值
							</el-button> -->
							<el-button size="mini" type="primary" @click="plCompile(scope.row)" style="margin: 5px;">发送列表
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeLl" @current-change="handleCurrentChangeLl"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="userDataLl.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我收到的合同" name="messageDp">
				<el-table v-loading="tableDataLoading" :data="userDataLl.records">
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
					<el-table-column prop="contractName" label="合同名称">
					</el-table-column>
					<el-table-column prop="contractNumber" label="合同编号">
					</el-table-column>
					<el-table-column prop="fileUrl" label="文件路径" width="300">
					</el-table-column>
					<el-table-column prop="lateSignDays" label="最晚签署天数" width="100">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
					<!-- <el-table-column fixed="right" prop="status" label="操作" width="130">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="userselectSz(scope.row)" style="margin: 5px;">合同数据值
							</el-button>
							<el-button size="mini" type="primary" @click="plCompile(scope.row)" style="margin: 5px;">发送列表
							</el-button>
						</template>
					</el-table-column> -->
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeLl" @current-change="handleCurrentChangeLl"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="userDataLl.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我抄送的合同" name="fans">
				<el-table v-loading="tableDataLoading" :data="fensiData.records">
					<el-table-column prop="id" label="编号" width="80" fixed="left">
					</el-table-column>
					<el-table-column prop="userName" label="发起用户" fixed="left">
						<template slot-scope="scope">
							　<span style="color: #4f9dec;cursor: pointer;" @click="updatesvideo(scope.row.userId)"
								v-text="scope.row.userName"></span>
						</template>
					</el-table-column>
					<el-table-column prop="type" label="创建类型" width="100">
						<template slot-scope="scope">
							<span  v-if="scope.row.type === 1 ">模板创建</span>
							<span  v-if="scope.row.type === 2 ">微信创建</span>
							<span  v-if="scope.row.type === 3 ">草稿箱创建</span>
						</template>
					</el-table-column>
					<el-table-column prop="contractName" label="合同名称">
					</el-table-column>
					<el-table-column prop="contractNumber" label="合同编号">
					</el-table-column>
					<el-table-column prop="fileUrl" label="文件路径">
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="180">
					</el-table-column>
					<el-table-column fixed="right" prop="status" label="操作" width="130">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="userselectSz(scope.row)" style="margin: 5px;">合同数据值
							</el-button>
							<!-- <el-button size="mini" type="primary" @click="plCompile(scope.row)" style="margin: 5px;">发送列表
							</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeFs" @current-change="handleCurrentChangeFs"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="fensiData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			
			<el-tab-pane label="我的消息" name="xiaoxi">
				<el-table v-loading="tableDataLoading" :data="tousuData.list">
					<el-table-column prop="id" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="title" label="消息标题">
					</el-table-column>
					<el-table-column prop="content" label="消息内容">
					</el-table-column>
					<el-table-column prop="createAt" label="创建时间">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeWdxx" @current-change="handleCurrentChangeWdxx"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="tousuData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="我的印章" name="yinzhang">
				<el-table v-loading="tableDataLoading" :data="fangkeData.records">
					<el-table-column fixed label="编号" width="80" prop="id">
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
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeYz" @current-change="handleCurrentChangeYz"
						:page-sizes="[10, 20, 30, 40]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next" :total="fangkeData.total">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 不可提现添加金额 -->
			<el-dialog title="修改金额" :visible.sync="dialogFormVisible1" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">修改类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">金额：</span>
					<el-input style="width:50%;" v-model="money" type="number" :min="0" :max="100" :controls="false"
						placeholder="请输入金额"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible1 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo1()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改年龄 -->
			<el-dialog title="修改信息" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;"  v-if="indexType!=4&&indexType!=5">
					<span style="width: 200px;display: inline-block;text-align: right;">年龄：</span>
					<el-input style="width:50%;" v-model="age" type="number" :min="0" :controls="false"
						placeholder="请输入年龄"></el-input>
				</div>
				<div style="margin-bottom: 10px;" v-if="indexType==4||indexType==5">
					<span style="width: 200px;display: inline-block;text-align: right;">佣金比例：</span>
					<el-input style="width:50%;" v-model="age" type="number" :min="0" :controls="false"
						placeholder="输入佣金比例"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改年龄 -->
			<el-dialog title="修改自动回复" :visible.sync="dialogFormVisible5" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">自动回复：</span>
					<el-input style="width:50%;" v-model="msg" type="text" placeholder="请输入自动回复"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible5 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo3()">确 定</el-button>
				</div>
			</el-dialog>

			<!-- 聊天内容 -->
			<el-dialog title="聊天内容" :visible.sync="dialogFormVisible4" center>
				<el-table v-loading="tableDataLoading1" :data="pinglunDatas.records">
					<el-table-column prop="commentId" label="编号" width="80">
					</el-table-column>
					<el-table-column prop="nickName" label="用户昵称">
						<template slot-scope="scope">
							<div style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row.userId)">
								{{scope.row.nickName ? scope.row.nickName : '未绑定'}}
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="imageUrl" label="用户头像">
						<template slot-scope="scope">
							<div v-if="scope.row.imageUrl == null || scope.row.imageUrl == ''">
								暂无图片
							</div>
							<div v-else>
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.imageUrl" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.imageUrl" alt="">
								</el-popover>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="信息内容" width="280">
						<template slot-scope="scope">
							<div>{{scope.row.content}}</div>
							<!-- <div v-if="scope.row.messageType==2">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px" :src="scope.row.content" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px" :src="scope.row.content" alt="">
								</el-popover>
							</div>
							<div v-if="scope.row.messageType==3">
								<audio :src="scope.row.content" type="audio/wav" controls="controls"></audio>
							</div>
							<div v-if="scope.row.messageType==4">
								<el-popover placement="top-start" title="" trigger="hover">
									<img style="width: 50px; height: 50px"
										:src="'/emoji/'+scope.row.content" alt=""
										slot="reference">
									<img style="width: 200px; height: 200px"
										:src="'/emoji/'+scope.row.content" alt="">
								</el-popover>
							</div> -->
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="150">
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChangeLtnr" @current-change="handleCurrentChangeLtnr"
						:page-sizes="[10, 20, 30, 50, 100]" :page-size="limit1" :current-page="page1"
						layout="total,sizes, prev, pager, next" :total="pinglunDatas.total">
					</el-pagination>
				</div>
			</el-dialog>
			<!-- 添加/修改常用语 -->
			<el-dialog :title="title2" :visible.sync="dialogFormVisible3" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">常用语：</span>
					<el-input style="width:50%;" v-model="contents" type="text" placeholder="请输入常用语"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible3 = false">取 消</el-button>
					<el-button type="primary" @click="addGuanjianci()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 修改信息 -->
			<el-dialog title="修改信息" :visible.sync="dialogFormVisibleU" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">微信名称：</span>
					<el-input style="width:50%;" v-model="userNameU" type="text"  placeholder="请输入微信名称"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">手机号：</span>
					<el-input style="width:50%;" v-model="phoneU" type="text"  placeholder="请输入手机号"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">邀请人邀请码：</span>
					<el-input style="width:50%;" v-model="inviterCodeU" type="text"  placeholder="请输入邀请人邀请码"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">邀请码：</span>
					<el-input style="width:50%;" v-model="invitationCodeU" type="text"  placeholder="请输入邀请码"></el-input>
				</div>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">合同数量：</span>
					<el-input style="width:50%;" v-model="freeTimesU" type="text"  placeholder="请输入合同数量"></el-input>
				</div>
				
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">图像：</span>
					<div style="display: inline-block;">
						<el-upload class="avatar-uploader" v-model="avatarU"
							:action="$http.adornUrlUp('alioss/upload')" :show-file-list="false"
							:on-success="handleAvatarSuccess1">
							<img v-if="avatarU" :src="avatarU" class="avatar"
								style="width: 148px;height: 148px;" />
							<i v-else class="el-icon-plus avatar-uploader-icon" style="font-size: 28px;color: #8c939d"></i>
						</el-upload>
					</div>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleU = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeToU()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 合同数组 -->
			<el-dialog title="合同字段" :visible.sync="dialogFormVisibleHtsz" center width="80%">
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
				</el-table>
			</el-dialog>
			<!-- 发送列表 -->
			<el-dialog title="发送列表" :visible.sync="dialogFormVisible10" center width="80%">
				<el-table v-loading="tableDataLoading" :data="pinglunData">
					<el-table-column prop="id" label="编号" width="80" fixed="left">
					</el-table-column>
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
				</el-table>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				openValue: 1,
				closeValue: 0,
				state: 0,
				limit: 10,
				page: 1,
				limit1: 10,
				page1: 1,
				size: 10,
				userId: '',
				money: '',
				title: '',
				content: '',
				phone: '',
				startTime: '',
				endTime: '',
				invitationCode: '',
				feiDatamin: '',
				info: {
					stockDate: this.getNowTime(), //日期
				},
				info2: {
					stockDate2: this.getNowTime2(), //日期
				},
				tablejifen: '',
				autonymData: {},
				tablemoney: {},
				tablenum: {},
				tableData: {},
				userData: {},
				userDataLl: {},
				userDataCp: {},
				userDataJp: {},
				fangkeData: {},
				tableDatamin: [],
				withdrawData: {},
				walletData: {},
				rechargeData: {},
				sendData: [],
				takeData: {},
				jiedanData: {},
				dingdanData: {},
				fensiData: {},
				guanzhuData: {},
				tableDataZz: {},
				activeName: 'first',
				dialogFormVisible: false,
				dialogFormVisible1: false,
				dialogFormVisible2: false,
				dialogFormVisible3: false,
				dialogFormVisible4: false,
				dialogFormVisible5: false,
				tableDataLoading: true,
				tableDataLoading1: true,
				relation_id: '',
				relationId: '',
				tkstatus: '',
				invitation: '',
				nowMonthOver: '',
				lastMonthOver: '',
				nowMonthPaid: '',
				lastMonthPaid: '',
				nowMonthOrderNumber: '',
				nowDayOrderNumber: '',
				hasCash: '',
				search: 0,
				classifyIds: 0,
				statusId: -1,
				myPhone: '',
				type: '',
				age: '',
				msg: '',
				chatConversationId: '',
				contents: '',
				guanjianciId: '',
				title2: '添加关键词',
				liaotianData: {},
				huihuaData: {},
				guanjianciData: {},
				tuanduiData: {},
				userId: '',
				tousuData: {},
				dongtaiData: {},
				dianzanData: {},
				
				pinglunDatas: {},
				dashangData: {},
				types: 1,
				typesList: [{
						type: 1,
						name: '一级'
					},
					{
						type: 2,
						name: '二级'
					}
				],
				states: 3,
				statesnum: [{
					value: 0,
					label: '投保'
				}, {
					value: 1,
					label: '竞拍中'
				}, {
					value: 2,
					label: '已结束'
				}],
				typeLl: 1,
				// 修改信息
				dialogFormVisibleU:false,
				inviterCodeU:'',
				avatarU:'',
				userNameU:'',
				phoneU:'',
				invitationCodeU:'',
				freeTimesU:'',
				indexType:'',
				dialogFormVisibleHtsz:false,
				userIds:[],
				dialogFormVisible10:false,
				pinglunData: [],
				
				
			}
		},
		// watch:{
		// 	userId(name){
		// 		this.userId = this.$route.query.userId
		// 		console.log('this.userId',this.userId)
		// 	}
		// },
		methods: {
			// 返回上一级
			prev() {
				this.$router.back()
			},
			// 详情跳转
			updates(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
			// 详情跳转
			updates1(userId) {
				this.$router.push({
					path: '/userDetail',
					query: {
						userId: userId
					}
				})
			},
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
			// tabs切换
			handleClick(tab, event) {
				if (tab._props.label == '用户详情') {
					this.dataSelect()
					// this.autonymSelect()
				}

				if (tab._props.label == '提现记录') {
					this.limit = 10,
						this.page = 1,
						this.withdrawSelect()
				}
				if (tab._props.label == '钱包明细') {
					this.limit = 10
					this.page = 1
					this.walletSelect()
				}
				if (tab._props.label == '充值明细') {
					this.limit = 10
					this.page = 1
					this.rechargeSelect()
				}
				if (tab._props.label == '我的接单') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.jiedanSelect()
				}
				if (tab._props.label == '我的发布') {
					this.limit = 10
					this.page = 1
					this.takeSelect()
				}
				if (tab._props.label == '我收藏的店铺') {
					this.limit = 10
					this.page = 1
					this.InformationSelect()
				}
				if (tab._props.label == '我的买入') {
					this.limit = 10
					this.page = 1
					this.types = 2
					this.dingdanSelect()
				}
				if (tab._props.label == '我的卖出') {
					this.limit = 10
					this.page = 1
					this.types = 1
					this.dingdanSelect()
				}
				if (tab._props.label == '我发起的合同') {
					this.limit = 10
					this.page = 1
					this.typeLl = 1
					this.userSelect()
				}
				if (tab._props.label == '我收到的合同') {
					this.limit = 10
					this.page = 1
					this.typeLl = 2
					this.userSelect()
				}
				if (tab._props.label == '我抄送的合同') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.fensiSelect()
				}
				if (tab._props.label == '我关注的服务') {
					this.limit = 10
					this.page = 1
					this.state = 0
					this.guanzhuSelect()
				}
				if (tab._props.label == '我的印章') {
					this.limit = 10
					this.page = 1
					this.fangkeSelect()
				}
				if (tab._props.label == '聊天记录') {
					this.limit = 10
					this.page = 1
					this.liaotianSelect()
				}
				if (tab._props.label == '我的竞拍') {
					this.limit = 10
					this.page = 1
					this.search = 0
					this.jingpaiSelect()
				}
				if (tab._props.label == '我的转增') {
					this.limit = 10
					this.page = 1
					this.types = 1
					this.tuanduiSelect()
				}
				if (tab._props.label == '我收到的转增') {
					this.limit = 10
					this.page = 1
					this.types = 2
					this.tuanduiSelect()
				}

				if (tab._props.label == '实名认证') {
					this.autonymSelect()
				}
				if (tab._props.label == '我的投诉') {
					this.limit = 10
					this.page = 1
					this.states = 3
					this.tousuSelect()
				}
				if (tab._props.label == '我的消息') {
					this.limit = 10
					this.page = 1
					this.states = 5
					this.tousuSelect()
				}
				if (tab._props.label == '我的铸造') {
					this.limit = 10
					this.page = 1
					this.sendSelect()
				}

				if (tab._props.label == '我的动态') {
					this.limit = 10
					this.page = 1
					this.dongtaiSelect()
				}
				if (tab._props.label == '我的喜欢') {
					this.limit = 10
					this.page = 1
					this.dianzanSelect()
				}

				if (tab._props.label == '我的打赏') {
					this.limit = 10
					this.page = 1
					this.dashangSelect()
				}
				if (tab._props.label == '空投奖励') {
					this.limit = 10
					this.page = 1
					this.yhqSelect()
				}
				
				if (tab._props.label == '我的团队') {
					this.limit = 10
					this.page = 1
					this.types = 1
					this.tuanduiSelect1()
				}
			},
			handleSizeChange(val) {
				this.limit = val
				this.teamSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.teamSelect()
			},
			handleSizeChangeTx(val) {
				this.limit = val
				this.withdrawSelect()
			},
			handleCurrentChangeTx(val) {
				this.page = val
				this.withdrawSelect()
			},
			handleSizeChangeQbmx(val) {
				this.limit = val
				this.walletSelect()
			},
			handleCurrentChangeQbmx(val) {
				this.page = val
				this.walletSelect()
			},
			handleSizeChangeCzmx(val) {
				this.limit = val
				this.rechargeSelect()
			},
			handleCurrentChangeCzmx(val) {
				this.page = val
				this.rechargeSelect()
			},
			handleSizeChangeDd(val) {
				this.limit = val
				this.dingdanSelect()
			},
			handleCurrentChangeDd(val) {
				this.page = val
				this.dingdanSelect()
			},
			handleSizeChangeLtnr(val) {
				this.limit1 = val
				this.pinglunSelects(this.talkId)
			},
			handleCurrentChangeLtnr(val) {
				this.page1 = val
				this.pinglunSelects(this.talkId)
			},
			handleSizeChangeWdxx(val) {
				this.limit = val
				this.tousuSelect()
			},
			handleCurrentChangeWdxx(val) {
				this.page = val
				this.tousuSelect()
			},
			handleSizeChangeLl(val) {
				this.limit = val
				this.userSelect()
			},
			handleCurrentChangeLl(val) {
				this.page = val
				this.userSelect()
			},
			handleSizeChangeYz(val) {
				this.limit = val
				this.fangkeSelect()
			},
			handleCurrentChangeYz(val) {
				this.page = val
				this.fangkeSelect()
			},

			handleSizeChangeZz(val) {
				this.limit = val
				this.sendSelect()
			},
			handleCurrentChangeZz(val) {
				this.page = val
				this.sendSelect()
			},
			handleSizeChange6(val) {
				this.limit = val
				this.jiedanSelect()
			},
			handleCurrentChange6(val) {
				this.page = val
				this.jiedanSelect()
			},

			handleSizeChange7(val) {
				this.limit = val
				this.feiSelect()
			},
			handleCurrentChange7(val) {
				this.page = val
				this.feiSelect()
			},
			handleSizeChangeFs(val) {
				this.limit = val
				this.fensiSelect()
			},
			handleCurrentChangeFs(val) {
				this.page = val
				this.fensiSelect()
			},
			handleSizeChangeGz(val) {
				this.limit = val
				this.guanzhuSelect()
			},
			handleCurrentChangeGz(val) {
				this.page = val
				this.guanzhuSelect()
			},
			handleSizeChangeLtjl(val) {
				this.limit = val
				this.liaotianSelect()
			},
			handleCurrentChangeLtjl(val) {
				this.page = val
				this.liaotianSelect()
			},

			handleSizeChangeJp(val) {
				this.limit = val
				this.jingpaiSelect()
			},
			handleCurrentChangeJp(val) {
				this.page = val
				this.jingpaiSelect()
			},
			handleSizeChangeZuanzeng(val) {
				this.limit = val
				this.tuanduiSelect()
			},
			handleCurrentChangeZuanzeng(val) {
				this.page = val
				this.tuanduiSelect()
			},
			handleSizeChangeKtjl(val) {
				this.limit = val
				this.yhqSelect()
			},
			handleCurrentChangeKtjl(val) {
				this.page = val
				this.yhqSelect()
			},


			handleSizeChangeDt(val) {
				this.limit = val
				this.dongtaiSelect()
			},
			handleCurrentChangeDt(val) {
				this.page = val
				this.dongtaiSelect()
			},
			handleSizeChangeDz(val) {
				this.limit = val
				this.dianzanSelect()
			},
			handleCurrentChangeDz(val) {
				this.page = val
				this.dianzanSelect()
			},
			handleSizeChangeFb(val) {
				this.limit = val
				this.takeSelect()
			},
			handleCurrentChangeFb(val) {
				this.page = val
				this.takeSelect()
			},
			handleSizeChange19(val) {
				this.limit = val
				this.dashangSelect()
			},
			handleCurrentChange19(val) {
				this.page = val
				this.dashangSelect()
			},
			handleSizeChangeTd(val) {
				this.limit = val
				this.tuanduiSelect1()
			},
			handleCurrentChangeTd(val) {
				this.page = val
				this.tuanduiSelect1()
			},
			timeDate2() {
				this.rechargeSelect()
			},
			// 可提现金额
			withdraw(id, val) {
				if (val == 0) {
					this.$message({
						message: '可提现金额为0元',
						type: 'error',
						duration: 1500,
						onClose: () => {
							this.dataSelect()
						}
					})
				} else {
					this.$confirm(`确定要推送提现消息吗?`, '提示', {
						confirmButtonText: '确定',
						cancelButtonText: '取消',
						type: 'warning'
					}).then(() => {
						this.$http({
							url: this.$http.adornUrl(`user/notification/${id}`),
							method: 'post',
							data: this.$http.adornData({})
						}).then(({
							data
						}) => {
							this.$message({
								message: '推送成功',
								type: 'success',
								duration: 1500,
								onClose: () => {
									this.dataSelect()
								}
							})
						})
					}).catch(() => {})
				}

			},
			// 不可提现金额充值
			rechargenone() {
				this.dialogFormVisible1 = true
			},
			StairNoticeTo1() {
				let userId = this.$route.query.userId
				if (this.type == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请选择修改类型',
						type: 'warning'
					})
					return
				}
				if (this.money == '') {
					this.$notify({
						title: '提示',
						duration: 1800,
						message: '请输入金额',
						type: 'warning'
					})
					return
				}
				this.$http({
					url: this.$http.adornUrl(`user/addCannotMoney/${userId}/${this.money}/${this.type}`),
					method: 'post',
					data: this.$http.adornData({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.type = ''
								this.money = ''
								this.dataSelect()
							}
						})
						this.dialogFormVisible1 = false
					} else {
						this.$message.error(data.msg)
					}

				})
			},
			StairNoticeTo2() {
				let userId = this.$route.query.userId
				let datas = {}
				datas.userId = userId
				if(this.indexType==4){
					datas.zhiRate = this.age
				}else if(this.indexType==5){
					datas.feiRate = this.age
				}else{
					datas.age = this.age
				}
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					console.log('data', data)
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dialogFormVisible2 = false
							this.dataSelect()
						}
					})
				})
			},
			// 更改状态
			stateChange(userId) {
				this.$confirm(`确定要更改用户状态吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
						method: 'get',
						data: this.$http.adornData({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			// 修改性别
			sexChange(userId, sex) {
				var sexs = 1;
				if (sex == 1) {
					sexs = 2
				}
				if (sex == 2) {
					sexs = 1
				}
				this.$confirm(`确定要更改用户性别吗?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl('user/updateUserByUserId'),
						method: 'post',
						data: this.$http.adornData({
							'userId': userId,
							'sex': sexs
						})
					}).then(({
						data
					}) => {
						console.log('data', data)
						this.$message({
							message: '修改成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					})
				}).catch(() => {})
			},
			ageChange(userId, age) {
				this.indexType = ''
				this.age = age
				this.dialogFormVisible2 = true
			},
			// 获取数据列表
			dataSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl(`user/${userId}`),
					method: 'get',
					params: this.$http.adornParams({
						// 'userId': userId
					})
				}).then(({
					data
				}) => {
					if (data.code === 0) {
						let returnData = data.data
						this.tablenum = returnData
						this.tablemoney = returnData
						this.tableData = returnData.userEntity
						this.phone = returnData.userEntity.phone
						this.invitationCode = returnData.userEntity.invitationCode
					}
				})
			},
			// 获取团队成员
			teamSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/selectInviteByUserIdLists'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableDatamin = returnData
				})
			},
			// 非直属
			feiSelect() {
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectInvitationCodeByUserIdLists'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'invitationCode': this.invitationCode
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.feiDatamin = returnData
				})
			},
			// 我发起的合同
			userSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				if(this.typeLl == 1){
					var url = 'admin/contract/getUserContract'
				}else{
					var url ='admin/contract/userReceivedContract'
				}
				this.$http({				
					url: this.$http.adornUrl(url),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						// 'type': this.typeLl

					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].orderTaking.homepageImg != '') {
							data.data.list[i].homepageImgs = data.data.list[i].orderTaking.homepageImg.split(',')
						}

					}
					let returnData = data.data
					this.userDataLl = returnData
				})
			},
			// 获取用户提现记录
			withdrawSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('/cash/selectPayDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.withdrawData = returnData
				})
			},
			// 获取钱包明细
			walletSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('Details/queryUserMoneyDetails'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.walletData = returnData
				})
			},
			// 获取充值明细
			rechargeSelect() {
				if (this.endTime == '') {
					this.endTime = this.info.stockDate
				}
				if (this.startTime == '') {
					this.startTime = this.info2.stockDate2
				}
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('cash/selectUserRechargeByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'endTime': this.endTime,
						'startTime': this.startTime,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					if (data.code == 0) {

						let returnData = data.data
						this.rechargeData = returnData
					}
					if (data.code == 500) {
						this.$message({
							message: data.msg,
							type: 'error',
							duration: 2500,
							onClose: () => {
								this.tableDataLoading = false
							}
						})
					}
				})
			},
			// 我的铸造
			sendSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/getMyCastingNftList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'status': '',
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableDataZz = returnData
				})
			},
			// 我的发布
			takeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/ScTableData/getDataList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'dataTitle': '',
						'classId': '',
						'status': '',
						'province': '',
						'city': '',
						'county': '',
						'screen': '',
					})
					
					
					// url: this.$http.adornUrl('admin/nftCollection/getMyReleaseNftList'),
					// method: 'get',
					// params: this.$http.adornParams({
					// 	'page': this.page,
					// 	'limit': this.limit,
					// 	'userId': userId,
					// 	'status': ''
					// })
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].homepageImg) {
							data.data.list[i].homepageImgs = data.data.list[i].homepageImg.split(',')
						}

					}
					let returnData = data.data
					this.takeData = returnData
				})
			},
			// 我的接单
			jiedanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('orders/selectMyTakeOrders'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.jiedanData = returnData
				})
			},
			// 我的买入/卖出 订单数据
			dingdanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/getMyBusinessNftList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': this.types,
						'status': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.dingdanData = returnData
				})
			},
			// 实名认证
			autonymSelect() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('student/selectStudentByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.autonymData = returnData
				})
			},
			// 我收藏的店铺
			InformationSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/scFollow/getMyFollowList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': 2
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					// for (var i in data.data.records) {
					// 	if (data.data.records[i].img) {
					// 		data.data.records[i].imgs = data.data.records[i].img.split(',')
					// 	}
					// }
					let returnData = data.data
					this.userDataCp = returnData
				})
			},
			// 我的竞拍
			jingpaiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/nftBranch/getAuctionList'),
					method: 'get',
					params: this.$http.adornParams({
						'userId': userId,
						'page': this.page,
						'limit': this.limit,
						'type': this.search,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.records) {
						if (data.data.records[i].img) {
							data.data.records[i].imgs = data.data.records[i].img.split(',')
						}
					}
					let returnData = data.data
					this.userDataJp = returnData
				})
			},
			selectJp() {
				this.page = 1
				this.jingpaiSelect()
			},
			// 查询
			phoneSelect() {
				this.page = 1
				this.InformationSelect()
			},
			// 我的印章
			fangkeSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/contractSeal/selectContractSealList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'signType':3,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fangkeData = returnData
				})
			},
			// 我抄送的合同
			fensiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({ 
					url: this.$http.adornUrl('admin/contract/copyReceivedContract'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.fensiData = returnData
				})
			},
			// 我关注的服务
			guanzhuSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/scFollow/getMyFollowList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': 1
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.guanzhuData = returnData
				})
			},
			
			// 关注/取关
			select(row) {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/user/insert?userId=' + userId + '&followUserId=' + row.userId),
					method: 'post',
					params: this.$http.adornParams({})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.fensiSelect()
								this.guanzhuSelect()
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
			// 取消关注、收藏
			selectShop(row,index) {
				var dataId = ''
				var shopId = ''
				if(index==1){
					dataId = row.dataId
					shopId = ''
				}else{
					dataId = ''
					shopId = row.shopId
				}
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/scFollow/updateFollow'),
					method: 'post',
					params: this.$http.adornParams({
						'type':index,
						'dataId':dataId,
						'shopId':shopId,
						'userId':userId
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
								if(index==1){
									this.guanzhuSelect()
								}else{
									this.InformationSelect()
								}
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
			// 我的聊天会话
			liaotianSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('chat/selectChatConversationPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.liaotianData = returnData;
				})
			},
			// 聊天内容
			complainDetails(row) {
				this.chatConversationId = row.chatConversationId
				this.neirongSelect(this.chatConversationId)
				this.dialogFormVisible4 = true
			},
			// 获取聊天内容列表
			neirongSelect(chatConversationId) {
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('chat/selectChatContent'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1,
						'limit': this.limit1,
						'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.huihuaData = returnData
				})
			},
			// 我的常用语
			guanjianciSelect() {

				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('message/page/20/' + this.page + '/' + this.limit),
					method: 'get',
					params: this.$http.adornParams({
						// 'page': this.page,
						// 'limit': this.limit,
						// 'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.guanjianciData = returnData
				})
			},
			// 添加关键词
			add() {
				this.title2 = '添加关键词'
				this.contents = ''
				this.guanjianciId = ''
				this.dialogFormVisible3 = true
			},
			// 修改关键词
			updateGjc(row) {
				this.title2 = '修改关键词'
				this.contents = row.content
				this.guanjianciId = row.id
				this.dialogFormVisible3 = true
			},
			addGuanjianci() {
				var datas = {}
				let userId = this.$route.query.userId
				if (this.guanjianciId == '') {
					var url = 'message/add'
					datas.state = 20
					datas.userId = userId
					datas.content = this.contents
				}
				if (this.guanjianciId != '') {
					var url = 'message/update'
					datas.state = 20
					datas.userId = userId
					datas.content = this.contents
					datas.id = this.guanjianciId
				}
				this.$http({
					url: this.$http.adornUrl(url),
					method: 'post',
					data: this.$http.adornData(datas)
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisible3 = false
								this.guanjianciSelect()
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
			deletes(row) {
				let userId = this.$route.query.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`message/delete/${row.id}`),
						method: 'get',
						params: this.$http.adornParams({})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.guanjianciSelect()
							}
						})
					})
				})
			},
			// 修改自动回复
			msgChange(userId, msg) {
				this.msg = msg
				this.dialogFormVisible5 = true
			},
			// 更改自动回复
			StairNoticeTo3() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'msg': this.msg
					})
				}).then(({
					data
				}) => {
					console.log('data', data)
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dialogFormVisible5 = false
							this.dataSelect()
						}
					})
				})
			},
			// 我的转增/我收到的转增
			tuanduiSelect() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/giftGiftTransferList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': this.types
						// 'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tuanduiData = returnData
				})
			},
			// 等级选择事件
			animeDat(state) {
				this.page = 1
				this.tuanduiSelect1()
			},
			// 我的团队收入
			tuanduiSelect1() {
				let userId = this.$route.query.userId
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('admin/invite/getUserIncomeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': this.types
						// 'chatConversationId': this.chatConversationId
						// 'content': this.content
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.tuanduiData = returnData
				})
			},
			// 我的投诉
			tousuSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('message/selectMessageByUserId'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'state': this.states,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					for (var i in data.data.list) {
						if (data.data.list[i].image) {
							data.data.list[i].image = data.data.list[i].image.split(',')
						}

					}
					let returnData = data.data;
					this.tousuData = returnData;
				})
			},
			// 我的动态
			dongtaiSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/user/selectTalkPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.limit,
						'userId': userId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.dongtaiData = returnData.page;
				})
			},
			// 谈论列表
			talkAbout(row) {
				this.talkId = row.talkId
				this.pinglunSelects(row.talkId)
				this.dialogFormVisible4 = true
			},
			// 获取动态评论数据
			pinglunSelects(talkId) {
				let gambitId = this.$route.query.gambitId
				this.tableDataLoading1 = true
				this.$http({
					url: this.$http.adornUrl('gambit/selectCommentPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'size': this.limit,
						'talkId': talkId
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data
					this.pinglunDatas = returnData
				})
			},
			// 删除动态
			advertdeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteTalkById`),
						method: 'post',
						params: this.$http.adornParams({
							'talkId': row.talkId
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dongtaiSelect()
							}
						})
					})
				})
			},
			// 删除评论
			pldeletes(row) {
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`gambit/deleteCommentById`),
						method: 'post',
						params: this.$http.adornParams({
							'commentId': row.commentId
						})
					}).then(({
						data
					}) => {
						this.$message({
							message: '删除成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.pinglunSelects(this.talkId)
							}
						})
					})
				})
			},
			// 我的喜欢
			dianzanSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/user/getMyLikeList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page - 1,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.dianzanData = returnData;
				})
			},
			// 我的卖出
			pinglunSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/nftCollection/getMyBusinessNftList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
						'type': this.types,
						'status': ''
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.pinglunData = returnData;
				})
			},
			// 我的打赏
			dashangSelect() {
				this.tableDataLoading1 = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('chat/selectChatConversationPage'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page1 - 1,
						'limit': this.limit1,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading1 = false
					let returnData = data.data;
					this.dashangData = returnData;
				})
			},
			// 空投记录
			yhqSelect() {
				this.tableDataLoading = true
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('admin/nftAirdropRecord/getUserReceiveList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'userId': userId,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data;
					this.dashangData = returnData;
				})
			},
			
			ageChangeU(row) {
				this.inviterCodeU = row.inviterCode
				this.avatarU = row.avatar
				this.userNameU = row.userName
				this.phoneU = row.phone
				this.invitationCodeU = row.invitationCode
				this.freeTimesU = row.freeTimes
				this.dialogFormVisibleU = true
			},
			handleAvatarSuccess1(file, fileList) {
				this.avatarU = file.data
			},
			StairNoticeToU() {
				let userId = this.$route.query.userId
				this.$http({
					url: this.$http.adornUrl('user/updateUserByUserId'),
					method: 'post',
					data: this.$http.adornData({
						'userId': userId,
						'inviterCode': this.inviterCodeU,
						'avatar': this.avatarU,
						'userName': this.userNameU,
						'phone': this.phoneU,
						'invitationCode': this.invitationCodeU,
						'freeTimes':this.freeTimesU,
					})
				}).then(({
					data
				}) => {
					console.log('data', data)
					this.$message({
						message: '修改成功',
						type: 'success',
						duration: 1500,
						onClose: () => {
							this.dialogFormVisibleU = false
							this.dataSelect()
						}
					})
				})
			},
			ageChangeYj(age, index) {
				this.indexType = index
				this.age = age
				this.dialogFormVisible2 = true
			},
			// 合同数值
			userselectSz(row) {
				this.userIds = row.dataJson
				this.dialogFormVisibleHtsz = true
				// this.tableDataLoading4 = true
				// this.userClass()
			},
			// 发送列表
			plCompile(row) {
				console.log(row)
				this.pinglunData = row.contractAndUsers
				this.dialogFormVisible10 = true
			},
		},
		mounted() {
			this.dataSelect()
		},
	}
</script>

<style scoped="scoped">
	.detailtabel h2 {
		margin-top: 0;
	}

	.detailtabel .table_main table {
		width: 100%;
	}

	.detailtabel .table_main {
		border: 1px solid #e8e8e8;
	}

	.detailtabel table tr {
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table tr th {
		background-color: #fafafa;
		padding: 16px 24px;
		border-right: 1px solid #e8e8e8;
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table tr td {
		padding: 16px 24px;
		border-right: 1px solid #e8e8e8;
		border-bottom: 1px solid #e8e8e8;
	}

	.detailtabel table th {
		color: rgba(0, 0, 0, .85);
		font-weight: 400;
		font-size: 14px;
		line-height: 1.5;
	}

	.border-rt {
		border-right: none !important;
	}

	.border-bt {
		border-bottom: none !important;
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
		width: 148px;
		height: 148px;
	}
	
	.el-upload:hover {
		border-color: #409EFF;
	}
	
	.avatar-uploader-icon {
		font-size: 28px;
		color: #8c939d;
		width: 148px;
		height: 148px;
		line-height: 148px;
		text-align: center;
	}
	
	.avatar {
		width: 148px;
		height: 148px;
		display: block;
	}
	
	.el-button+.el-button {
		/* margin-left: 0 !important;
		margin-top: 5px !important; */
	}
</style>