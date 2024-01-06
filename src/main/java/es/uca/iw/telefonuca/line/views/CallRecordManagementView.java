package es.uca.iw.telefonuca.line.views;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.SelectionMode;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.gridpro.GridPro;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import es.uca.iw.telefonuca.MainLayout;
import es.uca.iw.telefonuca.line.domain.CallRecord;
import es.uca.iw.telefonuca.line.services.CallRecordManagementService;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Gestión de registros de llamadas")
@Route(value = "callrecord-management", layout = MainLayout.class)
public class CallRecordManagementView extends Div {

    private final CallRecordManagementService callRecordManagementService;

    private GridPro<CallRecord> grid;
    private GridListDataView<CallRecord> gridListDataView;

    private Grid.Column<CallRecord> idColumn;
    private Grid.Column<CallRecord> senderColumn;
    private Grid.Column<CallRecord> receiverColumn;
    private Grid.Column<CallRecord> durationColumn;
    private Grid.Column<CallRecord> dateColumn;

    public CallRecordManagementView(CallRecordManagementService callRecordManagementService) {
        this.callRecordManagementService = callRecordManagementService;
        addClassName("data-grid-view");
        setSizeFull();
        createGrid();
        add(grid);
    }

    private void createGrid() {
        createGridComponent();
        addColumnsToGrid();
        addFiltersToGrid();
    }

    private void createGridComponent() {
        grid = new GridPro<>();
        grid.setSelectionMode(SelectionMode.MULTI);
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_COLUMN_BORDERS);
        grid.setHeight("100%");

        List<CallRecord> callRecords = callRecordManagementService.loadAll();
        gridListDataView = grid.setItems(callRecords);
    }

    private void addColumnsToGrid() {
        createIdColumn();
        createSenderColumn();
        createReceiverColumn();
        createDurationColumn();
        createDateColumn();
    }

    private void createIdColumn() {
        idColumn = grid.addColumn(new ComponentRenderer<>(callRecord -> {
            HorizontalLayout hl = new HorizontalLayout();
            hl.setAlignItems(Alignment.CENTER);
            Span span = new Span();
            span.setClassName("name");
            span.setText(callRecord.getId().toString());
            hl.add(span);
            return hl;
        })).setComparator(callRecord -> callRecord.getId()).setHeader("ID");
    }

    private void createSenderColumn() {
        senderColumn = grid
                .addColumn(CallRecord::getSender)
                .setHeader("Emisor");
    }

    private void createReceiverColumn() {
        receiverColumn = grid
                .addColumn(CallRecord::getReceiver)
                .setHeader("Receptor");
    }

    private void createDurationColumn() {
        durationColumn = grid
                .addColumn(callRecord -> {
                    long totalSeconds = callRecord.getDuration();
                    long hours = totalSeconds / 3600;
                    long minutes = (totalSeconds % 3600) / 60;
                    long seconds = totalSeconds % 60;
                    return String.format("%d:%02d:%02d", hours, minutes, seconds);
                })
                .setHeader("Duración");
    }

    private void createDateColumn() {
        dateColumn = grid
                .addColumn(new LocalDateRenderer<>(callRecord -> LocalDate.parse(callRecord.getDate().toString(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .setComparator(callRecord -> callRecord.getDate()).setHeader("Fecha").setWidth("180px")
                .setFlexGrow(0);
    }

    private void addFiltersToGrid() {
        HeaderRow filterRow = grid.appendHeaderRow();

        TextField idFilter = new TextField();
        idFilter.setPlaceholder("Filtro");
        idFilter.setClearButtonVisible(true);
        idFilter.setWidth("100%");
        idFilter.setValueChangeMode(ValueChangeMode.EAGER);
        idFilter.addValueChangeListener(event -> gridListDataView
                .addFilter(callRecord -> StringUtils.containsIgnoreCase(callRecord.getId().toString(),
                        idFilter.getValue())));
        filterRow.getCell(idColumn).setComponent(idFilter);

        TextField senderFilter = new TextField();
        senderFilter.setPlaceholder("Filtro");
        senderFilter.setClearButtonVisible(true);
        senderFilter.setWidth("100%");
        senderFilter.setValueChangeMode(ValueChangeMode.EAGER);
        senderFilter.addValueChangeListener(event -> gridListDataView.addFilter(callRecord -> StringUtils
                .containsIgnoreCase(Integer.toString(callRecord.getSender()), senderFilter.getValue())));
        filterRow.getCell(senderColumn).setComponent(senderFilter);

        TextField receiverFilter = new TextField();
        receiverFilter.setPlaceholder("Filtro");
        receiverFilter.setClearButtonVisible(true);
        receiverFilter.setWidth("100%");
        receiverFilter.setValueChangeMode(ValueChangeMode.EAGER);
        receiverFilter.addValueChangeListener(event -> gridListDataView.addFilter(callRecord -> StringUtils
                .containsIgnoreCase(Integer.toString(callRecord.getReceiver()), receiverFilter.getValue())));
        filterRow.getCell(receiverColumn).setComponent(receiverFilter);

        TextField durationFilter = new TextField();
        durationFilter.setPlaceholder("Filtro");
        durationFilter.setClearButtonVisible(true);
        durationFilter.setWidth("100%");
        durationFilter.setValueChangeMode(ValueChangeMode.EAGER);
        durationFilter.addValueChangeListener(event -> gridListDataView.addFilter(callRecord -> StringUtils
                .containsIgnoreCase(Long.toString(callRecord.getDuration()), durationFilter.getValue())));
        filterRow.getCell(durationColumn).setComponent(durationFilter);

        DatePicker dateFilter = new DatePicker();
        dateFilter.setPlaceholder("Filtro");
        dateFilter.setClearButtonVisible(true);
        dateFilter.setWidth("100%");
        dateFilter.addValueChangeListener(
                event -> gridListDataView.addFilter(callRecord -> areDatesEqual(callRecord, dateFilter)));
        filterRow.getCell(dateColumn).setComponent(dateFilter);
    }

    private boolean areDatesEqual(CallRecord callRecord, DatePicker dateFilter) {
        LocalDate dateFilterValue = dateFilter.getValue();
        if (dateFilterValue != null) {
            LocalDate callRecordDate = LocalDate.parse(callRecord.getDate().toString());
            return dateFilterValue.equals(callRecordDate);
        }
        return true;
    }

}
