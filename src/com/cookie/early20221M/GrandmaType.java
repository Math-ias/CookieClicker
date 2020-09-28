package com.cookie.early20221M;

import com.cookie.CookieClicker;
import com.cookie.effects.BuildingProductionEffect;
import com.cookie.BuildingType;
import com.cookie.effects.ProductionEffect;
import com.cookie.effects.ProductionUpgrade;
import com.cookie.SavedCookieClicker;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Early-game grandma type upgrades.
 */
public enum GrandmaType implements ProductionUpgrade {
  FARMER_GRANDMAS(55000, FarmerGrandmaFarmEffect.INSTANCE);

  private final double price;
  private final ProductionEffect extraEffect;

  /**
   * Create a new GrandmaType upgrade.
   *
   * @param price       The price of this upgrade.
   * @param extraEffect The extra effect of this upgrade other than doubling grandma efficiency.
   */
  GrandmaType(double price, ProductionEffect extraEffect) {
    this.price = price;
    this.extraEffect = extraEffect;
  }

  private final ProductionEffect X2GRANDMA = new BuildingProductionDoubler(Building.GRANDMA);

  private static String UNEXPECTED_BRANCH = "Reached an unexpected branch.";

  // In this class I go through the trouble of using switch statements to be future-proof.

  @Override
  public boolean purchasable(CookieClicker stats) {
    Map<BuildingType, Integer> inventory = stats.getBuildingInventory();
    Integer grandmas = inventory.getOrDefault(Building.GRANDMA, 0);
    return switch (this) {
      case FARMER_GRANDMAS -> grandmas >= 1 && inventory.getOrDefault(Building.FARM, 0) >= 15;
      default -> throw new AssertionError(UNEXPECTED_BRANCH);
    };
  }

  @Override
  public Collection<ProductionEffect> getEffects() {
    return Set.of(X2GRANDMA, this.extraEffect);
  }

  @Override
  public double price() {
    return this.price;
  }

  /**
   * A singleton instance of the effect of FARMER_GRANDMAS on Farms.
   */
  enum FarmerGrandmaFarmEffect implements BuildingProductionEffect {
    INSTANCE;

    @Override
    public double getEffect(SavedCookieClicker stats) {
      Map<BuildingType, Integer> inventory = stats.getBuildingInventory();
      return 1 + 0.01 * inventory.getOrDefault(Building.GRANDMA, 0);
    }

    @Override
    public BuildingType getTarget() {
      return Building.FARM;
    }

    @Override
    public TERM getTerm() {
      return TERM.MULTIPLIER;
    }
  }
}