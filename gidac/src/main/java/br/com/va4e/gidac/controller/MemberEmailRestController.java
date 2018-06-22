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
import br.com.va4e.gidac.entity.MemberEmail;
import br.com.va4e.gidac.repository.MemberEmailRepository;
import br.com.va4e.gidac.repository.MemberRepository;
import br.com.va4e.gidac.services.NotificationService;


@RestController
@RequestMapping("/api")
public class MemberEmailRestController {

	@Autowired
	private MemberEmailRepository memberEmailRepository;
	
	
	@Autowired
	private MemberRepository memberRepository;
	
	 @Autowired
     private NotificationService notifyService;
	 
	 @Autowired
	 private Environment env;	
	
	 // -------------------Retrieve All Member's Emails--------------------------------------------
	 
    @GetMapping("/member/{memberId}/email/")
    public ResponseEntity<?> listAllEmailsByMembersId(@PathVariable (value = "memberId") Long memberId, Pageable pageable) {

    	Page<MemberEmail> emailPage = memberEmailRepository.findByMemberId(memberId, pageable)	;

        if (!emailPage.hasContent()) {
        	notifyService.addErrorMessage(env.getProperty("NotFound.member.id") + memberId);
        	return ResponseEntity.notFound().build();
        }
        
        return new ResponseEntity<Page<MemberEmail>>(emailPage, HttpStatus.OK);

    }
	
	
 // -------------------Retrieve Single Member's Email------------------------------------------
    

    @GetMapping("/member/{memberId}/email/{emailId}/")
    public ResponseEntity<?> getMemberEmail(@PathVariable("memberId") long memberId, @PathVariable("emailId") long emailId) {

        MemberEmail memberEmail = memberEmailRepository.findById(emailId);
        

        if (memberEmail == null) {
        	notifyService.addErrorMessage(env.getProperty("NotFound.address.id") + emailId);
        	 return ResponseEntity.notFound().build();
           
        }
        
        return new ResponseEntity<MemberEmail>(memberEmail, HttpStatus.OK);

    }
	
	
    // -------------------Create a Member's Email -------------------------------------------

    @PostMapping("/member/{memberId}/email/")
    public ResponseEntity<?> createMemberEmail(@RequestBody MemberEmail memberEmail, @PathVariable("memberId") long memberId) {
 
    	Member actualMember = memberRepository.findById(memberId);
    	
    	if(actualMember==null) {

            notifyService.addErrorMessage("Member id: " + memberId + " not found.");
            
            return ResponseEntity.notFound().build();
    		
    	}
    	
    	memberEmail.setMember(actualMember);
    	
    	
    	MemberEmail testEmail = new MemberEmail();
    	
    	testEmail.setEmail(memberEmail.getEmail());
    	
    	testEmail.setMember(memberEmail.getMember());
    	
    	Example<MemberEmail> example = Example.of(testEmail);
    	
    	List<MemberEmail> results = memberEmailRepository.findAll(example);
    	
    	if(!results.isEmpty()) {
    		
    		notifyService.addErrorMessage("Address: " + memberEmail.toString() + " already exists.");
    		
    		return ResponseEntity.status(HttpStatus.CONFLICT).build();
    		
    	}
    	
    	 return  new ResponseEntity<MemberEmail>(memberEmailRepository.save(memberEmail), HttpStatus.CREATED);

    }
 
 // ------------------- Update a Member's Email ------------------------------------------------	
    @PutMapping("/member/{memberId}/email/{emailId}/")
    public ResponseEntity<?> updateMemberEmail(@Valid @RequestBody MemberEmail memberEmail, @PathVariable("memberId") long memberId, @PathVariable("emailId") long emailId) {

    	
    	Member currentMember = memberRepository.findById(memberId);
 
        if (currentMember == null) {

            notifyService.addErrorMessage("Member id " + memberId + " not found.");
            return ResponseEntity.notFound().build();
            
        }
        
        MemberEmail actualMemberEmail = memberEmailRepository.findById(emailId);

        if(actualMemberEmail == null) {
        	
        	 notifyService.addErrorMessage("Member's Email id " + emailId + " not found.");
        	 return ResponseEntity.notFound().build();
             
        }

        memberEmail.setMember(currentMember);
        memberEmail.setId(emailId);

        return  new ResponseEntity<MemberEmail>(memberEmailRepository.save(memberEmail), HttpStatus.OK);
    }
    
    // ------------------- Delete a Member's Email-----------------------------------------
    
    @DeleteMapping("/member/{memberId}/email/{emailId}/")
    public ResponseEntity<?> deleteMemberEmail(@PathVariable("emailId") long emailId) {

        MemberEmail actualEmail = memberEmailRepository.findById(emailId);
        
        if (actualEmail == null) {
        	
        	notifyService.addErrorMessage("Unable to delete. Member's Email id: " + emailId + " not found.");
        	return ResponseEntity.notFound().build();
        	
        }
        
        memberEmailRepository.delete(emailId);
        
        return ResponseEntity.ok().build();
    }
    
}
