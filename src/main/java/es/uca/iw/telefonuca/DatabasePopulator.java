package es.uca.iw.telefonuca;

import es.uca.iw.telefonuca.bill.domain.Bill;
import es.uca.iw.telefonuca.bill.services.BillManagementService;
import es.uca.iw.telefonuca.contract.domain.Contract;
import es.uca.iw.telefonuca.contract.services.ContractManagementService;
import es.uca.iw.telefonuca.line.domain.BlockedNumber;
import es.uca.iw.telefonuca.line.domain.CallRecord;
import es.uca.iw.telefonuca.line.domain.CustomerLine;
import es.uca.iw.telefonuca.line.domain.DataRecord;
import es.uca.iw.telefonuca.line.domain.Line;
import es.uca.iw.telefonuca.line.services.BlockedNumberManagementService;
import es.uca.iw.telefonuca.line.services.CallRecordManagementService;
import es.uca.iw.telefonuca.line.services.CustomerLineManagementService;
import es.uca.iw.telefonuca.line.services.DataRecordManagementService;
import es.uca.iw.telefonuca.line.services.LineManagementService;
import es.uca.iw.telefonuca.ticket.domain.Ticket;
import es.uca.iw.telefonuca.ticket.domain.TicketMessage;
import es.uca.iw.telefonuca.ticket.domain.TicketStatus;
import es.uca.iw.telefonuca.ticket.services.TicketManagementService;
import es.uca.iw.telefonuca.user.domain.Role;
import es.uca.iw.telefonuca.user.domain.User;
import es.uca.iw.telefonuca.user.services.UserManagementService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("!test")
public class DatabasePopulator implements CommandLineRunner {

    UserManagementService userService;
    TicketManagementService ticketService;
    LineManagementService lineManagementService;
    CustomerLineManagementService customerLineService;
    DataRecordManagementService dataRecordManagementService;
    CallRecordManagementService callRecordManagementService;
    BillManagementService billManagementService;
    ContractManagementService contractManagementService;
    BlockedNumberManagementService blockedNumberManagementService;

