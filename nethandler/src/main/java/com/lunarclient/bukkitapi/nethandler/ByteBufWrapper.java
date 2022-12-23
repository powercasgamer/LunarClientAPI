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
package com.lunarclient.bukkitapi.nethandler;

import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public final class ByteBufWrapper {

    private final ByteBuf buf;

    public ByteBufWrapper(final ByteBuf buf) {
        this.buf = buf;
    }

    public void writeVarInt(int b) {
        while ((b & -128) != 0) {
            this.buf.writeByte(b & 127 | 128);
            b >>>= 7;
        }

        this.buf.writeByte(b);
    }

    public int readVarInt() {
        int i = 0;
        int chunk = 0;
        byte b;

        do {
            b = this.buf.readByte();
            i |= (b & 127) << chunk++ * 7;

            if (chunk > 5) {
                throw new RuntimeException("VarInt too big");
            }
        } while ((b & 128) == 128);

        return i;
    }

    public void writeString(final String s) {
        final byte[] arr = s.getBytes(StandardCharsets.UTF_8);

        this.writeVarInt(arr.length);
        this.buf.writeBytes(arr);
    }

    public String readString() {
        final int len = readVarInt();

        final byte[] buffer = new byte[len];
        this.buf.readBytes(buffer);

        return new String(buffer, StandardCharsets.UTF_8);
    }

    public void writeUUID(final UUID uuid) {
        this.buf.writeLong(uuid.getMostSignificantBits());
        this.buf.writeLong(uuid.getLeastSignificantBits());
    }

    public UUID readUUID() {
        final long mostSigBits = this.buf.readLong();
        final long leastSigBits = this.buf.readLong();

        return new UUID(mostSigBits, leastSigBits);
    }

    public ByteBuf buf() {
        return this.buf;
    }

}
