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
package com.lunarclient.bukkitapi.nethandler;

import com.google.gson.*;
import com.lunarclient.bukkitapi.nethandler.client.obj.ModSettings;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class ModSettingsAdapter implements JsonDeserializer<ModSettings>, JsonSerializer<ModSettings> {

    @Override
    public ModSettings deserialize(final JsonElement jsonElement, final Type type, final JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        final ModSettings settings = new ModSettings();
        if (!jsonElement.isJsonObject()) {
            return settings;
        }

        final JsonObject object = jsonElement.getAsJsonObject();
        for (final Map.Entry<String, JsonElement> entry : object.entrySet()) {
            if (!entry.getValue().isJsonObject()) {
                continue;
            }
            final JsonObject modSettingObject = entry.getValue().getAsJsonObject();

            settings.getModSettings().put(entry.getKey(), deserializeModSetting(modSettingObject));
        }
        return settings;
    }

    @Override
    public JsonElement serialize(final ModSettings modSettings, final Type type, final JsonSerializationContext jsonSerializationContext) {
        final JsonObject object = new JsonObject();
        for (final Map.Entry<String, ModSettings.ModSetting> entry : modSettings.getModSettings().entrySet()) {
            object.add(entry.getKey(), serializeModSetting(entry.getValue()));
        }
        return object;
    }

    private JsonObject serializeModSetting(final ModSettings.ModSetting setting) {
        final JsonObject object = new JsonObject();
        final JsonObject properties = new JsonObject();
        object.addProperty("enabled", setting.isEnabled());
        for (final Map.Entry<String, Object> entry : setting.getProperties().entrySet()) {

            final JsonPrimitive primitive;
            if (entry.getValue() instanceof Boolean) {
                primitive = new JsonPrimitive((Boolean) entry.getValue());
            } else if (entry.getValue() instanceof String) {
                primitive = new JsonPrimitive((String) entry.getValue());
            } else if (entry.getValue() instanceof Number) {
                primitive = new JsonPrimitive((Number) entry.getValue());
            } else {
                continue;
            }
            properties.add(entry.getKey(), primitive);
        }
        object.add("properties", properties);
        return object;
    }

    private ModSettings.ModSetting deserializeModSetting(final JsonObject object) {
        final JsonObject propertiesObject = object.get("properties").getAsJsonObject();
        final Map<String, Object> properties = new HashMap<>();
        for (final Map.Entry<String, JsonElement> entry : propertiesObject.entrySet()) {
            if (!entry.getValue().isJsonPrimitive()) {
                continue;
            }
            final JsonPrimitive primitive = entry.getValue().getAsJsonPrimitive();
            final Object toSet;
            if (primitive.isString()) {
                toSet = primitive.getAsString();
            } else if (primitive.isNumber()) {
                toSet = primitive.getAsNumber();
            } else if (primitive.isBoolean()) {
                toSet = primitive.getAsBoolean();
            } else {
                continue;
            }
            properties.put(entry.getKey(), toSet);
        }
        return new ModSettings.ModSetting(object.get("enabled").getAsBoolean(), properties);
    }

}
