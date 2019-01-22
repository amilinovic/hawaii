import { createAction } from 'redux-actions';

export const OPEN_REQUEST_POPUP = 'OPEN_REQUEST_POPUP';
export const CLOSE_REQUEST_POPUP = 'CLOSE_REQUEST_POPUP';
export const SELECT_REQUEST_TYPE = 'SELECT_REQUEST_TYPE';

export const openRequestPopup = createAction(OPEN_REQUEST_POPUP);
export const closeRequestPopup = createAction(CLOSE_REQUEST_POPUP);
export const selectRequestType = createAction(SELECT_REQUEST_TYPE);
