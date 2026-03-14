package org.leaf.api.internal.command;

import org.leaf.Main;
import org.leaf.api.command.*;
import org.leaf.api.command.special.*;
import org.leaf.roblox.RobloxPlayer;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static org.leaf.api.internal.command.CommandName.resolveCommandName;

public class CommandFactory {

    public static Command parse(String rawCommand,
                                RobloxPlayer sender,
                                Instant timestamp) {

        String[] parts = rawCommand.split("\\s+");
        String commandName = parts[0].substring(1).toLowerCase();

        CommandName name = resolveCommandName(commandName);

        switch (name) {

            case Fixcar:
            case Killlogs:
            case ToVehicle:
            case Bans:
            case Commands:
            case Logs:
            case Admins:
            case Mods:
            case StopFire:
                return buildGenericCommand(rawCommand, commandName, name, sender, timestamp);

            case Heal:
            case To:
            case Bring:
            case Admin:
                return buildSingleTarget(rawCommand, commandName, name, sender, timestamp, parts);

            case Weather:
                return new WeatherCommand(rawCommand, sender, name, commandName, timestamp, parts[1]);

            case Message:
            case Hint:
                return new MessageCommand(rawCommand, sender, name, commandName, timestamp, rawCommand.substring(rawCommand.indexOf(" ") + 1));

            case StartFire:
            case StartNearFire:
                return new FireCommand(rawCommand, sender, name, commandName, timestamp, parts[1]);

            case Time: {
                int hours = 0;
                try {
                    hours = Integer.parseInt(parts[1]);
                } catch (NumberFormatException e) {
                    Main.logger.warning("Invalid time of day specified for time command. Got: " + parts[1] + ". Defaulting to 0.");
                }

                return new TimeCommand(rawCommand, sender, name, commandName, timestamp, hours);
            }

            case PeaceTimer:
            case PriorityTimer: {
                long seconds = 0;
                try {
                    seconds = Long.parseLong(parts[1]);
                } catch (NumberFormatException e) {
                    Main.logger.warning("Invalid number of seconds specified for timer command. Got: " + parts[1] + ". Defaulting to 0.");
                }

                return new DurationCommand(rawCommand, sender, name, commandName, timestamp, Duration.ofSeconds(seconds));
            }

            case Tp:
                return buildOptionalDualTarget(rawCommand, commandName, name, sender, timestamp, parts);

            case Kill:
            case Kick:
            case Ban:
            case Unadmin:
            case Unmod:
            case Refresh:
            case Wanted:
            case Jail:
            case Respawn:
                return buildSingleTargetWithOptionalReason(rawCommand, commandName, name, sender, timestamp, parts);

            case Pm:
                return buildPmCommand(rawCommand, commandName, name, sender, timestamp, parts);

            case Custom:
                return new CustomCommand(rawCommand, sender, name, commandName, List.of(parts).subList(1, parts.length));

            default:
                return new UncategorizedCommand(rawCommand, name, commandName, sender, timestamp);
        }
    }

    private static Command buildSingleTarget(String raw,
                                             String name,
                                             CommandName commandName,
                                             RobloxPlayer sender,
                                             Instant timestamp,
                                             String[] parts) {

        if (parts.length < 2)
            throw new IllegalArgumentException("Missing target");

        RobloxPlayer target = RobloxPlayer.parse(parts[1]);

        return new SingleTargetCommand(
                raw,
                sender,
                commandName,
                name,
                timestamp,
                target
        );
    }

    private static Command buildGenericCommand(String raw,
                                             String name,
                                             CommandName commandName,
                                             RobloxPlayer sender,
                                             Instant timestamp) {

        return new GenericCommand(raw, sender, commandName, name, timestamp);
    }

    private static Command buildOptionalDualTarget(String raw,
                                                   String name,
                                                   CommandName commandName,
                                                   RobloxPlayer sender,
                                                   Instant timestamp,
                                                   String[] parts) {

        if (parts.length <= 2) {
            return buildSingleTarget(raw, name, commandName, sender, timestamp, parts);
        }

        return new DualTargetCommand(raw, sender, commandName, name, timestamp, new RobloxPlayer(parts[1]), new RobloxPlayer(parts[2]));
    }

    private static Command buildSingleTargetWithOptionalReason(String raw,
                                                              String name,
                                                              CommandName commandName,
                                                              RobloxPlayer sender,
                                                              Instant timestamp,
                                                              String[] parts) {

        if (parts.length <= 2) {
            return buildSingleTarget(raw, name, commandName, sender, timestamp, parts);
        }

        return new SingleTargetWithMsgCmd(raw, sender, commandName, name, timestamp, new RobloxPlayer(parts[1]), raw.substring(raw.indexOf(" ") + 1));
    }

    private static Command buildPmCommand(String raw,
                                          String name,
                                          CommandName commandName,
                                          RobloxPlayer sender,
                                          Instant timestamp,
                                          String[] parts) {

        return new PmCommand(raw, sender, timestamp);
    }
}