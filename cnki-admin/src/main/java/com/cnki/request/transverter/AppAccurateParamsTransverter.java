package com.cnki.request.transverter;

import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.cnki.config.Channel;
import com.cnki.param.DocFieldName;
import com.cnki.param.HTTPParam;
import com.cnki.util.JsonUtil;

public class AppAccurateParamsTransverter extends AppParamsTransverter {

    public AppAccurateParamsTransverter(Channel channel) {
        super(false, false, channel);
    }

    @SuppressWarnings("unchecked")
    @Override
    public String convert(Object param) {
        if (param == null) {
            return null;
        }
        Map<String, Object> searchParams = (Map<String, Object>) param;
        String qs = (String) searchParams.get(HTTPParam.q.name());

        String queryStr = super.convert(searchParams);

        JSONObject queryJson = JSONObject.parseObject(queryStr);
        StringBuilder qval = new StringBuilder();
        qval.append(DocFieldName.SKUCODE + ":\"" + qs + "\""); // SKU编码
        qval.append(" OR " + DocFieldName.PRODUCT_NO + ":\"" + qs + "\""); // 产品编码
        queryJson.put("query", JsonUtil.jsonPut(new JSONObject(), "query_string",
                JsonUtil.jsonPut(new JSONObject(), "query", qval)));
        return queryJson.toJSONString();
    }



}
