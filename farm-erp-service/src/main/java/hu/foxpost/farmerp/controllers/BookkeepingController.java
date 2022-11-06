package hu.foxpost.farmerp.controllers;

import hu.foxpost.farmerp.dto.DeliveryDTO;
import hu.foxpost.farmerp.interfaces.IDeliveriesService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/booking")
@AllArgsConstructor
public class BookkeepingController {

    private final IDeliveriesService deliveriesService;

    // TODO: return deliveries
    public ArrayList<DeliveryDTO> getDeliveriesToBooking()  {
        return null;
    }
}
