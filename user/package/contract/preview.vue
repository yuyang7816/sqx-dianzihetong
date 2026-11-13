<template>
	<view style="padding-bottom: 180rpx;">
		<view class="yulan flex align-center justify-center">
			<view class="yulan-box flex align-center justify-center" :id="'yulan-box'">
				<!-- 合同图片 -->
				<image @click="prewImg(index, contractImg)" :src="item" v-for="(item, index) in contractImg"
					:key="index" mode="widthFix" :id="'page-' + index"></image>

				<!-- 可拖动的印章/签名层 -->
				<view v-for="(seal, sealIndex) in sealList" :key="'seal-' + seal.id" class="draggable-seal" :style="{
						left: seal.x + 'px',
						top: seal.y + 'px'
					}" @touchstart="touchStart($event, sealIndex)" @touchmove="touchMove($event, sealIndex)"
					@touchend="touchEnd($event, sealIndex)">
					<!-- 类型标识 -->
					<view class="seal-type-tag">{{ seal.type === 'sign' ? '签名' : '印章' }}</view>
					<image :src="seal.sealImg" mode="widthFix" :style="{
						width: seal.type === 'sign' ? '300rpx' : '200rpx'
					}"></image>
					<view class="seal-type-bottom">
						<!-- 日期标识 -->
						<view v-if="isShowData" class="seal-day-tag">{{newData}}</view>
						<!-- 页码标识 -->
						<view class="seal-page-tag">{{ seal.pageIndex + 1 }}页</view>
					</view>

					<!-- 删除按钮 -->
					<view class="delete-seal flex align-center justify-center" @click="removeSeal(sealIndex)">×</view>
				</view>
			</view>
		</view>

		<view class="taber">
			<view v-if="sealList.length>0" class="bottomTime flex align-center">
				<up-switch v-model="isShowData"></up-switch>
				<view class="bottomTime-txt">日期</view>
			</view>
			<view class="btn btn-add" @click="addSealMode()">添加印章</view>
			<view class="btn btn-submit" @click="confirmSign()" v-if="sealList.length > 0">确认签署 ({{ sealList.length }})
			</view>
		</view>

		<!-- 签署类型选择（仅个人类型显示） -->
		<up-action-sheet round="24" zIndex="99" :closeOnClickAction="false" :safeAreaInsetBottom="true" :actions="list"
			title="签署类型" :show="show" @close="show = false" @select="select"></up-action-sheet>

		<!-- 隐藏的 Canvas 用于合成 -->
		<!-- #ifdef H5 -->
		<canvas canvas-id="contractCanvas" id="contractCanvas" :style="canvasStyle"
			style="opacity: 0; position: fixed; left: -9999px;"></canvas>
		<!-- #endif -->

		<!-- #ifndef H5 -->
		<canvas type="2d" id="contractCanvas" :style="canvasStyle"
			style="opacity: 0; position: fixed; left: -9999px;"></canvas>
		<!-- #endif -->

		<!-- 手动签名 -->
		<up-popup round="24" zIndex="99" :show="showSign" @close="closeSign" closeable :closeOnClickOverlay="false">
			<view class="showSign">
				<view class="showSign-title">签名</view>
				<view class="showSign-contont">
					<up-signature ref="signature" :width="signWidth" :height="150" bg-color="transparent"
						:show-toolbar="true" @confirm="onConfirm" @error="onError"></up-signature>
				</view>
			</view>
		</up-popup>

		<!-- 侧边骑缝印入口 -->
		<!-- v-if="contontUserId === userId" -->
		<view class="rightSign" @click="openSeamPopup">
			骑缝印{{ seamSeal.sealImg ? '✓' : '' }}
		</view>

		<!-- 骑缝印弹窗 -->
		<up-popup :show="signShow" @close="closeSeam" mode="right">
			<view class="signShow">
				<view class="signShow-title">骑缝印设置</view>

				<!-- 骑缝印拖动区域 -->
				<view class="signShow-contont" ref="seamContainer">

					<!-- 分页指示线 -->
					<view class="signShow-contont-line"
						v-for="(item, index) in contractImg.length<10?contractImg.length:10" :key="index">
						<text>{{ index + 1 }}</text>
					</view>

					<!-- 骑缝印 - 固定显示在中间 -->
					<view v-if="seamSeal.sealImg" class="seam-seal-wrapper">
						<!-- :style="{ top: seamSeal.y + 'px' }" -->
						<image :src="seamSeal.sealImg" mode="widthFix" class="seam-seal-img"></image>
						<view class="seam-delete" @click.stop="removeSeamSeal()">×</view>
					</view>
				</view>

				<view class="signShow-num">
					共{{ contractImg.length }}页
				</view>

				<view class="signShow-btn flex align-center justify-between">
					<view @click="addSeamSeal()" class="signShow-btn-i flex align-center justify-center"
						style="background-color: #07c160;">
						添加印章
					</view>
					<view @click="confirmSeamSeal()" class="signShow-btn-i flex align-center justify-center">
						确认
					</view>
				</view>
			</view>
		</up-popup>
	</view>
