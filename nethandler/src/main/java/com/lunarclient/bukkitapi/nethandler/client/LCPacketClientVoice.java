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
import com.lunarclient.bukkitapi.nethandler.server.LCNetHandlerServer;
import com.lunarclient.bukkitapi.nethandler.shared.LCNetHandler;

import java.io.IOException;

public final class LCPacketClientVoice extends LCPacket {

    private byte[] data;

    public LCPacketClientVoice() {
    }

    public LCPacketClientVoice(final byte[] data) {
        this.data = data;
    }

    @Override
    public void write(final ByteBufWrapper b) throws IOException {
        writeBlob(b, this.data);
    }

    @Override
    public void read(final ByteBufWrapper b) throws IOException {
        this.data = readBlob(b);
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerServer) handler).handleVoice(this);
    }

    public byte[] getData() {
        return this.data;
    }
}
