package com.rollerite.sender;

import com.rollerite.RolleritePlugin;
import net.kyori.adventure.text.Component;

import java.util.Locale;

public record UnknownCommandSender(RolleritePlugin rolleritePlugin) implements RolleriteCommandSender
{
	@Override
	public void sendMessage(Component component)
	{
	
	}
	
	@Override
	public Locale getLocale()
	{
		throw new UnsupportedOperationException();
	}
}
