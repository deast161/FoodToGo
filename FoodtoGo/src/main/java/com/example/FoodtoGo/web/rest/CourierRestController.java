package com.example.FoodtoGo.web.rest;

import com.example.FoodtoGo.entity.*;
import com.example.FoodtoGo.repository.ConsumerRepository;
import com.example.FoodtoGo.repository.CourierRepository;
import com.example.FoodtoGo.repository.OrderRepository;
import com.example.FoodtoGo.web.dto.*;
import lombok.RequiredArgsConstructor;
import org.hibernate.engine.spi.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.JsonPath;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@RequiredArgsConstructor
@RestController
@CrossOrigin("*")
@RequestMapping(value="/api")
@CrossOrigin("*")
public class CourierRestController {
    @Autowired
    private CourierRepository courierRepository;
    private OrderRepository orderRepository;
    private ConsumerRepository consumerRepository;
    @PostMapping(
            value = "/registerCourier",
            consumes = {"application/json"}
    )
    public ResponseEntity<?> register(
            @RequestBody Courier courier
    ){
        if (courier.getCourierUsername().trim().isEmpty()) {
            HttpError httpError = new HttpError("Username cannot be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(httpError);
        }


        //Saves the consumer to the Database
        courierRepository.save(courier);
        return ResponseEntity.status(HttpStatus.CREATED).body(courier);
    }
    @GetMapping(
            value = "/courierlogin",
            produces = {
                    "application/json"
            },
            params = {"user"}
    )
    public ResponseEntity<?> getCourierByUsername(
            @RequestParam(name="user")String username
    ){
        if(username.isEmpty()) {
            HttpError httpError = new HttpError("Username cannot be empty");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(httpError);
        }
        Courier courier = courierRepository.findByCourierUsername(username);
        if (courier == null) {
            HttpError httpError = new HttpError("Username cannot be found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(httpError);
        }
        CourierDto courierDto = new CourierDto(courier.getCourierId(), courier.getCourierUsername(),courier.getCourierName(),courier.getHasOrder(),courier.getDateOfBirth(),courier.getLicenseNumber(),courier.getCarMake(),courier.getAccountType());
        return ResponseEntity.ok(courierDto);
    }
    @GetMapping(
            value = "{statusOrder}",
            produces = {
                    "application/json"
            }

    )
    public Iterable<Order> findByStatus(@PathVariable String statusOrder){
        return orderRepository.findByStatus(statusOrder);
    }
    @PatchMapping (
            value = "{statusOrder}",
            produces = {
                    "application/json"
            }
    )

    public ResponseEntity<?> patch(@RequestBody UpdateDeliveryStatus request){
        Order order=orderRepository.findById(request.getId()).get();
        order.setStatus(DeliveryStatus.valueOf(request.getStatus()));
        order=orderRepository.save(order);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }






}







