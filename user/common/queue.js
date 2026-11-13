export const getWxAppid = () => {
	// 填写自己的微信小程序 AppId；H5 调试可留空
	return '';
};
export const getInvitation = () => {
	return uni.getStorageSync('publicRelation');
};
export const getAppDownUrl = () => {
	return uni.getStorageSync('appurl');
};
export const publicYuMing = () => {
	// H5 分享 / 下载等场景使用的公网域名，按实际部署填写
	return 'http://localhost:9131';
};
