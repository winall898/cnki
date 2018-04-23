package com.kmzyc.search.facade.action;

import java.io.File;
import java.net.URLDecoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.kmzyc.search.config.Channel;
import com.kmzyc.search.param.HTTPParam;
import com.kmzyc.search.param.ModelAttribute;
import com.kmzyc.search.vo.Facter;
import com.kmzyc.search.vo.ProductItem;

@Component
public class BaseController extends AbstractController {
    /**
     * 用户搜索日志
     */
    protected static final Logger searchLog = LoggerFactory.getLogger("search-log");

    protected static final Logger LOG = LoggerFactory.getLogger(BaseController.class);

    /**
     * 判断请求是否包含指定参数
     * 
     * @param paramName
     * @return
     */
    protected boolean containts(String paramName) {
        String paramValue = getRequest().getParameter(paramName);
        return StringUtils.isNotBlank(paramValue);
    }

    /**
     * 获取视图名称
     * 
     * @param channel
     * @param name
     * @return
     */
    protected String getViewName(Channel channel, String name) {
        return channel.name() + File.separator + name;
    }

    /**
     * 获取报错视图
     * 
     * @return
     */
    protected String getErrorViewName() {
        return File.separator + "error";
    }

    /**
     * 判断是否为搜索结果的过滤搜索请求
     * 
     * @return
     */
    protected boolean isFilteQuery() {
        String isFilte = getRequest().getParameter(HTTPParam.f.name());
        return StringUtils.isNotBlank(isFilte);
    }


    /**
     * 判断是否搜索到结果
     * 
     * @param result
     * @return
     */
    @SuppressWarnings("unchecked")
    protected boolean haveProducts(Map<String, Object> result) {
        if (MapUtils.isEmpty(result)) {
            return false;
        }

        List<ProductItem> productList =
                (List<ProductItem>) result.get(ModelAttribute.productList.name());
        if (null == productList || productList.isEmpty()) {
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    protected void addModel(ModelAndView view, Map<String, Object> result) {

        String keyword = getParameter(HTTPParam.kw.name());
        view.addObject(ModelAttribute.keyword.name(), keyword);

        // 获取查询串
        String uri = getRequestURI();
        view.addObject(ModelAttribute.baseURL.name(), uri);
        String queryString = getQueryString();
        if (StringUtils.isNotBlank(queryString)) {
            if (queryString.startsWith("&")) {
                queryString = queryString.substring(1);
            }
            // url拼接存放
            view.addObject(ModelAttribute.searchURL.name(), uri + "?" + queryString);
        } else {

            view.addObject(ModelAttribute.searchURL.name(), uri);
        }

        // 排序

        view.addObject(ModelAttribute.sort.name(), getParameter(HTTPParam.sort.name()));

        // 分页参数
        String pageNum = getParameter(HTTPParam.pn.name());
        if (StringUtils.isBlank(pageNum)) {
            view.addObject(ModelAttribute.pn.name(), 1);
        } else {
            view.addObject(ModelAttribute.pn.name(), Integer.parseInt(pageNum));
        }

        int count = MapUtils.getIntValue(result, ModelAttribute.count.name());
        int pageCount = 1;
        if (count > 0) {
            pageCount = (count - 1) / 25 + 1;
        }
        view.addObject(ModelAttribute.pc.name(), pageCount);

        // 已选过滤条件
        List<Map<String, String>> filters = getFilter();
        view.addObject(ModelAttribute.selectedFilter.name(), filters);

        // 判断结果
        if (null == result || result.isEmpty()) {

            return;
        }

        // 删除已选过滤分组
        List<Facter> facterList = (List<Facter>) result.get(ModelAttribute.facterList.name());
        if (null != facterList && !facterList.isEmpty()) {
            List<Facter> newfacterList = Lists.newArrayList();
            for (Facter f : facterList) {
                boolean flag = false;
                // 判断该条件是否存在已选过滤条件中
                for (Map<String, String> filter : filters) {
                    if (f.getCode().equals(filter.get("code").toString())) {
                        flag = true;
                        break;
                    }
                }
                // 不存在于已选过滤条件中，集合追加
                if (!flag) {
                    newfacterList.add(f);
                }
            }
            result.put(ModelAttribute.facterList.name(), newfacterList);
        }

        // 产品
        view.addAllObjects(result);
    }

    /**
     * marketName=药材市场&属性名=属性值
     * 
     * @return
     */
    protected List<Map<String, String>> getFilter() {
        String filterText = getParameter(HTTPParam.f.name());
        if (StringUtils.isBlank(filterText)) {

            return Lists.newArrayList();
        }
        try {
            filterText = URLDecoder.decode(filterText, "UTF-8");
        } catch (Exception e) {
            filterText = getParameter(HTTPParam.f.name());
            LOG.error("对过滤搜索参数解码失败!", e);
        }

        // 已选过滤条件
        List<Map<String, String>> filterList = Lists.newArrayList();
        if (StringUtils.isNotBlank(filterText)) {
            Iterator<String> it = Splitter.on("&").trimResults().split(filterText).iterator();
            while (it.hasNext()) {
                try {
                    Map<String, String> filterMap = Maps.newHashMap();
                    String pairs = it.next();
                    String[] kv = pairs.split("=");
                    if (kv.length < 2) {

                        continue;
                    }

                    String pCode = kv[0];
                    String pValue = kv[1];
                    if (StringUtils.isBlank(pCode) || StringUtils.isBlank(pValue)) {

                        continue;
                    }
                    if (HTTPParam.marketName.name().equals(pCode)) {
                        filterMap.put("code", pCode);
                        filterMap.put("name", "药材市场");
                        filterMap.put("value", pValue);
                    } else {
                        String[] temp = pValue.split("_");
                        filterMap.put("code", pCode);
                        filterMap.put("name", temp[0]);
                        filterMap.put("value", temp[1]);
                    }
                    // 追加到集合中
                    filterList.add(filterMap);
                } catch (Exception e) {

                    LOG.error(e.getMessage());
                }
            }
        }
        return filterList;
    }
}
