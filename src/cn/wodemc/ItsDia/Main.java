package cn.wodemc.ItsDia;

import org.bukkit.plugin.java.*;
import java.util.*;
import org.bukkit.plugin.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.event.block.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import java.io.*;

public class Main extends JavaPlugin implements Listener
{
    public static long time;
    public static int interval;
    
    static {
        Main.time = new Date().getTime() - 1000000L;
        Main.interval = 0;
    }
    
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)this);
        this.CreateConfig();
        Main.interval = this.getConfig().getInt("time");
    }
    
    @EventHandler
    public void onPlayerDropItemEvent(final PlayerDropItemEvent event) {
        if (event.getPlayer().isOp() && event.getItemDrop().getItemStack().getType().toString().equals("EXTRABOTANY_GAIAWISE")) {
            Main.time = new Date().getTime() - 10000000L;
        }
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onPlayerInteractEvent(final PlayerInteractEvent event) {
        final Player p = event.getPlayer();
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && p.isSneaking()) {
            final ItemStack item = p.getItemInHand();
            if (item == null) {
                return;
            }
            final String type = item.getType().toString();
            final int dur = item.getDurability();
            if ((type.equals("BOTANIA_MANARESOURCE") && dur == 14) || (type.equals("BOTANIA_MANARESOURCE") && dur == 4) || (type.equals("EXTRABOTANY_GAIAWISE") && dur == 0)) {
                final long jg = (new Date().getTime() - Main.time) / 1000L;
                if (jg >= Main.interval) {
                    Main.time = new Date().getTime();
                }
                else {
                    p.sendTitle(ChatColor.RED + "召唤盖亚失败", ChatColor.BLUE + "请等待" + (Main.interval - jg) + "秒之后再试");
                    event.setCancelled(true);
                }
            }
        }
    }
    
    public void CreateConfig() {
        if (!new File(this.getDataFolder() + File.separator + "config.yml").exists()) {
            this.saveDefaultConfig();
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "无法找到config.yml,正在创建");
        }
        try {
            this.reloadConfig();
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "成功加载config");
        }
        catch (Exception e) {
            e.printStackTrace();
            this.getServer().getPluginManager().disablePlugin((Plugin)this);
            Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "无法读取config");
        }
    }
}
