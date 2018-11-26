/* *********************************************************************** *
 * project: org.matsim.*
 * *********************************************************************** *
 *                                                                         *
 * copyright       : (C) 2017 by the members listed in the COPYING,        *
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

package org.matsim.vsp.ev.dvrp;

import org.matsim.api.core.v01.network.Network;
import org.matsim.contrib.dvrp.data.Fleet;
import org.matsim.contrib.dvrp.data.FleetImpl;
import org.matsim.contrib.dvrp.data.Vehicle;
import org.matsim.contrib.dvrp.data.file.VehicleReader;
import org.matsim.contrib.dvrp.router.DvrpRoutingNetworkProvider;
import org.matsim.core.config.Config;
import org.matsim.core.config.ConfigGroup;
import org.matsim.vsp.ev.data.ElectricFleet;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.name.Named;

/**
 * @author michalm
 */
public class EvDvrpFleetProvider implements Provider<Fleet> {
	@Inject
	@Named(DvrpRoutingNetworkProvider.DVRP_ROUTING)
	private Network network;

	@Inject
	private Config config;

	@Inject
	private ElectricFleet evFleet;

	private final String file;

	public EvDvrpFleetProvider(String file) {
		this.file = file;
	}

	@Override
	public Fleet get() {
		FleetImpl fleet = new FleetImpl();
		new VehicleReader(network, fleet).parse(ConfigGroup.getInputFileURL(config.getContext(), file));

		FleetImpl evDvrpFleet = new FleetImpl();
		for (Vehicle v : fleet.getVehicles().values()) {
			evDvrpFleet.addVehicle(EvDvrpVehicle.create(v, evFleet));
		}
		return evDvrpFleet;
	}
}
