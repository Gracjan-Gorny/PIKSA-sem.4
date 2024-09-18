package com.example.demo;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

@Route("")
public class MainView extends VerticalLayout {

    public MainView() {
        Button addButton = new Button("Dodaj dokument");
        Button viewButton = new Button("Wyświetl dokumenty");

        addButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("add-document"));
        });
        viewButton.addClickListener(e -> {
            getUI().ifPresent(ui -> ui.navigate("view-document"));
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(addButton, viewButton);
        buttonLayout.setWidthFull();
        buttonLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        add(buttonLayout);
        setAlignItems(Alignment.CENTER);
    }
}
