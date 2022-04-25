package com.sunym.springes.utils;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.IndicesClient;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.xcontent.XContentType;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.UUID;

public class ElasticSearchRestHighClientUtil {

    /**
     * 检查索引是否存在
     *
     * @param client
     * @param indexName
     * @return
     * @throws IOException
     */
    public static boolean isIndexExists(RestHighLevelClient client, String indexName) throws IOException {
        // 1. 创建索引请求
        GetIndexRequest request = new GetIndexRequest(indexName);

        // 2. 执行请求
        IndicesClient indices = client.indices();
        boolean isExist = indices.exists(request, RequestOptions.DEFAULT);
        System.out.println("IsIndexExists=" + isExist);
        return isExist;
    }

    /**
     * 创建索引
     *
     * @param client
     * @param indexName
     * @return
     * @throws IOException
     */
    public static CreateIndexResponse createIndex(RestHighLevelClient client, String indexName) throws IOException {
        // 1. 创建索引请求
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        // 2. 执行请求
        IndicesClient indices = client.indices();
        CreateIndexResponse response = indices.create(request, RequestOptions.DEFAULT);
        return response;
    }

    /**
     * 删除索引
     *
     * @param client
     * @param indexName
     * @return
     * @throws IOException
     */
    public static AcknowledgedResponse deleteIndex(RestHighLevelClient client, String indexName) throws IOException {
        // 1. 创建索引请求
        DeleteIndexRequest request = new DeleteIndexRequest(indexName);
        // 2. 执行请求
        IndicesClient indices = client.indices();
        AcknowledgedResponse response = indices.delete(request, RequestOptions.DEFAULT);
        return response;
    }

    /**
     * 创建文档
     * @param client
     * @param indexName
     * @param data
     * @param timeout
     * @return
     * @throws IOException
     */
    public static IndexResponse createDocument(RestHighLevelClient client, String indexName, String mappingName, JSONObject data, String timeout) throws IOException {
        String id = UUID.randomUUID().toString();
        IndexRequest indexRequest = new IndexRequest(indexName, mappingName, id);
        // 设定超时
        if (!StringUtils.isEmpty(timeout)) {
            indexRequest.timeout("3s");
        }
        // 设定数据
        indexRequest.source(data.toJSONString(), XContentType.JSON);
        // 请求处理
        IndexResponse response = client.index(indexRequest, RequestOptions.DEFAULT);
        return response;
    }

}
