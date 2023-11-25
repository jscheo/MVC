package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
// 클래스 명에 Ctrl + Shift + t 를 누르면 testService 를 자동으로 만들어준다.
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    // memberRepository를 외부에서 넣어주게 된다. 즉 DI가 일어남 dependency injection 이 일어났다.

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
     * 회원가입
     */
    public Long join(Member member){

        long strat = System.currentTimeMillis();
        // 같은 이름이 있는 중복 회원x
        try{
            extracted(member); // 중복회원 검증 매서드가 실행되는 부분은 따로 리팩토링해서 빼는 것이 좋다.

            memberRepository.save(member);
            return member.getId();
        }finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - strat;
            System.out.println("timeMs = " + timeMs);
        }

    }

    private void extracted(Member member) {
        memberRepository.findByName(member.getName())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 회원입니다.");
                });
    }

    /**
     * 전체회원 조회
     */
    public List<Member> findMembers(){
        long start = System.currentTimeMillis();
        try{
            return memberRepository.findAll();
        }finally {
            long finish = System.currentTimeMillis();
            long timeMs = finish - start;
            System.out.println("findMembers =" + timeMs + "ms");
        }

    }

    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
