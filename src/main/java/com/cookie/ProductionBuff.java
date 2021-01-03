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

  /**
   * Indicates if some other ProductionBuff is equal to this one.
   * <p>
   * The other object is equal to this ProductionBuff, if and only if, it is also a ProductionBuff,
   * and both have ...
   * <ul>
   *   <li>Equal time left.</li>
   *   <li>Equal time total.</li>
   *   <li>Equal collections of effects. See {@link ProductionEffect}'s equals.</li>
   * </ul>
   *
   * @param o The object to compare to.
   * @return If this ProductionBuff is equal to the given object.
   */
  boolean equals(Object o);

  /**
   * Returns the hash code of this ProductionBuff.
   * <p>
   * The hashCode should be calculated as the Objects.hash of the time left, time total, and effects
   * collection.
   *
   * @return The hashCode of this ProductionBuff.
   */
  int hashCode();
}
