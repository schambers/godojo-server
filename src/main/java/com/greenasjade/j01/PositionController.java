package com.greenasjade.j01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class PositionController {

	private static final Logger log = LoggerFactory.getLogger(PositionController.class);
	
	private BoardPositionStore bp_store;
	private JosekiStore j_store;
	private MoveStore m_store;
	
	public PositionController(
			BoardPositionStore bp_store, 
			JosekiStore j_store,
			MoveStore m_store) {
		 this.bp_store = bp_store;
		 this.j_store = j_store;
		 this.m_store = m_store;
	}
	
    @RequestMapping("/position")
    public HttpEntity<Position> position(
            @RequestParam(value = "id", required = false, defaultValue = "root") String id) {

    	BoardPosition the_position; 
    	
    	log.info("Position request for: " + id);
    	
    	if (id.equals("root")) {
        	log.info("Looking up root...");
    		
    		the_position = this.bp_store.findByPlay("root");
    	} 
    	else {
    		the_position = this.bp_store.findById(Long.valueOf(id)).orElse(null);
    	}
    			
        Position position = new Position(the_position.getPlay());
        position.add(linkTo(methodOn(PositionController.class).position(id)).withSelfRel());

        return new ResponseEntity<>(position, HttpStatus.OK);
    }
}