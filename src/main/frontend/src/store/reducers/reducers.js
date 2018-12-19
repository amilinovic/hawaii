import { combineReducers } from 'redux';

import authorization from './getTokenReducer';
import teams from './teamsReducer';
import user from './userReducer';

export default combineReducers({
  authorization,
  teams,
  user
});
