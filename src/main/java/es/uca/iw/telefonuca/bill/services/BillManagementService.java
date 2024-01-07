package es.uca.iw.telefonuca.bill.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import es.uca.iw.telefonuca.bill.domain.Bill;
import es.uca.iw.telefonuca.bill.repositories.BillRepository;
import es.uca.iw.telefonuca.line.domain.CallRecord;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.domain.DataRecord;
import es.uca.iw.telefonuca.line.repositories.CallRecordRepository;
import es.uca.iw.telefonuca.line.repositories.CustomerLineRepository;
import es.uca.iw.telefonuca.line.repositories.DataRecordRepository;
import es.uca.iw.telefonuca.user.services.EmailService;
import es.uca.iw.telefonuca.user.services.UserManagementService;
import jakarta.transaction.Transactional;

@Service
public class BillManagementService {
    private final BillRepository billRepository;
    private final CustomerLineRepository customerLineRepository;
    private final CallRecordRepository callRecordRepository;
    private final DataRecordRepository dataRecordRepository;
    private final EmailService emailService;
    private final UserManagementService userManagementService;

    public BillManagementService(BillRepository billRepository, CustomerLineRepository customerLineRepository,
            CallRecordRepository callRecordRepository, DataRecordRepository dataRecordRepository,
            EmailService emailService,
            UserManagementService userManagementService) {
        this.billRepository = billRepository;
        this.customerLineRepository = customerLineRepository;
        this.callRecordRepository = callRecordRepository;
        this.dataRecordRepository = dataRecordRepository;
        this.emailService = emailService;
        this.userManagementService = userManagementService;

    }

    @Transactional
    public Bill loadById(UUID id) throws Exception {
        Optional<Bill> bill = billRepository.findById(id);
        if (bill.isEmpty()) {
            throw new Exception("No bill present with id: " + id);
        } else {
            return bill.get();
        }
    }

    @Transactional
    public Bill loadByContractIdAndYearAndMonth(UUID contractId, int year, int month) throws Exception {
        Optional<Bill> bill = billRepository.findByContractIdAndYearAndMonth(contractId, year, month);
        if (bill.isEmpty()) {
            throw new Exception(
                    "No bill present with contractId: " + contractId + " year: " + year + " month: " + month);
        } else {
            return bill.get();
        }
    }

    @Transactional
    public List<Bill> loadByContractIdAndYearAndMonthGreaterThanEqual(UUID contractId, int year, int month)
            throws Exception {
        List<Bill> bills = billRepository.findByContractIdAndYearAndMonthGreaterThanEqual(contractId, year, month);
        if (bills.isEmpty()) {
            throw new Exception(
                    "No bill present with contractId: " + contractId + " year: " + year + " month: " + month);
        } else {
            return bills;
        }
    }

    @Transactional
    public List<Bill> loadByContractIdAndYearAndMonthLessThanEqual(UUID contractId, int year, int month)
            throws Exception {
        List<Bill> bills = billRepository.findByContractIdAndYearAndMonthLessThanEqual(contractId, year, month);
        if (bills.isEmpty()) {
            throw new Exception(
                    "No bill present with contractId: " + contractId + " year: " + year + " month: " + month);
        } else {
            return bills;
        }
    }

    @Transactional
    public List<Bill> loadByContractIdAndYear(UUID contractId, int year) throws Exception {
        List<Bill> bills = billRepository.findByContractIdAndYear(contractId, year);
        if (bills.isEmpty()) {
            throw new Exception("No bill present with contractId: " + contractId + " year: " + year);
        } else {
            return bills;
        }
    }

    @Transactional
    public List<Bill> loadByContractId(UUID contractId) throws Exception {
        List<Bill> bills = billRepository.findByContractId(contractId);
        if (bills.isEmpty()) {
            throw new Exception("No bill present with contractId: " + contractId);
        } else {
            return bills;
        }
    }

    @Transactional
    public List<Bill> loadAll() {
        return billRepository.findAll();
    }

    @Transactional
    public void delete(Bill bill) {
        billRepository.delete(bill);
    }

    @Transactional
    public int count() {
        return (int) billRepository.count();
    }

    @Transactional
    public Bill save(Bill bill) {
        return billRepository.save(bill);
    }

