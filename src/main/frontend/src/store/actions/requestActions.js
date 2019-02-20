import { createAction } from 'redux-actions';

export const SELECT_REQUEST_TYPE = 'SELECT_REQUEST_TYPE';
export const SELECT_ABSENCE_TYPE = 'SELECT_ABSENCE_TYPE';
export const SELECT_START_DATE = 'SELECT_START_DATE';
export const SELECT_END_DATE = 'SELECT_END_DATE';

export const selectRequestType = createAction(SELECT_REQUEST_TYPE);
export const selectAbsenceType = createAction(SELECT_ABSENCE_TYPE);
export const selectStartDate = createAction(SELECT_START_DATE);
export const selectEndDate = createAction(SELECT_END_DATE);
