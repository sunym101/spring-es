package com.sunym.springes.utils;



import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.indices.CreateIndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import co.elastic.clients.transport.endpoints.BooleanResponse;

import java.io.IOException;

public class ElasticSearchApiClientUtil {



    /**
     * 创建索引
     *
     * @param client
     * @param indexName
     * @return
     * @throws IOException
     */
    public static CreateIndexResponse createIndex(ElasticsearchClient client, String indexName) throws IOException {
        CreateIndexResponse response = client.indices().create(c -> c.index(indexName));
        return response;
    }

    public static BooleanResponse isIndexExists(ElasticsearchClient client, String indexName) throws IOException {
        BooleanResponse response = client.indices().exists(c ->c.index(indexName));
        return response;
    }

    public static DeleteIndexResponse deleteIndex(ElasticsearchClient client, String indexName) throws IOException {
        DeleteIndexResponse response = client.indices().delete(c ->c.index(indexName));
        return response;
    }


}
