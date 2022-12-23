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

public final class LCPacketBossBar extends LCPacket {

    private int action;
    private String text;
    private float health;

    public LCPacketBossBar() {
    }

    public LCPacketBossBar(final int action, final String text, final float health) {
        this.action = action;
        this.text = text;
        this.health = health;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeVarInt(this.action);

        if (this.action == 0) {
            buf.writeString(this.text);
            buf.buf().writeFloat(this.health);
        }
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        this.action = buf.readVarInt();

        if (this.action == 0) {
            this.text = buf.readString();
            this.health = buf.buf().readFloat();
        }
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleBossBar(this);
    }

    public int getAction() {
        return this.action;
    }

    public String getText() {
        return this.text;
    }

    public float getHealth() {
        return this.health;
    }
}
