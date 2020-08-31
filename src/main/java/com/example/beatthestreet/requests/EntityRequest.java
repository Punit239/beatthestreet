package com.example.beatthestreet.requests;

public class EntityRequest {
	
	private final String entitySymbol;
	private final String dataType;
	
	private EntityRequest(EntityRequestBuilder entityRequestBuilder) {
		this.entitySymbol = entityRequestBuilder.entitySymbol;
		this.dataType = entityRequestBuilder.dataType;
	}
	
	public String getEntitySymbol() {
		return entitySymbol;
	}

	public String getDataType() {
		return dataType;
	}

	@Override
	public boolean equals(Object o) {

		if(o == null) {
			return false;
		} else if(o == this) {
			return true;
		} else if(o.getClass() != this.getClass()) {
			return false;
		}
		EntityRequest entityRequest = (EntityRequest) o;
		return this.dataType.equals(entityRequest.dataType) && this.entitySymbol.equals(entityRequest.entitySymbol);
	}

	@Override
	public int hashCode() {
		return this.entitySymbol.hashCode() + this.dataType.hashCode();
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