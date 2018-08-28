import { handleActions } from 'redux-actions';
import { receiveTeams } from '../actions/teamsActions';

export const initialState = [];

const actionHandlers = {
  [receiveTeams]: (state, action) => [action.payload]
};

const reducer = handleActions(actionHandlers, initialState);

export default reducer;
