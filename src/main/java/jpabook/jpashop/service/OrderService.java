package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문하기
     * @param memberId 회원 ID
     * @param itemId 상품 ID
     * @param count 수량
     * @return 주문 ID
     */
    @Transactional
    public Long order(String memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송 정보 생성 (Cascade:All , 따로 JPA 넣어주지 않아도 됨 -> 단일 참조,lifeCycle 함께 할 시에만...)
        Delivery delivery = new Delivery();
        delivery.setStatus(DeliveryStatus.PENDING);
        delivery.setDeliveryAddress(member.getAddress());   //기본 회원 배송지

        // 주문 상품 생성 (Cascade:All , 따로 JPA 넣어주지 않아도 됨)
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count); //기본가격, 단일 주문 상품

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 정보 저장
        orderRepository.save(order);
        return order.getId();
    }

    /**
     * 주문 취소
     * @param orderId 주문 ID
     */
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //취소
        order.cancel();
    }
/*
    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch)
    }
*/
}
