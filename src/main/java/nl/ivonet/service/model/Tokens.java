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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author Ivo Woltring
 */
@XmlRootElement
public class Tokens implements Iterable<Token> {

    @XmlElement
    private final List<Token> tokens;

    public Tokens() {
        this.tokens = new ArrayList<>();
    }

    public void add(final Token token) {
        this.tokens.add(token);
    }

    public Token get(final int idx) {
        return this.tokens.get(idx);
    }

    @Override
    public Iterator<Token> iterator() {
        return this.tokens.iterator();
    }
}
