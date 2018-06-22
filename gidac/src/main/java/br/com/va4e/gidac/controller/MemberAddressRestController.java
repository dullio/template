package br.com.va4e.gidac.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.va4e.gidac.entity.Member;
import br.com.va4e.gidac.entity.MemberAddress;
import br.com.va4e.gidac.repository.MemberAddressRepository;
import br.com.va4e.gidac.repository.MemberRepository;
import br.com.va4e.gidac.services.NotificationService;


@RestController
@RequestMapping("/api")
public class MemberAddressRestController {

	@Autowired
	private MemberAddressRepository memberAddressRepository;
	
	
	@Autowired
	private MemberRepository memberRepository;
	
	 @Autowired
     private NotificationService notifyService;
	 
	 @Autowired
	 private Environment env;	
	
	 // -------------------Retrieve All Member's Addresses--------------------------------------------
	 
    @GetMapping("/member/{memberId}/address/")
    public ResponseEntity<?> listAllAddressesByMembersId(@PathVariable (value = "memberId") Long memberId, Pageable pageable) {

    	Page<MemberAddress> addressPage = memberAddressRepository.findByMemberId(memberId, pageable)	;

        if (!addressPage.hasContent()) {
        	notifyService.addErrorMessage(env.getProperty("NotFound.member.id") + memberId);
        	return ResponseEntity.notFound().build();
        }
        
        return new ResponseEntity<Page<MemberAddress>>(addressPage, HttpStatus.OK);

    }
	
	
 // -------------------Retrieve Single Member's Address------------------------------------------
    

    @GetMapping("/member/{memberId}/address/{addressId}/")
    public ResponseEntity<?> getMemberAddress(@PathVariable("memberId") long memberId, @PathVariable("addressId") long addressId) {

        MemberAddress memberAddress = memberAddressRepository.findById(addressId);
        

        if (memberAddress == null) {
        	notifyService.addErrorMessage(env.getProperty("NotFound.address.id") + addressId);
        	 return ResponseEntity.notFound().build();
           
        }
        
        return new ResponseEntity<MemberAddress>(memberAddress, HttpStatus.OK);

    }
	
	
    // -------------------Create a Member's Address -------------------------------------------

    @PostMapping("/member/{memberId}/address/")
    public ResponseEntity<?> createMemberAddress(@RequestBody MemberAddress memberAddress, @PathVariable("memberId") long memberId) {
 
    	Member actualMember = memberRepository.findById(memberId);
    	
    	if(actualMember==null) {

            notifyService.addErrorMessage("Member id: " + memberId + " not found.");
            
            return ResponseEntity.notFound().build();
    		
    	}
    	
    	memberAddress.setMember(actualMember);
    	
    	
    	MemberAddress testAddress = new MemberAddress();

    	testAddress.setMember(memberAddress.getMember());
    	testAddress.setStreet(memberAddress.getStreet());
    	testAddress.setNumber(memberAddress.getNumber());
    	testAddress.setComplement(memberAddress.getComplement());
    	testAddress.setCep(memberAddress.getCep());
    	testAddress.setCity(memberAddress.getCity());
    	testAddress.setState(memberAddress.getState());
    	testAddress.setCountry(memberAddress.getCountry());
    	testAddress.setType(memberAddress.getType());

                         
    	Example<MemberAddress> example = Example.of(testAddress);
    	
    	List<MemberAddress> results = memberAddressRepository.findAll(example);
    	
    	if(!results.isEmpty()) {
    		
    		notifyService.addErrorMessage("Address: " + memberAddress.toString() + " already exists.");
    		
    		return ResponseEntity.status(HttpStatus.CONFLICT).build();
    		
    	}
    	
    	 return  new ResponseEntity<MemberAddress>(memberAddressRepository.save(memberAddress), HttpStatus.CREATED);

    }
 
 // ------------------- Update a Member's Address ------------------------------------------------	
    @PutMapping("/member/{memberId}/address/{addressId}/")
    public ResponseEntity<?> updateMemberAddress(@Valid @RequestBody MemberAddress memberAddress, @PathVariable("memberId") long memberId, @PathVariable("addressId") long addressId) {
    	//MemberAddress
    	
    	Member currentMember = memberRepository.findById(memberId);
 
        if (currentMember == null) {

            notifyService.addErrorMessage("Member id " + memberId + " not found.");
            return ResponseEntity.notFound().build();
            
        }
        
        MemberAddress actualMemberAddress = memberAddressRepository.findById(addressId);

        if(actualMemberAddress == null) {
        	
        	 notifyService.addErrorMessage("Member's Address id " + addressId + " not found.");
        	 return ResponseEntity.notFound().build();
             
        }

        memberAddress.setMember(currentMember);
        memberAddress.setId(addressId);

        return  new ResponseEntity<MemberAddress>(memberAddressRepository.save(memberAddress), HttpStatus.OK);
    }
    
    // ------------------- Delete a Member's Address-----------------------------------------
    
    @DeleteMapping("/member/{memberId}/address/{addressId}/")
    public ResponseEntity<?> deleteMemberAddress(@PathVariable("addressId") long addressId) {

        MemberAddress actualAddress = memberAddressRepository.findById(addressId);
        
        if (actualAddress == null) {
        	
        	notifyService.addErrorMessage("Unable to delete. Member's Address id: " + addressId + " not found.");
        	return ResponseEntity.notFound().build();
        	
        }
        
        memberAddressRepository.delete(addressId);
        
        return ResponseEntity.ok().build();
    }
    
}
