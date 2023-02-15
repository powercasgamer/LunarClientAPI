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
package com.lunarclient.bukkitapi;

import com.google.common.collect.Sets;
import com.lunarclient.bukkitapi.event.LCPacketReceivedEvent;
import com.lunarclient.bukkitapi.event.LCPacketSentEvent;
import com.lunarclient.bukkitapi.event.LCPlayerUnregisterEvent;
import com.lunarclient.bukkitapi.listener.LunarClientLoginListener;
import com.lunarclient.bukkitapi.nethandler.LCPacket;
import com.lunarclient.bukkitapi.nethandler.client.*;
import com.lunarclient.bukkitapi.nethandler.client.obj.ModSettings;
import com.lunarclient.bukkitapi.nethandler.client.obj.ServerRule;
import com.lunarclient.bukkitapi.nethandler.server.LCNetHandlerServer;
import com.lunarclient.bukkitapi.nethandler.shared.LCPacketWaypointAdd;
import com.lunarclient.bukkitapi.nethandler.shared.LCPacketWaypointRemove;
import com.lunarclient.bukkitapi.object.LCWaypoint;
import com.lunarclient.bukkitapi.object.StaffModule;
import com.lunarclient.bukkitapi.serverrule.LunarClientAPIServerRule;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

public final class LunarClientAPI extends JavaPlugin implements Listener {

    public static final String MESSAGE_CHANNEL = "lunarclient:pm";
    public static final ChatColor[] CHAT_COLORS = ChatColor.values();
    public static final Material[] MATERIALS = Material.values();
    public static boolean ASYNC_PACKETS;

    private static LunarClientAPI instance;
    private final Set<UUID> playersRunningLunarClient = Collections.newSetFromMap(new ConcurrentHashMap<>());
    private final Set<UUID> playersNotRegistered = new HashSet<>();
    private final Map<UUID, List<LCPacket>> packetQueue = new HashMap<>();
    private final Map<UUID, Function<World, String>> worldIdentifiers = new HashMap<>();
    private final List<LCWaypoint> waypoints = new ArrayList<>();
    private final String asyncMessage = "We will attempt to send packets async, if this causes errors disable it in the config" +
            "\n" +
            "This is an experimental feature, if you encounter any errors please report them on our github.";
    private LCNetHandlerServer netHandlerServer = new LunarClientDefaultNetHandler();
    // BukkitImpl Stuff
    private LCPacketModSettings packetModSettings = null;

    public static LunarClientAPI getInstance() {
        return LunarClientAPI.instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        saveDefaultConfig();
        ASYNC_PACKETS = getConfig().getBoolean("misc.async-packets");
        if (ASYNC_PACKETS) getLogger().warning(this.asyncMessage);
    }

    @Override
    public void onEnable() {
        if (ASYNC_PACKETS) getLogger().warning(this.asyncMessage);

        this.registerPluginChannel();
        this.getServer().getPluginManager().registerEvents(new LunarClientLoginListener(this), this);

        loadWaypoints();
        loadServerRules();
        loadDisabledMods();
    }

    @Override
    public void onDisable() {
        Bukkit.getMessenger().unregisterIncomingPluginChannel(this);
        Bukkit.getMessenger().unregisterOutgoingPluginChannel(this);
        HandlerList.unregisterAll((JavaPlugin) this);
        this.playersRunningLunarClient.clear();
        this.playersNotRegistered.clear();
        this.worldIdentifiers.clear();
        this.packetQueue.clear();
        this.waypoints.clear();
        this.packetModSettings = null;
        Bukkit.getScheduler().cancelTasks(this);
        instance = null;
    }

    /**
     * Registers the bukkit plugin channel based on configuration of allowed players
     */
    private void registerPluginChannel() {
        final Messenger messenger = getServer().getMessenger();
        messenger.registerOutgoingPluginChannel(this, LunarClientAPI.MESSAGE_CHANNEL);
        messenger.registerIncomingPluginChannel(this, LunarClientAPI.MESSAGE_CHANNEL, (channel, player, bytes) -> {
            final LCPacket packet = LCPacket.handle(bytes, player);
            if (packet == null) {
                throw new RuntimeException("packet is null");
            }
            this.getServer().getPluginManager().callEvent(new LCPacketReceivedEvent(player, packet));
            packet.process(this.netHandlerServer);
        });
    }

