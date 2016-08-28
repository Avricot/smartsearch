package com.avricot.search.front.domain;

import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.UUID;

@Table(name = "search", readConsistency = "ONE", writeConsistency = "ONE")
public class Search implements Serializable {
    private static final long serialVersionUID = 1L;

    @PartitionKey(0)
    private UUID clientId;

    @PartitionKey(1)
    private String searchType;

    @PartitionKey(2)
    private long periodPartition;

    @PartitionKey(3)
    private int serverPartition;

    @ClusteringColumn
    private UUID searchTime ;

    private ByteBuffer content;

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

    public long getPeriodPartition() {
        return periodPartition;
    }

    public void setPeriodPartition(final long periodPartition) {
        this.periodPartition = periodPartition;
    }

    public int getServerPartition() {
        return serverPartition;
    }

    public void setServerPartition(final int serverPartition) {
        this.serverPartition = serverPartition;
    }

    public UUID getSearchTime() {
        return searchTime;
    }

    public void setSearchTime(final UUID searchTime) {
        this.searchTime = searchTime;
    }

    public ByteBuffer getContent() {
        return content;
    }

    public void setContent(final ByteBuffer content) {
        this.content = content;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Search search = (Search) o;
        return searchTime != null ? searchTime.equals(search.searchTime) : search.searchTime == null;
    }

    @Override
    public int hashCode() {
        return searchTime != null ? searchTime.hashCode() : 0;
    }
}
