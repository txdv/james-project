/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mailbox.cassandra;

import org.apache.james.backends.cassandra.CassandraClusterExtension;
import org.apache.james.backends.cassandra.components.CassandraModule;
import org.apache.james.backends.cassandra.init.configuration.CassandraConfiguration;
import org.apache.james.blob.api.BlobStore;
import org.apache.james.mailbox.SubscriptionManager;
import org.apache.james.mailbox.SubscriptionManagerContract;
import org.apache.james.mailbox.cassandra.mail.CassandraACLMapper;
import org.apache.james.mailbox.cassandra.mail.CassandraApplicableFlagDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraAttachmentDAOV2;
import org.apache.james.mailbox.cassandra.mail.CassandraAttachmentMessageIdDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraDeletedMessageDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraFirstUnseenDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraMailboxCounterDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraMailboxDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraMailboxPathV3DAO;
import org.apache.james.mailbox.cassandra.mail.CassandraMailboxRecentsDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraMessageDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraMessageDAOV3;
import org.apache.james.mailbox.cassandra.mail.CassandraMessageIdDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraMessageIdToImapUidDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraModSeqProvider;
import org.apache.james.mailbox.cassandra.mail.CassandraThreadDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraThreadLookupDAO;
import org.apache.james.mailbox.cassandra.mail.CassandraUidProvider;
import org.apache.james.mailbox.cassandra.mail.CassandraUserMailboxRightsDAO;
import org.apache.james.mailbox.cassandra.mail.task.RecomputeMailboxCountersService;
import org.apache.james.mailbox.cassandra.modules.CassandraAnnotationModule;
import org.apache.james.mailbox.cassandra.modules.CassandraSubscriptionModule;
import org.apache.james.mailbox.store.BatchSizes;
import org.apache.james.mailbox.store.StoreSubscriptionManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.RegisterExtension;

/**
 * Test Cassandra subscription against some general purpose written code.
 */
class CassandraSubscriptionManagerTest implements SubscriptionManagerContract {

    @RegisterExtension
    static CassandraClusterExtension cassandraCluster = new CassandraClusterExtension(CassandraModule.aggregateModules(
        CassandraSubscriptionModule.MODULE,
        CassandraAnnotationModule.MODULE));

    private SubscriptionManager subscriptionManager;

    @Override
    public SubscriptionManager getSubscriptionManager() {
        return subscriptionManager;
    }

    @BeforeEach
    void setUp() {
        CassandraThreadDAO threadDAO = null;
        CassandraThreadLookupDAO threadLookupDAO = null;
        CassandraMessageIdToImapUidDAO imapUidDAO = null;
        CassandraMessageDAO messageDAO = null;
        CassandraMessageDAOV3 messageDAOV3 = null;
        CassandraMessageIdDAO messageIdDAO = null;
        CassandraMailboxCounterDAO mailboxCounterDAO = null;
        CassandraMailboxRecentsDAO mailboxRecentsDAO = null;
        CassandraMailboxDAO mailboxDAO = null;
        CassandraMailboxPathV3DAO mailboxPathV3DAO = null;
        CassandraFirstUnseenDAO firstUnseenDAO = null;
        CassandraApplicableFlagDAO applicableFlagDAO = null;
        CassandraDeletedMessageDAO deletedMessageDAO = null;
        CassandraAttachmentDAOV2 attachmentDAOV2 = null;
        CassandraAttachmentMessageIdDAO attachmentMessageIdDAO = null;
        CassandraACLMapper aclMapper = null;
        CassandraUserMailboxRightsDAO userMailboxRightsDAO = null;
        BlobStore blobStore = null;
        CassandraUidProvider uidProvider = null;
        CassandraModSeqProvider modSeqProvider = null;
        RecomputeMailboxCountersService recomputeMailboxCountersService = null;

        subscriptionManager = new StoreSubscriptionManager(
            new CassandraMailboxSessionMapperFactory(
                uidProvider,
                modSeqProvider,
                cassandraCluster.getCassandraCluster().getConf(),
                threadDAO,
                threadLookupDAO,
                messageDAO,
                messageDAOV3,
                messageIdDAO,
                imapUidDAO,
                mailboxCounterDAO,
                mailboxRecentsDAO,
                mailboxDAO,
                mailboxPathV3DAO,
                firstUnseenDAO,
                applicableFlagDAO,
                attachmentDAOV2,
                deletedMessageDAO,
                blobStore,
                attachmentMessageIdDAO,
                aclMapper,
                userMailboxRightsDAO,
                recomputeMailboxCountersService,
                CassandraConfiguration.DEFAULT_CONFIGURATION,
                BatchSizes.defaultValues()));
    }
}
