package com.example.aggregator.client;

import com.example.aggregator.model.Entry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AggregatorRestClientTest {

    @Mock
    private RestTemplate restTemplate;

    private AggregatorRestClient client;

    @BeforeEach
    void setUp() {
        client = new AggregatorRestClient(restTemplate);
    }

    @Test
    void getDefinitionFor_shouldReturnEntry_whenWordExists() {
        Entry expected = new Entry("test", "definition");
        when(restTemplate.getForObject(anyString(), eq(Entry.class))).thenReturn(expected);

        Entry result = client.getDefinitionFor("test");

        assertNotNull(result);
        assertEquals("test", result.getWord());
        verify(restTemplate).getForObject("http://localhost:9091/getWord/test", Entry.class);
    }

    @Test
    void getDefinitionFor_shouldReturnNull_whenWordNotFound() {
        when(restTemplate.getForObject(anyString(), eq(Entry.class))).thenReturn(null);

        Entry result = client.getDefinitionFor("nonexistent");

        assertNull(result);
    }

    @Test
    void getDefinitionFor_shouldThrowException_whenServiceUnavailable() {
        when(restTemplate.getForObject(anyString(), eq(Entry.class)))
                .thenThrow(new RestClientException("Service unavailable"));

        assertThrows(RestClientException.class, () -> client.getDefinitionFor("test"));
    }

    @Test
    void getWordsStartingWith_shouldReturnList_whenEntriesExist() {
        Entry[] entries = {new Entry("apple", "fruit"), new Entry("app", "application")};
        when(restTemplate.getForEntity(anyString(), eq(Entry[].class)))
                .thenReturn(ResponseEntity.ok(entries));

        List<Entry> result = client.getWordsStartingWith("app");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(restTemplate).getForEntity("http://localhost:9091/getWordsStartingWith/app", Entry[].class);
    }

    @Test
    void getWordsStartingWith_shouldReturnEmptyList_whenNoMatches() {
        Entry[] entries = {};
        when(restTemplate.getForEntity(anyString(), eq(Entry[].class)))
                .thenReturn(ResponseEntity.ok(entries));

        List<Entry> result = client.getWordsStartingWith("xyz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getWordsStartingWith_shouldThrowNullPointerException_whenResponseBodyIsNull() {
        when(restTemplate.getForEntity(anyString(), eq(Entry[].class)))
                .thenReturn(ResponseEntity.ok(null));

        assertThrows(NullPointerException.class, () -> client.getWordsStartingWith("test"));
    }

    @Test
    void getWordsThatContain_shouldReturnList_whenEntriesExist() {
        Entry[] entries = {new Entry("test", "def"), new Entry("testing", "def2")};
        when(restTemplate.getForEntity(anyString(), eq(Entry[].class)))
                .thenReturn(ResponseEntity.ok(entries));

        List<Entry> result = client.getWordsThatContain("est");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getWordsThatContain_shouldReturnEmptyList_whenNoMatches() {
        Entry[] entries = {};
        when(restTemplate.getForEntity(anyString(), eq(Entry[].class)))
                .thenReturn(ResponseEntity.ok(entries));

        List<Entry> result = client.getWordsThatContain("xyz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getWordsEndingWith_shouldReturnList_whenEntriesExist() {
        Entry[] entries = {new Entry("running", "def"), new Entry("walking", "def2")};
        when(restTemplate.getForEntity(anyString(), eq(Entry[].class)))
                .thenReturn(ResponseEntity.ok(entries));

        List<Entry> result = client.getWordsEndingWith("ing");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(restTemplate).getForEntity("http://localhost:9091/getWordsEndingWith/ing", Entry[].class);
    }

    @Test
    void getWordsEndingWith_shouldReturnEmptyList_whenNoMatches() {
        Entry[] entries = {};
        when(restTemplate.getForEntity(anyString(), eq(Entry[].class)))
                .thenReturn(ResponseEntity.ok(entries));

        List<Entry> result = client.getWordsEndingWith("xyz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getWordsThatContainConsecutiveLetters_shouldReturnList_whenEntriesExist() {
        Entry[] entries = {new Entry("book", "def"), new Entry("deer", "def2")};
        when(restTemplate.getForEntity(anyString(), eq(Entry[].class)))
                .thenReturn(ResponseEntity.ok(entries));

        List<Entry> result = client.getWordsThatContainConsecutiveLetters();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(restTemplate).getForEntity("http://localhost:9091/getWordsThatContainConsecutiveLetters", Entry[].class);
    }

    @Test
    void getWordsThatContainConsecutiveLetters_shouldReturnEmptyList_whenNoMatches() {
        Entry[] entries = {};
        when(restTemplate.getForEntity(anyString(), eq(Entry[].class)))
                .thenReturn(ResponseEntity.ok(entries));

        List<Entry> result = client.getWordsThatContainConsecutiveLetters();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
