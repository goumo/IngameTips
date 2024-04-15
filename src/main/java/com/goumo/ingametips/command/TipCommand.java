package com.goumo.ingametips.command;

import com.goumo.ingametips.network.Networking;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public class TipCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
            Commands.literal("ingametips").requires((c) -> c.hasPermission(2)).then(
                Commands.literal("add").then(
                    Commands.argument("targets", EntityArgument.players()).then(
                    Commands.argument("ID", StringArgumentType.string())
                        .executes((a) -> {
                            String ID = a.getArgument("ID", String.class);
                            int i = 0;

                            for(ServerPlayer sp : EntityArgument.getPlayers(a, "targets")) {
                                Networking.send(sp, ID);
                                i++;
                            }

                            return i;
                        }
                    ))).then(
                    Commands.literal("custom").then(
                        Commands.argument("targets", EntityArgument.players()).then(
                        Commands.argument("title", StringArgumentType.string()).then(
                        Commands.argument("content", StringArgumentType.string()).then(
                        Commands.argument("visible_time", IntegerArgumentType.integer()).then(
                        Commands.argument("history", BoolArgumentType.bool())
                            .executes((c) -> {
                                String title = c.getArgument("title", String.class);
                                String content = c.getArgument("content", String.class);
                                Integer visibleTime = c.getArgument("visible_time", Integer.class);
                                boolean history = c.getArgument("history", Boolean.class);


                                int i = 0;
                                for(ServerPlayer sp : EntityArgument.getPlayers(c, "targets")) {
                                    Networking.sendCustom(sp, title, content, visibleTime, history);
                                    i++;
                                }

                                return i;
                            }
        )))))))));
    }
}
