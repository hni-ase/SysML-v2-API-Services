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

@AnyMetaDefs(value = {
        @AnyMetaDef(name = "RecordMetaDef", metaType = "string", idType = "java.util.UUID",
                metaValues = {
                        @MetaValue(value = "Commit", targetEntity = CommitImpl.class),
                        @MetaValue(value = "DataIdentity", targetEntity = DataIdentityImpl.class),
                        @MetaValue(value = "DataVersion", targetEntity = DataVersionImpl.class),
                        @MetaValue(value = "Project", targetEntity = ProjectImpl.class),
                        @MetaValue(value = "Query", targetEntity = QueryImpl.class),
                        @MetaValue(value = "Record", targetEntity = RecordImpl.class),
                }),
})

@GenericGenerators(value = {
        @GenericGenerator(name = "UseExistingOrGenerateUUIDGenerator", strategy = "jpa.UseExistingOrGenerateUUIDGenerator")
})
package org.omg.sysml.record.impl;

import org.hibernate.annotations.*;
import org.omg.sysml.lifecycle.impl.CommitImpl;
import org.omg.sysml.lifecycle.impl.DataIdentityImpl;
import org.omg.sysml.lifecycle.impl.DataVersionImpl;
import org.omg.sysml.lifecycle.impl.ProjectImpl;
import org.omg.sysml.query.impl.QueryImpl;