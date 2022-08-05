package com.shah.bankingapplicationcrud.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorInfo {
    private List<ErrorDetail> errorDetail;

    public ErrorInfo(List<ErrorInfo.ErrorDetail> errorDetail) {
        this.errorDetail = errorDetail;
    }

    public ErrorInfo() {
    }

    public static ErrorInfo.ErrorInfoBuilder builder() {
        return new ErrorInfo.ErrorInfoBuilder();
    }

    public List<ErrorInfo.ErrorDetail> getErrorDetail() {
        if (null == this.errorDetail) {
            this.errorDetail = new ArrayList();
        }
        return this.errorDetail;
    }

    public void setErrorDetail(List<ErrorInfo.ErrorDetail> errorDetail) {
        this.errorDetail = errorDetail;
    }

    public ErrorInfo addErrorDetail(ErrorInfo.ErrorDetail errorDetail) {
        List<ErrorInfo.ErrorDetail> errorDetails = this.getErrorDetail();
        errorDetails.add(errorDetail);
        return this;
    }

    public String toString() {
        return "{\"errorDetail\":" + this.errorDetail + "}";
    }

    public static class ErrorInfoBuilder {
        private List<ErrorInfo.ErrorDetail> errorDetail;

        ErrorInfoBuilder() {
        }

        public ErrorInfo.ErrorInfoBuilder errorDetail(List<ErrorInfo.ErrorDetail> errorDetail) {
            this.errorDetail = errorDetail;
            return this;
        }

        public ErrorInfo build() {
            return new ErrorInfo(this.errorDetail);
        }

        public String toString() {
            return "ErrorInfo.ErrorInfoBuilder(errorDetail=" + this.errorDetail + ")";
        }
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDetail {
        public String errorType;
        public String errorCode;
        public String errorStatus;
        public String errorDescription;
        public String errorActor;
        public Object params;
        public List<ErrorInfo.ErrorDetail.ProviderError> providerError;

        public ErrorDetail(String errorType, String errorCode, String errorStatus, String errorDescription, String errorActor, Object params, List<ErrorInfo.ErrorDetail.ProviderError> providerError) {
            this.errorType = errorType;
            this.errorCode = errorCode;
            this.errorStatus = errorStatus;
            this.errorDescription = errorDescription;
            this.errorActor = errorActor;
            this.params = params;
            this.providerError = providerError;
        }

        public ErrorDetail() {
        }

        public static ErrorInfo.ErrorDetail.ErrorDetailBuilder builder() {
            return new ErrorInfo.ErrorDetail.ErrorDetailBuilder();
        }

        public List<ErrorInfo.ErrorDetail.ProviderError> getProviderError() {
            if (null == this.providerError) {
                this.providerError = new ArrayList();
            }
            return this.providerError;
        }

        public void setProviderError(List<ErrorInfo.ErrorDetail.ProviderError> providerError) {
            this.providerError = providerError;
        }

        public ErrorInfo.ErrorDetail addProviderError(ErrorInfo.ErrorDetail.ProviderError providerError) {
            List<ErrorInfo.ErrorDetail.ProviderError> providerErrors = this.getProviderError();
            providerErrors.add(providerError);
            return this;
        }

        public String toString() {
            return "{\"errorType\":\"" + this.errorType + "\", \"errorCode\":\"" + this.errorCode + "\", \"errorStatus\":\"" + this.errorStatus + "\", \"errorDescription\":\"" + this.errorDescription + "\", \"errorActor\":\"" + this.errorActor + "\", \"params\":\"" + this.params + "\", \"providerError\":" + this.providerError + "}";
        }

        public String getErrorType() {
            return this.errorType;
        }

        public void setErrorType(String errorType) {
            this.errorType = errorType;
        }

        public String getErrorCode() {
            return this.errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getErrorStatus() {
            return this.errorStatus;
        }

        public void setErrorStatus(String errorStatus) {
            this.errorStatus = errorStatus;
        }

        public String getErrorDescription() {
            return this.errorDescription;
        }

        public void setErrorDescription(String errorDescription) {
            this.errorDescription = errorDescription;
        }

        public String getErrorActor() {
            return this.errorActor;
        }

        public void setErrorActor(String errorActor) {
            this.errorActor = errorActor;
        }

        public Object getParams() {
            return this.params;
        }

        public void setParams(Object params) {
            this.params = params;
        }

        public static class ErrorDetailBuilder {
            private String errorType;
            private String errorCode;
            private String errorStatus;
            private String errorDescription;
            private String errorActor;
            private Object params;
            private List<ErrorInfo.ErrorDetail.ProviderError> providerError;

            ErrorDetailBuilder() {
            }

            public ErrorInfo.ErrorDetail.ErrorDetailBuilder errorType(String errorType) {
                this.errorType = errorType;
                return this;
            }

            public ErrorInfo.ErrorDetail.ErrorDetailBuilder errorCode(String errorCode) {
                this.errorCode = errorCode;
                return this;
            }

            public ErrorInfo.ErrorDetail.ErrorDetailBuilder errorStatus(String errorStatus) {
                this.errorStatus = errorStatus;
                return this;
            }

            public ErrorInfo.ErrorDetail.ErrorDetailBuilder errorDescription(String errorDescription) {
                this.errorDescription = errorDescription;
                return this;
            }

            public ErrorInfo.ErrorDetail.ErrorDetailBuilder errorActor(String errorActor) {
                this.errorActor = errorActor;
                return this;
            }

            public ErrorInfo.ErrorDetail.ErrorDetailBuilder params(Object params) {
                this.params = params;
                return this;
            }

            public ErrorInfo.ErrorDetail.ErrorDetailBuilder providerError(List<ErrorInfo.ErrorDetail.ProviderError> providerError) {
                this.providerError = providerError;
                return this;
            }

            public ErrorInfo.ErrorDetail build() {
                return new ErrorInfo.ErrorDetail(this.errorType, this.errorCode, this.errorStatus, this.errorDescription, this.errorActor, this.params, this.providerError);
            }

            public String toString() {
                return "ErrorInfo.ErrorDetail.ErrorDetailBuilder(errorType=" + this.errorType + ", errorCode=" + this.errorCode + ", errorStatus=" + this.errorStatus + ", errorDescription=" + this.errorDescription + ", errorActor=" + this.errorActor + ", params=" + this.params + ", providerError=" + this.providerError + ")";
            }
        }

        public static class ProviderError {
            public String providerErrorCode;
            public String providerErrorDetail;

            public ProviderError(String providerErrorCode, String providerErrorDetail) {
                this.providerErrorCode = providerErrorCode;
                this.providerErrorDetail = providerErrorDetail;
            }

            public ProviderError() {
            }

            public static ErrorInfo.ErrorDetail.ProviderError.ProviderErrorBuilder builder() {
                return new ErrorInfo.ErrorDetail.ProviderError.ProviderErrorBuilder();
            }

            public String toString() {
                return "{\"providerErrorCode\":\"" + this.providerErrorCode + "\", \"providerErrorDetail\":\"" + this.providerErrorDetail + "\"}";
            }

            public String getProviderErrorCode() {
                return this.providerErrorCode;
            }

            public void setProviderErrorCode(String providerErrorCode) {
                this.providerErrorCode = providerErrorCode;
            }

            public String getProviderErrorDetail() {
                return this.providerErrorDetail;
            }

            public void setProviderErrorDetail(String providerErrorDetail) {
                this.providerErrorDetail = providerErrorDetail;
            }

            public static class ProviderErrorBuilder {
                private String providerErrorCode;
                private String providerErrorDetail;

                ProviderErrorBuilder() {
                }

                public ErrorInfo.ErrorDetail.ProviderError.ProviderErrorBuilder providerErrorCode(String providerErrorCode) {
                    this.providerErrorCode = providerErrorCode;
                    return this;
                }

                public ErrorInfo.ErrorDetail.ProviderError.ProviderErrorBuilder providerErrorDetail(String providerErrorDetail) {
                    this.providerErrorDetail = providerErrorDetail;
                    return this;
                }

                public ErrorInfo.ErrorDetail.ProviderError build() {
                    return new ErrorInfo.ErrorDetail.ProviderError(this.providerErrorCode, this.providerErrorDetail);
                }

                public String toString() {
                    return "ErrorInfo.ErrorDetail.ProviderError.ProviderErrorBuilder(providerErrorCode=" + this.providerErrorCode + ", providerErrorDetail=" + this.providerErrorDetail + ")";
                }
            }
        }
    }
}

