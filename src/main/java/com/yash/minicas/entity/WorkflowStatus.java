package com.yash.minicas.entity;

public enum WorkflowStatus {
    NEW,
    PENDING_MODIFICATION,
    PENDING_DELETION,
    AUTHORIZED,
    REJECTED_NEW,
    REJECTED_MODIFICATION,
    REJECTED_DELETION
}
