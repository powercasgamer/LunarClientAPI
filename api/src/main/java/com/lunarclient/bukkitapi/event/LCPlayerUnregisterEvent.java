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

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.NotNull;

public final class LCPlayerUnregisterEvent extends PlayerEvent {

    private static final HandlerList handlerList = new HandlerList();

    /**
     * Called whenever a player unregisters the LC plugin channel
     *
     * @param player The player who has unregistered from the plugin channel.
     */
    public LCPlayerUnregisterEvent(@NotNull final Player player) {
        super(player);
    }

    public static HandlerList getHandlerList() {
        return LCPlayerUnregisterEvent.handlerList;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlerList;
    }

}
