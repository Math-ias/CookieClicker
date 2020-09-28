package com.cookie;

/**
 * A test suite of mixins and custom tets for a SimpleCookieClicker.
 */
public class SimpleCookieClickerTests implements CookieClickerTest {
  @Override
  public CookieClicker getImplementation() {
    return new SimpleCookieClicker();
  }
}
