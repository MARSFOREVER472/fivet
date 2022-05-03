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
 * FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package cl.ucn.disc.pdis.fivet.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Persona model.
 */
@DatabaseTable(tableName = "persona")
public class Persona {

    /**
     * The id.
     */
    @DatabaseField(generatedId = true)
    private int id;
    /**
     * The name.
     */
    @DatabaseField(canBeNull = false)
    private String nombre;
    /**
     * The direction.
     */
    @DatabaseField(canBeNull = false)
    private String direccion;
    /**
     * The phone.
     */
    @DatabaseField(canBeNull = false)
    private String telefonoFijo;
    /**
     * The mobile phone number.
     */
    @DatabaseField(canBeNull = false)
    private String telefonoMovil;
    /**
     * The email.
     */
    @DatabaseField(canBeNull = false)
    private String email;

    /**
     * The rut.
     */
    @DatabaseField(canBeNull = false, index = true)
    private String rut;

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTelefonoFijo() {
        return telefonoFijo;
    }

    public String getTelefonoMovil() {
        return telefonoMovil;
    }

    public String getEmail() {
        return email;
    }

    public String getRut() {
        return rut;
    }

    /**
     * Empty constructor.
     */
    public Persona() {
    }
    /**
     * The constructor from the class Persona.
     *
     * @param nombre
     * @param direccion
     * @param telefonoFijo
     * @param telefonoMovil
     * @param email
     * @param rut
     */

    public Persona(String nombre, String direccion, String telefonoFijo, String telefonoMovil, String email, String rut) {

        this.nombre = nombre;
        this.direccion = direccion;
        this.telefonoFijo = telefonoFijo;
        this.telefonoMovil = telefonoMovil;
        this.email = email;
        this.rut = rut;
    }
}
