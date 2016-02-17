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

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author Ivo Woltring
 */
public class RandomSingletonTest {

    @Ignore
    @Test
    public void testRandom() throws Exception {

        for (int i = 0; i < 100; i++) {
            final RandomSingleton instance = RandomSingleton.getInstance();
            System.out.println(instance.random(100));
        }
    }
}