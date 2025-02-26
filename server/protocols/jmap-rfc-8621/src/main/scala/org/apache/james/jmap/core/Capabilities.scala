/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 * http://www.apache.org/licenses/LICENSE-2.0                   *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/
package org.apache.james.jmap.core

import com.google.common.annotations.VisibleForTesting
import org.apache.james.jmap.core.CapabilityIdentifier.CapabilityIdentifier

object DefaultCapabilities {
  @VisibleForTesting
  def supported(configuration: JmapRfc8621Configuration): Set[CapabilityFactory] = Set(
    CoreCapabilityFactory(configuration.maxUploadSize),
    MailCapabilityFactory,
    QuotaCapabilityFactory,
    JmapQuotaCapabilityFactory,
    IdentitySortOrderCapabilityFactory,
    SharesCapabilityFactory,
    VacationResponseCapabilityFactory,
    SharesCapabilityFactory,
    SubmissionCapabilityFactory,
    WebSocketCapabilityFactory)
}

object Capabilities {
  def of(capabilities: Capability*): Capabilities = Capabilities(capabilities.toSet)
}

case class Capabilities(capabilities: Set[Capability]) {
  def ids: Set[CapabilityIdentifier] = capabilities.map(_.identifier())
}
