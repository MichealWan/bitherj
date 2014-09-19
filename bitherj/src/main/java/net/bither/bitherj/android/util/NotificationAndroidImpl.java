/*
* Copyright 2014 http://Bither.net
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*    http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package net.bither.bitherj.android.util;

import android.content.Intent;

import net.bither.bitherj.BitherjApplication;
import net.bither.bitherj.core.Address;
import net.bither.bitherj.core.NotificationService;
import net.bither.bitherj.core.Tx;
import net.bither.bitherj.utils.Utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationAndroidImpl implements NotificationService {
    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    public static final String ACTION_SYNC_FROM_SPV_FINISHED = "net.bither.bitherj.SPVFinishedNotification";
    public static final String ACTION_SYNC_LAST_BLOCK_CHANGE = "net.bither.bitherj.LastBlockChangedNotification";
    public static final String ACTION_ADDRESS_BALANCE = "net.bither.bitherj.balance";
    public static final String ACTION_PEER_STATE = "net.bither.bitherj.peer_state";
    public static final String ACTION_ADDRESS_LOAD_COMPLETE_STATE = "net.bither.bitherj.load_complete";

    public static final String ACTION_PEER_STATE_NUM_PEERS = "num_peers";

    public static final String MESSAGE_DELTA_BALANCE = "delta_balance";
    public static final String MESSAGE_ADDRESS = "address";
    public static final String MESSAGE_TX = "tx";
    public static final String MESSAGE_TX_NOTIFICATION_TYPE = "tx_notification_type";

    @Override
    public void sendBroadcastSyncSPVFinished(boolean isFinished) {
        if (isFinished) {
            BitherjApplication.setting.setBitherjDoneSyncFromSpv(isFinished);
            final Intent broadcast = new Intent(ACTION_SYNC_FROM_SPV_FINISHED);
            BitherjApplication.mContext.sendStickyBroadcast(broadcast);
        }
    }

    @Override
    public void removeBroadcastSyncSPVFinished() {
        BitherjApplication.mContext.removeStickyBroadcast(new Intent(
                ACTION_SYNC_FROM_SPV_FINISHED));
    }

    @Override
    public void sendLastBlockChange() {
        Intent broadcast = new Intent(ACTION_SYNC_LAST_BLOCK_CHANGE);
        BitherjApplication.mContext.sendBroadcast(broadcast);
    }

    @Override
    public void notificatTx(Address address, Tx tx, Tx.TxNotificationType txNotificationType, long deltaBalance) {
        final Intent broadcast = new Intent(ACTION_ADDRESS_BALANCE);
        broadcast.putExtra(MESSAGE_ADDRESS, address.getAddress());
        broadcast.putExtra(MESSAGE_DELTA_BALANCE, deltaBalance);
        if (tx != null) {
            broadcast.putExtra(MESSAGE_TX, tx.getTxHash());
        }
        broadcast.putExtra(MESSAGE_TX_NOTIFICATION_TYPE, txNotificationType.getValue());
        BitherjApplication.mContext.sendBroadcast(broadcast);
        log.debug("address " + address.getAddress()
                + " balance updated " + deltaBalance
                + (tx != null ? " tx " + Utils.hashToString(tx.getTxHash()) : "")
                + " type:" + txNotificationType.getValue());

    }

    @Override
    public void sendBroadcastPeerState(final int numPeers) {
        final Intent broadcast = new Intent(ACTION_PEER_STATE);

        broadcast.putExtra(ACTION_PEER_STATE_NUM_PEERS, numPeers);
        BitherjApplication.mContext.sendStickyBroadcast(broadcast);
    }

    @Override
    public void removeBroadcastPeerState() {
        BitherjApplication.mContext.removeStickyBroadcast(new Intent(
                ACTION_PEER_STATE));
    }

    @Override
    public void sendBroadcastAddressLoadCompleteState() {
        final Intent broadcast = new Intent(ACTION_ADDRESS_LOAD_COMPLETE_STATE);
        BitherjApplication.mContext.sendStickyBroadcast(broadcast);
    }

    @Override
    public void removeAddressLoadCompleteState() {
        BitherjApplication.mContext.removeStickyBroadcast(new Intent(ACTION_ADDRESS_LOAD_COMPLETE_STATE));
    }

    @Override
    public void sendConnectedChangeBroadcast(String connectedChangeBroadcast, boolean isConnected) {
        Intent intent = new Intent(connectedChangeBroadcast);
        intent.putExtra(connectedChangeBroadcast, isConnected);
        BitherjApplication.mContext.sendBroadcast(intent);
    }
}
