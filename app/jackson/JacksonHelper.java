/*
 * SysML v2 REST/HTTP Pilot Implementation
 * Copyright (C) 2020 InterCAX LLC
 * Copyright (C) 2020 California Institute of Technology ("Caltech")
 * Copyright (C) 2022 Twingineer LLC
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

import com.fasterxml.jackson.databind.*;
import play.libs.Json;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class JacksonHelper {
    // Java type erasure necessitates explicitly specifying JavaType for Collections. See https://github.com/FasterXML/jackson-databind/issues/23#issuecomment-6251193.
    public static JsonNode collectionToTree(Collection<?> collection, @SuppressWarnings("rawtypes") Class<? extends Collection> collectionClass, Class<?> elementClass) {
        return collectionToTree(collection, collectionClass, elementClass, Json.mapper());
    }

    public static JsonNode collectionToTree(Collection<?> collection, @SuppressWarnings("rawtypes") Class<? extends Collection> collectionClass, Class<?> elementClass, ObjectMapper mapper) {
        return collectionToTree(collection, collectionClass, elementClass, mapper, UnaryOperator.identity());
    }

    public static JsonNode collectionToTree(Collection<?> collection, @SuppressWarnings("rawtypes") Class<? extends Collection> collectionClass, Class<?> elementClass, ObjectMapper mapper, UnaryOperator<ObjectWriter> writerOperator) {
        return collectionToTree(collection, collectionClass, elementClass, mapper, writerOperator, UnaryOperator.identity());
    }

    public static JsonNode collectionToTree(Collection<?> collection, @SuppressWarnings("rawtypes") Class<? extends Collection> collectionClass, Class<?> elementClass, ObjectMapper mapper, UnaryOperator<ObjectWriter> writerOperator, UnaryOperator<ObjectReader> readerOperator) {
        JavaType javaType = collectionType(collectionClass, elementClass, mapper);
        return objectToTree(collection, mapper, om -> writerOperator.apply(om.writerFor(javaType)), om -> readerOperator.apply(om.readerFor(javaType)));
    }

    public static JavaType collectionType(@SuppressWarnings("rawtypes") Class<? extends Collection> collectionClass, Class<?> elementClass, ObjectMapper mapper) {
        return mapper.getTypeFactory().constructCollectionType(collectionClass, elementClass);
    }

    public static ObjectWriter writer(Object o, ObjectMapper mapper, UnaryOperator<ObjectWriter> writerOperator) {
        return writerOperator.apply(mapper.writerFor(o.getClass()));
    }

    public static ObjectReader reader(Object o, ObjectMapper mapper, UnaryOperator<ObjectReader> readerOperator) {
        return readerOperator.apply(mapper.readerFor(o.getClass()));
    }

    public static JsonNode objectToTree(Object o, ObjectMapper mapper) {
        ObjectReader reader = mapper.readerFor(o.getClass());
        ObjectWriter writer = mapper.writerFor(o.getClass());
        return objectToTree(o, reader, writer);
    }

    public static JsonNode objectToTree(Object o, ObjectMapper mapper, Function<ObjectMapper, ObjectWriter> writerFunction, Function<ObjectMapper, ObjectReader> readerFunction) {
        ObjectReader reader = readerFunction.apply(mapper);
        ObjectWriter writer = writerFunction.apply(mapper);
        return objectToTree(o, reader, writer);
    }

    private static JsonNode objectToTree(Object o, ObjectReader reader, ObjectWriter writer) throws UncheckedIOException {
        try {
            return reader.readTree(writer.writeValueAsString(o));
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
