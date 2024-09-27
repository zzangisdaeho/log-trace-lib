package com.autocrypt.logtracer.v3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class OrderServiceV3 {

    private final OrderRepositoryV3 orderRepository;
    private final TestEntityRepository testEntityRepository;


    public void orderItem(String itemId) {
        orderRepository.save(itemId);
    }

    public long dbInsertTest() {
        return testEntityRepository.save(new TestEntity()).getId();
    }
}
