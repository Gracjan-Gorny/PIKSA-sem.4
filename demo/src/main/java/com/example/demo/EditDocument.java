package com.example.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;

@Route("edit-document")
public class EditDocument extends VerticalLayout implements HasUrlParameter<String> {

    private TextField title = new TextField("Tytuł");
    private TextField year = new TextField("Rok");
    private TextField author = new TextField("Autor");
    private TextField category = new TextField("Kategoria");
    private TextField content = new TextField("Zawartość");


    private DocumentRepository documentRepository = new DocumentRepository();
    private Document document;

    public EditDocument() {
        title.setWidth("50%");
        year.setWidth("50%");
        author.setWidth("50%");
        category.setWidth("50%");
        content.setWidth("50%");

        Button saveButton = new Button("Zapisz zmiany");
        saveButton.addClickListener(e -> {
            saveDocument();
        });

        Button backButton = new Button("Wstecz");
        backButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("view-document"));
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, backButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        add(buttonLayout);

        add(title, year, author, category, content);
    }

    @Override
    public void setParameter(BeforeEvent event, String parameter) {
        try {
            int documentId = Integer.parseInt(parameter);
            document = documentRepository.getAllDocuments().stream()
                    .filter(doc -> doc.getId() == documentId)
                    .findFirst()
                    .orElse(null);

            if (document != null) {
                title.setValue(document.getTitle());
                year.setValue(String.valueOf(document.getYear()));
                author.setValue(document.getAuthor());
                category.setValue(document.getCategory());
                content.setValue(document.getContent());
            } else {
                Notification.show("Dokument nie został znaleziony");
                getUI().ifPresent(ui -> ui.navigate("view-document"));
            }
        } catch (NumberFormatException e) {
            Notification.show("Nieprawidłowy format ID");
            getUI().ifPresent(ui -> ui.navigate("view-document"));
        }
    }

    private void saveDocument() {
        try {
            document.setTitle(title.getValue());
            document.setYear(Integer.parseInt(year.getValue()));
            document.setAuthor(author.getValue());
            document.setCategory(category.getValue());
            document.setContent(content.getValue());

            documentRepository.updateDocument(document);
            Notification.show("Dokument został zaktualizowany");
            getUI().ifPresent(ui -> ui.navigate("view-document"));
        } catch (NumberFormatException e) {
            Notification.show("Nieprawidłowy format roku");
        }
    }
}
