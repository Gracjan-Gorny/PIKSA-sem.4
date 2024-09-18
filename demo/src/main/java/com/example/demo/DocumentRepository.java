package com.example.demo;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DocumentRepository {

    private static final String JSON_FILE_PATH = "documents.json";
    private static ObjectMapper mapper = new ObjectMapper()
            .enable(SerializationFeature.INDENT_OUTPUT);

    private List<Document> documents;

    public DocumentRepository() {
        documents = readDocumentsFromFile();
    }

    public List<Document> getAllDocuments() {
        return documents;
    }

    public void addDocument(Document document) {
        documents.add(document);
        saveDocumentsToFile();
    }

    public void updateDocument(Document updatedDocument) {
        for (int i = 0; i < documents.size(); i++) {
            if (documents.get(i).getId() == updatedDocument.getId()) {
                documents.set(i, updatedDocument);
                saveDocumentsToFile();
                return;
            }
        }
    }

    public void deleteDocument(Document document) {
        documents.remove(document);
        saveDocumentsToFile();
    }

    private List<Document> readDocumentsFromFile() {
        File file = new File(JSON_FILE_PATH);
        if (file.exists()) {
            try {
                return mapper.readValue(file, new TypeReference<List<Document>>() {});
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new ArrayList<>();
    }

    private void saveDocumentsToFile() {
        try {
            mapper.writeValue(new File(JSON_FILE_PATH), documents);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}