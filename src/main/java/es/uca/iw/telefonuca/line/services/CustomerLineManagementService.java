package es.uca.iw.telefonuca.line.services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import es.uca.iw.telefonuca.line.repositories.CustomerLineRepository;
import jakarta.transaction.Transactional;

import es.uca.iw.telefonuca.line.domain.CustomerLine;

@Service
public class CustomerLineManagementService {

    private final CustomerLineRepository customerLineRepository;

    public CustomerLineManagementService(CustomerLineRepository customerLineRepository) {
        this.customerLineRepository = customerLineRepository;
    }

    @Transactional
    public List<CustomerLine> loadAll() {
        return customerLineRepository.findAll();
    }

    @Transactional
    public CustomerLine loadCustomerLineById(UUID id) {
        Optional<CustomerLine> customerLine = customerLineRepository.findById(id);
        if (customerLine.isEmpty()) {
            throw new RuntimeException("No customerLine present with id: " + id);
        } else {
            return customerLine.get();
        }
    }

    @Transactional
    public List<CustomerLine> loadCustomerLineByPhoneNumber(int phoneNumber) {
        List<CustomerLine> customerLine = customerLineRepository.findByPhoneNumber(phoneNumber);
        if (customerLine.isEmpty()) {
            throw new RuntimeException("No customerLine present with phoneNumber: " + phoneNumber);
        } else {
            return customerLine;
        }
    }

    @Transactional
    public List<CustomerLine> loadCustomerLineByContractId(UUID customerId) {
        List<CustomerLine> customerLine = customerLineRepository.findByContractId(customerId);
        if (customerLine.isEmpty()) {
            throw new RuntimeException("No customerLine present with contractId: " + customerId);
        } else {
            return customerLine;
        }
    }

    @Transactional
    public List<CustomerLine> loadCustomerLineByPricePerMinute(float pricePerMinute) {
        List<CustomerLine> customerLine = customerLineRepository.findByPricePerMinute(pricePerMinute);
        if (customerLine.isEmpty()) {
            throw new RuntimeException("No customerLine present with pricePerMinute: " + pricePerMinute);
        } else {
            return customerLine;
        }
    }

    @Transactional
    public List<CustomerLine> loadCustomerLineByPricePerMegabyte(float pricePerMegabyte) {
        List<CustomerLine> customerLine = customerLineRepository.findByPricePerMegabyte(pricePerMegabyte);
        if (customerLine.isEmpty()) {
            throw new RuntimeException("No customerLine present with pricePerMegabyte: " + pricePerMegabyte);
        } else {
            return customerLine;
        }
    }

    @Transactional
    public List<CustomerLine> loadCustomerLineByFreeMinutes(int freeMinutes) {
        List<CustomerLine> customerLine = customerLineRepository.findByFreeMinutes(freeMinutes);
        if (customerLine.isEmpty()) {
            throw new RuntimeException("No customerLine present with freeMinutes: " + freeMinutes);
        } else {
            return customerLine;
        }
    }

    @Transactional
    public List<CustomerLine> loadCustomerLineByFreeMegabytes(int freeMegabytes) {
        List<CustomerLine> customerLine = customerLineRepository.findByFreeMegabytes(freeMegabytes);
        if (customerLine.isEmpty()) {
            throw new RuntimeException("No customerLine present with freeMegabytes: " + freeMegabytes);
        } else {
            return customerLine;
        }
    }

    @Transactional
    public void saveCustomerLine(CustomerLine customerLine) {
        customerLineRepository.save(customerLine);
    }

    @Transactional
    public void deleteCustomerLine(CustomerLine customerLine) {
        customerLineRepository.delete(customerLine);
    }

}
