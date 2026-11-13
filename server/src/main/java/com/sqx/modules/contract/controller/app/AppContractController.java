package com.sqx.modules.contract.controller.app;

import com.itextpdf.text.DocumentException;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.dao.UserDao;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.contract.dao.ContractAndUserDao;
import com.sqx.modules.contract.dao.ContractDao;
import com.sqx.modules.contract.dao.ContractTemplateDao;
import com.sqx.modules.contract.entity.Contract;
import com.sqx.modules.contract.entity.ContractAndUser;
import com.sqx.modules.contract.entity.ContractTemplate;
import com.sqx.modules.contract.entity.GeneratePdfRequest;
import com.sqx.modules.contract.service.ContractTemplateService;
import com.sqx.modules.contract.vo.ContractSignVO;
import com.sqx.modules.contract.vo.ContractUserVO;
import com.sqx.modules.contract.vo.ContractVO;
import com.sqx.modules.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.DocumentConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;


/**
 * @description：TODO
 * @author： qiao
 * @create： 2024/8/9 18:32
 */
@RestController
@RequestMapping(value = "/app/contract")
@Api(value = "用户端-合同模板", tags = {"用户端-合同模板"})
@Slf4j
public class AppContractController {

    private final DocxToPdfConverter docxToPdfConverter;

    public AppContractController(DocxToPdfConverter docxToPdfConverter) {
        this.docxToPdfConverter = docxToPdfConverter;
    }

    @Autowired
    private UploadUrl uploadUrl;

    @Autowired
    private ContractTemplateService contractTemplateService;

    @Autowired
    private ContractTemplateDao contractTemplateDao;

    @Autowired
    private ContractAndUserDao contractAndUserDao;

    @Autowired
    private ContractDao contractDao;

    @Autowired
    private UserDao userDao;


    @Login
    @GetMapping("/getContractTemplateList")
    @ApiOperation(value = "用户端获取合同模板列表")
    public Result getContractTemplateList(@ApiParam("页码") Integer page,
                                          @ApiParam("条数") Integer limit,
                                          ContractTemplate contractTemplate,
                                          @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
//        contractTemplate.setUserId(userId);
        return contractTemplateService.getContractTemplateList(page, limit, contractTemplate, userId);
    }

    @Login
    @PostMapping("updateContract")
    @ApiOperation(value = "用户端签署并上传生成的图片修改合同")
    public Result updateContract(@RequestBody ContractVO contractVO,
                                 @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.updateContract(contractVO, userId);
    }

    @Login
    @PostMapping("modifyContract")
    @ApiOperation(value = "用户端签署并上传生成的图片修改合同")
    public Result modifyContract(@RequestBody ContractVO contractVO,
                                 @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        if (userId != null) {
            UserEntity user = userDao.selectById(userId);
            if (user == null) {
                return Result.error("用户不存在,请重新登录！");
            }
        }
        if (contractVO.getPdfImageUrl() != null){
            Contract contract = contractDao.selectById(contractVO.getContractId());
            contract.setFileUrl(contractVO.getPdfImageUrl());
            contractDao.updateById(contract);
        }
        return Result.success();
    }



    @GetMapping("/getContractTemplate")
    @ApiOperation(value = "用户端获取合同模板详情")
    public Result getContractTemplate(@ApiParam("模板id") Long id) {
//        contractTemplate.setUserId(userId);
        return Result.success().put("data", contractTemplateDao.selectById(id));
    }

    @GetMapping(value = "/selectContractTemplateList")
    @ApiOperation(value = "用户端获取合同模板列表的动态额外字段")
    public Result selectContractTemplateList(ContractTemplate contractTemplate) {
        return contractTemplateService.selectContractTemplateList1(contractTemplate);
    }

    @Login
    @PostMapping("/initiatingContract")
    @ApiOperation(value = "用户端根据模板发起合同")
    public Result initiatingContract(@RequestBody ContractVO contractVO,
                                     @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        contractVO.setUserId(userId);
        return contractTemplateService.initiatingContract(contractVO);
    }

