package br.com.va4e.gidac.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
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
import org.springframework.web.util.UriComponentsBuilder;

import br.com.va4e.gidac.entity.Member;
import br.com.va4e.gidac.repository.MemberRepository;
import br.com.va4e.gidac.services.NotificationService;


@RestController
@RequestMapping("/api")
public class MemberRestController {

	@Autowired
	private MemberRepository memberRepository;
	
	 @Autowired
     private NotificationService notifyService;
	 
	 @Autowired
	 private Environment env;	
	 
	 
	 
	 //--------------------Filtered Members------------------------------------------------
	 @PostMapping("/member/filtered/")
	 public ResponseEntity<Page<Member>> getFilteredMembers(@RequestBody Member example, Pageable pageable ){
		 
		 
		 Page<Member> members = memberRepository.getFilteredMembers(example, pageable);
		 
		 if (!members.hasContent()) {
	            return new ResponseEntity<Page<Member>>(members,HttpStatus.NO_CONTENT);
	            // You many decide to return HttpStatus.NOT_FOUND
	        }
	        return new ResponseEntity<Page<Member>>(members, HttpStatus.OK);

		 
		 
	 }	 
	
	 // -------------------Retrieve All Members---------------------------------------------
	 
    @GetMapping("/member/")
    public ResponseEntity<List<Member>> listAllMembers() {
        List<Member> members = memberRepository.findAll();
        
        if (members.isEmpty()) {
            return new ResponseEntity<List<Member>>(members,HttpStatus.NO_CONTENT);
            // You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<Member>>(members, HttpStatus.OK);
    }
	
	
 // -------------------Retrieve Single Member------------------------------------------
    

    @GetMapping("/member/{id}")
    public ResponseEntity<?> getMember(@PathVariable("id") long id) {

        Member member = memberRepository.findOne(id);
        if (member == null) {
        	notifyService.addErrorMessage(env.getProperty("NotFound.member.id") + id);
        	return new ResponseEntity<Member>(member, HttpStatus.NOT_FOUND);
           
        }
        
        return new ResponseEntity<Member>(member, HttpStatus.OK);
    }
	
	
    // -------------------Create a Member-------------------------------------------
    

    @PostMapping("/member/")
    public ResponseEntity<?> createMember(@Valid @RequestBody Member member, UriComponentsBuilder ucBuilder) {
 

    	//Check if username exists
    	//TODO: Check other restrictions
    	
    	Member memberEx = new Member();

    	memberEx.setUserName(member.getUserName());
                         
    	Example<Member> example = Example.of(memberEx);
    	
    	List<Member> results = memberRepository.findAll(example);
    	
    	if(!results.isEmpty()) {
    		
    		notifyService.addErrorMessage("usermane " + member.getUserName() + "already exists.");
    		
    		return new ResponseEntity<Member>(member, HttpStatus.CONFLICT);
    		
    	}
    	memberRepository.save(member);
    	//memberRepository.save(member);
 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/member/{id}").buildAndExpand(member.getId()).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
 
 // ------------------- Update a Member ------------------------------------------------	
    @PutMapping("/member/{id}")
    public ResponseEntity<?> updateMember(@PathVariable("id") long id, @RequestBody Member member) {
    	
    	
    	Member currentMember = memberRepository.findOne(id);
 
        if (currentMember == null) {

            notifyService.addErrorMessage("Member id " + id + "not found.");
            return new ResponseEntity<Member>(member, HttpStatus.NOT_FOUND);
            
        }
        
        member.setId(id);

        return new ResponseEntity<Member>(memberRepository.save(member), HttpStatus.OK);
    }
    
    // ------------------- Delete a Member-----------------------------------------
    
    @DeleteMapping("/member/{id}")
    public ResponseEntity<?> deleteMember(@PathVariable("id") long id) {
 
        Member member  = memberRepository.findOne(id);
        if (member == null) {
        	notifyService.addErrorMessage("Unable to delete. Member id " + id + "not found.");
            return new ResponseEntity<Long>(id ,HttpStatus.NOT_FOUND);
        }
        memberRepository.delete(id);
        return new ResponseEntity<Member>(HttpStatus.OK);
    }
    
}
