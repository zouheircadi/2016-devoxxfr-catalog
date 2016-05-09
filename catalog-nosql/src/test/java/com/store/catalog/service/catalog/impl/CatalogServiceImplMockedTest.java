package com.store.catalog.service.catalog.impl;

import com.store.catalog.model.nosql.SearchableItem;
import com.store.catalog.mongo.ItemMongoDao;
import com.store.catalog.search.ItemSearchDao;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class CatalogServiceImplMockedTest  {



	private CatalogSearchServiceImpl catalogSearchService;

	@Mock
	ItemSearchDao itemSearchDao;

	@Mock
	ItemMongoDao itemMongoDao;
	

	@Before
	public void setUp() throws Exception {
		catalogSearchService = new CatalogSearchServiceImpl();
		catalogSearchService.setItemSearchDao(itemSearchDao);
		catalogSearchService.setItemMongoDao(itemMongoDao);
	}

	@After
	public void tearDown() throws Exception {
        catalogSearchService = null;
	}

	
	
	/* ---------------------------------- */
	/* ---------------------------------- */
	/* ---------------------------------- */
	

	
	@Test 
	public void searchItemsTest() throws Exception {
		String searchText = "xoom";
		int from = 0 ;
		int page  = 20 ;


		when(itemSearchDao.matchQuery(searchText, from, page)).thenReturn(createSearchableItemsWithTwoElement());

		
		List<SearchableItem> searchableItems = catalogSearchService.matchQuery(searchText, 0, 20);

        assertNotNull(searchableItems);
        assertEquals(2,searchableItems.size());
		
	}


	@Test
	@Ignore
	public void searchItemsMatchPhrase() throws Exception {
		String searchText = "xoom";
		int from = 1 ;
		int page  = 1 ;


		when(itemSearchDao.matchPhrasePrefixQuery(searchText, from, page)).thenReturn(createSearchableItemsWithOneElement());


		List<SearchableItem> searchableItems = catalogSearchService.matchPhrasePrefixQuery(searchText, from, page);

		assertNotNull(searchableItems);
		assertEquals(1,searchableItems.size());

	}

	@Test
	public void searchItemsMatchPhraseTwoElements() throws Exception {
		String searchText = "xoom";
		int from = 1 ;
		int page  = 2 ;


		when(itemSearchDao.matchPhrasePrefixQuery(searchText, from, page)).thenReturn(createSearchableItemsWithTwoElement());


		List<SearchableItem> searchableItems = catalogSearchService.matchPhrasePrefixQuery(searchText, from, page);

		assertNotNull(searchableItems);
		assertEquals(2,searchableItems.size());

	}
	
	
	/* ---------------------------------- */
	/* --------- Private methods -------- */
	/* ---------------------------------- */
	public List<SearchableItem> createSearchableItemsWithTwoElement(){
		SearchableItem item1 = new SearchableItem(Integer.valueOf(1),"image.jpg","Xoom the best phone","snippets1");

		SearchableItem item2 = new SearchableItem(Integer.valueOf(2),"image2.jpg","Samsung from Korea","snippets2");

		ArrayList<SearchableItem> lst = new ArrayList<SearchableItem>();
		lst.add(item1);
		lst.add(item2);

		return lst;

	}


	public List<SearchableItem> createSearchableItemsWithOneElement(){
		SearchableItem item1 = new SearchableItem(Integer.valueOf(1),"image.jpg","Xoom the best phone","snippets1");

		ArrayList<SearchableItem> lst = new ArrayList<SearchableItem>();
		lst.add(item1);

		return lst;

	}
	



	
}
