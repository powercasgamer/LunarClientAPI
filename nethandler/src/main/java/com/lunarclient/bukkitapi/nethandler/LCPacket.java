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

import com.lunarclient.bukkitapi.nethandler.client.*;
import com.lunarclient.bukkitapi.nethandler.server.LCPacketVoice;
import com.lunarclient.bukkitapi.nethandler.server.LCPacketVoiceChannel;
import com.lunarclient.bukkitapi.nethandler.server.LCPacketVoiceChannelRemove;
import com.lunarclient.bukkitapi.nethandler.server.LCPacketVoiceChannelUpdate;
import com.lunarclient.bukkitapi.nethandler.shared.LCNetHandler;
import com.lunarclient.bukkitapi.nethandler.shared.LCPacketEmoteBroadcast;
import com.lunarclient.bukkitapi.nethandler.shared.LCPacketWaypointAdd;
import com.lunarclient.bukkitapi.nethandler.shared.LCPacketWaypointRemove;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public abstract class LCPacket {

    private static final Map<Class<? extends LCPacket>, Integer> classToId = new HashMap<>();
    private static final Map<Integer, Class<? extends LCPacket>> idToClass = new HashMap<>();

    static {
        // server
        addPacket(0, LCPacketClientVoice.class);
        addPacket(16, LCPacketVoice.class);
        addPacket(1, LCPacketVoiceChannelSwitch.class);
        addPacket(2, LCPacketVoiceMute.class);

        addPacket(17, LCPacketVoiceChannel.class);
        addPacket(18, LCPacketVoiceChannelRemove.class);
        addPacket(19, LCPacketVoiceChannelUpdate.class);

        // client
        addPacket(3, LCPacketCooldown.class);
        addPacket(4, LCPacketHologram.class);
        addPacket(6, LCPacketHologramRemove.class);
        addPacket(5, LCPacketHologramUpdate.class);
        addPacket(7, LCPacketNametagsOverride.class);
        addPacket(8, LCPacketNametagsUpdate.class);
        addPacket(9, LCPacketNotification.class);
        addPacket(10, LCPacketServerRule.class);
        addPacket(11, LCPacketServerUpdate.class);
        addPacket(12, LCPacketStaffModState.class);
        addPacket(13, LCPacketTeammates.class);
        addPacket(14, LCPacketTitle.class);
        addPacket(15, LCPacketUpdateWorld.class);
        addPacket(20, LCPacketWorldBorder.class);
        addPacket(21, LCPacketWorldBorderRemove.class);
        addPacket(22, LCPacketWorldBorderUpdate.class);
        addPacket(25, LCPacketGhost.class);
        addPacket(28, LCPacketBossBar.class);
        addPacket(29, LCPacketWorldBorderCreateNew.class);
        addPacket(30, LCPacketWorldBorderUpdateNew.class);
        addPacket(31, LCPacketModSettings.class);

        // shared
        addPacket(26, LCPacketEmoteBroadcast.class);
        addPacket(23, LCPacketWaypointAdd.class);
        addPacket(24, LCPacketWaypointRemove.class);
    }

    private Object attachment;

    public static LCPacket handle(final byte[] data) {
        return handle(data, null);
    }

    public static LCPacket handle(final byte[] data, final Object attachment) {
        final ByteBufWrapper wrappedBuffer = new ByteBufWrapper(Unpooled.wrappedBuffer(data));

        final int packetId = wrappedBuffer.readVarInt();
        final Class<? extends LCPacket> packetClass = idToClass.get(packetId);

        if (packetClass != null) {
            try {
                final LCPacket packet = packetClass.newInstance();

                packet.attach(attachment);
                packet.read(wrappedBuffer);

                return packet;
            } catch (final IOException | InstantiationException | IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }

        return null;
    }

    public static byte[] getPacketData(final LCPacket packet) {
        return getPacketBuf(packet).array();
    }

    public static ByteBuf getPacketBuf(final LCPacket packet) {
        final ByteBufWrapper wrappedBuffer = new ByteBufWrapper(Unpooled.buffer());
        wrappedBuffer.writeVarInt(classToId.get(packet.getClass()));

        try {
            packet.write(wrappedBuffer);
        } catch (final IOException ex) {
            ex.printStackTrace();
        }

        return wrappedBuffer.buf();
    }

    private static void addPacket(final int id, final Class<? extends LCPacket> clazz) {
        if (classToId.containsKey(clazz)) {
            throw new IllegalArgumentException("Duplicate packet class (" + clazz.getSimpleName() + "), already used by " + classToId.get(clazz));
        } else if (idToClass.containsKey(id)) {
            throw new IllegalArgumentException("Duplicate packet ID (" + id + "), already used by " + idToClass.get(id).getSimpleName());
        }

        classToId.put(clazz, id);
        idToClass.put(id, clazz);
    }

    public abstract void write(ByteBufWrapper buf) throws IOException;

    public abstract void read(ByteBufWrapper buf) throws IOException;

    public abstract void process(LCNetHandler handler);

    public <T> void attach(final T obj) {
        this.attachment = obj;
    }

    @SuppressWarnings ("unchecked")
    public <T> T getAttachment() {
        return (T) this.attachment;
    }

    protected void writeBlob(final ByteBufWrapper b, final byte[] bytes) {
        b.buf().writeShort(bytes.length);
        b.buf().writeBytes(bytes);
    }

    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

    protected byte[] readBlob(final ByteBufWrapper b) {
        final short key = b.buf().readShort();

        if (key < 0) {
            System.out.println("Key was smaller than nothing! Weird key!");
        } else {
            final byte[] blob = new byte[key];
            b.buf().readBytes(blob);
            return blob;
        }

        return EMPTY_BYTE_ARRAY;
    }

}
