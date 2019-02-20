import { handleActions } from 'redux-actions';
import { navigateOut } from '../actions/navigateActions';
import { receiveTeam } from '../actions/teamActions';

export const initialState = null;

const actionHandlers = {
  [receiveTeam]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
