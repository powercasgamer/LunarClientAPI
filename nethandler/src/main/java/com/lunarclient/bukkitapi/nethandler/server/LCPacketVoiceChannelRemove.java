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
package com.lunarclient.bukkitapi.nethandler.server;

import com.lunarclient.bukkitapi.nethandler.ByteBufWrapper;
import com.lunarclient.bukkitapi.nethandler.LCPacket;
import com.lunarclient.bukkitapi.nethandler.client.LCNetHandlerClient;
import com.lunarclient.bukkitapi.nethandler.shared.LCNetHandler;

import java.util.UUID;

public final class LCPacketVoiceChannelRemove extends LCPacket {

    private UUID uuid;

    public LCPacketVoiceChannelRemove(final UUID uuid) {
        this.uuid = uuid;
    }

    public LCPacketVoiceChannelRemove() {
    }

    @Override
    public void write(final ByteBufWrapper b) {
        b.writeUUID(this.uuid);
    }

    @Override
    public void read(final ByteBufWrapper b) {
        this.uuid = b.readUUID();
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleVoiceChannelDelete(this);
    }

    public UUID getUuid() {
        return this.uuid;
    }
}
