package eu.luminis.sample.commands;

import eu.luminis.sample.values.Address;

import java.util.List;
import java.util.UUID;

public record CreateOrderCommand(List<UUID> items, Address shippingAddress) {
}
