import { handleActions } from 'redux-actions';
import { navigateOut } from '../actions/navigateActions';
import { receiveUsers } from '../actions/usersActions';

export const initialState = null;

const actionHandlers = {
  [receiveUsers]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
