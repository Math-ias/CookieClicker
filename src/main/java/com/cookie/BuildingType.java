package com.cookie;

/**
 * A type of building.
 * <p>
 * An instance should represent the entire type, and be re-used.
 */
public interface BuildingType {

  /**
   * Return the unit rate of this type of building.
   *
   * @return The rate of this type of building in cookies per tick.
   */
  double rate();

  /**
   * Return the unit price of this type of building.
   *
   * @return The unit price of this type of building.
   */
  double unitPrice();

}
