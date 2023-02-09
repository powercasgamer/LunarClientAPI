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

import com.google.common.base.Preconditions;
import com.lunarclient.bukkitapi.LCPacketWrapper;
import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketCooldown;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class LCCooldown implements LCPacketWrapper<LCPacketCooldown> {

    private final String name;
    private final long millis;
    private Material item;
    private final int itemId;
    // The packet that will be sent to the Lunar Client player
    // Because of @Data, we don't need a getter.
    private final LCPacketCooldown packet;


    /**
     * Used to create a persisting cooldown that can be sent to many players.
     *
     * @param name   A unique name that is not null
     * @param millis The duration for the cooldown in milliseconds, that is greater (or equal to reset) to 0.
     * @param itemId The ID of the material that will be the icon
     */
    public LCCooldown(@NotNull final String name, final long millis, final int itemId) {
        Preconditions.checkArgument(millis > 0, "Cooldown must have a valid time > 0.");
        this.name = Preconditions.checkNotNull(name, "Cooldown Name cannot be null.");
        this.millis = millis;
        this.item = LunarClientAPI.MATERIALS[itemId];
        this.itemId = itemId;
        this.packet = new LCPacketCooldown(name, millis, itemId);
    }

    /**
     * Used to create a persisting cooldown that can be sent to many players.
     *
     * @param name   A unique name that is not null
     * @param millis The duration for the cooldown in milliseconds, that is greater (or equal to reset) to 0.
     * @param item   The icon that will be displayed in Lunar Client.
     */
    public LCCooldown(@NotNull final String name, final long millis, @NotNull final Material item) {
        this(name, millis, convertMaterialToId(item));
    }

    /**
     * Simply creates a LCCooldown with seconds instead of milliseconds.
     *
     * @param name    A unique name that is not null
     * @param seconds the duration for the cooldown in seconds, that is greater (or equal to reset) to 0.
     * @param item    The icon that will be displayed in Lunar Client.
     */
    public LCCooldown(@NotNull final String name, final int seconds, @NotNull final Material item) {
        this(name, seconds * 1000L, item);
    }

    /**
     * Simply creates a LCCooldown with a variable amount of time
     *
     * @param name A unique name that is not null
     * @param time A duration greater than 0 milliseconds.
     * @param unit A unit of measurement to put the cooldown in
     * @param item The icon that will be displayed in Lunar Client.
     */
    public LCCooldown(@NotNull final String name, final int time, @NotNull final TimeUnit unit, @NotNull final Material item) {
        this(name, unit.toMillis(time), item);
    }


    /**
     * Clear the cooldown from a player before it naturally expires.
     *
     * @param player The player to clear the cooldown for.
     */
    public void clear(@NotNull final Player player) {
        send(player, new LCPacketCooldown(this.name, 0, this.itemId));
    }

    /**
     * Converts a Material into a Lunar usable ID.
     *
     * @param material The material to convert.
     * @return The ID of the material.
     */
    public static int convertMaterialToId(@NotNull final Material material) {
        final List<Material> materialList = Arrays.asList(LunarClientAPI.MATERIALS);
        return materialList.indexOf(material);
    }

    public String getName() {
        return this.name;
    }

    public long getMillis() {
        return this.millis;
    }

    public Material getItem() {
        return this.item;
    }

    public int getItemId() {
        return this.itemId;
    }

    public LCPacketCooldown getPacket() {
        return this.packet;
    }

    public void setItem(@NotNull final Material item) {
        this.item = item;
    }

    @Override
    public boolean equals(final Object o) {
        if (!(o instanceof LCCooldown)) return false;
        return Objects.equals(this, o);
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LCCooldown;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public String toString() {
        return "LCCooldown(name=" + this.getName() + ", millis=" + this.getMillis() + ", item=" + this.getItem() + ", itemId=" + this.getItemId() + ", packet=" + this.getPacket() + ")";
    }
}
