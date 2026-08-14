/*
 * SysML v2 REST/HTTP Pilot Implementation
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

package services;

import com.fasterxml.jackson.databind.JsonNode;
import dao.SchemaDao;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Optional;

@Singleton
public class SchemaService {

    private final SchemaDao dao;

    @Inject
    public SchemaService(SchemaDao dao) {
        this.dao = dao;
    }

    public Optional<JsonNode> getById(String id) {
        return dao.findById(id);
    }

    public List<JsonNode> get(String after, String before, int maxResults) {
        return dao.findAll(after, before, maxResults);
    }
}
