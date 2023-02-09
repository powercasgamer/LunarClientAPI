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
package com.lunarclient.bukkitapi.cooldown;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public final class LunarClientAPICooldown {

    private static final Map<String, LCCooldown> registeredCooldowns = new HashMap<>();

    private LunarClientAPICooldown() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    /**
     * Used to register a persisting cooldown.
     * Ideally 1 {@link LCCooldown} will be created for each task.
     * Create the {@link LCCooldown} once, register it and then send it to each player.
     *
     * @param cooldown The cooldown to register
     */
    public static void registerCooldown(@NotNull final LCCooldown cooldown) {
        registeredCooldowns.put(cooldown.getName().toLowerCase(), cooldown);
    }

    /**
     * Used to unregister a persisting cooldown.
     * If a cooldown will no longer be used or in shutdown, unregistering is a good idea.
     *
     * @param cooldown The cooldown to register
     */
    public static void unregisterCooldown(@NotNull final LCCooldown cooldown) {
        registeredCooldowns.remove(cooldown.getName().toLowerCase());
    }

    /**
     * Sends a cooldown to a Lunar Client player that has previously been registered.
     * This could be used instead of passing around a {@link LCCooldown} instance.
     *
     * @param player       The player to send a cooldown to
     * @param cooldownName The name of the {@link LCCooldown} that is sent.
     */
    public static void sendCooldown(@NotNull final Player player, @NotNull final String cooldownName) {
        final String cooldownId = cooldownName.toLowerCase();
        if (!registeredCooldowns.containsKey(cooldownId)) {
            throw new IllegalStateException("Attempted to send a cooldown that isn't registered [" + cooldownName + "]");
        }
        registeredCooldowns.get(cooldownId).send(player);
    }

    /**
     * Removes the {@link LCCooldown} associated with the provided cooldown name that is
     * currently registered from a Lunar Client player
     *
     * @param player The player to remove the cooldown for
     * @param cooldownName The name of the {@link LCCooldown} to remove
     */
    public static void clearCooldown(@NotNull final Player player, @NotNull final String cooldownName) {
        final String cooldownId = cooldownName.toLowerCase();
        if (!registeredCooldowns.containsKey(cooldownId)) {
            throw new IllegalStateException("Attempted to send a cooldown that isn't registered [" + cooldownName + "]");
        }
        registeredCooldowns.get(cooldownId).clear(player);
    }

    /**
     * Checks to see if the provided cooldown name is a currently registered cooldown
     *
     * @param cooldownName The name of the {@link LCCooldown} to check
     * @return {@code true} if the provided name is currently registered
     */
    public static boolean isCooldownRegistered(@NotNull final String cooldownName) {
        return registeredCooldowns.containsKey(cooldownName);
    }

    /**
     * Gets an {@link Optional} that either contains the {@link LCCooldown} associated with
     * the provided name or an empty {@link Optional} if {@link #isCooldownRegistered(String)}
     * returns {@code false} for the provided name.
     *
     * @param cooldownName The name of the {@link LCCooldown} to get
     * @return An {@link Optional} that either contains the {@link LCCooldown} associated with
     * the provided name or an empty {@link Optional} if {@link #isCooldownRegistered(String)}
     * returns {@code false} for the provided name.
     */
    public static @NotNull Optional<@Nullable LCCooldown> getCooldown(@NotNull final String cooldownName) {
        return Optional.ofNullable(registeredCooldowns.get(cooldownName));
    }
}
