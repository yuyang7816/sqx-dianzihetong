package com.sqx.modules.message.controller.app;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.app.annotation.Login;
import com.sqx.modules.app.entity.UserEntity;
import com.sqx.modules.app.service.UserService;
import com.sqx.modules.message.entity.MessageInfo;
import com.sqx.modules.message.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author fang
 * @date 2020/7/13
 */
@RestController
@Api(value = "用户端-消息管理", tags = {"用户端-消息管理"})
@RequestMapping(value = "/app/message")
public class AppMessageController {

    @Autowired
    private MessageService messageService;

    @Autowired
    private UserService userService;

    @Login
    @RequestMapping(value = "/selectMessageByUserId", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectUserRecharge(@ApiParam("页码") int page,
                                     @ApiParam("条数") int limit,
                                     @ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId,
                                     @ApiParam("消息状态 5用户消息 8注销") Integer state) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", page);
        map.put("limit", limit);
        map.put("userId", userId);
        map.put("state", state);
        PageUtils pageUtils = messageService.selectMessageList(map);
        messageService.updateSendState(userId, state);
        return Result.success().put("data", pageUtils);
    }

    @Login
    @RequestMapping(value = "/selectMessageByUserIdLimit1", method = RequestMethod.GET)
    @ApiOperation("查询用户消息")
    @ResponseBody
    public Result selectMessageByUserIdLimit1(@ApiParam("拦截器获取的用户id") @RequestAttribute("userId") Long userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("limit", 1);
        map.put("userId", userId);
        return Result.success().put("data", messageService.selectMessageList(map));
    }

    @Login
    @GetMapping("/selectMessageById")
    @ApiOperation("根据id查详情")
    public Result selectMessageById(@ApiParam("消息id") Long id) {
        return Result.success().put("data", messageService.getById(id));
    }


    @Login
    @PostMapping("/insertMessage")
    @ApiOperation("添加投诉")
    public Result insertMessage(@RequestBody MessageInfo messageInfo,
                                @ApiParam("用户id") @RequestAttribute("userId") Long userId) {
        if ("8".equals(messageInfo.getState())) {
            UserEntity userEntity = userService.getById(userId);
            messageInfo.setTitle(userEntity.getPhone());
            messageInfo.setContent(messageInfo.getContent());
            messageInfo.setState("8");
            messageInfo.setUserId(String.valueOf(userId));
            messageInfo.setUserName(userEntity.getUserName());
            int count = messageService.count(new QueryWrapper<MessageInfo>().eq("user_id", userId).eq("state", 8));
            if (count > 0) {
                return Result.error("注销申请已提交,请勿重复提交");
            }
        }
        messageInfo.setUserId(String.valueOf(userId));
        messageInfo.setIsSee("2");
        messageService.saveBody(messageInfo);
        return Result.success();
    }

    @Login
    @PostMapping("/updateMessage")
    @ApiOperation("修改")
    public Result updateMessage(@RequestBody MessageInfo messageInfo) {
        messageService.update(messageInfo);
        return Result.success();
    }

    @Login
    @PostMapping("/deleteMessage")
    @ApiOperation("删除")
    public Result deleteMessage(@ApiParam("消息id") Long id) {
        messageService.delete(id);
        return Result.success();
    }


}