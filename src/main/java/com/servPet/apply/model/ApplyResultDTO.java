package com.servPet.apply.model;

public class ApplyResultDTO<T> {
	 private boolean success;
	    private T data;
	    private String message;

	    public ApplyResultDTO(boolean success, T data, String message) {
	        this.success = success;
	        this.data = data;
	        this.message = message;
	    }

	    public static <T> ApplyResultDTO<T> success(T data, String message) {
	        return new ApplyResultDTO<>(true, data, message);
	    }

	    public static <T> ApplyResultDTO<T> failure(String message) {
	        return new ApplyResultDTO<>(false, null, message);
	    }

		public boolean isSuccess() {
			return success;
		}

		public void setSuccess(boolean success) {
			this.success = success;
		}

		public T getData() {
			return data;
		}

		public void setData(T data) {
			this.data = data;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	    
}
