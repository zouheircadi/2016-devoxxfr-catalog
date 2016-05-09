package com.store.catalog.search.impl;

import com.store.catalog.AbstractSearchTestCase;
import com.store.catalog.model.nosql.SearchableItem;
import com.store.catalog.search.ItemSearchDao;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;


@ContextConfiguration(locations = {"classpath:es-test.xml"})
public class ItemSearchDaoTest  extends AbstractSearchTestCase {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ItemSearchDao itemSearchDao;



    @Before
    public void setUp() throws Exception {
        createIndexReferential();
    }


    @After
    public void teadDown() throws Exception {
    }


    @Test
    public void controlMapping() throws Exception {

        MappingMetaData metaData = esClient.admin().indices().prepareGetMappings().get().getMappings().get(indexCatalog).get(typePhone);
        String mapping = new String(metaData.source().uncompressed());

        assertThat(mapping, containsString("age"));
        assertThat(mapping, containsString("name"));
        assertThat(mapping, containsString("snippet"));
        assertThat(mapping, containsString("imageUrl"));
    }




    @Test
    public void testCountAll() throws  Exception {
        assertNotNull(itemSearchDao);

        Long countAll =  itemSearchDao.countAll();

        assertNotNull(countAll);
        assertEquals(Long.valueOf(9), countAll);
    }


    @Test
    public void testSearchItems() throws  Exception {

        //search two xoom
        assertNotNull(itemSearchDao);

        List<SearchableItem> items = itemSearchDao
                .matchQuery("xoom", 0, 20);

        assertNotNull(items);
        assertEquals(2, items.size());

    }



    @Test
    public void testSearchItemsOne() throws  Exception {
        assertNotNull(itemSearchDao);

        List<SearchableItem> items =  itemSearchDao.matchQuery("samsung", 0, 20);

        assertNotNull(items);
        assertEquals(1,items.size());
    }


    @Test
    public void testSearchItemsTwo() throws  Exception {
        assertNotNull(itemSearchDao);

        List<SearchableItem> items =  itemSearchDao.matchQuery("xoom", 0, 20);

        assertNotNull(items);
        assertEquals(2,items.size());
    }




    @Test
    public void testMatchPrefixFirstPageWithTwoElements() throws  Exception {
        assertNotNull(itemSearchDao);

        List<SearchableItem> items =  itemSearchDao.matchPhrasePrefixQuery("sony", 0, 2);

        assertNotNull(items);
        assertEquals(2,items.size());
    }

    @Test
    public void testMatchPrefixSecondPageWithTwoElements() throws  Exception {
        assertNotNull(itemSearchDao);

        List<SearchableItem> items =  itemSearchDao.matchPhrasePrefixQuery("sony", 1, 2);

        assertNotNull(items);
        assertEquals(2,items.size());
    }

    @Test
    public void testMatchPrefixThirdPageWithOneElements() throws  Exception {
        assertNotNull(itemSearchDao);

        List<SearchableItem> items =  itemSearchDao.matchPhrasePrefixQuery("sony", 1, 2);

        assertNotNull(items);
        assertEquals(2,items.size());
    }


}