    /**
     * Called when the player has been online for 2 seconds without
     * sending any sort of registration stating their on lunar client.
     * <p>
     * This will remove all queued packets that we've saved, and prevent further packets
     * from being stored for this player.
     * <p>
     * Used in {@link LunarClientLoginListener}. Do not use unless you are certain you need this.
     *
     * @param player The player that has been online for at least 2 seconds.
     */
    public void failPlayerRegister(final Player player) {
        this.playersNotRegistered.add(player.getUniqueId());
        this.packetQueue.remove(player.getUniqueId());
    }

    /**
     * Register the player as a Lunar Client player based off of their
     * bukkit plugin channel registration.
     * <p>
     * NOTE: Do NOT use this as an anticheat method as this can be spoofed,
     * or changed. This is intended for cosmetic use only!
     *
     * @param player The player registering as a Lunar Client user.
     */
    public void registerPlayer(final Player player) {
        this.playersNotRegistered.remove(player.getUniqueId());
        this.playersRunningLunarClient.add(player.getUniqueId());
        if (this.packetQueue.containsKey(player.getUniqueId())) {
            for (final LCPacket packet : this.packetQueue.get(player.getUniqueId())) {
                this.sendPacket(player, packet);
            }

            this.packetQueue.remove(player.getUniqueId());
        }
    }

    /**
     * Unregisters a player as a LunarClient player. This can be done by the client
     * or by quitting. Either way we remove the player from the set, if they did not
     * quit we don't want to remove them from NotRegistered players as we would
     * continue to hold packets for them.
     * <p>
     * If they did not quit, we don't want to hold packets for them forever, so we
     * add them to the not registered players set.
     *
     * @param player The player who has unregistered themselves from the plugin channel.
     * @param quit   A {@link Boolean} value of weather the player quit or simply unregistered from the channel.
     */
    public void unregisterPlayer(@NotNull final Player player, final boolean quit) {
        this.playersRunningLunarClient.remove(player.getUniqueId());
        if (quit) {
            this.playersNotRegistered.remove(player.getUniqueId());
        } else {
            this.playersNotRegistered.add(player.getUniqueId());
            this.getServer().getPluginManager().callEvent(new LCPlayerUnregisterEvent(player));
        }
    }

    /**
     * Checks if the player is currently running lunar client.
     *
     * @param player {@link Player} suspect of using Lunar Client.
     * @return The {@link Boolean} value of weather the online player is currently using Lunar Client.
     */
    public boolean isRunningLunarClient(@NotNull final Player player) {
        return isRunningLunarClient(player.getUniqueId());
    }

    /**
     * Checks if a user is currently running LunarClient on the server.
     *
     * @param playerUuid The ID of the suspect LunarClient user.
     * @return The {@link Boolean} value of weather the player is currently running Lunar Client.
     */
    public boolean isRunningLunarClient(@NotNull final UUID playerUuid) {
        return this.playersRunningLunarClient.contains(playerUuid);
    }

    /**
     * Gets an immutable set of all the bukkit {@link Player} currently running lunar client.
     * <p>
     * NOTE: This would only be ideal for displaying an object of the player, for computation
     * it would probably be better to use the raw UUID of the players.
     *
     * @return An unmodifiableSet of the players currently running lunar client.
     */
    public Set<Player> getPlayersRunningLunarClient() {
        final Set<Player> players = Sets.newHashSet();
        for (final UUID uuid : this.playersRunningLunarClient) {
            players.add(Bukkit.getPlayer(uuid));
        }
        return Collections.unmodifiableSet(players);
    }

    /**
     * Force set a specific {@link StaffModule} for a specific player.
     * Useful for enabling only a few {@link StaffModule}
     *
     * @param player The player receiving the staff modules.
     * @param module The staff module to set to a state.
     * @param state  The new state of the StaffModule.
     */
    public void setStaffModuleState(@NotNull final Player player, @NotNull final StaffModule module, final boolean state) {
        this.sendPacket(player, new LCPacketStaffModState(module.name(), state));
    }

