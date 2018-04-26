package com.kmzyc.search.facade.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cnki.vo.Suggest;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.exception.SearchException;

public interface IAppESSearchService {
    /**
     * 关键词搜索
     * 
     * @param params
     * @return
     */
    public Map<String, Object> keywordSearch(Map<String, Object> params) throws SearchException;

    /**
     * 运营类目搜索
     * 
     * @param params
     * @return
     */
    public Map<String, Object> appCategorySearch(Map<String, Object> params);

    /**
     * app端 suggest搜索
     * 
     * @return
     */
    public List<Suggest> appSuggestSearch(final Channel channel, HttpServletRequest request);


}
