package com.rollerite.command;

import com.rollerite.RolleritePlugin;
import com.rollerite.player.RolleritePlayer;
import com.rollerite.sender.RolleriteCommandSender;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import org.bukkit.Tag;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import static com.rollerite.message.RolleriteMessage.*;

@RequiredArgsConstructor
public class FixCommand implements BasicCommand
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
			Player player = rolleritePlayer.getPlayer();
			ItemStack itemInHand = player.getEquipment().getItemInMainHand();
			
			if(itemInHand.isEmpty())
			{
				rolleritePlayer.sendMessage(COMMAND_FIX_HOLD_TOOL);
				return;
			}
			
			ItemMeta meta = itemInHand.getItemMeta();
			
			if(!(meta instanceof Damageable damageable))
			{
				rolleritePlayer.sendMessage(COMMAND_FIX_CANNOT_REPAIR);
				return;
			}
			
			if(!isTool(itemInHand))
			{
				rolleritePlayer.sendMessage(COMMAND_FIX_ONLY_TOOLS);
				return;
			}
			
			if(!damageable.hasDamage())
			{
				rolleritePlayer.sendMessage(COMMAND_FIX_ALREADY_REPAIRED);
				return;
			}
			
			damageable.resetDamage();
			itemInHand.setItemMeta(damageable);
			
			rolleritePlayer.sendMessage(COMMAND_FIX_REPAIRED);
		}
		else
		{
			rolleritePlayer.sendMessage(COMMAND_FIX_USAGE);
		}
	}
	
	@Override
	public @NotNull String permission()
	{
		return "command.fix";
	}
	
	@Override
	public boolean canUse(CommandSender sender)
	{
		return sender instanceof Player && sender.hasPermission(permission());
	}
	
	private boolean isTool(ItemStack item)
	{
		return item != null && Tag.ITEMS_BREAKS_DECORATED_POTS.isTagged(item.getType());
	}
}
