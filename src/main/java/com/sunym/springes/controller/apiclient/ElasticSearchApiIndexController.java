package com.sunym.springes.controller.apiclient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import com.alibaba.fastjson.JSONObject;
import com.sunym.springes.entity.ResultEntity;
import com.sunym.springes.utils.ElasticSearchApiClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController()
@Slf4j
@RequestMapping("api_client")
public class ElasticSearchApiIndexController {

    @Autowired
    @Qualifier("elasticsearchClient")
    ElasticsearchClient client;

    @PostMapping("isIndexExists")
    public Object isIndexExists(@RequestBody(required = false) String param) throws IOException {
        log.info("isIndexExists start! param=" + param);
        JSONObject jsonParam = JSONObject.parseObject(param);
        String indexName = jsonParam.getString("indexName");

        BooleanResponse response = ElasticSearchApiClientUtil.isIndexExists(client, indexName);
        boolean isExists = response.value();

        log.info("isIndexExists End! response=[" + isExists + "].");

        JSONObject resInfo = new JSONObject();
        resInfo.put("exists", isExists);
        return ResultEntity.ok(resInfo);
    }

    /**
     * 测试-创建索引
     *
     * @param param
     * @return
     * @throws IOException
     */
    @PostMapping("createIndex")
    public Object createIndex(@RequestBody String param) throws IOException {
        log.info("createIndex start! param=" + param);
        JSONObject jsonParam = JSONObject.parseObject(param);
        String indexName = jsonParam.getString("indexName");

        CreateIndexResponse response = ElasticSearchApiClientUtil.createIndex(client, indexName);
        log.info("createIndex End! result=" + JSONObject.toJSONString(response, true));
        return response;
    }

    /**
     * 测试-创建索引
     *
     * @param param
     * @return
     * @throws IOException
     */
    @PostMapping("deleteIndex")
    public Object deleteIndex(@RequestBody String param) {
        log.info("createIndex start! param=" + param);
        JSONObject jsonParam = JSONObject.parseObject(param);
        String indexName = jsonParam.getString("indexName");

        JSONObject resInfo = new JSONObject();
        DeleteIndexResponse response = null;
        try {
            response = ElasticSearchApiClientUtil.deleteIndex(client, indexName);
            log.info("deleteIndex End! result=[true]");
            resInfo.put("result", true);
        } catch (Exception e) {
            log.error("deleteIndex has exception:" + e.getMessage());
            resInfo.put("result", e.getMessage());
        }
        return ResultEntity.ok(resInfo);
    }

}