</template>

<script setup>
	import {
		timeFormat
	} from '@/uni_modules/uview-plus';
	import {
		ref,
		reactive,
		getCurrentInstance,
		onUnmounted,
		nextTick
	} from 'vue';
	import {
		onLoad,
		onShow,
		onPageScroll
	} from '@dcloudio/uni-app';
	import config from '../../common/config.js';
	const {
		proxy
	} = getCurrentInstance();

	// 原有变量
	const contractImg = reactive([]);
	const contractId = ref('');
	const list = reactive([]);
	const show = ref(false);
	const type = ref(2);
	const canvasStyle = ref({
		width: '1px',
		height: '1px'
	});
	const showSign = ref(false);
	const signImg = ref('');
	const signWidth = ref(uni.upx2px(750));

	// 印章拖动相关
	const sealList = reactive([]);
	const pageRect = ref({
		width: 0,
		height: 0
	});
	const scrollTop = ref(0);
	const screenHeight = ref(uni.getSystemInfoSync().windowHeight);

	// 临时存储签名图片
	const tempSignImg = ref('');

	// ==================== 骑缝印相关变量 ====================
	const signShow = ref(false);
	const seamContainer = ref(null);
	const containerHeight = ref(0);
	const seamSeal = reactive({
		sealImg: '',
		y: 0,
		sealHeight: 0
	});
	const seamStartY = ref(0);
	//当前日期
	const newData = ref();
	//是否添加日期
	const isShowData = ref(false);

	// 印章选择监听函数
	const handleSelectSeal = (data) => {
		console.log('触发')
		const sealUrl = type.value === 1 ? data.companySeal : data.sealUrl
		if (!sealUrl) return

		const centerX = pageRect.value.width / 2 - 50
		const centerY = scrollTop.value + screenHeight.value / 2
		const pageIndex = Math.floor(centerY / pageRect.value.height)

		sealList.push({
			pageIndex: pageIndex,
			x: centerX,
			y: centerY,
			sealImg: sealUrl,
			type: 'seal',
			id: Date.now(),
			isDragging: false,
			startX: 0,
			startY: 0
		})

		uni.showToast({
			title: '印章已添加，可拖动调整位置',
			icon: 'none'
		})
	};

	// ==================== 骑缝印选择监听函数 ====================
	const handleSelectSeamSeal = (data) => {
		const sealUrl = data.companySeal ? data.companySeal : data.sealUrl;
		console.log('骑缝印：选择的印章数据', data, 'URL:', sealUrl);
		if (!sealUrl) return;

		uni.getImageInfo({
			src: sealUrl,
			success: (imgInfo) => {
				seamSeal.sealImg = sealUrl;
				// ✅ 存储宽高比，绘制时再根据合同宽度计算
				seamSeal.sealHeight = imgInfo.height / imgInfo.width;

				// ✅ 不再在这里计算 y，只存储印章信息
				console.log('骑缝印：设置完成', {
					sealImg: seamSeal.sealImg,
					aspectRatio: seamSeal.sealHeight,
					y: seamSeal.y,
					containerHeight: containerHeight.value
				});

				uni.showToast({
					title: '骑缝印已选择',
					icon: 'none'
				});
			},
			fail: (err) => {
				console.error('骑缝印：初始图片加载失败', err);
				uni.showToast({
					title: '印章图片加载失败',
					icon: 'none'
				});
			}
		});
	};

	// 监听页面滚动
	onPageScroll((e) => {
		scrollTop.value = e.scrollTop
	});

	// 接收选择的印章
	onShow(() => {
		// uni.$once("selectSeal", handleSelectSeal)
		// uni.$once("selectSeamSeal", handleSelectSeamSeal);
	});

	// onUnmounted(() => {
	// 	uni.$off("selectSeal", handleSelectSeal)
	// 	uni.$off("selectSeamSeal", handleSelectSeamSeal);
	// });
	const contontUserId = ref(''); //发起人的userid
	const userId = ref('');
	onLoad(option => {
		newData.value = timeFormat(new Date().getTime(), 'yyyy-mm-dd');
		if (option.contontUserId) {
			contontUserId.value = Number(option.contontUserId)
			userId.value = Number(uni.getStorageSync('userId'))
		}

		if (option.contractId) {
			contractId.value = option.contractId
			type.value = Number(option.type)

			if (type.value === 1) {
				list.splice(0, list.length, ...[{
					name: '印章',
					fontSize: '14px',
					id: 2
				}])
			} else {
				list.splice(0, list.length, ...[{
						name: '手签',
						fontSize: '14px',
						id: 1
					},
					{
						name: '印章',
						fontSize: '14px',
						id: 2
					},
				])
			}

			if (option.pdfImageUrl) {
				contractImg.splice(0, contractImg.length, ...option.pdfImageUrl.split(','))
			}
			setTimeout(() => {
				uni.createSelectorQuery().select('#page-0').fields({
					size: true
				}).exec((res) => {
					if (res[0]) {
						pageRect.value = {
							width: res[0].width,
							height: res[0].height
						}
					}
				})
			}, 500)
		}
	});

	// ==================== 骑缝印拖动方法 ====================

	const openSeamPopup = () => {
		signShow.value = true;
		nextTick(() => {
			uni.createSelectorQuery()
				.select('.signShow-contont')
				.fields({
					size: true
				})
				.exec((res) => {
					if (res[0]) {
						containerHeight.value = res[0].height;
						// ✅ 如果有印章，重新计算居中位置
						if (seamSeal.sealImg && seamSeal.sealHeight > 0) {
							const estimatedHeight = containerHeight.value * seamSeal.sealHeight;
							seamSeal.y = (containerHeight.value - estimatedHeight) / 2;
						}
					}
				});
		});
	};

	const seamTouchStart = (e) => {
		if (!seamSeal.sealImg) return;
		seamStartY.value = e.touches[0].clientY - seamSeal.y;
	};

	const seamTouchMove = (e) => {
		if (!seamSeal.sealImg) return;
		const newY = e.touches[0].clientY - seamStartY.value;
		seamSeal.y = Math.max(0, Math.min(newY, containerHeight.value - seamSeal.sealHeight));
	};

	const seamTouchEnd = () => {};

	const closeSeam = () => {
		signShow.value = false;
	};

	const removeSeamSeal = () => {
		uni.showModal({
			title: '提示',
			content: '确定删除骑缝印吗？',
			success: (res) => {
				if (res.confirm) {
					seamSeal.sealImg = '';
					seamSeal.y = 0;
					seamSeal.sealHeight = 0;
				}
			}
		});
	};

	const addSeamSeal = () => {
		// uni.navigateTo({
		// 	url: '/package/seal/index?type=seam'
		// });
		uni.$once("selectSeamSeal", handleSelectSeamSeal);
		uni.navigateTo({
			url: '/package/seal/index?type=' + type.value + '&signType=2'
		})
	};

	const confirmSeamSeal = () => {
		if (!seamSeal.sealImg) {
			uni.showToast({
				title: '请先添加骑缝印',
				icon: 'none'
			});
			return;
		}
		signShow.value = false;
		uni.showToast({
			title: '骑缝印已设置',
			icon: 'success'
		});
	};

	// ==================== 签名相关方法 ====================

	const closeSign = () => {
		showSign.value = false
		tempSignImg.value = ''
	};

	const onError = (e) => {
		console.log(e, '签名导出失败')
		uni.showToast({
			title: '签名失败',
			icon: 'none'
		})
	};

	const onConfirm = (e) => {
		tempSignImg.value = e
		confirmSignTemp();
	};

	const confirmSignTemp = () => {
		if (!tempSignImg.value) {
			uni.showToast({
				title: '请先签名',
				icon: 'none'
			})
			return
		}

		const centerX = pageRect.value.width / 2 - 75
		const centerY = scrollTop.value + screenHeight.value / 2
		const pageIndex = Math.floor(centerY / pageRect.value.height)

		sealList.push({
			pageIndex: pageIndex,
			x: centerX,
			y: centerY,
			sealImg: tempSignImg.value,
			type: 'sign',
			id: Date.now(),
			isDragging: false,
			startX: 0,
			startY: 0
		})

		closeSign()

		uni.showToast({
			title: '签名已添加，可拖动调整位置',
			icon: 'none'
		})
	};

	// ==================== 拖动相关方法 ====================

	const touchStart = (e, sealIndex) => {
		const seal = sealList[sealIndex]
		seal.startX = e.touches[0].clientX - seal.x
		seal.startY = e.touches[0].clientY - seal.y
		seal.isDragging = true
	};

	const touchMove = (e, sealIndex) => {
		const seal = sealList[sealIndex]
		if (!seal.isDragging) return

		const newX = e.touches[0].clientX - seal.startX
		const newY = e.touches[0].clientY - seal.startY

		const maxX = pageRect.value.width - 100
		seal.x = Math.max(0, Math.min(newX, maxX))
		seal.y = Math.max(0, newY)

		updateSealPageIndex(seal)
	};

	const touchEnd = (e, sealIndex) => {
		const seal = sealList[sealIndex]
		seal.isDragging = false
	};

	const updateSealPageIndex = (seal) => {
		if (pageRect.value.height === 0) return
		const pageIndex = Math.floor(seal.y / pageRect.value.height)
		seal.pageIndex = Math.min(pageIndex, contractImg.length - 1)
	};

	const removeSeal = (index) => {
		uni.showModal({
			title: '提示',
			content: '确定删除这个吗？',
			success: (res) => {
				if (res.confirm) {
					sealList.splice(index, 1)
				}
			}
		})
	};

	// ==================== 印章/签名合成方法 ====================

	const downloadImage = (url) => {
		return new Promise((resolve, reject) => {
			// #ifdef H5
			resolve(url)
			// #endif

			// #ifndef H5
			if (!url.startsWith('http')) {
				resolve(url)
				return
			}
			uni.downloadFile({
				url: url,
				success: (res) => {
					if (res.statusCode === 200) {
						resolve(res.tempFilePath)
					} else {
						reject(new Error('下载失败：' + res.statusCode))
					}
				},
				fail: reject
			})
			// #endif
		})
	};

	const drawAllSealsToContract = () => {
		return new Promise((resolve, reject) => {
			const sealsByPage = {}
			sealList.forEach(seal => {
				if (!sealsByPage[seal.pageIndex]) {
					sealsByPage[seal.pageIndex] = []
				}
				sealsByPage[seal.pageIndex].push(seal)
			})

			const processPages = async () => {
				try {
					for (let pageIndex = 0; pageIndex < contractImg.length; pageIndex++) {
						const seals = sealsByPage[pageIndex] || []
						await drawSealsToPage(pageIndex, seals)
					}
					resolve()
				} catch (error) {
					console.error('合成失败:', error)
					reject(error)
				}
			}

			processPages()
		})
	};
	// ==================== 骑缝印绘制函数（最终修复版）====================
	const drawSeamSealToCanvas = (ctx, pageIndex, contractWidth, contractHeight, canvas) => {
		return new Promise((resolve) => {
			if (!seamSeal.sealImg) {
				resolve();
				return;
			}
			const totalPageCount = contractImg.length;
			if (totalPageCount === 0) {
				resolve();
				return;
			}

			uni.downloadFile({
				url: seamSeal.sealImg,
				header: {
					token: uni.getStorageSync('token')
				},
				success: (res) => {
					if (res.statusCode !== 200) {
						resolve();
						return;
					}
					const seamPath = res.tempFilePath;

					uni.getImageInfo({
						src: seamPath,
						success: (seamInfo) => {
							const totalPageCount = contractImg.length;
							const totalDisplayWidth = 200; // 骑缝印总显示宽度（可调）

							const srcWidth = seamInfo.width;
							const srcHeight = seamInfo.height;

							// 总显示高度（保持原图比例）
							const totalDisplayHeight = totalDisplayWidth * (srcHeight /
								srcWidth);

							// 每个切片在原图上的实际宽度
							const srcSliceWidth = srcWidth / totalPageCount;
							const srcSliceX = pageIndex * srcSliceWidth;

							// 每个切片的目标显示尺寸
							const targetSliceWidth = totalDisplayWidth /
								totalPageCount;
							const targetSliceHeight = totalDisplayHeight;

							// 目标 X（右边缘）
							const targetX = contractWidth - targetSliceWidth;

							// 垂直位置（发起人偏上示例）
							const minGap = 100;
							const isInitiator = contontUserId.value === userId.value;
							// let targetY = minGap;
							// if (targetY + targetSliceHeight > contractHeight) {
							// 	targetY = contractHeight - targetSliceHeight;
							// }
							let targetY = 0;
							if (isInitiator) {
								// 发起人：靠上
								targetY = minGap;
							} else {
								// 签署人：靠下
								targetY = contractHeight - targetSliceHeight - minGap;
							}

							if (targetY < 0) targetY = 0;
							// #ifdef H5
							ctx.drawImage(
								seamPath,
								srcSliceX, 0, srcSliceWidth, srcHeight,
								targetX, targetY, targetSliceWidth,
								targetSliceHeight
							);
							console.log('骑缝印：H5 成功绘制第', pageIndex, '页');
							resolve();
							// #endif
							// 不等于h5的时候使用createImage来绘制
							// #ifndef H5
							if (!canvas) {
								resolve();
								return;
							}
							const sealImgObj = canvas.createImage();
							sealImgObj.src = seamPath;
							sealImgObj.onload = () => {
								ctx.drawImage(
									sealImgObj,
									srcSliceX, 0, srcSliceWidth, srcHeight,
									targetX, targetY, targetSliceWidth,
									targetSliceHeight
								);
								resolve();
							};
							sealImgObj.onerror = (err) => {
								console.error('骑缝印图片对象加载失败', err);
								resolve();
							};
							// #endif


						},
						fail: (err) => {
							console.error('骑缝印 getInfo 失败', err);
							resolve();
						}
					});
				},
				fail: (err) => {
					console.error('骑缝印下载失败', err);
					resolve();
				}
			});
		});
	};

	const drawSealsToPage = (pageIndex, seals) => {
		return new Promise((resolve, reject) => {
			const pageUrl = contractImg[pageIndex]

			// #ifdef H5
			const downloadContract = pageUrl.startsWith('http') ?
				new Promise((r) => uni.downloadFile({
					url: pageUrl,
					success: r
				})) :
				Promise.resolve({
					tempFilePath: pageUrl
				})

			downloadContract.then((contractRes) => {
				const contractPath = contractRes.tempFilePath || contractRes.file

				uni.getImageInfo({
					src: contractPath,
					success: (imgInfo) => {
						const contractWidth = imgInfo.width
						const contractHeight = imgInfo.height

						canvasStyle.value = {
							width: contractWidth + 'px',
							height: contractHeight + 'px'
						}

						setTimeout(() => {
							const ctx = uni.createCanvasContext('contractCanvas')
							ctx.drawImage(contractPath, 0, 0, contractWidth,
								contractHeight)

							const scaleX = contractWidth / pageRect.value.width
							const scaleY = contractHeight / pageRect.value.height

							// ✅ 先绘制骑缝印
							drawSeamSealToCanvas(ctx, pageIndex, contractWidth,
								contractHeight, null).then(() => {
								// ✅ 原有逻辑完全不变
								const drawSealSequentially = async (
									index) => {
									if (index >= seals.length) {
										return Promise.resolve()
									}

									const seal = seals[index]
									const sealPath = seal.sealImg

									return new Promise((r) => {
										uni.getImageInfo({
											src: sealPath,
											success: (
												sealInfo
											) => {
												const
													displayWidth =
													seal
													.type ===
													'sign' ?
													300 :
													200
												const
													sealWidthPx =
													uni
													.upx2px(
														displayWidth
													)
												const
													sealHeightPx =
													sealWidthPx *
													(sealInfo
														.height /
														sealInfo
														.width
													)
												const
													relativeY =
													seal
													.y -
													(pageIndex *
														pageRect
														.value
														.height
													)

												// ✅ 原有逻辑：乘以 scaleX/scaleY
												ctx.drawImage(
													sealPath,
													seal
													.x *
													scaleX,
													relativeY *
													scaleY,
													sealWidthPx *
													scaleX,
													sealHeightPx *
													scaleY
												)

												// ✅ 新增：绘制日期
												if (isShowData
													.value &&
													newData
													.value
												) {
													ctx
														.save();
													ctx.fillStyle =
														'#ff4444';
													ctx.font =
														'22px sans-serif';
													ctx.textAlign =
														'center';
													ctx.textBaseline =
														'top';

													const
														sealCenterX =
														(seal
															.x *
															scaleX
														) +
														(sealWidthPx *
															scaleX
														) /
														2;
													const
														sealBottomY =
														(relativeY *
															scaleY
														) +
														(sealHeightPx *
															scaleY
														);
													const
														textY =
														sealBottomY +
														10;

													ctx.fillText(
														newData
														.value,
														sealCenterX,
														textY
													);
													ctx
														.restore();
												}

												r()
											},
											fail: r
										})
									}).then(() =>
										drawSealSequentially(
											index + 1))
								}

								drawSealSequentially(0).then(() => {
									ctx.draw(true, () => {
										setTimeout(() => {
											uni.canvasToTempFilePath({
												x: 0,
												y: 0,
												width: contractWidth,
												height: contractHeight,
												destWidth: contractWidth,
												destHeight: contractHeight,
												canvasId: 'contractCanvas',
												success: (
													canvasRes
												) => {
													contractImg
														.splice(
															pageIndex,
															1,
															canvasRes
															.tempFilePath
														)
													resolve
														()
												},
												fail: reject
											})
										}, 300)
									})
								})
							});
						}, 100)
					},
					fail: reject
				})
			}).catch(reject)
			// #endif

			// #ifndef H5
			downloadImage(pageUrl).then((contractPath) => {
				uni.getImageInfo({
					src: contractPath,
					success: (imgInfo) => {
						const contractWidth = imgInfo.width
						const contractHeight = imgInfo.height

						canvasStyle.value = {
							width: contractWidth + 'px',
							height: contractHeight + 'px'
						}

						uni.createSelectorQuery()
							.select('#contractCanvas')
							.fields({
								node: true,
								size: true
							})
							.exec((res) => {
								if (!res[0] || !res[0].node) {
									reject(new Error('Canvas 节点获取失败'))
									return
								}

								const canvas = res[0].node
								const ctx = canvas.getContext('2d')

								canvas.width = contractWidth
								canvas.height = contractHeight

								const bgImg = canvas.createImage()
								bgImg.src = contractPath
								bgImg.onload = () => {
									ctx.drawImage(bgImg, 0, 0, contractWidth,
										contractHeight)

									// ✅ 先绘制骑缝印
									drawSeamSealToCanvas(ctx, pageIndex,
											contractWidth, contractHeight, canvas)
										.then(
											() => {
												// ✅ 原有逻辑完全不变
												const drawSealSequentially =
													async (index) => {
														if (index >= seals
															.length)
															return Promise
																.resolve()

														const seal = seals[
															index]
														return downloadImage(
																seal.sealImg)
															.then((
																sealPath
															) => {
																return new Promise(
																	(
																		r
																	) => {
																		uni.getImageInfo({
																			src: sealPath,
																			success: (
																				sealInfo
																			) => {
																				const
																					displayWidth =
																					seal
																					.type ===
																					'sign' ?
																					300 :
																					200
																				const
																					sealWidthPx =
																					uni
																					.upx2px(
																						displayWidth
																					)
																				const
																					sealHeightPx =
																					sealWidthPx *
																					(sealInfo
																						.height /
																						sealInfo
																						.width
																					)
																				const
																					relativeY =
																					seal
																					.y -
																					(pageIndex *
																						pageRect
																						.value
																						.height
																					)

																				const
																					sealImg =
																					canvas
																					.createImage()
																				sealImg
																					.src =
																					sealPath
																				sealImg
																					.onload =
																					() => {
																						// ✅ 原有逻辑：乘以缩放比例
																						const
																							scaleW =
																							contractWidth /
																							pageRect
																							.value
																							.width;
																						const
																							scaleH =
																							contractHeight /
																							pageRect
																							.value
																							.height;

																						ctx.drawImage(
																							sealImg,
																							seal
																							.x *
																							scaleW,
																							relativeY *
																							scaleH,
																							sealWidthPx *
																							scaleW,
																							sealHeightPx *
																							scaleH
																						)

																						// ✅ 新增：绘制日期
																						if (isShowData
																							.value &&
																							newData
																							.value
																						) {
																							ctx
																								.save();
																							ctx.fillStyle =
																								'#ff4444';
																							ctx.font =
																								'22px sans-serif';
																							ctx.textAlign =
																								'center';
																							ctx.textBaseline =
																								'top';

																							const
																								sealCenterX =
																								(seal
																									.x *
																									scaleW
																								) +
																								(sealWidthPx *
																									scaleW
																								) /
																								2;
																							const
																								sealBottomY =
																								(relativeY *
																									scaleH
																								) +
																								(sealHeightPx *
																									scaleH
																								);
																							const
																								textY =
																								sealBottomY +
																								10;

																							ctx.fillText(
																								newData
																								.value,
																								sealCenterX,
																								textY
																							);
																							ctx
																								.restore();
																						}

																						r()
																					}
																				sealImg
																					.onerror =
																					() =>
																					r()
																			},
																			fail: () =>
																				r()
																		})
																	})
															}).then(() =>
																drawSealSequentially(
																	index + 1))
													}

												drawSealSequentially(0).then(
													() => {
														uni.canvasToTempFilePath({
															canvas: canvas,
															x: 0,
															y: 0,
															width: contractWidth,
															height: contractHeight,
															destWidth: contractWidth,
															destHeight: contractHeight,
															success: (
																canvasRes
															) => {
																contractImg
																	.splice(
																		pageIndex,
																		1,
																		canvasRes
																		.tempFilePath
																	)
																resolve
																	()
															},
															fail: (
																err
															) => {
																console
																	.error(
																		'canvasToTempFilePath 失败:',
																		err
																	)
																reject
																	(
																		err
																	)
															}
														})
													}).catch(reject)
											});
								}
								bgImg.onerror = (err) => {
									console.error('背景图片加载失败:', err)
									reject(err)
								}
							})
					},
					fail: (err) => {
						console.error('合同图片 getInfo 失败:', err)
						reject(err)
					}
				})
			}).catch(reject)
			// #endif
		})
	};

	// ==================== 业务方法 ====================

	const addSealMode = () => {
		if (type.value === 1) {
			uni.$once("selectSeal", handleSelectSeal)
			uni.navigateTo({
				url: '/package/seal/index?type=' + type.value + '&signType=1'
			})
		} else {
			show.value = true
		}
	};

	const select = (e) => {
		if (e.id === 1) {
			showSign.value = true
			show.value = false
		} else {
			uni.$once("selectSeal", handleSelectSeal)
			uni.navigateTo({
				url: '/package/seal/index?type=' + type.value + '&signType=1'
			})
		}
		show.value = false
	};

	const confirmSign = async () => {
		uni.showModal({
			title: '提示',
			content: `确认签署吗？共 ${sealList.length} 个印章/签名${seamSeal.sealImg ? '，含骑缝印' : ''}`,
			success: async (ret) => {
				if (ret.confirm) {
					uni.showLoading({
						title: '签署中...'
					})
					try {
						await drawAllSealsToContract()
						await submit()
						uni.hideLoading()
						uni.showToast({
							title: '签署成功',
							icon: 'success'
						})
					} catch (error) {
						uni.hideLoading()
						uni.showToast({
							title: '合成失败',
							icon: 'none'
						})
						console.error(error)
					}
				}
			}
		})
	};

	const base64ToBlob = (base64Data, contentType = 'image/png', sliceSize = 512) => {
		try {
			if (!base64Data || !base64Data.includes(',')) {
				throw new Error('无效的 base64 格式')
			}
			let base64 = base64Data.split(',')[1];
			base64 = decodeURIComponent(base64);
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
		} catch (error) {
			console.error('base64ToBlob 错误:', error);
			throw error;
		}
	};

	const uploadSingleImage = (fileSrc, fileName) => {
		return new Promise((resolve, reject) => {
			uni.uploadFile({
				url: config.APIHOST + '/alioss/upload',
				// #ifdef H5
				file: fileSrc,
				filePath: fileName,
				// #endif
				// #ifndef H5
				filePath: fileSrc,
				// #endif
				header: {
					token: uni.getStorageSync('token')
				},
				name: 'file',
				success: (uploadFileRes) => {
					try {
						let url = JSON.parse(uploadFileRes.data);
						if (url.code == 0) {
							resolve(url.data)
						} else {
							reject(url.msg)
						}
					} catch (e) {
						reject(e)
					}
				},
				fail: (err) => reject(err)
			});
		});
	};

	const submit = async () => {
		try {
			const uploadedUrls = []
			for (let i = 0; i < contractImg.length; i++) {
				uni.showLoading({
					title: `上传第 ${i + 1}/${contractImg.length} 页...`
				})
				let fileSrc = ''
				// #ifdef H5
				if (contractImg[i] && contractImg[i].startsWith('data:image')) {
					try {
						let upFile = base64ToBlob(contractImg[i], 'image/png')
						fileSrc = new File([upFile], `sign_${i}.png`, {
							type: 'image/png'
						});
					} catch (e) {
						console.error(`第${i + 1}页 base64 转换失败:`, e);
						fileSrc = contractImg[i]
					}
				} else {
					fileSrc = contractImg[i]
				}
				// #endif
				// #ifndef H5
				fileSrc = contractImg[i]
				// #endif

				const url = await uploadSingleImage(fileSrc, `sign_${i}.png`)
				uploadedUrls.push(url)
			}

			uni.hideLoading()

			let data = {
				contractId: contractId.value,
				pdfImageUrl: uploadedUrls.join(',')
			}

			const res = await proxy.$http.postJson('/app/contract/updateContract', data)
			if (res.code == 0) {
				uni.showToast({
					title: '已签署',
					icon: 'success'
				})
				uni.reLaunch({
					url: '/package/work/detail?id=' + contractId.value
				})
			} else {
				uni.showToast({
					title: res.msg,
					icon: 'none'
				})
			}
		} catch (error) {
			uni.hideLoading()
			console.error(error)
			uni.showToast({
				title: '上传失败',
				icon: 'none'
			})
		}
	};

	const prewImg = (index, imgs) => {
		uni.previewImage({
			urls: imgs,
			current: index
		})
	};
