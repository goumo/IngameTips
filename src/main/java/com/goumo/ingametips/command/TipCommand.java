package com.goumo.ingametips.command;

import com.goumo.ingametips.network.Networking;
import com.mojang.brigadier.CommandDispatcher;
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
                Commands.argument("name", StringArgumentType.string())
                        .executes((a) -> {
                            String ID = a.getArgument("name", String.class);
                            int i = 0;

                            for(ServerPlayer sp : EntityArgument.getPlayers(a, "targets")) {
                                Networking.send(sp, ID);
                                i++;
                            }

                            return i;
                        }
                ))))
        );
    }
}
