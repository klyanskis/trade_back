package com.trade_accounting.utils;

import com.trade_accounting.models.BankAccount;
import com.trade_accounting.models.Company;
import com.trade_accounting.models.Contractor;
import com.trade_accounting.models.ContractorGroup;
import com.trade_accounting.models.Employee;
import com.trade_accounting.models.Invoice;
import com.trade_accounting.models.LegalDetail;
import com.trade_accounting.models.TypeOfContractor;
import com.trade_accounting.models.TypeOfInvoice;
import com.trade_accounting.models.TypeOfPrice;
import com.trade_accounting.models.Warehouse;
import com.trade_accounting.models.dto.BankAccountDto;
import com.trade_accounting.models.dto.CompanyDto;
import com.trade_accounting.models.dto.ContractorDto;
import com.trade_accounting.models.dto.ContractorGroupDto;
import com.trade_accounting.models.dto.DepartmentDto;
import com.trade_accounting.models.dto.EmployeeDto;
import com.trade_accounting.models.dto.ImageDto;
import com.trade_accounting.models.dto.InvoiceDto;
import com.trade_accounting.models.dto.LegalDetailDto;
import com.trade_accounting.models.dto.PositionDto;
import com.trade_accounting.models.dto.RoleDto;
import com.trade_accounting.models.dto.TypeOfContractorDto;
import com.trade_accounting.models.dto.TypeOfPriceDto;
import com.trade_accounting.models.dto.WarehouseDto;
import org.modelmapper.ModelMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelDtoConverter {

    private static final ModelMapper modelMapper = new ModelMapper();

    private ModelDtoConverter() {
    }

    public static CompanyDto convertToCompanyDto(Company company) {
        CompanyDto companyDto = modelMapper.map(company, CompanyDto.class);
        companyDto.setLegalDetailDto(modelMapper.map(company.getLegalDetail(), LegalDetailDto.class));

        companyDto.getLegalDetailDto().setTypeOfContractorDto(
                modelMapper.map(company.getLegalDetail().getTypeOfContractor(), TypeOfContractorDto.class));

        return companyDto;
    }

    public static Company convertToCompany(CompanyDto dto, LegalDetail legalDetail, List<BankAccount> bankAccounts) {
        return new Company(
                dto.getId(),
                dto.getName(),
                dto.getInn(),
                dto.getSortNumber(),
                dto.getPhone(),
                dto.getFax(),
                dto.getEmail(),
                dto.getPayerVat(),
                dto.getAddress(),
                dto.getCommentToAddress(),
                dto.getLeader(),
                dto.getLeaderManagerPosition(),
                dto.getLeaderSignature(),
                dto.getChiefAccountant(),
                dto.getChiefAccountantSignature(),
                dto.getStamp(),
                legalDetail, bankAccounts);
    }

    public static EmployeeDto convertToEmployeeDto(Employee employee) {
        EmployeeDto employeeDto = modelMapper.map(employee, EmployeeDto.class);
        if (employee.getDepartment() != null) {
            employeeDto.setDepartmentDto(modelMapper.map(employee.getDepartment(), DepartmentDto.class));
        }
        if (employee.getPosition() != null) {
            employeeDto.setPositionDto((modelMapper.map(employee.getPosition(), PositionDto.class)));
        }
        if (employee.getRoles() != null) {
            employeeDto.setRoleDto(employee.getRoles().stream()
                    .map(role -> modelMapper.map(role, RoleDto.class)).collect(Collectors.toSet()));
        }
        if (employee.getImage() != null) {
            employeeDto.setImageDto(modelMapper.map(employee.getImage(), ImageDto.class));
        }
        return employeeDto;
    }

    public static InvoiceDto convertToInvoiceDto(Invoice invoice) {

        InvoiceDto invoiceDto = modelMapper.map(invoice, InvoiceDto.class);

        if (invoice.getCompany() != null) {
            invoiceDto.setCompanyDto(modelMapper.map(invoice.getCompany(), CompanyDto.class));
        }
        if (invoice.getContractor() != null) {
            invoiceDto.setContractorDto((modelMapper.map(invoice.getContractor(), ContractorDto.class)));
        }

        if (invoice.getWarehouse() != null) {
            invoiceDto.setWarehouseDto(modelMapper.map(invoice.getWarehouse(), WarehouseDto.class));
        }
        return invoiceDto;
    }

    public static LegalDetail convertToLegalDetail(LegalDetailDto dto, TypeOfContractor typeOfContractor) {
        return new LegalDetail(
                dto.getId(),
                dto.getLastName(),
                dto.getFirstName(),
                dto.getMiddleName(),
                dto.getAddress(),
                dto.getCommentToAddress(),
                dto.getInn(),
                dto.getOkpo(),
                dto.getOgrnip(),
                dto.getNumberOfTheCertificate(),
                LocalDate.parse(dto.getDateOfTheCertificate()),
                typeOfContractor);
    }

    public static TypeOfContractor convertToTypeOfContractor(TypeOfContractorDto dto) {
        return new TypeOfContractor(
                dto.getId(),
                dto.getName(),
                dto.getSortNumber());
    }

    public static Invoice convertToInvoice(InvoiceDto dto, TypeOfInvoice typeOfInvoice, Company company,
                                           Contractor contractor, Warehouse warehouse) {
        return new Invoice(
                dto.getId(),
                LocalDateTime.parse(dto.getDate()),
                typeOfInvoice,
                company,
                contractor,
                warehouse,
                dto.isSpend()
        );
    }

    public static TypeOfInvoice convertToTypeOfInvoice(TypeOfInvoice typeOfInvoice) {
        return TypeOfInvoice.valueOf(typeOfInvoice.toString());
    }

    public static Warehouse convertToWarehouse(WarehouseDto dto) {
        return new Warehouse(
                dto.getId(),
                dto.getName(),
                dto.getSortNumber(),
                dto.getAddress(),
                dto.getCommentToAddress(),
                dto.getComment()
        );
    }

    public static Contractor convertToContractor(ContractorDto dto, ContractorGroup contractorGroup,
                                                 TypeOfContractor typeOfContractor, TypeOfPrice typeOfPrice,
                                                 List<BankAccount> bankAccount, LegalDetail legalDetail) {
        return new Contractor(
                dto.getId(),
                dto.getName(),
                dto.getInn(),
                dto.getSortNumber(),
                dto.getPhone(),
                dto.getFax(),
                dto.getEmail(),
                dto.getAddress(),
                dto.getCommentToAddress(),
                dto.getComment(),
                contractorGroup,
                typeOfContractor,
                typeOfPrice,
                bankAccount,
                legalDetail
        );
    }

    public static ContractorGroup convertToContractorGroup(ContractorGroupDto dto) {
        return new ContractorGroup(
                dto.getId(),
                dto.getName(),
                dto.getSortNumber()
        );
    }

    public static TypeOfPrice convertToTypeOfPrice(TypeOfPriceDto dto) {
        return new TypeOfPrice(
                dto.getId(),
                dto.getName(),
                dto.getSortNumber()
        );
    }

    public static BankAccount convertToBankAccount(BankAccountDto dto) {
        return new BankAccount(
                dto.getId(),
                dto.getRcbic(),
                dto.getBank(),
                dto.getAddress(),
                dto.getCorrespondentAccount(),
                dto.getAccount(),
                dto.getMainAccount(),
                dto.getSortNumber()
        );
    }

    public static List<BankAccount> convertToListOfBankAccount(List<BankAccountDto> list) {
        List<BankAccount> bankAccountList = new ArrayList<>();
        for (BankAccountDto bankAccountDto : list) {
            bankAccountList.add(new BankAccount(
                    bankAccountDto.getId(),
                    bankAccountDto.getRcbic(),
                    bankAccountDto.getBank(),
                    bankAccountDto.getAddress(),
                    bankAccountDto.getCorrespondentAccount(),
                    bankAccountDto.getAccount(),
                    bankAccountDto.getMainAccount(),
                    bankAccountDto.getSortNumber()
            ));
        }
        return bankAccountList;
    }

    public static List<BankAccountDto> convertToListBankAccountDto(List<BankAccount> bankAccounts){
        List<BankAccountDto> dtos = new ArrayList<>();
        for (BankAccount bankAccount: bankAccounts){
            dtos.add(new BankAccountDto(
                    bankAccount.getId(),
                    bankAccount.getRcbic(),
                    bankAccount.getBank(),
                    bankAccount.getAddress(),
                    bankAccount.getCorrespondentAccount(),
                    bankAccount.getAccount(),
                    bankAccount.getMainAccount(),
                    bankAccount.getSortNumber()));
        }
        return dtos;
    }
}
