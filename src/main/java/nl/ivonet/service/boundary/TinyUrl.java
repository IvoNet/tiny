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

package nl.ivonet.service.boundary;

/**
 * @author Ivo Woltring
 */
public class TinyUrl {

    //    public static final String ALPHABET = "23456789bcdfghjkmnpqrstvwxyzBCDFGHJKLMNPQRSTVWXYZ-_";
    public static final String ALPHABET = "2Wd5YgtoZVMHviXyPbKLG3r1FqwaunhUDBef8E40zRxpITNlAcSmCQO6jsJk7";
    public static final int BASE = ALPHABET.length();

    public String encode(final int id) {
        int num = id;
        final StringBuilder str = new StringBuilder();
        while (num > 0) {
            str.insert(0, ALPHABET.charAt(num % BASE));
            num /= BASE;
        }
        return str.toString();
    }

    public int decode(final String str) {
        int num = 0;
        for (int i = 0; i < str.length(); i++) {
            num = (num * BASE) + ALPHABET.indexOf(str.charAt(i));
        }
        return num;
    }
}
