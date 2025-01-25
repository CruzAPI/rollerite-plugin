package com.rollerite.player;

import com.rollerite.RolleritePlugin;
import com.rollerite.util.TeleportRequest;
import com.rollerite.i18n.TranslatableMessage;
import com.rollerite.sender.RolleriteCommandSender;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
@Setter
public class RolleritePlayer implements RolleriteCommandSender
{
	private final RolleritePlugin rolleritePlugin;
	
	@Delegate
	private final Player player;
	
	private final Map<UUID, TeleportRequest> teleportRequests = new HashMap<>();
	
	private boolean godMode;
	
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
	
	public boolean addTeleportRequest(TeleportRequest teleportRequest)
	{
		return teleportRequests.putIfAbsent(teleportRequest.getSender().getUniqueId(), teleportRequest) == null;
	}
	
	public boolean removeTeleportRequest(TeleportRequest teleportRequest)
	{
		return teleportRequests.remove(teleportRequest.getSender().getUniqueId(), teleportRequest);
	}
	
	public boolean removeTeleportRequest(RolleritePlayer rolleritePlayer)
	{
		return teleportRequests.remove(rolleritePlayer.getUniqueId()) != null;
	}
}
