package me.minutz.dream.coinscrate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import me.minutz.dream.API;
import me.minutz.dream.DMaine;
import me.minutz.dream.Alpha.BY;
import me.minutz.dream.util.JSONMessage;

public class CoinsCrate
  implements Listener
{
  public static Plugin plugin;
  Location ChestLock = null;
  ItemStack Reward = new ItemStack(Material.GOLD_INGOT);
  ArmorStand ChestAS = null;
  double SpinSpeed = 0.05D;
  int EffectCounterLoop = 15;
  
  public void enable()
  {
    plugin = DMaine.getPlugin();
    Run();
    this.ChestLock = new Location(Bukkit.getWorld("world"), 902.5D, 114.5D, -461.5D);
    Bukkit.getPluginManager().registerEvents(this, DMaine.getPlugin());
    
    Bukkit.getScheduler().scheduleSyncDelayedTask(DMaine.getPlugin(), new Runnable()
    {
      public void run()
      {
        RunFast();
        for (int i = 0; i < 10; i++) {
          Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
            {
              public void run()
              {
                ArmourStandOrbitObjectEffect(ChestLock.clone().add(0.0D, 0.3D, 0.0D), new ItemStack(Material.GOLD_BLOCK), 1, true, true, true, true, 4, true);
              }
            }, randInt(0, 40));
        }
      }
    }, 5L);
  }
  
  @EventHandler
  public void itempickupevent(PlayerPickupItemEvent e)
  {
      Player p = e.getPlayer();
      if (e.getItem().getItemStack().getType() == Material.GOLD_INGOT)
      {
    	  if(e.getItem().hasMetadata("rew")) {
	    	  e.setCancelled(true);
	    	  if(API.isRegistered(BY.NAME, p.getName())) {
			        int Rand = randInt(5, 10);
			        
			        p.sendMessage(ChatColor.translateAlternateColorCodes('&', "&9You picked up a &6Gold Ingot &9worth &c" + Rand + " &6Coins."));
			        API.addCoins(p.getName(), Rand);
			        e.getItem().remove();
			  }else {
					JSONMessage.create("You are not registered! To receive Coins you need to register: ")
					.color(ChatColor.RED)
					.then("--------")
						.color(ChatColor.GOLD)
						.style(ChatColor.BOLD)
						.tooltip("Click here to register")
						.openURL("--------")
					.then("!")
						.color(ChatColor.RED)
					.send(p);
			  }
    	  }
      }
  }
  
  @EventHandler
  public void itempickupevent(PlayerArmorStandManipulateEvent e)
  {
    e.setCancelled(true);
  }
  
  public void Run()
  {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
    {
      public void run()
      {
        if (ChestAS == null) {
          SpawnChestAS();
        }
        EffectCounterLoop -= 1;
        if (EffectCounterLoop == 0)
        {
          Trigger();
          
          EffectCounterLoop = randInt(15, 45);
          SpinSpeed = 0.05D;
          return;
        }
        ChestAS.setCustomName(ChatColor.translateAlternateColorCodes('&', "&7-=- &bCoin Loot Chest &8[&9" + EffectCounterLoop + "&8s] &7-=-"));
        if (EffectCounterLoop < 10) {
          SpinSpeed += 0.01D * (10 - EffectCounterLoop);
        }
      }
    }, 0L, 20L);
  }
  
  public void RunFast()
  {
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable()
    {
      public void run()
      {
        if (ChestAS != null) {
          ChestAS.setHeadPose(ChestAS.getHeadPose().add(0.0D, SpinSpeed, 0.0D));
        }
      }
    }, 0L, 1L);
  }
  
  public Block getRandomBlock(List<Block> blist) {
		Random r = new Random();
		int ab = r.nextInt(blist.size()-1);
		Block b = blist.get(ab);
		return b;
  }
  
  public void Trigger()
  {
	  List<Block> l = getSphere(ChestLock, 5);
        for (int i = 0; i < 60; i++) {
        	final int di = i;
        	new BukkitRunnable() {
        		int in = di;
        		public void run() {
        		Block b = getRandomBlock(l);
          sendItem(b.getLocation(), Reward, randInt(100, 160 - in * 1),false);
        		}
        	}.runTaskLater(plugin, i);
        }
        
    	new BukkitRunnable() {
    		public void run() {
        Block b = getRandomBlock(l);
        sendItem(b.getLocation(), Reward, randInt(100, 160),true);
    		}
    	}.runTaskLater(plugin, randInt(1, 60));
  }
  
  public void SpawnChestAS()
  { 
    ArmorStand armorStand = (ArmorStand)this.ChestLock.getWorld().spawnEntity(this.ChestLock, EntityType.ARMOR_STAND);
    armorStand.setVisible(false);
    armorStand.setGravity(false);
    armorStand.setCustomNameVisible(true);
    armorStand.setHelmet(new ItemStack(Material.ENDER_CHEST));
    for (Entity E : armorStand.getNearbyEntities(3.0D, 3.0D, 3.0D)) {
      if (E instanceof ArmorStand || E instanceof Item) {
        E.remove();
      }
    }
    this.ChestAS = armorStand;
  }

  
  public void sendItem(Location location, ItemStack item, int removeAfter, boolean adv)
  {
    
    Item i = location.getWorld().dropItem(location, item);
    i.setPickupDelay(999999999);

    ArmorStand b = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
    b.setVisible(false);
    b.setSmall(true);
    b.setGravity(false);
    b.setPassenger(i);

    new BukkitRunnable() {
    	public void run() {
    		if(i.isDead()) {
    			cancel();
    			return;
    		}
    		if(!adv) {
	    		if(i.getTicksLived()>200) {
	    			i.remove();
	    		}
		    	if(i.isOnGround()) {
		    		i.remove();
		    	}
    		}
    	}
    }.runTaskTimer(plugin, 1L, 1L);
    
    Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable()
    {
      public void run()
      {
    	  UtilParticles.drawParticleLine(ChestAS.getEyeLocation(), i.getLocation(), ParticleEffect.REDSTONE, (int) ChestAS.getEyeLocation().distance(i.getLocation())*5, 255, 0, 0);
    	  b.remove();
    	  if(adv) {
        	  i.setMetadata("rew", new FixedMetadataValue(plugin, "protected"));
        	  i.setCustomName(Double.toString(randDouble(1.0D, 999999.0D)));
        	  i.setPickupDelay(0);
    	  }
      }
    }, removeAfter);
  }
  
  Random rand = new Random();
  
  public int randInt(int min, int max)
  {
    int randomNum = this.rand.nextInt(max - min + 1) + min;
    return randomNum;
  }
  
  public static double randDouble(double min, double max)
  {
    double result = Math.random() * (max - min) + min;
    return result;
  }
  
  public static void ItemOrbitObjectEffect(Location LocationToObit, ItemStack OrbitItem, final int OrbitCount)
  {
    double speed = 0.15D;
    Entity clicked = LocationToObit.getWorld().dropItem(LocationToObit.clone().add(0.15D, 0.0D, 0.0D), OrbitItem);
    Item i = (Item)clicked;
    i.setPickupDelay(100000);
    
    new BukkitRunnable()
    {
      int counter = 0;
      double angle = 0.0D;
      double step = 0.09424777960769379D;
      
      public void run()
      {
        i.setVelocity(new Vector(Math.cos(this.angle) * 0.15D, 0.0D, Math.sin(this.angle) * 0.15D));
        this.angle += this.step;
        if (++this.counter > OrbitCount * 100) {
          cancel();
        }
      }
    }.runTaskTimer(plugin, 0L, 1L);
  }
  
  public List<Block> getSphere(Location paramLocation, int paramInt) {
	    ArrayList<Block> localArrayList = new ArrayList<Block>();
	    int i = paramLocation.getBlockX();
	    int j = paramLocation.getBlockY();
	    int k = paramLocation.getBlockZ();
	    int m = paramInt * paramInt;
	    for (int n = i - paramInt; n <= i + paramInt; n++) {
	      for (int i1 = j - paramInt; i1 <= j + paramInt; i1++) {
	        for (int i2 = k - paramInt; i2 <= k + paramInt; i2++) {
	          if ((i - n) * (i - n) + (k - i2) * (k - i2) <= m) {
	        	  Block b = paramLocation.getWorld().getBlockAt(n, i1, i2);
	        	  if(b.getType()==Material.AIR) {
	        		  localArrayList.add(b);
	        	  }
	          }
	        }
	      }
	    }
	    return localArrayList;
	  }
  
  public void ArmourStandOrbitObjectEffect(Location LocationToObit, ItemStack OrbitItem, final int OrbitCount, boolean SmallItem, final boolean RotateItem, boolean UseGrowSmall, final boolean SpawnParticals, final int EffectData, boolean ReverseSpin)
  {
    double speed = 0.13D;
    final ArmorStand clicked = (ArmorStand)LocationToObit.getWorld().spawnEntity(LocationToObit.clone().add(0.13D, 0.0D, 0.0D), EntityType.ARMOR_STAND);
    
    clicked.setItemInHand(OrbitItem);
    clicked.setSmall(SmallItem);
    clicked.setArms(true);
    clicked.setRightArmPose(new EulerAngle(11.0D, 0.0D, 0.0D));
    clicked.setVisible(false);
    clicked.setGravity(false);
    
    new BukkitRunnable()
    {
      int counter = 0;
      double angle = 0.0D;
      double step = 0.12566370614359174D;
      double fSpeed = 0.13D;
      double itemMove1 = randDouble(0.06D, 0.1D);
      double itemMove2 = randDouble(0.1D, 0.15D);
      double itemMove3 = randDouble(0.06D, 0.1D);
      
      public void run()
      {
        clicked.setVelocity(new Vector(Math.cos(this.angle) * this.fSpeed, 0.0D, Math.sin(this.angle) * this.fSpeed));
        if (RotateItem) {
          clicked.setRightArmPose(clicked.getRightArmPose().add(this.itemMove1, this.itemMove2, this.itemMove3));
        }
        if ((SpawnParticals) && 
          (randInt(0, 10) == 0)) {
        }
        this.angle += this.step;
        if (++this.counter <= OrbitCount * 20) {}
      }
    }.runTaskTimer(plugin, 21L, 1L);
  }

}
