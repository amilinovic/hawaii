import { handleActions } from 'redux-actions';
import { RECEIVE_AUTHORIZATION } from '../actions/AuthorizationActions';

export const initialState = {
  response: null
};

const actionHandlers = {
  [RECEIVE_AUTHORIZATION]: (state, action) => ({ response: action.payload })
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
