/*
 * Copyright (C) 2016 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.syndesis.model.integration;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.immutables.value.Value;

import java.util.List;
import java.util.Optional;

@Value.Immutable
@JsonDeserialize(builder = OldIntegrationStatus.Builder.class)
public interface OldIntegrationStatus {

    /**
     * The desired state of the revision.
     * @return
     */
    IntegrationRevisionState getTargetState();

    /**
     * The current state of the revision.
     * @return
     */
    IntegrationRevisionState getCurrentState();

    /**
     * Message describing the currentState further (e.g. error message)
     * @return
     */
    Optional<String> getCurrentMessage();


    /**
     * Message which should become the currentMessage after reconciliation
     * @return
     */
    Optional<String> getTargetMessage();


    class Builder extends ImmutableOldIntegrationStatus.Builder {
        // allow access to ImmutableIntegrationStatus.Builder
    }
}
