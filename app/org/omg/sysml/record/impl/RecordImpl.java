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

package org.omg.sysml.record.impl;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import org.omg.sysml.record.Record;

import javax.annotation.Nullable;
import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import static jackson.RecordSerialization.IDENTITY_FIELD;

@MappedSuperclass
public abstract class RecordImpl implements Record {
    private UUID id;
    private String name;
    private Set<String> alias = new LinkedHashSet<>();
    private String description;

    @Override
    @Id
    @GeneratedValue(generator = "UseExistingOrGenerateUUIDGenerator")
    @Column(name = "id")
    @JsonGetter(value = IDENTITY_FIELD)
    public UUID getId() {
        return id;
    }

    @JsonSetter(value = IDENTITY_FIELD)
    public void setId(UUID id) {
        this.id = id;
    }

    @Override
    @JsonGetter
    @Lob
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    @Column(name = "name")
    public @Nullable String getName() {
        return name;
    }

    @Override
    @JsonSetter
    public void setName(@Nullable String name) {
        this.name = name;
        if (name != null) {
            this.alias.add(name);
        }
    }

    @Override
    @JsonGetter
    @Lob
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    @ElementCollection(targetClass = String.class)
    @CollectionTable
    public Set<String> getAlias() {
        return alias;
    }

    @Override
    @JsonSetter
    public void setAlias(Set<String> alias) {
        this.alias = alias;
        if (name != null) {
            this.alias.add(name);
        }
    }

    @Override
    @JsonGetter
    @Lob
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    @Column(name = "description")
    public @Nullable String getDescription() {
        return description;
    }

    @Override
    @JsonSetter
    public void setDescription(@Nullable String description) {
        this.description = description;
    }
}
