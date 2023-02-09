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
package com.lunarclient.bukkitapi.nethandler.client;

import com.lunarclient.bukkitapi.nethandler.ByteBufWrapper;
import com.lunarclient.bukkitapi.nethandler.LCPacket;
import com.lunarclient.bukkitapi.nethandler.shared.LCNetHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class LCPacketTeammates extends LCPacket {

    private UUID leader;
    private long lastMs;
    private Map<UUID, Map<String, Double>> players;

    public LCPacketTeammates() {
    }

    public LCPacketTeammates(final UUID leader, final long lastMs, final Map<UUID, Map<String, Double>> players) {
        this.leader = leader;
        this.lastMs = lastMs;
        this.players = players;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.buf().writeBoolean(this.leader != null);

        if (this.leader != null) {
            buf.writeUUID(this.leader);
        }

        buf.buf().writeLong(this.lastMs);
        buf.writeVarInt(this.players.size());

        this.players.forEach((uuid, posMap) -> {
            buf.writeUUID(uuid);
            buf.writeVarInt(posMap.size());

            posMap.forEach((key, val) -> {
                buf.writeString(key);
                buf.buf().writeDouble(val);
            });
        });
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        if (buf.buf().readBoolean()) {
            this.leader = buf.readUUID();
        }

        this.lastMs = buf.buf().readLong();

        final int playersSize = buf.readVarInt();
        this.players = new HashMap<>();

        for (int i = 0; i < playersSize; i++) {
            final UUID uuid = buf.readUUID();
            final int posMapSize = buf.readVarInt();
            final Map<String, Double> posMap = new HashMap<>();

            for (int j = 0; j < posMapSize; j++) {
                final String key = buf.readString();
                final double val = buf.buf().readDouble();

                posMap.put(key, val);
            }

            this.players.put(uuid, posMap);
        }
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleTeammates(this);
    }

    public UUID getLeader() {
        return this.leader;
    }

    public long getLastMs() {
        return this.lastMs;
    }

    public Map<UUID, Map<String, Double>> getPlayers() {
        return this.players;
    }
}
