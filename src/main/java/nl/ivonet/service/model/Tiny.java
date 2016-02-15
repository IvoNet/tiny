/*
 * Copyright 2016 Ivo Woltring <WebMaster@ivonet.nl>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nl.ivonet.service.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Calendar;



@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Tiny.findById", query = "SELECT t FROM Tiny t WHERE t.id = :id"),
        @NamedQuery(name = "Tiny.findByUrl", query = "SELECT t FROM Tiny t WHERE t.url = :url"),
        @NamedQuery(name = "Tiny.updateCounter", query = "UPDATE Tiny t SET t.counter = (t.counter + 1) WHERE t.id = :id"),
        @NamedQuery(name = "Tiny.popular", query = "SELECT t FROM Tiny t ORDER BY t.counter DESC"),
        @NamedQuery(name = "Tiny.maxId", query = "SELECT MAX(t.id) FROM Tiny t"),
        @NamedQuery(name = "Tiny.lucky", query = "SELECT t FROM Tiny t WHERE t.id >= :seed")
})
@Table(name = "Tiny")
public class Tiny {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute
    private Integer id;

    @XmlElement
    @Column(length = 4242)
    @NotNull
    private String url;

    @SuppressWarnings("FieldMayBeFinal")
    @XmlElement
    @NotNull
    private Long counter = 0L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="creation_date", columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP", insertable=false, updatable=false)
    private Calendar creationDate;


    public Integer getId() {
        return this.id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(final String url) {
        this.url = url;
    }

    public Long getCounter() {
        return this.counter;
    }

    public Calendar getCreationDate() {
        return this.creationDate;
    }

    @Override
    public String toString() {
        return "Tiny{" + "id=" +
               this.id +
               ", url='" +
               this.url +
               '\'' +
               ", counter=" +
               this.counter +
               ", creationDate=" +
               this.creationDate +
               '}';
    }
}
