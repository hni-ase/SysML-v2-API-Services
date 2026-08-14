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

package org.omg.sysml.query;

import org.omg.sysml.lifecycle.Project;
import org.omg.sysml.record.Record;

import javax.validation.constraints.NotNull;
import java.util.Set;

public interface Query extends Record {

    @Override
    @NotNull
    String getName();

    @Override
    void setName(@NotNull String name);

    Project getOwningProject();

    void setOwningProject(Project owningProject);

    Set<String> getSelect();

    void setSelect(Set<String> select);
/*
    Set<DataIdentity> getScope();

    void setScope(Set<DataIdentity> scope);

    Boolean getRecursiveInScope();

    void setRecursiveInScope(Boolean recursiveInScope);

    List<String> getOrderBy();

    void setOrderBy(List<String> orderBy);
*/

    Constraint getWhere();

    void setWhere(Constraint where);
}
