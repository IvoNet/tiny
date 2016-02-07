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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.net.URI;
import java.util.Calendar;

/**
 * @author Ivo Woltring
 */
@XmlRootElement
public class Token {

    @XmlElement
    private URI tinyUrl;
    @XmlElement
    private Integer id;
    @XmlElement
    private String destinationUrl;
    @XmlElement
    private Long counter;
    @XmlElement
    private Calendar creationDate;

    public Token() {
    }

    public Token(final URI tinyUrl, final Tiny tiny) {
        this.tinyUrl = tinyUrl;
        this.id = tiny.getId();
        this.destinationUrl = tiny.getUrl();
        this.counter = tiny.getCounter();
        this.creationDate = tiny.getCreationDate();
    }

    public URI getTinyUrl() {
        return this.tinyUrl;
    }

    public Integer getId() {
        return this.id;
    }

    public String getDestinationUrl() {
        return this.destinationUrl;
    }

    public Long getCounter() {
        return this.counter;
    }

    public Calendar getCreationDate() {
        return this.creationDate;
    }
}
