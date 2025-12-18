package com.example.InvestmentBankingDealPipelineManagementPortal;

import com.example.InvestmentBankingDealPipelineManagementPortal.user.Role;
import com.example.InvestmentBankingDealPipelineManagementPortal.user.User;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.Optional;

@EnableMongoRepositories
@SpringBootApplication
@EnableMongoAuditing(auditorAwareRef = "auditorAware",
        dateTimeProviderRef = "dateTimeProvider")
public class InvestmentBankingDealPipelineManagementPortalApplication {

    public static void main(String[] args) {
        SpringApplication.run(
                InvestmentBankingDealPipelineManagementPortalApplication.class, args);
    }

    @Bean
    public AuditorAware<String> auditorAware() {
        return () -> {
            var auth = org.springframework.security.core.context.SecurityContextHolder
                    .getContext()
                    .getAuthentication();

            if (auth == null || !auth.isAuthenticated()) {
                return Optional.of("SYSTEM");
            }

            return Optional.of(auth.getName());
        };
    }

    /**
     * Provides current timestamp for @CreatedDate / @LastModifiedDate
     */
    @Bean
    public DateTimeProvider dateTimeProvider() {
        return () -> Optional.of(Instant.now());
    }

    @Bean
    CommandLineRunner adminBootstrap(
            MongoTemplate mongoTemplate,
            PasswordEncoder passwordEncoder
    ) {
        return args -> {

            System.out.println("MongoDB database: " +
                    mongoTemplate.getDb().getName());

            Query query = new Query(
                    Criteria.where("username").is("admin")
            );

            boolean adminExists =
                    mongoTemplate.exists(query, User.class);

            if (!adminExists) {
                User admin = User.builder()
                        .username("admin")
                        .email("admin@bank.com")
                        .password(passwordEncoder.encode("Admin@123"))
                        .role(Role.ADMIN)
                        .active(true)
                        .createdAt(Instant.now())
                        .build();

                mongoTemplate.save(admin);
                System.out.println("✅ Default ADMIN user created");
            } else {
                System.out.println("✅ Admin already exists");
            }
        };
    }
}

