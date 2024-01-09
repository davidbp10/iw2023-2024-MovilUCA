package es.uca.iw.telefonuca.line.views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.services.ContractManagementService;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.domain.DataRecord;
import es.uca.iw.telefonuca.line.services.CustomerLineManagementService;
import es.uca.iw.telefonuca.line.services.DataRecordManagementService;
import es.uca.iw.telefonuca.line.services.LineManagementService;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("CUSTOMER")
@PageTitle("Mi consumo de datos")
@Route(value = "my-data-records", layout = MainLayout.class)
public class DataRecordUserView extends VerticalLayout {

    private final DataRecordManagementService dataRecordManagementService;
    private final AuthenticatedUser authenticatedUser;

    private final Grid<DataRecord> grid = new Grid<>(DataRecord.class);
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private TextField numberOfDataTransmissionsField;
    private TextField totalMegabytesField;

    public DataRecordUserView(DataRecordManagementService dataRecordManagementService,
            AuthenticatedUser authenticatedUser) {
        this.dataRecordManagementService = dataRecordManagementService;
        this.authenticatedUser = authenticatedUser;

        buildUI();
        filterDataRecords(); // Load all calls and compute stats when the view is first accessed
    }

    private void buildUI() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        startDatePicker = new DatePicker("Fecha de inicio");
        endDatePicker = new DatePicker("Fecha de fin");

        HorizontalLayout datePickersLayout = new HorizontalLayout(startDatePicker, endDatePicker);
        datePickersLayout.setAlignItems(Alignment.BASELINE);

        Button filterButton = new Button("Filtrar", click -> filterDataRecords());
        filterButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        numberOfDataTransmissionsField = new TextField("Número de transmisiones de datos");
        numberOfDataTransmissionsField.setReadOnly(true);

        totalMegabytesField = new TextField("Cantidad de megabytes consumidos");
        totalMegabytesField.setReadOnly(true);

        HorizontalLayout statsLayout = new HorizontalLayout(numberOfDataTransmissionsField, totalMegabytesField);
        statsLayout.setAlignItems(Alignment.BASELINE);

        grid.setSizeFull();
        grid.setColumns("phoneNumber", "megabytes", "date"); // Replace with actual property names
        // ... Add additional grid configuration if necessary ...

        add(datePickersLayout, filterButton, statsLayout, grid);
    }

    private void filterDataRecords() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isEmpty()) {
            Notification.show("No se pudo obtener el usuario autenticado.", 3000, Notification.Position.MIDDLE);
            return;
        }
        User user = maybeUser.get();
        List<DataRecord> dataRecords;

        // If no dates are selected, load all call records for the user
        if (startDate == null || endDate == null) {
            dataRecords = dataRecordManagementService.loadDataRecordByUserId(user.getId());
        } else {
            dataRecords = dataRecordManagementService.loadDataRecordByUserIdAndDates(user.getId(), startDate, endDate);
        }

        grid.setItems(dataRecords);
        updateStats(dataRecords);
    }

    private void updateStats(List<DataRecord> dataRecords) {
        int numberOfDataTransmissions = dataRecords.size();
        long totalMegabytes = dataRecords.stream().mapToLong(DataRecord::getMegabytes).sum();

        // Convert total megabytes to gigabytes and megabytes
        long totalGigabytes = totalMegabytes / 1024;
        long remainingMegabytes = totalMegabytes % 1024;

        numberOfDataTransmissionsField.setValue(String.valueOf(numberOfDataTransmissions));
        totalMegabytesField.setValue(String.format("%d GB %d MB", totalGigabytes, remainingMegabytes));
    }

}
