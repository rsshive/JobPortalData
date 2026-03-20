package com.jobportal.service;

import com.jobportal.model.Company;
import java.util.List;
import java.util.Optional;

public interface CompanyService {
    Company createCompany(Company company);
    Optional<Company> findById(Integer id);
    List<Company> findAll();
    Company updateCompany(Company company);
    void deleteCompany(Integer id);
}
