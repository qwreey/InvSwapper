package moe.qwreey.invswapper

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands

class PluginCommandBuilder(
    plugin: Invswapper
): Invswapper.Companion.PluginAssessor(plugin) {
    fun reload(ctx: CommandContext<CommandSourceStack>): Int {
        plugin.reloadConfig()
        configReader.reload(configFile)
        return Command.SINGLE_SUCCESS
    }
    fun resetSettings(ctx: CommandContext<CommandSourceStack>): Int {
        plugin.saveResource("config.yml", true)
        plugin.reloadConfig()
        configReader.reload(configFile)
        return Command.SINGLE_SUCCESS
    }
    fun build(): LiteralCommandNode<CommandSourceStack> {
        return (Commands.literal("invswapper")
            .then(
                Commands.literal("reload")
                    .executes(::reload)
            )
            .then(
                Commands.literal("reset-settings")
                    .executes(::resetSettings)
            )
        ).build()
    }
}
