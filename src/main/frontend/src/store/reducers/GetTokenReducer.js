import { handleActions } from 'redux-actions';
import { RECEIVE_TOKEN } from '../actions/GetTokenActions';

export const initialState = {};

const actionHandlers = {
  [RECEIVE_TOKEN]: (state, action) => action.payload
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
