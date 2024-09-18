package com.example.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

@Route("add-document")
public class AddDocument extends VerticalLayout {

    private TextField title = new TextField("Tytuł");
    private TextField year = new TextField("Rok");
    private TextField author = new TextField("Autor");
    private TextField category = new TextField("Kategoria");
    private TextArea content = new TextArea("Zawartość");

    private DocumentRepository documentRepository = new DocumentRepository();
    private int lastUsedId = 0;

    public AddDocument() {
        title.setWidth("50%");
        year.setWidth("50%");
        author.setWidth("50%");
        category.setWidth("50%");
        content.setWidth("50%");

        lastUsedId = documentRepository.getAllDocuments().stream()
                .mapToInt(Document::getId)
                .max()
                .orElse(0);

        Button saveButton = new Button("Zapisz dokument");
        saveButton.addClickListener(e -> saveDocument());

        Button backButton = new Button("Wstecz");
        backButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("")));

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, backButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        add(buttonLayout, title, year, author, category, content);
    }

    private void saveDocument() {
        String documentTitle = title.getValue();
        int documentYear = Integer.parseInt(year.getValue());
        String documentAuthor = author.getValue();
        String documentCategory = category.getValue();
        String documentContent = content.getValue();

        lastUsedId++;

        Document newDocument = new Document(lastUsedId, documentTitle, documentYear, documentAuthor, documentCategory, documentContent);

        documentRepository.addDocument(newDocument);

        Notification.show("Dokument zapisany pomyślnie!");

        title.clear();
        year.clear();
        author.clear();
        category.clear();
        content.clear();
    }
}
