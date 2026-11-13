package com.sqx.modules.contract.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.itextpdf.text.DocumentException;
import com.sqx.common.utils.Result;
import com.sqx.modules.contract.entity.Contract;
import com.sqx.modules.contract.entity.ContractTemplate;
import com.sqx.modules.contract.vo.ContractSignVO;
import com.sqx.modules.contract.vo.ContractUserVO;
import com.sqx.modules.contract.vo.ContractVO;

import java.io.IOException;

/**
* @author Admin
* @description 针对表【contract_template(合同模板表)】的数据库操作Service
* @createDate 2024-08-08 18:13:51
*/
public interface ContractTemplateService extends IService<ContractTemplate> {

    /**
     * 管理后台添加合同模板
     * @param contractTemplate
     * @return
     */
    Result addContractTemplate(ContractTemplate contractTemplate);

    /**
     * 管理后台删除合同模板
     * @param id
     * @return
     */
    Result deleteContractTemplate(Long id);

    /**
     * 管理后台获取合同模板详情
     * @param id
     * @return
     */
    Result getContractTemplateDetail(Long id);

    /**
     * 用户端或者管理后台获取合同模板列表
     * @param page
     * @param limit
     * @param contractTemplate
     * @return
     */
    Result getContractTemplateList(Integer page, Integer limit, ContractTemplate contractTemplate,Long userId);

    /**
     * 管理后台获取合同模板列表
     * @param page
     * @param limit
     * @param contractTemplate
     * @return
     */
    Result selectContractTemplateList(Integer page, Integer limit, ContractTemplate contractTemplate);

    /**
     * 用户端获取合同模板列表的动态额外字段
     * @param contractTemplate
     * @return
     */
    Result selectContractTemplateList1(ContractTemplate contractTemplate);


    /**
     * 用户端根据模板发起合同
     * @param contractVO
     * @return
     */
    Result initiatingContract(ContractVO contractVO);

    /**
     * 用户端选择签署或审批人和抄送人
     * @param contractUserVO
     * @return
     */
    Result selectSignatory(ContractUserVO contractUserVO);

    /**
     * 用户端获取我发起的合同列表
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    Result selectMySigned(Integer page, Integer limit,Long userId);

    /**
     * 用户端获取我收到的合同列表
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    Result selectMyReceive(Integer page, Integer limit,Long userId);

    /**
     * 用户端获取合同详情
     * @param id
     * @param userId
     * @return
     */
    Result selectByContractDetail(Long id,Long userId);

    /**
     * 用户端获取所有我的合同列表
     * @param page
     * @param limit
     * @param userId
     * @param isComplete
     * @return
     */
    Result selectAllContractList(Integer page, Integer limit,Long userId,String contractName,Integer isComplete);

    /**
     * 用户端获取抄送给我的合同列表
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    Result selectByMyCopyList(Integer page, Integer limit,Long userId);

    /**
     * 用户端获取我收到的、我发起的、抄送我的、我操作的、他人操作的、已完成合同数量
     * @param userId
     * @return
     */
    Result selectStatistics(Long userId);

    /**
     * 用户端获取待我签署、待我审批、草稿箱、我发起的、我收到的合同数量
     * @param userId
     * @return
     */
    Result selectMySignature(Long userId);

    /**
     * 用户端签署或审批我的合同
     * @param contractSignVO
     * @return
     */
    Result goSignContract(ContractSignVO contractSignVO);

    /**
     * 用户端获取草稿箱合同列表
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    Result selectDraftBox(Integer page, Integer limit,Long userId);

    /**
     * 用户端将合同添加到草稿箱
     * @param contractVO
     * @param userId
     * @return
     */
    Result addDraftBox(ContractVO contractVO,Long userId);

    /**
     * 用户端获取待我签署合同列表
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    Result selectSignWait(Integer page, Integer limit,Long userId);

    /**
     * 用户端获取我审批的合同列表
     * @param page
     * @param limit
     * @param userId
     * @return
     */
    Result selectMyApproval(Integer page, Integer limit,Long userId);

    /**
     * 用户端通过微信发起合同
     * @param contractVO
     * @return
     */
    Result initiateByWx(ContractVO contractVO);

    /**
     * 用户端将合同从草稿箱发起
     * @param contractVO
     * @return
     */
    Result draftBoxReceiving(ContractVO contractVO);

    byte[] generatePDF(ContractVO contractVO) throws DocumentException, IOException;

    Result selectAllContractList1(Integer page, Integer limit, Long userId);

    Result userReceivedContract(Integer page, Integer limit, Long userId);

    Result copyReceivedContract(Integer page, Integer limit, Long userId);

    Result getAllContractList(Integer page, Integer limit, Contract contract);

    Result updateContract(ContractVO contractVO, Long userId);

    Result selectRecipients(ContractUserVO contractUserVO, Long userId);

    Result deleteDraftBox(ContractVO contractVO, Long userId);

    Result initiateSingleContract(ContractVO contractVO);

    Result addSingleDraftBox(ContractVO contractVO, Long userId);

    Result retractContract(ContractVO contractVO,Long userId);
}
