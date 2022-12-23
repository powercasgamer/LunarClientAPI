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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class LCPacketGhost extends LCPacket {

    private List<UUID> addGhostList;
    private List<UUID> removeGhostList;

    public LCPacketGhost() {
    }

    public LCPacketGhost(final List<UUID> uuidList, final List<UUID> removeGhostList) {
        this.addGhostList = uuidList;
        this.removeGhostList = removeGhostList;
    }

    @Override
    public void write(final ByteBufWrapper buf) throws IOException {
        buf.writeVarInt(this.addGhostList.size());

        for (final UUID uuid : this.addGhostList) {
            buf.writeUUID(uuid);
        }

        buf.writeVarInt(this.removeGhostList.size());

        for (final UUID uuid : this.removeGhostList) {
            buf.writeUUID(uuid);
        }
    }

    @Override
    public void read(final ByteBufWrapper buf) throws IOException {
        final int addListSize = buf.readVarInt();
        this.addGhostList = new ArrayList<>(addListSize);

        for (int i = 0; i < addListSize; i++) {
            this.addGhostList.add(buf.readUUID());
        }

        final int removeListSize = buf.readVarInt();
        this.removeGhostList = new ArrayList<>(removeListSize);

        for (int i = 0; i < removeListSize; i++) {
            this.removeGhostList.add(buf.readUUID());
        }
    }

    @Override
    public void process(final LCNetHandler handler) {
        ((LCNetHandlerClient) handler).handleGhost(this);
    }

    public List<UUID> getAddGhostList() {
        return this.addGhostList;
    }

    public List<UUID> getRemoveGhostList() {
        return this.removeGhostList;
    }
}
