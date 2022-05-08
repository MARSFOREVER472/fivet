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

package cl.ucn.disc.pdis.fivet.model;

import cl.ucn.disc.pdis.fivet.model.orm.BaseEntity;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

/**
 * The model of the control object created for the patients.
 *
 * @author Marcelo Lam
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@DatabaseTable(tableName = "control")
public class Control extends BaseEntity {

    /**
     * The fecha of the control carried out.
     */
     @Getter
     @DatabaseField(canBeNull = false)
     private ZonedDateTime fecha;

    /**
     * The patient of the temperatura in the control.
     */
     @Getter
     @DatabaseField(canBeNull = false)
     private Float temperatura;

    /**
     * The peso of the patient in the control.
     */
     @Getter
     @DatabaseField(canBeNull = false)
     private Float peso;

    /**
     * The altura of the patient in the control.
     */
    @Getter
    @DatabaseField(canBeNull = false)
    private Float altura;

    /**
     * The patient's diagnostico.
     */
     @Getter
     @DatabaseField(canBeNull = false)
     private String diagnostico;

    /**
     * The vet's name.
     */
     @Getter
     @DatabaseField(canBeNull = false)
     private String nomVeterinario;

    /**
     * The date of the next date.
     */
    @Getter
    @DatabaseField(canBeNull = false)
    private ZonedDateTime proxControl;

}
