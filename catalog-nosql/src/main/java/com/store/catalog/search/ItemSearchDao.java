package com.store.catalog.search;



import com.store.catalog.model.nosql.SearchableItem;

import java.util.List;

public interface ItemSearchDao {


    /**
     *
     * @param searchableItem
     * @return
     * @throws Exception
     */
    public String create(SearchableItem searchableItem) throws Exception ;


    /**
     *
     * @param searchableItem
     * @return
     * @throws Exception
     */
    public SearchableItem update(SearchableItem searchableItem) throws Exception ;


    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public SearchableItem get(String id) throws Exception;


    /**
     *
     * @param id
     * @return
     * @throws Exception
     */
    public boolean delete(String id) throws Exception ;


    /**
     *
     * @param keyword
     * @param from
     * @param page
     * @return
     * @throws Exception
     */
    public List<SearchableItem> matchQuery(String keyword, Integer from, Integer page) throws Exception;


    /**
     *
     * @param keyword
     * @param from
     * @param page
     * @return
     * @throws Exception
     */
    public List<SearchableItem> matchPhrasePrefixQuery(String keyword, Integer from, Integer page) throws Exception;


    /**
     *
     * @return
     * @throws Exception
     */
    public Long countAll() throws Exception;

}
