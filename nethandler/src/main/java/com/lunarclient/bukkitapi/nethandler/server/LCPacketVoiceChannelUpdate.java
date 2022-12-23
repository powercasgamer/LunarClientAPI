/*
 * LunarClientAPI
 * Copyright (c) 2022 Moonsworth
 * Copyright (c) 2022 powercas_gamer
 * Copyright (c) 2022 contributors
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
package com.lunarclient.bukkitapi.nethandler.server;

import com.lunarclient.bukkitapi.nethandler.ByteBufWrapper;
import com.lunarclient.bukkitapi.nethandler.LCPacket;
import com.lunarclient.bukkitapi.nethandler.client.LCNetHandlerClient;
import com.lunarclient.bukkitapi.nethandler.shared.LCNetHandler;

import java.util.UUID;

public final class LCPacketVoiceChannelUpdate extends LCPacket {

    /**
     * 0 for adding a player
     * 1 for removing a player
     * 2 for marking a player as listening
     * 3 for marking a player as deafened
     */
    public int status;

    private UUID channelUuid;

    private UUID uuid;

    private String name;

    public LCPacketVoiceChannelUpdate(final int status, final UUID channelUuid, final UUID uuid, final String name) {
        this.status = status;
        this.channelUuid = channelUuid;
        this.uuid = uuid;
        this.name = name;
    }

    public LCPacketVoiceChannelUpdate() {
    }

    @Override
    public void write(final ByteBufWrapper b) {
        b.writeVarInt(this.status);
        b.writeUUID(this.channelUuid);
        b.writeUUID(this.uuid);
        b.writeString(this.name);
    }

    @Override
    public void read(final ByteBufWrapper b) {
        this.status = b.readVarInt();
        this.channelUuid = b.readUUID();
        this.uuid = b.readUUID();
        this.name = b.readString();
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleVoiceChannelUpdate(this);
    }

    public int getStatus() {
        return this.status;
    }

    public UUID getChannelUuid() {
        return this.channelUuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }
}
