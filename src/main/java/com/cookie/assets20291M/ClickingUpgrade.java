package com.cookie.assets20291M;

import com.cookie.CookieClicker;
import com.cookie.BuildingProductionEffect;
import com.cookie.BuildingType;
import com.cookie.ClickingProductionEffect;
import com.cookie.ProductionEffect;
import com.cookie.ProductionUpgrade;
import com.cookie.SavedCookieClicker;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * The necessary clicking upgrades for simulating early-game play.
 */
public enum ClickingUpgrade implements ProductionUpgrade {
  /**
   * One of the first upgrades for clicking.
   * <p>
   * Interestingly, this does not improve cursor production like others.
   */
  PLASTIC_MOUSE(Collections.singleton(ClickingCPSBoost.INSTANCE), 50000),
  /**
   * An upgrade to both clicking, and cursor production.
   * <p>
   * This effect gives 0.1 cookies for each non-cursor building (specifically checking equals to the
   * Cursor implementation in {@link Building}) in inventory.
   */
  THOUSAND_FINGERS(Set.of(CursorThousandFingersEffect.INSTANCE,
          ClickingThousandFingersEffect.INSTANCE), 100000);

  private final Collection<ProductionEffect> effects;
  private final double price;

  /**
   * Create a new ClickingUpgrade singleton with the following constants.
   *
   * @param effects The collection of effects to return.
   * @param price   The price of this upgrade in cookies.
   */
  ClickingUpgrade(Collection<ProductionEffect> effects, double price) {
    this.effects = effects;
    this.price = price;
  }

  private static final String UNEXPECTED_BRANCH = "Reached an unexpected branch.";

  @Override
  public boolean isPurchasable(CookieClicker stats) {
    Map<BuildingType, Integer> inventory = stats.getBuildingInventory();
    long cursors = inventory.getOrDefault(Building.CURSOR, 0);
    switch (this) {
      case PLASTIC_MOUSE:
        return cursors >= 25;
      case THOUSAND_FINGERS:
        return stats.getHandmadeCookies() >= 1000;
      default:
        throw new AssertionError(UNEXPECTED_BRANCH);
    }
  }

  @Override
  public Collection<ProductionEffect> getEffects() {
    return this.effects;
  }

  @Override
  public double price() {
    return this.price;
  }

  // In general, to make effect comparison developer-stress-free I've employed singletons.

  /**
   * A singleton instance of a clicking boost that provides one percent of CPS.
   */
  enum ClickingCPSBoost implements ClickingProductionEffect {
    INSTANCE;

    @Override
    public double getNumber(CookieClicker stats) {
      double cps = stats.getBuildingInventory()
              .keySet()
              .stream()
              .mapToDouble(buildingType -> stats.getRate(buildingType))
              .sum();
      return cps * 0.01;
    }

    @Override
    public TERM getTerm() {
      return TERM.CONSTANT;
    }
  }

  /**
   * A singleton instance of the clicking effect of thousand fingers.
   */
  enum ClickingThousandFingersEffect implements ClickingProductionEffect {
    INSTANCE;

    @Override
    public double getNumber(CookieClicker stats) {
      Map<BuildingType, Integer> inventory = stats.getBuildingInventory();
      long nonCursors = inventory.entrySet()
              .stream()
              .filter(entry -> !entry.getKey().equals(Building.CURSOR))
              .mapToLong(Map.Entry::getValue)
              .sum();
      return 0.1 * nonCursors;
    }

    @Override
    public TERM getTerm() {
      return TERM.CONSTANT;
    }
  }

  /**
   * A singleton instance of the cursor effect of thousand fingers.
   */
  enum CursorThousandFingersEffect implements BuildingProductionEffect {
    INSTANCE;

    @Override
    public double getNumber(SavedCookieClicker stats) {
      Map<BuildingType, Integer> inventory = stats.getBuildingInventory();
      long nonCursors = inventory.entrySet()
              .stream()
              .filter(entry -> !entry.getKey().equals(Building.CURSOR))
              .mapToLong(Map.Entry::getValue)
              .sum();
      return 0.1 * nonCursors;
    }

    @Override
    public BuildingType getTarget() {
      return Building.CURSOR;
    }

    @Override
    public TERM getTerm() {
      return TERM.CONSTANT;
    }
  }
}