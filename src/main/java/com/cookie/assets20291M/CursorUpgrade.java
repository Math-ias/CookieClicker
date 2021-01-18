package com.cookie.assets20291M;

import com.cookie.BuildingType;
import com.cookie.CookieClicker;
import com.cookie.ClickingProductionEffect;
import com.cookie.ProductionEffect;
import com.cookie.ProductionUpgrade;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * The necessary upgrades to double cursor (and clicking) production for early-game play.
 */
public enum CursorUpgrade implements ProductionUpgrade {
  /**
   * The first of a long series to upgrade both cursor and clicking production.
   */
  REINFORCED_INDEX_FINGER(1, 100),
  /**
   * The second of a long series to double both cursor and clicking production.
   */
  CARPAL_TUNNEL_PREVENTION_CREAM(1, 500),
  /**
   * The last upgrade implemented to upgrade both cursor and clicking production.
   */
  AMBIDEXTROUS(10, 10000);

  private final long minimumCursors;
  private final double price;

  /**
   * Create a new CursorUpgrade.
   *
   * @param minimumCursors The minimum number of cursors to own to unlock purchasing this upgrade.
   * @param price          The price of this upgrade in cookies.
   */
  CursorUpgrade(long minimumCursors, double price) {
    this.minimumCursors = minimumCursors;
    this.price = price;
  }

  private static final ProductionEffect DOUBLE_CURSOR = new BuildingProductionDoubler(Building.CURSOR);

  @Override
  public boolean purchasable(CookieClicker stats) {
    Map<BuildingType, Integer> inventory = stats.getBuildingInventory();
    Integer cursors = inventory.getOrDefault(Building.CURSOR, 0);
    return cursors >= minimumCursors;
  }

  @Override
  public Collection<ProductionEffect> getEffects() {
    return Set.of(DOUBLE_CURSOR,
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
    public double getNumber(CookieClicker stats) {
      return 2;
    }

    @Override
    public TERM getTerm() {
      return TERM.MULTIPLIER;
    }
  }
}