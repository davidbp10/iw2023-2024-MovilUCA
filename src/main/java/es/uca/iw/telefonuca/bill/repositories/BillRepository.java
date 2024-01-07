package es.uca.iw.telefonuca.bill.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import es.uca.iw.telefonuca.bill.domain.Bill;

public interface BillRepository extends JpaRepository<Bill, UUID> {

    Optional<Bill> findById(UUID id);

    Optional<Bill> findByContractIdAndYearAndMonth(UUID contractId, int year, int month);

    List<Bill> findByContractId(UUID contractId);

    List<Bill> findByContractIdAndYear(UUID contractId, int year);

    List<Bill> findByContractIdAndYearAndMonthGreaterThanEqual(UUID contractId, int year, int month);

    List<Bill> findByContractIdAndYearGreaterThanEqual(UUID contractId, int year);

    List<Bill> findByContractIdAndYearLessThanEqual(UUID contractId, int year);

    List<Bill> findByContractIdAndYearAndMonthLessThanEqual(UUID contractId, int year, int month);

    List<Bill> findByContractIdAndYearAndMonthLessThanEqualAndYearAndMonthGreaterThanEqual(UUID contractId, int year,
            int month, int year2, int month2);

    List<Bill> findByContractIdAndYearAndMonthGreaterThanEqualAndYearAndMonthLessThanEqual(UUID contractId, int year,

            int month, int year2, int month2);

    List<Bill> findByTotal(double total);

    List<Bill> findByTotalGreaterThanEqual(double total);

    List<Bill> findByTotalLessThanEqual(double total);

    List<Bill> findByTotalGreaterThanEqualAndTotalLessThanEqual(double total, double total2);

}
