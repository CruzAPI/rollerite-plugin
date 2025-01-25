package com.rollerite.message;

import com.google.common.base.Preconditions;
import com.rollerite.RolleritePlugin;
import com.rollerite.i18n.MessageBundle;
import com.rollerite.i18n.TranslatableMessage;
import com.rollerite.type.Gamemode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Locale;
import java.util.function.BiFunction;

import static net.kyori.adventure.text.minimessage.tag.resolver.Placeholder.*;

@RequiredArgsConstructor
@Getter
public enum RolleriteMessage implements TranslatableMessage<RolleriteMessage.Context>
{
	PLAYER_NOT_FOUND("player-not-found"),
	GAME_MODE_NOT_FOUND("game-mode-not-found"),
	
	COMMAND_FIX_USAGE("command.fix.usage"),
	COMMAND_FIX_HOLD_TOOL("command.fix.hold-tool"),
	COMMAND_FIX_ONLY_TOOLS("command.fix.only-tools"),
	COMMAND_FIX_CANNOT_REPAIR("command.fix.cannot-repair"),
	COMMAND_FIX_ALREADY_REPAIRED("command.fix.already-repaired"),
	COMMAND_FIX_REPAIRED("command.fix.repaired"),
	
	COMMAND_ENDERCHEST_USAGE("command.enderchest.usage"),
	
	COMMAND_GAMEMODE_USAGE("command.gamemode.usage"),
	COMMAND_GAMEMODE_CONSOLE_USAGE("command.gamemode.console-usage"),
	COMMAND_GAMEMODE_CHANGED("command.gamemode.changed", (locale, context) -> new TagResolver[]
	{
		component("gamemode", context.getGamemode().getStylishedTranslatableComponent()),
	}),
	COMMAND_GAMEMODE_TARGET_CHANGED("command.gamemode.target-changed", (locale, context) -> new TagResolver[]
	{
		component("player", context.getPlayer().displayName()),
		component("gamemode", context.getGamemode().getStylishedTranslatableComponent()),
	}),
	COMMAND_GAMEMODE_ALREADY("command.gamemode.already"),
	COMMAND_GAMEMODE_TARGET_ALREADY("command.gamemode.target-already", (locale, context) -> new TagResolver[]
	{
		component("player", context.getPlayer().displayName()),
	}),
	
	COMMAND_GOD_USAGE("command.god.usage"),
	COMMAND_GOD_CONSOLE_USAGE("command.god.console-usage"),
	COMMAND_GOD_ENABLE("command.god.enable"),
	COMMAND_GOD_DISABLE("command.god.disable"),
	COMMAND_GOD_TARGET_ENABLE("command.god.target-enable", (locale, context) -> new TagResolver[]
	{
		component("player", context.getPlayer().displayName()),
	}),
	COMMAND_GOD_TARGET_DISABLE("command.god.target-disable", (locale, context) -> new TagResolver[]
	{
		component("player", context.getPlayer().displayName()),
	}),
	
	COMMAND_OPENINV_USAGE("command.openinv.usage"),
	
	COMMAND_TPACCEPT_YOU_ACCEPTED("command.tpaccept.you-accepted", (locale, context) -> new TagResolver[]
	{
		component("player", context.getSenderPlayer().displayName()),
	}),
	COMMAND_TPACCEPT_TELEPORT_FAILED("command.tpaccept.teleport-failed"),
	COMMAND_TPACCEPT_USAGE("command.tpaccept.usage"),
	
