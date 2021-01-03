package com.cookie;

import java.util.Collection;

/**
 * An upgrade.
 */
public interface ProductionUpgrade {
  /**
   * Returns if this upgrade's purchase requirements have been met.
   *
   * @param stats The game state to query for statistics.
   * @return True if this upgrade can be purchased, otherwise false.
   * @throws NullPointerException If stats is null.
   */
  boolean purchasable(CookieClicker stats);

  /**
   * Returns the contained effects of this upgrade.
   *
   * @return A collection of effects to apply to production.
   */
  Collection<ProductionEffect> getEffects();

  /**
   * Return the unit price of this asset.
   *
   * @return The price of this asset.
   */
  double price();
}
