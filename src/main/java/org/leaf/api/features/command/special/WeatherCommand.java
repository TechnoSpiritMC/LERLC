package org.leaf.api.features.command.special;

import org.leaf.api.features.command.Command;
import org.leaf.api.features.command.CommandName;
import org.leaf.roblox.RobloxPlayer;

import java.time.Instant;

/// Class encapsulating the weather ERLC Command.
public class WeatherCommand extends Command {
    private final String weather;

    public WeatherCommand(String raw,
                        RobloxPlayer sender,
                        CommandName commandName,
                        String trueName,
                        Instant timestamp,
                        String weather) {

        super(raw, trueName, commandName, sender, timestamp);
        this.weather = weather;
    }

    /// Returns the weather set.
    public String getWeather() {
        return weather;
    }
}
