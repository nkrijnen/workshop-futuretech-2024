import type { ChangeShippingAddressCommand, CreateOrderCommand } from "./commands.js"
import type { OrderCreatedEvent, OrderEvent, ShippingAddressChangedEvent } from "./events.js"

export class OrderHandler {
    constructor(private readonly history: OrderEvent[]) {
    }

    private get hasBeenShipped(): boolean {
        return this.history.some(event => event.type == "OrderShipped")
    }

    static handleCreate(command: CreateOrderCommand) {
        const event: OrderCreatedEvent = {
            type: "OrderCreated",
            atTime: new Date(),
            items: command.items,
            shippingAddress: command.shippingAddress,
        }
        return event;
    }

    handleChangeShipping(command: ChangeShippingAddressCommand): ShippingAddressChangedEvent {
        if (this.hasBeenShipped) throw new Error("Already shipped")

        const event: ShippingAddressChangedEvent = {
            type: "ShippingAddressChanged",
            atTime: new Date,
            shippingAddress: command.shippingAddress,
        }
        return event
    }
}
