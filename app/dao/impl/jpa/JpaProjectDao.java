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

package dao.impl.jpa;

import dao.ProjectDao;
import jpa.manager.JPAManager;
import org.omg.sysml.lifecycle.Branch;
import org.omg.sysml.lifecycle.Project;
import org.omg.sysml.lifecycle.impl.BranchImpl;
import org.omg.sysml.lifecycle.impl.ProjectImpl;
import org.omg.sysml.lifecycle.impl.ProjectImpl_;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.time.ZonedDateTime;
import java.util.Optional;

@Singleton
public class JpaProjectDao extends SimpleJpaDao<Project, ProjectImpl> implements ProjectDao {

    @Inject
    public JpaProjectDao(JPAManager jpaManager) {
        super(jpaManager, ProjectImpl.class, ProjectImpl_.id);
    }

    @Override
    public Optional<Project> update(Project project) {
        if (project.getDefaultBranch() == null) {
            throw new IllegalArgumentException("Default branch must be provided");
        }
        return super.update(project);
    }

    @Override
    public Optional<Project> persist(Project project) {
        if (project.getDefaultBranch() != null) {
            throw new IllegalArgumentException("Cannot specify default branch when creating Project");
        }
        Branch defaultBranch = new BranchImpl();
        defaultBranch.setOwningProject(project);
        defaultBranch.setName(Project.DEFAULT_BRANCH_NAME);
        // TODO Add timestamp to Project and inherit into defaultBranch
        defaultBranch.setCreated(ZonedDateTime.now());
        project.setDefaultBranch(defaultBranch);
        return super.persist(project);
    }
}
