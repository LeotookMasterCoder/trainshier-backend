package com.trainshier.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.trainshier.entity.User;
import com.trainshier.enums.UserRole;
import com.trainshier.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;

/**
 * Database initializer.
 *
 * Guarantees that essential system accounts always exist (admin + demo instructors).
 * Does NOT seed training/simulation data — all of that must come from real usage.
 * Also migrates any plain-text passwords to BCrypt.
 * Installs the cascading delete trigger for the English schema.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting database initialization...");

        // 1. Install cascading delete trigger for the English schema
        installCascadeTrigger();

        // 2. Guarantee essential accounts (admin + demo instructors + demo apprentice) always exist
        ensureUser("admin@trainshier.com",        "Administrador Sistema",  "Admin123*",      UserRole.ADMINISTRATOR, "0013410739");
        ensureUser("instructor@trainshier.com",   "Instructor Principal",   "Instructor123*", UserRole.INSTRUCTOR,    "0005908111");
        ensureUser("instructor2@trainshier.com",  "Laura Gómez",            "Instructor123*", UserRole.INSTRUCTOR,    "RFID-INSTRUCTOR-889");
        ensureUser("instructor3@trainshier.com",  "Andrés Molina",          "Instructor123*", UserRole.INSTRUCTOR,    "RFID-INSTRUCTOR-890");
        ensureUser("aprendiz@trainshier.com",    "Aprendiz Demo",          "Aprendiz123*",   UserRole.APPRENTICE,    "0002378679");

        // Force update the RFID UIDs of demo accounts to match requirements
        forceRfid("admin@trainshier.com", "0013410739");
        forceRfid("instructor@trainshier.com", "0005908111");
        forceRfid("aprendiz@trainshier.com", "0002378679");

        // 3. Migrate any plain-text passwords to BCrypt and ensure active=true
        List<User> users = userRepository.findAll();
        for (User user : users) {
            boolean changed = false;
            String pwd = user.getPassword();
            if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$") && !pwd.startsWith("$2y$")) {
                log.info("Migrating plain-text password for: {}", user.getEmail());
                user.setPassword(passwordEncoder.encode(pwd));
                changed = true;
            }
            if (user.getActive() == null) {
                user.setActive(true);
                changed = true;
            }
            if (changed) {
                userRepository.save(user);
            }
        }

        log.info("Database initialization completed.");
    }

    /** Ensure a user exists; if it doesn't, create it. Never overwrites existing data. */
    private void ensureUser(String email, String name, String password, UserRole role, String rfidUid) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isEmpty()) {
            log.info("Creating essential account: {}", email);
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user.setActive(true);
            user.setRfidUid(rfidUid);
            userRepository.save(user);
        } else {
            // Ensure active field is set for legacy rows
            User user = existing.get();
            boolean changed = false;
            if (user.getActive() == null) {
                user.setActive(true);
                changed = true;
            }
            if (user.getRfidUid() == null && rfidUid != null) {
                user.setRfidUid(rfidUid);
                changed = true;
            }
            if (changed) {
                userRepository.save(user);
            }
        }
    }

    private void forceRfid(String email, String rfidUid) {
        userRepository.findByEmail(email).ifPresent(user -> {
            user.setRfidUid(rfidUid);
            userRepository.save(user);
        });
    }

    private void installCascadeTrigger() {
        try {
            log.info("Installing BEFORE DELETE trigger on 'users' table...");
            jdbcTemplate.execute(
                "CREATE OR REPLACE FUNCTION trg_clean_user_data() " +
                "RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    DELETE FROM simulation_errors WHERE transaction_id IN (" +
                "        SELECT t.id FROM transactions t " +
                "        JOIN session_access a ON t.access_id = a.id " +
                "        WHERE a.apprentice_id = OLD.id); " +
                "    DELETE FROM transaction_details WHERE transaction_id IN (" +
                "        SELECT t.id FROM transactions t " +
                "        JOIN session_access a ON t.access_id = a.id " +
                "        WHERE a.apprentice_id = OLD.id); " +
                "    DELETE FROM transaction_payments WHERE transaction_id IN (" +
                "        SELECT t.id FROM transactions t " +
                "        JOIN session_access a ON t.access_id = a.id " +
                "        WHERE a.apprentice_id = OLD.id); " +
                "    DELETE FROM invoices WHERE transaction_id IN (" +
                "        SELECT t.id FROM transactions t " +
                "        JOIN session_access a ON t.access_id = a.id " +
                "        WHERE a.apprentice_id = OLD.id); " +
                "    DELETE FROM inventory_simulation WHERE access_id IN (" +
                "        SELECT id FROM session_access WHERE apprentice_id = OLD.id); " +
                "    DELETE FROM transactions WHERE access_id IN (" +
                "        SELECT id FROM session_access WHERE apprentice_id = OLD.id); " +
                "    DELETE FROM instructor_comments WHERE apprentice_id = OLD.id; " +
                "    DELETE FROM reports WHERE user_id = OLD.id; " +
                "    DELETE FROM rfid_requests WHERE user_id = OLD.id; " +
                "    DELETE FROM session_access WHERE apprentice_id = OLD.id; " +
                "    DELETE FROM simulation_errors WHERE transaction_id IN (" +
                "        SELECT t.id FROM transactions t " +
                "        JOIN session_access a ON t.access_id = a.id " +
                "        JOIN simulation_sessions s ON a.session_id = s.id " +
                "        WHERE s.instructor_id = OLD.id); " +
                "    DELETE FROM transaction_details WHERE transaction_id IN (" +
                "        SELECT t.id FROM transactions t " +
                "        JOIN session_access a ON t.access_id = a.id " +
                "        JOIN simulation_sessions s ON a.session_id = s.id " +
                "        WHERE s.instructor_id = OLD.id); " +
                "    DELETE FROM transaction_payments WHERE transaction_id IN (" +
                "        SELECT t.id FROM transactions t " +
                "        JOIN session_access a ON t.access_id = a.id " +
                "        JOIN simulation_sessions s ON a.session_id = s.id " +
                "        WHERE s.instructor_id = OLD.id); " +
                "    DELETE FROM invoices WHERE transaction_id IN (" +
                "        SELECT t.id FROM transactions t " +
                "        JOIN session_access a ON t.access_id = a.id " +
                "        JOIN simulation_sessions s ON a.session_id = s.id " +
                "        WHERE s.instructor_id = OLD.id); " +
                "    DELETE FROM inventory_simulation WHERE access_id IN (" +
                "        SELECT a.id FROM session_access a " +
                "        JOIN simulation_sessions s ON a.session_id = s.id " +
                "        WHERE s.instructor_id = OLD.id); " +
                "    DELETE FROM transactions WHERE access_id IN (" +
                "        SELECT a.id FROM session_access a " +
                "        JOIN simulation_sessions s ON a.session_id = s.id " +
                "        WHERE s.instructor_id = OLD.id); " +
                "    DELETE FROM session_access WHERE session_id IN (" +
                "        SELECT id FROM simulation_sessions WHERE instructor_id = OLD.id); " +
                "    DELETE FROM simulation_configuration WHERE session_id IN (" +
                "        SELECT id FROM simulation_sessions WHERE instructor_id = OLD.id); " +
                "    DELETE FROM instructor_comments WHERE instructor_id = OLD.id; " +
                "    DELETE FROM rfid_requests WHERE reviewed_by = OLD.id; " +
                "    DELETE FROM simulation_sessions WHERE instructor_id = OLD.id; " +
                "    RETURN OLD; " +
                "END; $$ LANGUAGE plpgsql;"
            );
            jdbcTemplate.execute(
                "DROP TRIGGER IF EXISTS trg_before_delete_users ON users"
            );
            jdbcTemplate.execute(
                "CREATE TRIGGER trg_before_delete_users " +
                "BEFORE DELETE ON users " +
                "FOR EACH ROW " +
                "EXECUTE FUNCTION trg_clean_user_data()"
            );
            log.info("Cascade delete trigger installed successfully.");
        } catch (Exception e) {
            log.warn("Could not install cascade trigger: {}", e.getMessage());
        }
    }
}
