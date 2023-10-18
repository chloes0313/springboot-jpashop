package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    /**
     * 상품 저장
     * @param item 상품
     */
    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    /**
     * 상품 조회 - 전체
     * @return 상품 리스트
     */
    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    /**
     * 상품 조회 - 이름
     * @param itemNm 상품명
     * @return 상품 리스트
     */
    public List<Item> findItemsByName(String itemNm) {
        return itemRepository.findByName(itemNm);
    }

    /**
     * 상품 조회 - ID
     * @param itemId 상품ID
     * @return 상품
     */
    public Item findItem(Long itemId) {
        return itemRepository.findOne(itemId);
    }

}
