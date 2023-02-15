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
package com.lunarclient.bukkitapi.title;

import com.lunarclient.bukkitapi.LunarClientAPI;
import com.lunarclient.bukkitapi.nethandler.client.LCPacketTitle;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

public final class LCTitleBuilder {

    /**
     * Create a LCTitle by type and message.
     * The message is final, but the type can be changed
     * until the packet is built.
     *
     * @param message The message that will be displayed.
     * @param type    The {@link TitleType} to send to the client
     * @return A new title builder.
     */
    public static LCTitleBuilder of(@NotNull final String message, @NotNull final TitleType type) {
        return new LCTitleBuilder(message, type);
    }

    /**
     * Create a LCTitle with a message.
     * The type defaults to a title.
     *
     * @param message The message to display to the user.
     * @return The new title builder.
     */
    public static LCTitleBuilder of(@NotNull final String message) {
        return of(message, TitleType.TITLE);
    }

    private final String message;
    private TitleType type;
    private float scale;
    private Duration displayDuration;
    private Duration fadeInDuration;
    private Duration fadeOutDuration;

    /**
     * Starts a new builder with default params.
     * If nothing else is specified then it will
     * default to a title with your message that lasts
     * 1.5 seconds and fades in and out for .5 seconds.
     *
     * @param message The final message specified to start the builder.
     * @param type    The type to start the builder.
     */
    private LCTitleBuilder(@NotNull final String message, @NotNull final TitleType type) {
        this.message = message;
        this.type = type;
        this.scale = 1;
        this.displayDuration = Duration.ofMillis(1500);
        this.fadeInDuration = Duration.ofMillis(500);
        this.fadeOutDuration = Duration.ofMillis(500);
    }

    /**
     * Specify the {@link TitleType} for the title to be displayed.
     *
     * @param type The type of type to display.
     * @return This builder with the updated value.
     */
    public LCTitleBuilder type(@NotNull final TitleType type) {
        this.type = type;
        return this;
    }

    /**
     * Scale up or down the title sent to the client.
     *
     * @param scale The new scale (greater than 0) that the message will be displayed on.
     * @return This builder with the updated value.
     */
    public LCTitleBuilder scale(final float scale) {
        this.scale = scale;
        return this;
    }

    /**
     * Specify the display time for which this
     * title will be displayed to the user.
     * <p>
     * The title will display for fade in + DISPLAY TIME + fade out = total display time.
     *
     * @param duration The {@link Duration} of how long to display the title for.
     * @return This builder with the updated value.
     */
    public LCTitleBuilder displayFor(@NotNull final Duration duration) {
        this.displayDuration = duration;
        return this;
    }

    /**
     * Specify the amount of time it takes
     * the title to fade in for the user.
     * <p>
     * The title will display for FADE IN + duration + fade out = total display time.
     *
     * @param duration The {@link Duration} of how long to fade in the title for.
     * @return This builder with the updated value.
     */
    public LCTitleBuilder fadeInFor(@NotNull final Duration duration) {
        this.fadeInDuration = duration;
        return this;
    }

    /**
     * Specify the amount of time it takes
     * the title to fade out for the user.
     * <p>
     * The title will display for fade in + duration + FADE OUT = total display time.
     *
     * @param duration The {@link Duration} of how long to fade in the title for.
     * @return This builder with the updated value.
     */
    public LCTitleBuilder fadeOutFor(@NotNull final Duration duration) {
        this.fadeOutDuration = duration;
        return this;
    }

    /**
     * Take all the inputs from the builder and convert
     * it into a single LCPacketTitle you can use to
     * save and send to players as needed.
     * <p>
     * Ideally if possible you would build the title packet once
     * and then send it to all the players that can see it.
     * A good example of this would be a welcome title, where
     * its entirely static but needs to be sent to all users.
     *
     * @return The {@link LCPacketTitle} to send to Lunar Client users as needed.
     */
    public LCPacketTitle build() {
        return new LCPacketTitle(this.type.name().toLowerCase(), this.message, this.scale, this.displayDuration.toMillis(), this.fadeInDuration.toMillis(), this.fadeOutDuration.toMillis());
    }

    /**
     * Builds the current packet and sends it to all players required.
     * <p>
     * A good use case for this would be if all players online need
     * to see the same message, at the same time, and it won't be sent again.
     * Like an announcement for an objective in a mini-game.
     *
     * @param players All the {@link Player} that need to see the title.
     * @return The {@link LCPacketTitle} generated that can be used later if needed.
     */
    public LCPacketTitle sendAndBuild(@NotNull final Player... players) {
        final LCPacketTitle title = build();
        for (final Player player : players) {
            LunarClientAPI.getInstance().sendPacket(player, title);
        }
        return title;
    }
}
