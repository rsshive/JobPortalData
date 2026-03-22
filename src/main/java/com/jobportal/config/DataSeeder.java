package com.jobportal.config;

import com.jobportal.model.*;
import com.jobportal.model.enums.AccountStatus;
import com.jobportal.model.enums.Role;
import com.jobportal.model.enums.VacancyStatus;
import com.jobportal.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final ApplicantRepository applicantRepository;
    private final RecruiterRepository recruiterRepository;
    private final VacancyRepository vacancyRepository;
    private final CompanyRepository companyRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        System.out.println("Seeding mock data...");

        // 1. Create Admin
        if (!userRepository.existsByEmail("admin@jobportal.com")) {
            Admin admin = Admin.builder()
                    .email("admin@jobportal.com")
                    .passwordHash(passwordEncoder.encode("Admin@123"))
                    .role(Role.ADMIN)
                    .status(AccountStatus.ACTIVE)
                    .build();
            adminRepository.save(admin);
        }

        // 2. Create Applicant
        if (!userRepository.existsByEmail("applicant@example.com")) {
            Applicant applicant = Applicant.builder()
                    .email("applicant@example.com")
                    .passwordHash(passwordEncoder.encode("Applicant@123"))
                    .role(Role.APPLICANT)
                    .status(AccountStatus.ACTIVE)
                    .fullName("Nguyen Van A")
                    .phone("0123456789")
                    .skills("Java, Spring Boot, React")
                    .education("FPT University - Software Engineering")
                    .workExperience("1 year intern at FPT Software")
                    .jobPreferences("Backend Developer, HCMC")
                    .build();
            applicantRepository.save(applicant);
        }

        // 3. Create Company (only if not existing)
        Company company = companyRepository.findAll().stream()
                .filter(c -> "Tech Solutions JSC".equals(c.getName()))
                .findFirst()
                .orElseGet(() -> {
                    Company newCompany = Company.builder()
                            .name("Tech Solutions JSC")
                            .description("Leading software outsourcing company in Vietnam")
                            .location("District 1, Ho Chi Minh City")
                            .website("https://techsolutions.vn")
                            .industry("Information Technology")
                            .size("500+")
                            .build();
                    return companyRepository.save(newCompany);
                });

        // 4. Create Recruiter
        Recruiter recruiter;
        if (!userRepository.existsByEmail("recruiter@techsolutions.vn")) {
            recruiter = Recruiter.builder()
                    .email("recruiter@techsolutions.vn")
                    .passwordHash(passwordEncoder.encode("Recruiter@123"))
                    .role(Role.RECRUITER)
                    .status(AccountStatus.ACTIVE)
                    .company(company)
                    .build();
            recruiter = recruiterRepository.save(recruiter);
        } else {
            recruiter = (Recruiter) userRepository.findByEmail("recruiter@techsolutions.vn").get();
        }

        // 5. Create Vacancies only if this recruiter has no vacancies
        if (vacancyRepository.findByRecruiter(recruiter).isEmpty()) {
        Vacancy vacancy1 = Vacancy.builder()
                .recruiter(recruiter)
                .company(company)
                .title("Senior Java Developer")
                .description("Looking for an experienced Java Backend Developer with Spring Boot knowledge.")
                .requiredSkills("Java, Spring Boot, PostgreSQL, Microservices")
                .experienceLevel("Senior")
                .salaryMin(1500)
                .salaryMax(2500)
                .location("Ho Chi Minh City")
                .employmentType("Full-time")
                .applicationDeadline(Date.from(Instant.now().plus(30, ChronoUnit.DAYS)))
                .status(VacancyStatus.OPEN)
                .build();

        Vacancy vacancy2 = Vacancy.builder()
                .recruiter(recruiter)
                .company(company)
                .title("ReactJS Frontend Engineer")
                .description("Develop responsive web applications using ReactJS and Tailwind CSS.")
                .requiredSkills("ReactJS, TypeScript, Tailwind")
                .experienceLevel("Mid-level")
                .salaryMin(1000)
                .salaryMax(1800)
                .location("Remote")
                .employmentType("Full-time")
                .applicationDeadline(Date.from(Instant.now().plus(15, ChronoUnit.DAYS)))
                .status(VacancyStatus.OPEN)
                .build();
                
        Vacancy vacancy3 = Vacancy.builder()
                .recruiter(recruiter)
                .company(company)
                .title("Data Analyst")
                .description("Analyze business data to find actionable insights. SQL and Python required.")
                .requiredSkills("SQL, Python, Excel, PowerBI")
                .experienceLevel("Junior")
                .salaryMin(600)
                .salaryMax(1200)
                .location("Hanoi")
                .employmentType("Full-time")
                .applicationDeadline(Date.from(Instant.now().plus(45, ChronoUnit.DAYS)))
                .status(VacancyStatus.OPEN)
                .build();

        Vacancy vacancy4 = Vacancy.builder()
                .recruiter(recruiter)
                .company(company)
                .title("UX/UI Designer Intern")
                .description("Join our creative team to design modern interfaces for Web and Mobile platforms.")
                .requiredSkills("Figma, Adobe XD, Prototyping")
                .experienceLevel("Intern")
                .salaryMin(200)
                .salaryMax(400)
                .location("Da Nang")
                .employmentType("Internship")
                .applicationDeadline(Date.from(Instant.now().plus(20, ChronoUnit.DAYS)))
                .status(VacancyStatus.PENDING)
                .build();
                
        Vacancy vacancy5 = Vacancy.builder()
                .recruiter(recruiter)
                .company(company)
                .title("DevOps Engineer")
                .description("Maintain and automate CI/CD pipelines, manage Docker containers and Kubernetes clusters.")
                .requiredSkills("Docker, Kubernetes, Jenkins, AWS")
                .experienceLevel("Mid-level")
                .salaryMin(1800)
                .salaryMax(3000)
                .location("Remote")
                .employmentType("Contract")
                .applicationDeadline(Date.from(Instant.now().plus(10, ChronoUnit.DAYS)))
                .status(VacancyStatus.CLOSED)
                .build();

        vacancyRepository.save(vacancy1);
        vacancyRepository.save(vacancy2);
        vacancyRepository.save(vacancy3);
        vacancyRepository.save(vacancy4);
        vacancyRepository.save(vacancy5);

        System.out.println("Mock data seeding completed successfully!");
        }
    }
}
