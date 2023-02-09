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
package com.lunarclient.bukkitapi.object;

/**
 * Lunar client has hidden StaffModule that can be
 * sent to a user at any given point.
 * <p>
 * Currently only XRAY is implemented but there are
 * future plans to expand this to possibly NAME_TAGS,
 * and there is still discussion of whether BUNNY_HOP
 * will make a return.
 * <p>
 * NOTE: Lunar Client will NOT check if the user is
 * actually staff, if the user is given any StaffModule
 * by the server it is assumed that the server knows what
 * it is doing and will not check permissions or other.
 */
public enum StaffModule {

    XRAY,
    @Deprecated(forRemoval = true)
    NAME_TAGS,
    @Deprecated(forRemoval = true)
    BUNNY_HOP

}
