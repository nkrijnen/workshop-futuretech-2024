import { assert, describe, expect, it } from 'vitest'
import { Address, type LegoSetNumber } from '../src/model.js'
import { CreateOrderCommand, OrderHandler } from '../src/orderhandler.js'

describe('box handler', () => {
    const millenniumFalcon: LegoSetNumber = 75192
    const campusAddress = new Address("One Infinite Loop", "CA 95014", "Cupertino", "United States")
    const operaHouseAddress = new Address("2 Macquarie Street", "NSW 2000", "Sydney", "Australia")

    it('should create order', () => {
        const event = OrderHandler.handleCreate(
            new CreateOrderCommand([millenniumFalcon], campusAddress)
        )

        assert.equal(campusAddress, event.shippingAddress)
    })
})
