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

package org.omg.sysml.metamodel.impl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.omg.sysml.lifecycle.Data;
import org.omg.sysml.lifecycle.impl.DataImpl;

import javax.persistence.Transient;

import static jackson.RecordSerialization.IDENTITY_FIELD;

public abstract class SysMLTypeImpl extends DataImpl implements Data {

    public abstract java.util.UUID getElementId();

    public abstract void setElementId(java.util.UUID identifier);

    @Transient
    @JsonGetter(value = IDENTITY_FIELD)
    public java.util.UUID getId() {
        return getElementId();
    }

    @JsonSetter(value = IDENTITY_FIELD)
    public void setId(java.util.UUID id) {
        setElementId(id);
    }
}
