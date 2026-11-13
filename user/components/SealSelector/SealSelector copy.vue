<!-- components/SealSelector.vue -->
<template>
	<view class="seal-container">
		<!-- 印章预览区域 -->
		<view class="seal-preview flex align-center justify-center">
			<canvas v-if="canvasWidth>0" :style="{width:canvasWidth+'px'}" style="height: 70px;" ref="canvasRef"
				canvas-id="mainCanvas" id="mainCanvas"></canvas>
		</view>
		<!-- 印章样式选择 -->
		<view class="seal-styles">
			<view class="style-title">印章样式</view>
			<view class="style-list flex align-center justify-between">
				<view @click="selectYzClass(index)" class="style-list-i flex align-center justify-center"
					:class="current===index?'style-list-active':''" v-for="(item,index) in sealStyles" :key="index">
					<image :src="item.src" mode=""></image>
				</view>
			</view>
		</view>

		<!-- 操作按钮 -->
		<!-- <view class="action-btns">
			<button @click="generateImage">生成图片</button>
			<button @click="saveToGallery">保存到相册</button>
		</view> -->
	</view>
</template>

<script setup>
	import {
		ref,
		onMounted,
		nextTick,
		reactive
	} from 'vue';

	// 印章样式配置
	const sealStyles = reactive([{
			name: '矩形章(带框)',
			type: 'rectangle',
			hasBorder: true,
			textAlign: 'center',
			src: '/static/image/sealSelector/hbk.png'
		},
		{
			name: '矩形章(不带框)',
			type: 'rectangle',
			hasBorder: false,
			textAlign: 'center',
			src: '/static/image/sealSelector/hmbk.png'
		},
		{
			name: '方形左大字(带框)',
			type: 'square',
			hasBorder: true,
			textAlign: 'left',
			src: '/static/image/sealSelector/sbk.png'
		},
	]);
	const current = ref(0); //当前选中样式
	const emit = defineEmits(['imageGenerated']);
	// 组件属性
	const props = defineProps({
		name: {
			type: String,
			default: ''
		}
	});

	// 组件状态
	const selectedStyle = ref(0);
	const canvasRef = ref(null);

	// 获取 canvas 上下文的工具函数
	const getCanvasContext = (canvasId) => {
		return uni.createCanvasContext(canvasId);
	};
	//切换样式
	const selectYzClass = (index) => {
		current.value = index
		canvasWidth.value = 0
		if (index == 2) { //重新初始化画布大小
			if (props.name.length % 2 === 0) {
				canvasWidth.value = fontSizeWidth.value / 2 * props.name.length / 2 + 40
			} else {
				canvasWidth.value = fontSizeWidth.value / 2 * (props.name.length + 1) / 2 + 40
			}
		} else {
			canvasWidth.value = fontSizeWidth.value * props.name.length + 20
		}
		nextTick(() => {
			setTimeout(() => {
				drawSeal(props.name, sealStyles[index], 'mainCanvas');
			}, 30)
		})
	};
	const canvasWidth = ref(0); //canvas宽度
	const fontSizeWidth = ref(38); //字体宽度
	// 画布绘制方法
	const drawSeal = (name, style, canvasId) => {
		const ctx = getCanvasContext(canvasId);
		const width = canvasWidth.value;
		const height = 70;
		// 清空画布
		ctx.clearRect(0, 0, width, height);

		// 设置字体
		let fontSize = 28;
		if (style.type === 'square' && current.value === 2) {
			fontSize = 14; // 两个字时字体大小减半
		}

		ctx.setFontSize(fontSize);
		ctx.setFillStyle('#e63946'); // 红色

		// 计算文字位置
		let textX, textY;

		if (style.type === 'rectangle') {
			// 矩形章 - 横向排列，一列一个字
			const charWidth = fontSize * 0.8; // 字符宽度
			const spacing = 5; // 字符间距

			// 计算起始位置
			let startX = (width - (name.length * charWidth + (name.length - 1) * spacing)) / 2 - 2;
			let yPosition = height / 2 + 10;

			// 绘制每个字符
			for (let i = 0; i < name.length; i++) {
				const x = startX + i * (charWidth + spacing);
				ctx.fillText(name[i], x, yPosition);
			}

		} else if (style.type === 'square') {
			// 方形章 - 从右到左，一列两个字
			const charWidth = fontSize * 0.8;
			const spacing = 5;
			const rowSpacing = 5; // 列间距

			// 计算总列数
			const totalCols = Math.ceil(name.length / 2);

			// 计算起始位置（从右边开始）
			let startX = width - 27 - charWidth;

			// 绘制每个字符
			for (let i = 0; i < name.length; i++) {
				console.log(name[i])
				// 每两个字共享相同的 x 坐标，从右往左排列
				const colIndex = Math.floor(i / 2); // 当前是第几列
				let x;
				x = startX - colIndex * (charWidth + rowSpacing);
				// 根据奇偶数确定 y 坐标（上下排列）
				let y;

				if ((i + 1) % 2 === 0) { // 偶数 - 下
					y = height / 2 + 15;
				} else { // 奇数 - 上
					y = height / 2 - 2;
				}
				// 设置字体大小：最后一个字用28，其他用14
				let currentFontSize = 14;
				if (i === name.length - 1 && name.length % 2 === 1) {
					currentFontSize = 28;
					x = x / 2 + 5
					y = y + (y / 2 - 5)
				} else {
					if (name.length % 2 === 0) {
						x = x + 2
					} else {
						x = x + 5
					}
				}
				ctx.setFontSize(currentFontSize);
				ctx.fillText(name[i], x, y);
			}
		}
		// 绘制边框
		if (style.hasBorder) {
			ctx.setStrokeStyle('#e63946');
			ctx.setLineWidth(2);

			if (style.type === 'rectangle') {
				ctx.strokeRect(10, 10, width - 20, height - 20);
			} else if (style.type === 'square') {
				ctx.strokeRect(10, 10, width - 20, height - 20);
			}
		}
		// 提交绘制
		ctx.draw();
	};

	// 生成图片
	const generateImage = () => {
		console.log(canvasRef.value, '走生成图片了')
		uni.showModal({
			title: '提示',
			content: '确认生成印章并保存吗？',
			complete: (ret) => {
				if (ret.confirm) {
					uni.showLoading({
						title: '生成印章中...'
					})
					uni.canvasToTempFilePath({
						x: 0,
						y: 0,
						width: canvasWidth.value,
						height: 70,
						destWidth: canvasWidth.value,
						destHeight: 70,
						canvasId: 'mainCanvas',
						success: function(res) {
							// 在H5平台下，tempFilePath 为 base64
							uni.hideLoading()
							console.log(res.tempFilePath)
							let upFile = ''
							// #ifdef H5
							upFile = base64ToBlob(res.tempFilePath, 'image/png')
							// #endif
							// #ifndef H5
							upFile = res.tempFilePath
							// #endif
							emit('imageGenerated', upFile)
						},
						fail() {
							uni.hideLoading();
							uni.showToast({
								title: '生成印章失败'
							})
						}
					})
				}
			}
		})

	};
	const base64ToBlob = (base64Data, contentType = 'image/png', sliceSize = 512) => {
		// 去掉 data:image/png;base64, 前缀
		const base64 = base64Data.split(',')[1];
		const byteCharacters = atob(base64);
		const byteArrays = [];

		for (let offset = 0; offset < byteCharacters.length; offset += sliceSize) {
			const slice = byteCharacters.slice(offset, offset + sliceSize);
			const byteNumbers = new Array(slice.length);
			for (let i = 0; i < slice.length; i++) {
				byteNumbers[i] = slice.charCodeAt(i);
			}
			const byteArray = new Uint8Array(byteNumbers);
			byteArrays.push(byteArray);
		}

		return new Blob(byteArrays, {
			type: contentType
		});
	};
	// 暴露方法
	defineExpose({
		generateImage
	});
	// 组件挂载后初始化
	onMounted(() => {
		// 初始化主画布
		canvasWidth.value = fontSizeWidth.value * props.name.length + 20
		nextTick(() => {
			if (props.name && canvasRef.value) {
				console.log('走了吧？')
				drawSeal(props.name, sealStyles[current.value], 'mainCanvas');
			}
		});
	});
</script>

<style lang="scss" scoped>
	.seal-container {
		/* padding: 30rpx; */
		background: #ffffff;
		border-radius: 24rpx;
	}

	.seal-preview {
		width: 100%;
		height: 250rpx;
		margin-top: 20rpx;
		background-color: #F5F5F5;
	}

	.seal-styles {
		width: 100%;
		margin-top: 20rpx;
	}

	.style-title {
		font-weight: 500;
		font-size: 32rpx;
		color: #333333;
		margin-bottom: 20rpx;
	}

	.style-list {
		.style-list-active {
			border: 1px solid #e63946 !important;
			background-color: rgba(230, 57, 70, 0.2);
		}

		.style-list-i {
			width: 180rpx;
			height: 180rpx;
			padding: 10rpx;
			border-radius: 12rpx;
			border: 1px solid #ffffff;


			image {
				width: 190rpx;
				height: 190rpx;
			}
		}
	}
</style>