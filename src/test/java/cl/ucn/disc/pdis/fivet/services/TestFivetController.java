/*
 * Copyright (c) 2022 Marcelo Lam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cl.ucn.disc.pdis.fivet.services;

import cl.ucn.disc.pdis.fivet.model.Persona;
import cl.ucn.disc.pdis.fivet.orm.ZonedDateTimeType;
import com.j256.ormlite.field.DataPersisterManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Testing the Test Fivet Controller.
 *
 * @author Marcelo Lam
 */
@Slf4j
public final class TestFivetController {

    @SneakyThrows
    @Test
    @DisplayName("The Main Test for Fivet Controller")
    void theMain() {
        log.debug("Starting the MainTest...");

        log.debug("Registering the ZonedDateTimeType...");
        DataPersisterManager.registerDataPersisters(ZonedDateTimeType.INSTANCE);

        // The driver connection
        String databaseUrl = "jdbc:h2:mem:fivet";
        // String databaseUrl = "jdbc:postgresql:fivet";
        // String databaseUrl = "jdbc:sqlite:fivet.db";
        // String databaseUrl = "jdbc:mysql://localhost:3306/fivet";

        log.debug("Building the Connection, using: {}", databaseUrl);
        @Cleanup
        ConnectionSource cs = new JdbcConnectionSource(databaseUrl);

        log.debug("Dropping the tables...");
        // Drop the database
        TableUtils.dropTable(cs, Persona.class, true);

        log.debug("Creating the tables...");
        // Create the database
        TableUtils.createTable(cs, Persona.class);

        // The Controller
        FivetController fivetController = new FivetControllerImpl(databaseUrl);

        // Add Persona
        log.debug("Add persona...");
        {
            Persona persona = Persona.builder()
                    .rut("123456780")
                    .nombre("Random Person")
                    .email("random.person@gmail.com")
                    .direccion("Random Avenue 123")
                    .telefonoMovil("912345678")
                    .telefonoFijo("2233441")
                    .build();
            fivetController.addPersona(persona, "soyunacontraseña");
            log.debug("To db: {}", ToStringBuilder.reflectionToString(persona, ToStringStyle.MULTI_LINE_STYLE));
        }

        // Log in...
        {
            // Using the following credentials
            String login = "123456780";
            String pass = "soyunacontraseña";

            Optional<Persona> persona = fivetController.autenticar(login, pass);
            Assertions.assertTrue(persona.isPresent(), "The persona was Empty");

            // With a person that not exists in the controller
            login = "0";
            pass = "soyunacontraseña";
            Optional<Persona> persona2 = fivetController.autenticar(login, pass);
            Assertions.assertFalse(persona2.isPresent(), "The persona was not Empty");

            // With incorrect password
            login = "123456780";
            pass = "incorrect";
            Optional<Persona> persona3 = fivetController.autenticar(login, pass);
            Assertions.assertFalse(persona3.isPresent(), "The persona was not Empty");

        }

        // Drop the database
        TableUtils.dropTable(cs, Persona.class, true);

        log.debug("Done.");
    }


}
