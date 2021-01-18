package com.cookie;

import java.util.Collection;

/**
 * An upgrade.
 */
public interface ProductionUpgrade {
  /**
   * Returns if this upgrade's purchase requirements have been met.
   * <p>
   * These requirements should NOT include price, just the rules required to unlock the upgrade.
   *
   * @param stats The game state to query for statistics.
   * @return True if this upgrade can be purchased, otherwise false.
   * @throws NullPointerException If stats is null.
   */
  boolean isPurchasable(CookieClicker stats);

  /**
   * Returns the contained effects of this upgrade.
   *
   * @return A collection of effects to apply to production.
   */
  Collection<ProductionEffect> getEffects();

  /**
   * Return the unit price of this asset.
   *
   * @return The price of this asset in cookies.
   */
  double price();
}
