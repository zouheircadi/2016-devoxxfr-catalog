package com.store.catalog.ws.rest.config;

import com.store.catalog.ws.rest.view.*;
import com.store.catalog.ws.rest.view.exception.CustomExceptionMapper;
import com.store.catalog.ws.rest.view.nosql.CatalogAdminResource;
import com.store.catalog.ws.rest.view.nosql.ElasticResource;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;

import javax.ws.rs.ApplicationPath;

/**
 * Created by ZCadi on 26/10/2015.
 */
@ApplicationPath("/*")
public class ApplicationConfig  extends ResourceConfig{



    public ApplicationConfig () {
        register(CORSResponseFilter.class);
        register(RequestContextFilter.class);
        register(JacksonFeature.class);
        register(Hello.class);
        register(CatalogAdminResource.class);
        register(ElasticResource.class);


        // Enable Tracing support.
        //property(ServerProperties.TRACING, "ALL");

        register(CustomExceptionMapper.class);
    }

}
