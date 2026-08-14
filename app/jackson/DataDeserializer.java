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

package jackson;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import javassist.util.proxy.ProxyFactory;
import org.omg.sysml.lifecycle.Data;
import org.omg.sysml.metamodel.Element;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static jackson.RecordSerialization.IDENTITY_FIELD;
import static org.omg.sysml.metamodel.impl.ElementImpl_.QUALIFIED_NAME;

public class DataDeserializer extends StdDeserializer<Data> implements ContextualDeserializer {
    private static final Map<Class<?>, Class<?>> PROXY_MAP = new HashMap<>();
    private final EntityManager entityManager;
    private final JavaType type;

    public DataDeserializer(EntityManager entityManager) {
        this(entityManager, null);
    }

    public DataDeserializer(EntityManager entityManager, JavaType type) {
        super((Class<?>) null);
        this.entityManager = entityManager;
        this.type = type;
    }

    public DataDeserializer() {
        this(null);
    }

    @Override
    public Data deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        if (p.currentToken() != JsonToken.START_OBJECT) {
            throw new JsonParseException(p, "Expected START_OBJECT. Received " + p.getCurrentName() + ".");
        }

        Data data;
        // Proxy class to handle abstract classes
        Class<?> proxyClass = PROXY_MAP.computeIfAbsent(this.type.getRawClass(), clazz -> {
            ProxyFactory factory = new ProxyFactory();
            factory.setSuperclass(clazz);
            return factory.createClass();
        });
        try {
            data = (Data) proxyClass.getConstructor().newInstance();
        } catch (ReflectiveOperationException e) {
            throw new IOException(e);
        }

        JsonToken token;
        while ((token = p.nextToken()) != null && token != JsonToken.END_OBJECT) {
            if (token == JsonToken.FIELD_NAME) {
                String name = p.getCurrentName();
                p.nextToken();
                switch (name) {
                    case IDENTITY_FIELD:
                        data.setId(UUID.fromString(p.getText()));
                        break;
                    case QUALIFIED_NAME:
                        if (data instanceof Element) {
                            ((Element) data).setQualifiedName(p.getText());
                        }
                        break;
                }
            }
        }
        return data;
    }

    @Override
    public Object deserializeWithType(
            JsonParser p, DeserializationContext ctxt, TypeDeserializer typeDeserializer) throws IOException {
        return deserialize(p, ctxt);
    }

    @Override
    public JsonDeserializer<?> createContextual(DeserializationContext ctxt, BeanProperty property) {
        //beanProperty is null when the type to deserialize is the top-level type or a generic type, not a type of a bean property
        JavaType type = ctxt.getContextualType() != null ? ctxt.getContextualType() : property.getMember().getType();
        return new DataDeserializer(entityManager, type);
    }
}