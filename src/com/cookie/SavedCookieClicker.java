package com.cookie;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * The state of an incremental game of Cookie Clicker.
 * <p>
 * The intention is to match what is available in a saved game of Cookie Clicker.
 * <p>
 * Interface methods should only use ticks as units of time. Cookie Clicker in a browser runs at
 * thirty ticks per second.
 * <p>
 * Cookies are typed as doubles on purpose. Primitive types like integers, and longs cannot store
 * large enough numbers. Additionally, cookie clicker uses floating point numbers for bank and
 * rate.
 * <p>
 * The state captured by this interface should be treated as immutable. Equals and hashCode for this
 * state is left intentionally vague for different extensions.
 */
public interface SavedCookieClicker {
  // Core game values.

  /**
   * Returns the total time spent in this save.
   *
   * @return The total time spent in this save measured in ticks.
   */
  long getTicks();

  /**
   * Returns the current amount of cookies in the bank.
   *
   * @return The total number of cookies currently stored in the bank.
   */
  double getCurrentBank();

  /**
   * Returns the current game inventory of buildings.
   *
   * @return A read-only map of building types to the count of how many are currently owned. Items
   * that aren't currently owned are not necessarily mapped to zero.
   */
  Map<BuildingType, Integer> getBuildingInventory();

  /**
   * Returns the current game inventory of production upgrades.
   *
   * @return A read-only collection of upgrades.
   */
  Set<ProductionUpgrade> getProductionUpgrades();

  /**
   * Returns the currently activated production buffs.
   *
   * @return A read-only collection of active (non-zero time) buffs.
   */
  Collection<ProductionBuff> getActiveProductionBuffs();

  /**
   * Returns the current clicking rate.
   *
   * @return The current clicking rate in clicks per tick.
   */
  double getClickingRate();

  // Game collected statistics.

  /**
   * Returns the current amount of cookies baked all-time.
   *
   * @return The total number of cookies baked all-time.
   */
  double getCookiesBaked();

  /**
   * Returns the current amount of cookies generated in one click.
   *
   * @return The number of cookies per single click of the big cookie.
   */
  double getCookiesPerClick();

  /**
   * Returns the number of cookies baked by clicking the big cookie.
   *
   * @return The total number of cookies earned by the mouse clicking the big cookie all-time.
   */
  double getHandmadeCookies();

  /**
   * Returns the number of times the big cookie was clicked.
   *
   * @return The total number of times the big cookie was clicked all time.
   */
  double getCookieClicks();
}
