package com.rollerite;

import com.rollerite.command.TestCommand;
import com.rollerite.listener.PlayerListener;
import com.rollerite.service.MessageBundleLoader;
import com.rollerite.service.PropertiesLoader;
import com.rollerite.service.ResourceCopier;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.Collection;

@Getter
public class RolleritePlugin extends JavaPlugin
{
	@Getter
	private static RolleritePlugin instance;
	
	private PlayerListener playerListener;
	
	private MessageBundleLoader messageBundleLoader;
	private PropertiesLoader propertiesLoader;
	private ResourceCopier resourceCopier;
	
	@Override
	public void onEnable()
	{
		instance = this;
		
		messageBundleLoader = new MessageBundleLoader(this);
		propertiesLoader = new PropertiesLoader(this);
		resourceCopier = new ResourceCopier(this);
		
		resourceCopier.copyResources("message", false);
		
		registerCommands();
		registerListeners();
	}
	
	private void registerCommands()
	{
		registerBasicCommand("test", new TestCommand(this));
	}
	
	private void registerBasicCommand(String label, BasicCommand basicCommand, String... aliases)
	{
		registerBasicCommand(label, Arrays.asList(aliases), basicCommand);
	}
	
	private void registerBasicCommand(String label, Collection<String> aliases, BasicCommand basicCommand)
	{
		LifecycleEventManager<Plugin> manager = this.getLifecycleManager();
		
		manager.registerEventHandler(LifecycleEvents.COMMANDS, event ->
		{
			event.registrar().register(label, aliases, basicCommand);
		});
	}
	
	private void registerListeners()
	{
		registerListener(playerListener = new PlayerListener(this));
	}
	
	private void registerListener(Listener listener)
	{
		getServer().getPluginManager().registerEvents(listener, this);
	}
}