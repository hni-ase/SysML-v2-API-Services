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

package jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import javax.persistence.EntityManager;
import java.io.IOException;

public abstract class JpaIdentityDeserializer<T> extends StdDeserializer<T> {
    private EntityManager entityManager;

    public JpaIdentityDeserializer(EntityManager entityManager) {
        super((Class<?>) null);
        this.entityManager = entityManager;
    }

    public JpaIdentityDeserializer() {
        this(null);
    }

    protected abstract boolean isIdentityField(String field);

    protected abstract T deserializeFromIdentity(JsonParser parser) throws IOException;

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() != JsonToken.START_OBJECT) {
            throw new JsonParseException(p, "Expected START_OBJECT. Received " + p.getCurrentName() + ".");
        }
        T t = null;
        JsonToken token;
        while ((token = p.nextToken()) != null && token != JsonToken.END_OBJECT) {
            if (t == null && token == JsonToken.FIELD_NAME && isIdentityField(p.getCurrentName())) {
                token = p.nextToken();
                if (token == null) {
                    throw new JsonParseException(p, "No value for identity field.");
                }
                t = deserializeFromIdentity(p);
            }
        }
        return t;
    }

    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(p, ctxt);
    }

    public EntityManager getEntityManager() {
        return entityManager;
    }
}