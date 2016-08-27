package com.avricot.search.front.service;

import com.avricot.search.front.avro.SearchDetail1;
import org.apache.avro.AvroRuntimeException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 *
 */
@RunWith(MockitoJUnitRunner.class)
public class SearchServiceTest {

    @InjectMocks
    private SearchService searchService ;

    @Test
    public void serializeSearchDetail() throws Exception {
        final SearchDetail1 detail = SearchDetail1.newBuilder()
                .setClientId("toto")
                .setSearchType("type")
                .setSearchTime(666)
                .setQuery("q")
                .setIds(Arrays.asList("Buenos Aires", "Córdoba", "La Plata"))
                .setReceiveDuration(10)
                .setSentDuration(10)
                .setTotalDuration(10)
                .setResultCount(10).build();
        final byte[] b = searchService.serializeSearchDetail(detail);
        final SearchDetail1 detail2 = searchService.deserializeSearchDetail(b);
        Assert.assertThat(detail, new ReflectionEquals(detail2));
    }

    @Test
    public void serializeSearchDetailNull() throws Exception {
        final SearchDetail1 detail = SearchDetail1.newBuilder()
                .setClientId("toto")
                .setSearchType("type")
                .setSearchTime(666).build();
        final byte[] b = searchService.serializeSearchDetail(detail);
        final SearchDetail1 detail2 = searchService.deserializeSearchDetail(b);
        Assert.assertThat(detail, new ReflectionEquals(detail2));
    }


    @Test(expected = AvroRuntimeException.class)
    public void serializeSearchDetailMissingField() throws Exception {
        final SearchDetail1 detail = SearchDetail1.newBuilder()
                .setClientId("toto")
                .setSearchTime(666).build();
        final byte[] b = searchService.serializeSearchDetail(detail);
        final SearchDetail1 detail2 = searchService.deserializeSearchDetail(b);
        Assert.assertThat(detail, new ReflectionEquals(detail2));
    }

}
