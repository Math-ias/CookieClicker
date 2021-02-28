package com.cookie.assets20291M;

import com.cookie.BuildingProductionEffect;
import com.cookie.BuildingProductionEffectTest;
import com.cookie.mocks.MockBuildingType;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tries the BuildingProductionEffectTest suite against the multiplier helper class.
 */
public class BuildingProductionMultiplierEffectTest extends BuildingProductionEffectTest {

  /**
   * Tests the implementation of the multiplier constructor.
   */
  @Test
  public void testBuildingMultiplierIllegalArguments() {
    assertThrows(NullPointerException.class, () -> new BuildingProductionMultiplier(null, 2));
  }

  @Override
  public BuildingProductionEffect getImplementation() {
    return new BuildingProductionMultiplier(MockBuildingType.RATE1PRICE1, 2);
  }
}
