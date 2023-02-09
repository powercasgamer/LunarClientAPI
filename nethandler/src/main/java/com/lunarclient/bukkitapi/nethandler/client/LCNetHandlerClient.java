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
package com.lunarclient.bukkitapi.nethandler.client;

import com.lunarclient.bukkitapi.nethandler.server.LCPacketVoice;
import com.lunarclient.bukkitapi.nethandler.server.LCPacketVoiceChannel;
import com.lunarclient.bukkitapi.nethandler.server.LCPacketVoiceChannelRemove;
import com.lunarclient.bukkitapi.nethandler.server.LCPacketVoiceChannelUpdate;
import com.lunarclient.bukkitapi.nethandler.shared.LCNetHandler;

public interface LCNetHandlerClient extends LCNetHandler {

    void handleBossBar(LCPacketBossBar packet);

    void handleCooldown(LCPacketCooldown packet);

    void handleGhost(LCPacketGhost packet);

    void handleAddHologram(LCPacketHologram packet);

    void handleRemoveHologram(LCPacketHologramRemove packet);

    void handleUpdateHologram(LCPacketHologramUpdate packet);

    void handleOverrideNametags(LCPacketNametagsOverride packet);

    void handleNametagsUpdate(LCPacketNametagsUpdate packet);

    void handleNotification(LCPacketNotification packet);

    void handleServerRule(LCPacketServerRule packet);

    void handleServerUpdate(LCPacketServerUpdate packet);

    void handleStaffModState(LCPacketStaffModState packet);

    void handleTeammates(LCPacketTeammates packet);

    void handleTitle(LCPacketTitle packet);

    void handleUpdateWorld(LCPacketUpdateWorld packet);

    void handleWorldBorder(LCPacketWorldBorder packet);

    void handleWorldBorderCreateNew(LCPacketWorldBorderCreateNew packet);

    void handleWorldBorderRemove(LCPacketWorldBorderRemove packet);

    void handleWorldBorderUpdate(LCPacketWorldBorderUpdate packet);

    void handleWorldBorderUpdateNew(LCPacketWorldBorderUpdateNew packet);

    void handleVoice(LCPacketVoice packet);

    void handleVoiceChannels(LCPacketVoiceChannel packet);

    void handleVoiceChannelUpdate(LCPacketVoiceChannelUpdate packet);

    void handleVoiceChannelDelete(LCPacketVoiceChannelRemove packet);

    void handleModSettings(LCPacketModSettings packetModSettings);
}
