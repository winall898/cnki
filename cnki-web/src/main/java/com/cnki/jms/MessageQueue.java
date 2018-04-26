package com.cnki.jms;

import com.cnki.jms.product.ProductMsgCustomer;
import com.cnki.jms.product.PromotionMsgCustomer;
import com.cnki.jms.product.ShopMsgCustomer;
import com.cnki.util.SpringBeanUtil;

/**
 * 消息频道名称
 * 
 * @author zhoulinhong
 * @since 20160510
 */
public enum MessageQueue {

  product_modify {
    @Override
    public MessageCustomer getCustomerClass() {
      return SpringBeanUtil.getBean("productMsgCustomer", ProductMsgCustomer.class);
    }
  },

  product_promotion {
    @Override
    public MessageCustomer getCustomerClass() {
      return SpringBeanUtil.getBean("promotionMsgCustomer", PromotionMsgCustomer.class);
    }
  },

  product_shopmain {
    @Override
    public MessageCustomer getCustomerClass() {
      return SpringBeanUtil.getBean("shopMsgCustomer", ShopMsgCustomer.class);
    }
  };

  public abstract MessageCustomer getCustomerClass();
}