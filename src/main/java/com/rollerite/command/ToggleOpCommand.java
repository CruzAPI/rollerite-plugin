package com.rollerite.command;

import com.rollerite.RolleritePlugin;
import com.rollerite.player.RolleritePlayer;
import com.rollerite.sender.RolleriteCommandSender;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.rollerite.message.RolleriteMessage.*;

@RequiredArgsConstructor
public class ToggleOpCommand implements BasicCommand
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
			rolleritePlayer.setOp(!rolleritePlayer.isOp());
			rolleritePlayer.sendMessage(rolleritePlayer.isOp() ? COMMAND_TOGGLEOP_ENABLED : COMMAND_TOGGLEOP_DISABLED);
		}
		else
		{
			rolleritePlayer.sendMessage(COMMAND_TOGGLEOP_USAGE);
		}
	}
	
	@Override
	public boolean canUse(CommandSender sender)
	{
		return sender instanceof Player;
	}
}
