package com.trade_accounting.repositories;

import com.trade_accounting.models.Employee;
import com.trade_accounting.models.Role;
import com.trade_accounting.models.dto.DepartmentDto;
import com.trade_accounting.models.dto.EmployeeDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("select new com.trade_accounting.models.dto.EmployeeDto(" +
            "em.id, " +
            "em.lastName, " +
            "em.firstName, " +
            "em.middleName, " +
            "em.sortNumber, " +
            "em.phone, " +
            "em.inn, " +
            "em.description, " +
            "em.email," +
            "em.password) from Employee em")
    List<EmployeeDto> getAll();

    @Query("select new com.trade_accounting.models.dto.EmployeeDto(" +
            "em.id, " +
            "em.lastName, " +
            "em.firstName, " +
            "em.middleName, " +
            "em.sortNumber, " +
            "em.phone, " +
            "em.inn, " +
            "em.description, " +
            "em.email," +
            "em.password) from Employee em " +
            "where em.id = :id")
    EmployeeDto getById(@Param("id") Long id);


}
