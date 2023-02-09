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
import java.util.*;

public final class LCPacketNametagsUpdate extends LCPacket {

    private Map<UUID, List<String>> playersMap;

    public LCPacketNametagsUpdate() {
    }

    public LCPacketNametagsUpdate(final Map<UUID, List<String>> playersMap) {
        this.playersMap = playersMap;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeVarInt(this.playersMap == null ? -1 : this.playersMap.size());

        if (this.playersMap != null) {
            this.playersMap.forEach((uuid, tags) -> {
                buf.writeUUID(uuid);
                buf.writeVarInt(tags.size());

                for (final String tag : tags) {
                    buf.writeString(tag);
                }
            });
        }
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        final int playersMapSize = buf.readVarInt();

        if (playersMapSize != -1) {
            this.playersMap = new HashMap<>();

            for (int i = 0; i < playersMapSize; i++) {
                final UUID uuid = buf.readUUID();
                final int tagsSize = buf.readVarInt();
                final List<String> tags = new ArrayList<>(tagsSize);

                for (int j = 0; j < tagsSize; j++) {
                    tags.add(buf.readString());
                }

                this.playersMap.put(uuid, tags);
            }
        }
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleNametagsUpdate(this);
    }

    public Map<UUID, List<String>> getPlayersMap() {
        return this.playersMap;
    }
}
