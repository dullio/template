package br.com.va4e.gidac.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.va4e.gidac.entity.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findById(Long memberId);

	/*
	 * TODO: Corrigir consulta dos memberos filtrados
	 * 
	 * 
	 * 
	 */

	/*
	 * @Query("SELECT c from Contact c WHERE " +
	 * "CASE WHEN c.contactSSN is not null THEN c.contactSSN.ssn ELSE c.contactSSN END "
	 * + "LIKE CONCAT(COALESCE(:#{#example.contactSSN?.ssn},''),'%') " +
	 * "AND c.firstName LIKE CONCAT(COALESCE(:#{#example.firstName},''),'%') " +
	 * "AND c.lastName LIKE CONCAT(COALESCE(:#{#example.lastName},''),'%') " +
	 * "AND c.dateOfBirth = COALESCE(:#{#example.dateOfBirth},c.dateOfBirth) " +
	 * "AND COALESCE(c.married,FALSE) = COALESCE(:#{#example.married != null ? (!#example.married ? NULL : #example.married) : NULL},COALESCE(c.married,FALSE)) "
	 * +
	 * "AND COALESCE(c.children,0) = COALESCE(:#{#example.children},COALESCE(c.children,0)) "
	 * )
	 */

	@Query("SELECT c from Member c WHERE "
			//+ "CASE WHEN :#{#example.active} is not null THEN c.active=:#{#example.active} END "
			//+ "CASE WHEN :#{#example.active} is not null THEN AND c.active=:#{#example.active} END  AND"
			+ "c.firstName LIKE CONCAT(COALESCE(:#{#example.firstName},''),'%') "
			+ "AND c.lastName LIKE CONCAT(COALESCE(:#{#example.lastName},''),'%') "
			+ "AND c.userName LIKE CONCAT(COALESCE(:#{#example.userName},''),'%') "
			+ "AND c.cpf LIKE CONCAT(COALESCE(:#{#example.cpf},''),'%') "
			+ "AND c.rg LIKE CONCAT(COALESCE(:#{#example.rg},''),'%') "			
			+ "AND c.birthday = COALESCE(:#{#example.birthday},c.birthday) "
			+ "AND c.gender LIKE CONCAT(COALESCE(:#{#example.gender},''),'%') ")
			//+ "AND c.active = COALESCE(:#{#example.active != null ? (!#example.active ? NULL : #example.active) : NULL},COALESCE(c.active,FALSE)) "
			
	public Page<Member> getFilteredMembers(@Param(value = "example") Member example, Pageable pageable);

}