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
import es.uca.iw.telefonuca.line.domain.CallRecord;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.services.CallRecordManagementService;
import es.uca.iw.telefonuca.line.services.CustomerLineManagementService;
import es.uca.iw.telefonuca.line.services.LineManagementService;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;

@RolesAllowed("CUSTOMER")
@PageTitle("Mi registro de llamadas")
@Route(value = "my-call-records", layout = MainLayout.class)
public class CallRecordUserView extends VerticalLayout {

    private final CallRecordManagementService callRecordManagementService;
    private final AuthenticatedUser authenticatedUser;

    private final Grid<CallRecord> grid = new Grid<>(CallRecord.class);
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private TextField numberOfCallsField;
    private TextField totalDurationField;

    public CallRecordUserView(CallRecordManagementService callRecordManagementService,
            AuthenticatedUser authenticatedUser) {
        this.callRecordManagementService = callRecordManagementService;
        this.authenticatedUser = authenticatedUser;

        buildUI();
        filterCallRecords(); // Load all calls and compute stats when the view is first accessed
    }

    private void buildUI() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        startDatePicker = new DatePicker("Fecha de inicio");
        endDatePicker = new DatePicker("Fecha de fin");

        HorizontalLayout datePickersLayout = new HorizontalLayout(startDatePicker, endDatePicker);
        datePickersLayout.setAlignItems(Alignment.BASELINE);

        Button filterButton = new Button("Filtrar", click -> filterCallRecords());
        filterButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        numberOfCallsField = new TextField("Número de llamadas");
        numberOfCallsField.setReadOnly(true);

        totalDurationField = new TextField("Tiempo total");
        totalDurationField.setReadOnly(true);

        HorizontalLayout statsLayout = new HorizontalLayout(numberOfCallsField, totalDurationField);
        statsLayout.setAlignItems(Alignment.BASELINE);

        grid.setSizeFull();
        grid.setColumns("sender", "receiver", "duration", "date"); // Replace with actual property names
        // ... Add additional grid configuration if necessary ...

        add(datePickersLayout, filterButton, statsLayout, grid);
    }

    private void filterCallRecords() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isEmpty()) {
            Notification.show("No se pudo obtener el usuario autenticado.", 3000, Notification.Position.MIDDLE);
            return;
        }
        User user = maybeUser.get();
        List<CallRecord> callRecords;

        // If no dates are selected, load all call records for the user
        if (startDate == null || endDate == null) {
            callRecords = callRecordManagementService.loadCallRecordByUserId(user.getId());
        } else {
            callRecords = callRecordManagementService.loadCallRecordByUserIdAndDates(user.getId(), startDate, endDate);
        }

        grid.setItems(callRecords);
        updateStats(callRecords);
    }

    private void updateStats(List<CallRecord> callRecords) {
        int numberOfCalls = callRecords.size();
        long totalDurationInSeconds = callRecords.stream().mapToLong(CallRecord::getDuration).sum();

        long hours = totalDurationInSeconds / 3600;
        long minutes = (totalDurationInSeconds % 3600) / 60;
        long seconds = totalDurationInSeconds % 60;

        numberOfCallsField.setValue(String.valueOf(numberOfCalls));
        totalDurationField.setValue(String.format("%02d:%02d:%02d", hours, minutes, seconds));
    }
}
