package com.cookie.assets20291M;

import com.cookie.BuildingType;

/**
 * The necessary building types for simulating early game play.
 */
public enum Building implements BuildingType {

  CURSOR(15, 0.1 / 30), GRANDMA(100, 1 / 30), FARM(1100, 8 / 30),
  MINE(12000, 47 / 30), FACTORY(130000, 260 / 30);

  private final double unitPrice;
  private final double unitRate;

  /**
   * Create a new building singleton with constants.
   *
   * @param unitPrice The constant unit price to use.
   * @param rate      The unit rate to use.
   */
  Building(double unitPrice, double rate) {
    this.unitPrice = unitPrice;
    this.unitRate = rate;
  }

  @Override
  public double unitPrice() {
    return this.unitPrice;
  }

  @Override
  public double rate() {
    return this.unitRate;
  }
}
