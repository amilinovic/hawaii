import { handleActions } from 'redux-actions';
import { receiveAllowance } from '../actions/allowanceActions';
import { navigateOut } from '../actions/navigateActions';

export const initialState = {};

const actionHandlers = {
  [receiveAllowance]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
