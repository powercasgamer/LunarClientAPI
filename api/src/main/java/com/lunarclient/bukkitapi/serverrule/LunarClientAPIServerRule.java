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
package com.lunarclient.bukkitapi.serverrule;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketServerRule;
import com.lunarclient.bukkitapi.nethandler.client.obj.ServerRule;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public final class LunarClientAPIServerRule {

    // The reason this is a Map is so that hopefully we will only have 1 server rule packet
    // per ServerRule. It would be extremely weird behavior if multiple with the same type were sent.
    private static final Map<ServerRule, LCPacketServerRule> customServerRules = new EnumMap<>(ServerRule.class);

    private LunarClientAPIServerRule() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Set a server rule to a boolean value.
     * All current server rules (02/02/2021)
     * use a boolean value.
     *
     * @param rule  The ServerRule with type of boolean.
     * @param value The value of the ServerRule.
     */
    public static void setRule(@NotNull final ServerRule rule, final boolean value) {
        customServerRules.put(rule, new LCPacketServerRule(rule, value));
    }

    /**
     * Send all set server rules to player(s).
     * This will likely work best to be sent on join.
     * <p>
     * If a server rule is updated, the players will need to be updated.
     * This is not an automatic process. The ideal usage would be to set
     * all server rules when the plugin loads, then when each player joins
     * send them all server rules.
     *
     * @param players The player(s) to get all the previously set server rules.
     */
    public static void sendServerRule(@NotNull final Player... players) {
        if (customServerRules.isEmpty()) {
            return;
        }
        for (final Player player : players) {
            for (final LCPacketServerRule value : customServerRules.values()) {
                LunarClientAPI.getInstance().sendPacket(player, value);
            }
        }
    }
}
