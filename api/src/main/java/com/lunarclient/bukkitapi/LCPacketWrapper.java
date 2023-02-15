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
package com.lunarclient.bukkitapi;

import com.lunarclient.bukkitapi.nethandler.LCPacket;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface LCPacketWrapper<T extends LCPacket> {

    /**
     * The packet that will be sent to the player
     * that is formed in the wrapper implementation.
     *
     * @return A LCPacket to send to the Lunar Client player
     */
    T getPacket();

    /**
     * Send the wrapped packet to the player.
     *
     * @param player The online Lunar Client user to receive the packet.
     */
    default void send(@NotNull final Player player) {
        send(player, getPacket());
    }

    /**
     * Send any LCPacket to a Lunar Client user. This is used in `send` method above.
     * <p>
     * NOTE: This is intended to send only the wrapped packet, but can be used to
     * send other packets. Although it technically can be done, in most cases it would
     * be better to use {@link LunarClientAPI} to send a packet.
     *
     * @param player An online Lunar client user to receive the packet.
     * @param packet The packet to send to the user.
     */
    default void send(@NotNull final Player player, @NotNull final LCPacket packet) {
        LunarClientAPI.getInstance().sendPacket(player, packet);
    }


}
