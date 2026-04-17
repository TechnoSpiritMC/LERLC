package org.leaf.api.command.special;

import org.leaf.api.command.Command;
import org.leaf.api.internal.AbstractPlayer;
import org.leaf.api.internal.command.CommandName;

import java.time.Instant;

/// Class encapsulating the weather ERLC Command.
public class WeatherCommand extends Command {
    private final String weather;

    public WeatherCommand(String raw,
                          AbstractPlayer sender,
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
