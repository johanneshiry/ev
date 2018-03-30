/* *********************************************************************** *
 * project: org.matsim.*
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2015 by the members listed in the COPYING,        *
 *                   LICENSE and WARRANTY file.                            *
 * email           : info at matsim dot org                                *
 *                                                                         *
 * *********************************************************************** *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *   See also COPYING, LICENSE and WARRANTY file                           *
 *                                                                         *
 * *********************************************************************** */

package org.matsim.vsp.ev.charging;

import org.matsim.core.api.experimental.events.EventsManager;
import org.matsim.core.mobsim.framework.events.MobsimAfterSimStepEvent;
import org.matsim.core.mobsim.framework.listeners.MobsimAfterSimStepListener;
import org.matsim.vsp.ev.EvConfigGroup;
import org.matsim.vsp.ev.data.Charger;
import org.matsim.vsp.ev.data.ChargingInfrastructure;

import com.google.inject.Inject;

public class ChargingHandler implements MobsimAfterSimStepListener {
	private final Iterable<Charger> chargers;
	private final int chargeTimeStep;

	@Inject
	public ChargingHandler(ChargingInfrastructure chargingInfrastructure, EvConfigGroup evConfig,
			EventsManager eventsManager) {
		this.chargers = chargingInfrastructure.getChargers().values();
		this.chargeTimeStep = evConfig.getChargeTimeStep();

		for (Charger c : chargers) {
			c.getLogic().initEventsHandling(eventsManager);
		}
	}

	@Override
	public void notifyMobsimAfterSimStep(@SuppressWarnings("rawtypes") MobsimAfterSimStepEvent e) {
		if ((e.getSimulationTime() + 1) % chargeTimeStep == 0) {
			for (Charger c : chargers) {
				c.getLogic().chargeVehicles(chargeTimeStep, e.getSimulationTime());
			}
		}
	}
}
