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

package cl.ucn.disc.pdis.fivet.model.orm;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * The ORMLite implementation of DAO.
 *
 * @author Marcelo Lam
 *
 */
public final class ORMLiteDAO<T extends BaseEntity> implements DAO<T> {

    /**
     * The real DAO (connection to ORMLite DAO).
     */
    private final Dao<T, Integer> theDao;

    /**
     * The Constructor of ORMLiteDAO
     * @param cs        the connection to the database.
     * @param clazz     the type of T.
     */
    @SneakyThrows(SQLException.class)
    public ORMLiteDAO(@NonNull final ConnectionSource cs, @NonNull Class<T> clazz){
        this.theDao = DaoManager.createDao(cs,clazz);
    }


    /**
     * Get optional T
     *
     * @param id to search
     */
    @SneakyThrows(SQLException.class)
    @Override
    public Optional<T> get(final Integer id) {

        // Exec the SQL
        T t = this.theDao.queryForId(id);
        return t == null ? Optional.empty() : Optional.of(t);

        /*  Lo mismo que la linea 72:

        if( t== null){
            return Optional.empty();
        }else{
            return Optional.of(t);
        }
         */
    }

    /**
     * Get all the Ts
     *
     * @return the List of T
     */
    @SneakyThrows(SQLException.class)
    @Override
    public List<T> getAll() {
        return this.theDao.queryForAll();
    }

    /**
     * Save a T.
     *
     * @param t to save
     */
    @SneakyThrows(SQLException.class)
    @Override
    public void save(T t) {
        int created = this.theDao.create(t);

        if (created != 1){
            throw new SQLException("Rows created =/= 1");
        }
    }

    /**
     * Delete a T
     *
     * @param t to delete
     */
    @SneakyThrows(SQLException.class)
    @Override
    public void delete(T t) {
        this.delete(t.getId());
    }



    /**
     * Delete a T with id
     *
     * @param id of th et to delete.
     */
    @SneakyThrows(SQLException.class)
    @Override
    public void delete(Integer id) {
        int deleted = this.theDao.deleteById(id);
        if (deleted != 1){
            throw new SQLException("Rows deleted =/= 1");
        }
    }
}
