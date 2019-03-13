import { handleActions } from 'redux-actions';
import { navigateOut } from '../actions/navigateActions';
import { receiveYear } from '../actions/yearActions';

export const initialState = null;

const actionHandlers = {
  [receiveYear]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
