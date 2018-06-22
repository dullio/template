package br.com.va4e.gidac.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.va4e.gidac.entity.MemberPhone;

@Repository
public interface MemberPhoneRepository extends JpaRepository<MemberPhone,Long>{

	
	Page<MemberPhone> findByMemberId(Long memberId, Pageable pageable);
	MemberPhone findById(Long memberPhoneId);

}