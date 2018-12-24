import { createAction } from 'redux-actions';

export const INIT_DATE = 'INIT_DATE';
export const INCREMENT_YEAR = 'INCREMENT_YEAR';
export const DECREMENT_YEAR = 'DECREMENT_YEAR';

export const initDate = createAction(INIT_DATE);
export const incrementYear = createAction(INCREMENT_YEAR);
export const decrementYear = createAction(DECREMENT_YEAR);
