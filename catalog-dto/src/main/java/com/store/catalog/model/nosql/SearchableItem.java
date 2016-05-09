package com.store.catalog.model.nosql;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.store.catalog.model.AbstractBean;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by ZCadi on 04/11/2015.
 */
public class SearchableItem implements AbstractBean {


    @JsonProperty("id")
    private String id;

    private Integer age;

    private String imageUrl;

    private String name;

    private String snippet;

    public Integer getAge() {
        return age;
    }

    public SearchableItem() {
    }

    public SearchableItem(String id, Integer age, String imageUrl, String name, String snippet) {
        this.id = id;
        this.age = age;
        this.imageUrl = imageUrl;
        this.name = name;
        this.snippet = snippet;
    }

    public SearchableItem(Integer age, String imageUrl, String name, String snippet) {
        this.age = age;
        this.imageUrl = imageUrl;
        this.name = name;
        this.snippet = snippet;

        if (name != null) {
            id = createId();
        } else {
            throw new RuntimeException("Id cannot be set");
        }
    }


    @JsonProperty("id")
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }


    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }


    public int hashCode(Object obj) {
        return new HashCodeBuilder().append(id) .append(name).append(imageUrl).hashCode();
    }


    public String createId() {
        return hashString(name);
    }



    public String hashString(String message) {

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hashedBytes = null;
        try {
            hashedBytes = digest.digest(message.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < hashedBytes.length; i++) {
            stringBuffer.append(Integer.toString((hashedBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }

        return stringBuffer.toString();

    }



}

