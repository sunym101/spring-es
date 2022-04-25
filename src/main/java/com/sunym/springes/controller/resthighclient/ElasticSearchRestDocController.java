package com.sunym.springes.controller.resthighclient;

import com.alibaba.fastjson.JSONObject;
import com.sunym.springes.entity.ResultEntity;
import com.sunym.springes.utils.ElasticSearchRestHighClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.rest.RestStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@RequestMapping("rest_high_doc")
public class ElasticSearchRestDocController {

    @Autowired
    @Qualifier("restHighLevelClient")
    RestHighLevelClient client;

    /**
     * 创建文档
     *
     * @param param
     * @return
     * @throws IOException
     */
    @PostMapping("createDocument")
    public String createDocument(@RequestBody String param) throws IOException {
        log.info("createDocument start! param = " + param);
        JSONObject jsonParam = JSONObject.parseObject(param);
        String indexName = jsonParam.getString("indexName");
        String mappingName = jsonParam.getString("mappingName");
        JSONObject jsonData = jsonParam.getJSONObject("data");

        // 1. 检查Index是否存在
        log.info("检查Index[" + indexName + "]是否存在");
        boolean isExist = ElasticSearchRestHighClientUtil.isIndexExists(client, indexName);
        if (!isExist) {
            // 不存在时创建Index
            log.info("不存在时创建Index[" + indexName + "]!");
            ElasticSearchRestHighClientUtil.createIndex(client, indexName);
        }

        // 2. 添加文档
        log.info("在Index[" + indexName + "]中添加文档，开始...");
        IndexResponse response = ElasticSearchRestHighClientUtil.createDocument(client, indexName, mappingName, jsonData, null);
        RestStatus restStatus = response.status();
        log.info("在Index[" + indexName + "]中添加文档，结束！response.status=" + restStatus, JSONObject.toJSONString(response, true));
        return JSONObject.toJSONString(new ResultEntity("0000", restStatus.toString(), response), true);
    }

    /**
     * 测试-创建索引
     *
     * @param param
     * @return
     * @throws IOException
     */
    @PostMapping("deleteDocument")
    public String deleteDocument(@RequestBody String param) throws IOException {
        JSONObject jsonParam = JSONObject.parseObject(param);
        String indexName = jsonParam.getString("indexName");

        // 1. 创建索引请求
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        // 2. 执行请求
        IndicesClient indices = client.indices();
        AcknowledgedResponse response = indices.delete(request, RequestOptions.DEFAULT);
        System.out.println("AcknowledgedResponse=" + response);
        return JSONObject.toJSONString(response, true);
    }

}
