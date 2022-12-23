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

public final class LCPacketCooldown extends LCPacket {

    private String message;
    private long durationMs;
    private int iconId;

    public LCPacketCooldown() {
    }

    public LCPacketCooldown(final String message, final long durationMs, final int iconId) {
        this.message = message;
        this.durationMs = durationMs;
        this.iconId = iconId;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeString(this.message);
        buf.buf().writeLong(this.durationMs);
        buf.buf().writeInt(this.iconId);
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        this.message = buf.readString();
        this.durationMs = buf.buf().readLong();
        this.iconId = buf.buf().readInt();
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleCooldown(this);
    }

    public String getMessage() {
        return this.message;
    }

    public long getDurationMs() {
        return this.durationMs;
    }

    public int getIconId() {
        return this.iconId;
    }
}
