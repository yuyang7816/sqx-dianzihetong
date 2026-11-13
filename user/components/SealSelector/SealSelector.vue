<!-- components/SealSelector.vue -->
<template>
	<view class="seal-container">
		<!-- 印章预览区域 -->
		<view class="seal-preview flex align-center justify-center">
			<!-- #ifdef H5 -->
			<canvas v-if="canvasWidth>0" :style="{width:canvasWidth+'px'}" style="height: 70px;" ref="canvasRef"
				canvas-id="mainCanvas" id="mainCanvas"></canvas>
			<!-- #endif -->

			<!-- #ifndef H5 -->
			<canvas :style="{width:canvasWidth+'px', height: '70px'}" ref="canvasRef" type="2d"
				id="mainCanvas"></canvas>
			<!-- #endif -->
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
	</view>
</template>

<script setup>
	import {
		ref,
		onMounted,
		nextTick,
		reactive,
		getCurrentInstance
	} from 'vue';

	const instance = getCurrentInstance();

	const sealStyles = reactive([{
			name: '矩形章 (带框)',
			type: 'rectangle',
			hasBorder: true,
			textAlign: 'center',
			src: '/static/image/sealSelector/hbk.png'
		},
		{
			name: '矩形章 (不带框)',
			type: 'rectangle',
			hasBorder: false,
			textAlign: 'center',
			src: '/static/image/sealSelector/hmbk.png'
		},
		{
			name: '方形左大字 (带框)',
			type: 'square',
			hasBorder: true,
			textAlign: 'left',
			src: '/static/image/sealSelector/sbk.png'
		},
	]);

	const current = ref(0);
	const emit = defineEmits(['imageGenerated']);

	const props = defineProps({
		name: {
			type: String,
			default: ''
		}
	});

	const canvasRef = ref(null);
	const canvasWidth = ref(0);
	const fontSizeWidth = ref(38);
	let mpCanvas = null;

	const selectYzClass = (index) => {
		current.value = index;
		canvasWidth.value = 0;

		if (index == 2) {
			if (props.name.length % 2 === 0) {
				canvasWidth.value = fontSizeWidth.value / 2 * props.name.length / 2 + 40;
			} else {
				canvasWidth.value = fontSizeWidth.value / 2 * (props.name.length + 1) / 2 + 40;
			}
		} else {
			canvasWidth.value = fontSizeWidth.value * props.name.length + 20;
		}

		nextTick(() => {
			setTimeout(() => {
				drawSeal(props.name, sealStyles[index]);
			}, 200);
		});
	};

	// ✅ H5 绘制方法（传统 Canvas API）
	const drawSealH5 = (name, style) => {
		const ctx = uni.createCanvasContext('mainCanvas');
		drawSealContentH5(ctx, name, style, canvasWidth.value, 70);
		ctx.draw();
	};

	// ✅ 小程序绘制方法（Canvas 2D API）
	const drawSealMP = (name, style) => {
		const query = uni.createSelectorQuery().in(instance);
		query.select('#mainCanvas')
			.fields({
				node: true,
				size: true
			})
			.exec((res) => {
				if (res[0] && res[0].node) {
					mpCanvas = res[0].node;
					const ctx = mpCanvas.getContext('2d');

					const dpr = uni.getSystemInfoSync().pixelRatio;
					mpCanvas.width = canvasWidth.value * dpr;
					mpCanvas.height = 70 * dpr;
					ctx.scale(dpr, dpr);

					drawSealContentMP(ctx, name, style, canvasWidth.value, 70);
				} else {
					console.error('Canvas 节点获取失败', res);
				}
			});
	};

	const drawSeal = (name, style) => {
		// #ifdef H5
		drawSealH5(name, style);
		// #endif

		// #ifndef H5
		drawSealMP(name, style);
		// #endif
	};

	// ✅ H5 绘制逻辑（传统 Canvas API）
	const drawSealContentH5 = (ctx, name, style, width, height) => {
		ctx.clearRect(0, 0, width, height);

		let fontSize = 28;
		if (style.type === 'square' && current.value === 2) {
			fontSize = 14;
		}

		ctx.setFontSize(fontSize);
		ctx.setFillStyle('#e63946');

		if (style.type === 'rectangle') {
			const charWidth = fontSize * 0.8;
			const spacing = 5;
			let startX = (width - (name.length * charWidth + (name.length - 1) * spacing)) / 2 - 2;
			let yPosition = height / 2 + 10;

			for (let i = 0; i < name.length; i++) {
				const x = startX + i * (charWidth + spacing);
				ctx.fillText(name[i], x, yPosition);
			}
		} else if (style.type === 'square') {
			const charWidth = fontSize * 0.8;
			const spacing = 5;
			const rowSpacing = 5;
			let startX = width - 27 - charWidth;

			for (let i = 0; i < name.length; i++) {
				const colIndex = Math.floor(i / 2);
				let x = startX - colIndex * (charWidth + rowSpacing);
				let y;

				if ((i + 1) % 2 === 0) {
					y = height / 2 + 15;
				} else {
					y = height / 2 - 2;
				}

				let currentFontSize = 14;
				if (i === name.length - 1 && name.length % 2 === 1) {
					currentFontSize = 28;
					x = x / 2 + 5;
					y = y + (y / 2 - 5);
				} else {
					if (name.length % 2 === 0) {
						x = x + 2;
					} else {
						x = x + 5;
					}
				}
				ctx.setFontSize(currentFontSize);
				ctx.fillText(name[i], x, y);
			}
		}

		if (style.hasBorder) {
			ctx.setStrokeStyle('#e63946');
			ctx.setLineWidth(2);
			ctx.strokeRect(10, 10, width - 20, height - 20);
		}
	};

	// ✅ 小程序绘制逻辑（Canvas 2D API）
	const drawSealContentMP = (ctx, name, style, width, height) => {
		ctx.clearRect(0, 0, width, height);

		let fontSize = 28;
		if (style.type === 'square' && current.value === 2) {
			fontSize = 14;
		}

		// ✅ Canvas 2D 使用 font 属性
		ctx.font = `${fontSize}px sans-serif`;
		ctx.fillStyle = '#e63946';

		if (style.type === 'rectangle') {
			const charWidth = fontSize * 0.8;
			const spacing = 5;
			let startX = (width - (name.length * charWidth + (name.length - 1) * spacing)) / 2 - 2;
			let yPosition = height / 2 + 10;

			for (let i = 0; i < name.length; i++) {
				const x = startX + i * (charWidth + spacing);
				ctx.fillText(name[i], x, yPosition);
			}
		} else if (style.type === 'square') {
			const charWidth = fontSize * 0.8;
			const spacing = 5;
			const rowSpacing = 5;
			let startX = width - 27 - charWidth;

			for (let i = 0; i < name.length; i++) {
				const colIndex = Math.floor(i / 2);
				let x = startX - colIndex * (charWidth + rowSpacing);
				let y;

				if ((i + 1) % 2 === 0) {
					y = height / 2 + 15;
				} else {
					y = height / 2 - 2;
				}

				let currentFontSize = 14;
				if (i === name.length - 1 && name.length % 2 === 1) {
					currentFontSize = 28;
					x = x / 2 + 5;
					y = y + (y / 2 - 5);
				} else {
					if (name.length % 2 === 0) {
						x = x + 2;
					} else {
						x = x + 5;
					}
				}
				// ✅ Canvas 2D 需要重新设置 font
				ctx.font = `${currentFontSize}px sans-serif`;
				ctx.fillText(name[i], x, y);
			}
		}

		if (style.hasBorder) {
			// ✅ Canvas 2D 使用 strokeStyle 和 lineWidth 属性
			ctx.strokeStyle = '#e63946';
			ctx.lineWidth = 2;
			ctx.strokeRect(10, 10, width - 20, height - 20);
		}
	};

	const generateImage = () => {
		uni.showModal({
			title: '提示',
			content: '确认生成印章并保存吗？',
			complete: (ret) => {
				if (ret.confirm) {
					uni.showLoading({
						title: '生成印章中...'
					});

					// #ifdef H5
					uni.canvasToTempFilePath({
						x: 0,
						y: 0,
						width: canvasWidth.value,
						height: 70,
						destWidth: canvasWidth.value,
						destHeight: 70,
						canvasId: 'mainCanvas',
						success: function(res) {
							uni.hideLoading();
							let upFile = base64ToBlob(res.tempFilePath, 'image/png');
							emit('imageGenerated', upFile);
						},
						fail() {
							uni.hideLoading();
							uni.showToast({
								title: '生成印章失败'
							});
						}
					});
					// #endif

					// #ifndef H5
					if (!mpCanvas) {
						uni.hideLoading();
						uni.showToast({
							title: 'Canvas 未初始化'
						});
						return;
					}

					const dpr = uni.getSystemInfoSync().pixelRatio;
					uni.canvasToTempFilePath({
						x: 0,
						y: 0,
						width: canvasWidth.value,
						height: 70,
						destWidth: canvasWidth.value * dpr,
						destHeight: 70 * dpr,
						canvas: mpCanvas,
						success: function(res) {
							uni.hideLoading();
							emit('imageGenerated', res.tempFilePath);
						},
						fail(err) {
							uni.hideLoading();
							console.error('生成印章失败:', err);
							uni.showToast({
								title: '生成印章失败'
							});
						}
					});
					// #endif
				}
			}
		});
	};

	const base64ToBlob = (base64Data, contentType = 'image/png', sliceSize = 512) => {
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

	defineExpose({
		generateImage
	});

	onMounted(() => {
		canvasWidth.value = fontSizeWidth.value * props.name.length + 20;
		nextTick(() => {
			setTimeout(() => {
				console.log(props.name)
				console.log(canvasRef.value)
				if (props.name) {
					console.log('走了吗？')
					// drawSeal(props.name, sealStyles[current.value]);
					selectYzClass(current.value);
				}
			}, 300);
		});
	});
</script>

<style lang="scss" scoped>
	.seal-container {
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