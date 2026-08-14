/*
 * SysML v2 REST/HTTP Pilot Implementation
 * Copyright (C) 2020  InterCAX LLC
 * Copyright (C) 2020  California Institute of Technology ("Caltech")
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

package org.omg.sysml.query.impl;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.ManyToAny;
import org.omg.sysml.query.CompositeConstraint;
import org.omg.sysml.query.CompositeOperator;
import org.omg.sysml.query.Constraint;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

// @Embeddable // Can't embed and have polymorphism between Constraints, which is necessary for CompositeConstraint.constraint
@Entity(name = "CompositeConstraint")
@DiscriminatorValue(value = "CompositeConstraint")
@JsonTypeName(value = "CompositeConstraint")
public class CompositeConstraintImpl extends ConstraintImpl implements CompositeConstraint {
    private Collection<Constraint> constraint;
    private CompositeOperator operator;

    @Override
    @ManyToAny(metaDef = "ConstraintMetaDef", metaColumn = @javax.persistence.Column(name = "attributeType"), fetch = FetchType.EAGER)
    @JoinTable(name = "CompositeConstraint_constraint",
            joinColumns = @JoinColumn(name = "classId"),
            inverseJoinColumns = @JoinColumn(name = "attributeId"))
    @Cascade({org.hibernate.annotations.CascadeType.ALL})
    public Collection<Constraint> getConstraint() {
        if (constraint == null) {
            return new ArrayList<>();
        }
        return constraint;
    }

    @Override
    @JsonDeserialize(contentAs = ConstraintImpl.class)
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    public void setConstraint(Collection<Constraint> constraint) {
        this.constraint = constraint;
    }

    @Override
    @Enumerated(EnumType.STRING)
    public CompositeOperator getOperator() {
        if (operator == null) {
            operator = CompositeOperator.AND;
        }
        return operator;
    }

    @Override
    public void setOperator(CompositeOperator operator) {
        this.operator = operator;
    }
}
