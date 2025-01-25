package com.rollerite.command;

import com.rollerite.RolleritePlugin;
import com.rollerite.player.RolleritePlayer;
import com.rollerite.sender.RolleriteCommandSender;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

import static com.rollerite.message.RolleriteMessage.COMMAND_TRASH_USAGE;
import static com.rollerite.message.RolleriteMessage.INVENTORY_TRASH_TITLE;

@RequiredArgsConstructor
public class TrashCommand implements BasicCommand
{
	private final RolleritePlugin rolleritePlugin;
	
	@Override
	public void execute(CommandSourceStack commandSourceStack, String[] args)
	{
		CommandSender commandSender = commandSourceStack.getSender();
		RolleriteCommandSender rolleriteCommandSender = rolleritePlugin.getCommandSenderManager().getCommandSender(commandSender);
		
		if(!(rolleriteCommandSender instanceof RolleritePlayer rolleritePlayer))
		{
			return;
		}
		
		if(args.length == 0)
		{
			Inventory trashInventory = rolleritePlayer.createInventory(9 * 4, INVENTORY_TRASH_TITLE);
			rolleritePlayer.getPlayer().openInventory(trashInventory);
		}
		else
		{
			rolleritePlayer.sendMessage(COMMAND_TRASH_USAGE);
		}
	}
	
	@Override
	public @NotNull String permission()
	{
		return "command.trash";
	}
	
	@Override
	public boolean canUse(CommandSender sender)
	{
		return sender instanceof Player && sender.hasPermission(permission());
	}
}
