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

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import io.syndesis.model.Kind;
import io.syndesis.model.ResourceIdentifier;
import io.syndesis.model.WithId;
import io.syndesis.model.WithName;
import io.syndesis.model.WithTags;
import io.syndesis.model.action.ConnectorAction;
import io.syndesis.model.connection.Connection;
import io.syndesis.model.user.User;
import io.syndesis.model.validation.UniqueProperty;
import io.syndesis.model.validation.UniquenessRequired;

import org.immutables.value.Value;

@Value.Immutable
@JsonDeserialize(builder = Integration.Builder.class)
@UniqueProperty(value = "name", groups = UniquenessRequired.class)
@SuppressWarnings("immutables")
public interface Integration extends WithId<Integration>, WithTags, WithName, Serializable {

    @Override
    default Kind getKind() {
        return Kind.Integration;
    }

    /**
     * The list of versioned revisions.
     * The items in this list should be versioned and are not meant to be mutated.
     * @return
     */
    Set<IntegrationRevision> getRevisions();

    Optional<IntegrationRevision> getDraftRevision();

    Optional<Integer> getDeployedRevisionId();

    Optional<String> getUserId();

    List<User> getUsers();


    @Deprecated
    Optional<String> getIntegrationTemplateId();

    @Deprecated
    Optional<String> getConfiguration();

    @Deprecated
    @Value.Default
    default List<Connection> getConnections() {
        return Collections.emptyList();
    }

    @Deprecated
    @Value.Default
    default List<? extends Step> getSteps() {
        return Collections.emptyList();
    }

    @Deprecated
    @Value.Default
    default List<ResourceIdentifier> getResources() {
        return Collections.emptyList();
    }

    @Deprecated
    Optional<String> getDescription();

    @Deprecated
    Optional<IntegrationRevisionState> getDesiredStatus();

    @Deprecated
    Optional<IntegrationRevisionState> getCurrentStatus();

    @Deprecated
    @Value.Default
    default List<String> getStepsDone() {
        return Collections.emptyList();
    }

    @Deprecated
    Optional<String> getStatusMessage();

    @Deprecated
    Optional<Date> getLastUpdated();

    @Deprecated
    Optional<Date> getCreatedDate();

    @Deprecated
    Optional<BigInteger> getTimesUsed();


    @Deprecated
    @Value.Derived
    default boolean isInactive() {
        return getCurrentStatus()
            .map(s -> s == IntegrationRevisionState.Undeployed || s == IntegrationRevisionState.Inactive)
            .orElse(getDesiredStatus().map(s -> s == IntegrationRevisionState.Undeployed || s == IntegrationRevisionState.Inactive)
                .orElse(false));
    }

    @Deprecated
    @Value.Derived
    default Set<String> getUsedConnectorIds() {
        return getSteps().stream()//
            .map(s -> s.getAction())//
            .filter(Optional::isPresent)//
            .map(Optional::get)//
            .filter(ConnectorAction.class::isInstance)//
            .map(ConnectorAction.class::cast)//
            .map(a -> a.getDescriptor().getConnectorId())//
            .filter(Objects::nonNull)//
            .collect(Collectors.toSet());
    }


    default IntegrationRevision lastRevision() {
        return getRevisions().stream().max(Comparator.comparingInt(r -> r.getVersion().orElse(0))).get();
    }

    class Builder extends ImmutableIntegration.Builder {
        // allow access to ImmutableIntegration.Builder
    }

}