    /**
     * Gives a player ALL staff modules.
     * Not recommended, a better path would to explicitly give a player each module.
     *
     * @param player The player to receive the enabled staff modules.
     */
    public void giveAllStaffModules(@NotNull final Player player) {
        for (final StaffModule module : StaffModule.values()) {
            this.setStaffModuleState(player, module, true);
        }
    }

    /**
     * Disables ALL staff modules for a specific player
     * regardless of if they are enabled.
     *
     * @param player The player receiving the new staff module state.
     */
    public void disableAllStaffModules(@NotNull final Player player) {
        for (final StaffModule module : StaffModule.values()) {
            this.setStaffModuleState(player, module, false);
        }
    }

    /**
     * Sends a validated teammate object to the player.
     * Tells the player of all its known teammates, ensure they're both online and in the world.
     *
     * @param player The player to receive the teammates
     * @param packet The teammates to send to the player.
     */
    public void sendTeammates(@NotNull final Player player, @NotNull final LCPacketTeammates packet) {
        this.validatePlayers(player, packet);
        this.sendPacket(player, packet);
    }

    /**
     * Validate teammates for the player (sendingTo).
     * Ensures the teammates are online and in the same world before sending the location.
     * Used above in sendTeammates.
     *
     * @param sendingTo The player we're sending teammates to.
     * @param packet    The teammates packet to verify.
     */
    private void validatePlayers(@NotNull final Player sendingTo, @NotNull final LCPacketTeammates packet) {
        for (final Map.Entry<UUID, Map<String, Double>> entry : packet.getPlayers().entrySet()) {
            final UUID uuid = entry.getKey();
            final Player player = Bukkit.getPlayer(uuid);
            if (player == null || !player.isOnline()) {
                getLogger().warning(String.format("The player connected to UUID %s is null or offline", uuid));
                return; // maybe?
            }
            if (player.getWorld().getUID().equals(sendingTo.getWorld().getUID())) {
                packet.getPlayers().remove(uuid);
            }
        }
    }

    /**
     * Create a hologram for a player on the server.
     *
     * @param player   The observer of the new hologram.
     * @param id       The randomly generated UUID for the hologram. This will need to be saved for other hologram actions.
     * @param position The location (x, y, z) of where the hologram will be placed in the world.
     * @param lines    The lines of the hologram to be sent to the player.
     */
    public void addHologram(@NotNull final Player player, @NotNull final UUID id, @NotNull final Vector position, @NotNull final String @NotNull [] lines) {
        this.sendPacket(player, new LCPacketHologram(id, position.getX(), position.getY(), position.getZ(), Arrays.asList(lines)));
    }

    /**
     * Update the lines of a previously added hologram for a specific player.
     *
     * @param player The observer of the new hologram lines.
     * @param id     The ID of the previously placed hologram.
     * @param lines  The new lines to show to the player.
     */
    public void updateHologram(@NotNull final Player player, @NotNull final UUID id, @NotNull final String @NotNull [] lines) {
        this.sendPacket(player, new LCPacketHologramUpdate(id, Arrays.asList(lines)));
    }

    /**
     * Remove a previously set hologram for a specific player.
     *
     * @param player The player to remove the hologram for.
     * @param id     The ID of the previously created hologram.
     */
    public void removeHologram(@NotNull final Player player, @NotNull final UUID id) {
        this.sendPacket(player, new LCPacketHologramRemove(id));
    }

    /**
     * Override the normal (bukkit) nametag with lunar client nametags.
     * This supports multiple lines, so index 0 will be bottom of the nametags.
     *
     * @param target  The player who's nametag will be affected for the viewer.
     * @param nametag The list of nametags that will be sent to the viewer. Supports color codes.
     * @param viewer  The observer who will see the targets nametag as a lunar client nametag.
     */
    public void overrideNametag(@NotNull final Player target, @NotNull final List<String> nametag, @NotNull final Player viewer) {
        this.sendPacket(viewer, new LCPacketNametagsOverride(target.getUniqueId(), nametag));
    }

