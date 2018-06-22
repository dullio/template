package br.com.va4e.gidac.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.va4e.gidac.entity.MemberAddress;

@Repository
public interface MemberAddressRepository extends JpaRepository<MemberAddress,Long>{
	
	
	Page<MemberAddress> findByMemberId(Long memberId, Pageable pageable);
	MemberAddress findById(Long memberAddressId);

}