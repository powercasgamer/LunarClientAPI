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
package com.lunarclient.bukkitapi.nethandler.client;

import com.lunarclient.bukkitapi.nethandler.ByteBufWrapper;
import com.lunarclient.bukkitapi.nethandler.LCPacket;
import com.lunarclient.bukkitapi.nethandler.shared.LCNetHandler;

import java.io.IOException;

public class LCPacketWorldBorderCreateNew extends LCPacket {

    private String id;

    private String world;

    private boolean cancelsEntry, cancelsExit;

    private boolean canShrinkExpand;

    private int color = 0xFF3333FF;

    private double minX, minZ, maxX, maxZ;

    public LCPacketWorldBorderCreateNew(final String id, final String world, final boolean cancelsEntry, final boolean cancelsExit, final boolean canShrinkExpand, final int color, final double minX, final double minZ, final double maxX, final double maxZ) {
        this.id = id;
        this.world = world;
        this.cancelsEntry = cancelsEntry;
        this.cancelsExit = cancelsExit;
        this.canShrinkExpand = canShrinkExpand;
        this.color = color;
        this.minX = minX;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxZ = maxZ;
    }

    public LCPacketWorldBorderCreateNew() {
    }

    @Override
    public void write(final ByteBufWrapper b) throws IOException {
        b.buf().writeBoolean(this.id != null);
        if (this.id != null) b.writeString(this.id);
        b.writeString(this.world);
        b.buf().writeBoolean(this.cancelsEntry);
        b.buf().writeBoolean(this.cancelsExit);
        b.buf().writeBoolean(this.canShrinkExpand);
        b.buf().writeInt(this.color);
        b.buf().writeDouble(this.minX);
        b.buf().writeDouble(this.minZ);
        b.buf().writeDouble(this.maxX);
        b.buf().writeDouble(this.maxZ);
    }

    @Override
    public void read(final ByteBufWrapper b) throws IOException {
        if (b.buf().readBoolean()) {
            this.id = b.readString();
        }

        this.world = b.readString();
        this.cancelsEntry = b.buf().readBoolean();
        this.cancelsExit = b.buf().readBoolean();
        this.canShrinkExpand = b.buf().readBoolean();
        this.color = b.buf().readInt();
        this.minX = b.buf().readDouble();
        this.minZ = b.buf().readDouble();
        this.maxX = b.buf().readDouble();
        this.maxZ = b.buf().readDouble();
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleWorldBorderCreateNew(this);
    }

    public String getId() {
        return this.id;
    }

    public String getWorld() {
        return this.world;
    }

    public boolean isCancelsEntry() {
        return this.cancelsEntry;
    }

    public boolean isCancelsExit() {
        return this.cancelsExit;
    }

    public boolean isCanShrinkExpand() {
        return this.canShrinkExpand;
    }

    public int getColor() {
        return this.color;
    }

    public double getMinX() {
        return this.minX;
    }

    public double getMinZ() {
        return this.minZ;
    }

    public double getMaxX() {
        return this.maxX;
    }

    public double getMaxZ() {
        return this.maxZ;
    }
}
