export type LegoSetNumber = number

export class Address {
    constructor(
        readonly street: string,
        readonly postalCode: string,
        readonly city: string,
        readonly country: string,
    ) { }
}
