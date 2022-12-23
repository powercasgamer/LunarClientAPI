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
package com.lunarclient.bukkitapi.object;

import com.google.common.base.Objects;
import com.lunarclient.bukkitapi.LunarClientAPI;
import org.bukkit.Location;
import org.jetbrains.annotations.NotNull;

public final class LCWaypoint {

    private final String name;
    private final int x;
    private final int y;
    private final int z;
    private final String world;
    private final int color;
    private final boolean forced;
    private final boolean visible;

    /**
     * Create a waypoint with any visibility.
     * <p>
     * NOTE: Just because a waypoint object is created, that doesn't mean
     * it will be sent to users. To display the waypoint you must still send
     * it to the Lunar Client user.
     *
     * @param name     The name of the waypoint (this is important if you need to edit it later, save the name).
     * @param location The bukkit {@link Location} to display the waypoint at. Whole blocks only, partial blocks aren't accounted for.
     * @param color    The HEX color to set as the color for the waypoint.
     * @param forced   If the client should be able to remove the waypoint, or if it is forced for gameplay reasons.
     * @param visible  Weather or not the waypoint should be visible. This will likely always be true.
     */
    public LCWaypoint(@NotNull final String name, @NotNull final Location location, final int color, final boolean forced, final boolean visible) {
        this(name, location.getBlockX(), location.getBlockY(), location.getBlockZ(), LunarClientAPI.getInstance().getWorldIdentifier(location.getWorld()), color, forced, visible);
    }

    /**
     * Create a waypoint that is automatically visible when sent.
     * <p>
     * NOTE: Just because a waypoint object is created, that doesn't mean
     * it will be sent to users. To display the waypoint you must still send
     * it to the Lunar Client user.
     *
     * @param name     The name of the waypoint (this is important if you need to edit it later, save the name).
     * @param location The bukkit {@link Location} to display the waypoint at. Whole blocks only, partial blocks aren't accounted for.
     * @param color    The HEX color to set as the color for the waypoint.
     * @param forced   If the client should be able to remove the waypoint, or if it is forced for gameplay reasons.
     */
    public LCWaypoint(@NotNull final String name, @NotNull final Location location, final int color, final boolean forced) {
        this(name, location, color, forced, true);
    }

    public LCWaypoint(@NotNull final String name, final int x, final int y, final int z, @NotNull final String world, final int color, final boolean forced, final boolean visible) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
        this.world = world;
        this.color = color;
        this.forced = forced;
        this.visible = visible;
    }

    public String getName() {
        return this.name;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

    public String getWorld() {
        return this.world;
    }

    public int getColor() {
        return this.color;
    }

    public boolean isForced() {
        return this.forced;
    }

    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public boolean equals(final Object o) {
        return Objects.equal(this, o);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this);
    }

    @Override
    public String toString() {
        return "LCWaypoint(name=" + this.getName() + ", x=" + this.getX() + ", y=" + this.getY() + ", z=" + this.getZ() + ", world=" + this.getWorld() + ", color=" + this.getColor() + ", forced=" + this.isForced() + ", visible=" + this.isVisible() + ")";
    }
}
