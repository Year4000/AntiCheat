/*
 * AntiCheat for Bukkit.
 * Copyright (C) 2012 H31IX http://h31ix.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package net.h31ix.anticheat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {
    private File configFile = new File(Anticheat.getPlugin().getDataFolder()+"/config.yml");
    private File levelFile = new File(Anticheat.getPlugin().getDataFolder()+"/data/level.yml");
    private File bukkitFile = new File("bukkit.yml");
    private FileConfiguration config;
    private FileConfiguration level;
    private FileConfiguration bukkit;
    private boolean logConsole;
    private boolean logXRay;
    private boolean alertXRay;
    private boolean autoUpdate;
    private boolean verboseStartup;
    private String updateFolder;
    private List<String> worlds = new ArrayList<String>();
    
    public Configuration()
    {
        load();
    }
    
    private void save()
    {
        try 
        {
            config.save(configFile);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean logConsole()
    {
        return logConsole;
    }
    
    public boolean logXRay()
    {
        return logXRay;
    }
    
    public boolean alertXRay()
    {
        return alertXRay;
    }    
    
    public boolean autoUpdate()
    {
        return autoUpdate;
    }  
    
    public boolean verboseStartup()
    {
        return verboseStartup;
    } 
    
    public String updateFolder()
    {
        return updateFolder;
    }     
    
    public final void load()
    {
        Anticheat.checkConfig();
        config = YamlConfiguration.loadConfiguration(configFile);
        level = YamlConfiguration.loadConfiguration(levelFile);
        bukkit = YamlConfiguration.loadConfiguration(bukkitFile);
        updateFolder = bukkit.getString("settings.update-folder");
        if(config.getString("Logging.Log to console") != null)
        {
            boolean b = config.getBoolean("Logging.Log to console");
            config.set("Logging", null);
            config.set("System.Log to console",b);
            save();
        }          
        logConsole = config.getBoolean("System.Log to console");
        if(config.getList("Enable in") == null)
        {
            List<String> w = new ArrayList<String>();
            for(World world : Bukkit.getServer().getWorlds())
            {
                w.add(world.getName());
            }
            config.set("Enable in", w);
            save();
        }
        worlds = (List<String>)(config.getList("Enable in")); 
        if(config.getString("XRay.Log xray stats") == null)
        {
            config.set("XRay.Log xray stats", true);
            save();
        }   
        logXRay = config.getBoolean("XRay.Log xray stats"); 
        if(config.getString("System.Auto update") == null)
        {
            config.set("System.Auto update", true);
            save();
        }        
        autoUpdate = config.getBoolean("System.Auto update");
        if(config.getString("System.Verbose startup") == null)
        {
            config.set("System.Verbose startup", false);
            save();
        }        
        verboseStartup = config.getBoolean("System.Verbose startup");     
        if(config.getString("XRay.Alert when xray is found") == null)
        {
            config.set("XRay.Alert when xray is found", false);
            save();
        }        
        alertXRay = config.getBoolean("XRay.Alert when xray is found");         
    }
    
    public String getResult(String event)
    {
        return config.getString("Events.Level "+event);
    }
    
    public void setLog(boolean b)
    {
        config.set("System.Log to console", b);
        this.logConsole = b;
        try 
        {
            config.save(configFile);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public List<String> getWorlds()
    {
        return worlds;
    }
    
    public File getConfigFile()
    {
        return configFile;
    }
    
    public File getLevelFile()
    {
        return levelFile;
    }
    
    public boolean checkInWorld(World world)
    {
        return worlds.contains(world.getName());
    }
    
    public int getLevel(String player)
    {
        int x = 0;
        if(level.getString(player) != null)
        {
            x = level.getInt(player);
        }
        return x;
    }
    
    public void saveLevel(String player, int x)
    {
        level.set(player,x);
    }
    
    public void saveLevels()
    {
        try 
        {
            level.save(levelFile);
        } 
        catch (IOException ex) 
        {
            Logger.getLogger(Configuration.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
}