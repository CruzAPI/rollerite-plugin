package com.rollerite.command;

import com.rollerite.RolleritePlugin;
import com.rollerite.message.RolleriteMessage;
import com.rollerite.player.RolleritePlayer;
import com.rollerite.sender.RolleriteCommandSender;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static com.rollerite.message.RolleriteMessage.*;

@RequiredArgsConstructor
public class GodCommand implements BasicCommand
{
	private final RolleritePlugin rolleritePlugin;
	
	@Override
	public Collection<String> suggest(CommandSourceStack commandSourceStack, String[] args)
	{
		List<String> suggestions = new ArrayList<>();
		
		if(args.length == 0 || args.length == 1)
		{
			String arg0 = args.length == 0 ? "" : args[0];
			
			for(Player player : rolleritePlugin.getServer().getOnlinePlayers())
			{
				if(player.getName().toLowerCase().startsWith(arg0.toLowerCase()))
				{
					suggestions.add(player.getName());
				}
			}
			
			return suggestions;
		}
		
		return Collections.emptyList();
	}
	
	@Override
	public void execute(CommandSourceStack commandSourceStack, String[] args)
	{
		CommandSender commandSender = commandSourceStack.getSender();
		RolleriteCommandSender rolleriteCommandSender = rolleritePlugin.getCommandSenderManager().getCommandSender(commandSender);
		
		if(args.length == 0 && rolleriteCommandSender instanceof RolleritePlayer rolleritePlayer
				|| args.length == 1)
		{
			Context ctx = new Context();
			
			Player target = args.length == 0
					? ((RolleritePlayer) rolleriteCommandSender).getPlayer()
					: rolleritePlugin.getServer().getPlayerExact(args[0]);
			
			ctx.player(target);
			
			if(target == null)
			{
				rolleriteCommandSender.sendMessage(PLAYER_NOT_FOUND, ctx);
				return;
			}
			
			RolleritePlayer targetRolleritePlayer = rolleritePlugin.getPlayerListener().get(target);
			
			targetRolleritePlayer.setGodMode(!targetRolleritePlayer.isGodMode());
			boolean godMode = targetRolleritePlayer.isGodMode();
			
			if(commandSender == target)
			{
				rolleriteCommandSender.sendMessage(godMode ? COMMAND_GOD_ENABLE : COMMAND_GOD_DISABLE, ctx);
			}
			else
			{
				rolleriteCommandSender.sendMessage(godMode ? COMMAND_GOD_TARGET_ENABLE : COMMAND_GOD_TARGET_DISABLE, ctx);
			}
		}
		else
		{
			RolleriteMessage usageMessage = rolleriteCommandSender instanceof RolleritePlayer
					? COMMAND_GOD_USAGE
					: COMMAND_GOD_CONSOLE_USAGE;
			rolleriteCommandSender.sendMessage(usageMessage);
		}
	}
	
	@Override
	public @Nullable String permission()
	{
		return "command.gamemode";
	}
}
