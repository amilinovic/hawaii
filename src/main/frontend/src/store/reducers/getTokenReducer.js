import { handleActions } from 'redux-actions';
import { clearAuthorization } from '../actions/authorizationActions';
import { receiveToken } from '../actions/getTokenActions';

export const initialState = {};

const actionHandlers = {
  [receiveToken]: (state, action) => action.payload,
  [clearAuthorization]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
