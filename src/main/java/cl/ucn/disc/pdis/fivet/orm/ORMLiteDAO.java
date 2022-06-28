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

package cl.ucn.disc.pdis.fivet.orm;

import com.google.common.collect.Lists;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.support.ConnectionSource;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;


/**
 * The ORMLite implementation of DAO.
 *
 * @author Marcelo Lam
 *
 */
@Slf4j
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
        if (t == null) {
            return Optional.empty();
        }
        if (t.getDeletedAt() != null) {
            return Optional.empty();
        }
        return Optional.of(t);
    }


    /**
     * Get all the Ts
     *
     * @return the List of T
     */
    @SneakyThrows(SQLException.class)
    @Override
    public List<T> getAll() {
        List<T> list = Lists.newArrayList();

        for (T t : theDao.queryForAll()) {
            if (t.getDeletedAt() == null) {
                list.add(t);
            }
        }
        return list;
    }

    /**
     * Get optional T by some attribute.
     *
     * @param attribute to filter.
     * @param value     of attribute.
     * @return the optional T.
     */
    @SneakyThrows
    @Override
    public Optional<T> get(String attribute, Object value) {
        // Retrieve from database
        List<T> filtered = this.removeDeleted(this.theDao.queryForEq(attribute, value));

        // Show a warning
        if (filtered.size() > 1) {
            log.warn("Founded more than one value in {} for {}", value, attribute);
        }
        return filtered.isEmpty() ? Optional.empty() : Optional.of(filtered.get(0));
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

        if (created != 1) {
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
        t.deletedAt = ZonedDateTime.now();
        int deleted = this.theDao.update(t);

        if (deleted != 1) {
            throw new SQLException("Rows updated != 1");
        }
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
        Optional<T> t = this.get(id);
        if (t.isEmpty()) {
            return;
        }
        t.get().deletedAt = ZonedDateTime.now();

        if (this.theDao.update(t.get()) != 1){
            throw new SQLException("Rows deleted =/= 1");
        }
    }

    /**
     * Remove the deleted T
     *
     * @param theList to filter.
     * @return the List filtered.
     */
    private List<T> removeDeleted(List<T> theList) {
        // Remove the deleteds
        return theList.stream()
                .filter(t -> t.getDeletedAt() == null)
                .toList();
    }
}