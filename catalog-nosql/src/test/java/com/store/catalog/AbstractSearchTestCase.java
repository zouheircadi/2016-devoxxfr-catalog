package com.store.catalog;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.store.catalog.model.nosql.SearchableItem;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexResponse;
import org.elasticsearch.action.admin.indices.flush.FlushRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static junit.framework.TestCase.fail;
import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by ZCadi on 05/11/2015.
 */


@RunWith(SpringJUnit4ClassRunner.class)
public abstract class AbstractSearchTestCase {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    protected Client esClient;

    @Autowired
    protected ObjectMapper jacksonMapper;

    @Autowired
    protected  ApplicationContext applicationContext;


    @Value("${es.index.name.catalogdb}")
    protected String indexCatalog;

    @Value("${es.index.type.phone}")
    protected  String typePhone;



    protected void createIndexReferential() throws Exception{
        //create and delete mapping manually

        IndicesAdminClient indicesAdminClient = esClient.admin().indices();

        //delete
        try {
            indicesAdminClient.delete(new DeleteIndexRequest(indexCatalog)).get();
        }catch (Exception e){

        }


        //create index and flush
        indicesAdminClient.create(new CreateIndexRequest(indexCatalog)).get();
        indicesAdminClient.flush(new FlushRequest(indexCatalog).force(true)).get();

        //retrieve mapping and put it
        String mapping = getMappingFromFile();
        indicesAdminClient.preparePutMapping(indexCatalog).setType(typePhone).setSource(mapping).get();

        //bulk inject test data
        BulkRequestBuilder brb = esClient.prepareBulk();
        addTypePhones(brb);
        BulkResponse bulkResponse = brb.get();
        Assert.assertFalse(bulkResponse.hasFailures());

        //refresh
        indicesAdminClient.prepareRefresh(indexCatalog).get();




    }


    protected String getMappingFromFile() throws  Exception{
        Resource resource =
                applicationContext
                        .getResource("classpath:/es.test/conf/phone.json");

        StringBuilder builder = new StringBuilder();
        String line;

        try{
            InputStream is = resource.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));


            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();

        }catch(IOException e){
            throw new Exception();
        }

        return  builder.toString();
    }


    protected void createPhoneMapping() throws IOException {

        final XContentBuilder mappingBuilder = jsonBuilder().startObject()
                .startObject("properties")
                .startObject("age")
                .field("type", "integer")
                .field("store", "yes")
                .field("index", "not_analyzed")
                .endObject()
                .startObject("name")
                .field("type", "string")
                .field("index", "analyzed")
                .endObject()
                .startObject("snippet")
                .field("type", "string")
                .field("store", "yes")
                .field("index", "analyzed")
                .endObject()
                .startObject("imageUrl")
                .field("type", "string")
                .field("store", "yes")
                .field("index", "analyzed")
                .endObject()
                .endObject()
                .endObject();

        esClient.admin().indices().preparePutMapping(indexCatalog).setType(typePhone).setSource(mappingBuilder).get();

    }


    protected void addTypePhones(BulkRequestBuilder brb) throws Exception {

        //create an instance of business object
        SearchableItem item1 = new SearchableItem(Integer.valueOf(1),"image.jpg","Xoom the best phone","snippets1");

        //create an instance of index object
        IndexRequest irq1 = new IndexRequest(indexCatalog, typePhone,item1.getId());

        //set the business object as json
        irq1.source(jacksonMapper.writeValueAsString(item1));

        //add to bulkRequestBuilder
        brb.add(irq1);


        //repeat this operation n time
        SearchableItem item2 = new SearchableItem(Integer.valueOf(2), "image2.jpg", "Samsung from Korea", "snippets2");
        IndexRequest irq2 = new IndexRequest(indexCatalog, typePhone,item2.getId());
        irq2.source(jacksonMapper.writeValueAsString(item2));
        brb.add(irq2);

        SearchableItem item3 = new SearchableItem(Integer.valueOf(3),"image3.jpg","sony first item","snippets3");
        IndexRequest irq3 = new IndexRequest(indexCatalog,typePhone,item3.getId());
        irq3.source(jacksonMapper.writeValueAsString(item3));
        brb.add(irq3);


        SearchableItem item4 = new SearchableItem(Integer.valueOf(4),"image4.jpg","Another xoom from the US","snippets");
        IndexRequest irq4 = new IndexRequest(indexCatalog, typePhone,item4.getId());
        irq4.source(jacksonMapper.writeValueAsString(item4));
        brb.add(irq4);

        SearchableItem item5 = new SearchableItem(Integer.valueOf(5),"image5.jpg","sony second time","snippets");
        IndexRequest irq5 = new IndexRequest(indexCatalog, typePhone,item5.getId());
        irq5.source(jacksonMapper.writeValueAsString(item5));
        brb.add(irq5);

        SearchableItem item6 = new SearchableItem(Integer.valueOf(6),"image6.jpg","sony the third","snippets");
        IndexRequest irq6 = new IndexRequest(indexCatalog, typePhone,item6.getId());
        irq6.source(jacksonMapper.writeValueAsString(item6));
        brb.add(irq6);

        SearchableItem item7 = new SearchableItem(Integer.valueOf(7),"image7.jpg","this a not sony the third","snippets");
        IndexRequest irq7 = new IndexRequest(indexCatalog, typePhone,item7.getId());
        irq7.source(jacksonMapper.writeValueAsString(item7));
        brb.add(irq7);

        SearchableItem item8 = new SearchableItem(Integer.valueOf(8),"image8.jpg","sony the forth","snippets");
        IndexRequest irq8 = new IndexRequest(indexCatalog, typePhone,item8.getId());
        irq8.source(jacksonMapper.writeValueAsString(item8));
        brb.add(irq8);

        SearchableItem item9 = new SearchableItem(Integer.valueOf(9),"image9.jpg","windows phone","snippets");
        IndexRequest irq9 = new IndexRequest(indexCatalog, typePhone,item9.getId());
        irq9.source(jacksonMapper.writeValueAsString(item9));
        brb.add(irq9);



    }


}
