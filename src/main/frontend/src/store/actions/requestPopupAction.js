import { createAction } from 'redux-actions';

export const OPEN_REQUEST_POPUP = 'OPEN_REQUEST_POPUP';
export const CLOSE_REQUEST_POPUP = 'CLOSE_REQUEST_POPUP';

export const openRequestPopup = createAction(OPEN_REQUEST_POPUP);
export const closeRequestPopup = createAction(CLOSE_REQUEST_POPUP);
