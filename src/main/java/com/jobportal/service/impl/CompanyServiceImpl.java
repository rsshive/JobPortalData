package com.jobportal.service.impl;

import com.jobportal.model.Company;
import com.jobportal.repository.CompanyRepository;
import com.jobportal.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    public Optional<Company> findById(Integer id) {
        return companyRepository.findById(id);
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    @Transactional
    public Company updateCompany(Company company) {
        if (!companyRepository.existsById(company.getCompanyId())) {
            throw new RuntimeException("Company not found");
        }
        return companyRepository.save(company);
    }

    @Override
    @Transactional
    public void deleteCompany(Integer id) {
        companyRepository.deleteById(id);
    }
}
