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
package com.lunarclient.bukkitapi.nethandler.shared;

import com.lunarclient.bukkitapi.nethandler.ByteBufWrapper;
import com.lunarclient.bukkitapi.nethandler.LCPacket;

import java.io.IOException;
import java.util.UUID;

public final class LCPacketEmoteBroadcast extends LCPacket {

    private UUID uuid; // User doing the emote
    private int emoteId;

    public LCPacketEmoteBroadcast() {
    }

    public LCPacketEmoteBroadcast(final UUID uuid, final int emoteId) {
        this.uuid = uuid;
        this.emoteId = emoteId;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeUUID(this.uuid);
        buf.buf().writeInt(this.emoteId);
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        this.uuid = buf.readUUID();
        this.emoteId = buf.buf().readInt();
    }

    @Override
    public void process(final LCNetHandler handler) {
        handler.handleEmote(this);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public int getEmoteId() {
        return this.emoteId;
    }
}
