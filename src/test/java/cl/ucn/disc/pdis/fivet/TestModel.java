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

package cl.ucn.disc.pdis.fivet;

import cl.ucn.disc.pdis.fivet.model.Control;
import cl.ucn.disc.pdis.fivet.model.FichaMedica;
import cl.ucn.disc.pdis.fivet.model.Persona;
import cl.ucn.disc.pdis.fivet.orm.DAO;
import cl.ucn.disc.pdis.fivet.orm.ORMLiteDAO;
import com.j256.ormlite.support.ConnectionSource;
import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * The Test of the Model.
 *
 * @author Marcelo Lam.
 */
@Slf4j
public class TestModel {

    /**
     * Testing the Model.
     */
    @SneakyThrows
    @Test
    public void TestModel() {

        log.info("Starting the test.. ");

        // The password encoder.
        PasswordEncoder passwordEncoder = new Argon2PasswordEncoder();

        log.debug("Build the ConnectionSource ..");

        // The Connection.
        @Cleanup
        ConnectionSource cs = ORMLiteDAO.buildConnectionSource("jdbc:h2:mem:fivet");
        // ConnectionSource cs = ORMLiteDAO.buildConnectionSource("jdbc:sqlite:fivet_test.db");

        log.debug("Loading FichaMedica ..");
        DAO<FichaMedica> fichaMedicaDAO = new ORMLiteDAO<>(cs, FichaMedica.class);
        fichaMedicaDAO.dropAndCreateTable();

        log.debug("Loading Persona ..");
        DAO<Persona> personaDAO = new ORMLiteDAO<>(cs, Persona.class);
        personaDAO.dropAndCreateTable();

        log.debug("Loading Control ..");
        DAO<Control> controlDAO = new ORMLiteDAO<>(cs, Control.class);
        controlDAO.dropAndCreateTable();

        // Saving a Persona.
        {
            Persona persona = Persona.builder()
                    .rut("197105992")
                    .nombre("Marcelo Lam Biaggini")
                    .email("marcelo.lam@alumnos.ucn.cl")
                    .password("mlam123")
                    .direccion("Angamos 0610")
                    .build();
            personaDAO.save(persona);
            print("Persona", persona);
            Assertions.assertNotNull(persona.getId(), "ID was null");
        }

        // Retrieve a Persona.
        {
            Persona persona = personaDAO.get(1).orElseThrow();
            print("Persona", persona);

            // Save a Ficha Medica.
            {
                FichaMedica fichaMedica = FichaMedica.builder()
                        .numeroFicha(100)
                        .nombrePaciente("Rusia")
                        .especie("Felino")
                        .fechaNacimiento(ZonedDateTime.now())
                        .raza("Tabby")
                        .color("Amarillo")
                        .tipo("Gigante")
                        .duenio(persona)
                        .sexo(FichaMedica.Sexo.MACHO)
                        .build();
                fichaMedicaDAO.save(fichaMedica);
                Assertions.assertNotNull(fichaMedica.getId(), "ID was null");
                print("FichaMedica", fichaMedica);
            }
        }

        // Retrieve a FichaMedica
        {
            FichaMedica fichaMedica = fichaMedicaDAO.get(1).orElseThrow();
            print("FichaMedica", fichaMedica);
            print("Duenio", fichaMedica.getDuenio());
            Assertions.assertNotNull(fichaMedica.getDuenio(), "Duenio was null");

            // Create a control
            {
                Control control = Control.builder()
                        .altura(0.4f)
                        .diagnostico("Sin novedad")
                        .fecha(ZonedDateTime.now())
                        .peso(5.2f)
                        .temperatura(35.7f)
                        // .fichaMedica(fichaMedica)
                        .veterinario(fichaMedica.getDuenio())
                        .build();
                fichaMedica.add(control);
            }
        }

        // Retrieve a FichaMedica with Control
        {
            FichaMedica fichaMedica = fichaMedicaDAO.get(1).orElseThrow();
            print("FichaMedica", fichaMedica);
            Assertions.assertNotNull(fichaMedica.getControles(), "Controles was null");
            Assertions.assertEquals(1, fichaMedica.getControles().size(), "Controles !=1");
            for (Control c : fichaMedica.getControles()) {
                print("Control", c);
            }
        }

        log.info("Done.");
    }

    /**
     * Print a Object with message
     *
     * @param message to use
     * @param o to use
     */
    private static void print(String message, Object o) {
        log.debug("{}: {}", message, ToStringBuilder.reflectionToString(o, ToStringStyle.MULTI_LINE_STYLE));
    }


}
