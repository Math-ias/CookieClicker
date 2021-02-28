package com.cookie.assets20291M;

import com.cookie.BuildingProductionEffect;
import com.cookie.BuildingProductionEffectTest;
import com.cookie.ClickingProductionEffect;
import com.cookie.ClickingProductionEffectTest;

// A collection of tests for the ClickingUpgrade effects.

/**
 * A test suite for the PlasticFingers effect.
 */
class PlasticFingersEffectTest extends ClickingProductionEffectTest {
  public ClickingProductionEffect getImplementation() {
    return ClickingUpgrade.PlasticMouseEffect.INSTANCE;
  }
}

/**
 * A test suite for the ThousandFingers effect for clicking.
 */
class ClickingThousandFingersEffectTest extends ClickingProductionEffectTest {
  public ClickingProductionEffect getImplementation() {
    return ClickingUpgrade.ClickingThousandFingersEffect.INSTANCE;
  }
}

/**
 * A test suite for the ThousandFingers effect for cursors.
 */
class CursorThousandFingersEffectTest extends BuildingProductionEffectTest {
  @Override
  public BuildingProductionEffect getImplementation() {
    return ClickingUpgrade.CursorThousandFingersEffect.INSTANCE;
  }
}