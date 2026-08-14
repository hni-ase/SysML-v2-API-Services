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

import com.fasterxml.jackson.core.JsonParser;
import org.omg.sysml.lifecycle.impl.BranchImpl;
import org.omg.sysml.lifecycle.impl.CommitImpl;
import org.omg.sysml.lifecycle.impl.ProjectImpl;
import org.omg.sysml.record.Record;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class RecordSerialization {

    public static final String IDENTITY_FIELD = "@id";

    public static class RecordSerializer extends JpaIdentitySerializer<Record> {

        @Override
        protected String getIdentityField() {
            return IDENTITY_FIELD;
        }

        @Override
        protected Object serializeToIdentity(Record record) {
            return record != null ? record.getId() : null;
        }
    }

    public static abstract class RecordDeserializer<R extends Record> extends JpaIdentityDeserializer<R> {
        public RecordDeserializer(EntityManager entityManager) {
            super(entityManager);
        }

        public RecordDeserializer() {
            super();
        }

        protected abstract Class<R> getRecordClass();

        @Override
        protected boolean isIdentityField(String field) {
            return IDENTITY_FIELD.equals(field);
        }

        @Override
        protected R deserializeFromIdentity(JsonParser parser) throws IOException {
            UUID id = UUID.fromString(parser.getText());
            R record = getEntityManager().find(getRecordClass(), id);
            if (record == null) {
                throw new IOException(new EntityNotFoundException("Record " + id + " not found."));
            }
            return record;
        }
    }

    public static class CommitDeserializer extends RecordDeserializer<CommitImpl> {
        public CommitDeserializer(EntityManager entityManager) {
            super(entityManager);
        }

        public CommitDeserializer() {
            super();
        }

        @Override
        protected Class<CommitImpl> getRecordClass() {
            return CommitImpl.class;
        }
    }

    public static class ProjectDeserializer extends RecordDeserializer<ProjectImpl> {
        public ProjectDeserializer(EntityManager entityManager) {
            super(entityManager);
        }

        public ProjectDeserializer() {
            super();
        }

        @Override
        protected Class<ProjectImpl> getRecordClass() {
            return ProjectImpl.class;
        }
    }

    public static class BranchDeserializer extends RecordDeserializer<BranchImpl> {
        public BranchDeserializer(EntityManager entityManager) {
            super(entityManager);
        }

        public BranchDeserializer() {
            super();
        }

        @Override
        protected Class<BranchImpl> getRecordClass() {
            return BranchImpl.class;
        }
    }
}
