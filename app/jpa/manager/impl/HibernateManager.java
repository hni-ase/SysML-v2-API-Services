/*
 * SysML v2 REST/HTTP Pilot Implementation
 * Copyright (C) 2020 InterCAX LLC
 * Copyright (C) 2020 California Institute of Technology ("Caltech")
 * Copyright (C) 2021 Twingineer LLC
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * @license LGPL-3.0-or-later <http://spdx.org/licenses/LGPL-3.0-or-later>
 */

package jpa.manager.impl;

import jpa.manager.JPAManager;

import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Singleton
public class HibernateManager implements JPAManager {
    public static final String PERSISTENCE_UNIT_NAME = "sysml2-hibernate";

    private static final String ENV_JDBC_URL = "SYSML_JDBC_URL";
    private static final String ENV_JDBC_USER = "SYSML_JDBC_USER";
    private static final String ENV_JDBC_PASSWORD = "SYSML_JDBC_PASSWORD";

    private final EntityManagerFactory entityManagerFactory;

    public HibernateManager() {
        entityManagerFactory = Persistence.createEntityManagerFactory(
                PERSISTENCE_UNIT_NAME,
                jdbcOverridesFromEnvironment()
        );
    }

    /**
     * Optional JDBC overrides from environment. When unset, values from
     * {@code conf/META-INF/persistence.xml} are used.
     */
    private static Map<String, Object> jdbcOverridesFromEnvironment() {
        Map<String, Object> overrides = new HashMap<>();
        putIfPresent(overrides, "javax.persistence.jdbc.url", System.getenv(ENV_JDBC_URL));
        putIfPresent(overrides, "javax.persistence.jdbc.user", System.getenv(ENV_JDBC_USER));
        putIfPresent(overrides, "javax.persistence.jdbc.password", System.getenv(ENV_JDBC_PASSWORD));
        return overrides;
    }

    private static void putIfPresent(Map<String, Object> target, String key, String value) {
        if (value != null && !value.trim().isEmpty()) {
            target.put(key, value.trim());
        }
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