    @Login
    @PostMapping("/initiateSingleContract")
    @ApiOperation(value = "用户端发起单个用户签署的合同")
    public Result initiateSingleContract(@RequestBody ContractVO contractVO,
                                         @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        contractVO.setUserId(userId);
        return contractTemplateService.initiateSingleContract(contractVO);
    }

    @Login
    @PostMapping("/selectSignatory")
    @ApiOperation(value = "用户端选择签署或审批人和抄送人")
    public Result selectSignatory(@RequestBody ContractUserVO contractUserVO,
                                  @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        contractUserVO.setUserId(userId);
        return contractTemplateService.selectSignatory(contractUserVO);
    }

    @Login
    @PostMapping("/selectRecipients")
    @ApiOperation(value = "用户端选择抄送人(收件人)")
    public Result selectRecipients(@RequestBody ContractUserVO contractUserVO,
                                   @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.selectRecipients(contractUserVO, userId);
    }


    @Login
    @GetMapping("/selectMySigned")
    @ApiOperation(value = "用户端获取我发起的合同列表")
    public Result selectMySigned(@ApiParam("页码") Integer page,
                                 @ApiParam("条数") Integer limit,
                                 @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.selectMySigned(page, limit, userId);
    }

    @Login
    @GetMapping("/selectMyReceive")
    @ApiOperation(value = "用户端获取我收到的合同列表")
    public Result selectMyReceive(@ApiParam("页码") Integer page,
                                  @ApiParam("条数") Integer limit,
                                  @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.selectMyReceive(page, limit, userId);
    }

    @Login
    @GetMapping("/selectByContractDetail")
    @ApiOperation(value = "用户端获取合同详情")
    public Result selectByContractDetail(@ApiParam("合同id") Long id,
                                         @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.selectByContractDetail(id, userId);
    }

    @Login
    @GetMapping("/selectAllContractList")
    @ApiOperation(value = "用户端获取所有我的合同列表")
    public Result selectAllContractList(@ApiParam("页码") Integer page,
                                        @ApiParam("条数") Integer limit,
                                        @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId,
                                        @ApiParam("合同名称") String contractName,
                                        @ApiParam("0未完成 1已完成 2已审批 3已过期") Integer isComplete) {
        return contractTemplateService.selectAllContractList(page, limit, userId, contractName, isComplete);
    }

    @Login
    @GetMapping("/selectByMyCopyList")
    @ApiOperation(value = "用户端获取抄送给我的合同列表")
    public Result selectByMyCopyList(@ApiParam("页码") Integer page,
                                     @ApiParam("条数") Integer limit,
                                     @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.selectByMyCopyList(page, limit, userId);
    }

    @Login
    @GetMapping("/selectStatistics")
    @ApiOperation(value = "用户端获取我收到的、我发起的、抄送我的、我操作的、他人操作的、已完成合同数量")
    public Result selectStatistics(@ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.selectStatistics(userId);
    }

    @Login
    @GetMapping("/selectMySignature")
    @ApiOperation(value = "用户端获取待我签署、待我审批、草稿箱、我发起的、我收到的合同数量")
    public Result selectMySignature(@ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.selectMySignature(userId);
    }

    @Login
    @PostMapping("/goSignContract")
    @ApiOperation(value = "用户端签署或审批我的合同")
    public Result goSignContract(@RequestBody ContractSignVO contractSignVO,
                                 @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        contractSignVO.setUserId(userId);
        return contractTemplateService.goSignContract(contractSignVO);
    }

    @Login
    @GetMapping("/selectDraftBox")
    @ApiOperation(value = "用户端获取草稿箱合同列表")
    public Result selectDraftBox(@ApiParam("页码") Integer page,
                                 @ApiParam("条数") Integer limit,
                                 @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.selectDraftBox(page, limit, userId);
    }

