package com.cookie.mocks;

import com.cookie.BuildingType;

/**
 * A collection of mock building types for testing.
 */
public enum MockBuildingType implements BuildingType {
  RATE1PRICE1(1, 1);

  private final double rate;
  private final double unitPrice;

  /**
   * Create a new MockBuildingType with the given constants.
   *
   * @param rate      The rate to create the mock building type with.
   * @param unitPrice The price of the mock building type.
   */
  MockBuildingType(double rate, double unitPrice) {
    this.rate = rate;
    this.unitPrice = unitPrice;
  }

  @Override
  public double rate() {
    return this.rate;
  }

  @Override
  public double unitPrice() {
    return this.unitPrice;
  }
}
