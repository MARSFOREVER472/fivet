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

import cl.ucn.disc.pdis.fivet.model.Control;
import cl.ucn.disc.pdis.fivet.model.Examen;
import cl.ucn.disc.pdis.fivet.model.FichaMedica;
import cl.ucn.disc.pdis.fivet.model.Persona;
import cl.ucn.disc.pdis.fivet.orm.DAO;
import cl.ucn.disc.pdis.fivet.orm.ORMLiteDAO;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.swing.text.html.Option;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The fivet controller implementation.
 *
 * @author Marcelo Lam.
 */
@Slf4j
public class FivetControllerImpl implements FivetController {

    /**
     * The DAO of the entities.
     */
    private DAO<Persona> daoPersona;
    private DAO<FichaMedica> daoFichaMedica;

    /**
     * The hasher.
     */
    private final static PasswordEncoder PASSWORD_ENCODER = new Argon2PasswordEncoder();

    /**
     * The FivetControllerImpl Constructor
     * @param dbUrl to connect
     */
    @SneakyThrows
    public FivetControllerImpl(String dbUrl) {

        ConnectionSource cs = new JdbcConnectionSource(dbUrl);
        this.daoPersona = new ORMLiteDAO<>(cs,Persona.class);
        this.daoFichaMedica = new ORMLiteDAO<>(cs, FichaMedica.class);

        // Create the table from Persona annotations
        TableUtils.createTableIfNotExists(cs, Persona.class);

        // Create the table from Persona annotations
        TableUtils.createTableIfNotExists(cs, FichaMedica.class);

        // Create the table from Persona annotations
        TableUtils.createTableIfNotExists(cs, Control.class);

        // Create the table from Persona annotations
        TableUtils.createTableIfNotExists(cs, Examen.class);

    }

    /**
     * Check if the Email or Rut exists in the system
     *
     * @param login rut or email
     * @return a persona
     */
    @Override
    public Optional<Persona> retrieveByLogin(String login) {
        var persona = this.daoPersona.get("rut", login);
        if(persona.isPresent()) {
            return persona;
        }
        return this.daoPersona.get("email", login);
    }


    /**
     * Authentication of a person in the system
     * @param login The login account
     * @param passwd The password of the user
     * @return a Persona
     */
    @Override
    public Optional<Persona> autenticar(String login, String passwd) {
        Optional<Persona> persona = this.daoPersona.get("rut", login);

        // Verify if the rut is in the database.
        if (persona.isEmpty()) {
            persona = this.daoPersona.get("email", login);
        }
        // Verify if the Email is in the database.
        if (persona.isEmpty()) {
            return Optional.empty();
        }

        // Check the password to verify that is the correct password.
        if (BCrypt.checkpw(passwd, persona.get().getPassword())) {
            return persona;
        }

        // Wrong password, return an empty.
        log.warn("Incorrect password");
        return Optional.empty();
    }

    /**
     * Add a Persona into the backend.
     *
     * @param persona  to add.
     * @param password to hash.
     */
    @Override
    public void addPersona(Persona persona, String password) {
        persona.setPassword(BCrypt.hashpw(password,
                BCrypt.gensalt(12)));
        this.daoPersona.save(persona);
    }

    /**
     * Add a Control in a FichaMedica.
     *
     * @param control to add.
     */
    @Override
    public void addControl(Control control) {
        Optional<FichaMedica> fichaMedica = this.daoFichaMedica.get(control.getFichaMedica().getNumeroFicha());
        Optional<Persona> veterinario = this.daoPersona.get("rut", control.getVeterinario().getRut());
        if (fichaMedica.isPresent() && veterinario.isPresent()) {
            control.setVeterinario(veterinario.get());
            control.setFichaMedica(fichaMedica.get());
        }
        fichaMedica.get().getControles().add(control);
    }

    /**
     * Add a FichaMedica in the system.
     *
     * @param fichaMedica to add.
     */
    @Override
    public void addFichaMedica(FichaMedica fichaMedica) {
        Collection<Persona> personas = daoPersona.getAll();
        for (Persona persona : personas) {
            if (persona.getRut().equals(fichaMedica.getDuenio().getRut())) {
                fichaMedica.setDuenio(persona);
                break;
            }
        }
        this.daoFichaMedica.save(fichaMedica);
    }

    /**
     * Get a FichaMedica by the numeroFicha.
     *
     * @param numeroFicha to use.
     */
    @Override
    public Optional<FichaMedica> getFichaMedica(Integer numeroFicha) {
        Optional<FichaMedica> fichaMedica = this.daoFichaMedica.get(numeroFicha);
        return fichaMedica;
    }

    /**
     * Search 0 or more FichaMedica and return a List of FichaMedica.
     * @param q to use.
     * @param fichasMedicasDB the FichaMedica list of the data base.
     * @param attribute to use.
     * @return list of FichaMedica.
     */
    @Override
    public Collection<FichaMedica> searchFichaMedica(String q, Collection<FichaMedica> fichasMedicasDB
            , Integer attribute) {
        Collection<FichaMedica> fichasMedicas = new ArrayList<>();
        if (attribute == 1) {
            for (FichaMedica fichaMedica : fichasMedicasDB) {
                int coincidencias = StringUtils.countMatches(q, fichaMedica.getDuenio().getRut());
                if (coincidencias > 1) {
                    fichasMedicas.add(fichaMedica);
                }
            }
        }
        else if (attribute == 2) {
            for (FichaMedica fichaMedica : fichasMedicasDB) {
                int coincidencias = StringUtils.countMatches(q, fichaMedica.getNombrePaciente());
                if (coincidencias > 1) {
                    fichasMedicas.add(fichaMedica);
                }
            }
        }
        else if (attribute == 3) {
            for (FichaMedica fichaMedica : fichasMedicasDB) {
                int coincidencias = StringUtils.countMatches(q, fichaMedica.getDuenio().getNombre());
                if (coincidencias > 1) {
                    fichasMedicas.add(fichaMedica);
                }
            }
        }

        return fichasMedicas;
    }


    /**
     * Delete a persona by id.
     *
     * @param idPersona the id.
     */

    @Override
    public void delete(Integer idPersona) {
        this.daoPersona.delete(idPersona);

    }

    /**
     * Return all FichaMedica in the system.
     *
     * @return Collection of FichaMedica.
     */
    @Override
    public List<FichaMedica> getAllFichaMedica() {
        List<FichaMedica> lista = this.daoFichaMedica.getAll();
        return lista;
    }
}
