<template>
	<div>
		<el-tabs v-model="activeName" @tab-click="handleClick">
			<el-tab-pane label="全部用户" name="first">
				<div style="margin:5px;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:5px;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:5px;display: inline-block;">
					<span>邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请码"
						v-model="inviterCode"></el-input>
				</div>&emsp;&emsp;
				
				<div style="margin:5px;display: inline-block;">
					<span>渠道来源:</span>
					<el-select v-model="platformT" style="width:150px;margin-left: 10px;" @change="select()">
						<el-option v-for="item in homeDataQ" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>&emsp;&emsp;
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">

					<el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="balance" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="isVip" label="是否为会员">
						<template slot-scope="scope">
							<span v-if="scope.row.isVip==1">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="渠道来源"></el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					
					<el-table-column prop="vipEndTime" label="会员到期时间" width="160"></el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="warning" @click="zengsongVip(scope.row)" :disabled="!isAuth('userList:updateVip')" >修改会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)" :disabled="!isAuth('userList:updateVip')" v-if="scope.row.isVip==1" plain>取消会员</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="普通用户" name="second">
				<div style="margin:2% 0;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:5px;display: inline-block;">
					<span>邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请码"
						v-model="inviterCode"></el-input>
				</div>&emsp;&emsp;
				
				<div style="margin:5px;display: inline-block;">
					<span>渠道来源:</span>
					<el-select v-model="platformT" style="width:150px;margin-left: 10px;" @change="select()">
						<el-option v-for="item in homeDataQ" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>&emsp;&emsp;
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">

					<el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="balance" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="isAuthentication" label="是否为创作者">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==2">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="渠道来源"></el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="warning" @click="zengsongVip(scope.row)" :disabled="!isAuth('userList:updateVip')" >修改会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)" :disabled="!isAuth('userList:updateVip')" v-if="scope.row.isVip==1" plain>取消会员</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="会员用户" name="third">
				<div style="margin:2% 0;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:5px;display: inline-block;">
					<span>邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请码"
						v-model="inviterCode"></el-input>
				</div>&emsp;&emsp;
				
				<div style="margin:5px;display: inline-block;">
					<span>渠道来源:</span>
					<el-select v-model="platformT" style="width:150px;margin-left: 10px;" @change="select()">
						<el-option v-for="item in homeDataQ" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>&emsp;&emsp;
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">

					<el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="balance" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="isAuthentication" label="是否为创作者">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==2">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="渠道来源"></el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="warning" @click="zengsongVip(scope.row)" :disabled="!isAuth('userList:updateVip')" >修改会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)" :disabled="!isAuth('userList:updateVip')" v-if="scope.row.isVip==1" plain>取消会员</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<el-tab-pane label="店铺用户" name="fore">
				<div style="margin:2% 0;display: inline-block;">
					<span>用户昵称:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入用户昵称"
						v-model="userName"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:2% 0;display: inline-block;">
					<span>手机号:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入手机号"
						v-model="phone"></el-input>
				</div>&emsp;&emsp;
				<div style="margin:5px;display: inline-block;">
					<span>邀请码:</span>
					<el-input style="width: 150px;" @keydown.enter.native="select" clearable placeholder="请输入邀请码"
						v-model="inviterCode"></el-input>
				</div>&emsp;&emsp;
				
				<div style="margin:5px;display: inline-block;">
					<span>渠道来源:</span>
					<el-select v-model="platformT" style="width:150px;margin-left: 10px;" @change="select()">
						<el-option v-for="item in homeDataQ" :key="item.value" :label="item.label" :value="item.value">
						</el-option>
					</el-select>&nbsp;&nbsp;&nbsp;
				</div>&emsp;&emsp;
				<div style="display: inline-block;">
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="select">查询
					</el-button>
					<el-button style="margin-left:15px;" size="mini" type="primary" icon="document" @click="cleans">重置
					</el-button>
					<el-button style="margin-left:15px;" :disabled="!isAuth('user:add')" size="mini" type="primary" icon="document"
						@click="titles='新增';dialogVisible=true">新增
					</el-button>
				</div>
				<el-table v-loading="tableDataLoading" :data="tableData.list">

					<el-table-column prop="userId" label="编号" fixed width="80"></el-table-column>
					<el-table-column fixed prop="userName" label="昵称" width="150">
						<template slot-scope="scope">
							<span style="color: #f56c6c;">{{ scope.row.userName ? scope.row.userName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="sex" label="性别">
						<template slot-scope="scope">
							<span v-if="scope.row.sex==1">男</span>
							<span v-else-if="scope.row.sex==2">女</span>
							<span v-else>未设置</span>
						</template>
					</el-table-column>
					<el-table-column label="图像">
						<template slot-scope="scope">
							<img v-if="scope.row.avatar==null" src="~@/assets/img/avatar.png" alt="" width="40"
								height="40">
							<img v-else :src="scope.row.avatar" alt="" width="40" height="40">
						</template>
					</el-table-column>
					<el-table-column prop="phone" label="手机号">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;" @click="updates(scope.row)">
								{{ scope.row.phone ? scope.row.phone : '未绑定' }}
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="balance" label="当前余额" width="80"></el-table-column>
					<el-table-column prop="isAuthentication" label="是否为创作者">
						<template slot-scope="scope">
							<span v-if="scope.row.isAuthentication==2">是</span>
							<span v-else>否</span>
						</template>
					</el-table-column>
					<el-table-column prop="platform" label="渠道来源"></el-table-column>
					<el-table-column prop="invitationCode" label="邀请码"></el-table-column>
					<el-table-column label="邀请人邀请码">
						<template slot-scope="scope">
							<span style="color: #4f9dec;cursor: pointer;"
								@click="updates2(scope.row)">{{ scope.row.inviterCode ? scope.row.inviterCode : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiRate" label="一级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.zhiRate?scope.row.zhiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'zhiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="feiRate" label="二级佣金比例" width="150">
						<template slot-scope="scope">
							<span>{{scope.row.feiRate?scope.row.feiRate:'0'}}</span>
							<el-button size="mini" :disabled="!isAuth('userList:updatebl')"
								style="color: #4f9dec;background: #fff;border: none;"
								@click="xiugai(scope.row,'feiRate')">修改</el-button>
							</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBaoName" label="支付宝名称">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBaoName ? scope.row.zhiFuBaoName : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="zhiFuBao" label="支付宝账号">
						<template slot-scope="scope">
							<span>{{ scope.row.zhiFuBao ? scope.row.zhiFuBao : '未绑定' }}</span>
						</template>
					</el-table-column>
					<el-table-column prop="wxImg" label="收款二维码" width="150">
						<template slot-scope="scope">
							<div v-if="scope.row.wxImg == null || scope.row.wxImg == ''">
								暂无收款二维码
							</div>
							<div v-else style="display:flex;flex-wrap: wrap;">
								<div style="margin: 2px;">
									<el-popover placement="top-start" title="" trigger="hover">
										<img style="width: 50px; height: 50px" :src="scope.row.wxImg" alt=""
											slot="reference">
										<img style="width: 300px; height: 300px" :src="scope.row.wxImg" alt="">
									</el-popover>
								</div>
							</div>
						</template>
					</el-table-column>
					<el-table-column prop="createTime" label="创建时间" width="160"></el-table-column>
					<el-table-column prop="state " label="状态">
						<template slot-scope="scope">
							<el-switch v-model="scope.row.status" @change="change(scope.row.status,scope.row.userId)"
								:active-value="openValue" :disabled="!isAuth('userList:delete')"
								:inactive-value="closeValue" active-color="#13ce66" inactive-color="#ff4949">
							</el-switch>
						</template>
					</el-table-column>
					<el-table-column fixed="right" label="操作" width="120">
						<template slot-scope="scope">
							<el-button size="mini" type="primary" @click="updates(scope.row)">用户详情</el-button>
							<el-button size="mini" type="warning" @click="zengsongVip(scope.row)" :disabled="!isAuth('userList:updateVip')" >修改会员</el-button>
							<el-button size="mini" type="warning" @click="quxiaoVip(scope.row)" :disabled="!isAuth('userList:updateVip')" v-if="scope.row.isVip==1" plain>取消会员</el-button>
							<el-button size="mini" type="danger" :disabled="!isAuth('userList:delete')"
								@click="deleteuser(scope.row)">删除用户</el-button>
							<!-- <el-button style="margin-left:15px;" :disabled="!isAuth('user:updata')" size="mini" type="primary" icon="document"
								@click="uploadUser(scope.row)">修改用户
							</el-button> -->
						</template>
					</el-table-column>
				</el-table>
				<div style="text-align: center;margin-top: 10px;">
					<el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
						:page-sizes="[5, 10, 15, 20]" :page-size="limit" :current-page="page"
						layout="total,sizes, prev, pager, next,jumper" :total="tableData.totalCount">
					</el-pagination>
				</div>
			</el-tab-pane>
			<!-- 修改比例 -->
			<el-dialog title="修改佣金比例" :visible.sync="dialogFormVisible2" center>
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">佣金比例：</span>
					<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
						placeholder="请输入佣金比例"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisible2 = false">取 消</el-button>
					<el-button type="primary" @click="StairNoticeTo2()">确 定</el-button>
				</div>
			</el-dialog>
			<!-- 新增平台用户 -->
			<el-dialog :title="titles" :visible.sync="dialogVisible" width="60%" @close="close">
				<el-form :model="userinfo" ref="dynamicValidateForm" label-width="100px" class="demo-dynamic">
					<el-form-item prop="userName" label="用户名"
						:rules="[{ required: true, message: '请输入用户名', trigger: 'blur' }]">
						<el-input v-model="userinfo.userName" placeholder="请输入用户名"></el-input>
					</el-form-item>
					<el-form-item prop="nickName" label="作者昵称"
						:rules="[{ required: true, message: '请输入作者昵称', trigger: 'blur' }]">
						<el-input v-model="userinfo.nickName" placeholder="请输入作者昵称"></el-input>
					</el-form-item>
					<el-form-item prop="avatar" label="头像"
						:rules="[{ required: true, message: '请上传头像', trigger: 'blur' }]">
						<el-upload class="avatar-uploader" :action="$http.adornUrl(`/alioss/upload`)"
							:show-file-list="false" :on-success="handleAvatarSuccess">
							<img v-if="userinfo.avatar" :src="userinfo.avatar" class="avatar">
							<i v-else class="el-icon-plus avatar-uploader-icon"></i>
						</el-upload>
					</el-form-item>
					<el-form-item prop="sex" label="性别" :rules="[{ required: true, message: '请选择性别', trigger: 'blur'}]">
						<el-radio-group v-model="userinfo.sex">
							<el-radio :label="1">男</el-radio>
							<el-radio :label="2">女</el-radio>
						</el-radio-group>
					</el-form-item>
					<el-form-item prop="age" label="年龄"
						:rules="[{ required: true, message: '请输入年龄', trigger: 'blur' }]">
						<el-input v-model="userinfo.age" placeholder="请输入年龄"></el-input>
					</el-form-item>
					<el-form-item prop="phone" label="手机号"
						:rules="[{ required: true, message: '请输入手机号', trigger: 'blur' },{ pattern: /^1[3456789]\d{9}$/, message: '请输入正确手机号码', trigger: 'blur' }]">
						<el-input v-model="userinfo.phone" placeholder="请输入手机号"></el-input>
					</el-form-item>
					<el-form-item prop="password" label="密码">
						<el-input v-model="userinfo.password" show-password placeholder="请输入密码"></el-input>
					</el-form-item>
					<el-form-item prop="sampleReelsUrl" label="作品集地址"
						:rules="[{ required: true, message: '请输入作品集地址', trigger: 'blur' }]">
						<el-input v-model="userinfo.sampleReelsUrl" placeholder="请输入作品集地址"></el-input>
					</el-form-item>
					<el-form-item prop="email" label="邮箱"
						:rules="[{ required: true, message: '请输入邮箱', trigger: 'blur' },{ type: 'email', message: '请输入正确的邮箱地址', trigger: ['blur', 'change']}]">
						<el-input v-model="userinfo.email" placeholder="请输入邮箱"></el-input>
					</el-form-item>
					<el-form-item prop="introduction" label="个人简介"
						:rules="[{ required: true, message: '请输入个人简介', trigger: 'blur' }]">
						<el-input v-model="userinfo.introduction" type="textarea" placeholder="请输入个人简介"></el-input>
					</el-form-item>
				</el-form>
				<span slot="footer" class="dialog-footer">
					<el-button @click="dialogVisible = false">取 消</el-button>
					<el-button type="primary" @click="submitForm('dynamicValidateForm')">确 定</el-button>
				</span>
			</el-dialog>
			<!-- 修改会员 -->
			<el-dialog title="修改会员" :visible.sync="dialogFormVisibleZs" center>
				<!-- <div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">类型：</span>
					<el-radio-group v-model="type">
						<el-radio :label="1">添加</el-radio>
						<el-radio :label="2">减少</el-radio>
					</el-radio-group>
				</div> -->
				<div style="margin-bottom: 10px;">
					<span style="width: 200px;display: inline-block;text-align: right;">修改天数：</span>
					<el-input style="width:50%;" v-model="proportion" type="number" :min="0" :controls="false"
						placeholder="请输入修改天数"></el-input>
				</div>
				<div slot="footer" class="dialog-footer">
					<el-button @click="dialogFormVisibleZs = false">取 消</el-button>
					<el-button type="primary" @click="updateVip()">确 定</el-button>
				</div>
			</el-dialog>
		</el-tabs>
	</div>
</template>

<script>
	export default {
		data() {
			return {
				titles: '',
				dialogVisible: false,
				userinfo: {
					userName: '',
					nickName: '',
					avatar: '',
					sex: 1,
					age: '',
					phone: '',
					password: '',
					typeUser: 1,
					sampleReelsUrl: '',
					email: '',
					introduction: '',
					userId:'',
				},
				typeUser: '',
				openValue: 1,
				closeValue: 2,
				limit: 10,
				page: 1,
				userName: '',
				isAuthentication: '',
				nickNam: '',
				phone: '',
				platform: 'all',
				isRelation: -1,
				isTuan: -1,
				value: '',
				isVip: '',
				payData: [],
				nickName: '',
				grade: '',
				name: '',
				activeName: 'first',
				tableDataLoading: true,
				dialogFormVisible2: false,
				tableData: {},
				campus: '',
				homeData1: [],
				proportion: '',
				texts: '',
				userIdss: '',
				platformT:'',
				homeDataQ:[
					{
						value: '',
						label: '全部'
					}, {
						value: 'H5',
						label: 'H5'
					}, {
						value: 'app',
						label: 'app'
					},{
						value: '小程序',
						label: '小程序'
					},
				],
				userType:'',
				inviterCode:'',
				dialogFormVisibleZs:false,
				isVip:'',
				type:1,
			}
		},
		methods: {
			//修改弹窗赋值
			uploadUser(row){
				this.userinfo = {
					userName: row.userName,
					nickName: row.nickName,
					sex: row.sex,
					age: row.age,
					avatar: row.avatar,
					phone: row.phone,
					userId:row.userId,
					// password: row.password,
					typeUser: 1,
					sampleReelsUrl: row.sampleReelsUrl,
					email: row.email,
					introduction: row.introduction,
				}
				this.titles='修改';
				this.dialogVisible=true
			},
			//上传
			handleAvatarSuccess(res, file) {
				this.userinfo.avatar = res.data;
			},
			close() {
				this.userinfo = {
					userName: '',
					nickName: '',
					sex: 1,
					age: '',
					avatar:'',
					phone: '',
					password: '',
					typeUser: 1,
					sampleReelsUrl: '',
					email: '',
					introduction: '',
				}
			},
			//新增用户
			submitForm(formName) {
				this.$refs[formName].validate((valid) => {
					if (valid) {
						let url = ""
						let from = this.userinfo
						if(this.titles == '新增'){
							url = "/user/addUser"
							delete from.userId
						}else{
							url = "/user/updateTbUser"
							// delete from.password
						}
						this.$http({
							url: this.$http.adornUrl(url),
							method: 'post',
							data: this.$http.adornData(from)
						}).then(({
							data
						}) => {
							if (data && data.code == 0) {
								this.$message({
									message: '操作成功',
									type: 'success',
									duration: 1500,
									onClose: () => {
										this.dialogVisible = false
										this.dataSelect()
									}
								})
							} else {
								this.$message({
									message: data.msg,
									type: 'error',
									duration: 1500,
									onClose: () => {
										this.dialogVisible = false
										this.dataSelect()
									}
								})
							}

						})
					} else {
						console.log('error submit!!');
						return false;
					}
				});
			},
			// tabs切换
			handleClick(tab, event) {
				this.page = 1
				this.limit = 10
				this.phone = ''
				this.inviterCode = ''
				if (tab._props.label == '全部用户') {
					this.userType = ''
					this.isVip = ''
					this.dataSelect()
				}
				if (tab._props.label == '普通用户') {
					this.userType = 1
					this.isVip = 0
					this.dataSelect()
				}
				if (tab._props.label == '会员用户') {
					this.userType = ''
					this.isVip = 1
					this.dataSelect()
				}
				if (tab._props.label == '店铺用户') {
					this.userType = 2
					this.isVip = ''
					this.dataSelect()
				}
				

			},
			// 状态
			change(val, userId) {
				this.$http({
					url: this.$http.adornUrl(`user/updateUserStatusByUserId?userId=${userId}`),
					method: 'get',
					data: this.$http.adornData({})
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
			// 详情跳转
			updates2(row) {
				this.$http({
					url: this.$http.adornUrl('user/selectUserByInvitationCode'),
					method: 'get',
					params: this.$http.adornParams({
						'invitationCode': row.inviterCode
					})
				}).then(({
					data
				}) => {
					if (data.code == 0) {
						this.$router.push({
							path: '/userDetail',
							query: {
								userId: data.data.userEntity.userId
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
			handleSizeChange(val) {
				this.limit = val
				this.dataSelect()
			},
			handleCurrentChange(val) {
				this.page = val
				this.dataSelect()
			},
			// 查询
			select() {
				this.page = 1
				this.dataSelect()
			},
			// 重置
			cleans() {
				this.page = 1
				this.userName = ''
				this.phone = ''
				this.platformT = ''
				this.dataSelect()
			},
			//删除用户
			deleteuser(row) {
				let delid = row.userId
				this.$confirm(`确定删除此条信息?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
					this.$http({
						url: this.$http.adornUrl(`user/deleteUserByUserId/${delid}`),
						method: 'post',
						params: this.$http.adornData({})
					}).then(({
						data
					}) => {
						if (data.code == 0) {
							this.$message({
								message: '删除成功',
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
				})
			},
			// 获取数据列表
			dataSelect() {  
				this.tableDataLoading = true
				this.$http({
					url: this.$http.adornUrl('user/selectUserList'),
					method: 'get',
					params: this.$http.adornParams({
						'page': this.page,
						'limit': this.limit,
						'isVip': this.isVip,
						'userName': this.userName,
						'phone': this.phone,
						'platform':this.platformT,
						'userType':this.userType,
						'inviterCode':this.inviterCode,
					})
				}).then(({
					data
				}) => {
					this.tableDataLoading = false
					let returnData = data.data
					this.tableData = returnData
				})
			},
			StairNoticeTo2() {
				var datas = {}
				if (this.texts == 'rate') {
					datas.rate = this.proportion
				}
				if (this.texts == 'zhiRate') {
					datas.zhiRate = this.proportion
				}
				if (this.texts == 'feiRate') {
					datas.feiRate = this.proportion
				}
				datas.userId = this.userIdss
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
			// 修改会员弹框
			zengsongVip(row){
				this.type = 1
				this.proportion = ''
				this.userIdss = row.userId
				this.dialogFormVisibleZs = true
			},
			// 设置会员
			updateVip(row){
			if (this.proportion === '') {
				this.$notify({
					title: '提示',
					duration: 1800,
					message: '请输入天数',
					type: 'warning'
				});
				return
			}
				this.$http({
					url: this.$http.adornUrl('admin/vipDetails/giveUserVip'),
					method: 'post',
					params: this.$http.adornParams({
						'userId':this.userIdss,
						'num':this.proportion,
						// 'type':this.type
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dialogFormVisibleZs = false
								this.dataSelect()
							}
						})
					}else{
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
			// 取消会员
			quxiaoVip(row){
				this.$confirm(`确定要取消会员?`, '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {
				this.$http({
					url: this.$http.adornUrl('admin/vipDetails/cancelUserVip'),
					method: 'post',
					// data: this.$http.adornData({
					params: this.$http.adornParams({
						'userId':row.userId
					})
				}).then(({
					data
				}) => {
					if(data.code==0){
						this.$message({
							message: '操作成功',
							type: 'success',
							duration: 1500,
							onClose: () => {
								this.dataSelect()
							}
						})
					}else{
				this.$message({
					message: data.msg,
					type: 'warning',
					duration: 1500,
					onClose: () => {
					}
				})
					}
			
				})
				})
			},
			xiugai(row, text) {
				if (text == 'rate') {
					this.proportion = row.rate
				}
				if (text == 'zhiRate') {
					this.proportion = row.zhiRate
				}
				if (text == 'feiRate') {
					this.proportion = row.feiRate
				}
				this.texts = text
				this.userIdss = row.userId
				this.dialogFormVisible2 = true
			},
			// StairNoticeTo2() {
			// 	var datas = {}
			// 	if (this.texts == 'rate') {
			// 		datas.rate = this.proportion
			// 	}
			// 	if (this.texts == 'zhiRate') {
			// 		datas.zhiRate = this.proportion
			// 	}
			// 	if (this.texts == 'feiRate') {
			// 		datas.feiRate = this.proportion
			// 	}
			// 	datas.userId = this.userIdss
			// 	this.$http({
			// 		url: this.$http.adornUrl('user/updateUserByUserId'),
			// 		method: 'post',
			// 		data: this.$http.adornData(datas)
			// 	}).then(({
			// 		data
			// 	}) => {
			// 		console.log('data', data)
			// 		this.$message({
			// 			message: '修改成功',
			// 			type: 'success',
			// 			duration: 1500,
			// 			onClose: () => {
			// 				this.dialogFormVisible2 = false
			// 				this.dataSelect()
			// 			}
			// 		})
			// 	})
			// },
		},
		mounted() {
			this.dataSelect()
		}
	}
</script>

<style scoped="scoped">
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
