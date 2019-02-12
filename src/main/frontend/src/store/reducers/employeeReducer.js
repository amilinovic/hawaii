import { handleActions } from 'redux-actions';

export const initialState = [];

const actionHandlers = {};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
