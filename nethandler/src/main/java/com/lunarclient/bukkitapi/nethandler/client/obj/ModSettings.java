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
package com.lunarclient.bukkitapi.nethandler.client.obj;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.lunarclient.bukkitapi.nethandler.ModSettingsAdapter;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class ModSettings {

    public static final Gson GSON = new GsonBuilder().registerTypeHierarchyAdapter(ModSettings.class, new ModSettingsAdapter()).create();

    private final Map<String, ModSetting> modSettings = new HashMap<>();

    public ModSettings addModSetting(final String modId, final ModSetting setting) {
        this.modSettings.put(modId, setting);
        return this;
    }

    public ModSetting getModSetting(final String modId) {
        return this.modSettings.get(modId);
    }

    public Map<String, ModSetting> getModSettings() {
        return this.modSettings;
    }

    public static class ModSetting {
        private boolean enabled;
        private Map<String, Object> properties;

        public ModSetting() {
        } // for serialization

        public ModSetting(final boolean enabled, final Map<String, Object> properties) {
            this.enabled = enabled;
            this.properties = properties;
        }

        public boolean isEnabled() {
            return this.enabled;
        }

        public Map<String, Object> getProperties() {
            return this.properties;
        }

        @Override
        public String toString() {
            return "ModSetting{" +
                    "enabled=" + this.enabled +
                    ", properties=" + this.properties +
                    '}';
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final ModSetting that = (ModSetting) o;
            return this.enabled == that.enabled &&
                    Objects.equals(this.properties, that.properties);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.enabled, this.properties);
        }
    }

}
