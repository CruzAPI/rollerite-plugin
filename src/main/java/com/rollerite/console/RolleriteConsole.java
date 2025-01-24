package com.rollerite.console;

import com.rollerite.RolleritePlugin;
import com.rollerite.sender.RolleriteCommandSender;
import net.kyori.adventure.text.Component;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Locale;

public class RolleriteConsole implements RolleriteCommandSender
{
	private final RolleritePlugin rolleritePlugin;
	private final ConsoleCommandSender consoleCommandSender;
	
	public RolleriteConsole(RolleritePlugin rolleritePlugin)
	{
		this.rolleritePlugin = rolleritePlugin;
		this.consoleCommandSender = rolleritePlugin.getServer().getConsoleSender();;
	}
	
	@Override
	public void sendMessage(Component component)
	{
		consoleCommandSender.sendMessage(component);
	}
	
	@Override
	public Locale getLocale()
	{
		return Locale.US;
	}
}
