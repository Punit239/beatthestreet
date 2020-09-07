package com.example.beatthestreet.requests;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
public class EntityRequest {
	
	private final String entitySymbol;
	private final String period;
	
	private EntityRequest(EntityRequestBuilder entityRequestBuilder) {
		this.entitySymbol = entityRequestBuilder.entitySymbol;
		this.period = entityRequestBuilder.period;
	}
	
	public static class EntityRequestBuilder {
		
		private String entitySymbol;
		private String period;
		
		public EntityRequestBuilder setEntitySymbol(String entitySymbol) {
			this.entitySymbol = entitySymbol;
			return this;
		}
		
		public EntityRequestBuilder setPeriod(String period) {
			this.period = period;
			return this;
		}
		
		public EntityRequest build() {
			return new EntityRequest(this);
		}
	}
}