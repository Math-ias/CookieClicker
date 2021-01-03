package com.cookie;

/**
 * A test suite of mixins and custom tests for a SimpleCookieClicker.
 */
public class SimpleCookieClickerTest implements CookieClickerTest {
  @Override
  public CookieClicker getImplementation() {
    return new SimpleCookieClicker();
  }
}