    @Login
    @PostMapping("/addSingleDraftBox")
    @ApiOperation(value = "用户端将个人签署的合同添加到草稿箱")
    public Result addSingleDraftBox(@RequestBody ContractVO contractVO,
                                    @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.addSingleDraftBox(contractVO, userId);
    }

    @Login
    @PostMapping("/addDraftBox")
    @ApiOperation(value = "用户端将合同添加到草稿箱")
    public Result addDraftBox(@RequestBody ContractVO contractVO,
                              @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.addDraftBox(contractVO, userId);
    }


    @Login
    @PostMapping("/deleteDraftBox")
    @ApiOperation(value = "用户端删除草稿箱合同")
    public Result deleteDraftBox(@RequestBody ContractVO contractVO,
                                 @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.deleteDraftBox(contractVO, userId);
    }

    @Login
    @GetMapping("/selectSignWait")
    @ApiOperation(value = "用户端获取待我签署合同列表")
    public Result selectSignWait(@ApiParam("页码") Integer page,
                                 @ApiParam("条数") Integer limit,
                                 @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.selectSignWait(page, limit, userId);
    }

    @Login
    @GetMapping("/selectMyApproval")
    @ApiOperation(value = "用户端获取我审批的合同列表")
    public Result selectMyApproval(@ApiParam("页码") Integer page,
                                   @ApiParam("条数") Integer limit,
                                   @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.selectMyApproval(page, limit, userId);
    }

    @Login
    @PostMapping("/initiateByWx")
    @ApiOperation(value = "用户端通过微信发起合同")
    public Result initiateByWx(@RequestBody ContractVO contractVO,
                               @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        contractVO.setUserId(userId);
        return contractTemplateService.initiateByWx(contractVO);
    }

    @Login
    @PostMapping("/draftBoxReceiving")
    @ApiOperation(value = "用户端将合同从草稿箱重新发起合同")
    public Result draftBoxReceiving(@RequestBody ContractVO contractVO,
                                    @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        contractVO.setUserId(userId);
        return contractTemplateService.draftBoxReceiving(contractVO);
    }

    @Login
    @ApiOperation("发起人撤回合同")
    @PostMapping("/retractContract")
    public Result retractContract(@RequestBody ContractVO contractVO,
                                  @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        return contractTemplateService.retractContract(contractVO,userId);
    }

    @Login
    @PostMapping("/generateContract")
    @ApiOperation(value = "用户端获取合同模板列表")
    public Result generateContract(ContractVO contractVO,
                                   @RequestAttribute("userId") Long userId) throws DocumentException, IOException {
        contractVO.setUserId(userId);
        byte[] bytes = contractTemplateService.generatePDF(contractVO);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=contract.pdf");
        return Result.success().put("data", bytes).put("headers", httpHeaders);
    }