    /**
     * Reset anything done to the nametag.
     * This will reset hideNametag or any
     * other action done to the nametag.
     *
     * @param target The player's nametag that will be reset for the viewer.
     * @param viewer The observer who will see the targets nametag as normal (bukkit).
     */
    public void resetNametag(@NotNull final Player target, @NotNull final Player viewer) {
        this.sendPacket(viewer, new LCPacketNametagsOverride(target.getUniqueId(), null));
    }

    /**
     * Hide the target's username from the viewer.
     *
     * @param target This player's nametag will be hidden from the viewer.
     * @param viewer The observer who will hide the targets' nametag.
     */
    public void hideNametag(@NotNull final Player target, @NotNull final Player viewer) {
        this.sendPacket(viewer, new LCPacketNametagsOverride(target.getUniqueId(), Collections.emptyList()));
    }

    /**
     * Gets the world identifier weather that is custom or
     * the worlds generated id for sending waypoints to the client.
     *
     * @param world The world to get the identifier for
     * @return {@link String} of the set name of the world, or default to the world's unique id.
     */
    public String getWorldIdentifier(@NotNull final World world) {
        final UUID worldIdentifier = world.getUID();

        if (worldIdentifier == null) {
            return UUID.randomUUID().toString();
        }

        return this.worldIdentifiers.containsKey(worldIdentifier)
                ? this.worldIdentifiers.get(worldIdentifier).apply(world)
                : worldIdentifier.toString();
    }

    /**
     * Registers a custom name for the world to be identified as on the client.
     * <p>
     * Not required, if not set it will default to the unquie id of the world.
     *
     * @param world      The bukkit object for the world.
     * @param identifier The new function identifier.
     */
    public void registerWorldIdentifier(@NotNull final World world, @NotNull final Function<World, String> identifier) {
        this.worldIdentifiers.put(world.getUID(), identifier);
    }

    /**
     * Send a waypoint to a lunarclient player.
     * <p>
     * Note: You will likely need to persist this object in order to remove it later.
     *
     * @param player   A player running lunar client.
     * @param waypoint A new waypoint object to send to the player.
     */
    public void sendWaypoint(@NotNull final Player player, @NotNull final LCWaypoint waypoint) {
        this.sendPacket(player, new LCPacketWaypointAdd(waypoint.getName(), waypoint.getWorld(), waypoint.getColor(), waypoint.getX(), waypoint.getY(), waypoint.getZ(), waypoint.isForced(), waypoint.isVisible()));
    }

    /**
     * Remove a waypoint that the server sent from a player.
     *
     * @param player   A player running lunar client.
     * @param waypoint A waypoint object that the server has previously sent.
     */
    public void removeWaypoint(@NotNull final Player player, @NotNull final LCWaypoint waypoint) {
        this.sendPacket(player, new LCPacketWaypointRemove(waypoint.getName(), waypoint.getWorld()));
    }

    /**
     * Send a notification to a lunar client player
     *
     * @param player       A player running lunar client
     * @param notification The notification to send to the player
     */
    public void sendNotification(@NotNull final Player player, @NotNull final LCPacketNotification notification) {
        this.sendPacket(player, notification);
    }

