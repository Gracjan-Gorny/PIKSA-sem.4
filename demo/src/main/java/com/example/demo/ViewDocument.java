package com.example.demo;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Route("view-document")
public class ViewDocument extends VerticalLayout {

    private DocumentRepository documentRepository = new DocumentRepository();
    private Grid<Document> grid = new Grid<>();
    private Select<String> searchFieldSelect;
    private TextField searchTextField;
    private Button searchButton;

    public ViewDocument() {
        Button backButton = new Button("Wstecz");
        backButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate(""));
        });

        HorizontalLayout backLayout = new HorizontalLayout(backButton);
        backLayout.setWidthFull();
        backLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        searchButton = new Button("Wyszukaj dokument");
        searchButton.addClickListener(e -> {
            searchDocuments();
        });

        searchFieldSelect = new Select<>();
        searchFieldSelect.setItems("ID", "Tytuł", "Rok", "Autor", "Kategoria");
        searchFieldSelect.setValue("ID");

        searchTextField = new TextField();
        searchTextField.setPlaceholder("Wpisz szukaną wartość");

        HorizontalLayout searchLayout = new HorizontalLayout(searchFieldSelect, searchTextField, searchButton);
        searchLayout.setWidthFull();
        searchLayout.setAlignItems(Alignment.BASELINE);

        setupGrid();

        add(backLayout, searchLayout, grid);
    }

    private void setupGrid() {
        grid.addColumn(Document::getId)
                .setHeader("ID")
                .setSortable(true)
                .setComparator(Comparator.comparing(Document::getId));
        grid.addColumn(Document::getTitle)
                .setHeader("Tytuł")
                .setSortable(true)
                .setComparator(Comparator.comparing(Document::getTitle));
        grid.addColumn(Document::getYear)
                .setHeader("Rok")
                .setSortable(true)
                .setComparator(Comparator.comparing(Document::getYear));
        grid.addColumn(Document::getAuthor)
                .setHeader("Autor")
                .setSortable(true)
                .setComparator(Comparator.comparing(Document::getAuthor));
        grid.addColumn(Document::getCategory)
                .setHeader("Kategoria")
                .setSortable(true)
                .setComparator(Comparator.comparing(Document::getCategory));

        grid.addComponentColumn(document -> {
            Button deleteButtonForDocument = new Button("Usuń");
            deleteButtonForDocument.addClickListener(e -> {
                deleteSelectedDocument(document);
            });
            return deleteButtonForDocument;
        });

        grid.addComponentColumn(document -> {
            Button editButtonForDocument = new Button("Edytuj");
            editButtonForDocument.addClickListener(e -> {
                getUI().ifPresent(ui -> ui.navigate("edit-document/" + document.getId()));
            });
            return editButtonForDocument;
        });

        grid.asSingleSelect().addValueChangeListener(event -> {
            Document selectedDocument = event.getValue();
            if (selectedDocument != null) {
                showDocumentContent(selectedDocument);
            }
        });

        grid.setItems(documentRepository.getAllDocuments());
    }

    private void searchDocuments() {
        String searchField = searchFieldSelect.getValue();
        String searchText = searchTextField.getValue().toLowerCase().trim();

        List<Document> searchResults;

        if (searchText.isEmpty()) {
            searchResults = documentRepository.getAllDocuments();
        } else {
            switch (searchField) {
                case "ID":
                    try {
                        int id = Integer.parseInt(searchText);
                        searchResults = documentRepository.getAllDocuments().stream()
                                .filter(doc -> doc.getId() == id)
                                .collect(Collectors.toList());
                    } catch (NumberFormatException e) {
                        Notification.show("Nieprawidłowy format ID");
                        return;
                    }
                    break;
                case "Tytuł":
                    searchResults = documentRepository.getAllDocuments().stream()
                            .filter(doc -> doc.getTitle().toLowerCase().contains(searchText))
                            .collect(Collectors.toList());
                    break;
                case "Rok":
                    try {
                        int year = Integer.parseInt(searchText);
                        searchResults = documentRepository.getAllDocuments().stream()
                                .filter(doc -> doc.getYear() == year)
                                .collect(Collectors.toList());
                    } catch (NumberFormatException e) {
                        Notification.show("Nieprawidłowy format roku");
                        return;
                    }
                    break;
                case "Autor":
                    searchResults = documentRepository.getAllDocuments().stream()
                            .filter(doc -> doc.getAuthor().toLowerCase().contains(searchText))
                            .collect(Collectors.toList());
                    break;
                case "Kategoria":
                    searchResults = documentRepository.getAllDocuments().stream()
                            .filter(doc -> doc.getCategory().toLowerCase().contains(searchText))
                            .collect(Collectors.toList());
                    break;
                default:
                    Notification.show("Nieznane pole wyszukiwania");
                    return;
            }
        }

        grid.setItems(searchResults);
    }

    private void deleteSelectedDocument(Document document) {
        documentRepository.deleteDocument(document);
        grid.setItems(documentRepository.getAllDocuments());
        Notification.show("Dokument został usunięty");
    }

    private void showDocumentContent(Document document) {
        Dialog dialog = new Dialog();
        dialog.setWidth("50%");
        dialog.setHeight("50%");

        Span contentSpan = new Span (document.getContent());
        contentSpan.getStyle().set("white-space", "pre-wrap");

        Button closeButton = new Button("Zamknij", event -> dialog.close());
        VerticalLayout dialogLayout = new VerticalLayout(contentSpan, closeButton);
        dialog.add(dialogLayout);

        dialog.open();
    }
}
