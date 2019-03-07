import { handleActions } from 'redux-actions';
import { navigateOut } from '../actions/navigateActions';
import { receiveTeams } from '../actions/teamsActions';

export const initialState = null;

const actionHandlers = {
  [receiveTeams]: (state, action) => action.payload,
  [navigateOut]: () => null
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
