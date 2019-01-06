package com.greenasjade.godojo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Relationship;
import org.neo4j.ogm.annotation.Transient;


@NodeEntity
public class Move {
	
	@Transient 
	private static final Logger log = LoggerFactory.getLogger(Move.class);
	
	@Id @GeneratedValue private Long id;
	
	@Property("placement")
	private String placement;
	
	@Relationship(type="PARENT", direction=Relationship.INCOMING)
	BoardPosition parent;
	
	@Relationship("CHILD")
	BoardPosition child;
	
	@Relationship(type="MOVE", direction=Relationship.INCOMING)
	private Set<Joseki> joseki;
	
	public String getPlacement() {
		return placement;
	}
	
	public Move() {
		// Empty constructor required as of Neo4j API 2.0.5
	};

	public Move(BoardPosition parent, String placement, BoardPosition child) {
		this.parent = parent;
		this.placement = placement;
		this.child = child;
		child.setParent(this);
	}
	    
    public void addJoseki(Joseki joseki) {
		if (this.joseki == null) {
			this.joseki = new HashSet<>();
		}    	
    	this.joseki.add(joseki);
    }
    
    public String toString() {
    	String p = this.parent == null ? "(none)" : this.parent.getPlay();
    	String pl = this.placement == null ? "(none)" : this.placement;
    	String c = this.child == null ? "(none)" : this.child.getPlay();
    	List<String> j = Optional.ofNullable(this.joseki).orElse(
				Collections.emptySet()).stream()
				.map(Joseki::getName)
				.collect(Collectors.toList());
    	
    	return p + " -> " + pl + " -> " + c + " (" + j + ")";
    }
} 