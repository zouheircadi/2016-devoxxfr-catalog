package com.store.catalog.search.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.catalog.model.nosql.SearchableItem;
import com.store.catalog.search.ItemSearchDao;
import org.elasticsearch.action.count.CountResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ZCadi on 05/11/2015.
 */
@Component
public class ItemSearchDaoImpl implements ItemSearchDao {


    private final Logger log = LoggerFactory.getLogger(this.getClass());


    @Value("${es.index.name.catalogdb}")
    protected String indexCatalog;


    @Value("${es.index.type.phone}")
    protected String typePhone;



    protected String timeout;

    @Autowired
    protected Client esClient;


    @Autowired
    protected ObjectMapper jacksonMapper;


    public void setTimeout(String timeout) {
        this.timeout = timeout;
    }


    public void setEsClient(Client esClient) {
        this.esClient = esClient;
    }

    @Override
    public String create(SearchableItem searchableItem) throws Exception {

        String json = jacksonMapper.writeValueAsString(searchableItem);
        IndexResponse response = esClient.prepareIndex(indexCatalog,typePhone,searchableItem.getId())
                .setSource(json)
                .execute()
                .actionGet();

        esClient.admin().indices().prepareRefresh().execute().actionGet();

        return response.getId();
    }

    @Override
    public SearchableItem update(SearchableItem searchableItem) throws Exception {
        String json = jacksonMapper.writeValueAsString(searchableItem);

        esClient.prepareUpdate(indexCatalog,typePhone,searchableItem.getId()).setDoc(json).get();

        esClient.admin().indices().prepareRefresh().execute().actionGet();

        GetResponse resultResponse = esClient.prepareGet(indexCatalog,typePhone,searchableItem.getId())
                .execute()
                .actionGet();


        SearchableItem result = jacksonMapper.readValue(resultResponse.getSourceAsString(), SearchableItem.class);

        return result;
    }


    @Override
    public SearchableItem get(String id) throws Exception {
        GetResponse resultResponse = esClient.prepareGet(indexCatalog,typePhone,id)
                .execute()
                .actionGet();


        SearchableItem result = null;
        if (resultResponse!= null && resultResponse.isExists()) {
            result = jacksonMapper.readValue(resultResponse.getSourceAsString(), SearchableItem.class);
        }

        return result;

    }


    @Override
    public boolean delete(String id) throws Exception {

        DeleteResponse response = esClient.prepareDelete(indexCatalog, typePhone, id)
                .execute()
                .actionGet();

        return response.isFound();
    }


    /*

     */
    public List<SearchableItem> matchQuery(String keyword, Integer from, Integer page) throws Exception{
        List<SearchableItem> items = new ArrayList<>();

        QueryBuilder queryBuilder = QueryBuilders.matchQuery("name",keyword);

        SearchResponse response = esClient.prepareSearch()
                .setIndices(indexCatalog)
                .setTypes(typePhone)
                .setQuery(queryBuilder)
                .setFrom(from)
                .setSize(page)
                .get();

        for(SearchHit hit : response.getHits().getHits()) {
            SearchableItem item = jacksonMapper.readValue(hit.getSourceAsString(),SearchableItem.class);
            items.add(item);
        }


        return items;



    }

    @Override
    public List<SearchableItem> matchPhrasePrefixQuery(String keyword, Integer from, Integer page) throws Exception {

        List<SearchableItem> items = new ArrayList<>();

        if (from == null) {
            from = Integer.valueOf(0);
        }

        if (page == null) {
            page = Integer.valueOf(10);
        }

        QueryBuilder queryBuilder = QueryBuilders.matchPhrasePrefixQuery("name",keyword);

        SearchResponse response = esClient.prepareSearch(indexCatalog)
                .setTypes(typePhone)
                .setQuery(queryBuilder)
                .setFrom(from)
                .setSize(page)
                .execute()
                .actionGet();

        for (SearchHit hit : response.getHits().getHits()) {
            SearchableItem item = jacksonMapper.readValue(hit.getSourceAsString(),SearchableItem.class);

            items.add(item);
        }



        return items;
    }




    @Override
    public Long countAll() throws Exception {

        SearchResponse response = esClient.prepareSearch(indexCatalog)
                .setQuery(QueryBuilders.termQuery("_type", typePhone))
                .setSize(0)
                .execute()
                .actionGet();

        if (response != null) {
            return Long.valueOf(response.getHits().getTotalHits()) ;
        } else {
            return null;
        }
    }

}
