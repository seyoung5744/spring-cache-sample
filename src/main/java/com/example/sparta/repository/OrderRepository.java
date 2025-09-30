package com.example.sparta.repository;

import com.example.sparta.dto.OrderSimpleDto;
import com.example.sparta.entity.Order;
import com.example.sparta.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    // 주문 상세: 아이템과 상품까지 한 번에 로딩 (N+1 방지)
    @Query("""
           select o from Order o
           left join fetch o.orderItems oi
           left join fetch oi.product
           where o.id = :id
           """)
    Optional<Order> findByIdWithItems(@Param("id") Long id);

    // 사용자별 주문 요약(개수만): 페이징 + Group By 프로젝션
    @Query("""
           select new com.example.sparta.dto.OrderSimpleDto(o.id, count(oi.id))
           from Order o
           left join o.orderItems oi
           where o.user.id = :userId
           group by o.id
           """)
    Page<OrderSimpleDto> findSummariesByUserId(@Param("userId") Long userId, Pageable pageable);
}

