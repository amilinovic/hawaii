import { createAction } from 'redux-actions';

export const CLOSE_MODAL = 'CLOSE_MODAL';
export const RESET_MODAL_STATE = 'RESET_MODAL_STATE';

export const closeModal = createAction(CLOSE_MODAL);
export const resetModalState = createAction(RESET_MODAL_STATE);
