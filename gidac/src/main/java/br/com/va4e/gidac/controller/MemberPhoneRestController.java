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
import br.com.va4e.gidac.entity.MemberPhone;
import br.com.va4e.gidac.repository.MemberPhoneRepository;
import br.com.va4e.gidac.repository.MemberRepository;
import br.com.va4e.gidac.services.NotificationService;


@RestController
@RequestMapping("/api")
public class MemberPhoneRestController {

	@Autowired
	private MemberPhoneRepository memberPhoneRepository;
	
	
	@Autowired
	private MemberRepository memberRepository;
	
	 @Autowired
     private NotificationService notifyService;
	 
	 @Autowired
	 private Environment env;	
	
	 // -------------------Retrieve All Member's Phones--------------------------------------------
	 
    @GetMapping("/member/{memberId}/phone/")
    public ResponseEntity<?> listAllPhonesByMembersId(@PathVariable (value = "memberId") Long memberId, Pageable pageable) {

    	Page<MemberPhone> phonePage = memberPhoneRepository.findByMemberId(memberId, pageable)	;

        if (!phonePage.hasContent()) {
        	notifyService.addErrorMessage(env.getProperty("NotFound.member.id") + memberId);
        	return ResponseEntity.notFound().build();
        }
        
        return new ResponseEntity<Page<MemberPhone>>(phonePage, HttpStatus.OK);

    }
	
	
 // -------------------Retrieve Single Member's Phone------------------------------------------
    

    @GetMapping("/member/{memberId}/phone/{phoneId}/")
    public ResponseEntity<?> getMemberPhone(@PathVariable("memberId") long memberId, @PathVariable("phoneId") long phoneId) {

        MemberPhone memberPhone = memberPhoneRepository.findById(phoneId);
        

        if (memberPhone == null) {
        	notifyService.addErrorMessage(env.getProperty("NotFound.address.id") + memberPhone);
        	 return ResponseEntity.notFound().build();
           
        }
        
        return new ResponseEntity<MemberPhone>(memberPhone, HttpStatus.OK);

    }
	
	
    // -------------------Create a Member's Phone -------------------------------------------

    @PostMapping("/member/{memberId}/phone/")
    public ResponseEntity<?> createMemberPhone(@RequestBody MemberPhone memberPhone, @PathVariable("memberId") long memberId) {
 
    	Member actualMember = memberRepository.findById(memberId);
    	
    	if(actualMember==null) {

            notifyService.addErrorMessage("Member id: " + memberId + " not found.");
            
            return ResponseEntity.notFound().build();
    		
    	}
    	
    	memberPhone.setMember(actualMember);
    	
    	
    	MemberPhone testPhone = new MemberPhone();

    	testPhone.setDdd(memberPhone.getDdd());
    	testPhone.setPhone(memberPhone.getPhone());
    	testPhone.setExtension(memberPhone.getExtension());
    	testPhone.setType(memberPhone.getType());
    	testPhone.setMember(memberPhone.getMember());
                        
    	Example<MemberPhone> example = Example.of(testPhone);
    	
    	List<MemberPhone> results = memberPhoneRepository.findAll(example);
    	
    	if(!results.isEmpty()) {
    		
    		notifyService.addErrorMessage("Address: " + memberPhone.toString() + " already exists.");
    		
    		return ResponseEntity.status(HttpStatus.CONFLICT).build();
    		
    	}
    	
    	 return  new ResponseEntity<MemberPhone>(memberPhoneRepository.save(memberPhone), HttpStatus.CREATED);

    }
 
 // ------------------- Update a Member's Phone ------------------------------------------------	
    @PutMapping("/member/{memberId}/phone/{phoneId}/")
    public ResponseEntity<?> updateMemberPhone(@Valid @RequestBody MemberPhone memberPhone, @PathVariable("memberId") long memberId, @PathVariable("phoneId") long phoneId) {

    	
    	Member currentMember = memberRepository.findById(memberId);
 
        if (currentMember == null) {

            notifyService.addErrorMessage("Member id " + memberId + " not found.");
            return ResponseEntity.notFound().build();
            
        }
        
        MemberPhone actualMemberPhone = memberPhoneRepository.findById(phoneId);

        if(actualMemberPhone == null) {
        	
        	 notifyService.addErrorMessage("Member's Phone id " + phoneId + " not found.");
        	 return ResponseEntity.notFound().build();
             
        }

        memberPhone.setMember(currentMember);
        memberPhone.setId(phoneId);

        return  new ResponseEntity<MemberPhone>(memberPhoneRepository.save(memberPhone), HttpStatus.OK);
    }
    
    // ------------------- Delete a Member's Phone-----------------------------------------
    
    @DeleteMapping("/member/{memberId}/phone/{phoneId}/")
    public ResponseEntity<?> deleteMemberPhone(@PathVariable("phoneId") long phoneId) {

        MemberPhone actualPhone = memberPhoneRepository.findById(phoneId);
        
        if (actualPhone == null) {
        	
        	notifyService.addErrorMessage("Unable to delete. Member's Phone id: " + phoneId + " not found.");
        	return ResponseEntity.notFound().build();
        	
        }
        
        memberPhoneRepository.delete(phoneId);
        
        return ResponseEntity.ok().build();
    }
    
}
