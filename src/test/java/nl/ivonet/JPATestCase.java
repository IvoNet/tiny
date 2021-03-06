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

package nl.ivonet;

import nl.ivonet.service.model.Tiny;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;
import java.util.Random;


/**
 * Use this testcase to play with and test your Entities against a test db.
 */
public class JPATestCase {

    //Not setup in the @Before method because it only needs to be done once during the whole test.
    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("testdbPU");
    private final EntityManager em = this.emf.createEntityManager();
    private final EntityTransaction tx = this.em.getTransaction();

    @Ignore
    @Test
    public void testDummyInsert() throws Exception {

        List<Tiny> tinies = this.em.createNamedQuery("Tiny.findByUrl", Tiny.class)
                                   .setParameter("url", "http://ivonet.nl")
                                   .getResultList();
        if (tinies.isEmpty()) {
            final Tiny dummy = new Tiny();
            dummy.setUrl("http://ivonet.nl");
            final Tiny dummy2 = new Tiny();
            dummy2.setUrl("http://ivonet.it");
            final Tiny dummy3 = new Tiny();
            dummy3.setUrl("http://ivo2u.it");
            this.tx.begin();
            this.em.persist(dummy);
            this.em.persist(dummy2);
            this.em.persist(dummy3);
            this.tx.commit();

        }

        tinies = this.em.createNamedQuery("Tiny.findByUrl", Tiny.class)
                        .setParameter("url", "http://ivonet.nl")
                        .getResultList();
        System.out.println("tiny = " + tinies.get(0)
                                             .getId());

        this.tx.begin();

        final Integer max = this.em.createNamedQuery("Tiny.maxId", Integer.class)
                                   .getSingleResult();
        System.out.println("max = " + max);

        final Random random = new Random();
        Tiny result = this.em.createNamedQuery("Tiny.lucky", Tiny.class)
                             .setParameter("seed", random.nextInt(max))
                             .setMaxResults(1)
                             .getSingleResult();
        System.out.println("result = " + result.getId());
        result = this.em.createNamedQuery("Tiny.lucky", Tiny.class)
                        .setParameter("seed", random.nextInt(max))
                        .setMaxResults(1)
                        .getSingleResult();
        System.out.println("result = " + result.getId());
        result = this.em.createNamedQuery("Tiny.lucky", Tiny.class)
                        .setParameter("seed", random.nextInt(max))
                        .setMaxResults(1)
                        .getSingleResult();
        System.out.println("result = " + result.getId());

//        System.out.println("tiny.getId() = " + this.em.createNamedQuery("Tiny.lucky", Tiny.class)
//                                         .getSingleResult().getId());
//        System.out.println("tiny.getId() = " + this.em.createNamedQuery("Tiny.lucky", Tiny.class)
//                                         .getSingleResult().getId());

//        Tiny tiny = this.em.createNamedQuery("Tiny.findById", Tiny.class).setParameter("id", 1).getSingleResult();
        this.tx.commit();

//        this.tx.begin();
//        tiny = this.em.createNamedQuery("Tiny.findById", Tiny.class).setParameter("id", 1).getSingleResult();
//        this.em.createNamedQuery("Tiny.updateCounter").setParameter("id", 1).executeUpdate();
//        this.tx.commit();


    }
}