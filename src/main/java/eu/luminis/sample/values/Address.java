package eu.luminis.sample.values;

public record Address(
        String street,
        String postalCode,
        String city,
        String country
) {
}