package com.common.dto;

import com.common.enums.CodeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonResponseDto<T> {
    private int resultCode;
    private String resultMessage;
    private  T result;

    public CommonResponseDto(CodeEnum codeEnum) {
        this.resultCode = codeEnum.getCode();
        this.resultMessage = codeEnum.getMessage();
    }

    public CommonResponseDto(int resultCode, String resultMessage) {
        this.resultCode = resultCode;
        this.resultMessage = resultMessage;
    }

    public CommonResponseDto(T result) {
        this.resultCode = CodeEnum.SUCCESS.getCode();
        this.resultMessage = CodeEnum.SUCCESS.getMessage();
        this.result = result;
    }

    public CommonResponseDto(CodeEnum codeEnum, T result) {
        this.resultCode = codeEnum.getCode();
        this.resultMessage = codeEnum.getMessage();
        this.result = result;
    }

    public static <T> ResponseEntity<CommonResponseDto> success(T result) {
        return ResponseEntity.status(CodeEnum.SUCCESS.getCode()).body(new CommonResponseDto<>(result));
    }

    public static ResponseEntity<CommonResponseDto> error() {
        return ResponseEntity.status(CodeEnum.ERROR.getCode()).body(new CommonResponseDto<>(CodeEnum.ERROR));
    }

    public static ResponseEntity<CommonResponseDto> error(int resultCode, String resultMessage) {
        return ResponseEntity.status(resultCode).body(new CommonResponseDto(resultCode, resultMessage));
    }

    public static ResponseEntity<CommonResponseDto> error(CodeEnum codeEnum) {
        return ResponseEntity.status(codeEnum.getCode()).body(new CommonResponseDto<>(codeEnum));
    }


}
