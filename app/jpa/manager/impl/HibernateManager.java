/*
 * SysML v2 REST/HTTP Pilot Implementation
 * Copyright (C) 2020 InterCAX LLC
 * Copyright (C) 2020 California Institute of Technology ("Caltech")
 * Copyright (C) 2021 Twingineer LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the Eclipse Public License as published by
 * the Eclipse Foundation, version 2 of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * Eclipse Public License for more details.
 *
 * You should have received a copy of the Eclipse Public License
 * along with this program.  If not, see <https://www.eclipse.org/legal/epl-2.0/>.
 *
 * @license EPL-2.0 <http://spdx.org/licenses/EPL-2.0>
 */

package jpa.manager.impl;

import jpa.manager.JPAManager;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.function.Consumer;
import java.util.function.Function;

@Singleton
public class HibernateManager implements JPAManager {
    public static final String PERSISTENCE_UNIT_NAME = "sysml2-hibernate";

    private final EntityManagerFactory entityManagerFactory;

    public HibernateManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    @Override
    public String getPersistenceUnitName() {
        return PERSISTENCE_UNIT_NAME;
    }

    @Override
    public EntityManagerFactory getEntityManagerFactory() {
        return entityManagerFactory;
    }

    @Override
    public <R> R transact(Function<EntityManager, R> function) {
        EntityManager entityManager = getEntityManagerFactory().createEntityManager();
        try {
            return function.apply(entityManager);
        } finally {
            entityManager.close();
        }
    }

    @Override
    public void transact(Consumer<EntityManager> consumer) {
        EntityManager entityManager = getEntityManagerFactory().createEntityManager();
        try {
            consumer.accept(entityManager);
        } finally {
            entityManager.close();
        }
    }
}
