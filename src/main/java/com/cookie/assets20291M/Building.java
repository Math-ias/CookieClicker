package com.cookie.assets20291M;

import com.cookie.BuildingType;

/**
 * The necessary building types for simulating early game play.
 */
public enum Building implements BuildingType {
  /**
   * An auto-clicking cursor orbiting the Big Cookie.
   */
  CURSOR(15, 0.1 / 30),
  /**
   * An older woman, but also technically a building.
   */
  GRANDMA(100, 1 / 30),
  /**
   * A plot of land, also technically a building.
   */
  FARM(1100, 8 / 30),
  /**
   * A cookie mine.
   */
  MINE(12000, 47 / 30),
  /**
   * A building where cookies are manufactured.
   */
  FACTORY(130000, 260 / 30);

  private final double unitPrice;
  private final double unitRate;

  /**
   * Create a new building singleton with constants.
   *
   * @param unitPrice The constant unit price in cookies to use.
   * @param rate      The unit rate to use in cookies per tick.
   */
  Building(double unitPrice, double rate) {
    this.unitPrice = unitPrice;
    this.unitRate = rate;
  }

  @Override
  public double getUnitPrice() {
    return this.unitPrice;
  }

  @Override
  public double getRate() {
    return this.unitRate;
  }
}
