package com.rollerite.service;

import com.rollerite.RolleritePlugin;
import lombok.RequiredArgsConstructor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

@RequiredArgsConstructor
public class PropertiesLoader
{
	private final RolleritePlugin rolleritePlugin;
	
	public Properties loadProperties(String fileName) throws IOException
	{
		return loadProperties(new File(rolleritePlugin.getDataFolder(), fileName));
	}
	
	public Properties loadProperties(File file) throws IOException
	{
		Properties properties = new Properties();
		
		if(!file.exists())
		{
			throw new FileNotFoundException("File not found: " + file.getAbsolutePath());
		}
		
		try(FileInputStream input = new FileInputStream(file))
		{
			properties.load(input);
			rolleritePlugin.getLogger().info("Properties loaded: " + file.getAbsolutePath());
			
			return properties;
		}
	}
}
