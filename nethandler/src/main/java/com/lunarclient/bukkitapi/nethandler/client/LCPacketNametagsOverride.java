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
package com.lunarclient.bukkitapi.nethandler.client;

import com.lunarclient.bukkitapi.nethandler.ByteBufWrapper;
import com.lunarclient.bukkitapi.nethandler.LCPacket;
import com.lunarclient.bukkitapi.nethandler.shared.LCNetHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class LCPacketNametagsOverride extends LCPacket {

    private UUID player;
    private List<String> tags;

    public LCPacketNametagsOverride() {
    }

    public LCPacketNametagsOverride(final UUID player, final List<String> tags) {
        this.player = player;
        this.tags = tags;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeUUID(this.player);
        buf.buf().writeBoolean(this.tags != null);

        if (this.tags != null) {
            buf.writeVarInt(this.tags.size());

            for (final String tag : this.tags) {
                buf.writeString(tag);
            }
        }
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        this.player = buf.readUUID();

        if (buf.buf().readBoolean()) {
            final int tagsSize = buf.readVarInt();
            this.tags = new ArrayList<>(tagsSize);

            for (int i = 0; i < tagsSize; i++) {
                this.tags.add(buf.readString());
            }
        }
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleOverrideNametags(this);
    }

    public UUID getPlayer() {
        return this.player;
    }

    public List<String> getTags() {
        return this.tags;
    }
}
