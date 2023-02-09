/*
 * LunarClientAPI
 * Copyright (c) 2022-2023 Moonsworth
 * Copyright (c) 2022-2023 powercas_gamer
 * Copyright (c) 2022-2023 contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.lunarclient.bukkitapi;

import com.lunarclient.bukkitapi.nethandler.client.LCPacketClientVoice;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketVoiceChannelSwitch;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketVoiceMute;
import com.lunarclient.bukkitapi.nethandler.server.LCNetHandlerServer;
import com.lunarclient.bukkitapi.nethandler.server.LCPacketStaffModStatus;
import com.lunarclient.bukkitapi.nethandler.shared.LCPacketEmoteBroadcast;
import com.lunarclient.bukkitapi.nethandler.shared.LCPacketWaypointAdd;
import com.lunarclient.bukkitapi.nethandler.shared.LCPacketWaypointRemove;

/**
 * An empty implementation of the server nethandler.
 */
public class LunarClientDefaultNetHandler implements LCNetHandlerServer {

    /**
     * Called when a player changes their staff mode state.
     * Not currently implemented.
     *
     * @param lcPacketStaffModStatus All the status of the players staff mode.
     */
    @Override
    public void handleStaffModStatus(final LCPacketStaffModStatus lcPacketStaffModStatus) {

    }

    /**
     * Called when the client adds or edits a waypoint when the server controls waypoints.
     * A waypoint that is edited will call handleRemoveWaypoint before creating the new waypoint.
     * See {@link com.lunarclient.bukkitapi.nethandler.client.obj.ServerRule} for how to control waypoints.
     *
     * @param lcPacketWaypointAdd The waypoint added by the client.
     */
    @Override
    public void handleAddWaypoint(final LCPacketWaypointAdd lcPacketWaypointAdd) {

    }

    /**
     * Called when a player removes a waypoint. See above (handleAddWaypoint) for more detail.
     *
     * @param lcPacketWaypointRemove The waypoint name and world removed by the player.
     */
    @Override
    public void handleRemoveWaypoint(final LCPacketWaypointRemove lcPacketWaypointRemove) {

    }

    // Do not implement these, they do nothing and will not work.


    /**
     * Deprecated entirely from a time when Emotes went through the server.
     */
    @Override
    @Deprecated
    public void handleEmote(final LCPacketEmoteBroadcast lcPacketEmoteBroadcast) {
        // DO NOT ATTEMPT TO IMPLEMENT
    }

    /**
     * Deprecated entirely, may return once a solution for voice is found.
     * <p>
     * Not currently a work in progress.
     */
    @Override
    @Deprecated
    public void handleVoice(final LCPacketClientVoice lcPacketClientVoice) {
        // DO NOT ATTEMPT TO IMPLEMENT
    }

    /**
     * Deprecated entirely, may return once a solution for voice is found.
     * <p>
     * Not currently a work in progress.
     */
    @Override
    @Deprecated
    public void handleVoiceMute(final LCPacketVoiceMute lcPacketVoiceMute) {
        // DO NOT ATTEMPT TO IMPLEMENT
    }

    /**
     * Deprecated entirely, may return once a solution for voice is found.
     * <p>
     * Not currently a work in progress.
     */
    @Override
    @Deprecated
    public void handleVoiceChannelSwitch(final LCPacketVoiceChannelSwitch lcPacketVoiceChannelSwitch) {
        // DO NOT ATTEMPT TO IMPLEMENT
    }
}
