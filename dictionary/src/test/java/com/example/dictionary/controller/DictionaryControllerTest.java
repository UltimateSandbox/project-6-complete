package com.example.dictionary.controller;

import com.example.dictionary.exception.WordNotFoundException;
import com.example.dictionary.model.Entry;
import com.example.dictionary.service.DictionaryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DictionaryControllerTest {

    @Mock
    private DictionaryService dictionaryService;

    private DictionaryController controller;

    @BeforeEach
    void setUp() {
        controller = new DictionaryController(dictionaryService);
    }

    @Test
    void getWord_shouldReturnEntry_whenWordExists() throws WordNotFoundException {
        Entry expected = new Entry("test", "definition");
        when(dictionaryService.getWord("test")).thenReturn(expected);

        Entry result = controller.getWord("test");

        assertNotNull(result);
        assertEquals("test", result.getWord());
        verify(dictionaryService).getWord("test");
    }

    @Test
    void getWord_shouldThrowException_whenWordNotFound() throws WordNotFoundException {
        when(dictionaryService.getWord("nonexistent")).thenThrow(new WordNotFoundException("Word not found"));

        assertThrows(WordNotFoundException.class, () -> controller.getWord("nonexistent"));
    }

    @Test
    void getWord_shouldHandleEmptyString() throws WordNotFoundException {
        Entry expected = new Entry("", "empty");
        when(dictionaryService.getWord("")).thenReturn(expected);

        Entry result = controller.getWord("");

        assertNotNull(result);
        assertEquals("", result.getWord());
    }

    @Test
    void getWordsStartingWith_shouldReturnList_whenEntriesExist() {
        List<Entry> entries = Arrays.asList(
                new Entry("apple", "fruit"),
                new Entry("app", "application")
        );
        when(dictionaryService.getWordsStartingWith("app")).thenReturn(entries);

        List<Entry> result = controller.getWordsStartingWith("app");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(dictionaryService).getWordsStartingWith("app");
    }

    @Test
    void getWordsStartingWith_shouldReturnEmptyList_whenNoMatches() {
        when(dictionaryService.getWordsStartingWith("xyz")).thenReturn(Collections.emptyList());

        List<Entry> result = controller.getWordsStartingWith("xyz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getWordsThatContain_shouldReturnList_whenEntriesExist() {
        List<Entry> entries = Arrays.asList(
                new Entry("testing", "def1"),
                new Entry("test", "def2")
        );
        when(dictionaryService.getWordsThatContain("est")).thenReturn(entries);

        List<Entry> result = controller.getWordsThatContain("est");

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getWordsThatContain_shouldReturnEmptyList_whenNoMatches() {
        when(dictionaryService.getWordsThatContain("xyz")).thenReturn(Collections.emptyList());

        List<Entry> result = controller.getWordsThatContain("xyz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getWordsEndingWith_shouldReturnList_whenEntriesExist() {
        List<Entry> entries = Arrays.asList(
                new Entry("running", "def1"),
                new Entry("walking", "def2")
        );
        when(dictionaryService.getWordsEndingWith("ing")).thenReturn(entries);

        List<Entry> result = controller.getWordsEndingWith("ing");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(dictionaryService).getWordsEndingWith("ing");
    }

    @Test
    void getWordsEndingWith_shouldReturnEmptyList_whenNoMatches() {
        when(dictionaryService.getWordsEndingWith("xyz")).thenReturn(Collections.emptyList());

        List<Entry> result = controller.getWordsEndingWith("xyz");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getWordsThatContainConsecutiveLetters_shouldReturnList_whenEntriesExist() {
        List<Entry> entries = Arrays.asList(
                new Entry("book", "def1"),
                new Entry("deer", "def2")
        );
        when(dictionaryService.getWordsThatContainConsecutiveDoubleLetters()).thenReturn(entries);

        List<Entry> result = controller.getWordsThatContainConsecutiveLetters();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(dictionaryService).getWordsThatContainConsecutiveDoubleLetters();
    }

    @Test
    void getWordsThatContainConsecutiveLetters_shouldReturnEmptyList_whenNoMatches() {
        when(dictionaryService.getWordsThatContainConsecutiveDoubleLetters()).thenReturn(Collections.emptyList());

        List<Entry> result = controller.getWordsThatContainConsecutiveLetters();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void entryByWord_shouldReturnEntry_whenWordExists() throws WordNotFoundException {
        Entry expected = new Entry("graphql", "definition");
        when(dictionaryService.getWord("graphql")).thenReturn(expected);

        Entry result = controller.entryByWord("graphql");

        assertNotNull(result);
        assertEquals("graphql", result.getWord());
        verify(dictionaryService).getWord("graphql");
    }

    @Test
    void entryByWord_shouldThrowException_whenWordNotFound() throws WordNotFoundException {
        when(dictionaryService.getWord(anyString())).thenThrow(new WordNotFoundException("Word not found"));

        assertThrows(WordNotFoundException.class, () -> controller.entryByWord("nonexistent"));
    }

    @Test
    void getWordsStartingWith_shouldHandleSingleCharacter() {
        List<Entry> entries = Arrays.asList(new Entry("a", "letter"));
        when(dictionaryService.getWordsStartingWith("a")).thenReturn(entries);

        List<Entry> result = controller.getWordsStartingWith("a");

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}
