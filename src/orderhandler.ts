import type { Address, LegoSetNumber } from "./model.js";

export class CreateOrderCommand {
    constructor(
        readonly items: LegoSetNumber[],
        readonly shippingAddress: Address
    ) { }
}

export class OrderCreatedEvent {
    constructor(
        readonly items: LegoSetNumber[],
        readonly shippingAddress: Address,
        readonly atTime: Date,
    ) { }
}

export class OrderHandler {
    static handleCreate(command: CreateOrderCommand) {
        return new OrderCreatedEvent(
            command.items,
            command.shippingAddress,
            new Date()
        )
    }
}
