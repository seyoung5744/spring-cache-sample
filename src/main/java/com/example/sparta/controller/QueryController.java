package com.example.sparta.controller;

import com.example.sparta.dto.OrderReadDto;
import com.example.sparta.dto.OrderSimpleDto;
import com.example.sparta.dto.ProductReadDto;
import com.example.sparta.service.QueryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class QueryController {

    private final QueryService queryService;

    public QueryController(QueryService queryService) {
        this.queryService = queryService;
    }

    // 주문 상세
    @GetMapping("/orders/{orderId}")
    public OrderReadDto getOrder(@PathVariable Long orderId) {
        return queryService.getOrderDetail(orderId);
    }

    // 사용자별 주문 목록(요약)
    @GetMapping("/users/{userId}/orders")
    public Page<OrderSimpleDto> getUserOrders(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return queryService.getUserOrderSummaries(userId, pageable);
    }

    // 상품 목록
    @GetMapping("/products")
    public Page<ProductReadDto> getProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return queryService.listProducts(pageable);
    }
}