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
package com.lunarclient.bukkitapi.nethandler.client.obj;

public enum ServerRule {

    /**
     * Whether minimap is allowed
     * Expected value: (String) NEUTRAL or FORCED_OFF
     */
    MINIMAP_STATUS("minimapStatus", String.class),

    /**
     * Whether the server will store waypoints, instead of the client
     */
    SERVER_HANDLES_WAYPOINTS("serverHandlesWaypoints", Boolean.class),

    /**
     * A warning message will be shown when attempting to disconnect if the current
     * game is competitive.
     */
    COMPETITIVE_GAME("competitiveGame", Boolean.class),

    /**
     * If this server forces shaders to be disabled
     */
    SHADERS_DISABLED("shadersDisabled", Boolean.class),

    /**
     * If the server runs legacy enchanting (pre 1.8)
     */
    LEGACY_ENCHANTING("legacyEnchanting", Boolean.class),

    /**
     * If this server has enabled voice chat
     */
    VOICE_ENABLED("voiceEnabled", Boolean.class),

    /**
     * Whether to revert combat mechanics to 1.7
     */
    LEGACY_COMBAT("legacyCombat", Boolean.class);

    private final String id;
    private final Class<?> type;

    ServerRule(final String id, final Class<?> type) {
        this.id = id;
        this.type = type;
    }

    public static ServerRule getRule(final String id) {
        for (final ServerRule existing : ServerRule.values()) {
            if (existing.id.equals(id)) {
                return existing;
            }
        }

        return null;
    }

    public String getId() {
        return this.id;
    }

    public Class<?> getType() {
        return this.type;
    }
}
