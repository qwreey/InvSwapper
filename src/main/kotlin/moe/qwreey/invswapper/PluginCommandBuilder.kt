package moe.qwreey.invswapper

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import org.bukkit.Bukkit
import org.bukkit.permissions.Permission

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
                    .requires{ it.getSender().hasPermission("plugin.permission") }
                    .executes(::reload)
            )
            .then(
                Commands.literal("reset-settings")
                    .requires{ it.getSender().hasPermission("plugin.permission") }
                    .executes(::resetSettings)
            )
        ).build()
    }
}
