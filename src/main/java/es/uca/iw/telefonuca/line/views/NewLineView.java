package es.uca.iw.telefonuca.line.views;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.NativeLabel;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.line.domain.Line;
import es.uca.iw.telefonuca.line.services.LineManagementService;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Nueva línea")
@Route(value = "/lines/new", layout = MainLayout.class)
public class NewLineView extends Composite<VerticalLayout> {

    private final LineManagementService lineManagementService;
    private NativeLabel status = new NativeLabel(); // Variable de estado

    TextField name = new TextField();
    TextField description = new TextField();
    IntegerField pricePerMinute = new IntegerField();
    IntegerField pricePerMegabyte = new IntegerField();
    IntegerField minimumMonths = new IntegerField();
    IntegerField freeMinutes = new IntegerField();
    IntegerField freeMegabytes = new IntegerField();
    Button saveButton = new Button("Guardar", event -> saveLine());
    Button resetButton = new Button("Limpiar", event -> clearFields());

    public NewLineView(LineManagementService lineManagementService) {
        this.lineManagementService = lineManagementService;

        VerticalLayout layoutColumn2 = new VerticalLayout();
        H3 h3 = new H3();
        FormLayout formLayout2Col = new FormLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.START);
        getContent().setAlignItems(Alignment.CENTER);
        layoutColumn2.setWidth("100%");
        layoutColumn2.setMaxWidth("800px");
        layoutColumn2.setHeight("min-content");
        h3.setText("Nueva línea a ofertar");
        h3.setWidth("100%");
        formLayout2Col.setWidth("100%");
        name.setLabel("Nombre del plan");
        description.setLabel("Descripción");
        pricePerMinute.setLabel("Precio por minuto de llamada");
        pricePerMegabyte.setLabel("Precio por megabyte consumido");
        minimumMonths.setLabel("Meses mínimos de permanencia");
        freeMinutes.setLabel("Minutos gratis");
        freeMegabytes.setLabel("Megabytes gratis");
        status.getElement().getStyle().set("margin-left", "1em");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");
        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        resetButton.setWidth("min-content");
        getContent().add(layoutColumn2);
        layoutColumn2.add(h3);
        layoutColumn2.add(formLayout2Col);
        formLayout2Col.add(name);
        formLayout2Col.add(description);
        formLayout2Col.add(pricePerMinute);
        formLayout2Col.add(pricePerMegabyte);
        formLayout2Col.add(minimumMonths);
        formLayout2Col.add(freeMinutes);
        formLayout2Col.add(freeMegabytes);

        layoutColumn2.add(layoutRow);
        layoutRow.add(saveButton);
        layoutRow.add(resetButton);
        layoutRow.add(status);

    }

    private void saveLine() {
        Line line = new Line();
        line.setName(name.getValue());
        line.setDescription(description.getValue());
        line.setPricePerMinute(pricePerMinute.getValue());
        line.setPricePerMegabyte(pricePerMegabyte.getValue());
        line.setMinimumMonths(minimumMonths.getValue());
        line.setFreeMinutes(freeMinutes.getValue());
        line.setFreeMegabytes(freeMegabytes.getValue());

        try {
            lineManagementService.saveLine(line);
            status.setText("Línea guardada con éxito.");
            status.getStyle().set("color", "green"); // Estilo para el mensaje de éxito
        } catch (Exception e) {
            status.setText("Error al guardar la línea.");
            status.getStyle().set("color", "red"); // Estilo para el mensaje de error
        }
    }

    private void clearFields() {
        name.clear();
        description.clear();
        pricePerMinute.clear();
        pricePerMegabyte.clear();
        minimumMonths.clear();
        freeMinutes.clear();
        freeMegabytes.clear();
    }

}