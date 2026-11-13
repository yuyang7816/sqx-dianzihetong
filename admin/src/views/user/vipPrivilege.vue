<template>
  <div>
    <div style="float: right;margin-right:2%;">
      <el-button style='margin: 10px 0;' :disabled="!isAuth('bannerList:add')" size="mini" type="primary"
        icon="document" @click="addNotice">添加会员特权</el-button>
    </div>
    <el-table v-loading="tableDataLoading" :data="tableData.list">
      
      <el-table-column fixed prop="id" label="编号" width="50">
      </el-table-column>
      <el-table-column prop="imageUrl" label="图片">
        <template slot-scope="scope">
          　　<img :src="scope.row.imageUrl" width="60" height="60" />
        </template>
      </el-table-column>
      <el-table-column prop="name" label="特权名称">
      </el-table-column>
	  <el-table-column prop="describes" label="说明">
	  </el-table-column>
	  
	  <el-table-column prop="state" label="是否启用">
	  	<template slot-scope="scope">
	  		<el-switch v-model="scope.row.state" @change="change(scope.row.state,scope.row)" :disabled="!isAuth('bannerList:update')"
	  			:active-value="openValue" :inactive-value="closeValue" active-color="#13ce66"
	  			inactive-color="#ff4949">
	  		</el-switch>
	  	</template>
	  </el-table-column>
      <!-- <el-table-column prop="sort" label="排序">
      </el-table-column> -->
      <el-table-column label="操作" width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" :disabled="!isAuth('bannerList:update')"
            @click="amendBanner(scope.row)">修改
          </el-button>
          <el-button size="mini" type="danger" :disabled="!isAuth('bannerList:delete')" @click="deletes(scope.row)">删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align: center;margin-top: 10px;">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
        :page-sizes="[10, 20, 30, 50, 100]" :page-size="limit" :current-page="page" layout="total,sizes, prev, pager, next,jumper"
        :total="tableData.totalCount">
      </el-pagination>
    </div>
    <el-dialog :title="title" :visible.sync="dialogFormVisible" center>
    	<div style="margin-bottom: 10px;display: flex;">
    		<span style="width: 200px;display: inline-block;text-align: right;">图片：</span>
    		<div style=" width:148px;height:148px;border: 1px dashed #c0ccda;border-radius: 6px;text-align: center;line-height: 148px;">
    			<el-upload
    				class="avatar-uploader"
    				v-model="memberImg"
    				:action="$http.adornUrlUp('alioss/upload')"
    				:show-file-list="false"
    				:on-success="handleAvatarSuccess"
    				>
    				<img v-if="memberImg" :src="memberImg" class="avatar" style="border-radius: 6px;width: 148px;height: 148px;"/>
    				<i v-else class="el-icon-plus avatar-uploader-icon"></i>
    			</el-upload>
    		</div>
    	</div>
    	<div style="margin-bottom: 10px;">
    		<span style="width: 200px;display: inline-block;text-align: right;">名称：</span>
    		<el-input style="width:50%;" v-model="memberName" placeholder="请输入名称"></el-input>
    	</div>
		<div style="margin-bottom: 10px;">
			<span style="width: 200px;display: inline-block;text-align: right;">说明：</span>
			<el-input style="width:50%;" v-model="describes" placeholder="请输入说明"></el-input>
		</div>
		
      <!-- <div style="margin-bottom: 10px;">
      	<span style="width: 200px;display: inline-block;text-align: right;">排序：</span>
      	<el-input style="width:50%;" v-model="sort" placeholder="请输入排序" onkeyup="value=value.replace(/^(0+)|[^\d]+/g,'')"></el-input>
      </div> -->
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="addNoticeTo()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  export default {
    data() {
      return {
        page:1,
        limit:10,
        tableDataLoading: true,
        dialogFormVisible:false,
        tableData: [],
        memberName:'',
        memberImg:'',
        memberId:'',
		describes:'',
        sort:'',
        title:'添加',
		state:'',
		openValue: 1,
		closeValue: 2,
      }
    },
    methods: {
      // 获取数据列表
      dataSelect() {
        this.tableDataLoading = true
        this.$http({
         url: this.$http.adornUrl('banner/selectBannerPage'),
         method: 'get',
         params: this.$http.adornParams({
         	'page':this.page,
         	'limit':this.limit,
         	'state': -1,
         	'classify': 10,
         	'type':''
         })
        }).then(({
          data
        }) => {
          if (data && data.code === 0) {
            console.log('会员列表',data)
            this.tableDataLoading = false
            let returnData = data.data;
            this.tableData = returnData;
          }

        })
      },
      //上传成功
      handleAvatarSuccess(file) {
      	this.memberImg= file.data
      },
      addNotice(){
		  this.memberImg = ''
		  	this.memberId = ''
		  	this.memberName=''
			this.describes = ''
		  	this.sort = ''
		  this.title ='添加'
      	this.dialogFormVisible = true
      },
      handleCurrentChange(val){
        this.page = val
        this.dataSelect()
      },
      handleSizeChange(val){
        this.limit = val
        this.dataSelect()
      },
      // 添加banner图
      addNoticeTo(){
      	if (this.memberImg == '') {
      	    this.$notify({
      	        title: '提示',
      	        duration: 1800,
      	        message: '请上传图片',
      	        type: 'warning'
      	    });
      	    return
      	}
      	if (this.memberName == '') {
      	    this.$notify({
      	        title: '提示',
      	        duration: 1800,
      	        message: '请输入名称',
      	        type: 'warning'
      	    });
      	    return
      	}
		if (this.describes == '') {
		    this.$notify({
		        title: '提示',
		        duration: 1800,
		        message: '请输入说明',
		        type: 'warning'
		    });
		    return
		}
		
      	// this.classify = 1
        if(this.title=='添加'){
          var url = 'banner/insertBanner'
        }else{
          var url = 'banner/updateBannerById'
        }
      	this.$http({
			
			url: this.$http.adornUrl(url),
			method: 'post',
			data: this.$http.adornData({
				'classify': 10,
				'imageUrl': this.memberImg,
				'state': 1,
				'url': '',
				'name': this.memberName,
				'describes': this.describes,
				'id':this.memberId
			})
			
      		// url: this.$http.adornUrl(url),
      		// method: 'post',
      		// data: this.$http.adornData({
      		// 	'memberImg':this.memberImg,
      		// 	'memberId':this.memberId,
      		// 	'memberName':this.memberName,
      		// 	'sort':this.sort
      		// })
      	}).then(({data}) => {
			if(data.code==0){
				this.dialogFormVisible = false
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
      },
      // 修改
      amendBanner(rows){
      	
      	this.dialogFormVisible = true
      	this.id = rows.id
      	this.memberImg = rows.imageUrl
      	this.memberId = rows.id
      	this.sort = rows.sort
      	this.memberName = rows.name
		this.describes = rows.describes
        this.title = '修改'
      },
      //删除一级
      deletes(row) {
        let delid = row.id
        this.$confirm(`确定删除此条信息?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
           url: this.$http.adornUrl(`banner/deleteBannerById?ids=${delid}`),
           method: 'get',
           data: this.$http.adornData({})
          }).then(({
            data
          }) => {
			  if(data.code==0){
				  this.$message({
				    message: '删除成功',
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
        }).catch(() => {})
      },
	  // 是否启用
	  change(val, row) {
	  	this.$http({
	  		url: this.$http.adornUrl(`banner/updateBannerStateById?id=${row.id}`),
	  		method: 'post',
	  		params: this.$http.adornParams({})
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
						this.dataSelect()
					}
				})
			}
	  		
	  	})
	  },
    },
    mounted() {
      this.dataSelect()
    }
  }
</script>

<style>
</style>
