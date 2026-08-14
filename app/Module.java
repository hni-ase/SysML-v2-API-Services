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

import com.google.inject.AbstractModule;
import config.MetamodelProvider;
import config.impl.JPAMetamodelProvider;
import dao.*;
import dao.impl.FlatSchemaDao;
import dao.impl.jpa.*;
import jackson.databind.ObjectMapperFactory;
import jackson.databind.impl.HibernateObjectMapperFactory;
import jpa.manager.JPAManager;
import jpa.manager.impl.HibernateManager;

public class Module extends AbstractModule {

    @Override
    protected void configure() {
        bind(MetamodelProvider.class).to(JPAMetamodelProvider.class).asEagerSingleton();
        bind(JPAManager.class).to(HibernateManager.class).asEagerSingleton();
        bind(ObjectMapperFactory.class).to(HibernateObjectMapperFactory.class).asEagerSingleton();
        bind(DataDao.class).to(JpaDataDao.class);
        bind(ElementDao.class).to(JpaElementDao.class);
        bind(ProjectDao.class).to(JpaProjectDao.class);
        bind(RelationshipDao.class).to(JpaRelationshipDao.class);
        bind(CommitDao.class).to(JpaCommitDao.class);
        bind(QueryDao.class).to(JpaQueryDao.class);
        bind(BranchDao.class).to(JpaBranchDao.class);
        bind(TagDao.class).to(JpaTagDao.class);
        bind(SchemaDao.class).to(FlatSchemaDao.class);
    }
}