package eu.inn.biosign.util;

/*
 * #%L
 * Java Applet for biometric trait acquisition [http://www.biosignin.org]
 * MemoryWarningSystem.java is part of BioSignIn project
 * %%
 * Copyright (C) 2014 Innovery SpA
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * #L%
 */

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryNotificationInfo;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.util.ArrayList;
import java.util.Collection;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationListener;

/**
 * This memory warning system will call the listener when we
 * exceed the percentage of available memory specified.  There
 * should only be one instance of this object created, since the
 * usage threshold can only be set to one number.
 */
public class MemoryWarningSystem {
  private final Collection<Listener> listeners =
      new ArrayList<Listener>();

  public interface Listener {
    public void memoryUsageLow(long usedMemory, long maxMemory);
  }

  public MemoryWarningSystem() {
    MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
    NotificationEmitter emitter = (NotificationEmitter) mbean;
    emitter.addNotificationListener(new NotificationListener() {
      public void handleNotification(Notification n, Object hb) {
        if (n.getType().equals(
            MemoryNotificationInfo.MEMORY_THRESHOLD_EXCEEDED)) {
          long maxMemory = tenuredGenPool.getUsage().getMax();
          long usedMemory = tenuredGenPool.getUsage().getUsed();
          for (Listener listener : listeners) {
            listener.memoryUsageLow(usedMemory, maxMemory);
          }
        }
      }
    }, null, null);
  }

  public boolean addListener(Listener listener) {
    return listeners.add(listener);
  }

  public boolean removeListener(Listener listener) {
    return listeners.remove(listener);
  }

  private static final MemoryPoolMXBean tenuredGenPool =
      findTenuredGenPool();

  public static void setPercentageUsageThreshold(double percentage) {
    if (percentage <= 0.0 || percentage > 1.0) {
      throw new IllegalArgumentException("Percentage not in range");
    }
    long maxMemory = tenuredGenPool.getUsage().getMax();
    long warningThreshold = (long) (maxMemory * percentage);
    tenuredGenPool.setUsageThreshold(warningThreshold);
  }

  /**
   * Tenured Space Pool can be determined by it being of type
   * HEAP and by it being possible to set the usage threshold.
   */
  private static MemoryPoolMXBean findTenuredGenPool() {
    for (MemoryPoolMXBean pool :
        ManagementFactory.getMemoryPoolMXBeans()) {
      // I don't know whether this approach is better, or whether
      // we should rather check for the pool name "Tenured Gen"?
      if (pool.getType() == MemoryType.HEAP &&
          pool.isUsageThresholdSupported()) {
        return pool;
      }
    }
    throw new AssertionError("Could not find tenured space");
  }
}
