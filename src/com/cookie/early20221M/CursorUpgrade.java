package com.cookie.early20221M;

import com.cookie.BuildingType;
import com.cookie.CookieClicker;
import com.cookie.effects.ClickingProductionEffect;
import com.cookie.effects.ProductionEffect;
import com.cookie.effects.ProductionUpgrade;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Cursor upgrades usable early-game.
 */
public enum CursorUpgrade implements ProductionUpgrade {
  REINFORCED_INDEX_FINGER(1, 100),
  CARPAL_TUNNEL_PREVENTION_CREAM(1, 500),
  AMBIDEXTROUS(10, 10000);

  private final long minimumCursors;
  private final double price;

  /**
   * Create a new CursorUpgrade.
   * @param minimumCursors  The minimum number of cursors needed to buy this upgrade.
   * @param price The price of this upgrade.
   */
  CursorUpgrade(long minimumCursors, double price) {
    this.minimumCursors = minimumCursors;
    this.price = price;
  }

  private static ProductionEffect X2CURSOR = new BuildingProductionDoubler(Building.CURSOR);

  @Override
  public boolean purchasable(CookieClicker stats) {
    Map<BuildingType, Integer> inventory = stats.getBuildingInventory();
    Integer cursors = inventory.getOrDefault(Building.CURSOR, 0);
    return cursors >= minimumCursors;
  }

  @Override
  public Collection<ProductionEffect> getEffects() {
    return Set.of(X2CURSOR,
            ClickingMultiplierEffect.INSTANCE);
  }

  @Override
  public double price() {
    return this.price;
  }

  /**
   * A singleton instance of the clicking multiplier effect.
   */
  enum ClickingMultiplierEffect implements ClickingProductionEffect {
    INSTANCE;

    @Override
    public double getEffect(CookieClicker stats) {
      return 2;
    }

    @Override
    public TERM getTerm() {
      return TERM.MULTIPLIER;
    }
  }
}