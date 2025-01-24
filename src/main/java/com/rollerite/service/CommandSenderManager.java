package com.rollerite.service;

import com.rollerite.RolleritePlugin;
import com.rollerite.sender.RolleriteCommandSender;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class CommandSenderManager
{
	private final RolleritePlugin rolleritePlugin;
	
	public @NotNull RolleriteCommandSender getCommandSender(CommandSender commandSender)
	{
		if(commandSender instanceof ConsoleCommandSender)
		{
			return rolleritePlugin.getRolleriteConsole();
		}
		else if(commandSender instanceof Player player)
		{
			return rolleritePlugin.getPlayerListener().get(player);
		}
		else
		{
			return rolleritePlugin.getUnknownCommandSender();
		}
	}
}
