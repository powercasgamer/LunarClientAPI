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

public final class LCPacketHologramUpdate extends LCPacket {

    private UUID uuid;
    private List<String> lines;

    public LCPacketHologramUpdate() {
    }

    public LCPacketHologramUpdate(final UUID uuid, final List<String> lines) {
        this.uuid = uuid;
        this.lines = lines;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeUUID(this.uuid);
        buf.writeVarInt(this.lines.size());

        for (final String s : this.lines) {
            buf.writeString(s);
        }
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        this.uuid = buf.readUUID();
        final int linesSize = buf.readVarInt();
        this.lines = new ArrayList<>(linesSize);

        for (int i = 0; i < linesSize; i++) {
            this.lines.add(buf.readString());
        }
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleUpdateHologram(this);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public List<String> getLines() {
        return this.lines;
    }
}
