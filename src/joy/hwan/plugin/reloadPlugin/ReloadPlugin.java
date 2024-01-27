package joy.hwan.plugin.reloadPlugin;

import java.io.File;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadPlugin extends BukkitRunnable implements Listener {

	private final Server server;
	private final HashMap<String, Long> pluginMetaInfoMap;

	private final String pluginDirectoryPath;

	ConsoleCommandSender consol = Bukkit.getConsoleSender();

	public ReloadPlugin(JavaPlugin plugin, HashMap<String, Long> pluginMetaInfoMap) {
		this.server = plugin.getServer();
		this.pluginMetaInfoMap = pluginMetaInfoMap;
		this.pluginDirectoryPath = plugin.getDataFolder().getParentFile().getAbsolutePath();
	}

	@Override
	public void run() {
		try {
			boolean reload = isReload();

			if (reload) {
				server.reload();

				consol.sendMessage(ChatColor.AQUA + "[플러그인 리로드 성공]");
			} else {
				consol.sendMessage(ChatColor.GREEN + "[플러그인 변경 사항이 없습니다]");
			}

		} catch (Exception error) {
			consol.sendMessage(ChatColor.RED + "[플러그인 리로드 실패]");
		}
	}

	public boolean isReload() {
		File[] pluginFiles = new File(pluginDirectoryPath).listFiles();

		boolean reload = false;

		Integer fileLnegth = 0;

		for (File file : pluginFiles) {
			String fileName = file.getName();

			if (fileName.matches(".*\\.jar$")) {
				fileLnegth += 1;

				try {
					long prevLastModified = pluginMetaInfoMap.get(fileName);

					if (prevLastModified != file.lastModified()) { // 파일 수정이 생겼을 경우
						return true;
					}
				} catch (NullPointerException error) { // 새로운 파일이 추가됐을 경우
					reload = true;
					break;
				}

			}

		}

		if (pluginMetaInfoMap.size() != fileLnegth) { // 기존 파일이 삭제됐을 경우
			return true;
		}

		return reload;
	}
}
