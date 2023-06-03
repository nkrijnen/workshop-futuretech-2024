package eu.luminis.sample.commands;

import eu.luminis.sample.values.Address;

public record ChangeShippingAddressCommand(Address shippingAddress) {
}
