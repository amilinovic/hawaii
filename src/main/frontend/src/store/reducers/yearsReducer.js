import { handleActions } from 'redux-actions';
import { navigateOut } from '../actions/navigateActions';
import { receiveYears } from '../actions/yearsActions';

export const initialState = null;

const actionHandlers = {
  [receiveYears]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
