import { handleActions } from 'redux-actions';
import { RECEIVE_TOKEN } from '../actions/GetTokenActions';

export const initialState = {
  response: false
};

const actionHandlers = {
  [RECEIVE_TOKEN]: (state, action) => ({ response: action.payload })
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
