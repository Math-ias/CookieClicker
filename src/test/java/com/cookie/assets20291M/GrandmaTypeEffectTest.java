package com.cookie.assets20291M;

import com.cookie.BuildingProductionEffect;
import com.cookie.BuildingProductionEffectTest;

/**
 * A test suite to check the FarmerGrandma effect.
 */
class FarmerGrandmaEffectTest extends BuildingProductionEffectTest {
  @Override
  public BuildingProductionEffect getImplementation() {
    return GrandmaType.FarmerGrandmaFarmEffect.INSTANCE;
  }
}
