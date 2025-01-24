package com.rollerite.command;

import com.rollerite.RolleritePlugin;
import com.rollerite.player.RolleritePlayer;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

import static com.rollerite.message.RolleriteMessage.HELLO_PLAYER;
import static com.rollerite.message.RolleriteMessage.HELLO_WORLD;

@RequiredArgsConstructor
public class TestCommand implements BasicCommand
{
	private final RolleritePlugin rolleritePlugin;
	
	@Override
	public void execute(CommandSourceStack commandSourceStack, String[] args)
	{
		if(!(commandSourceStack.getSender() instanceof Player player))
		{
			return;
		}
		
		RolleritePlayer rolleritePlayer = rolleritePlugin.getPlayerListener().get(player);
		
		rolleritePlayer.sendMessage(HELLO_WORLD);
		rolleritePlayer.sendMessage(HELLO_PLAYER.withContext(ctx -> ctx.player(player)));
	}
}
