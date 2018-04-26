package com.cnki.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.cnki.cache.CacheUtil;
import com.cnki.config.Channel;
import com.cnki.constant.ESSortParam;
import com.cnki.constant.JSPResource;
import com.cnki.param.HTTPParam;
import com.cnki.param.ModelAttribute;
import com.cnki.service.IB2BESSearchService;
import com.cnki.service.IOtherSystemService;
import com.cnki.service.SearchDBDataService;
import com.cnki.vo.Brand;
import com.cnki.vo.Suggest;
import com.google.common.cache.Cache;

/**
 * 请求处理类
 * 
 * @author river
 * 
 */
@Controller("b2bSearchAction")
@RequestMapping("/10")
public class B2BWebSearchAction extends BaseController {

    protected static final Logger LOG = LoggerFactory.getLogger(B2BWebSearchAction.class);

    @Resource(name = "searchDBDataService")
    protected SearchDBDataService searchDBDataService;

    @Resource(name = "b2bESSearchService")
    protected IB2BESSearchService b2bESSearchService;

    @Resource(name = "otherSystemService")
    private IOtherSystemService otherSystemService;

    private void setViewName(ModelAndView view, String name) {
        String viewName = getViewName(Channel.b2b, name);
        view.setViewName(viewName);
    }

    private SearchConfig getConfig(boolean facet, boolean edismax, boolean elevation,
            boolean forceEle, boolean highlight) {
        SearchConfig config = SearchConfig.build(getParameterMap()).setFacet(facet)
                .setEdismax(edismax).setEnableElevation(elevation).setForceElevation(forceEle)
                .setHighlight(highlight);
        return config;
    }

    @RequestMapping("/sensitive")
    public ModelAndView sensitive() {
        String viewName = getViewName(Channel.b2b, JSPResource.unmatchSearch);
        ModelAndView view = new ModelAndView(viewName);
        view.addObject(ModelAttribute.keyword.name(), getParameter(HTTPParam.kw.name()));
        return view;
    }

    /**
     * 品牌主页搜索 brandPage
     */
    @RequestMapping("/brandWebSearch")
    public ModelAndView brandWebSearch() {
        ModelAndView view = new ModelAndView();
        setViewName(view, JSPResource.brandPage);

        if (!containts(HTTPParam.bid.name())) {
            setViewName(view, JSPResource.unmatchSearch);
            return view;
        }

        Map<String, Object> result = new HashMap<String, Object>();
        try {
            SearchConfig config = getConfig(true, true, false, false, false);
            // 默认促销排序前面
            if (!config.getConfig().containsKey(HTTPParam.sort.name())) {
                config.getConfig().put("sort", new String[] {String.valueOf(ESSortParam.I)});
            }
            String bid = getParameter(HTTPParam.bid.name());
            result = b2bESSearchService.brandWebSearch(bid, config);

            if (null != result) {
                Brand b = (Brand) result.remove("brand_info");
                if (null == b) {
                    LOG.error("获取品基本数据失败!品牌ID：" + bid);
                    setViewName(view, JSPResource.unmatchSearch);
                } else {
                    result.put(ModelAttribute.brand.name(), b);

                    // 获取品牌名称用于页面title描述
                    if (StringUtils.isNotBlank(b.getBrandName())) {
                        setAttribute("title", b.getBrandName() + " - 商品搜索 - 康美中药城");
                        setAttribute("keywords", b.getBrandName() + "价格，" + b.getBrandName()
                                + "说明书，" + b.getBrandName() + "效果，" + b.getBrandName() + "作用");
                    }
                }
            }
        } catch (Exception e) {
            LOG.error("获取品基本数据失败!品牌ID：" + getParameter(HTTPParam.bid.name()), e);
            view.setViewName(getErrorViewName());
        }
        addModel(view, result);
        return view;
    }

    /**
     * suggest搜索
     * 
     * @return
     */
    @RequestMapping(value = "/suggest", produces = {"application/json"})
    @ResponseBody
    public String suggestSearch() {

        if (!containts(HTTPParam.q.name()))
            return "";

        String callback = getParameter("callback");
        String json = "";
        try {
            Cache<Object, Object> cache = CacheUtil.getCache(Channel.b2b.name() + ".suggest.search",
                    3000, 12, TimeUnit.HOURS, false);
            final String prefix = getParameter(HTTPParam.q.name()).toLowerCase();
            Object jsonObj = cache.get(prefix, new Callable<List<Suggest>>() {

                @Override
                public List<Suggest> call() throws Exception {
                    SearchConfig config = SearchConfig.build(getParameterMap());
                    config.getConfig().put("q", new String[] {prefix});
                    List<Suggest> result = b2bESSearchService.suggestSearch(config);
                    return result;
                }
            });

            if (!StringUtils.isBlank(callback) && jsonObj != null) {

                json = callback + "(" + JSONObject.toJSONString(jsonObj) + ")";
            }
        } catch (Exception e) {

            LOG.error("SUGGEST搜索获取数据失败!搜索词条为：" + getParameter(HTTPParam.q.name()), e);
        }

        return json;
    }


    /**
     * 店铺商品搜索
     * 
     * @return
     */
    @RequestMapping("/shopProducts")
    public ModelAndView shopProducts() {
        ModelAndView view = new ModelAndView();
        setViewName(view, JSPResource.shopProduct);

        String shopid = getParameter(HTTPParam.shopid.name());
        if (StringUtils.isEmpty(shopid)) {
            LOG.warn("店铺类目搜索传递的店铺ID参数为空,无法进行搜索.");
            return view;
        }

        // 搜索日志记录
        String kw = getParameter(HTTPParam.kw.name());
        if (StringUtils.isNotBlank(kw)) {
            searchLog.info("搜索关键词：" + kw);
        }

        SearchConfig config = getConfig(true, true, false, false, false);
        // 默认促销排序前面
        if (!config.getConfig().containsKey(HTTPParam.sort.name())) {
            config.getConfig().put("sort", new String[] {String.valueOf(ESSortParam.I)});
        }

        Map<String, Object> result = b2bESSearchService.shopProductsSearch(config);
        result.put(ModelAttribute.shopid.name(), shopid);

        // 获取店铺名称用于页面title描述
        String shopName = searchDBDataService.getShopNameId(Integer.parseInt(shopid));
        if (StringUtils.isNotBlank(shopName)) {
            setAttribute("title", shopName);
            setAttribute("keywords",
                    shopName + "价格，" + shopName + "说明书，" + shopName + "效果，" + shopName + "作用");
        }

        addModel(view, result);
        return view;
    }

}
