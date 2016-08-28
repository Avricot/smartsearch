package com.avricot.search.front.service;

import com.avricot.search.front.avro.SearchDetail1;
import com.avricot.search.front.domain.Search;
import com.avricot.search.front.repository.ClientRepository;
import com.avricot.search.front.repository.SearchRepository;
import com.avricot.search.front.util.DateUtils;
import com.datastax.driver.core.utils.UUIDs;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 *
 */
@Service
public class SearchService {
    private static final int QUERY_MAX_LENGHT = 1000;
    private final Logger log = LoggerFactory.getLogger(SearchService.class);

    @Inject
    private SearchRepository searchRepository;
    @Inject
    private ClientRepository clientRepository;
    @Inject
    private ClientService clientService;

    private SpecificDatumWriter<SearchDetail1> datumWriter = new SpecificDatumWriter<>(SearchDetail1.class);
    private SpecificDatumReader<SearchDetail1> datumReader = new SpecificDatumReader<>(SearchDetail1.class);


    /**
     * Make sure the referer is correct and save the search.
     * TODO : test.
     */
    public void saveSearch(SearchDTO searchDTO, String referer) throws IOException {
        if (searchDTO.getQuery().length() > QUERY_MAX_LENGHT) {
            log.info("invalid query, log skipped, search={}", searchDTO);
        } else if (searchDTO.getIds().size() > 200) {
            log.info("too many ids {}, log skipped, search={}", searchDTO);
        } else if (!clientService.isRefererCorrect(searchDTO.getClientId(), referer)) {
            log.info("invalid clientId/referer, log skipped, search={}", searchDTO);
        } else {
            final UUID searchTime = UUIDs.timeBased();
            final long timestamp = UUIDs.unixTimestamp(searchTime);
            final SearchDetail1 detail = SearchDetail1.newBuilder()
                    .setClientId(searchDTO.getClientId().toString())
                    .setSearchType(searchDTO.getSearchType())
                    .setSearchTime(timestamp)
                    .setQuery(searchDTO.getQuery())
                    .setIds(searchDTO.getIds())
                    .setReceiveDuration(searchDTO.getReceiveDuration())
                    .setSentDuration(searchDTO.getReceiveDuration())
                    .setTotalDuration(searchDTO.getTotalDuration())
                    .setResultCount(searchDTO.getResultCount())
                    .build();
            final byte[] content = serializeSearchDetail(detail);
            Search search = new Search();
            search.setSearchTime(searchTime);
            search.setContent(ByteBuffer.wrap(content));
            search.setSearchType(searchDTO.getSearchType());
            search.setPeriodPartition(DateUtils.roundTimestampHourly(timestamp));
            search.setServerPartition(1);
            search.setClientId(searchDTO.getClientId());
            searchRepository.save(search);
        }
    }

    protected byte[] serializeSearchDetail(final SearchDetail1 detail) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(baos, null);
        datumWriter.write(detail, encoder);
        encoder.flush();
        return baos.toByteArray();
    }


    protected SearchDetail1 deserializeSearchDetail(final byte[] bytes) throws IOException {
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
        return datumReader.read(null, decoder);
    }

}
