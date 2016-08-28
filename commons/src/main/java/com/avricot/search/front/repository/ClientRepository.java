package com.avricot.search.front.repository;

import com.avricot.search.front.domain.Client;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.datastax.driver.mapping.Mapper;
import com.datastax.driver.mapping.MappingManager;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Cassandra repository for the Client entity.
 */
@Repository
public class ClientRepository {

    @Inject
    private Session session;

    private Mapper<Client> mapper;

    private PreparedStatement findAllStmt;

    private PreparedStatement truncateStmt;

    @PostConstruct
    public void init() {
        mapper = new MappingManager(session).mapper(Client.class);
        findAllStmt = session.prepare("SELECT * FROM client");
        truncateStmt = session.prepare("TRUNCATE client");
    }

    public List<Client> findAll() {
        List<Client> clients = new ArrayList<>();
        BoundStatement stmt =  findAllStmt.bind();
        session.execute(stmt).all().stream().map(
            row -> {
                Client client = new Client();
                client.setId(row.getUUID("id"));
                client.setName(row.getString("name"));
                client.setReferers(row.getSet("referers", String.class));
                return client;
            }
        ).forEach(clients::add);
        return clients;
    }

    public Client findOne(UUID id) {
        return mapper.get(id);
    }

    public Client save(Client client) {
        if (client.getId() == null) {
            client.setId(UUID.randomUUID());
        }
        mapper.save(client);
        return client;
    }

    public void delete(UUID id) {
        mapper.delete(id);
    }

    public void deleteAll() {
        BoundStatement stmt =  truncateStmt.bind();
        session.execute(stmt);
    }
}
