/*
 * SysML v2 REST/HTTP Pilot Implementation
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

package dao;

import org.omg.sysml.lifecycle.Project;
import org.omg.sysml.lifecycle.Tag;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TagDao extends Dao<Tag> {

    List<Tag> findAllByProject(Project project, UUID after, UUID before, int maxResults);

    Optional<Tag> findByProjectAndId(Project project, UUID id);

    Optional<Tag> deleteByProjectAndId(Project project, UUID id);
}
