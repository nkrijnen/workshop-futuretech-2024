import { assert, describe, expect, it } from 'vitest'
import { Address, type LegoSetNumber } from '../src/model.js'
import { OrderHandler } from '../src/orderhandler.js'
import { ChangeShippingAddressCommand, CreateOrderCommand } from '../src/commands.js'
import type { OrderCreatedEvent, OrderShippedEvent } from '../src/events.js'

describe("box handler", () => {
    const millenniumFalcon: LegoSetNumber = 75192
    const campusAddress = new Address("One Infinite Loop", "CA 95014", "Cupertino", "United States")
    const operaHouseAddress = new Address("2 Macquarie Street", "NSW 2000", "Sydney", "Australia")

    it("should create order", () => {
        const event = OrderHandler.handleCreate(
            new CreateOrderCommand([millenniumFalcon], campusAddress)
        )

        assert.equal(campusAddress, event.shippingAddress)
    })

    it("should allow changing shipping address as long as it's not shipped yet", () => {
        const createdEvent: OrderCreatedEvent = {
            type: 'OrderCreated', atTime: new Date(), items: [millenniumFalcon], shippingAddress: campusAddress,
        }
        const history = [
            createdEvent,
        ]
        const handler = new OrderHandler(history)

        const event = handler.handleChangeShipping(new ChangeShippingAddressCommand(operaHouseAddress))

        assert.equal(operaHouseAddress, event.shippingAddress)
    })

    it("should not allow changing shipping address after being shipped", () => {
        const createdEvent: OrderCreatedEvent = {
            type: 'OrderCreated', atTime: new Date(), items: [millenniumFalcon], shippingAddress: campusAddress,
        }
        const shippedEvent: OrderShippedEvent = {
            type: 'OrderShipped', atTime: new Date(), shippingProvider: 'UPS',
        }
        const history = [
            createdEvent,
            shippedEvent,
        ]
        const handler = new OrderHandler(history)

        expect(() =>
            handler.handleChangeShipping(new ChangeShippingAddressCommand(operaHouseAddress))
        ).toThrowError('Already shipped')
    })
})
