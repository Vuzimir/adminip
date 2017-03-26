package vuzimir.bungee.adminip;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
//import java.util.concurrent.TimeUnit;

import com.huskehhh.mysql.mysql.MySQL;

import java.sql.Connection;
import java.sql.ResultSet;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;



public class main extends Plugin {
	
	
	private static main Mejin;
	public static main getMain() {
	    return Mejin;
	}
	 private String host, port2, database, username, password;
	 private int port;
	 Statement statement;
	 
	public Configuration config;
	public Configuration config2;
    public ConfigurationProvider configp;
    public File configfile;
    public static String lobby;
    private static String upit;
    static Connection c = null;
    public boolean configfil = false;
    
    
	@Override
	public void onEnable() {	
		getProxy().getPluginManager().registerListener(this, new events());
		
		Mejin = this;
//======================================COMMANDS====================================
		getProxy().getPluginManager().registerCommand(this, new addadminip());	
//======================================CONFIG====================================
		configfile = new File(ProxyServer.getInstance().getPluginsFolder()+ "/AdminIP/config.yml");
		
		if(configfile.exists()){
        }else{
            try {
                configfile.createNewFile();
                configfil = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		configp = ConfigurationProvider.getProvider(YamlConfiguration.class);
		try {
            config = configp.load(configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
		if(configfil == true) {
			config.set("settings.lobby", "HUB");
			config.set("mysql.host", "localhost");
			config.set("mysql.port", 3306);
			config.set("mysql.database", "minecraft");
			config.set("mysql.username", "root");
			config.set("mysql.password", "password");
			try {
				configp.save(config, configfile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		lobby = config.getString("settings.lobby"); //lobby
		host = config.getString("mysql.host");
		port = config.getInt("mysql.port");
		database = config.getString("mysql.database");
		username = config.getString("mysql.username");
		password = config.getString("mysql.password");
		port2 = String.valueOf(port);
		
//======================================MYSQL====================================
		MySQL MySQL = new MySQL(host, port2, database, username, password);
		try {
			c = MySQL.openConnection();
		} catch (ClassNotFoundException e) {


		} catch (SQLException e) {
			getLogger().info(ChatColor.RED + "Could not connect to mysql database, check your database connection!");
			ProxyServer.getInstance().stop();
		}
		
		upit = String.format("CREATE TABLE IF NOT EXISTS `admins` (`ID` int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,`uuid` varchar(256) NOT NULL,`name` varchar(64) NOT NULL,`loginip` varchar(64) NOT NULL) ENGINE=MyISAM DEFAULT CHARSET=latin1;");
		Statement statementtable;
		try {
			statementtable = c.createStatement();
			statementtable.executeUpdate(upit);
		} catch (SQLException e) {
			getLogger().info(ChatColor.RED + "Could not create table for database!");
			ProxyServer.getInstance().stop();
			e.printStackTrace();
		}
//==================================================================================
    }
	
	
	@Override
	public void onDisable() {
		
	}
	@SuppressWarnings("unchecked")
	public void AddIp(String server, String where) {	
		File configfile = new File(getProxy().getPluginsFolder().getParentFile(), "config.yml");
        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<LinkedHashMap<String, Object>> list = (ArrayList<LinkedHashMap<String, Object>>) config.getList("listeners");
		ArrayList<LinkedHashMap<String, Object>> listeners = list;
        LinkedHashMap<String, Object> listener = listeners.get(0);
       
        // Changing the forced hosts
        LinkedHashMap<String, String> linkedHashMap = (LinkedHashMap<String, String>) listener.get("forced_hosts");
		LinkedHashMap<String, String> hosts = linkedHashMap;
        hosts.put(server, where); // We add a new forced host
        listener.put("forced_hosts", hosts); // We save the forced hosts
           
        // The saving part
        listeners.set(0, listener);
        config.set("listeners", listeners);
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, configfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
       
        // The reloading part
        /*getProxy().getScheduler().schedule(this, new Runnable(){
            @Override
            public void run() {
                getProxy().getPluginManager().dispatchCommand(getProxy().getConsole(), "greload");
            }
        }, 3L, TimeUnit.SECONDS);*/
	}
	

	public void AddUser(String uuid, String name, String ipadress) throws SQLException {
		upit = String.format("INSERT INTO admins (`uuid`, `name`, `loginip`) VALUES ('%s', '%s', '%s');", uuid, name, ipadress);
		Statement statement = c.createStatement();
		statement.executeUpdate(upit);
	}
	
	public String CheckUser(String uuid, String name) throws SQLException {
		String logip = null;
		Statement statement = c.createStatement();
		ResultSet res = statement.executeQuery("SELECT loginip FROM admins WHERE uuid = '" + uuid + "' AND name = '" + name + "';");
		res.next();
		if(res.getString("loginip") != null) {
			logip = res.getString("loginip");
		}	
		return logip;
	}
}
