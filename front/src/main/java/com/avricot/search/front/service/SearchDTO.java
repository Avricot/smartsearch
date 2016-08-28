package com.avricot.search.front.service;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 */
public class SearchDTO {
    @NotNull
    private UUID clientId;
    @NotNull
    private String searchType;
    private UUID searchTime;
    private int sentDuration;
    private int receiveDuration;
    private int totalDuration;
    @NotNull
    private String query;
    private List<String> ids = new ArrayList<>();
    private int resultCount;

    public UUID getClientId() {
        return clientId;
    }

    public void setClientId(final UUID clientId) {
        this.clientId = clientId;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(final String searchType) {
        this.searchType = searchType;
    }

    public UUID getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(final UUID searchTime) {
        this.searchTime = searchTime;
    }

    public int getSentDuration() {
        return sentDuration;
    }

    public void setSentDuration(final int sentDuration) {
        this.sentDuration = sentDuration;
    }

    public int getReceiveDuration() {
        return receiveDuration;
    }

    public void setReceiveDuration(final int receiveDuration) {
        this.receiveDuration = receiveDuration;
    }

    public int getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(final int totalDuration) {
        this.totalDuration = totalDuration;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(final String query) {
        this.query = query;
    }

    public List<String> getIds() {
        return ids;
    }

    public void setIds(final List<String> ids) {
        this.ids = ids;
    }

    public int getResultCount() {
        return resultCount;
    }

    public void setResultCount(final int resultCount) {
        this.resultCount = resultCount;
    }

    @Override
    public String toString() {
        return "SearchDTO{" +
                "clientId=" + clientId +
                ", searchType='" + searchType + '\'' +
                ", searchTime=" + searchTime +
                ", sentDuration=" + sentDuration +
                ", receiveDuration=" + receiveDuration +
                ", totalDuration=" + totalDuration +
                ", query='" + query + '\'' +
                ", ids=" + ids +
                ", resultCount=" + resultCount +
                '}';
    }
}