    public DatabasePopulator(UserManagementService userService, TicketManagementService ticketService, LineManagementService lineManagementService,
                            CustomerLineManagementService customerLineService, DataRecordManagementService dataRecordManagementService, CallRecordManagementService callRecordManagementService,
                            BillManagementService billManagementService, ContractManagementService contractManagementService, BlockedNumberManagementService blockedNumberManagementService) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.lineManagementService = lineManagementService;
        this.customerLineService = customerLineService;
        this.dataRecordManagementService = dataRecordManagementService;
        this.callRecordManagementService = callRecordManagementService;
        this.billManagementService = billManagementService;
        this.contractManagementService = contractManagementService;
        this.blockedNumberManagementService = blockedNumberManagementService;
    }

    @Override
    public void run(String... args) throws Exception {

        if (userService.count() == 0) {
            int i = 1;
            /// Creamos usuarios para cada rol
            Role[] roles = Role.values();
            for (Role role : roles) {
                User user = new User();
                user.setUsername(role.name().toLowerCase());
                user.setPassword(role.name().toLowerCase());
                user.setEmail(role.name().toLowerCase() + "@uca.es");
                user.setName(role.name().toLowerCase());
                user.setSurname(role.name().toLowerCase());
                user.addRole(role);
                userService.registerUser(user);
                userService.activateUser(user.getEmail(), user.getRegisterCode());
                System.out.println(role.name() + " created");

                // Crear un contrato para el usuario
                Contract contract = new Contract();
                contract.setOwnerId(user.getId()); // El ID del usuario es el propietario del contrato
                contract.setMonthsAgreed(12); // Por ejemplo, el contrato dura 12 meses
                contract.setCarrier("Operador"); // Por ejemplo, el operador es "Operador"
                contract.setStartAt(LocalDate.now()); // La fecha de inicio es hoy
                contract.setFinishAt(LocalDate.now().plusMonths(contract.getMonthsAgreed())); // La fecha de finalización es hoy más los meses acordados
                contract.setSharedData(false); // Por ejemplo, los datos no son compartidos
                // Guardar el contrato
                contractManagementService.saveContract(contract);
                System.out.println("Contract created");

                Bill bill = new Bill();
                bill.setContractId(contract.getId()); // Suponiendo que el ID del usuario se puede usar como contratoID
                bill.setYear(Calendar.getInstance().get(Calendar.YEAR)); // Año actual
                bill.setMonth(Calendar.getInstance().get(Calendar.MONTH)); // Mes actual
                bill.setTotal(1000);
                bill.setTotalMegabytes(4000);
                bill.setTotalMinutes(100000);
                billManagementService.saveBill(bill);
                System.out.println("Bill created");

                Line line = new Line();
                line.setName("New Customer Line " + i);
                line.setDescription("This is a new customer line " + i);
                line.setPricePerMinute(100); // Céntimos
                line.setPricePerMegabyte(50); // Céntimos
                line.setMinimumMonths(1);
                line.setFreeMinutes(10);
                line.setFreeMegabytes(10);
                lineManagementService.saveLine(line); // Save the line
                System.out.println("Line created");

                // Crear una línea de cliente para el usuario
                CustomerLine customerLine = new CustomerLine();
                customerLine.setPhoneNumber(123456781+i); // Por ejemplo, el número de teléfono es 123456789
                customerLine.setContractId(contract.getId()); // El ID del usuario es el contrato de la línea de cliente
                customerLine.setLineId(line.getId()); // Generar un nuevo ID para la línea de cliente
                customerLine.setRoaming(false); // Por ejemplo, la línea de cliente no tiene roaming
                customerLine.setPricePerMinute(1); // Por ejemplo, el precio por minuto es 1
                customerLine.setPricePerMegabyte(1); // Por ejemplo, el precio por megabyte es 1
                customerLine.setFreeMinutes(10); // Por ejemplo, hay 10 minutos gratuitos
                customerLine.setFreeMegabytes(10); // Por ejemplo, hay 10 megabytes gratuitos
                // Guardar la línea de cliente
                customerLineService.saveCustomerLine(customerLine);
                System.out.println("Customer Line created");

                Ticket ticket = new Ticket();
                ticket.setCustomerLineId(customerLine.getId()); // El ID del usuario es la línea del cliente del ticket
                ticket.setSubject("Asunto del ticket"); // Por ejemplo, el asunto del ticket es "Asunto del ticket"
                ticket.setStatus(TicketStatus.PENDING_ANSWER_BY_CUSTOMER); // Por ejemplo, el estado del ticket es OPEN
                ticket.setDate(LocalDate.now()); // La fecha del ticket es hoy
                // Guardar el ticket
                ticketService.saveTicket(ticket);
                System.out.println("Ticket created");

                // Crear un mensaje de ticket para el usuario
                TicketMessage ticketMessage = new TicketMessage();
                ticketMessage.setTicketId(ticket.getId()); // El ID del usuario es el ID del ticket del mensaje de ticket
                ticketMessage.setContent("Contenido del mensaje de ticket"); // Por ejemplo, el contenido del mensaje de ticket es "Contenido del mensaje de ticket"
                ticketMessage.setDate(LocalDateTime.now()); // La fecha del mensaje de ticket es ahora
                // Guardar el mensaje de ticket
                ticketService.saveTicketMessage(ticketMessage);
                System.out.println("Ticket Message created");

                DataRecord record = new DataRecord();
                record.setPhoneNumber(customerLine.getPhoneNumber());
                record.setMegabytes(1000 + i);
                record.setDate(LocalDate.now());            
                // Aquí necesitas llamar a algún método de tu servicio para guardar el registro.
                dataRecordManagementService.saveDataRecord(record);
                System.out.println("Data record created");

                CallRecord callRecord = new CallRecord();
                callRecord.setSender(customerLine.getPhoneNumber());
                callRecord.setReceiver(100000000 + i + 1);
                callRecord.setDuration(120 + i); // duración en segundos
                callRecord.setDate(LocalDate.now());               
                // Aquí necesitas llamar a algún método de tu servicio para guardar el registro.
                callRecordManagementService.saveCallRecord(callRecord);
                System.out.println("Call record created");

                // Crear un número bloqueado para el usuario
                BlockedNumber blockedNumber = new BlockedNumber();
                blockedNumber.setBlocker(customerLine.getPhoneNumber());
                blockedNumber.setBlocked(987654321 + i);
                // Guardar el número bloqueado
                blockedNumberManagementService.saveBlockedNumber(blockedNumber);
                System.out.println("Blocked number created");

                i++;
            }
        }    
    }
}