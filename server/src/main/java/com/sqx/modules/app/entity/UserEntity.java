package com.sqx.modules.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 用户
 *
 */
@Data
@ApiModel("用户")
@TableName("tb_user")
public class UserEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@ApiModelProperty("用户id")
	@TableId(type = IdType.AUTO,value = "user_id")
	private Long userId;
	/**
	 * 用户名
	 */
	@ApiModelProperty("用户名")
	@TableField("user_name")
	private String userName;

	/**
	 * 手机号
	 */
	@ApiModelProperty("手机号")
	private String phone;

	/**
	 * 头像
	 */
	@ApiModelProperty("头像")
	private String avatar;

	/**
	 * 性别 1男 2女
	 */
	@ApiModelProperty("性别 1男 2女")
	private Integer sex;

	/**
	 * 年龄
	 */
	@ApiModelProperty("年龄")
	private Integer age;

	/**
	 * 收益
	 */
	@ApiModelProperty("收益")
	private BigDecimal balance;

	/**
	 * 微信小程序openid
	 */
	@ApiModelProperty("微信小程序openid")
	@TableField("open_id")
	private String openId;

	/**
	 * 微信app openid
	 */
	@ApiModelProperty("微信公众号openid")
	@TableField("wx_open_id")
	private String wxOpenId;

	/**
	 * 密码
	 */
	@ApiModelProperty("密码")
	private String password;

	/**
	 * 创建时间
	 */
	@TableField("create_time")
	@ApiModelProperty("创建时间")
	private String createTime;

	/**
	 * 更新时间
	 */
	@TableField("update_time")
	@ApiModelProperty("更新时间")
	private String updateTime;

	/**
	 * 苹果id
	 */
	@TableField("apple_id")
	@ApiModelProperty("苹果id")
	private String appleId;

	/**
	 * 手机类型 1安卓 2ios
	 */
	@TableField("sys_phone")
	@ApiModelProperty("手机类型 1安卓 2ios")
	private Integer sysPhone;

	/**
	 * 状态 1正常 2禁用
	 */
	@ApiModelProperty("状态 1正常 2禁用")
	private Integer status;

	/**
	 * 来源 app 小程序 公众号
	 */
	@ApiModelProperty("来源 app 小程序 公众号")
	private String platform;

	/**
	 * 积分
	 */
	@TableField(exist = false)
	@ApiModelProperty("积分")
	private Integer jifen;

	/**
	 * 邀请码
	 */
	@TableField("invitation_code")
	@ApiModelProperty("邀请码")
	private String invitationCode;

	/**
	 * 邀请人邀请码
	 */
	@TableField("inviter_code")
	@ApiModelProperty("邀请人邀请码")
	private String inviterCode;

	/**
	 * 邀请人的人上级
	 */
	@ApiModelProperty(value = "邀请人的人上级")
	@TableField("inviter_inviter_code")
	private String inviterInviterCode;

	@ApiModelProperty("个推消息id")
	private String clientid;

	/**
	 * 一级佣金
	 */
	@ApiModelProperty("一级佣金")
	private BigDecimal zhiRate;

	/**
	 * 二级佣金
	 */
	@ApiModelProperty("二级佣金")
	private BigDecimal feiRate;

	/**
	 * 支付宝账号
	 */
	@ApiModelProperty("支付宝账号")
	private String zhiFuBao;

	/**
	 * 支付宝名称
	 */
	@ApiModelProperty("支付宝名称")
	private String zhiFuBaoName;

	/**
	 * 是否实名认证 1是 0否
	 */
	@ApiModelProperty("是否实名认证 1是 0否")
	private Integer isAuthentication;

	/**
	 * 企业认证状态 1已认证 2未认证
	 */
	@ApiModelProperty("企业认证状态 1已认证 2未认证")
	private Integer isEnterpriseAuthentication;

	@TableField(exist = false)
	@ApiModelProperty("佣金比例")
	private BigDecimal rate;

	/**
	 * 是否接受消息推送 1是 2否
	 */
	@ApiModelProperty("是否接受消息推送 1是 2否")
	private Integer isSendMsg;

	@ApiModelProperty("微信二维码")
	private String wxImg;

	@ApiModelProperty("消息")
	private String msg;

	@TableField(exist = false)
	@ApiModelProperty("余额")
	private String money;

	@ApiModelProperty("是否会员 1是")
	private Integer isVip;

	@TableField(exist = false)
	@ApiModelProperty("收藏数量")
	private Integer followCount;

	@ApiModelProperty("会员到期时间")
	private String vipEndTime;

	@TableField(exist = false)
	@ApiModelProperty("状态")
	private Integer statuss;

	@TableField(exist = false)
	@ApiModelProperty("是否实名")
	private boolean real;

	/**
	 * 免费发布合同次数：初始值取系统配置
	 */
	@ApiModelProperty("免费发布合同次数：初始值取系统配置")
	private Integer freeTimes;

	/**
	 * 0待审核 1已通过 2已拒绝
	 */
	@ApiModelProperty("0待审核 1已通过 2已拒绝")
	@TableField(exist = false)
	private Integer status1;

}
