package com.cnki;

import com.cnki.exception.SearchException;
import com.cnki.request.SearchParam;
import com.cnki.response.SearchResult;


/**
 * 搜索类标识接口
 * 
 * @author river
 * 
 */
public interface ISearcher {

  public SearchResult search(SearchParam params) throws SearchException;
}