    @PostMapping("/generatePDF")
    @ApiOperation(value = "用户端生成PDF")
    public Result generatePDF(@RequestBody GeneratePdfRequest request) throws Exception {
        Path localTemplatePath = null;
        Path filledDocx = null;
        Path pdfPath = null;
        Path imageOutputDir = null;
//        Long contractId = request.getContractId();
//        List<ContractAndUser> list = contractAndUserDao.selectByContractId(contractId);
//        list.forEach(item -> {
//            if (item.getType() == 2) {
//                if (item.getUserName() != null) {
//                    request.getFieldValues().put("ceshiA", item.getUserName());
//                }
//                if (item.getIdCard() != null) {
//                    request.getFieldValues().put("shenfenzhengA", item.getIdCard());
//                }
////                if (item.getUserPhone() != null) {
////                    request.getFieldValues().put("userPhoneA", item.getUserPhone());
////                }
//                if (item.getSignUserName() != null) {
//                    request.getFieldValues().put("ceshiB", item.getSignUserName());
//                }
//                if (item.getIdCardNumber() != null) {
//                    request.getFieldValues().put("shenfenzhengB", item.getIdCardNumber());
//                }
////                if (item.getSignUserPhone() != null) {
////                    request.getFieldValues().put("userPhoneB", item.getSignUserPhone());
////                }
//            } else {
//                if (item.getUserName() != null) {
//                    request.getFieldValues().put("ceshiA", item.getUserName());
//                }
//                if (item.getUserLegalPerson() != null) {
//                    request.getFieldValues().put("shenfenzhengA", item.getUserLegalPerson());
//                }
//                if (item.getSignUserName() != null) {
//                    request.getFieldValues().put("ceshiB", item.getSignUserName());
//                }
//                if (item.getSignLegalPerson() != null) {
//                    request.getFieldValues().put("shenfenzhengB", item.getSignLegalPerson());
//                }
//            }
//        });
        String templatePath = request.getTemplatePath();
        Map<String, String> fieldValues = request.getFieldValues();
        try {
            localTemplatePath = FileDownloadUtil.downloadFileFromUrl(templatePath);
            Map<String, String> placeholderValues = new HashMap<>();
            for (Map.Entry<String, String> entry : fieldValues.entrySet()) {
                String placeholder = "{" + entry.getKey() + "}";
                placeholderValues.put(placeholder, entry.getValue());
            }
            // 填充 Word
            filledDocx = Files.createTempFile("filled_", ".docx");
            WordFiller.fillWordTemplate(localTemplatePath.toString(), filledDocx.toString(), placeholderValues);

            // 转 PDF
            pdfPath = Paths.get(filledDocx.toString().replace(".docx", ".pdf"));
            docxToPdfConverter.convertDocxToPdf(filledDocx.toString(), pdfPath.toString());

            // 返回 PDF
//            FileSystemResource resource = new FileSystemResource(pdfPath.toFile());

            // 构造原始文件名
//            String originalFilename = "contract_" + contractId + ".pdf";
//            String pdfUrl = uploadUrl.uploadFile(pdfPath, originalFilename);

            // 创建图片输出目录
            imageOutputDir = Files.createTempDirectory("pdf_images_");
            System.out.println("准备转换 PDF: " + pdfPath.toAbsolutePath());
            System.out.println("文件存在吗？: " + Files.exists(pdfPath)); // 如果是 false，那就是上游的问题

           // 如果不存在，直接抛出更友好的错误，不要让它进到 PDDocument.load 里
            if (!Files.exists(pdfPath)) {
                throw new FileNotFoundException("PDF 文件不存在，可能LibreOffice转换失败导致。路径: " + pdfPath);
            }

            // 转图片（70 DPI 预览够了应该）
            List<Path> imagePaths = PdfToImageConverter.convertPdfToImages(pdfPath, imageOutputDir, 70);

            if (imagePaths.isEmpty()) {
                throw new RuntimeException("PDF 转图片失败：未生成任何页面");
            }

            List<String> imageUrlList = IntStream.range(0, imagePaths.size())
                    .parallel()
                    .mapToObj(i -> {
                        Path path = imagePaths.get(i);
                        String name = "contract_page_" + (i + 1) + ".png";
                        return uploadUrl.uploadFile(path, name);
                    })
                    .collect(Collectors.toList());
//            List<String> imageUrlList = new ArrayList<>();
//            for (int i = 0; i < imagePaths.size(); i++) {
//                Path imagePath = imagePaths.get(i);
//                String imageName = "contract_page_" + (i + 1) + ".png";
//                String imageUrl = uploadUrl.uploadFile(imagePath, imageName);
//                imageUrlList.add(imageUrl);
//            }


//            // 上传第一页（或全部）到本地/OSS
//            String firstImageUrl = uploadUrl.uploadFile(imagePaths.get(0), "contract_preview.png");

            return Result.success().put("data", imageUrlList);
        } catch (Exception e) {
            log.error("生成PDF或转图片失败", e);
            throw e;
        } finally {
            // 清理所有临时文件
            deleteQuietly(localTemplatePath);
            deleteQuietly(filledDocx);
            deleteQuietly(pdfPath);
            // 清理图片临时目录
            if (imageOutputDir != null && Files.exists(imageOutputDir)) {
                try (Stream<Path> stream = Files.walk(imageOutputDir)) {
                    stream.sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                } catch (IOException e) {
                    log.warn("清理图片临时目录失败: {}", imageOutputDir, e);
                }
            }
        }
    }

