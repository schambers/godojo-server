/* Storage definition for a Joseki */

package com.greenasjade.godojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.ArrayList;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Transient;

@NodeEntity
public class Joseki {
	
	@Transient 
	private static final Logger log = LoggerFactory.getLogger(Joseki.class);
	
	@Id @GeneratedValue private Long id;
	
    @Relationship("MOVE")
    public List<Move> moves;
    
    @Property("name")
    public String name;
    
	public Joseki() {
		// Empty constructor required as of Neo4j API 2.0.5
	}
	
	public Joseki(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
    public String toString() {
    	return this.name + ": " + Optional.ofNullable(this.moves).orElse(
				Collections.emptyList()).stream()
				.map(Move::getPlacement)
				.collect(Collectors.toList());
    }
    
    public void addMove(Move move) {
		if (this.moves == null) {
			this.moves = new ArrayList<>();
		}    	
    	this.moves.add(move);
    	move.addJoseki(this);
    }
} 
