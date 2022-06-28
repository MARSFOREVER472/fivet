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

package cl.ucn.disc.pdis.fivet;

import cl.ucn.disc.pdis.fivet.model.Persona;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


/**
 * The test of Database.
 *
 * @author Marcelo Lam
 */
public class TestDatabase {

    /**
     * The method of the Database test.
     */
    @Test
    public void testDatabase() {
        String databaseUrl = "jdbc:h2:mem:fivet_db";
        try (ConnectionSource connectionSource = new JdbcConnectionSource(databaseUrl)) {
            TableUtils.createTableIfNotExists(connectionSource, Persona.class);
            // Create dao.
            Dao<Persona, Integer> daoPersona = DaoManager.createDao(connectionSource, Persona.class);

            Persona persona = new Persona();
            int row = daoPersona.create(persona);
            Assertions.assertEquals(1, row);

            Persona personaFromDb = daoPersona.queryForId(persona.getId());
            Assertions.assertEquals(persona.getNombre(), personaFromDb.getNombre());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}