### Business Use Case: Flight Booking Price Guarantee and Validation

#### Overview
Implement a `BookingService` fragment that validates a booking request and calculates the final price, considering dynamic pricing rules and a "Price Guarantee" period. The logic should be encapsulated in a single class that interacts with external data via mocks or caches.

#### Entities
1.  **Flight**: Represents a scheduled flight.
    *   `flightId: String`
    *   `basePrice: Double`
    *   `availableSeats: Int`
    *   `departureTime: LocalDateTime`
2.  **BookingRequest**: Data transfer object for the booking attempt.
    *   `flightId: String`
    *   `passengerCount: Int`
    *   `quoteId: String` (Reference to a previously issued price quote)
3.  **PriceQuote**: A temporary record of a price offered to the user.
    *   `quoteId: String`
    *   `quotedPrice: Double`
    *   `expirationTime: LocalDateTime`

#### Requirements for Implementation
The business logic should perform the following steps:
1.  **Availability Check**: Verify if the requested number of seats is available for the given `flightId`.
2.  **Price Validation**:
    *   Retrieve the `PriceQuote` using the `quoteId`.
    *   If the quote has not expired, use the `quotedPrice`.
    *   If the quote is expired or missing, calculate a new price based on the `Flight`'s `basePrice` plus a "last-minute" surcharge (e.g., +20%) if the departure is within 24 hours.
3.  **Consistency Check**: Ensure the calculated/quoted price does not exceed a maximum variance (e.g., 50%) from the current `basePrice` in the system to prevent booking on stale or erroneous data.
4.  **Finalization**: Return a `BookingResult` containing the confirmed price and a status (SUCCESS, EXPIRED_QUOTE_NEW_PRICE_APPLIED, or REJECTED_NO_SEATS).

#### Dependencies (To be Mocked)
*   `FlightCache`: To retrieve the latest `Flight` entity.
*   `QuoteCache`: To retrieve the `PriceQuote` by ID.
*   `Clock`: To handle time-sensitive logic (expiration and last-minute checks).

#### Complexity Level: Moderate
This use case is of moderate complexity because it involves conditional logic based on time, coordination between multiple data sources (Flights and Quotes), and business rules for price adjustment and validation, all while remaining suitable for a single-class implementation.