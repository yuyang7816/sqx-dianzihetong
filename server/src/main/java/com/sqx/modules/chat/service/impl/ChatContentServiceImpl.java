package com.sqx.modules.chat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.modules.chat.dao.ChatContentDao;
import com.sqx.modules.chat.entity.ChatContent;
import com.sqx.modules.chat.service.ChatContentService;
import org.springframework.stereotype.Service;


@Service
public class ChatContentServiceImpl extends ServiceImpl<ChatContentDao, ChatContent> implements ChatContentService {


    @Override
    public PageUtils selectChatContentPage(Integer page, Integer limit, Long chatConversationId,String content) {
        return new PageUtils(baseMapper.selectChatContentPage(new Page<>(page,limit),chatConversationId,content));
    }

    @Override
    public int updateChatContentStatusByUserIdAndChatId(Long userId,Long chatConversationId){
        return baseMapper.updateChatContentStatusByUserIdAndChatId(userId, chatConversationId);
    }

    @Override
    public int selectChatCount(Long userId) {
        return baseMapper.selectChatCount(userId);
    }

}