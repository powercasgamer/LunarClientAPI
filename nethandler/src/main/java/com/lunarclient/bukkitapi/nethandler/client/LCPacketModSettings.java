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
import com.lunarclient.bukkitapi.nethandler.client.obj.ModSettings;
import com.lunarclient.bukkitapi.nethandler.shared.LCNetHandler;

import java.io.IOException;

public final class LCPacketModSettings extends LCPacket {

    private ModSettings settings;

    public LCPacketModSettings() {
    }

    public LCPacketModSettings(final ModSettings modSettings) {
        this.settings = modSettings;
    }


    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeString(ModSettings.GSON.toJson(this.settings));
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        this.settings = ModSettings.GSON.fromJson(buf.readString(), ModSettings.class);
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleModSettings(this);
    }

    public ModSettings getSettings() {
        return this.settings;
    }
}
