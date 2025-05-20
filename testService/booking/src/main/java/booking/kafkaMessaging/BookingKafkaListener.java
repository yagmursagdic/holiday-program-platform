package booking.kafkaMessaging;

import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;

@KafkaListener(groupId = "booking")
public class BookingKafkaListener {

  @Topic("booking-events")
  public void receive(String message) {
    System.out.println("Received: " + message);
  }

  /*
   * or
   * @Topic("bookings")
    public void receive(Booking booking) {
    System.out.println("Received: " + booking);
  }
   */
    
}
