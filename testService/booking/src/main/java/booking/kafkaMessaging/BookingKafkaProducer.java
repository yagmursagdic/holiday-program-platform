package booking.kafkaMessaging;

import io.micronaut.configuration.kafka.annotation.KafkaClient;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaClient
public interface BookingKafkaProducer {

  @Topic("booking-events")
  void sendBookingEvent(String message);

  /*
   * or
   * @Topic("bookings")
    void sendBooking(Booking booking);
   */
    
}
