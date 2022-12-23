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

public final class LCPacketStaffModState extends LCPacket {

    private String mod;
    private boolean state;

    public LCPacketStaffModState() {
    }

    public LCPacketStaffModState(final String mod, final boolean state) {
        this.mod = mod;
        this.state = state;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeString(this.mod);
        buf.buf().writeBoolean(this.state);
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        this.mod = buf.readString();
        this.state = buf.buf().readBoolean();
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleStaffModState(this);
    }

    public String getMod() {
        return this.mod;
    }

    public boolean isState() {
        return this.state;
    }
}
