package com.rollerite.player;

import com.rollerite.RolleritePlugin;
import com.rollerite.i18n.Messageable;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.Locale;

@RequiredArgsConstructor
public class RolleritePlayer implements Messageable
{
	private final RolleritePlugin rolleritePlugin;
	private final Player player;
	
	@Override
	public void sendMessage(Component component)
	{
		player.sendMessage(component);
	}
	
	@Override
	public Locale getLocale()
	{
		return player.locale();
	}
}
