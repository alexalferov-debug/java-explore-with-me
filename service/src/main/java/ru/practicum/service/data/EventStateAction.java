package ru.practicum.service.data;

public enum EventStateAction {
    CANCEL_REVIEW,
    PUBLISH_EVENT;

    public static EventState getByAction(final EventStateAction action) {
        if (action == CANCEL_REVIEW){
            return EventState.CANCELED;
        }
        if (action == PUBLISH_EVENT){
            return EventState.PUBLISHED;
        }
        return null;
    }
}
