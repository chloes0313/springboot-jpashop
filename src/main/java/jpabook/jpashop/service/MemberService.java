package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) //성능 최적화, 쓰기 필요한 메서드에 별도로 지적
@RequiredArgsConstructor // final field만 가지고 생성자 생성
public class MemberService {
    //@Autowired : 필드 직접 주입 (Field Injection)
    //final : 컴파일 시 주입 여부 체크 가능
    private final MemberRepository memberRepository;

    // Setter Injection : 바로 주입하지 않고 bean property 등으로 전달 받아 주입 받을 수 있음!
    // 테스트 케이스 실행 시 Mock 주입하여 사용 가능
    // 어디서든 변경이 가능해 지므로 유의(public)
    /*@Autowired
    public void setMemberRepository(MemberRepository memberRepository) {

        this.memberRepository = memberRepository;
    }*/

    //Constructor Injection : 생성자에서 주입
    //@Autowired : 생성자 1개 일때 스프링에서 찾아서 자동 주입
    /*public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }*/


    /**
     * 회원 가입
     * @param member 회원
     * @return Long 회원ID
     */
    @Transactional
    public String join(Member member) {
        validateDuplicatedMember(member);// 중복회원 검증 -> Exception 발생
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 회원 조회 - 전체
     * @return List<Member>
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원 조회 - ID
     * @param id 회원 ID
     * @return Member
     */
    public Member findOne(String id) {
        return memberRepository.findOne(id);
    }

    /**
     * 회원 조회 - 이름
     * @param name 회원 이름
     * @return List
     */
    public List<Member> findMembers(String name) {
        return memberRepository.findByName(name);
    }

    /**
     * 중복 회원 검증
     * @param member 회원
     */
    private void validateDuplicatedMember(Member member) {
        //동시성 문제 해결을 위해 -> DB에서 id 유니크 제약 조건...
        Member findMember = memberRepository.findOne(member.getId());
        System.out.println("findMember : "+ findMember);
        if(findMember != null) {
            throw new IllegalStateException("이미 존재하는 회원ID 입니다.");
        }
    }
}