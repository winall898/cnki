package com.cnki;

import com.cnki.exception.SearchException;
import com.cnki.request.ParamsTransverter;
import com.cnki.request.SearchParam;
import com.cnki.response.ResultTransverter;
import com.cnki.response.SearchResult;

/**
 * 
 * @author river
 * 
 */
public abstract class AbstractSearcher implements ISearcher {

  protected ParamsTransverter reqTransverter;

  protected ResultTransverter resTransverter;

  protected String queryString;

  /**
   * 设置搜索参数转换器
   * 
   * @param transverter
   * @return
   */
  public AbstractSearcher setParamTransverter(ParamsTransverter transverter) {
    this.reqTransverter = transverter;
    return this;
  }

  /**
   * 设置搜索结果转换器
   * 
   * @param transverter
   * @return
   */
  public AbstractSearcher setResultTransverter(ResultTransverter transverter) {
    this.resTransverter = transverter;
    return this;
  }

  public String getQueryString() {
    return queryString;
  }

  protected SearchParam beforSearch(SearchParam param) {
    if (null == reqTransverter) {

      return param;
    }
    return reqTransverter.convert(param);
  }

  protected SearchResult afterSearch(SearchResult result) {
    if (null == resTransverter) {
      return result;
    }
    return resTransverter.convert(result);
  }

  @Override
  public SearchResult search(SearchParam param) throws SearchException {
    if (null == param) {
      return null;
    }
    SearchParam newParam = beforSearch(param);
    SearchResult resp = query(newParam);
    return afterSearch(resp);
  }

  protected abstract SearchResult query(SearchParam param) throws SearchException;
}
