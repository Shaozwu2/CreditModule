package com.kooppi.nttca.portal.common.domain;

public class ModifiableEvent {

	private Modifiable modifiable;
	
	public ModifiableEvent(Modifiable modifiable){
		this.modifiable = modifiable;
	}

	public Modifiable getModifiable() {
		return modifiable;
	}

	public void setModifiable(Modifiable modifiable) {
		this.modifiable = modifiable;
	}
	
}