</script>

<style lang="scss">
	.signShow {
		width: 400rpx;
		padding: 20rpx;
		height: 100%;

		.signShow-title {
			text-align: center;
			padding: 20rpx;
			font-size: 32rpx;
			font-weight: bold;
		}

		.signShow-contont {
			width: 400rpx;
			height: 60vh;
			background-color: #f9f9f9;
			border-radius: 16rpx;
			position: relative;
			overflow: hidden;
			border: 2rpx dashed #ddd;
			display: flex;
			align-items: center;
			justify-content: space-between;

			.signShow-contont-line {
				width: 1px;
				height: 100%;
				background-color: #cccccc;
				position: relative;

				text {
					position: absolute;
					right: 10rpx;
					top: 50%;
					transform: translateY(-50%);
					font-size: 20rpx;
					color: #0096FF;
					background: #f9f9f9;
					padding: 0 8rpx;
				}
			}

			.seam-seal-wrapper {
				width: 100%;
				position: absolute;
				left: 50%;
				top: 50%;
				touch-action: none;
				z-index: 10;
				transform: translate(-50%, -50%);

				.seam-seal-img {
					width: 100%;
					pointer-events: none;
					display: block;
				}

				.seam-delete {
					position: absolute;
					top: -20rpx;
					right: 10rpx;
					width: 40rpx;
					height: 40rpx;
					background: #ff4444;
					color: #fff;
					border-radius: 50%;
					text-align: center;
					line-height: 40rpx;
					font-size: 28rpx;
					z-index: 10;
				}
			}
		}

		.signShow-num {
			text-align: center;
			margin-top: 20rpx;
			color: #0096FF;
			font-size: 26rpx;
		}

		.signShow-btn {
			width: 100%;
			margin-top: 40rpx;

			.signShow-btn-i {
				flex: 1;
				height: 70rpx;
				color: #FFFFFF;
				background-color: #0096FF;
				border-radius: 16rpx;
				font-size: 28rpx;
			}

			.signShow-btn-i:nth-of-type(1) {
				margin-right: 20rpx;
			}
		}
	}

	.rightSign {
		writing-mode: vertical-lr;
		border-left: 1px solid #0096FF;
		border-top: 1px solid #0096FF;
		border-bottom: 1px solid #0096FF;
		padding: 10rpx;
		color: #0096FF;
		position: fixed;
		top: 50%;
		right: 0;
		transform: translate(0, -50%);
		border-radius: 10rpx 0 0 10rpx;
		letter-spacing: 4px;
		background: #fff;
		z-index: 98;
	}

	.yulan {
		width: 100%;
		height: auto;
		margin-top: 20rpx;

		.yulan-box {
			flex-direction: column;
			width: 686rpx;
			position: relative;

			image {
				width: 100%;
			}
		}
	}

	.taber {
		background: #FFFFFF;
		padding: 30rpx;
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		z-index: 99;
		display: flex;
		gap: 20rpx;

		.bottomTime-txt {
			font-size: 26rpx;
			margin-left: 10rpx;
		}

		.btn {
			flex: 1;
			height: 88rpx;
			border-radius: 16rpx;
			font-weight: 500;
			font-size: 30rpx;
			color: #FFFFFF;
			display: flex;
			align-items: center;
			justify-content: center;
		}

		.btn-add {
			background: #0096FF;
		}

		.btn-submit {
			background: #07c160;
		}
	}

	.draggable-seal {
		position: absolute;
		z-index: 10;
		touch-action: none;
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;

		image {
			pointer-events: none;
		}

		.seal-type-tag {
			width: fit-content;
			background: rgba(7, 193, 96, 0.9);
			color: #fff;
			padding: 4rpx 16rpx;
			border-radius: 12rpx;
			font-size: 22rpx;
			font-weight: 500;
			white-space: nowrap;
			box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.3);
			z-index: 12;
		}

		.seal-day-tag {
			background: transparent;
			color: #ff4444;
			padding: 4rpx 16rpx;
			border-radius: 12rpx;
			font-size: 22rpx;
			font-weight: 500;
			white-space: nowrap;
			z-index: 12;
		}

		.seal-type-bottom {
			// position: absolute;
			// bottom: -50rpx;
			// left: 50%;
			// transform: translateX(-50%);
			display: flex;
			align-items: center;
			justify-content: center;
			flex-direction: column;

		}

		.seal-page-tag {
			width: fit-content;
			background: rgba(0, 150, 255, 0.9);
			color: #fff;
			padding: 4rpx 16rpx;
			border-radius: 12rpx;
			font-size: 22rpx;
			font-weight: 500;
			white-space: nowrap;
			box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.3);
			z-index: 12;

		}

		.delete-seal {
			position: absolute;
			top: -20rpx;
			right: -20rpx;
			width: 40rpx;
			height: 40rpx;
			background: #ff4444;
			color: #fff;
			border-radius: 50%;
			font-size: 28rpx;
			z-index: 11;
			box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.3);
		}
	}

	.showSign-title {
		text-align: center;
		padding: 30rpx;
		font-size: 30rpx;
		font-weight: bold;
	}

	.showSign-contont {
		width: 100%;
	}
</style>