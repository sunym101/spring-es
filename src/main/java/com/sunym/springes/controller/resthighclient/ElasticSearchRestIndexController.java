package com.sunym.springes.controller.resthighclient;

import com.alibaba.fastjson.JSONObject;
import com.sunym.springes.utils.ElasticSearchRestHighClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController()
@Slf4j
@RequestMapping("rest_high_index")
public class ElasticSearchRestIndexController {

    @Autowired
    @Qualifier("restHighLevelClient")
    RestHighLevelClient client;

    @PostMapping("isIndexExists")
    public Object isIndexExists(@RequestBody(required = false) String param) throws IOException {
        log.info("isIndexExists start! param=" + param);
        JSONObject jsonParam = JSONObject.parseObject(param);
        String indexName = jsonParam.getString("indexName");

        boolean isExist = ElasticSearchRestHighClientUtil.isIndexExists(client, indexName);
        log.info("isIndexExists End! result=" + isExist);
        return isExist;
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

        CreateIndexResponse response = ElasticSearchRestHighClientUtil.createIndex(client, indexName);
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
    public Object deleteIndex(@RequestBody String param) throws IOException {
        log.info("createIndex start! param=" + param);
        JSONObject jsonParam = JSONObject.parseObject(param);
        String indexName = jsonParam.getString("indexName");

        AcknowledgedResponse response = ElasticSearchRestHighClientUtil.deleteIndex(client, indexName);
        log.info("deleteIndex End! result=" + JSONObject.toJSONString(response, true));
        return response;
    }

}
