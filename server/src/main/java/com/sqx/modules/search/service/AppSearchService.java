package com.sqx.modules.search.service;

import com.sqx.common.utils.Result;

/**
 * app搜索
 */
public interface AppSearchService {

    Result insetAppSearch(String searchName, Long userId);

    Result  selectAppSearchNum(Long userId);

    Result deleteAppSearch( Long userId);
}
