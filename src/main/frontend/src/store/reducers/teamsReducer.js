import { handleActions } from 'redux-actions';
import { receiveTeams } from '../actions/teamsActions';

export const initialState = {
  teams: null
};

const actionHandlers = {
  [receiveTeams]: (state, action) => ({ ...action.payload })
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
