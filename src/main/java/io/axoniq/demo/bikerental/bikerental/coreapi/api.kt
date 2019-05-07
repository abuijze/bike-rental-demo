package io.axoniq.demo.bikerental.bikerental.coreapi

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RegisterBikeCommand(@TargetAggregateIdentifier val bikeId: String, val location: String)
data class RentBikeCommand(@TargetAggregateIdentifier val bikeId: String, val renter: String)
data class ReturnBikeCommand(@TargetAggregateIdentifier val bikeId: String, val location: String)

data class BikeRegisteredEvent(val bikeId: String, val location: String)
data class BikeRentedEvent(val bikeId: String, val renter: String)
data class BikeReturnedEvent(val bikeId: String, val location: String)