	COMMAND_TPA_DENIED("command.tpa.denied", (locale, context) -> new TagResolver[]
	{
		component("player", context.getReceiverPlayer().displayName()),
	}),
	COMMAND_TPA_NO_REQUEST("command.tpa.no-request", (locale, context) -> new TagResolver[]
	{
		component("player", context.getSenderPlayer().displayName()),
	}),
	COMMAND_TPA_REQUEST_RECEIVED("command.tpa.request-received", (locale, context) -> new TagResolver[]
	{
		component("player", context.getSenderPlayer().displayName()),
		parsed("player_name", context.getSenderPlayer().getName()),
	}),
	COMMAND_TPA_REQUEST_SENT("command.tpa.request-sent", (locale, context) -> new TagResolver[]
	{
		component("player", context.getReceiverPlayer().displayName()),
	}),
	COMMAND_TPA_REQUEST_ALREADY_PENDING("command.tpa.request-already-pending", (locale, context) -> new TagResolver[]
	{
		component("player", context.getReceiverPlayer().displayName()),
	}),
	COMMAND_TPA_SELF_REQUEST("command.tpa.self-request"),
	COMMAND_TPA_REQUESTER_EXPIRED("command.tpa.requester.expired", (locale, context) -> new TagResolver[]
	{
		component("player", context.getReceiverPlayer().displayName()),
	}),
	COMMAND_TPA_RECEIVER_EXPIRED("command.tpa.receiver.expired", (locale, context) -> new TagResolver[]
	{
		component("player", context.getSenderPlayer().displayName()),
	}),
	COMMAND_TPA_ACCEPTED("command.tpa.accepted", (locale, context) -> new TagResolver[]
	{
		component("player", context.getReceiverPlayer().displayName()),
	}),
	COMMAND_TPA_TELEPORT_FAILED("command.tpa.teleport-failed", (locale, context) -> new TagResolver[]
	{
		component("player", context.getReceiverPlayer().displayName()),
	}),
	COMMAND_TPA_USAGE("command.tpa.usage"),
	
	COMMAND_TPDENY_USAGE("command.tpdeny.usage"),
	COMMAND_TPDENY_YOU_DENIED("command.tpdeny.you-denied", (locale, context) -> new TagResolver[]
	{
		component("player", context.getSenderPlayer().displayName()),
	}),
	
	COMMAND_TRASH_USAGE("command.trash.usage"),
	
	INVENTORY_TRASH_TITLE("inventory.trash.title"),
	;
	
	private final String key;
	private final BiFunction<Locale, Context, TagResolver[]> translateArgumentsBiFunction;
	
	RolleriteMessage(String key)
	{
		this(key, (locale, context) -> new TagResolver[0]);
	}
	
	@Override
	public MessageBundle getMessageBundle(Locale locale)
	{
		return RolleritePlugin.getInstance().getMessageBundleLoader().getBundle("message/message", locale);
	}
	
	@Override
	public TagResolver[] translateArguments(Locale locale, Context context)
	{
		return translateArgumentsBiFunction.apply(locale, context);
	}
	
	@Override
	public Plugin getPlugin()
	{
		return RolleritePlugin.getInstance();
	}
	
	@Override
	public Context newContext()
	{
		return new Context();
	}
	
	public static class Context
	{
		private Player player;
		private Player senderPlayer;
		private Player receiverPlayer;
		private Gamemode gamemode;
		
		public Context player(Player player)
		{
			this.player = player;
			return this;
		}
		
		public Context senderPlayer(Player senderPlayer)
		{
			this.senderPlayer = senderPlayer;
			return this;
		}
		
		public Context receiverPlayer(Player receiverPlayer)
		{
			this.receiverPlayer = receiverPlayer;
			return this;
		}
		
		public Context gamemode(Gamemode gamemode)
		{
			this.gamemode = gamemode;
			return this;
		}
		
		public Player getPlayer()
		{
			return Preconditions.checkNotNull(player, "player is null");
		}
		
		public Player getSenderPlayer()
		{
			return Preconditions.checkNotNull(senderPlayer, "senderPlayer is null");
		}
		
		public Player getReceiverPlayer()
		{
			return Preconditions.checkNotNull(receiverPlayer, "receiverPlayer is null");
		}
		
		public Gamemode getGamemode()
		{
			return Preconditions.checkNotNull(gamemode, "gamemode is null");
		}
	}
}
