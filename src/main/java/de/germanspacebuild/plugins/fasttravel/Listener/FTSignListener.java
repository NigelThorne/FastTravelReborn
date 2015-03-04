package de.germanspacebuild.plugins.fasttravel.Listener;

import de.germanspacebuild.plugins.fasttravel.FastTravel;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelDB;
import de.germanspacebuild.plugins.fasttravel.data.FastTravelSign;
import de.germanspacebuild.plugins.fasttravel.util.BlockUtil;
import de.germanspacebuild.plugins.fasttravel.util.FastTravelUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Created by oneill011990 on 04.03.2015.
 */
public class FTSignListener implements Listener{

    private FastTravel plugin;

    public FTSignListener(FastTravel plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void onSignChange(SignChangeEvent event){

        String lines[] = event.getLines();

        if (!FastTravelUtil.isFTSign(lines))
            return;

        if (event.getPlayer().hasPermission(FastTravel.PERMS_BASE + "create")) {
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Perms.Not");
            event.getBlock().breakNaturally(new ItemStack(Material.SIGN, 1));
            return;
        }

        // Check for valid name
        Pattern an = Pattern.compile("^[a-zA-Z0-9_-]+$");
        if (!an.matcher(lines[1]).find()) {
            event.getBlock().breakNaturally(new ItemStack(Material.SIGN, 1));
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.InvalidName");
            return;
        }

        Block blockAbove = event.getBlock().getWorld().getBlockAt(event.getBlock().getX(), event.getBlock().getY() + 1,
                event.getBlock().getZ());
        if (!Arrays.asList(BlockUtil.safeBlocks).contains(blockAbove)) {
            event.getBlock().breakNaturally(new ItemStack(Material.SIGN, 1));
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.BlockAbove");
            return;
        }

        if (FastTravelDB.getSign(lines[1]) != null){
            event.getBlock().breakNaturally(new ItemStack(Material.SIGN, 1));
            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.Exists".replaceAll("%sign", lines[1]));
            return;
        }

        else {
            FastTravelSign newFTSign = new FastTravelSign(lines[1], event.getPlayer().getUniqueId(), event.getBlock());

            // Economy support - set default price
            if (plugin.getEconomy() != null) {
                float defPrice = (float) plugin.getConfig().getDouble("economy.default-price");
                if (defPrice > 0)
                    event.setLine(2, "Price: " + defPrice);
                newFTSign.setPrice(defPrice);
            }

            FastTravelDB.addSign(newFTSign);

            plugin.getIOManger().sendTranslation(event.getPlayer(), "Sign.Created");

            newFTSign.addPlayer(event.getPlayer().getUniqueId());

            // Colorize sign
            event.setLine(0, ChatColor.DARK_PURPLE + "[FastTravel]");
            event.setLine(1, ChatColor.DARK_BLUE + lines[1]);

            FastTravelDB.save();

        }


    }

}