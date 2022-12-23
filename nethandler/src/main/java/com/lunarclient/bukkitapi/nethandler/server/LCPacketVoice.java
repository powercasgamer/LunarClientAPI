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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public final class LCPacketVoice extends LCPacket {
    /**
     * The UUID of all the senders, since we're using multiplex
     * we don't actually know the bytes per user
     */
    private Set<UUID> uuids;

    /**
     * The "smashed" bytes of all the voice.
     */
    private byte[] data;

    public LCPacketVoice(final Set<UUID> uuids, final byte[] data) {
        this.uuids = uuids;
        this.data = data;
    }

    public LCPacketVoice() {
    }

    @Override
    public void write(final ByteBufWrapper b) {
        b.writeVarInt(this.uuids.size());
        this.uuids.forEach(b::writeUUID);
        writeBlob(b, this.data);
    }

    @Override
    public void read(final ByteBufWrapper b) {
        this.uuids = new HashSet<>();
        final int size = b.readVarInt();
        for (int i = 0; i < size; i++) {
            this.uuids.add(b.readUUID());
        }
        this.data = readBlob(b);
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleVoice(this);
    }

    /**
     * For comparability, will be removed.
     *
     * @return The 0 index of the sent players.
     */
    @Deprecated
    public UUID getUuid() {
        return new ArrayList<>(this.uuids).get(0);
    }

    public Set<UUID> getUuids() {
        return this.uuids;
    }

    public byte[] getData() {
        return this.data;
    }
}
