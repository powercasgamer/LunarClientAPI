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
package com.lunarclient.bukkitapi.event;

import com.lunarclient.bukkitapi.nethandler.LCPacket;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class LCPacketReceivedEvent extends PlayerEvent {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final LCPacket packet;

    /**
     * Called when the player sends a {@link LCPacket}
     * to the server from the client.
     *
     * @param player The LunarClient player sending the packet.
     * @param packet The incoming packet from the client.
     */
    public LCPacketReceivedEvent(@NotNull final Player player, @NotNull final LCPacket packet) {
        super(player);
        this.packet = packet;
    }

    public static HandlerList getHandlerList() {
        return LCPacketReceivedEvent.HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public LCPacket getPacket() {
        return this.packet;
    }
}
