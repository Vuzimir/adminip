package vuzimir.bungee.adminip;

import java.sql.SQLException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;


public class addadminip extends Command {

	public addadminip() {
		super("addadminip", "vuzimir.commands.addadminip");
	}
	@Override
	public void execute(CommandSender sender, String[] args) {
		if(args.length < 2) {
			sender.sendMessage(new TextComponent(ChatColor.WHITE + "Use: " + ChatColor.BLUE + "/addadminip <playername [CASE SENSITIVY]> <server prefix>"));
			return;
		}
		
		ProxiedPlayer p = ProxyServer.getInstance().getPlayer(args[0]);

		if(p == null) {
			sender.sendMessage(new TextComponent(ChatColor.RED + "Player not online!"));
			return;
		}
		String name = p.toString();
		String uuid = p.getUUID();
		StringBuilder ajpi = new StringBuilder();
		for(int i = 1; i < args.length; i++) {
			ajpi.append(args[i]).append("");
		}
		String ajpiadd = ajpi.toString();
		main.getMain().AddIp(ajpiadd,main.lobby);
		
		try {
			main.getMain().AddUser(uuid,name,ajpiadd);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		sender.sendMessage(new TextComponent(ChatColor.BLUE + "You added a connection ip: " + ChatColor.YELLOW + ajpi + ChatColor.BLUE +" for player: " + ChatColor.YELLOW + name));
		sender.sendMessage(new TextComponent(ChatColor.RED + "NOTE: CHANGES WILL BE APPLYED ON BUNGEECORD RESTART!"));
	}
}