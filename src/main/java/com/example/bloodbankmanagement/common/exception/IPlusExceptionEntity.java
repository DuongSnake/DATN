package com.example.bloodbankmanagement.common.exception;

import com.example.bloodbankmanagement.common.untils.DateUtil;
import com.example.bloodbankmanagement.common.untils.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class IPlusExceptionEntity {

	public static final String TRACE_ID = "trace.id";
	private String responseMsg;
	private String responseCd;
	private String responseTs;
	private String traceId;
	private List<FieldError> errors;



	private IPlusExceptionEntity(final ErrorCode code, final List<FieldError> errors) {
		this.responseMsg = code.getMessage();
		this.errors = errors;
		this.responseCd = code.getCode();
		this.responseTs = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DATETIME_FORMAT_NORMAL_SSS));
		this.traceId = MDC.get(TRACE_ID);
	}

	private IPlusExceptionEntity(final ErrorCode code) {
		this.responseMsg = code.getMessage();
		this.responseCd = code.getCode();
		this.errors = new ArrayList<>();
		this.responseTs = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DateUtil.DATETIME_FORMAT_NORMAL_SSS));
		this.traceId = MDC.get(TRACE_ID);
	}


	public static IPlusExceptionEntity of(final ErrorCode code, final BindingResult bindingResult) {
		return new IPlusExceptionEntity(code, FieldError.of(bindingResult));
	}

	public static IPlusExceptionEntity of(final ErrorCode code) {
		return new IPlusExceptionEntity(code);
	}

	public static IPlusExceptionEntity of(final ErrorCode code, final List<FieldError> errors) {
		return new IPlusExceptionEntity(code, errors);
	}

	public static IPlusExceptionEntity of(MethodArgumentTypeMismatchException e) {
		final String value = ObjectUtils.isEmpty(e.getValue()) ? "" : String.valueOf(e.getValue());
		final List<FieldError> errors = FieldError.of(e.getName(), value, e.getErrorCode());
		return new IPlusExceptionEntity(ErrorCode.INVALID_TYPE_VALUE, errors);
	}


	@Getter
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@ToString
	public static class FieldError implements Serializable {
		private String field;
		private String value;
		private String reason;

		private FieldError(final String field, final String value, final String reason) {
			this.field = field;
			this.value = value;
			this.reason = reason;
		}

		public static List<FieldError> of(final String field, final String value, final String reason) {
			List<FieldError> fieldErrors = new ArrayList<>();
			fieldErrors.add(new FieldError(field, value, reason));
			return fieldErrors;
		}

		public static List<FieldError> of(final BindingResult bindingResult) {
			final List<org.springframework.validation.FieldError> fieldErrors = bindingResult.getFieldErrors();
			return fieldErrors.stream()
					.map(error -> new FieldError(
							error.getField().replace("data.", ""),
							error.getRejectedValue() == null ? "" : error.getRejectedValue().toString(),
							error.getDefaultMessage()))
					.collect(Collectors.toList());
		}
		public static FieldError from(final String field, final String value, final String reason) {
			return new FieldError(field, value, reason);
		}
	}

}
