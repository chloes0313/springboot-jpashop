package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember("member1","김회원");
        Book book = createBook("Effective Java", 12000, 8);

        int orderCount = 2;
        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order order = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 'ORDER' 여야한다.", OrderStatus.ORDER, order.getStatus());
        assertEquals("주문한 상품 종류 수가 정확 해야한다.", 1, order.getOrderItems().size());
        assertEquals("주문 사량만큼 재고가 줄어야 한다", 6 , book.getStockQuantity());
        assertEquals("주문 가격은 (주문 상품 가격 * 수량) 이다.", 12000 * orderCount, order.getTotalPrice());
    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember("member1", "김회원");
        Item item = createBook("Effective Java", 12000, 8);

        int orderCount = 10;

        //when
        orderService.order(member.getId(), item.getId(), orderCount);

        //then
        fail("재고수량 부족 예외가 발생하야한다!");

    }

    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember("beeeen_0613","햄빈");
        Item item = createBook("Melting Point", 17900, 100);

        int orderCount = 10;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order findOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 살태는 CANCEL.", OrderStatus.CANCEL, findOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가한다.", 100, item.getStockQuantity());
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember(String id, String name) {
        Member member = new Member();
        member.setId(id);
        member.setName(name);
        member.setAddress(new Address("용인시","금화로","12345"));
        em.persist(member);
        return member;
    }
}