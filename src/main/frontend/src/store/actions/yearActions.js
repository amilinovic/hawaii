import { createAction } from 'redux-actions';

export const REQUEST_YEAR = 'REQUEST_YEAR';
export const RECEIVE_YEAR = 'RECEIVE_YEAR';
export const RECEIVE_YEAR_ERROR = 'RECEIVE_YEAR_ERROR';

export const requestYear = createAction(REQUEST_YEAR);
export const receiveYear = createAction(RECEIVE_YEAR);
export const errorReceivingYear = createAction(RECEIVE_YEAR_ERROR);

export const REMOVE_YEAR = 'REMOVE_YEAR';
export const REMOVE_YEAR_SUCCESS = 'REMOVE_YEAR_SUCCESS';
export const REMOVE_YEAR_ERROR = 'REMOVE_YEAR_ERROR';

export const removeYear = createAction(REMOVE_YEAR);
export const removeYearSuccess = createAction(REMOVE_YEAR_SUCCESS);
export const errorRemovingYear = createAction(REMOVE_YEAR_ERROR);

export const UPDATE_YEAR = 'UPDATE_YEAR';
export const UPDATE_YEAR_SUCCESSFUL = 'UPDATE_YEAR_SUCCESSFUL';
export const UPDATE_YEAR_ERROR = 'UPDATE_YEAR_ERROR';

export const updateYear = createAction(UPDATE_YEAR);
export const updateYearSuccessful = createAction(UPDATE_YEAR_SUCCESSFUL);
export const updateYearError = createAction(UPDATE_YEAR_ERROR);

export const CREATE_YEAR = 'CREATE_YEAR';
export const CREATE_YEAR_SUCCESSFUL = 'CREATE_YEAR_SUCCESSFUL';
export const CREATE_YEAR_ERROR = 'CREATE_YEAR_ERROR';

export const createYear = createAction(CREATE_YEAR);
export const createYearSuccessful = createAction(CREATE_YEAR_SUCCESSFUL);
export const createYearError = createAction(CREATE_YEAR_ERROR);
