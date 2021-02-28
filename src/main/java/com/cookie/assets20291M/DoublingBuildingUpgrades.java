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
  /**
   * The first upgrade in a series to double grandma production.
   */
  FORWARDS_FROM_GRANDMA(Building.GRANDMA, 1, 1000),
  /**
   * One upgrade in a series to double grandma production.
   */
  STEEL_PLATED_ROLLING_PINS(Building.GRANDMA, 5, 5000),
  /**
   * The last upgrade implemented in the series to double grandma production.
   */
  LUBRICATED_DENTURES(Building.GRANDMA, 25, 50000),

  /**
   * The first upgrade in a series to double farm production.
   */
  CHEAP_HOES(Building.FARM, 1, 11000),
  /**
   * One upgrade in a series to double farm production.
   */
  FERTILIZER(Building.FARM, 5, 55000),
  /**
   * The last upgrade implemented in the series to double farm production.
   */
  COOKIE_TREES(Building.FARM, 25, 550000),

  /**
   * The first upgrade implemented to double mine production.
   */
  SUGAR_GAS(Building.MINE, 1, 120000),
  /**
   * The last upgrade implemented to double mine production.
   */
  MEGADRILL(Building.MINE, 1, 600000);

  private final BuildingType target;
  private final long targetMinimum;
  private final double price;
  private final ProductionEffect effect;

  /**
   * Create a new BasicBuildingUpgrade.
   *
   * @param target        The BuildingType to target with this upgrade.
   * @param targetMinimum The number of the target to be owned before being purchasable.
   * @param price         The price of this upgrade in cookies.
   */
  DoublingBuildingUpgrades(BuildingType target, long targetMinimum, double price) {
    this.target = target;
    this.targetMinimum = targetMinimum;
    this.price = price;
    this.effect = new BuildingProductionMultiplier(this.target, 2);
  }

  @Override
  public boolean isPurchasable(CookieClicker stats) {
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