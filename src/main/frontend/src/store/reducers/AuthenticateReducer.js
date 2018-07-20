import { handleActions } from 'redux-actions';
import { RECEIVE_AUTHENTICATION } from '../actions/AuthenticateActions';

export const initialState = {
  response: false
};

const actionHandlers = {
  [RECEIVE_AUTHENTICATION]: (state, action) => ({ response: action.payload })
};
const reducer = handleActions(actionHandlers, initialState);

export default reducer;
