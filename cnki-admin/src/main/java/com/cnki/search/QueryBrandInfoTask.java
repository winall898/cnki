package com.cnki.search;

import java.util.concurrent.RecursiveTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cnki.service.SearchDBDataService;
import com.cnki.util.ApplicationContextUtil;
import com.cnki.vo.Brand;

public class QueryBrandInfoTask extends RecursiveTask<Brand> {

  private static final long serialVersionUID = 5909361416153699625L;

  private static final Logger LOG = LoggerFactory.getLogger(QueryBrandInfoTask.class);

  private final int brandID;

  public QueryBrandInfoTask(int brandID) {
    this.brandID = brandID;
  }

  @Override
  protected Brand compute() {
    SearchDBDataService searchDBDataService = (SearchDBDataService) ApplicationContextUtil
        .getApplicationContext().getBean("searchDBDataService");
    try {
      return searchDBDataService.getBrandDetails(brandID);
    } catch (Exception e) {
      LOG.error("获取品牌基本信息失败！品牌ID为: " + brandID, e);
    }
    return null;
  }

}
