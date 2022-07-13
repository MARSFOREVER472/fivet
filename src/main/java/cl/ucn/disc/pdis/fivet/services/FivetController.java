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
import cl.ucn.disc.pdis.fivet.model.FichaMedica;
import cl.ucn.disc.pdis.fivet.model.Persona;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The Fivet Controller Interface.
 */
public interface FivetController {

    /**
     * Check if the Email or Rut exists in the system.
     * @param login rut or email.
     * @return a persona.
     */
    Optional<Persona> retrieveByLogin(String login);
    /**
     *
     * @param login The login account.
     * @param password The password of the user.
     * @return a persona.
     */
    Optional<Persona> autenticar(String login, String password);

    /**
     * Add a Persona in the backend.
     * @param persona to add.
     * @param password to hash.
     */
    void addPersona(Persona persona, String password);

    // Ficha

    /**
     * Add a Control in a FichaMedica.
     * @param control to add.
     */
    void addControl(Control control);

    /**
     * Add a FichaMedica in the system.
     * @param fichaMedica to add.
     */
    void addFichaMedica(FichaMedica fichaMedica);

    /**
     * Get a FichaMedica by the numeroFicha.
     * @param numeroFicha to use.
     */
    Optional<FichaMedica> getFichaMedica(Integer numeroFicha);

    /**
     * Search 0 or more FichaMedica and return a List of FichaMedica.
     * @param q to use.
     * @param fichasMedicasDB the FichaMedica list of the data base.
     * @param attribute to use.
     * @return list of FichaMedica.
     */
    Collection<FichaMedica> searchFichaMedica(String q, Collection<FichaMedica>
            fichasMedicasDB, Integer attribute);

    /**
     * Delete a persona by id.
     * @param idPersona the id.
     */
    void delete(Integer idPersona);

    // TODO Metodo Ficha

    /**
     * Return all FichaMedica in the system.
     * @return Collection of FichaMedica.
     */
    List<FichaMedica> getAllFichaMedica();
}
