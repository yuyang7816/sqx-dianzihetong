package com.sqx.modules.chat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.chat.entity.ChatConversation;


public interface ChatConversationService extends IService<ChatConversation> {

    PageUtils selectChatConversationPage(Integer page, Integer limit, Long userId,String nickName);

    int insertChatConversation(ChatConversation chatConversation);

    ChatConversation selectChatConversation(Long userId,Long focusedUserId);

    Result insertChatConversations(ChatConversation chatConversation);

}