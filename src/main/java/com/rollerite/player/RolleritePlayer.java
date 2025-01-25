package com.rollerite.player;

import com.rollerite.RolleritePlugin;
import com.rollerite.i18n.Messageable;
import com.rollerite.i18n.TranslatableMessage;
import com.rollerite.sender.RolleriteCommandSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Locale;

@RequiredArgsConstructor
@Getter
@Setter
public class RolleritePlayer implements RolleriteCommandSender
{
	private final RolleritePlugin rolleritePlugin;
	private final Player player;
	
	private boolean godMode;
	
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
	
	public <C> Inventory createInventory(int size, TranslatableMessage<C> translatableMessage)
	{
		return createInventory(size, translatableMessage, (C) null);
	}
	
	public <C> Inventory createInventory(int size, TranslatableMessage<C> translatableMessage, C context)
	{
		return rolleritePlugin.getServer().createInventory(player, size, translatableMessage.translate(getLocale(), context));
	}
}
