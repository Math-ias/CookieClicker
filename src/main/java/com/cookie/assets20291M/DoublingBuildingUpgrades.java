package com.cookie.assets20291M;

import com.cookie.BuildingType;
import com.cookie.CookieClicker;
import com.cookie.ProductionEffect;
import com.cookie.ProductionUpgrade;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * The necessary upgrades to double building production in early-game play.
 */
public enum DoublingBuildingUpgrades implements ProductionUpgrade {
  FORWARDS_FROM_GRANDMA(Building.GRANDMA, 1, 1000),
  STEEL_PLATED_ROLLING_PINS(Building.GRANDMA, 5, 5000),
  LUBRICATED_DENTURES(Building.GRANDMA, 25, 50000),

  CHEAP_HOES(Building.FARM, 1, 11000),
  FERTILIZER(Building.FARM,5, 55000),
  COOKIE_TREES(Building.FARM,25, 550000),

  SUGAR_GAS(Building.MINE, 1, 120000),
  MEGADRILL(Building.MINE, 1, 600000);

  private final BuildingType target;
  private final long targetMinimum;
  private final double price;
  private final ProductionEffect effect;

  /**
   * Create a new BasicBuildingUpgrade.
   *
   * @param target      The building type target of this upgrade.
   * @param targetMinimum The number of the target required to buy this upgrade.
   * @param price       The price of this upgrade in cookies.
   */
  DoublingBuildingUpgrades(BuildingType target, long targetMinimum, double price) {
    this.target = target;
    this.targetMinimum = targetMinimum;
    this.price = price;
    this.effect = new BuildingProductionDoubler(this.target);
  }

  @Override
  public boolean purchasable(CookieClicker stats) {
    Map<BuildingType, Integer> inventory = stats.getBuildingInventory();
    return inventory.getOrDefault(this.target, 0) >= this.targetMinimum;
  }

  @Override
  public Collection<ProductionEffect> getEffects() {
    return Collections.singleton(this.effect);
  }

  @Override
  public double price() {
    return this.price;
  }
}