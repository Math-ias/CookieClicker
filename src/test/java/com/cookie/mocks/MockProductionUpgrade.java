package com.cookie.mocks;

import com.cookie.CookieClicker;
import com.cookie.ProductionEffect;
import com.cookie.ProductionUpgrade;

import java.util.Collection;
import java.util.Collections;

/**
 * A collection of mock production upgrades to use for testing.
 */
public enum MockProductionUpgrade implements ProductionUpgrade {
  DUD(1, Collections.EMPTY_SET),
  MUST_OWN_1RATE1PRICE1(1, Collections.EMPTY_SET);

  private final double price;
  private final Collection<ProductionEffect> effects;

  MockProductionUpgrade(double price, Collection<ProductionEffect> effects) {
    this.price = price;
    this.effects = effects;
  }

  @Override
  public boolean purchasable(CookieClicker stats) {
    switch (this) {
      case DUD:
        return true;
      case MUST_OWN_1RATE1PRICE1:
        return stats.getBuildingInventory()
                .getOrDefault(MockBuildingType.RATE1PRICE1, 0) > 0;
      default:
        throw new AssertionError("Unexpected branch!");
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
}
