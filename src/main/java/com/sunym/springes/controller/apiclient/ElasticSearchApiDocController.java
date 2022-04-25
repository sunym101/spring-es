package com.sunym.springes.controller.apiclient;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import co.elastic.clients.json.JsonData;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sunym.springes.entity.ResultEntity;
import com.sunym.springes.entity.ResultInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("api_client")
public class ElasticSearchApiDocController {

    @Autowired
    @Qualifier("elasticsearchClient")
    ElasticsearchClient client;

    /**
     * 创建文档
     *
     * @param param
     * @return
     * @throws IOException
     */
    @PostMapping("createDocument")
    public Object createDocument(@RequestBody String param) {
        log.info("createDocument start! param = " + param);
        JSONObject jsonParam = JSONObject.parseObject(param);
        String indexName = jsonParam.getString("indexName");
        JSONObject jsonData = jsonParam.getJSONObject("data");

        String id = UUID.randomUUID().toString();
        Reader input = new StringReader(jsonData.toJSONString());
        IndexRequest<JsonData> request = IndexRequest.of(i -> i
                .index(indexName)
                .withJson(input)
        );

        IndexResponse response = null;
        try {
            response = client.index(request);
            log.info("Indexed with version " + response.version());

            return ResultEntity.ok(new ResultInfo("response.version=" + response.version()));
        } catch (Exception e) {
            log.error("createDocument Exception:" + e.getMessage());
            return ResultEntity.ng(new ResultInfo("exception=" + e.getMessage()));
        }
    }

    @PostMapping("getDocumentById")
    public Object getDocumentById(@RequestBody String param) {
        log.info("getDocumentById start! param = " + param);
        JSONObject jsonParam = JSONObject.parseObject(param);
        String indexName = jsonParam.getString("indexName");
        String id = jsonParam.getString("id");

        GetResponse<ObjectNode> response = null;
        try {
            response = client.get(g -> g
                            .index(indexName)
                            .id(id), // 查询条件
                    ObjectNode.class
            );
            if (response.found()) {
                ObjectNode json = response.source();
                String phone = json.get("phone").asText();
                log.info("id=[" + id + "], json=[" + json.asText() + "].");
                return "data=" + json;

            } else {
                log.info("member not found");
                return "member not found";
            }


        } catch (IOException e) {
            e.printStackTrace();
            return "exception:" + e.getMessage();
        }
    }

    @PostMapping("searchDocuments")
    public Object searchDocuments(@RequestBody String param) {
        log.info("searchDocuments start! param = " + param);
        JSONObject jsonParam = JSONObject.parseObject(param);
        String indexName = jsonParam.getString("indexName");
        String member_no = jsonParam.getString("member_no");

        try {
            SearchResponse<JSONObject> response = client.search(s -> s
                    .index(indexName)
                    .query(q -> q
                            .match(t -> t
                                    .field("member_no")
                                    .query(member_no)
                            )
                    ), JSONObject.class
            );

            TotalHits total = response.hits().total();
            boolean isExactResult = total.relation() == TotalHitsRelation.Eq;

            if (isExactResult) {
                log.info("There are " + total.value() + " results");
            } else {
                log.info("There are more than " + total.value() + " results");
            }

            List<Hit<JSONObject>> hits = response.hits().hits();
            JSONArray array = new JSONArray(hits.size());
            for (Hit<JSONObject> hit : hits) {
                JSONObject member = hit.source();
                array.add(member);
                log.info("Found Member= " + member.toJSONString() + ", score= " + hit.score());
            }

            return "success! total=" + total.value() + ", data=" + array.toJSONString();
        } catch (Exception e) {
            log.error("searchDocuments 发生异常：" + e.getMessage(), e.fillInStackTrace());
            e.printStackTrace();
            return "exception:" + e.getMessage();
        }
    }


}
