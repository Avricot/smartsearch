package com.avricot.search.front.repository;

import com.avricot.search.front.domain.Search;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.utils.UUIDs;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.nio.ByteBuffer;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the Search entity.
 */
@Repository
public class SearchRepository {

    @Inject
    private Session session;

    private Mapper<Search> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Search.class);
        findAllStmt = session.prepare("SELECT * FROM search");
        truncateStmt = session.prepare("TRUNCATE search");
    }

    public List<Search> findAll() {
        List<Search> searches = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
        row -> {
                Search search = new Search();
                search.setClientId(row.getUUID("clientId"));
                search.setSearchTime(row.getUUID("searchTime"));
                search.setPeriodPartition(row.getInt("periodPartition"));
                search.setServerPartition(row.getInt("serverPartition"));
                search.setContent(row.getBytes("content"));
                return search;
            }
        ).forEach(searches::add);
        return searches;
    }

    public Search findOne(UUID id) {
        return mapper.get(id);
    }

    public Search save(Search search) {
        if (search.getSearchTime() == null) {
            search.setSearchTime(UUIDs.timeBased());
        }
        mapper.save(search);
        return search;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
