<script setup>
	import {
		getCurrentInstance
	} from 'vue';
	const {
		proxy
	} = getCurrentInstance();
	import {
		onLaunch,
		onShow,
		onHide
	} from '@dcloudio/uni-app';

	onLaunch(() => {

		//企业微信客服链接 431
		proxy.$http.get('/app/common/type/431').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('kefu', res.data.value)
			}
		})
		//企业微信客服appid 297
		proxy.$http.get('/app/common/type/297').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('kefuAppId', res.data.value)
			}
		})
		//客服类型  296
		proxy.$http.get('/app/common/type/296').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('SelKeFu', res.data.value)
			}
		})
		//客服电话  298
		proxy.$http.get('/app/common/type/298').then(res => {
			if (res.code == 0 && res.data.value) {
				uni.setStorageSync('kefuPhone', res.data.value)
			}
		})
	});
	onShow(() => {
		console.log('onShow')
	});
	onHide(() => {
		console.log('onHide')
	});
</script>

<style lang="scss">
	/*每个页面公共css */
	//uview-plus公共样式
	@import "@/uni_modules/uview-plus/index.scss";
	//自定义公共样式
	@import "@/static/css/main.css";

	page {
		background-color: #F5F7FF;
	}
</style>