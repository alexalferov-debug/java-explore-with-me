package ru.practicum.service.data;

public enum EventStateAction {
    CANCEL_REVIEW,
    REJECT_EVENT,
    SEND_TO_REVIEW,
    PUBLISH_EVENT;

    public static EventState getByAction(final EventStateAction action) {
        if (action == CANCEL_REVIEW || action == REJECT_EVENT) {
            return EventState.CANCELED;
        }
        if (action == PUBLISH_EVENT) {
            return EventState.PUBLISHED;
        }
        if (action == SEND_TO_REVIEW) {
            return EventState.PENDING;
        }
        return null;
    }
}
