package com.sqx.modules.chat.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.modules.chat.entity.ChatContent;

public interface ChatContentService  extends IService<ChatContent> {

    PageUtils selectChatContentPage(Integer page, Integer limit, Long chatConversationId,String content);

    int updateChatContentStatusByUserIdAndChatId(Long userId,Long chatConversationId);

    int selectChatCount(Long userId);

}