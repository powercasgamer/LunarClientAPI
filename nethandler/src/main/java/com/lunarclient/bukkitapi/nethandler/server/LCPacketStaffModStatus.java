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
import com.lunarclient.bukkitapi.nethandler.shared.LCNetHandler;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public final class LCPacketStaffModStatus extends LCPacket {

    private final Set<String> enabled;

    public LCPacketStaffModStatus() {
        this.enabled = new HashSet<>();
    }

    public LCPacketStaffModStatus(final Set<String> enabled) {
        this.enabled = enabled;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeVarInt(this.enabled.size());

        for (final String mod : this.enabled) {
            buf.writeString(mod);
        }
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        final int size = buf.readVarInt();

        for (int i = 0; i < size; i++) {
            this.enabled.add(buf.readString());
        }
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerServer) handler).handleStaffModStatus(this);
    }

    public Set<String> getEnabled() {
        return this.enabled;
    }
}