    @Autowired
    private DocumentConverter documentConverter;


    @PostMapping("/convertFile")
    @ApiOperation("将文件转换为PDF")
    public Result convertFile(@ApiParam("文件路径") String filePath) throws Exception {
        Path downloadedFile = null;
        Path pdfPath = null;
        Path imageOutputDir = null;
        try {
            // 1. 下载原始文件
            downloadedFile = FileDownloadUtil.downloadFileFromUrl(filePath);
            String fileName = downloadedFile.getFileName().toString().toLowerCase();
            // 2. 统一转为 PDF
            if (fileName.endsWith(".pdf")) {
                pdfPath = downloadedFile;
            } else if (fileName.endsWith(".docx") || fileName.endsWith(".doc") ||
                    fileName.endsWith(".xlsx") || fileName.endsWith(".xls")) {
                pdfPath = Files.createTempFile("converted_", ".pdf");
                // 无参 execute()，因为 converter 已绑定 manager
                documentConverter
                        .convert(downloadedFile.toFile())
                        .to(pdfPath.toFile())
                        .execute(); //  安全！
            } else {
                throw new IllegalArgumentException("不支持的文件格式: " + fileName);
            }
            // 3. PDF 转图片
            imageOutputDir = Files.createTempDirectory("pdf_images_");
            System.out.println("准备转换 PDF: " + pdfPath.toAbsolutePath());
            System.out.println("文件存在吗？: " + Files.exists(pdfPath)); // 如果是 false，那就是上游的问题

            // 如果不存在，直接抛出更友好的错误，不要让它进到 PDDocument.load 里
            if (!Files.exists(pdfPath)) {
                throw new FileNotFoundException("PDF 文件不存在，可能LibreOffice转换失败导致。路径: " + pdfPath);
            }
            List<Path> imagePaths = PdfToImageConverter.convertPdfToImages(pdfPath, imageOutputDir, 70);
            if (imagePaths.isEmpty()) {
                throw new RuntimeException("转换失败：未生成任何图片");
            }
            // 4. 上传图片
            List<String> imageUrlList = IntStream.range(0, imagePaths.size())
                    .parallel()
                    .mapToObj(i -> {
                        Path path = imagePaths.get(i);
                        String name = "contract_page_" + (i + 1) + ".png";
                        return uploadUrl.uploadFile(path, name);
                    })
                    .collect(Collectors.toList());
//            List<String> imageUrlList = new ArrayList<>();
//            for (int i = 0; i < imagePaths.size(); i++) {
//                Path imagePath = imagePaths.get(i);
//                String imageName = "page_" + (i + 1) + ".png";
//                String imageUrl = uploadUrl.uploadFile(imagePath, imageName);
//                imageUrlList.add(imageUrl);
//            }
            return Result.success().put("data", imageUrlList);
        } finally {
            // 清理临时文件
            deleteQuietly(downloadedFile);
            if (pdfPath != null && !pdfPath.equals(downloadedFile)) {
                deleteQuietly(pdfPath);
            }
            if (imageOutputDir != null && Files.exists(imageOutputDir)) {
                try (Stream<Path> stream = Files.walk(imageOutputDir)) {
                    stream.sorted(Comparator.reverseOrder())
                            .map(Path::toFile)
                            .forEach(File::delete);
                } catch (IOException e) {
                    log.warn("清理图片目录失败", e);
                }
            }
        }
    }

    private void deleteQuietly(Path path) {
        if (path != null) {
            try {
                Files.deleteIfExists(path);
            } catch (IOException ex) {
                log.warn("Failed to delete temp file: {}", path, ex);
            }
        }
    }
}
