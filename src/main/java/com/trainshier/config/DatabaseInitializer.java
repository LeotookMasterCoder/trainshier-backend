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

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("Starting database initialization and password migration check...");

        // 1. Ensure columns exist in the database schema dynamically
        try {
            log.info("Ensuring rfid_uid column exists in the database table 'usuarios'...");
            jdbcTemplate.execute("ALTER TABLE usuarios ADD COLUMN IF NOT EXISTS rfid_uid text;");
        } catch (Exception e) {
            log.error("Could not add rfid_uid column via ALTER TABLE: {}", e.getMessage());
        }

        // 1.5. Ensure cascading delete function and trigger exist
        try {
            log.info("Creating BEFORE DELETE trigger and helper function for table 'usuarios'...");
            jdbcTemplate.execute(
                "CREATE OR REPLACE FUNCTION trg_clean_user_data() " +
                "RETURNS TRIGGER AS $$ " +
                "BEGIN " +
                "    DELETE FROM pagos_transaccion WHERE transaccion_id IN ( " +
                "        SELECT t.id FROM transacciones t  " +
                "        JOIN accesos_sesion a ON t.acceso_id = a.id_acceso " +
                "        WHERE a.aprendiz_id = OLD.id_usuario " +
                "    ); " +
                "    DELETE FROM detalle_transaccion WHERE transaccion_id IN ( " +
                "        SELECT t.id FROM transacciones t  " +
                "        JOIN accesos_sesion a ON t.acceso_id = a.id_acceso " +
                "        WHERE a.aprendiz_id = OLD.id_usuario " +
                "    ); " +
                "    DELETE FROM errores_simulacion WHERE transaccion_id IN ( " +
                "        SELECT t.id FROM transacciones t  " +
                "        JOIN accesos_sesion a ON t.acceso_id = a.id_acceso " +
                "        WHERE a.aprendiz_id = OLD.id_usuario " +
                "    ); " +
                "    DELETE FROM comentarios_instructor WHERE transaccion_id IN ( " +
                "        SELECT t.id FROM transacciones t  " +
                "        JOIN accesos_sesion a ON t.acceso_id = a.id_acceso " +
                "        WHERE a.aprendiz_id = OLD.id_usuario " +
                "    ); " +
                "    DELETE FROM transacciones WHERE acceso_id IN ( " +
                "        SELECT a.id_acceso FROM accesos_sesion a WHERE a.aprendiz_id = OLD.id_usuario " +
                "    ); " +
                "    DELETE FROM accesos_sesion WHERE aprendiz_id = OLD.id_usuario; " +
                "    DELETE FROM reports WHERE user_id = OLD.id_usuario; " +
                "    DELETE FROM pagos_transaccion WHERE transaccion_id IN ( " +
                "        SELECT t.id FROM transacciones t  " +
                "        JOIN accesos_sesion a ON t.acceso_id = a.id_acceso " +
                "        JOIN sesiones_simulacion s ON a.sesion_id = s.id_sesion " +
                "        WHERE s.instructor_id = OLD.id_usuario " +
                "    ); " +
                "    DELETE FROM detalle_transaccion WHERE transaccion_id IN ( " +
                "        SELECT t.id FROM transacciones t  " +
                "        JOIN accesos_sesion a ON t.acceso_id = a.id_acceso " +
                "        JOIN sesiones_simulacion s ON a.sesion_id = s.id_sesion " +
                "        WHERE s.instructor_id = OLD.id_usuario " +
                "    ); " +
                "    DELETE FROM errores_simulacion WHERE transaccion_id IN ( " +
                "        SELECT t.id FROM transacciones t  " +
                "        JOIN accesos_sesion a ON t.acceso_id = a.id_acceso " +
                "        JOIN sesiones_simulacion s ON a.sesion_id = s.id_sesion " +
                "        WHERE s.instructor_id = OLD.id_usuario " +
                "    ); " +
                "    DELETE FROM comentarios_instructor WHERE transaccion_id IN ( " +
                "        SELECT t.id FROM transacciones t  " +
                "        JOIN accesos_sesion a ON t.acceso_id = a.id_acceso " +
                "        JOIN sesiones_simulacion s ON a.sesion_id = s.id_sesion " +
                "        WHERE s.instructor_id = OLD.id_usuario " +
                "    ); " +
                "    DELETE FROM transacciones WHERE acceso_id IN ( " +
                "        SELECT a.id_acceso FROM accesos_sesion a " +
                "        JOIN sesiones_simulacion s ON a.sesion_id = s.id_sesion " +
                "        WHERE s.instructor_id = OLD.id_usuario " +
                "    ); " +
                "    DELETE FROM accesos_sesion WHERE sesion_id IN ( " +
                "        SELECT s.id_sesion FROM sesiones_simulacion s WHERE s.instructor_id = OLD.id_usuario " +
                "    ); " +
                "    DELETE FROM comentarios_instructor WHERE instructor_id = OLD.id_usuario; " +
                "    DELETE FROM sesiones_simulacion WHERE instructor_id = OLD.id_usuario; " +
                "    RETURN OLD; " +
                "END; " +
                "$$ LANGUAGE plpgsql;"
            );
            jdbcTemplate.execute(
                "CREATE OR REPLACE TRIGGER trg_before_delete_usuarios " +
                "BEFORE DELETE ON usuarios " +
                "FOR EACH ROW " +
                "EXECUTE FUNCTION trg_clean_user_data();"
            );
            log.info("Trigger and function created successfully!");
        } catch (Exception e) {
            log.error("Could not create before delete trigger on 'usuarios': {}", e.getMessage());
        }

        // 2. Seed demo accounts if they don't exist
        ensureDemoUser("aprendiz@trainshier.com", "Aprendiz Caja POS", "Aprendiz123*", UserRole.APPRENTICE, "1029384756");
        ensureDemoUser("instructor@trainshier.com", "Instructor Formación", "Instructor123*", UserRole.INSTRUCTOR, "5678901234");
        ensureDemoUser("admin@trainshier.com", "Administrador Sistema", "Admin123*", UserRole.ADMINISTRATOR, "9876543210");

        // 3. Scan all users and migrate plain text passwords to BCrypt
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String pwd = user.getPassword();
            // BCrypt hashes start with $2a$, $2b$, or $2y$
            if (pwd != null && !pwd.startsWith("$2a$") && !pwd.startsWith("$2b$") && !pwd.startsWith("$2y$")) {
                log.info("Migrating plain-text password to BCrypt for user: {}", user.getEmail());
                user.setPassword(passwordEncoder.encode(pwd));
                userRepository.save(user);
            }
        }
        log.info("Database initialization and migration completed.");
    }

    private void ensureDemoUser(String email, String name, String password, UserRole role, String rfidUid) {
        Optional<User> existing = userRepository.findByEmail(email);
        if (existing.isEmpty()) {
            log.info("Creating missing demo user: {}", email);
            User user = new User();
            user.setEmail(email);
            user.setName(name);
            user.setPassword(passwordEncoder.encode(password));
            user.setRole(role);
            user.setRfidUid(rfidUid);
            userRepository.save(user);
        } else {
            User user = existing.get();
            boolean changed = false;
            String pwd = user.getPassword();
            if (pwd != null && !pwd.startsWith("$2")) {
                log.info("Demo user {} exists but has plain-text password. Encrypting...", email);
                user.setPassword(passwordEncoder.encode(password));
                changed = true;
            }
            if (user.getRfidUid() == null) {
                log.info("Demo user {} exists but lacks RFID UID. Associating: {}", email, rfidUid);
                user.setRfidUid(rfidUid);
                changed = true;
            }
            if (changed) {
                userRepository.save(user);
            }
        }
    }
}
