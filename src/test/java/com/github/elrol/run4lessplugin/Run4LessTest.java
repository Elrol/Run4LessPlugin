package com.github.elrol.run4lessplugin;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class Run4LessTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(Run4LessPlugin.class);
		RuneLite.main(args);
	}
}