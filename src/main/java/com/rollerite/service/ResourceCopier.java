package com.rollerite.service;

import com.rollerite.RolleritePlugin;
import lombok.RequiredArgsConstructor;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

@RequiredArgsConstructor
public class ResourceCopier
{
	private final RolleritePlugin rolleritePlugin;
	
	public void copyResources(String resourceFolder, boolean replace)
	{
		copyResources(resourceFolder, rolleritePlugin.getDataFolder(), replace);
	}
	
	public void copyResources(String resourceFolder, File outputFolder, boolean replace)
	{
		if(!outputFolder.exists())
		{
			outputFolder.mkdirs();
		}
		
		try
		{
			URL jarUrl = rolleritePlugin.getClass().getProtectionDomain().getCodeSource().getLocation();
			String jarPath = jarUrl.getPath();
			
			try(JarFile jarFile = new JarFile(jarPath))
			{
				Enumeration<JarEntry> entries = jarFile.entries();
				
				while(entries.hasMoreElements())
				{
					JarEntry entry = entries.nextElement();
					String name = entry.getName();
					
					if(name.startsWith(resourceFolder + "/") && !entry.isDirectory())
					{
						File destinationFile = new File(outputFolder, name);
						
						if(destinationFile.exists() && !replace)
						{
							continue;
						}
						
						destinationFile.getParentFile().mkdirs();
						
						try(InputStream inputStream = rolleritePlugin.getClass().getClassLoader().getResourceAsStream(name))
						{
							if(inputStream != null)
							{
								copyFile(inputStream, destinationFile);
							}
						}
					}
				}
			}
		}
		catch(IOException e)
		{
			rolleritePlugin.getLogger().severe("Erro ao copiar recursos: " + e.getMessage());
		}
	}
	
	private void copyFile(InputStream input, File output)
	{
		try(OutputStream outputStream = new FileOutputStream(output))
		{
			byte[] buffer = new byte[1024];
			int bytesRead;
			
			while((bytesRead = input.read(buffer)) != -1)
			{
				outputStream.write(buffer, 0, bytesRead);
			}
		}
		catch(IOException e)
		{
			rolleritePlugin.getLogger().log(Level.SEVERE, "Failed to copy file " + output.getAbsolutePath(), e);
		}
	}
}
