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

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * Trial for a random number generator in singleton.
 * @author Ivo Woltring
 */
public class RandomSingleton {

    private final SecureRandom random;

    private RandomSingleton() {
        try {
            this.random = SecureRandom.getInstance("SHA1PRNG");
            this.random.nextInt();
        } catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }

    public static RandomSingleton getInstance() {
        return Instance.SINGLETON;
    }

    private static final class Instance {
        static final RandomSingleton SINGLETON = new RandomSingleton();
    }

    /**
     * Generate a random number.
     *
     * @param max int > 0
     * @return random generated number
     */
    public int random(final int max) {
        return this.random.nextInt(max);
    }

}
