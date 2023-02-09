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
import com.lunarclient.bukkitapi.nethandler.client.obj.ServerRule;
import com.lunarclient.bukkitapi.nethandler.shared.LCNetHandler;

import java.io.IOException;

public final class LCPacketServerRule extends LCPacket {

    private ServerRule rule;
    private int intValue;
    private float floatValue;
    private boolean booleanValue;
    private String stringValue = "";

    public LCPacketServerRule() {
    }

    public LCPacketServerRule(final ServerRule rule, final float value) {
        this.rule = rule;
        this.floatValue = value;
    }

    public LCPacketServerRule(final ServerRule rule, final boolean value) {
        this.rule = rule;
        this.booleanValue = value;
    }

    public LCPacketServerRule(final ServerRule rule, final int value) {
        this.rule = rule;
        this.intValue = value;
    }

    public LCPacketServerRule(final ServerRule rule, final String value) {
        this.rule = rule;
        this.stringValue = value;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeString(this.rule.getId());
        buf.buf().writeBoolean(this.booleanValue);
        buf.buf().writeInt(this.intValue);
        buf.buf().writeFloat(this.floatValue);
        buf.writeString(this.stringValue);
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        this.rule = ServerRule.getRule(buf.readString());
        this.booleanValue = buf.buf().readBoolean();
        this.intValue = buf.buf().readInt();
        this.floatValue = buf.buf().readFloat();
        this.stringValue = buf.readString();
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleServerRule(this);
    }

    public ServerRule getRule() {
        return this.rule;
    }

    public int getIntValue() {
        return this.intValue;
    }

    public float getFloatValue() {
        return this.floatValue;
    }

    public boolean isBooleanValue() {
        return this.booleanValue;
    }

    public String getStringValue() {
        return this.stringValue;
    }
}
