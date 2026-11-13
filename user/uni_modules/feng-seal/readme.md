# feng-seal 印章生成工具

## 快速使用

```vue
<feng-seal ref="seal" :companyText="上弦文" :sealCode="下弦文" :sealText="印章名称"></feng-seal>
```

生成并返回图片地址

```js
this.$refs.seal.make().then((res) => {
	console.log(res);
});
```

### props参数详情

| 参数        | 类型    | 默认值 | 说明                                                         |
| ----------- | ------- | ------ | ------------------------------------------------------------ |
| size        | string  | 400px  | 印章宽高                                                     |
| companyText | string  |        | 上弦文                                                       |
| sealCode    | string  |        | 下弦文                                                       |
| sealText    | string  |        | 印章名称                                                     |
| color       | string  | red    | 颜色                                                         |
| inCircle    | Boolean | false  | 是否显示印章内圈                                             |
| hidden      | Boolean | false  | 是否隐藏canvas画布                                           |
| imageType   | string  | png    | 返回图片的类型  ，可选类型（png，base64）                    |
| centerImage | string  |        | 默认显示五角星，修改需要传小程序内图片链接，使用uni.download()下载的链接 |

