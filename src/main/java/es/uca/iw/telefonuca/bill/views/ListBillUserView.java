package es.uca.iw.telefonuca.bill.views;

import java.io.IOException;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.services.ContractManagementService;
import es.uca.iw.telefonuca.bill.domain.Bill;
import es.uca.iw.telefonuca.bill.services.BillManagementService;
import es.uca.iw.telefonuca.line.services.CustomerLineManagementService;

import com.itextpdf.text.DocumentException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.line.services.CallRecordManagementService;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.security.AuthenticatedUser;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Mis facturas")
@Route(value = "my-bills", layout = MainLayout.class)
public class ListBillUserView extends VerticalLayout {

    private final AuthenticatedUser authenticatedUser;

    private ComboBox<Contract> contractComboBox;
    private ComboBox<Month> monthDatePicker;
    private DatePicker yearDatePicker;
    private Grid<Bill> billGrid;

    private final ContractManagementService contractManagementService;
    private final BillManagementService billManagementService;

    public ListBillUserView(ContractManagementService contractManagementService,
            CustomerLineManagementService customerLineManagementService,
            BillManagementService billManagementService,
            CallRecordManagementService callRecordManagementService,
            AuthenticatedUser authenticatedUser) {
        this.contractManagementService = contractManagementService;
        this.billManagementService = billManagementService;
        this.authenticatedUser = authenticatedUser;

        buildUI();
        filterBills();
    }

    private void buildUI() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        contractComboBox = new ComboBox<>("Contracts");
        monthDatePicker = new ComboBox<Month>("Month");
        yearDatePicker = new DatePicker("Year");
        billGrid = new Grid<>(Bill.class);

        contractComboBox.setItemLabelGenerator(Contract::getIdAsString);
        contractComboBox.setClearButtonVisible(true);

        monthDatePicker.setItems(Month.values());
        monthDatePicker.setPlaceholder("Month");

        yearDatePicker.setPlaceholder("Year");
        yearDatePicker.setMin(LocalDate.of(2000, 1, 1));
        yearDatePicker.setMax(LocalDate.now());

        billGrid.setSizeFull();
        billGrid.setColumns("contractId", "year", "month", "totalMinutes", "totalMegabytes");
        ValueProvider<Bill, String> totalValueProvider = bill -> {
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.FRANCE);
            currencyFormat.setMaximumFractionDigits(2);
            return currencyFormat.format(bill.getTotal() / 100);
        };

        Grid.Column<Bill> totalColumn = billGrid.addColumn(totalValueProvider);
        totalColumn.setHeader("Total");

        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            List<Contract> contracts = contractManagementService.loadContractsByOwnerId(user.getId());
            contractComboBox.setItems(contracts);
        }

        HorizontalLayout filtersLayout = new HorizontalLayout(contractComboBox, monthDatePicker, yearDatePicker);
        filtersLayout.setAlignItems(Alignment.BASELINE);

        Button filterButton = new Button("Filtrar", click -> filterBills());
        filterButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button generateBillButton = new Button("Generar Factura", click -> generateBill());
        generateBillButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button sendEmailButton = new Button("Enviar a mi correo electrónico", click -> sendEmail());
        sendEmailButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonsLayout = new HorizontalLayout(filterButton, generateBillButton, sendEmailButton);
        buttonsLayout.setSpacing(true);

        add(filtersLayout, buttonsLayout, billGrid);
    }

    private void filterBills() {
        Optional<User> maybeUser = authenticatedUser.get();
        if (maybeUser.isEmpty()) {
            Notification.show("No se pudo obtener el usuario autenticado.", 3000, Notification.Position.MIDDLE);
            return;
        }
        User user = maybeUser.get();

        List<Contract> contracts = contractManagementService.loadContractsByOwnerId(user.getId());
        List<UUID> contractIds = contracts.stream().map(Contract::getId).collect(Collectors.toList());

        List<Bill> bills = billManagementService.loadByContractIdIn(contractIds);
        billGrid.setItems(bills);
    }

    private void generateBill() {
        Contract selectedContract = contractComboBox.getValue();
        Month selectedMonth = monthDatePicker.getValue();
        int selectedYear = yearDatePicker.getValue().getYear();

        if (selectedContract != null && selectedMonth != null && selectedYear > 0) {
            int monthAsInt = selectedMonth.ordinal() + 1; // Add 1 because January is 0
            Bill bill = billManagementService.generateBill(selectedContract.getId(), selectedYear, monthAsInt);
            billManagementService.saveBill(bill);
        } else {
            Notification.show("Por favor selecciona un contrato, un mes y un año.", 3000, Notification.Position.MIDDLE);
        }
    }

    private void sendEmail() {
        Contract selectedContract = contractComboBox.getValue();
        Month selectedMonth = monthDatePicker.getValue();
        int selectedYear = yearDatePicker.getValue().getYear();

        if (selectedContract != null && selectedMonth != null && selectedYear > 0) {
            int monthAsInt = selectedMonth.ordinal() + 1; // Add 1 because January is 0
            Optional<User> maybeUser = authenticatedUser.get();
            if (maybeUser.isPresent()) {
                User user = maybeUser.get();
                try {
                    Bill bill = billManagementService.generateBill(selectedContract.getId(), selectedYear, monthAsInt);
                    billManagementService.generateAndSendPdfToEmailFromBill(bill, user.getEmail());
                } catch (DocumentException e) {
                    // Handle DocumentException
                    e.printStackTrace();
                } catch (IOException e) {
                    // Handle IOException
                    e.printStackTrace();
                }
            }
        } else {
            Notification.show("Por favor selecciona un contrato, un mes y un año.", 3000, Notification.Position.MIDDLE);
        }
    }

}
