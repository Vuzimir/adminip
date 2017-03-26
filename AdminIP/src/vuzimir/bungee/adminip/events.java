package vuzimir.bungee.adminip;

import java.sql.SQLException;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class events implements Listener {

    @EventHandler
    public void onLogin(PostLoginEvent event) {
    	ProxiedPlayer igr = event.getPlayer();
    	String uuid = igr.getUUID();
    	String name = igr.getName().toString();
    	String result = null;
    	String conhost = event.getPlayer().getPendingConnection().getVirtualHost().getHostString();
    	
    	try {
    		result = main.getMain().CheckUser(uuid,name);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	if(result != null) {
    		if(result.equals(conhost)) {
    			
    		}
    		else {
    			igr.disconnect(new TextComponent("Kicked, you need to use Admin IP adress!"));
    		}
    		
    	}
    }

}
