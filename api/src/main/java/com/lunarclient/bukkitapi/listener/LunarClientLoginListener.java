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
package com.lunarclient.bukkitapi.listener;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.event.LCPlayerRegisterEvent;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketUpdateWorld;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import com.lunarclient.bukkitapi.serverrule.LunarClientAPIServerRule;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.jetbrains.annotations.NotNull;

public class LunarClientLoginListener implements Listener {

    private final LunarClientAPI lunarClientAPI;

    public LunarClientLoginListener(@NotNull final LunarClientAPI lunarClientAPI) {
        this.lunarClientAPI = lunarClientAPI;
    }

    @EventHandler (priority = EventPriority.LOWEST)
    public void onPlayerJoin(final PlayerJoinEvent event) {
        final Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(this.lunarClientAPI, () -> {
            if (!this.lunarClientAPI.isRunningLunarClient(player)) {
                this.lunarClientAPI.failPlayerRegister(player);
            }
        }, 2 * 20L);

        if (this.lunarClientAPI.getPacketModSettings() != null) {
            LunarClientAPI.getInstance().sendPacket(event.getPlayer(), this.lunarClientAPI.getPacketModSettings());
        }

        LunarClientAPIServerRule.sendServerRule(event.getPlayer());

        if (!this.lunarClientAPI.getWaypoints().isEmpty()) {
            for (final LCWaypoint waypoint : this.lunarClientAPI.getWaypoints()) {
                LunarClientAPI.getInstance().sendWaypoint(event.getPlayer(), waypoint);
            }
        }
    }

    @EventHandler
    public void onRegister(final PlayerRegisterChannelEvent event) {
        if (!event.getChannel().equalsIgnoreCase(LunarClientAPI.MESSAGE_CHANNEL)) {
            return;
        }
        final Player player = event.getPlayer();

        this.lunarClientAPI.registerPlayer(player);
        this.lunarClientAPI.getServer().getPluginManager().callEvent(new LCPlayerRegisterEvent(event.getPlayer()));

        this.updateWorld(event.getPlayer());
    }

    @EventHandler
    public void onUnregister(final PlayerUnregisterChannelEvent event) {
        if (event.getChannel().equalsIgnoreCase(LunarClientAPI.MESSAGE_CHANNEL)) {
            this.lunarClientAPI.unregisterPlayer(event.getPlayer(), false);
        }
    }

    @EventHandler
    public void onUnregister2(final PlayerQuitEvent event) {
        this.lunarClientAPI.unregisterPlayer(event.getPlayer(), true);
    }

    @EventHandler (priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldChange(final PlayerChangedWorldEvent event) {
        updateWorld(event.getPlayer());
    }

    private void updateWorld(@NotNull final Player player) {
        final String worldIdentifier = this.lunarClientAPI.getWorldIdentifier(player.getWorld());

        this.lunarClientAPI.sendPacket(player, new LCPacketUpdateWorld(worldIdentifier));
    }
}