    @Transactional
    public String generateBill(Bill bill) {
        // Obtén todas las líneas de cliente para el contrato de la factura
        List<CustomerLine> customerLines = customerLineRepository.findByContractId(bill.getContractId());

        StringBuilder billText = new StringBuilder();
        billText.append("Detalles del usuario...\n");

        // Cálculo del total general
        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (CustomerLine customerLine : customerLines) {
            // Calcula el primer y último día del mes de la factura
            LocalDate startOfMonth = YearMonth.of(bill.getYear(), bill.getMonth()).atDay(1);
            LocalDate endOfMonth = YearMonth.of(bill.getYear(), bill.getMonth()).atEndOfMonth();

            // Obtén todos los registros de llamadas para el número de teléfono de la línea
            // de cliente durante el mes de la factura
            List<CallRecord> callRecords = callRecordRepository
                    .findBySenderAndDateBetween(customerLine.getPhoneNumber(), startOfMonth, endOfMonth);
            callRecords.addAll(callRecordRepository.findByReceiverAndDateBetween(customerLine.getPhoneNumber(),
                    startOfMonth, endOfMonth));

            // Obtiene todos los registros de datos para el número de teléfono de la línea
            // de cliente durante el mes de la factura
            List<DataRecord> dataRecords = dataRecordRepository
                    .findByPhoneNumberAndDateBetween(customerLine.getPhoneNumber(), startOfMonth, endOfMonth);

            // Calcula el total de segundos y megabytes
            long totalSeconds = callRecords.stream().mapToLong(CallRecord::getDuration).sum();
            long totalMB = dataRecords.stream().mapToLong(DataRecord::getMegabytes).sum();

            // Calcula el total en horas, minutos y segundos
            long hours = TimeUnit.SECONDS.toHours(totalSeconds);
            long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60;
            long seconds = totalSeconds % 60;

            // Calcula el subtotal de la línea de cliente
            BigDecimal subtotal = BigDecimal.valueOf(customerLine.getPricePerMinute() * totalSeconds / 60)
                    .add(BigDecimal.valueOf(customerLine.getPricePerMegabyte() * totalMB));

            // Agrega los detalles de la factura al StringBuilder
            billText.append(String.format("Duración total de llamadas: %02d:%02d:%02d\n", hours, minutes, seconds));
            billText.append("Megabytes consumidos: ").append(totalMB).append("\n");
            billText.append("Precio por minuto: ").append(customerLine.getPricePerMinute()).append("\n");
            billText.append("Precio por megabyte: ").append(customerLine.getPricePerMegabyte()).append("\n");
            billText.append("Subtotal: ").append(subtotal).append("\n");

            // Actualiza el total general
            totalGeneral = totalGeneral.add(subtotal);
        }

        // Agrega el total general al StringBuilder
        billText.append("Total: ").append(totalGeneral).append("\n");

        return billText.toString();
    }

    @Transactional
    private File generatePdfFromBill(Bill bill) throws DocumentException, IOException {
        // Obtén todas las líneas de cliente para el contrato de la factura
        List<CustomerLine> customerLines = customerLineRepository.findByContractId(bill.getContractId());

        Document document = new Document();
        String outputDirectory = "generatedBills";
        File directory = new File(outputDirectory);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        String filePath = outputDirectory + "/Factura-" + bill.getContractId() + ".pdf";
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        // Agrega los detalles del usuario al PDF
        Paragraph userDetails = new Paragraph("Detalles del usuario...");
        document.add(userDetails);

        // Crea una tabla para los detalles de la factura
        PdfPTable table = new PdfPTable(new float[] { 1, 1, 1, 1, 1, 1 });
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);

        // Agrega los títulos de la factura a la tabla
        table.addCell("Duración total de llamadas");
        table.addCell("Megabytes consumidos");
        table.addCell("Precio por minuto");
        table.addCell("Precio por megabyte");
        table.addCell("Subtotal");

        // Cálculo del total general
        BigDecimal totalGeneral = BigDecimal.ZERO;

        for (CustomerLine customerLine : customerLines) {
            // Calcula el primer y último día del mes de la factura
            LocalDate startOfMonth = YearMonth.of(bill.getYear(), bill.getMonth()).atDay(1);
            LocalDate endOfMonth = YearMonth.of(bill.getYear(), bill.getMonth()).atEndOfMonth();

            // Obtén todos los registros de llamadas para el número de teléfono de la línea
            // de cliente durante el mes de la factura
            List<CallRecord> callRecords = callRecordRepository
                    .findBySenderAndDateBetween(customerLine.getPhoneNumber(), startOfMonth, endOfMonth);
            callRecords.addAll(callRecordRepository.findByReceiverAndDateBetween(customerLine.getPhoneNumber(),
                    startOfMonth, endOfMonth));

            // Obtiene todos los registros de datos para el número de teléfono de la línea
            // de cliente durante el mes de la factura
            List<DataRecord> dataRecords = dataRecordRepository
                    .findByPhoneNumberAndDateBetween(customerLine.getPhoneNumber(), startOfMonth, endOfMonth);

            // Calcula el total de segundos y megabytes
            long totalSeconds = callRecords.stream().mapToLong(CallRecord::getDuration).sum();
            long totalMB = dataRecords.stream().mapToLong(DataRecord::getMegabytes).sum();

            // Calcula el total en horas, minutos y segundos
            long hours = TimeUnit.SECONDS.toHours(totalSeconds);
            long minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60;
            long seconds = totalSeconds % 60;

            // Calcula el subtotal de la línea de cliente
            BigDecimal subtotal = BigDecimal.valueOf(customerLine.getPricePerMinute() * totalSeconds / 60)
                    .add(BigDecimal.valueOf(customerLine.getPricePerMegabyte() * totalMB));

            // Agrega los detalles de la factura a la tabla
            table.addCell(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            table.addCell(String.valueOf(totalMB));
            table.addCell(String.valueOf(customerLine.getPricePerMinute()));
            table.addCell(String.valueOf(customerLine.getPricePerMegabyte()));
            table.addCell(subtotal.toString());

            // Actualiza el total general
            totalGeneral = totalGeneral.add(subtotal);
        }

        // Agrega el total general a la tabla
        table.addCell("Total");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell("");
        table.addCell(totalGeneral.toString());

        // Agrega la tabla al PDF
        document.add(table);

        document.close();
        return new File(filePath);
    }

    @Transactional
    public void generateAndSendPdfToEmailFromBill(Bill bill, String email) throws DocumentException, IOException {
        File pdfFile = generatePdfFromBill(bill);
        try {
            if (pdfFile != null && pdfFile.exists()) {
                emailService.sendEmailWithBill(userManagementService.loadUserByEmail(email), pdfFile);
            }
        } finally {
            if (pdfFile != null && pdfFile.exists()) {
                pdfFile.delete(); // Elimina el archivo después de enviar el correo
            }
        }
    }
}
