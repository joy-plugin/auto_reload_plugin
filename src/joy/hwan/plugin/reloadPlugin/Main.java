package joy.hwan.plugin.reloadPlugin;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
	ConsoleCommandSender consol = Bukkit.getConsoleSender();

	private HashMap<String, Long> pluginMetaInfoMap = new HashMap<String, Long>();

	String pluginDirectoryPath = getDataFolder().getParentFile().getAbsolutePath();

	// 1 tick = 1 / 20 seconds, so 20L = 1 second
	long second = 20l;
	long minute = second * 60;

	@Override
	public void onDisable() {
		// TODO Auto-generated method stub
		super.onDisable();
		consol.sendMessage(ChatColor.AQUA + "[플러그인 비활성화 중 입니다]");
	}

	@Override
	public void onEnable() {
		// TODO Auto-generated method stub
		super.onEnable();

		consol.sendMessage(ChatColor.AQUA + "[플러그인 활성화 중 입니다]");

		pluginMetaInfoMap = setPluginMetaInfo();

		ReloadPlugin reloadPlugin = new ReloadPlugin(this, pluginMetaInfoMap);

		reloadPlugin.runTaskTimer(this, minute, minute);

	}

	public HashMap<String, Long> setPluginMetaInfo() {
		HashMap<String, Long> pluginMetaInfoMap = new HashMap<String, Long>();

		File[] pluginFiles = new File(pluginDirectoryPath).listFiles();

		for (File file : pluginFiles) {
			String fileName = file.getName();

			if (fileName.matches(".*\\.jar$")) {
				pluginMetaInfoMap.put(fileName, file.lastModified());
			}
		}

		return pluginMetaInfoMap;
	}

}
