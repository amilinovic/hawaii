import { handleActions } from 'redux-actions';
import { receiveUser } from '../actions/userActions';

export const initialState = {};

const actionHandlers = {
  [receiveUser]: (state, action) => action.payload
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
