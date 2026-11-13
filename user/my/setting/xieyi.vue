<template>
	<view class="xieyi">
		<up-parse :content="content"></up-parse>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue';
	import {
		onLoad
	} from '@dcloudio/uni-app';
	const {
		proxy
	} = getCurrentInstance();

	const content = ref('');
	const type = ref('');
	onLoad(option => {
		if (option.type) {
			type.value = option.type
			uni.showLoading({
				title: '加载中...'
			})
			getGuize()
		}
	});
	const getGuize = () => {
		proxy.$http.getT('/app/common/type/' + type.value).then(res => {
			uni.hideLoading()
			if (res.code === 0) {
				uni.setNavigationBarTitle({
					title: res.data.min
				})
				content.value = res.data.value;
			}
		});
	}
</script>

<style lang="scss" scoped>
	.xieyi {
		line-height: 26px;
		padding: 32upx;
	}
</style>