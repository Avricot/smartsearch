package com.avricot.search.front.service;

import com.avricot.search.front.domain.Client;
import com.avricot.search.front.repository.ClientRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.UUID;

/**
 *
 */
@Service
public class ClientService {

    @Inject
    private ClientRepository clientRepository;

    @Cacheable("referers")
    public boolean isRefererCorrect(UUID clientId, String referer){
        final Client client = clientRepository.findOne(clientId);
        if(client == null){
            return false;
        }
        if(referer == null){
            referer = "null";
        }
        if(client.getReferers() == null){
            return false;
        }
        return client.getReferers().contains(referer);
    }
}
