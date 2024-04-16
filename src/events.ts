import type { Address, LegoSetNumber } from "./model.js"

export interface OrderEvent {
    readonly type: string
    readonly atTime: Date
}

export interface OrderCreatedEvent extends OrderEvent {
    readonly type: "OrderCreated"
    readonly items: LegoSetNumber[]
    readonly shippingAddress: Address
}

export interface ShippingAddressChangedEvent extends OrderEvent {
    readonly type: "ShippingAddressChanged"
    readonly shippingAddress: Address
}

export interface OrderShippedEvent extends OrderEvent {
    readonly type: "OrderShipped"
    readonly shippingProvider: string
}