    /**
     * The most basic component of the Lunar Client API used
     * to send packets to the Lunar Client player.
     * Implementation with direct method calls is not recommended,
     * and will likely be very brittle in regard to functionality.
     * <p>
     * sendPacket will either send the packet immediately to the Lunar Client player
     * or if for some reason there is a delay in the connection, it will queue the packet for
     * consumption at a later date, when the player registers.
     *
     * @param player The bukkit representation of the {@link Player} to receive the packet.
     * @param packet The Lunar Client packet that should be sent to the Lunar Client player.
     * @return {@link Boolean} value of weather the packet was sent.
     */
    public boolean sendPacket(@NotNull final Player player, @NotNull final LCPacket packet) {
        final UUID playerId = player.getUniqueId();
        if (isRunningLunarClient(playerId)) {
            if (ASYNC_PACKETS) {
                Bukkit.getScheduler().runTask(this, () -> {
                    player.sendPluginMessage(this, MESSAGE_CHANNEL, LCPacket.getPacketData(packet));
                    Bukkit.getPluginManager().callEvent(new LCPacketSentEvent(player, packet));
                });
            } else {
                player.sendPluginMessage(this, MESSAGE_CHANNEL, LCPacket.getPacketData(packet));
                Bukkit.getPluginManager().callEvent(new LCPacketSentEvent(player, packet));
            }
            return true;
        }

        // If the player hasn't been on for 2 seconds, but also
        // hasn't registered we hold on to hope they are just lagging,
        // and so we save packets for them until they are proven not
        // lunar client players.
        // Either way, the packet failed to send (this time).

        if (!this.playersNotRegistered.contains(playerId)) {
            this.packetQueue.computeIfAbsent(playerId, $ -> new ArrayList<>());
            this.packetQueue.get(playerId).add(packet);
        }
        return false;
    }

    public void setNetHandlerServer(@NotNull final LCNetHandlerServer netHandlerServer) {
        this.netHandlerServer = netHandlerServer;
    }

    public void loadWaypoints() {
        // if we don't have waypoints, don't continue.
        if (!getConfig().getBoolean("waypoints.enabled", false)) {
            return;
        }

        // Get all the list of waypoints
        final ConfigurationSection section = getConfig().getConfigurationSection("waypoints.list");
        for (final String key : section.getKeys(false)) {
            final String name = section.getString(key + ".name");
            final int x = section.getInt(key + ".x", 0);
            final int y = section.getInt(key + ".y", 80);
            final int z = section.getInt(key + ".z", 0);
            final String worldName = section.getString(key + ".world");
            final World world = Bukkit.getWorld(Objects.requireNonNull(worldName));
            if (world == null) {
                throw new IllegalStateException("The world with the name '" + worldName + "' is null!");
            }
            final String worldId = getWorldIdentifier(world);
            final int color = section.getInt(key + ".color");
            final boolean forced = section.getBoolean(key + ".forced", true);
            final boolean visible = section.getBoolean(key + ".visible", true);
            this.waypoints.add(new LCWaypoint(
                    name,
                    x,
                    y,
                    z,
                    worldId,
                    color,
                    forced,
                    visible
            ));
        }
    }

    /**
     * We are going to load the server rules from the config, and
     * then immediately set them in the API and let that handle the
     * caching for us. This will allow us to simply just call send
     * once the player is registered.
     */
    private void loadServerRules() {
        if (getConfig().contains("server-rules")) {
            for (final ServerRule value : ServerRule.values()) {
                if (getConfig().contains("server-rules." + value.name()) && getConfig().isBoolean("server-rules." + value.name())) {
                    LunarClientAPIServerRule.setRule(value, getConfig().getBoolean("server-rules." + value.name()));
                }
                if (!getConfig().contains("server-rules." + value.name())) {
                    getConfig().set("server-rules." + value.name(), false);
                }
            }
        }
    }

    /**
     * We are going to load the disabled mods from the config, and
     * cache the packet until we have users who have registered.
     * <p>
     * Once they registered, we will send the packet. We only
     * want to have 1 packet we send to a lot of users.
     */
    private void loadDisabledMods() {
        // If we have the disabled mods key, and it's a list we want to set mod settings.
        if (getConfig().contains("force-disabled-mods") && getConfig().isList("force-disabled-mods")) {
            final ModSettings modSettings = new ModSettings();
            // Go through all the items in the list, and disable each mod.
            for (final String modId : getConfig().getStringList("force-disabled-mods")) {
                modSettings.addModSetting(modId, new ModSettings.ModSetting(false, new HashMap<>()));
            }
            this.packetModSettings = new LCPacketModSettings(modSettings);
        }
    }

    public LCPacketModSettings getPacketModSettings() {
        return this.packetModSettings;
    }

    public List<LCWaypoint> getWaypoints() {
        return this.waypoints;
    }
}
