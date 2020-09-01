package com.example.beatthestreet.requests;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class EntityRequest {
	
	private final String entitySymbol;
	private final String dataType;
	
	private EntityRequest(EntityRequestBuilder entityRequestBuilder) {
		this.entitySymbol = entityRequestBuilder.entitySymbol;
		this.dataType = entityRequestBuilder.dataType;
	}
	
	public static class EntityRequestBuilder {
		
		private String entitySymbol;
		private String dataType;
		
		public EntityRequestBuilder setEntitySymbol(String entitySymbol) {
			this.entitySymbol = entitySymbol;
			return this;
		}
		
		public EntityRequestBuilder setdataType(String dataType) {
			this.dataType = dataType;
			return this;
		}
		
		public EntityRequest build() {
			return new EntityRequest(this);
		}
	}
}