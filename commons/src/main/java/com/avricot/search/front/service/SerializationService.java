package com.avricot.search.front.service;

import com.avricot.search.front.avro.SearchDetail1;
import org.apache.avro.io.BinaryDecoder;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.specific.SpecificDatumReader;
import org.apache.avro.specific.SpecificDatumWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 *
 */
@Service
public class SerializationService {
    private static final int QUERY_MAX_LENGHT = 1000;
    private final Logger log = LoggerFactory.getLogger(SerializationService.class);


    private SpecificDatumWriter<SearchDetail1> datumWriter = new SpecificDatumWriter<>(SearchDetail1.class);
    private SpecificDatumReader<SearchDetail1> datumReader = new SpecificDatumReader<>(SearchDetail1.class);


    public byte[] serializeSearchDetail(final SearchDetail1 detail) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final BinaryEncoder encoder = EncoderFactory.get().binaryEncoder(baos, null);
        datumWriter.write(detail, encoder);
        encoder.flush();
        return baos.toByteArray();
    }


    public SearchDetail1 deserializeSearchDetail(final byte[] bytes) throws IOException {
        BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(bytes, null);
        return datumReader.read(null, decoder);
    }

}
