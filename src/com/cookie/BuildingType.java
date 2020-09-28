package com.cookie;

/**
 * A building in Cookie Clicker is a purchasable item in CookieClicker.
 * <p>
 * This class represents an entire type of that building. Instances of this class should be
 * re-used.
 */
public interface BuildingType {

  /**
   * Return the unit rate of this building type.
   *
   * @return The rate of this type of building in cookies per tick.
   */
  double rate();

  /**
   * Return the unit price of this type of building. This should stay fixed.
   *
   * @return The unit price of this type of building.
   */
  double unitPrice();

}
