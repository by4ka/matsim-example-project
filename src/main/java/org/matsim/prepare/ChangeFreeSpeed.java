package org.matsim.prepare;

import org.matsim.api.core.v01.TransportMode;
import org.matsim.api.core.v01.network.Network;
import org.matsim.core.network.NetworkUtils;

public class ChangeFreeSpeed {

    public static void main (String[] args) {

        var network = NetworkUtils.readNetwork("/Users/shakshuka/Downloads/berlin-v5.5.3-1pct.output_network.xml");

        changeNetwork(network);

        NetworkUtils.writeNetwork(network, "/Users/shakshuka/Downloads/berlin1pct.output_network_changed.xml");

    }
    public static void changeNetwork(Network network) {
        for (var link: network.getLinks().values()) {

            if (link.getAllowedModes().contains(TransportMode.car))
                link.setFreespeed(10);
        }
    }
}
