package br.com.va4e.gidac.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.va4e.gidac.entity.MemberEmail;

@Repository
public interface MemberEmailRepository extends JpaRepository<MemberEmail,Long>{
	
	Page<MemberEmail> findByMemberId(Long memberId, Pageable pageable);
	MemberEmail findById(Long memberPhoneId);

}