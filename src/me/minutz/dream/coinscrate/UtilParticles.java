package me.minutz.dream.coinscrate;

import java.awt.Color;

import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class UtilParticles
{
  public static void drawParticleLine(Location from, Location to, ParticleEffect effect, int particles, int r, int g, int b)
  {
    Location location = from.clone();
    Location target = to.clone();
    double amount = particles;
    Vector link = target.toVector().subtract(location.toVector());
    float length = (float)link.length();
    link.normalize();

    float ratio = length / particles;
    Vector v = link.multiply(ratio);
    Location loc = location.clone().subtract(v);
    int step = 0;
    for (int i = 0; i < particles; i++) {
      if (step >= amount)
        step = 0;
      step++;
      loc.add(v);
      if (effect == ParticleEffect.REDSTONE)
        effect.display(new ParticleEffect.OrdinaryColor(r, g, b), loc, 128.0D);
      else
        effect.display(0.0F, 0.0F, 0.0F, 0.0F, 1, loc, 128.0D);
    }
  }
  public static void display(ParticleEffect effect, Location location, int amount, float speed) {
    effect.display(0.0F, 0.0F, 0.0F, speed, amount, location, 128.0D);
  }

  public static void display(ParticleEffect effect, Location location, int amount) {
    effect.display(0.0F, 0.0F, 0.0F, 0.0F, amount, location, 128.0D);
  }

  public static void display(ParticleEffect effect, Location location) {
    display(effect, location, 1);
  }

  public static void display(ParticleEffect effect, double x, double y, double z, Location location, int amount) {
    effect.display((float)x, (float)y, (float)z, 0.0F, amount, location, 128.0D);
  }

  public static void display(ParticleEffect effect, int red, int green, int blue, Location location, int amount) {
    for (int i = 0; i < amount; i++)
      effect.display(new ParticleEffect.OrdinaryColor(red, green, blue), location, 128.0D);
  }

  public static void display(int red, int green, int blue, Location location) {
    display(ParticleEffect.REDSTONE, red, green, blue, location, 1);
  }

  public static void display(ParticleEffect effect, int red, int green, int blue, Location location) {
    display(effect, red, green, blue, location, 1);
  }
}