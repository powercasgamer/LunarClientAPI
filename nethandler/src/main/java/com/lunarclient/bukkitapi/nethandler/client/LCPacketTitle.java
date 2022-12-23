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

public final class LCPacketTitle extends LCPacket {

    private String type;
    private String message;
    private float scale;
    private long displayTimeMs;
    private long fadeInTimeMs;
    private long fadeOutTimeMs;

    public LCPacketTitle() {
    }

    public LCPacketTitle(final String type, final String message, final long displayTimeMs, final long fadeInTimeMs, final long fadeOutTimeMs) {
        this(type, message, 1.0F, displayTimeMs, fadeInTimeMs, fadeOutTimeMs);
    }

    public LCPacketTitle(final String type, final String message, final float scale, final long displayTimeMs, final long fadeInTimeMs, final long fadeOutTimeMs) {
        this.type = type;
        this.message = message;
        this.scale = scale;
        this.displayTimeMs = displayTimeMs;
        this.fadeInTimeMs = fadeInTimeMs;
        this.fadeOutTimeMs = fadeOutTimeMs;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeString(this.type);
        buf.writeString(this.message);
        buf.buf().writeFloat(this.scale);
        buf.buf().writeLong(this.displayTimeMs);
        buf.buf().writeLong(this.fadeInTimeMs);
        buf.buf().writeLong(this.fadeOutTimeMs);
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        this.type = buf.readString();
        this.message = buf.readString();
        this.scale = buf.buf().readFloat();
        this.displayTimeMs = buf.buf().readLong();
        this.fadeInTimeMs = buf.buf().readLong();
        this.fadeOutTimeMs = buf.buf().readLong();
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleTitle(this);
    }

    public String getType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

    public float getScale() {
        return this.scale;
    }

    public long getDisplayTimeMs() {
        return this.displayTimeMs;
    }

    public long getFadeInTimeMs() {
        return this.fadeInTimeMs;
    }

    public long getFadeOutTimeMs() {
        return this.fadeOutTimeMs;
    }
}
