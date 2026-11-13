package com.sqx.modules.contract.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sqx.modules.app.entity.UserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/12 10:23
 */
@Data
@ApiModel("合同用户相关VO")
public class ContractUserVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 合同id
     */
    @ApiModelProperty("合同id")
    private Long contractId;

    /**
     * 发起人id
     */
    @ApiModelProperty("发起人id")
    private Long userId;

    /**
     * 选择签署人或审批人列表
     */
    @ApiModelProperty("选择签署人或审批人列表")
    private Long signedBy;

    /**
     * 签署人列表
     */
    @ApiModelProperty("签署人列表")
    private List<UserEntity> userList;

    /**
     * 合同开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("合同开始时间")
    private Date startTime;

    /**
     * 合同结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty("合同结束时间")
    private Date endTime;

    /**
     * 抄送人id
     */
    @ApiModelProperty(name = "抄送人id")
    private Long copyUser;

    /**
     * 合同类型：1企业  2个人
     */
    @ApiModelProperty(name = "合同类型：1企业  2个人")
    private Integer type;

    /**
     * 收件人姓名
     */
    @ApiModelProperty("收件人姓名")
    private String name;

    /**
     * 收件人手机号
     */
    @ApiModelProperty("收件人手机号")
    private String phone;

}
