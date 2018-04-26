package com.cnki.jms.product;

import com.cnki.util.SpringBeanUtil;

/**
 * 搜索base类
 * 
 * @author zhoulinhong
 * @since 20160510
 */
public class SearchCmdsBase {

  public SearchCmdsBase() {}

  public static Object getBean(String beanName) {

    return SpringBeanUtil.getBean(beanName);
  }
}
