<template>
	<view style="padding-top: 200rpx;">
		<up-empty :mode="mode" :icon="image" :text="contont">
		</up-empty>
	</view>
</template>

<script setup>
	defineProps({
		//内容
		contont: {
			type: String,
			default: '暂无数据'
		},
		mode: {
			type: String,
			default: 'list'
		},
		//图片
		image: {
			type: String,
			default: '/static/image/public/empty.png'
		}
	})
</script>

<style lang="scss">

</style>