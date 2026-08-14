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

package org.omg.sysml.lifecycle.impl;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.Any;
import org.omg.sysml.lifecycle.Data;
import org.omg.sysml.lifecycle.DataIdentity;
import org.omg.sysml.lifecycle.DataVersion;
import org.omg.sysml.record.impl.RecordImpl;

import javax.persistence.*;

@Entity(name = "DataVersionImpl")
@Table(name = "DataVersion")
@JsonTypeName(value = "DataVersion")
public class DataVersionImpl extends RecordImpl implements DataVersion {
    private Data payload;
    private DataIdentity identity;

    @Any(metaDef = "DataMetaDef", metaColumn = @Column(name = "payloadType"), fetch = FetchType.EAGER)
    @JoinColumn(name = "payloadId")
    @org.hibernate.annotations.Cascade(value = org.hibernate.annotations.CascadeType.ALL)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    public Data getPayload() {
        return payload;
    }

    @JsonDeserialize(as = DataImpl.class)
    public void setPayload(Data data) {
        this.payload = data;
    }

    @ManyToOne(targetEntity = DataIdentityImpl.class, cascade = javax.persistence.CascadeType.ALL, fetch = FetchType.EAGER)
    public DataIdentity getIdentity() {
        return identity;
    }

    @JsonDeserialize(as = DataIdentityImpl.class)
    public void setIdentity(DataIdentity identity) {
        this.identity = identity;
    }

    @Transient
    @JsonProperty("@type")
    public String getType() {
        return DataVersion.NAME;
    }
}
