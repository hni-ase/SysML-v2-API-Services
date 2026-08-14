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

package jackson.jsonld;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.function.UnaryOperator;

public final class JsonLdNode<N> {

    private final JsonNode context;
    private final N entity;
    private final String type;
    private final UnaryOperator<JsonNode> postProcessor;

    public JsonLdNode(JsonNode context, N entity, String type, UnaryOperator<JsonNode> postProcessor) {
        this.context = context;
        this.entity = entity;
        this.type = type;
        this.postProcessor = postProcessor;
    }

    @JsonProperty("@context")
    public JsonNode getContext() {
        return context;
    }

    @JsonUnwrapped
    // JsonTypeInfo doesn't work in conjunction with JsonUnwrapped. See SerializationFeature#FAIL_ON_UNWRAPPED_TYPE_IDENTIFIERS.
    // @JsonTypeInfo(use = JsonTypeInfo.Id.NAME)
    public N getEntity() {
        return entity;
    }

    @JsonProperty("@type")
    public String getType() {
        return type;
    }

    public JsonNode postProcess(JsonNode json) {
        return postProcessor.apply(json);
    }
}
