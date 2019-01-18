import { combineReducers } from 'redux';

import employee from './randomUserApiReducer';
import authorization from './getTokenReducer';
import teams from './teamsReducer';
import user from './userReducer';

export default combineReducers({
  authorization,
  employee,
  teams,
  user
});
