package mx.unam.fca.demo.config;

import mx.unam.fca.demo.models.AppUser;
import mx.unam.fca.demo.repositories.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(AdminSeeder.class);

    private final AppUserRepository users;
    private final PasswordEncoder passwordEncoder;
    private final String adminPassword;

    public AdminSeeder(AppUserRepository users, PasswordEncoder passwordEncoder,
            @Value("${demo.security.admin-password:}") String adminPassword) {
        this.users = users;
        this.passwordEncoder = passwordEncoder;
        this.adminPassword = adminPassword;
    }

    @Override
    public void run(String... args) {
        if (adminPassword.isBlank()) {
            logger.warn("DEMO_ADMIN_PASSWORD no esta definida: no se creo el usuario admin.");
            return;
        }
        if (users.findByUsername("admin").isEmpty()) {
            users.save(new AppUser("admin", passwordEncoder.encode(adminPassword), "ADMIN"));
        }
    }
}