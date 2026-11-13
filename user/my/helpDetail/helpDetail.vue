<template>
	<view>
		<view class="helpTitle">{{ resultData.helpWordTitle }}</view>
		<view class="helpCons">
			<up-parse class="helpCon" :content="content"></up-parse>
		</view>
	</view>
</template>

<script setup>
	import {
		reactive,
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app'
	const {
		proxy
	} = getCurrentInstance()
	const helpWordId = ref('');
	const resultData = reactive({});
	const content = ref('');
	onLoad(options => {
		if (options.helpWordId) {
			helpWordId.value = options.helpWordId
			getContent()
		}
	})
	const getContent = () => {
		let data = {
			helpWordId: helpWordId.value,
		}
		proxy.$http.get("/app/help/selectHelpWordDetails", data).then(res => {
			if (res.code == 0) {
				uni.setNavigationBarTitle({
					title: res.data.helpWordTitle
				});
				Object.assign(resultData, res.data)
				content.value = res.data.helpWordContent.replace(new RegExp("img", "g"),
					'img style="width:100%;height:auto;"')
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		});

	};
</script>

<style>
	.helpTitle {
		font-size: 40rpx;
		font-weight: bold;
		margin: 50rpx 30rpx 30rpx;
		color: #333333;
	}

	.helpCons {
		width: 686rpx;
		margin: 0 auto;
	}

	.helpCon {
		font-size: 30rpx;
		color: #333333;
		line-height: 2em;
	}
</style>