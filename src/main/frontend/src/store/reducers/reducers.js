import { combineReducers } from 'redux';
import authorization from './getTokenReducer';
import team from './teamReducer';
import teams from './teamsReducer';
import user from './userReducer';
import users from './usersReducer';

export default combineReducers({
  authorization,
  teams,
  team,
  user,
  users
});
