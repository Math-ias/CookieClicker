package com.cookie;

import java.util.Collection;
import java.util.Optional;

/**
 * An expiring set of effects on cookie production.
 */
public interface ProductionBuff {
  /**
   * The amount of time left before this buff expires.
   *
   * @return The total amount of time left on this buff measured in ticks (positive).
   */
  long getTimeLeft();

  /**
   * The amount of time this buff was constructed with.
   *
   * @return The total amount of time this buff will be active for measured in ticks (positive).
   */
  long getTimeTotal();

  /**
   * Provides a new version of this buff fast-forwarded.
   *
   * @param ticks The amount of time to warp this buff forward by in ticks.
   * @return The production buff if it exists after the amount of time, empty if it doesn't exist.
   * @throws IllegalArgumentException If ticks is negative.
   */
  Optional<ProductionBuff> warp(long ticks);

  /**
   * Returns the effects of this production buff.
   *
   * @return The collection of effects this buff has on production.
   */
  Collection<ProductionEffect> getEffects();
}
